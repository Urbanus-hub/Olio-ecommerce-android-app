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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_recycler_view);
        totalPriceText = findViewById(R.id.total_price);
        checkoutButton = findViewById(R.id.checkout_button);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter adapter = new CartAdapter(Cart.getInstance().getItems());
        recyclerView.setAdapter(adapter);

        // Update total price
        updateTotalPrice();

        // Checkout button click listener
        // In CartActivity.java, update the checkout button click listener:
        // In CartActivity.java, modify the checkout button click listener:
        checkoutButton.setOnClickListener(v -> {
            if (Cart.getInstance().getItems().isEmpty()) {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            double totalAmount = Cart.getInstance().getTotalPrice();
            PaymentDialog dialog = new PaymentDialog(this, totalAmount, new PaymentDialog.OnPaymentCompleteListener() {
                @Override
                public void onPaymentComplete() {
                    // Save the order to history


                    Cart.getInstance().clearCart();
                    Toast.makeText(CartActivity.this, "Order placed successfully!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });
            dialog.show();
        });


    }

    private void updateTotalPrice() {
        double total = Cart.getInstance().getTotalPrice();
        totalPriceText.setText(String.format("Total: $%.2f", total));
    }
}