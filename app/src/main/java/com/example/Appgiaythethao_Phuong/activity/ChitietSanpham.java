package com.example.Appgiaythethao_Phuong.activity;



import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


// ... (Các imports khác)

import com.bumptech.glide.Glide; // Thư viện giúp tải và hiển thị hình ảnh từ URL
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.model.GioHang; // Model đại diện cho một mặt hàng trong giỏ hàng
import com.example.Appgiaythethao_Phuong.model.Sanpham; // Model đại diện cho sản phẩm
import java.text.DecimalFormat; // Dùng để định dạng giá tiền

/**
 * Lớp ChitietSanpham: Hiển thị thông tin chi tiết của một sản phẩm
 * và xử lý chức năng thêm sản phẩm đó vào Giỏ hàng.
 */
public class ChitietSanpham extends AppCompatActivity {

    // Khai báo các thành phần giao diện (Views) và đối tượng dữ liệu
    private ImageView mImageViewSanpham;
    private TextView mTxtTensp, mTxtGiasp, mTxtMotasp;
    private Button mBtnThemGioHang;
    private Button mBtnQuayLai;
    private Sanpham mSanpham; // Đối tượng sản phẩm đang được xem

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Liên kết Activity với file layout chitietsanpham.xml
        setContentView(R.layout.chitietsanpham);

        initViews();            // 1. Ánh xạ các Views
        getThongTinSanpham();   // 2. Lấy dữ liệu sản phẩm và hiển thị
        setupListeners();       // 3. Thiết lập các sự kiện click
    }

    /**
     * Phương thức initViews(): Ánh xạ các Views từ layout XML.
     */
    private void initViews() {
        mImageViewSanpham = findViewById(R.id.imageViewChitietSanpham);
        mTxtTensp = findViewById(R.id.textViewTensp);
        mTxtGiasp = findViewById(R.id.textViewGiasp);
        mTxtMotasp = findViewById(R.id.textViewMotasp);
        mBtnThemGioHang = findViewById(R.id.buttonThemGioHang);
        mBtnQuayLai = findViewById(R.id.buttonQuayLai);
    }

    /**
     * Phương thức getThongTinSanpham(): Nhận đối tượng Sanpham từ Intent và cập nhật giao diện.
     */
    private void getThongTinSanpham() {
        // Nhận đối tượng Sanpham (đã được Serialize) từ Intent
        mSanpham = (Sanpham) getIntent().getSerializableExtra("thongtinsanpham");

        if (mSanpham != null) {
            // Hiển thị Tên sản phẩm và Mô tả
            mTxtTensp.setText(mSanpham.getTensp());
            mTxtMotasp.setText(mSanpham.getMotasp());

            // Định dạng giá tiền và hiển thị
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            String formattedPrice = decimalFormat.format(mSanpham.getGiasp());
            mTxtGiasp.setText("Giá: " + formattedPrice + " Đ");

            // Tải và hiển thị hình ảnh từ URL bằng thư viện Glide
            Glide.with(this)
                    .load(mSanpham.getHinhanhsp())
                    .placeholder(R.drawable.ic_launcher_foreground) // Ảnh hiển thị tạm thời
                    .error(R.drawable.ic_launcher_background) // Ảnh hiển thị khi lỗi
                    .into(mImageViewSanpham);
        }
    }

    /**
     * Phương thức setupListeners(): Thiết lập sự kiện lắng nghe cho các nút bấm.
     */
    private void setupListeners() {

        // Sự kiện click cho nút Quay lại: đóng Activity hiện tại
        mBtnQuayLai.setOnClickListener(v -> finish());

        // Sự kiện click cho nút Thêm Giỏ hàng: gọi phương thức xử lý giỏ hàng
        mBtnThemGioHang.setOnClickListener(v -> themGioHang());
    }

    /**
     * Phương thức themGioHang(): Xử lý logic thêm sản phẩm vào danh sách giỏ hàng tĩnh.
     */
    private void themGioHang() {
        if (mSanpham == null) {
            Toast.makeText(this, "Không thể thêm sản phẩm rỗng.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean exists = false;

        // 1. Kiểm tra sản phẩm đã có trong giỏ hàng (MainActivity.manggiohang) chưa
        for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
            // So sánh dựa trên ID sản phẩm
            if (MainActivity.manggiohang.get(i).getIdsp() == mSanpham.getId()) {
                // Nếu đã tồn tại: Cập nhật số lượng (+1)
                int newQuantity = MainActivity.manggiohang.get(i).getSoluong() + 1;
                MainActivity.manggiohang.get(i).setSoluong(newQuantity);
                exists = true;
                break;
            }
        }

        // 2. Nếu chưa tồn tại: Thêm mới vào giỏ hàng
        if (!exists) {
            MainActivity.manggiohang.add(
                    new GioHang(
                            mSanpham.getId(),          // idsp
                            mSanpham.getTensp(),       // tensp
                            mSanpham.getGiasp(),       // giasp
                            mSanpham.getHinhanhsp(),   // hinhanhsp
                            1)                         // soluong ban đầu là 1
            );
        }

        // 3. Thông báo thành công và kết thúc màn hình chi tiết
        Toast.makeText(this, "Thêm sản phẩm thành công!", Toast.LENGTH_SHORT).show();
        finish();
    }
}