package com.example.Appgiaythethao_Phuong.activity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.adapter.GiohangAdapter;
import com.example.Appgiaythethao_Phuong.model.GioHang; // Đảm bảo import model GioHang

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Lớp ChiTietDonHangActivity: Hiển thị chi tiết của một đơn hàng đã được đặt,
 * bao gồm thông tin tổng quan (ID đơn hàng, Tên khách hàng, Tổng tiền) và
 * danh sách các sản phẩm (chi tiết giỏ hàng) trong đơn.
 */
public class ChiTietDonHangActivity extends AppCompatActivity {

    // Khai báo các thành phần giao diện (Views)
    private Toolbar toolbar;
    private TextView txtOrderID, txtTenKhachHang, txtTongTienCuoi;
    private ListView listViewChiTiet;
    private GiohangAdapter adapterChiTiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Liên kết Activity với file layout
        setContentView(R.layout.activity_chitietdonhang);

        // Gọi các phương thức thiết lập
        AnhXa();
        ActionToolbar();
        NhanVaHienThiDuLieu();
    }

    /**
     * Phương thức AnhXa(): Ánh xạ các Views từ layout XML vào biến Java.
     */
    private void AnhXa() {
        toolbar = findViewById(R.id.toolbarChiTietDonHang);
        txtOrderID = findViewById(R.id.textViewOrderID);
        txtTenKhachHang = findViewById(R.id.textViewTenKhachHang);
        txtTongTienCuoi = findViewById(R.id.textViewTongTienCuoi);
        listViewChiTiet = findViewById(R.id.listViewChiTietDonHang);
    }

    /**
     * Phương thức ActionToolbar(): Thiết lập Toolbar làm ActionBar và xử lý sự kiện nút Quay lại.
     */
    private void ActionToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            // Bật nút Quay lại (mũi tên)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi Tiết Đơn Hàng");

            // Xử lý khi click vào nút Quay lại
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // Kết thúc Activity hiện tại, quay về màn hình trước
                }
            });
        }
    }

    /**
     * Phương thức NhanVaHienThiDuLieu(): Nhận dữ liệu từ Intent và hiển thị lên giao diện.
     */
    private void NhanVaHienThiDuLieu() {
        // 1. Nhận dữ liệu từ Intent (được gửi từ màn hình trước, ví dụ: Xác nhận đơn hàng)
        Intent intent = getIntent();
        int idDonHang = intent.getIntExtra("idDonHangMoi", 0); // Mã đơn hàng
        String tenKhachHang = intent.getStringExtra("tenKhachHang"); // Tên khách hàng

        // Nhận danh sách chi tiết sản phẩm (ArrayList<GioHang> phải là Serializable)
        ArrayList<GioHang> chiTietGH = (ArrayList<GioHang>) intent.getSerializableExtra("chiTietGioHang");

        if (chiTietGH != null && chiTietGH.size() > 0) {
            // 2. Thiết lập Adapter cho ListView chi tiết
            adapterChiTiet = new GiohangAdapter(this, chiTietGH);
            listViewChiTiet.setAdapter(adapterChiTiet);

            // 3. Tùy chỉnh chiều cao ListView để hiển thị đủ tất cả items (quan trọng nếu dùng trong ScrollView)
            setListViewHeightBasedOnChildren(listViewChiTiet);

            // 4. Hiển thị thông tin tổng quan
            txtOrderID.setText("Mã đơn hàng: #" + idDonHang);
            txtTenKhachHang.setText("Khách hàng: " + tenKhachHang);

            // 5. Tính toán và hiển thị tổng tiền cuối cùng
            long tongThanhToan = tinhTongThanhToan(chiTietGH);

            // Định dạng tiền tệ (ví dụ: 1000000 -> 1,000,000)
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            txtTongTienCuoi.setText("Tổng thanh toán: " + decimalFormat.format(tongThanhToan) + " Đ");
        } else {
            // Trường hợp không nhận được dữ liệu
            txtOrderID.setText("Không có dữ liệu đơn hàng.");
        }
    }

    /**
     * Phương thức tinhTongThanhToan(): Tính tổng tiền của đơn hàng.
     * @param gioHangs Danh sách các sản phẩm trong đơn hàng.
     * @return Tổng số tiền thanh toán (long).
     */
    private long tinhTongThanhToan(ArrayList<GioHang> gioHangs) {
        long tongTien = 0;
        for (GioHang item : gioHangs) {
            // Tổng tiền = (Giá sản phẩm * Số lượng) cộng dồn
            tongTien += (item.getGiasp() * item.getSoluong());
        }
        return tongTien;
    }

    /**
     * Phương thức setListViewHeightBasedOnChildren(): Thiết lập chiều cao cho ListView
     * dựa trên tổng chiều cao của tất cả các item.
     * Kỹ thuật này giúp ListView hiển thị đầy đủ trong một ScrollView.
     *
     * @param listView ListView cần điều chỉnh chiều cao.
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        GiohangAdapter listAdapter = (GiohangAdapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            // Đo chiều cao của từng item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            totalHeight += listItem.getMeasuredHeight();
        }

        // Lấy thông số layout hiện tại của ListView
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        // Đặt lại chiều cao: Tổng chiều cao của items + tổng chiều cao của các đường phân cách (dividers)
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // Áp dụng thông số mới và yêu cầu layout vẽ lại
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}