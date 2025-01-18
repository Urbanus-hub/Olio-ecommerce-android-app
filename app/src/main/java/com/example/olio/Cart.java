package com.example.olio;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private List<Product> cartItems;
    private CartUpdateListener listener;

    private Cart() {
        cartItems = new ArrayList<>();
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addItem(Product product) {
        cartItems.add(product);
        if (listener != null) {
            listener.onCartUpdated(cartItems.size());
        }
    }

    public List<Product> getItems() {
        return cartItems;
    }

    public void setCartUpdateListener(CartUpdateListener listener) {
        this.listener = listener;
    }

    public interface CartUpdateListener {
        void onCartUpdated(int itemCount);
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product product : cartItems) {
            total += product.getPrice();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
        if (listener != null) {
            listener.onCartUpdated(0);
        }
    }
}