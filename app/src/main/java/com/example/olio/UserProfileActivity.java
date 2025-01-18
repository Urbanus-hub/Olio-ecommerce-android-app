package com.example.olio;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class UserProfileActivity extends AppCompatActivity {
    private TextView userNameText;
    private TextView userEmailText;
    private RecyclerView orderHistoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize views
        userNameText = findViewById(R.id.user_name);
        userEmailText = findViewById(R.id.user_email);
        orderHistoryRecyclerView = findViewById(R.id.order_history_recycler);

        // Set up user details (you would typically get this from a database or preferences)
        userNameText.setText("John Doe"); // Replace with actual user data
        userEmailText.setText("john.doe@example.com"); // Replace with actual user data

        // Set up order history RecyclerView
        orderHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // You would typically get order history from a database
        OrderHistoryAdapter adapter = new OrderHistoryAdapter();
        orderHistoryRecyclerView.setAdapter(adapter);
    }
}