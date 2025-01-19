package com.example.olio;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class UserProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Setup toolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        // Initialize views
        ImageView profileImage = findViewById(R.id.profile_image);
        TextView userName = findViewById(R.id.user_name);
        TextView userEmail = findViewById(R.id.user_email);
        CardView ordersCard = findViewById(R.id.orders_card);
        CardView wishlistCard = findViewById(R.id.wishlist_card);
        CardView settingsCard = findViewById(R.id.settings_card);
        MaterialButton editProfileButton = findViewById(R.id.edit_profile_button);

        // Set dummy data (replace with real user data in production)
        userName.setText("John Doe");
        userEmail.setText("john.doe@example.com");

        // Click listeners
        editProfileButton.setOnClickListener(v -> {
            // TODO: Implement edit profile functionality
        });

        ordersCard.setOnClickListener(v -> {
            // TODO: Navigate to orders activity
        });

        wishlistCard.setOnClickListener(v -> {
            // TODO: Navigate to wishlist activity
        });

        settingsCard.setOnClickListener(v -> {
            // TODO: Navigate to settings activity
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}