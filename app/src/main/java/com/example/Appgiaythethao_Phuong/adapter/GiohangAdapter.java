package com.example.Appgiaythethao_Phuong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.activity.GioHangActivity;
import com.example.Appgiaythethao_Phuong.activity.MainActivity;
import com.example.Appgiaythethao_Phuong.model.GioHang;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class GiohangAdapter extends BaseAdapter {

    Context context;
    ArrayList<GioHang> arrayGioHang;

    public GiohangAdapter(Context context, ArrayList<GioHang> arrayGioHang) {
        this.context = context;
        this.arrayGioHang = arrayGioHang;
    }

    @Override
    public int getCount() {
        return arrayGioHang.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayGioHang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // ⭐ KHAI BÁO CLASS VIEWHOLDER ĐÚNG CÁCH (NÊN LÀ static) ⭐
    public static class ViewHolder {
        public TextView txtTen, txtGia, txtSoLuong, txtTongTungMon;
        public ImageView imgGioHang;
        public Button btnTang, btnGiam;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // Dòng này an toàn, chỉ cần đảm bảo R.layout.item_giohang tồn tại
            convertView = inflater.inflate(R.layout.item_giohang, null);

            // ⭐ ÁNH XẠ CÁC VIEW CẦN THIẾT ⭐
            viewHolder.txtTen = convertView.findViewById(R.id.textViewTenGioHang);
            viewHolder.txtGia = convertView.findViewById(R.id.textViewGiaGioHang);
            viewHolder.txtTongTungMon = convertView.findViewById(R.id.textViewTongTungMon);
            viewHolder.imgGioHang = convertView.findViewById(R.id.imageViewGioHang);
            viewHolder.txtSoLuong = convertView.findViewById(R.id.textViewSoLuong);
            viewHolder.btnTang = convertView.findViewById(R.id.buttonTang);
            viewHolder.btnGiam = convertView.findViewById(R.id.buttonGiam);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final GioHang gioHang = (GioHang) getItem(position); // Đặt final để dùng trong inner class
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

        // 1. Cài đặt dữ liệu
        viewHolder.txtTen.setText(gioHang.getTensp());
        viewHolder.txtGia.setText("Giá: " + decimalFormat.format(gioHang.getGiasp()) + " Đ");
        viewHolder.txtSoLuong.setText(String.valueOf(gioHang.getSoluong()));

        long tongTungMon = gioHang.getGiasp() * gioHang.getSoluong();
        viewHolder.txtTongTungMon.setText("Tổng: " + decimalFormat.format(tongTungMon) + " Đ");

        Glide.with(context)
                .load(gioHang.getHinhsp())
                .into(viewHolder.imgGioHang);

        // 2. XỬ LÝ SỰ KIỆN TĂNG/GIẢM ⭐

        viewHolder.btnTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tăng số lượng (SL)
                int newQuantity = gioHang.getSoluong() + 1;
                gioHang.setSoluong(newQuantity);

                // Cập nhật lại giao diện và tổng tiền
                notifyDataSetChanged();
                ((GioHangActivity) context).TinhTongTien(); // ⭐ GỌI HÀM CẬP NHẬT TỔNG TIỀN ⭐
            }
        });

        viewHolder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentQuantity = gioHang.getSoluong();

                if (currentQuantity > 1) {
                    // Giảm số lượng
                    int newQuantity = currentQuantity - 1;
                    gioHang.setSoluong(newQuantity);

                    notifyDataSetChanged();
                    ((GioHangActivity) context).TinhTongTien();
                } else if (currentQuantity == 1) {
                    // ⭐ LOGIC XỬ LÝ XÓA SẢN PHẨM (GIẢM TỪ 1 VỀ 0) ⭐
                    Toast.makeText(context, "Đã xóa " + gioHang.getTensp() + " khỏi giỏ hàng.", Toast.LENGTH_SHORT).show();

                    // Xóa khỏi danh sách toàn cục
                    MainActivity.manggiohang.remove(position);

                    // Cập nhật
                    notifyDataSetChanged();
                    ((GioHangActivity) context).TinhTongTien();

                    // (Tùy chọn) Nếu giỏ hàng trống, kiểm tra và đóng Activity
                    if (MainActivity.manggiohang.size() == 0) {
                        Toast.makeText(context, "Giỏ hàng trống!", Toast.LENGTH_LONG).show();
                        // Nếu cần đóng GioHangActivity, bạn cần truyền một tham chiếu Activity
                        // hoặc gọi hàm xử lý Activity từ GioHangActivity
                    }
                }
            }
        });

        return convertView;
    }
}