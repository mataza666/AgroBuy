package com.project.onlineshop.model;

public class Cart {
    private final int id;
    private final int customerId;
    private final int productId;
    public Cart(int id, int customerId, int productId) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getProductId() {
        return productId;
    }


}
