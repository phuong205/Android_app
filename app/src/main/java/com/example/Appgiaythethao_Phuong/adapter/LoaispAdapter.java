package com.example.Appgiaythethao_Phuong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.model.Loaisp;

import java.util.ArrayList;
import com.squareup.picasso.Picasso;

public class LoaispAdapter extends BaseAdapter {
    Context mycontext;
    int mylayout;
    ArrayList<Loaisp> mangloaisp;

    @Override
    public int getCount() {
        return mangloaisp.size();
    }

    public LoaispAdapter(Context mycontext, int mylayout, ArrayList<Loaisp> mangloaisp) {
        this.mycontext = mycontext;
        this.mylayout = mylayout;
        this.mangloaisp = mangloaisp;
    }
    @Override
    public Object getItem(int i) {
        return mangloaisp.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class ViewHolder
    {
        TextView txtvTenloai;
        ImageView imgHinhloai;
    };
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater=(LayoutInflater)
                    mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(mylayout,null);
            viewHolder.txtvTenloai= (TextView)convertView.findViewById(R.id.textviewloaisp);

            viewHolder.imgHinhloai=(ImageView) convertView.findViewById(R.id.imageviewloaisp);
            convertView.setTag(viewHolder);
        }else { viewHolder=(ViewHolder) convertView.getTag(); }

        Loaisp loaisp=mangloaisp.get(i);
        viewHolder.txtvTenloai.setText(loaisp.getTenloai());

        String imageUrl = loaisp.getHinhloai();

        // ⭐ SỬA LỖI: KIỂM TRA URL TRƯỚC KHI GỌI PICASSO.load() ⭐
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.vitamin)
                    .error(R.drawable.ic_launcher_background)
                    .into(viewHolder.imgHinhloai);
        } else {
            // Đặt hình ảnh mặc định (hoặc icon hệ thống) cho mục menu đặc biệt (Lịch sử Đơn hàng)
            viewHolder.imgHinhloai.setImageResource(android.R.drawable.ic_menu_recent_history);
        }

        return convertView;
    }
}