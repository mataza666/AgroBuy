package com.project.onlineshop.model;

import java.util.List;

public class Orders {
    private int idOrder;
    private int customerId;
    private double totalPrice;
    private String orderDate;
    private String address;
    private List<OrderItem> orderItemList;

    public Orders(int idOrder, int customerId, double totalPrice, String orderDate, String address, List<OrderItem> orderItemList) {
        this.idOrder = idOrder;
        this.customerId = customerId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.address = address;
        this.orderItemList = orderItemList;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public int getCustomerId() {
        return customerId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getAddress() {
        return address;
    }

    public List<OrderItem> getOrderItemList() {
        return orderItemList;
    }
}