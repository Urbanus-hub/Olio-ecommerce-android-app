package com.example.olio;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PaymentDialog extends Dialog {
    private Context context;
    private double amount;
    private OnPaymentCompleteListener listener;

    public PaymentDialog(Context context, double amount, OnPaymentCompleteListener listener) {
        super(context);
        this.context = context;
        this.amount = amount;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_payment);

        TextView amountText = findViewById(R.id.amount_text);
        EditText phoneNumberInput = findViewById(R.id.phone_number_input);
        Button payButton = findViewById(R.id.pay_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        amountText.setText(String.format("Amount to pay: KES %.2f", amount));

        payButton.setOnClickListener(v -> {
            String phoneNumber = phoneNumberInput.getText().toString();
            if (isValidPhoneNumber(phoneNumber)) {
                initiatePayment(phoneNumber);
            } else {
                Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Basic validation for Kenyan phone numbers
        return phoneNumber.matches("^(254|0|\\+254)7[0-9]{8}$");
    }

    private void initiatePayment(String phoneNumber) {
        // Here you would integrate with the actual M-Pesa API
        // For demonstration, we'll simulate a successful payment
        simulatePaymentProcess();
    }

    private void simulatePaymentProcess() {
        // Show progress dialog
        Toast.makeText(context, "Processing payment...", Toast.LENGTH_SHORT).show();

        // Simulate API delay
        new android.os.Handler().postDelayed(() -> {
            // Payment successful
            if (listener != null) {
                listener.onPaymentComplete();
            }
            Toast.makeText(context, "Payment successful!", Toast.LENGTH_LONG).show();
            dismiss();
        }, 2000);
    }

    public interface OnPaymentCompleteListener {
        void onPaymentComplete();
    }
}