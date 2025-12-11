package com.example.Appgiaythethao_Phuong.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.adapter.SanphamAdapter;
import com.example.Appgiaythethao_Phuong.model.Sanpham;
import com.example.Appgiaythethao_Phuong.ultil.CheckConnection;
import com.example.Appgiaythethao_Phuong.ultil.Server; // Chứa đường dẫn API

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Lớp SanphamActivity: Hiển thị danh sách sản phẩm theo ID loại sản phẩm (Category)
 * và triển khai tính năng Load More/Pagination (tải thêm khi cuộn).
 */
public class SanphamActivity extends AppCompatActivity {

    Toolbar toolbarSanpham;
    RecyclerView recyclerViewSanpham;
    SanphamAdapter sanphamAdapter;
    ArrayList<Sanpham> mangSanpham;

    // Biến lưu trữ ID và Tên loại sản phẩm được truyền từ Activity trước
    int idsanpham = 0;
    String tenLoaiSanpham = "";

    // Biến cho Phân trang (Pagination)
    int page = 1;           // Số trang hiện tại đang tải (bắt đầu từ 1)
    boolean isLoading = false; // Cờ kiểm tra đang có yêu cầu tải nào đang chạy không
    boolean limitData = false; // Cờ kiểm tra đã hết dữ liệu trên server chưa (phân trang cuối)

    // Biến cho Scroll Listener
    GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanpham);

        AnhXa();            // 1. Ánh xạ Views và khởi tạo Adapter
        GetIdLoaiSanPham(); // 2. Lấy ID và Tên loại sản phẩm

        // 3. Kiểm tra kết nối mạng và tải dữ liệu
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            GetData(page); // Tải trang đầu tiên (page=1)
            LoadMoreData(); // Thiết lập sự kiện lắng nghe cuộn
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Bạn hãy kiểm tra kết nối Internet!");
            finish();
        }
    }

    /**
     * Phương thức AnhXa(): Ánh xạ Views và khởi tạo RecyclerView.
     */
    private void AnhXa() {
        toolbarSanpham = findViewById(R.id.toolbar_sanpham);
        recyclerViewSanpham = findViewById(R.id.recyclerview_sanpham);
        mangSanpham = new ArrayList<>();

        // Khởi tạo Adapter và gán cho RecyclerView
        sanphamAdapter = new SanphamAdapter(getApplicationContext(), mangSanpham);
        recyclerViewSanpham.setAdapter(sanphamAdapter);

        // RecyclerView cần LayoutManager. Sử dụng GridLayoutManager với 2 cột.
        layoutManager = new GridLayoutManager(this, 2);
        recyclerViewSanpham.setLayoutManager(layoutManager);
    }

    /**
     * Phương thức GetIdLoaiSanPham(): Lấy ID và Tên loại sản phẩm được gửi qua Intent.
     */
    private void GetIdLoaiSanPham() {
        idsanpham = getIntent().getIntExtra("idsanpham", -1);
        tenLoaiSanpham = getIntent().getStringExtra("tenloaisanpham");
        Log.d("GiaTriLoaiSanPham", "ID nhận được: " + idsanpham + ", Tên: " + tenLoaiSanpham);

        // Gọi thiết lập Toolbar sau khi đã có tên để đặt tiêu đề
        ActionToolbar(tenLoaiSanpham);
    }

    /**
     * Thiết lập cho Toolbar (nút back, tiêu đề)
     * @param title Tiêu đề Toolbar (thường là tên loại sản phẩm)
     */
    private void ActionToolbar(String title) {
        setSupportActionBar(toolbarSanpham);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getSupportActionBar() != null && title != null) {
            getSupportActionBar().setTitle(title);
        }

        toolbarSanpham.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity khi nhấn nút Back
            }
        });
    }

    /**
     * Phương thức GetData(): Gọi API để lấy dữ liệu sản phẩm theo ID và trang hiện tại.
     * @param currentPage Số trang cần tải (Page 1, 2, 3...)
     */
    private void GetData(int currentPage) {
        // Đặt cờ đang tải để ngăn Load More gọi liên tục
        isLoading = true;

        // Tạo đường dẫn API: Truyền tham số page và idsanpham (ID loại sản phẩm)
        String duongdan = Server.DuongdanCategory + "?page=" + currentPage + "&idsanpham=" + idsanpham;
        Log.d("API_CALL", "Tải trang " + currentPage + " cho ID " + idsanpham + ". Link: " + duongdan);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(duongdan, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null && response.length() > 0) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            // Phân tích dữ liệu JSON và tạo đối tượng Sanpham
                            int id = jsonObject.getInt("id");
                            String tensp = jsonObject.getString("tensanpham");
                            Integer giasp = jsonObject.getInt("giasanpham");
                            String hinhanhsp = jsonObject.getString("hinhanhsanpham");
                            String motasp = jsonObject.getString("motasanpham");
                            int idsp = jsonObject.getInt("idsanpham");

                            mangSanpham.add(new Sanpham(id, tensp, giasp, hinhanhsp, motasp, idsp));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // Tắt cờ đang tải và cập nhật adapter
                    isLoading = false;
                    sanphamAdapter.notifyDataSetChanged();
                } else {
                    // Trường hợp mảng JSON rỗng: Đã hết dữ liệu để tải
                    limitData = true;
                    isLoading = false;
                    if (currentPage == 1) {
                        // Trường hợp danh mục này chưa có sản phẩm nào
                        CheckConnection.ShowToast_Short(getApplicationContext(), "Chưa có sản phẩm cho danh mục này");
                    } else {
                        // Trường hợp Load More đến cuối
                        CheckConnection.ShowToast_Short(getApplicationContext(), "Đã hết sản phẩm để hiển thị");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading = false; // Tắt cờ đang tải khi có lỗi
                CheckConnection.ShowToast_Short(getApplicationContext(), "Lỗi tải dữ liệu: " + error.toString());
                Log.e("VOLLEY_ERROR", "Lỗi tải dữ liệu: " + error.toString());
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    /**
     * Phương thức LoadMoreData(): Thiết lập OnScrollListener để tự động Load thêm dữ liệu.
     */
    private void LoadMoreData() {
        recyclerViewSanpham.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                // Logic kiểm tra cần Load More:
                // 1. (!isLoading): Không có yêu cầu tải nào đang chạy
                // 2. (!limitData): Vẫn còn dữ liệu để tải (chưa đến trang cuối)
                // 3. (visibleItemCount + firstVisibleItemPosition) >= totalItemCount: Đã cuộn gần đến cuối
                // 4. totalItemCount >= 10: Đảm bảo đã tải ít nhất 1 trang đầy đủ
                if (!isLoading && !limitData && CheckConnection.haveNetworkConnection(getApplicationContext())) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 10) {

                        // Tăng số trang lên 1
                        page++;
                        // Gọi phương thức GetData để tải trang tiếp theo
                        GetData(page);
                    }
                }
            }
        });
    }
}