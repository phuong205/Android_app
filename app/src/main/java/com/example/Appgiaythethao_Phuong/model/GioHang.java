package com.example.Appgiaythethao_Phuong.model;

import java.io.Serializable;

/**
 * Lớp Model dùng để lưu trữ thông tin sản phẩm trong Giỏ hàng.
 * Nó khác với Sanpham.java ở chỗ có thêm trường 'soluong'.
 */
public class GioHang implements Serializable {

    // ⭐ CÁC TRƯỜNG DỮ LIỆU CẦN THIẾT ⭐
    private int idsp;          // ID sản phẩm (dùng để kiểm tra sản phẩm đã có trong giỏ chưa)
    private String tensp;      // Tên sản phẩm
    private long giasp;        // Giá sản phẩm (Nên dùng long để tránh tràn số với các giá trị tiền lớn)
    private String hinhsp;     // Hình ảnh sản phẩm
    private int soluong;       // Số lượng sản phẩm khách hàng muốn mua

    // --- CONSTRUCTOR ---

    // Constructor đầy đủ tham số
    public GioHang(int idsp, String tensp, long giasp, String hinhsp, int soluong) {
        this.idsp = idsp;
        this.tensp = tensp;
        this.giasp = giasp;
        this.hinhsp = hinhsp;
        this.soluong = soluong;
    }

    // Constructor mặc định (cần thiết nếu sử dụng các thư viện như Firebase)
    public GioHang() {
    }


    // --- GETTERS VÀ SETTERS ---

    public int getIdsp() {
        return idsp;
    }

    public void setIdsp(int idsp) {
        this.idsp = idsp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public long getGiasp() {
        return giasp;
    }

    public void setGiasp(long giasp) {
        this.giasp = giasp;
    }

    public String getHinhsp() {
        return hinhsp;
    }

    public void setHinhsp(String hinhsp) {
        this.hinhsp = hinhsp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}