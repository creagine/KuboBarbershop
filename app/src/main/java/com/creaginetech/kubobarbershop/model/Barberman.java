package com.creaginetech.kubobarbershop.model;

public class Barberman {

    private String Image;
    private String Nama;

    public Barberman() {
    }

    public Barberman(String Image, String Nama) {
        this.Image = Image;
        this.Nama = Nama;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String Image) {
        this.Image = Image;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String Nama) {
        this.Nama = Nama;
    }
}
