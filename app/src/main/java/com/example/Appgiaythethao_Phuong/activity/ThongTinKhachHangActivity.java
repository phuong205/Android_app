package com.example.Appgiaythethao_Phuong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.model.GioHang;
import com.example.Appgiaythethao_Phuong.ultil.CheckConnection;
import com.example.Appgiaythethao_Phuong.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Lớp ThongTinKhachHangActivity: Thu thập thông tin giao hàng và xử lý
 * việc gửi đơn hàng lên Server qua 2 bước API (tạo đơn hàng chính, và gửi chi tiết).
 */
public class ThongTinKhachHangActivity extends AppCompatActivity {

    private static final String LOG_TAG_DEBUG = "ORDER_DEBUG";
    private static final String LOG_TAG_ERROR = "VOLLEY_ERROR";

    EditText edtTen, edtSdt, edtEmail, edtDiaChi;
    Button btnXacNhan;
    Toolbar toolbarThongTin;
    RadioGroup radioGroupPayment;

    private String phuongThucTT = "COD"; // Giá trị mặc định (Thanh toán khi nhận hàng)
    private ArrayList<GioHang> chiTietDonHangDaDat; // Lưu giỏ hàng trước khi xóa để gửi chi tiết

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongtinkhachhang);

        anhXa();
        actionToolbar();
        setupPaymentSelection();
        eventButton();
    }

    /**
     * Phương thức anhXa(): Ánh xạ Views.
     */
    private void anhXa() {
        edtTen = findViewById(R.id.editTextTenKhachHang);
        edtSdt = findViewById(R.id.editTextSoDienThoai);
        edtEmail = findViewById(R.id.editTextEmail);
        edtDiaChi = findViewById(R.id.editTextDiaChi);
        btnXacNhan = findViewById(R.id.buttonXacNhan);
        toolbarThongTin = findViewById(R.id.toolbarThongTin);
        radioGroupPayment = findViewById(R.id.radioGroupPayment);
    }

    /**
     * Phương thức setupPaymentSelection(): Thiết lập lắng nghe cho RadioGroup Phương thức thanh toán.
     */
    private void setupPaymentSelection() {
        radioGroupPayment.check(R.id.radioCash); // Chọn mặc định là COD
        radioGroupPayment.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioCash) {
                phuongThucTT = "COD";
            } else if (checkedId == R.id.radioTransfer) {
                phuongThucTT = "CHUYEN_KHOAN";
            }
        });
    }

    /**
     * Phương thức actionToolbar(): Thiết lập Toolbar và nút Quay lại.
     */
    private void actionToolbar() {
        setSupportActionBar(toolbarThongTin);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Thông tin giao hàng");
            toolbarThongTin.setNavigationOnClickListener(v -> finish());
        }
    }

    /**
     * Phương thức eventButton(): Xử lý sự kiện nút Xác nhận (Validation và gọi API).
     */
    private void eventButton() {
        btnXacNhan.setOnClickListener(v -> {
            // 1. Lấy dữ liệu từ EditText
            final String ten = edtTen.getText().toString().trim();
            final String sdt = edtSdt.getText().toString().trim();
            final String email = edtEmail.getText().toString().trim();
            final String diachi = edtDiaChi.getText().toString().trim();

            // 2. Kiểm tra ràng buộc (Validation)
            if (ten.isEmpty() || sdt.isEmpty() || diachi.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Vui lòng nhập đầy đủ thông tin bắt buộc!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Kiểm tra kết nối mạng
            if (!CheckConnection.haveNetworkConnection(getApplicationContext())) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối mạng!", Toast.LENGTH_SHORT).show();
                return;
            }

            // 4. Lưu tạm giỏ hàng hiện tại trước khi gọi API (sẽ xóa sau khi đặt thành công)
            chiTietDonHangDaDat = new ArrayList<>(MainActivity.manggiohang);

            // 5. Bắt đầu quy trình đặt hàng: Gửi thông tin đơn hàng (bước 1)
            guiThongTinDonHang(ten, sdt, email, diachi);
        });
    }

    /**
     * Phương thức guiThongTinDonHang(): Gửi thông tin cơ bản của đơn hàng (Bước 1).
     * Server trả về ID đơn hàng vừa tạo.
     */
    private void guiThongTinDonHang(String ten, String sdt, String email, String diachi) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.DuongDanDonHang,
                response -> {
                    // Xử lý phản hồi từ Server
                    try {
                        JSONObject jsonObject = new JSONObject(response.trim());
                        if (jsonObject.has("idDonHang")) {
                            int idDonHang = jsonObject.getInt("idDonHang"); // Nhận ID đơn hàng vừa tạo
                            Log.d(LOG_TAG_DEBUG, "Tạo đơn thành công ID: " + idDonHang);
                            // Gọi bước 2: Gửi chi tiết đơn hàng
                            guiChiTietDonHang(idDonHang, ten);
                        } else {
                            // Trường hợp Server trả về lỗi
                            String error = jsonObject.optString("error", "Lỗi không xác định");
                            Toast.makeText(getApplicationContext(), "Lỗi Server: " + error, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e(LOG_TAG_DEBUG, "Lỗi JSON: " + response);
                        Toast.makeText(getApplicationContext(), "Lỗi dữ liệu trả về!", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Log.e(LOG_TAG_ERROR, "Volley Error: " + error.toString());
            Toast.makeText(getApplicationContext(), "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                // 1. Gửi thông tin khách hàng và phương thức thanh toán
                hashMap.put("tenkhachhang", ten);
                hashMap.put("sodienthoai", sdt);
                hashMap.put("email", email.isEmpty() ? "" : email);
                hashMap.put("phuongthuctt", phuongThucTT);
                hashMap.put("diachi", diachi);

                // 2. GỬI ID NGƯỜI DÙNG (Dùng cho tính năng lịch sử đơn hàng)
                hashMap.put("id_nguoidung", String.valueOf(MainActivity.CURRENT_USER_ID));

                // 3. TÍNH VÀ GỬI TỔNG TIỀN
                long tongTien = 0;
                for (GioHang gh : MainActivity.manggiohang) {
                    tongTien += (gh.getGiasp() * gh.getSoluong());
                }
                hashMap.put("tongtien", String.valueOf(tongTien)); // Gửi tổng tiền

                Log.d(LOG_TAG_DEBUG, "Đang gửi đơn: UserID=" + MainActivity.CURRENT_USER_ID + ", TongTien=" + tongTien);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * Phương thức guiChiTietDonHang(): Gửi danh sách sản phẩm (Bước 2).
     * @param idDonHang ID của đơn hàng vừa được tạo ở Bước 1.
     * @param tenKhachHang Tên khách hàng (để hiển thị lên màn hình chi tiết)
     */
    private void guiChiTietDonHang(final int idDonHang, final String tenKhachHang) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.DuongDanChitietdonhang,
                response -> {
                    // Xử lý phản hồi từ Server (thường là "1" nếu thành công)
                    if (response.trim().equals("1") || response.contains("success")) {

                        // Xóa giỏ hàng sau khi đặt hàng thành công
                        MainActivity.manggiohang.clear();
                        Toast.makeText(getApplicationContext(), "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();

                        // Chuyển sang màn hình Chi tiết đơn hàng vừa tạo
                        Intent intent = new Intent(ThongTinKhachHangActivity.this, ChiTietDonHangActivity.class);
                        intent.putExtra("idDonHangMoi", idDonHang); // ID đơn hàng
                        intent.putExtra("tenKhachHang", tenKhachHang); // Tên khách hàng
                        intent.putExtra("chiTietGioHang", chiTietDonHangDaDat); // Danh sách sản phẩm (Serializable)

                        // Xóa các Activity cũ, đặt ChiTietDonHangActivity là Activity mới
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Lỗi lưu chi tiết đơn hàng!", Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            Toast.makeText(getApplicationContext(), "Lỗi kết nối chi tiết!", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> hashMap = new HashMap<>();
                JSONArray jsonArray = new JSONArray();

                // Chuyển danh sách sản phẩm (chiTietDonHangDaDat) thành chuỗi JSON Array
                for (GioHang gh : chiTietDonHangDaDat) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("idsanpham", gh.getIdsp());
                        jsonObject.put("soluong", gh.getSoluong());
                        jsonObject.put("gia", gh.getGiasp());
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Gửi ID đơn hàng và chuỗi JSON chứa chi tiết sản phẩm
                hashMap.put("iddonhang", String.valueOf(idDonHang));
                hashMap.put("jsonchitiet", jsonArray.toString());

                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}