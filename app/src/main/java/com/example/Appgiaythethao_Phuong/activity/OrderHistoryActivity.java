package com.example.Appgiaythethao_Phuong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.adapter.OrderAdapter;
import com.example.Appgiaythethao_Phuong.model.Order;
import com.example.Appgiaythethao_Phuong.ultil.CheckConnection;
import com.example.Appgiaythethao_Phuong.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Lớp OrderHistoryActivity: Hiển thị danh sách lịch sử đơn hàng của người dùng đã đăng nhập.
 */
public class OrderHistoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listViewOrders;
    private ArrayList<Order> mangDonHang;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // --- BƯỚC 1: KIỂM TRA ĐĂNG NHẬP ---
        // Nếu không có USER ID (chưa đăng nhập), chuyển hướng đến màn hình đăng nhập
        if (MainActivity.CURRENT_USER_ID <= 0) {
            Toast.makeText(getApplicationContext(), "Vui lòng đăng nhập để xem lịch sử.", Toast.LENGTH_LONG).show();
            Intent loginIntent = new Intent(this, DangNhapActivity.class);
            startActivity(loginIntent);
            finish(); // Kết thúc Activity này để ngăn người dùng quay lại
            return;
        }

        // --- BƯỚC 2: KHỞI TẠO VÀ TẢI DỮ LIỆU ---
        AnhXa();
        ActionToolbar();
        SetupListView();

        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            GetOrderHistory(); // Tải dữ liệu từ Server
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Vui lòng kiểm tra kết nối Internet!");
        }
    }

    /**
     * Phương thức AnhXa(): Liên kết Views với ID.
     */
    private void AnhXa() {
        toolbar = findViewById(R.id.toolbarOrderHistory);
        listViewOrders = findViewById(R.id.listViewOrderHistory);
    }

    /**
     * Phương thức SetupListView(): Khởi tạo danh sách dữ liệu và Adapter cho ListView.
     */
    private void SetupListView() {
        mangDonHang = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, mangDonHang);
        listViewOrders.setAdapter(orderAdapter);
    }

    /**
     * Phương thức ActionToolbar(): Thiết lập Toolbar và nút Quay lại.
     */
    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Lịch sử Đơn hàng");
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    /**
     * Phương thức GetOrderHistory(): Gửi yêu cầu JsonArrayRequest để lấy danh sách đơn hàng.
     */
    private void GetOrderHistory() {
        // 1. Lấy USER ID hiện tại (được lưu sau khi đăng nhập)
        int currentUserId = MainActivity.CURRENT_USER_ID;

        // 2. Lấy SỐ ĐIỆN THOẠI hiện tại (LƯU Ý: Dùng hardcode để test nếu biến static chưa có)
        // Nếu biến static chưa có, hãy tạm thời dùng dòng này để test:
        String currentPhone = "0353395733";
        // Nếu đã có biến static: String currentPhone = MainActivity.CURRENT_PHONE;

        // 3. Tạo đường dẫn API với tham số truyền qua URL (GET request)
        // Truyền cả user_id VÀ sodienthoai để đảm bảo tìm thấy đơn hàng trong mọi trường hợp
        String duongdan = Server.DuongDanLichSuDonHang + "?user_id=" + currentUserId + "&sodienthoai=" + currentPhone;

        Log.d("ORDER_FETCH", "Link API: " + duongdan);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(duongdan, new Response.Listener<JSONArray>() {
            // Listener: Xử lý phản hồi thành công từ Server
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length() > 0) {
                    mangDonHang.clear(); // Xóa dữ liệu cũ
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);

                            // Phân tích dữ liệu JSON và tạo đối tượng Order
                            int id = jsonObject.getInt("id");
                            String tenKhachHang = jsonObject.getString("tenkhachhang");
                            String sdt = jsonObject.getString("sodienthoai");
                            String ngayDat = jsonObject.getString("ngaydat");
                            // Dùng optLong/optString để tránh crash nếu trường không tồn tại
                            long tongTien = jsonObject.optLong("tongtien", 0);
                            String phuongThucTT = jsonObject.optString("phuongthuctt", "COD");

                            mangDonHang.add(new Order(id, tenKhachHang, sdt, ngayDat, tongTien, phuongThucTT));
                        } catch (JSONException e) {
                            Log.e("ORDER_HISTORY", "Lỗi JSON: " + e.getMessage());
                        }
                    }
                    orderAdapter.notifyDataSetChanged(); // Cập nhật ListView
                } else {
                    // Trường hợp mảng rỗng (không tìm thấy đơn hàng)
                    Toast.makeText(getApplicationContext(), "Bạn chưa có đơn hàng nào.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            // Listener: Xử lý lỗi Volley (kết nối hoặc server)
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY_ORDER", "Lỗi Volley: " + error.toString());
                Toast.makeText(getApplicationContext(), "Lỗi tải dữ liệu server.", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }
}