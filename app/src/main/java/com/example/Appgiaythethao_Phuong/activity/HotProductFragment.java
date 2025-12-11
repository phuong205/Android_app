package com.example.Appgiaythethao_Phuong.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.adapter.SanphamAdapter;
import com.example.Appgiaythethao_Phuong.model.Sanpham;
import com.example.Appgiaythethao_Phuong.ultil.CheckConnection;
import com.example.Appgiaythethao_Phuong.ultil.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Lớp HotProductFragment: Hiển thị danh sách các sản phẩm nổi bật
 * sử dụng RecyclerView với GridLayoutManager và tải dữ liệu từ API bằng Volley.
 */
public class HotProductFragment extends Fragment {

    private static final String LOG_TAG = "HotProductFragment";

    // Khai báo Views và Data
    private RecyclerView recyclerViewHotProduct;
    private ArrayList<Sanpham> mangsanpham;
    private SanphamAdapter sanphamAdapter;

    public HotProductFragment() {
        // Bắt buộc phải có constructor rỗng
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate (gắn) layout XML cho Fragment
        return inflater.inflate(R.layout.fragment_hot_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ view sau khi layout đã được tạo
        anhXa(view);

        // Khởi tạo và cấu hình Adapter, LayoutManager
        setupRecyclerView();

        // Kiểm tra kết nối mạng trước khi tải dữ liệu
        if (CheckConnection.haveNetworkConnection(requireContext())) {
            getDuLieuSPNoiBat();
        } else {
            Toast.makeText(getContext(), "Vui lòng kiểm tra lại kết nối mạng!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Phương thức anhXa(): Ánh xạ các thành phần giao diện.
     * @param view Root View của Fragment.
     */
    private void anhXa(View view) {
        // Phải dùng view.findViewById() trong Fragment
        recyclerViewHotProduct = view.findViewById(R.id.recyclerview2);
    }

    /**
     * Phương thức setupRecyclerView(): Khởi tạo danh sách, Adapter và LayoutManager.
     */
    private void setupRecyclerView() {
        mangsanpham = new ArrayList<>();

        // Sử dụng requireContext() an toàn hơn getContext()
        sanphamAdapter = new SanphamAdapter(requireContext(), mangsanpham);

        recyclerViewHotProduct.setHasFixedSize(true);
        // Thiết lập LayoutManager dạng lưới (Grid) với 2 cột
        recyclerViewHotProduct.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerViewHotProduct.setAdapter(sanphamAdapter);
    }

    /**
     * Phương thức getDuLieuSPNoiBat(): Tải dữ liệu sản phẩm nổi bật từ API bằng Volley.
     */
    private void getDuLieuSPNoiBat() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        // Khởi tạo yêu cầu mảng JSON
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.DuongDansanphamnoibat, response -> {
            // Listener: Xử lý phản hồi thành công
            if (response != null) {
                // Xóa dữ liệu cũ
                mangsanpham.clear();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        // Parse dữ liệu từ JSONObject và tạo đối tượng Sanpham
                        int id = jsonObject.getInt("id");
                        String tensanpham = jsonObject.getString("tensanpham");
                        int giasanpham = jsonObject.getInt("giasanpham");
                        String hinhanhsanpham = jsonObject.getString("hinhanhsanpham");
                        String motasanpham = jsonObject.getString("motasanpham");
                        int idsanpham = jsonObject.getInt("idsanpham");

                        // Thêm sản phẩm vào danh sách
                        mangsanpham.add(new Sanpham(id, tensanpham, giasanpham, hinhanhsanpham, motasanpham, idsanpham));

                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Lỗi khi parse JSON: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
                // Thông báo cho adapter cập nhật giao diện
                sanphamAdapter.notifyDataSetChanged();
            }
        }, error -> {
            // Listener: Xử lý lỗi Volley (kết nối hoặc server)
            Log.e(LOG_TAG, "Lỗi Volley: " + error.toString());
            Toast.makeText(getContext(), "Lỗi tải dữ liệu sản phẩm.", Toast.LENGTH_SHORT).show();
        });

        // Thêm yêu cầu vào hàng đợi Volley
        requestQueue.add(jsonArrayRequest);
    }
}