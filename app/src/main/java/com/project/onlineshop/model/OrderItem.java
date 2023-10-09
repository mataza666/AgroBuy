package com.project.onlineshop.model;

public class OrderItem {
    private int id;
    private int orderId;
    private int productId;

    public OrderItem(int id, int orderId, int productId) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }
}

