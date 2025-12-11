package com.example.Appgiaythethao_Phuong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.ultil.CheckConnection;
import com.example.Appgiaythethao_Phuong.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Lớp DangNhapActivity: Xử lý giao diện và logic đăng nhập người dùng.
 * Gửi yêu cầu xác thực tới Server bằng Volley.
 */
public class DangNhapActivity extends AppCompatActivity {

    // Khai báo Views
    private EditText edtUsername, edtPassword;
    private Button btnDangNhap, btnDangKy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangnhap);

        AnhXa();
        EventButton();
    }

    /**
     * Phương thức AnhXa(): Liên kết các Views với ID trong layout.
     */
    private void AnhXa() {
        edtUsername = findViewById(R.id.editTextUsername);
        edtPassword = findViewById(R.id.editTextPassword);
        btnDangNhap = findViewById(R.id.buttonDangNhap);
        btnDangKy = findViewById(R.id.buttonDangKy);
    }

    /**
     * Phương thức EventButton(): Thiết lập xử lý sự kiện click cho các nút.
     */
    private void EventButton() {

        // 1. Xử lý nút Đăng nhập
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = edtUsername.getText().toString().trim();
                final String password = edtPassword.getText().toString().trim();

                // Kiểm tra ràng buộc
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(DangNhapActivity.this, "Vui lòng nhập đủ tên đăng nhập và mật khẩu.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra kết nối mạng
                if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
                    XuLyDangNhap(username, password);
                } else {
                    Toast.makeText(DangNhapActivity.this, "Không có kết nối Internet.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // 2. Xử lý nút Đăng ký: Chuyển sang màn hình Đăng ký
        btnDangKy.setOnClickListener(v -> {
            Intent intent = new Intent(DangNhapActivity.this, DangKyActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Phương thức XuLyDangNhap(): Gửi yêu cầu đăng nhập (POST) tới Server bằng Volley.
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     */
    private void XuLyDangNhap(final String username, final String password) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        // Tạo yêu cầu POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.DuongDanDangNhap,
                // Listener: Xử lý phản hồi thành công
                response -> {
                    Log.d("LOGIN_RESPONSE", response);
                    try {

                        JSONObject jsonObject = new JSONObject(response.trim());
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {

                            int userId = jsonObject.getInt("user_id");

                            MainActivity.CURRENT_USER_ID = userId;


                            Toast.makeText(DangNhapActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();


                            // Chuyển về màn hình chính (MainActivity)
                            Intent intent = new Intent(DangNhapActivity.this, MainActivity.class);
                            // Xóa các Activity cũ, đặt MainActivity là Activity gốc mới
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish(); // Đóng màn hình đăng nhập

                        } else {
                            // Đăng nhập thất bại (sai thông tin)
                            Toast.makeText(DangNhapActivity.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e("LOGIN_ERROR", "Lỗi phân tích JSON: " + e.getMessage());
                        Toast.makeText(DangNhapActivity.this, "Lỗi server: Phản hồi không hợp lệ.", Toast.LENGTH_LONG).show();
                    }
                },
                // Listener: Xử lý lỗi kết nối Volley
                error -> {
                    Log.e("VOLLEY_ERROR", "Lỗi kết nối API đăng nhập: " + error.toString());
                    Toast.makeText(DangNhapActivity.this, "Lỗi kết nối máy chủ.", Toast.LENGTH_SHORT).show();
                }) {

            // Ghi đè getParams() để gửi tham số POST
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("username", username);
                hashMap.put("password", password);
                return hashMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}