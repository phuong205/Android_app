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
 * Lớp NewProductFragment: Hiển thị danh sách các sản phẩm mới nhất
 * sử dụng RecyclerView với GridLayoutManager và tải dữ liệu từ API bằng Volley.
 */
public class NewProductFragment extends Fragment {

    private static final String LOG_TAG = "NewProductFragment";

    // Khai báo Views và Data
    private RecyclerView recyclerView;
    private ArrayList<Sanpham> mangsanpham; // Danh sách sản phẩm
    private SanphamAdapter sanphamAdapter; // Adapter cho RecyclerView

    public NewProductFragment() {
        // Bắt buộc phải có constructor rỗng
    }

    /**
     * Phương thức onCreateView: Được gọi để Fragment tạo và trả về giao diện người dùng.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Gắn layout XML (fragment_new_product) vào Fragment
        return inflater.inflate(R.layout.fragment_new_product, container, false);
    }

    /**
     * Phương thức onViewCreated: Được gọi ngay sau onCreateView, an toàn để tương tác với Views.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ View và cấu hình RecyclerView
        anhXa(view);
        setupRecyclerView();

        // Kiểm tra kết nối mạng trước khi lấy dữ liệu
        if (CheckConnection.haveNetworkConnection(requireContext())) {
            getDuLieuSPMoiNhat(); // Bắt đầu tải dữ liệu
        } else {
            Toast.makeText(getContext(), "Vui lòng kiểm tra lại kết nối mạng!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Phương thức anhXa(): Ánh xạ RecyclerView.
     * @param view Root View của Fragment.
     */
    private void anhXa(View view) {
        // Phải dùng view.findViewById() trong Fragment
        recyclerView = view.findViewById(R.id.recyclerview);
    }

    /**
     * Phương thức setupRecyclerView(): Khởi tạo danh sách, Adapter và LayoutManager.
     */
    private void setupRecyclerView() {
        mangsanpham = new ArrayList<>();

        // Khởi tạo Adapter
        sanphamAdapter = new SanphamAdapter(requireContext(), mangsanpham);

        recyclerView.setHasFixedSize(true); // Tối ưu hóa hiệu suất

        // Thiết lập LayoutManager dạng lưới (Grid) với 2 cột
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));

        recyclerView.setAdapter(sanphamAdapter);
    }

    /**
     * Phương thức getDuLieuSPMoiNhat(): Tải dữ liệu sản phẩm mới nhất từ API bằng Volley.
     */
    private void getDuLieuSPMoiNhat() {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        // Tạo yêu cầu JsonArrayRequest để lấy danh sách sản phẩm mới nhất
        // Đường dẫn API được lấy từ lớp Server
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.DuongDansanpham, response -> {
            // Listener: Xử lý phản hồi thành công từ Server
            if (response != null) {
                mangsanpham.clear(); // Xóa dữ liệu cũ để cập nhật dữ liệu mới

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        // Parse dữ liệu và tạo đối tượng Sanpham
                        int id = jsonObject.getInt("id");
                        String tensanpham = jsonObject.getString("tensanpham");
                        int giasanpham = jsonObject.getInt("giasanpham");
                        String hinhanhsanpham = jsonObject.getString("hinhanhsanpham");
                        String motasanpham = jsonObject.getString("motasanpham");
                        int idsanpham = jsonObject.getInt("idsanpham"); // ID loại sản phẩm

                        // Thêm đối tượng Sanpham vào danh sách
                        mangsanpham.add(new Sanpham(id, tensanpham, giasanpham, hinhanhsanpham, motasanpham, idsanpham));
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Lỗi khi parse JSON: " + e.getMessage());
                    }
                }
                // Thông báo cho adapter rằng dữ liệu đã thay đổi, cập nhật giao diện
                sanphamAdapter.notifyDataSetChanged();
            }
        }, error -> {
            // Listener: Xử lý lỗi Volley (kết nối hoặc server)
            Log.e(LOG_TAG, "Lỗi Volley: " + error.toString());
            Toast.makeText(getContext(), "Lỗi tải dữ liệu sản phẩm.", Toast.LENGTH_SHORT).show();
        });

        // Thêm yêu cầu vào hàng đợi Volley để thực thi
        requestQueue.add(jsonArrayRequest);
    }
}