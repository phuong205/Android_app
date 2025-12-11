package com.example.Appgiaythethao_Phuong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.adapter.GiohangAdapter;

import java.text.DecimalFormat;

/**
 * Lớp GioHangActivity: Hiển thị danh sách sản phẩm trong giỏ hàng,
 * tính tổng tiền, và xử lý các hành động tiếp tục mua hoặc thanh toán.
 */
public class GioHangActivity extends AppCompatActivity {

    // Khai báo Views và Adapter
    ListView listViewGioHang;
    TextView txtTongTien;
    Button btnThanhToan, btnTiepTucMua;
    Toolbar toolbarGioHang;
    GiohangAdapter adapterGioHang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);

        AnhXa();
        ActionToolbar();

        TinhTongTien(); // Tính tổng tiền ban đầu
        KiemTraDuLieu(); // Kiểm tra trạng thái rỗng của giỏ hàng
        EventButton(); // Thiết lập sự kiện cho các nút
    }

    /**
     * Phương thức AnhXa(): Liên kết Views với ID trong layout và khởi tạo Adapter.
     */
    private void AnhXa() {
        listViewGioHang = findViewById(R.id.listViewGioHang);
        txtTongTien = findViewById(R.id.textViewTongTien);
        btnThanhToan = findViewById(R.id.buttonThanhToan);
        btnTiepTucMua = findViewById(R.id.buttonTiepTucMua);
        toolbarGioHang = findViewById(R.id.toolbarGioHang);

        // Khởi tạo Adapter với danh sách giỏ hàng tĩnh từ MainActivity
        adapterGioHang = new GiohangAdapter(GioHangActivity.this, MainActivity.manggiohang);
        listViewGioHang.setAdapter(adapterGioHang);
    }

    /**
     * Phương thức ActionToolbar(): Thiết lập Toolbar và nút Quay lại.
     */
    private void ActionToolbar() {
        setSupportActionBar(toolbarGioHang);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Giỏ Hàng Của Bạn");

            toolbarGioHang.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Đóng Activity giỏ hàng
                }
            });
        }
    }

    /**
     * Phương thức KiemTraDuLieu(): Kiểm tra giỏ hàng rỗng hay không và điều chỉnh giao diện (hiện/ẩn nút Thanh toán).
     */
    private void KiemTraDuLieu() {

        if (MainActivity.manggiohang == null || MainActivity.manggiohang.size() <= 0) {
            adapterGioHang.notifyDataSetChanged(); // Cập nhật ListView (hiển thị trống)
            txtTongTien.setText("Tổng tiền: 0 Đ");
            Toast.makeText(this, "Giỏ hàng của bạn đang trống!", Toast.LENGTH_LONG).show();
            btnThanhToan.setVisibility(View.INVISIBLE); // Ẩn nút thanh toán
        } else {
            adapterGioHang.notifyDataSetChanged(); // Cập nhật dữ liệu
            btnThanhToan.setVisibility(View.VISIBLE); // Hiện nút thanh toán
        }
    }

    /**
     * Phương thức TinhTongTien(): Tính toán tổng giá trị của tất cả sản phẩm trong giỏ hàng.
     */
    public void TinhTongTien() {
        long tongTien = 0;

        if (MainActivity.manggiohang != null) {
            for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
                // Công thức tính tổng: Tổng tiền += (Giá sản phẩm * Số lượng)
                tongTien += (MainActivity.manggiohang.get(i).getGiasp() * MainActivity.manggiohang.get(i).getSoluong());
            }
        }

        // Định dạng tiền tệ (ví dụ: 1000000 -> 1,000,000)
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        if (txtTongTien != null) {
            txtTongTien.setText("Tổng tiền: " + decimalFormat.format(tongTien) + " Đ");
        }
    }

    /**
     * Phương thức onResume(): Đảm bảo dữ liệu được cập nhật mỗi khi quay lại Activity (sau khi sửa giỏ hàng).
     */
    @Override
    protected void onResume() {
        super.onResume();
        TinhTongTien(); // Tính lại tổng tiền
        KiemTraDuLieu(); // Kiểm tra lại trạng thái giỏ hàng
    }

    /**
     * Phương thức EventButton(): Thiết lập sự kiện cho nút Thanh toán và Tiếp tục mua.
     */
    private void EventButton() {

        // 1. Nút Tiếp tục mua: Chuyển về màn hình chính
        btnTiepTucMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GioHangActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Đóng Activity giỏ hàng
            }
        });

        // 2. Nút Thanh toán
        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.manggiohang != null && MainActivity.manggiohang.size() > 0) {
                    // Chuyển sang màn hình nhập thông tin khách hàng
                    Intent intent = new Intent(GioHangActivity.this, ThongTinKhachHangActivity.class);
                    startActivity(intent);
                    finish(); // Đóng Activity giỏ hàng
                } else {
                    Toast.makeText(GioHangActivity.this, "Giỏ hàng trống, vui lòng thêm sản phẩm!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}