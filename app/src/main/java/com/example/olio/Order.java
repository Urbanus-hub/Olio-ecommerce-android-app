package com.example.olio;

import java.util.List;

public class Order {
    private String orderId;
    private String orderDate;
    private double totalAmount;
    private String status;
    private List<Product> products;

    // Constructor
    public Order(String orderId, String orderDate, double totalAmount, String status, List<Product> products) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.products = products;
    }

    // Getters
    public String getOrderId() { return orderId; }
    public String getOrderDate() { return orderDate; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public List<Product> getProducts() { return products; }
}