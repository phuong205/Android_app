package com.example.Appgiaythethao_Phuong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.Appgiaythethao_Phuong.R;
import com.example.Appgiaythethao_Phuong.adapter.LoaispAdapter;
import com.example.Appgiaythethao_Phuong.model.GioHang;
import com.example.Appgiaythethao_Phuong.model.Loaisp;
import com.example.Appgiaythethao_Phuong.ultil.CheckConnection;
import com.example.Appgiaythethao_Phuong.ultil.Server;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Lớp MainActivity: Hoạt động như màn hình chính và bộ điều hướng của ứng dụng.
 * Quản lý Toolbar, ViewFlipper, Navigation Drawer, TabLayout (chứa Fragments) và Giỏ hàng toàn cục.
 */
public class MainActivity extends AppCompatActivity {

    // --- Global State (Trạng thái toàn cục) ---
    // Lưu thông tin người dùng đang hoạt động (static để truy cập từ mọi nơi)
    public static String CURRENT_PHONE = "";
    public static int CURRENT_USER_ID = 0;
    // Danh sách Giỏ hàng tĩnh (static) dùng chung cho toàn bộ ứng dụng
    public static ArrayList<GioHang> manggiohang;

    // --- Views ---
    private Toolbar toolbar;
    private ViewFlipper viewFlipper; // Component để chạy banner quảng cáo
    private TabLayout tabLayout; // Thanh chứa tab (Mới nhất, Nổi bật)
    private DrawerLayout drawerLayout; // Layout chứa Navigation Drawer
    private NavigationView navigationView; // View chứa nội dung của menu bên hông (ít dùng trực tiếp)
    private ListView categoryListView; // ListView hiển thị danh mục trong Drawer
    private TextView cartItemCountTextView; // Badge (số lượng) trên icon giỏ hàng

    // --- Data & Adapters ---
    private LoaispAdapter categoryAdapter;
    private ArrayList<Loaisp> categoryList; // Danh sách các loại sản phẩm
    private RequestQueue requestQueue; // Hàng đợi Volley để xử lý yêu cầu mạng

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();           // 1. Ánh xạ Views và khởi tạo dữ liệu Giỏ hàng
        setupToolbar();         // 2. Thiết lập Toolbar và nút mở Drawer
        setupViewFlipper();     // 3. Thiết lập banner quảng cáo
        setupNavigationDrawer();// 4. Thiết lập sự kiện click cho các mục trong Drawer
        setupTabs();            // 5. Thiết lập sự kiện chuyển Fragment khi chọn Tab

        fetchCategoryData();    // 6. Tải dữ liệu danh mục sản phẩm từ Server

        if (savedInstanceState == null) {
            // Lần đầu chạy Activity, mặc định load Fragment "Sản phẩm Mới nhất"
            loadFragment(new NewProductFragment());
        }
    }

    /**
     * Phương thức onResume: Được gọi khi Activity quay lại trạng thái hoạt động.
     * Đảm bảo giỏ hàng được cập nhật sau khi người dùng thêm/xóa sản phẩm.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge(); // Cập nhật số lượng sản phẩm trên badge giỏ hàng
    }

    /**
     * Phương thức initialize(): Ánh xạ Views và khởi tạo các đối tượng dữ liệu.
     */
    private void initialize() {
        // Ánh xạ Views từ layout
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewflipper);
        tabLayout = findViewById(R.id.tabLayout);
        navigationView = findViewById(R.id.navigationview);
        categoryListView = findViewById(R.id.listviewmanhinhchinh);
        drawerLayout = findViewById(R.id.drawerlayout);

        // Khởi tạo các đối tượng cần thiết
        requestQueue = Volley.newRequestQueue(this);
        categoryList = new ArrayList<>();

        // Khởi tạo Adapter cho ListView Danh mục (Navigation Drawer)
        categoryAdapter = new LoaispAdapter(this, R.layout.item_loaisp, categoryList);
        categoryListView.setAdapter(categoryAdapter);

        // Đảm bảo danh sách giỏ hàng được khởi tạo lần đầu (nếu chưa có)
        if (manggiohang == null) {
            manggiohang = new ArrayList<>();
        }
    }

    /**
     * Phương thức setupToolbar(): Thiết lập Toolbar làm Action Bar và nút mở Drawer.
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        // Bật nút Home (thường là icon menu)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Đặt icon 3 gạch cho nút Home
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size);
        // Xử lý sự kiện click vào icon menu: Mở Navigation Drawer từ trái sang
        toolbar.setNavigationOnClickListener(view -> drawerLayout.openDrawer(GravityCompat.START));
    }

    /**
     * Phương thức setupViewFlipper(): Thiết lập banner quảng cáo tự động trượt.
     */
    private void setupViewFlipper() {
        // Danh sách các URL hình ảnh quảng cáo
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://clevercollection.vn/cdn/shop/files/BANNER_WEB_1280X496-01_png.jpg?v=1763087710&width=1400");
        mangquangcao.add("https://clevercollection.vn/cdn/shop/files/DISNEY_NGANG-02.png?v=1763179305&width=1400");
        mangquangcao.add("https://bizweb.dktcdn.net/100/347/092/themes/708609/assets/slider_1.jpg?1760866522146");
        // ... (Thêm các URL khác)

        for (String url : mangquangcao) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // Sử dụng thư viện Glide để tải ảnh từ URL
            Glide.with(this).load(url).into(imageView);
            viewFlipper.addView(imageView); // Thêm ImageView vào ViewFlipper
        }

        viewFlipper.setFlipInterval(5000); // Tự động chuyển đổi sau 5 giây
        viewFlipper.setAutoStart(true);

        // Thiết lập hiệu ứng chuyển cảnh
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation slideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_right);
        viewFlipper.setInAnimation(slideIn);
        viewFlipper.setOutAnimation(slideOut);
    }

    /**
     * Phương thức setupNavigationDrawer(): Thiết lập sự kiện click cho ListView Danh mục.
     */
    private void setupNavigationDrawer() {
        categoryListView.setOnItemClickListener((parent, view, position, id) -> {
            Loaisp selectedCategory = categoryList.get(position);
            int idloaisp = selectedCategory.getId();
            String tenloaisp = selectedCategory.getTenloai();

            // Kiểm tra ID tùy chỉnh cho "Lịch sử Đơn hàng" (ID=99)
            if (idloaisp == 99) {
                Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
                startActivity(intent);
            } else {
                // Xử lý logic lọc ID (có thể cần ánh xạ ID API với ID trong code)
                int id_de_loc = idloaisp;
                if (idloaisp == 1) {
                    id_de_loc = 3;
                } else if (idloaisp == 2) {
                    id_de_loc = 5;
                }

                // Chuyển sang màn hình sản phẩm theo loại (SanphamActivity)
                Intent intent = new Intent(MainActivity.this, SanphamActivity.class);
                intent.putExtra("idsanpham", id_de_loc); // Gửi ID loại sản phẩm để lọc
                intent.putExtra("tenloaisanpham", tenloaisp); // Gửi tên để đặt tiêu đề
                startActivity(intent);
            }
            // Đóng menu sau khi chọn
            drawerLayout.closeDrawer(GravityCompat.START);
        });
    }

    /**
     * Phương thức fetchCategoryData(): Tải danh sách danh mục sản phẩm từ API.
     */
    private void fetchCategoryData() {
        if (!CheckConnection.haveNetworkConnection(this)) {
            CheckConnection.ShowToast_Short(this, "Vui lòng kiểm tra kết nối Internet!");
            return;
        }

        // Tạo yêu cầu JsonArrayRequest để lấy danh sách loại sản phẩm
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.DuongDanLoaisp, response -> {
            try {
                categoryList.clear(); // Xóa dữ liệu cũ
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = response.getJSONObject(i);
                    // Parse dữ liệu từ JSON
                    int id = jsonObject.getInt("id");
                    String tenLoaisp = jsonObject.getString("tenloaisanpham");
                    String hinhAnhLoaisp = jsonObject.getString("hinhanhloaisanpham");

                    categoryList.add(new Loaisp(id, tenLoaisp, hinhAnhLoaisp));
                }
                // Thêm mục tùy chỉnh "Lịch sử Đơn hàng" vào cuối danh sách
                categoryList.add(new Loaisp(99, "Lịch sử Đơn hàng", ""));
                categoryAdapter.notifyDataSetChanged(); // Cập nhật ListView
            } catch (JSONException e) {
                Log.e("MainActivity_JSON", "Lỗi phân tích JSON: " + e.getMessage());
            }
        }, error -> {
            Log.e("MainActivity_Volley", "Lỗi tải dữ liệu: " + error.toString());
            CheckConnection.ShowToast_Short(this, "Lỗi kết nối server.");
        });

        requestQueue.add(jsonArrayRequest); // Thêm yêu cầu vào hàng đợi Volley
    }

    /**
     * Phương thức setupTabs(): Thiết lập sự kiện chuyển Fragment khi chọn Tab.
     */
    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                // Vị trí 0: Tab Mới nhất
                if (tab.getPosition() == 0) {
                    selectedFragment = new NewProductFragment();
                    // Vị trí 1: Tab Nổi bật
                } else if (tab.getPosition() == 1) {
                    selectedFragment = new HotProductFragment();
                }
                if (selectedFragment != null) {
                    loadFragment(selectedFragment); // Tải Fragment tương ứng
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) { }
            @Override public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    /**
     * Phương thức loadFragment(): Thực hiện việc thay thế Fragment trong FrameLayout.
     * @param fragment Fragment mới cần hiển thị.
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment) // Thay thế nội dung FrameLayout bằng Fragment mới
                .commit();
    }

    /**
     * Phương thức onCreateOptionsMenu: Tạo menu Options (Icon giỏ hàng).
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_giohang, menu);
        final MenuItem menuItem = menu.findItem(R.id.menuGioHang);
        View actionView = menuItem.getActionView(); // Lấy View tùy chỉnh của icon giỏ hàng

        if (actionView != null) {
            // Ánh xạ TextView hiển thị số lượng sản phẩm (Cart Badge)
            cartItemCountTextView = actionView.findViewById(R.id.cartBadgeTextView);
            // Thiết lập click listener trên toàn bộ View tùy chỉnh
            actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        }

        updateCartBadge(); // Cập nhật số lượng sản phẩm ngay khi menu được tạo
        return true;
    }

    /**
     * Phương thức onOptionsItemSelected: Xử lý click vào các mục trong Options Menu.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuGioHang) {
            // Chuyển sang màn hình Giỏ hàng
            startActivity(new Intent(this, GioHangActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Phương thức updateCartBadge(): Cập nhật số lượng sản phẩm trên badge giỏ hàng.
     */
    private void updateCartBadge() {
        if (cartItemCountTextView == null) return;
        int count = (manggiohang != null) ? manggiohang.size() : 0; // Đếm số lượng sản phẩm

        if (count == 0) {
            cartItemCountTextView.setVisibility(View.GONE); // Ẩn badge nếu giỏ hàng trống
        } else {
            // Hiển thị số lượng (giới hạn hiển thị tối đa 99)
            cartItemCountTextView.setText(String.valueOf(Math.min(count, 99)));
            cartItemCountTextView.setVisibility(View.VISIBLE);
        }
    }
}