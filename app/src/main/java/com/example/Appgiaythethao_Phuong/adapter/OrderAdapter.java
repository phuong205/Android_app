package com.example.Appgiaythethao_Phuong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.model.Order;

import java.text.DecimalFormat; // ⭐ IMPORT BẮT BUỘC ⭐
import java.util.ArrayList;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Order> arrayOrder;

    // ⭐ KHAI BÁO BIẾN FORMAT TIỀN TỆ ⭐
    private DecimalFormat decimalFormat;

    public OrderAdapter(Context context, ArrayList<Order> arrayOrder) {
        this.context = context;
        this.arrayOrder = arrayOrder;
        // Khởi tạo DecimalFormat ngay trong constructor
        this.decimalFormat = new DecimalFormat("###,###,###");
    }

    @Override
    public int getCount() {
        return arrayOrder.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayOrder.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {
        public TextView tvOrderID, tvNgayDat, tvTongTien, tvPhuongThucTT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_order_history, null);

            viewHolder.tvOrderID = (TextView) convertView.findViewById(R.id.tvOrderID);
            viewHolder.tvNgayDat = (TextView) convertView.findViewById(R.id.tvNgayDat);
            viewHolder.tvTongTien = (TextView) convertView.findViewById(R.id.tvTongTien);
            viewHolder.tvPhuongThucTT = (TextView) convertView.findViewById(R.id.tvPhuongThucTT);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Order order = (Order) getItem(position);

        viewHolder.tvOrderID.setText("Mã ĐH: #" + order.getId());
        viewHolder.tvNgayDat.setText("Ngày đặt: " + order.getNgaydat());

        // ⭐ ĐÃ SỬA LỖI: Dùng decimalFormat trực tiếp để định dạng tiền tệ ⭐
        String tongTienFormatted = decimalFormat.format(order.getTongtien());
        viewHolder.tvTongTien.setText(tongTienFormatted + " Đ");

        String ptttText = order.getPhuongthuctt().equals("COD") ? "Thanh toán khi nhận" : "Chuyển khoản";
        viewHolder.tvPhuongThucTT.setText("PTTT: " + ptttText);

        return convertView;
    }
}