package com.example.Appgiaythethao_Phuong.model;

public class Loaisp {
    int id;
    String tenloai;
    String hinhloai;

    public Loaisp(int id, String tenloai, String hinhloai) {
        this.id = id;
        this.tenloai = tenloai;
        this.hinhloai = hinhloai;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTenloai() {
        return tenloai;
    }
    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }
    public String getHinhloai() {
        return hinhloai;
    }
    public void setHinhloai(String hinhloai) {
        this.hinhloai = hinhloai;
    }
}


