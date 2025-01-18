package com.example.olio;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView titleText;
    private TextView priceText;
    private TextView descriptionText;
    private Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        productImage = findViewById(R.id.detail_product_image);
        titleText = findViewById(R.id.detail_product_title);
        priceText = findViewById(R.id.detail_product_price);
        descriptionText = findViewById(R.id.detail_product_description);
        addToCartButton = findViewById(R.id.add_to_cart_button);

        // Get data from intent
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0.0);
        String imageUrl = getIntent().getStringExtra("image");

        // Set data to views
        titleText.setText(title);
        priceText.setText(String.format("$%.2f", price));
        descriptionText.setText(description);
        Picasso.get().load(imageUrl).into(productImage);

        // Add to cart button click listener
        addToCartButton.setOnClickListener(v -> {
            // Here you would add the product to cart
            Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
        });
        // In ProductDetailActivity.java, update the addToCartButton click listener:
        addToCartButton.setOnClickListener(v -> {
            Product product = new Product();
            product.setTitle(title);
            product.setPrice(price);
            product.setDescription(description);
            product.setImage(imageUrl);

            Cart.getInstance().addItem(product);
            Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
        });
    }
}