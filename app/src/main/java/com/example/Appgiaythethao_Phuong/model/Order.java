package com.example.Appgiaythethao_Phuong.model;

import java.io.Serializable;

public class Order implements Serializable {

    private int id;                 // Mã đơn hàng (madonhang / id)
    private String tenkhachhang;    // Tên khách hàng đặt
    private String sodienthoai;     // SĐT
    private String ngaydat;         // Ngày đặt hàng
    private long tongtien;          // Tổng giá trị đơn hàng
    private String phuongthuctt;    // Phương thức thanh toán (COD, CHUYEN_KHOAN)

    // --- CONSTRUCTOR ---
    public Order(int id, String tenkhachhang, String sodienthoai, String ngaydat, long tongtien, String phuongthuctt) {
        this.id = id; // ⭐ ĐÃ SỬA LỖI ⭐
        this.tenkhachhang = tenkhachhang;
        this.sodienthoai = sodienthoai;
        this.ngaydat = ngaydat;
        this.tongtien = tongtien;
        this.phuongthuctt = phuongthuctt;
    }

    // Constructor mặc định (optional)
    public Order() {
    }

    // --- GETTERS VÀ SETTERS ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenkhachhang() {
        return tenkhachhang;
    }

    public void setTenkhachhang(String tenkhachhang) {
        this.tenkhachhang = tenkhachhang;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getNgaydat() {
        return ngaydat;
    }

    public void setNgaydat(String ngaydat) {
        this.ngaydat = ngaydat;
    }

    public long getTongtien() {
        return tongtien;
    }

    public void setTongtien(long tongtien) {
        this.tongtien = tongtien;
    }

    public String getPhuongthuctt() {
        return phuongthuctt;
    }

    public void setPhuongthuctt(String phuongthuctt) {
        this.phuongthuctt = phuongthuctt;
    }
}