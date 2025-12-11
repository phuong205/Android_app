package com.example.Appgiaythethao_Phuong.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.activity.ChitietSanpham;
import com.example.Appgiaythethao_Phuong.model.Sanpham;
import com.example.Appgiaythethao_Phuong.ultil.CheckConnection;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanphamAdapter extends RecyclerView.Adapter<SanphamAdapter.ItemHolder> {

    private Context context;
    private ArrayList<Sanpham> sanphamArrayList;

    // Constructor nhận context và danh sách sản phẩm
    public SanphamAdapter(Context context, ArrayList<Sanpham> sanphamArrayList) {
        this.context = context;
        this.sanphamArrayList = sanphamArrayList;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item sản phẩm
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dongsanphammoinhat, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        // Lấy sản phẩm tại vị trí position
        Sanpham sanpham = sanphamArrayList.get(position);

        // Đặt tên sản phẩm lên TextView
        holder.txttensanpham.setText(sanpham.getTensp());

        // Định dạng giá tiền theo dạng 1,000,000
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgiasanpham.setText("Giá: " + decimalFormat.format(sanpham.getGiasp()) + " Đ");

        // Load hình ảnh sản phẩm dùng Glide
        Glide.with(context)
                .load(sanpham.getHinhanhsp())
                .placeholder(R.drawable.ic_launcher_background) // hình mặc định khi loading
                .error(R.drawable.newicon) // hình khi load lỗi
                .into(holder.imghinhsanpham);
    }

    @Override
    public int getItemCount() {
        return sanphamArrayList.size();
    }

    // ViewHolder giữ tham chiếu các view trong item layout
    public class ItemHolder extends RecyclerView.ViewHolder {

        public ImageView imghinhsanpham;
        public TextView txttensanpham, txtgiasanpham;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);

            imghinhsanpham = itemView.findViewById(R.id.imageviewsanpham);
            txttensanpham = itemView.findViewById(R.id.textviewtensanpham);
            txtgiasanpham = itemView.findViewById(R.id.textviewgiasanpham);

            // Xử lý click item mở chi tiết sản phẩm
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Sanpham sanpham = sanphamArrayList.get(position);

                    Intent intent = new Intent(context, ChitietSanpham.class);
                    intent.putExtra("thongtinsanpham", sanpham);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    CheckConnection.ShowToast_Short(context, sanpham.getTensp());

                    context.startActivity(intent);
                }
            });
        }
    }
}
