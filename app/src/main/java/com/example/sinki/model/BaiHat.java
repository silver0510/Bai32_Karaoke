package com.example.sinki.model;

/**
 * Created by Sinki on 8/25/2017.
 */

public class BaiHat {
    private  String ma;
    private String ten;
    private String caSi;
    private boolean like;

    public BaiHat() {
    }

    public BaiHat(String ma, String ten, String caSi, boolean like) {
        this.ma = ma;
        this.ten = ten;
        this.caSi = caSi;
        this.like = like;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getCaSi() {
        return caSi;
    }

    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
