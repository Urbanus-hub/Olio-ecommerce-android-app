package com.example.olio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView totalPriceText;
    private Button checkoutButton;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initializeViews();
        setupRecyclerView();
        setupCheckoutButton();
        updateTotalPrice();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.cart_recycler_view);
        totalPriceText = findViewById(R.id.total_price);
        checkoutButton = findViewById(R.id.checkout_button);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(Cart.getInstance().getItems());
        recyclerView.setAdapter(adapter);
    }

    private void setupCheckoutButton() {
        checkoutButton.setOnClickListener(v -> handleCheckout());
    }

    private void handleCheckout() {
        if (Cart.getInstance().getItems().isEmpty()) {
            Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalAmount = Cart.getInstance().getTotalPrice();
        PaymentDialog dialog = new PaymentDialog(this, totalAmount, new PaymentDialog.OnPaymentListener() {
            @Override
            public void onPaymentSuccess() {
                // Payment was processed successfully but waiting for user to press Done
                Toast.makeText(CartActivity.this, "Payment successful!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaymentError(Exception e) {
                // Payment failed
                Toast.makeText(CartActivity.this,
                        "Payment failed: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaymentCancelled() {
                // User cancelled the payment
                Toast.makeText(CartActivity.this,
                        "Payment cancelled",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaymentComplete() {
                // User pressed Done button after successful payment
                Cart.getInstance().clearCart();
                adapter.notifyDataSetChanged();
                updateTotalPrice();
                Toast.makeText(CartActivity.this,
                        "Order placed successfully!",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });
        dialog.show();
    }

    private void updateTotalPrice() {
        double total = Cart.getInstance().getTotalPrice();
        totalPriceText.setText(String.format("Total: ksh%.2f", total));
    }
}