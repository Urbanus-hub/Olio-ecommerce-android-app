package com.example.olio;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class PaymentDialog extends Dialog {
    private final Context context;
    private final double amount;
    private final OnPaymentListener listener;
    private ProgressBar progressBar;
    private View contentView;
    private EditText phoneNumberInput;
    private Button payButton;
    private Button cancelButton;
    private boolean isPaymentSuccess = false;

    public PaymentDialog(@NonNull Context context, double amount, @NonNull OnPaymentListener listener) {
        super(context);
        this.context = context;
        this.amount = amount;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initializePaymentView();
        setCancelable(false);
    }

    private void initializePaymentView() {
        setContentView(R.layout.dialog_payment);  // Your original payment dialog layout
        initializeViews();
        setupListeners();
        displayAmount();
    }

    private void initializeViews() {
        contentView = findViewById(R.id.payment_content);
        progressBar = findViewById(R.id.progress_bar);
        phoneNumberInput = findViewById(R.id.phone_number_input);
        payButton = findViewById(R.id.pay_button);
        cancelButton = findViewById(R.id.cancel_button);

        contentView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void setupListeners() {
        payButton.setOnClickListener(v -> {
            String phoneNumber = phoneNumberInput.getText().toString().trim();
            validateAndInitiatePayment(phoneNumber);
        });

        cancelButton.setOnClickListener(v -> {
            if (!isPaymentSuccess) {
                listener.onPaymentCancelled();
                dismiss();
            }
        });
    }

    private void displayAmount() {
        TextView amountText = findViewById(R.id.amount_text);
        amountText.setText(String.format("Amount to pay: KES %.2f", amount));
    }

    private void validateAndInitiatePayment(String phoneNumber) {
        if (phoneNumber.isEmpty()) {
            showError("Please enter a phone number");
            return;
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            showError("Please enter a valid Kenyan phone number");
            return;
        }

        String formattedNumber = formatPhoneNumber(phoneNumber);
        initiatePayment(formattedNumber);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^(?:254|\\+254|0)(7|1)[0-9]{8}$";
        return phoneNumber.matches(regex);
    }

    private String formatPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replaceAll("[\\s\\-+]", "");
        if (phoneNumber.startsWith("0")) {
            phoneNumber = "254" + phoneNumber.substring(1);
        }
        return phoneNumber;
    }

    private void initiatePayment(String phoneNumber) {
        showWaitingForPaymentView();  // Show waiting view

        new Thread(() -> {
            try {
                MpesaHelper mpesaHelper = new MpesaHelper();
                mpesaHelper.initiatePayment(phoneNumber, amount);  // Trigger STK Push

                // Now, we wait for payment confirmation (via API call or callback)
                waitForPaymentConfirmation();
            } catch (Exception e) {
                new Handler(context.getMainLooper()).post(() -> {
                    hideLoading();
                    handlePaymentError(e);
                });
            }
        }).start();
    }

    private void waitForPaymentConfirmation() {
        // Simulate waiting for the payment to be confirmed (replace with actual logic)
        new Handler(context.getMainLooper()).postDelayed(() -> {
            boolean paymentSuccess = checkPaymentStatus();  // Implement this logic

            if (paymentSuccess) {
                handlePaymentSuccess();  // Show success screen only after confirmation
            } else {
                handlePaymentError(new Exception("Payment cancelled or failed."));
            }
        }, 10000);  // Wait for 10 seconds (adjust as needed)
    }

    private boolean checkPaymentStatus() {
        // Replace this with logic to query the server or listen for the payment callback
        return true;  // Simulate success; change this for actual success/failure check
    }

    private void handlePaymentSuccess() {
        isPaymentSuccess = true;
        listener.onPaymentSuccess();
        showSuccessView();  // Show the success view
    }

    private void handlePaymentError(Exception e) {
        isPaymentSuccess = false;
        showError("Payment failed or cancelled: " + e.getMessage());
        listener.onPaymentError(e);
        showErrorOrCancelledView();  // Show the error/cancelled view
    }

    private void showWaitingForPaymentView() {
        contentView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        payButton.setEnabled(false);
        cancelButton.setEnabled(false);

        // Show "Waiting for Payment Confirmation" view
        setContentView(R.layout.layout_waiting_for_payment);
    }

    private void showSuccessView() {
        // Set content to the success view
        setContentView(R.layout.layout_payment_success);

        TextView amountPaid = findViewById(R.id.paid_amount);
        amountPaid.setText(String.format("Amount paid: KES %.2f", amount));

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setOnClickListener(v -> {
            listener.onPaymentComplete();
            dismiss();
        });
    }

    private void showErrorOrCancelledView() {
        setContentView(R.layout.layout_payment_error_cancelled);

        Button retryButton = findViewById(R.id.retry_button);
        retryButton.setOnClickListener(v -> {
            // Retry payment
            contentView.setVisibility(View.VISIBLE);
            initializePaymentView();
        });

        Button closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dismiss());
    }

    private void showError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void hideLoading() {
        progressBar.setVisibility(View.GONE);  // Hide the progress bar
        contentView.setVisibility(View.VISIBLE);  // Bring back the payment content view
        payButton.setEnabled(true);  // Re-enable buttons
        cancelButton.setEnabled(true);
    }

    private void resolve() {
        // Reset the dialog and bring everything to the initial state
        isPaymentSuccess = false;
        payButton.setEnabled(true);
        cancelButton.setEnabled(true);
        contentView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (!isPaymentSuccess) {
            listener.onPaymentCancelled();
            super.onBackPressed();
        }
    }

    public interface OnPaymentListener {
        void onPaymentSuccess();
        void onPaymentError(Exception e);
        void onPaymentCancelled();
        void onPaymentComplete();
    }
}
