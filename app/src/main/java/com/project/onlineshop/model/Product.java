package com.project.onlineshop.model;

public class Product {
    private int id_product;
    private String nama_product;
    private double harga_product;
    private String gambar_product;

    public Product(int id_product, String nama_product, double harga_product, String gambar_product) {
        this.id_product = id_product;
        this.nama_product = nama_product;
        this.harga_product = harga_product;
        this.gambar_product = gambar_product;
    }

    // Getters and setters

    public int getIdProduct() {
        return id_product;
    }

    public void setIdProduct(int id_product) {
        this.id_product = id_product;
    }

    public String getNamaProduct() {
        return nama_product;
    }

    public void setNamaProduct(String nama_product) {
        this.nama_product = nama_product;
    }

    public double getHargaProduct() {
        return harga_product;
    }

    public void setHargaProduct(double harga_product) {
        this.harga_product = harga_product;
    }

    public String getGambarProduct() {
        return gambar_product;
    }

    public void setGambarProduct(String gambar_product) {
        this.gambar_product = gambar_product;
    }
}
