package com.example.Appgiaythethao_Phuong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError; // Lớp xử lý lỗi xác thực (khi gửi tham số POST)
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest; // Yêu cầu gửi dữ liệu và nhận phản hồi dạng chuỗi
import com.android.volley.toolbox.Volley; // Thư viện Volley để quản lý kết nối mạng
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.ultil.CheckConnection; // Lớp kiểm tra trạng thái kết nối Internet
import com.example.Appgiaythethao_Phuong.ultil.Server; // Chứa đường dẫn API (Server.DuongDanDangKy)

import org.json.JSONException;
import org.json.JSONObject; // Dùng để xử lý phản hồi JSON từ server

import java.util.HashMap;
import java.util.Map; // Dùng để tạo Map (HashMap) chứa các tham số gửi lên server

/**
 * Lớp DangKyActivity: Xử lý giao diện và logic đăng ký tài khoản mới.
 * Sử dụng thư viện Volley để gửi thông tin đăng ký lên Server.
 */
public class DangKyActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện
    private EditText edtUsername, edtPassword, edtTen, edtSdt;
    private Button btnDangKy, btnHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dangky);

        AnhXa(); // Ánh xạ Views
        EventButton(); // Thiết lập sự kiện click cho các nút
    }

    /**
     * Phương thức AnhXa(): Liên kết các biến Java với ID trong file layout.
     */
    private void AnhXa() {
        edtUsername = findViewById(R.id.editTextRegUsername);
        edtPassword = findViewById(R.id.editTextRegPassword);
        edtTen = findViewById(R.id.editTextRegTen);
        edtSdt = findViewById(R.id.editTextRegSdt);
        btnDangKy = findViewById(R.id.buttonRegDangKy);
        btnHuy = findViewById(R.id.buttonRegHuy);
    }

    /**
     * Phương thức EventButton(): Thiết lập xử lý sự kiện khi người dùng click vào các nút.
     */
    private void EventButton() {
        // 1. Xử lý sự kiện Đăng ký
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy dữ liệu từ trường nhập liệu và xóa khoảng trắng thừa
                final String username = edtUsername.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();
                final String ten = edtTen.getText().toString().trim();
                final String sdt = edtSdt.getText().toString().trim();

                // Kiểm tra ràng buộc dữ liệu (Validation)
                if (username.isEmpty() || password.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(DangKyActivity.this, "Vui lòng nhập đầy đủ thông tin.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra kết nối Internet trước khi gửi yêu cầu
                if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                    // Nếu có kết nối, tiến hành gửi yêu cầu đăng ký
                    XuLyDangKy(username, password, ten, sdt);
                } else {
                    Toast.makeText(DangKyActivity.this, "Không có kết nối Internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 2. Xử lý sự kiện Hủy: Đóng Activity đăng ký, quay về màn hình trước
        btnHuy.setOnClickListener(v -> finish());
    }

    /**
     * Phương thức XuLyDangKy(): Gửi yêu cầu đăng ký (POST request) đến Server sử dụng Volley.
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @param ten Tên người dùng
     * @param sdt Số điện thoại
     */
    private void XuLyDangKy(final String username, final String password, final String ten, final String sdt) {
        // Khởi tạo hàng đợi Volley
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Khởi tạo StringRequest cho phương thức POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.DuongDanDangKy,
                // Listener: Xử lý phản hồi thành công từ Server
                response -> {
                    Log.d("REGISTER_RESPONSE", response);
                    try {
                        // Phân tích phản hồi JSON từ Server
                        JSONObject jsonObject = new JSONObject(response.trim());
                        boolean success = jsonObject.getBoolean("success"); // Trạng thái đăng ký thành công/thất bại
                        String message = jsonObject.getString("message"); // Thông báo đi kèm

                        if (success) {
                            // Đăng ký thành công
                            Toast.makeText(DangKyActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
                            // Đóng màn hình đăng ký
                            finish();
                        } else {
                            // Đăng ký thất bại (ví dụ: username đã tồn tại)
                            Toast.makeText(DangKyActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e("REGISTER_ERROR", "Lỗi phân tích JSON: " + e.getMessage());
                        Toast.makeText(DangKyActivity.this, "Lỗi server: Phản hồi không hợp lệ.", Toast.LENGTH_LONG).show();
                    }
                },
                // Listener: Xử lý lỗi kết nối Volley
                error -> {
                    Log.e("VOLLEY_ERROR", "Lỗi kết nối API đăng ký: " + error.toString());
                    Toast.makeText(DangKyActivity.this, "Lỗi kết nối máy chủ.", Toast.LENGTH_SHORT).show();
                }) {

            // Ghi đè phương thức getParams() để đính kèm dữ liệu gửi đi (POST data)
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                // Đưa các tham số vào HashMap
                hashMap.put("username", username);
                hashMap.put("password", password);
                hashMap.put("ten", ten);
                hashMap.put("sodienthoai", sdt); // Tên tham số phải khớp với tên biến Server mong muốn nhận
                return hashMap;
            }
        };
        // Thêm yêu cầu vào hàng đợi Volley để nó được thực thi
        requestQueue.add(stringRequest);
    }
}