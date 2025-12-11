package com.example.Appgiaythethao_Phuong.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Lớp StartActivity: Màn hình chờ (Splash Screen) thực hiện kiểm tra trạng thái
 * đăng nhập của người dùng qua SharedPreferences và chuyển hướng.
 */
public class StartActivity extends AppCompatActivity {

    // Biến static này có thể không cần thiết trong lớp này nếu bạn chỉ dùng nó trong MainActivity
    public static int CURRENT_USER_ID = 0;

    // Khóa/Tên file dùng để lưu trữ dữ liệu trong bộ nhớ cục bộ (SharedPreferences)
    public static final String PREFS_NAME = "MyUserPrefs";
    // Khóa dùng để lưu và lấy ID người dùng
    public static final String USER_ID_KEY = "currentUserId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Tùy chọn: Bạn có thể đặt một layout cho màn hình chờ ở đây
        // setContentView(R.layout.activity_start);

        // Sử dụng Handler để trì hoãn việc chuyển màn hình, tạo hiệu ứng Splash Screen
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                kiemTraTrangThaiDangNhap();
            }
        }, 1000); // Trì hoãn 1000 mili giây (1 giây)
    }

    /**
     * Phương thức kiemTraTrangThaiDangNhap(): Đọc SharedPreferences và chuyển hướng.
     */
    private void kiemTraTrangThaiDangNhap() {
        // Lấy đối tượng SharedPreferences với tên file là PREFS_NAME
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        // Đọc giá trị ID người dùng, mặc định là 0 nếu khóa USER_ID_KEY chưa tồn tại
        int currentUserId = settings.getInt(USER_ID_KEY, 0);

        Intent intent;

        // Nếu ID > 0, người dùng được coi là đã đăng nhập
        if (currentUserId > 0) {
            // ⭐ ĐÃ ĐĂNG NHẬP: Thiết lập ID và chuyển đến Trang Chính ⭐

            // Thiết lập ID người dùng cho biến static trong MainActivity để dùng chung
            MainActivity.CURRENT_USER_ID = currentUserId;

            // Chuyển hướng đến màn hình chính
            intent = new Intent(StartActivity.this, MainActivity.class);
        } else {
            // ⭐ CHƯA ĐĂNG NHẬP: Chuyển đến màn hình Đăng nhập ⭐

            // Chuyển hướng đến màn hình đăng nhập
            intent = new Intent(StartActivity.this, DangNhapActivity.class);
        }

        startActivity(intent);
        // Đóng StartActivity để người dùng không thể nhấn nút Back quay lại màn hình chờ
        finish();
    }
}