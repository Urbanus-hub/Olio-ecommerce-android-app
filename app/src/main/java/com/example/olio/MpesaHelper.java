package com.example.olio;

import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MpesaHelper {
    private static final String BASE_URL = "https://sandbox.safaricom.co.ke/";
    private static final String CONSUMER_KEY = "fKtDWjAQRGchyTsoRZfaGqESob83PlFdJVKlPohpnJLAdprw";
    private static final String CONSUMER_SECRET = "QOnGh9IdSFRZfuKcl55q2AYIgBYqGWnaS6AGPRZnjxNqcWZJqTwtrsmvEuwkOYzg";
    private static final String BUSINESS_SHORTCODE = "174379";
    private static final String PASSKEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private OkHttpClient client;
    private String lastCheckoutRequestId;

    public MpesaHelper() {
        this.client = new OkHttpClient();
    }

    public String initiatePayment(String phoneNumber, double amount) throws Exception {
        String accessToken = getAccessToken();
        String timestamp = generateTimestamp();
        String password = generatePassword(timestamp);

        JsonObject payload = createSTKPushPayload(phoneNumber, amount, timestamp, password);
        String response = makeSTKPushRequest(payload, accessToken);

        JsonObject jsonResponse = new Gson().fromJson(response, JsonObject.class);
        lastCheckoutRequestId = jsonResponse.get("CheckoutRequestID").getAsString();

        return lastCheckoutRequestId;
    }

    public PaymentStatus getPaymentStatus(String checkoutRequestId) throws Exception {
        String accessToken = getAccessToken();
        String timestamp = generateTimestamp();
        String password = generatePassword(timestamp);

        JsonObject payload = new JsonObject();
        payload.addProperty("BusinessShortCode", BUSINESS_SHORTCODE);
        payload.addProperty("Password", password);
        payload.addProperty("Timestamp", timestamp);
        payload.addProperty("CheckoutRequestID", checkoutRequestId);

        Request request = new Request.Builder()
                .url(BASE_URL + "mpesa/stkpushquery/v1/query")
                .header("Authorization", "Bearer " + accessToken)
                .post(RequestBody.create(payload.toString(), JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                return PaymentStatus.FAILED;
            }

            JsonObject jsonResponse = new Gson().fromJson(response.body().string(), JsonObject.class);
            int resultCode = jsonResponse.get("ResultCode").getAsInt();

            switch (resultCode) {
                case 0:
                    return PaymentStatus.SUCCESS;
                case 1037:  // Timeout waiting for user input
                    return PaymentStatus.FAILED;
                case 1032:  // Cancelled by user
                    return PaymentStatus.CANCELLED;
                case 1:     // PIN required
                    return PaymentStatus.PIN_REQUIRED;
                default:
                    return PaymentStatus.PENDING;
            }
        }
    }

    private String getAccessToken() throws Exception {
        String credentials = Base64.encodeToString(
                (CONSUMER_KEY + ":" + CONSUMER_SECRET).getBytes(),
                Base64.NO_WRAP
        );

        Request request = new Request.Builder()
                .url(BASE_URL + "oauth/v1/generate?grant_type=client_credentials")
                .header("Authorization", "Basic " + credentials)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new Exception("Failed to get access token");

            JsonObject jsonResponse = new Gson().fromJson(response.body().string(), JsonObject.class);
            return jsonResponse.get("access_token").getAsString();
        }
    }

    private String generateTimestamp() {
        return new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
                .format(new Date());
    }

    private String generatePassword(String timestamp) {
        String passwordString = BUSINESS_SHORTCODE + PASSKEY + timestamp;
        return Base64.encodeToString(passwordString.getBytes(), Base64.NO_WRAP);
    }

    private JsonObject createSTKPushPayload(String phoneNumber, double amount, String timestamp, String password) {
        JsonObject payload = new JsonObject();
        payload.addProperty("BusinessShortCode", BUSINESS_SHORTCODE);
        payload.addProperty("Password", password);
        payload.addProperty("Timestamp", timestamp);
        payload.addProperty("TransactionType", "CustomerPayBillOnline");
        payload.addProperty("Amount", (int) amount);  // M-Pesa expects whole numbers
        payload.addProperty("PartyA", phoneNumber);
        payload.addProperty("PartyB", BUSINESS_SHORTCODE);
        payload.addProperty("PhoneNumber", phoneNumber);
        payload.addProperty("CallBackURL", "https://your-callback-url.com/callback");
        payload.addProperty("AccountReference", "TestPayment");
        payload.addProperty("TransactionDesc", "Payment for service");

        return payload;
    }

    private String makeSTKPushRequest(JsonObject payload, String accessToken) throws Exception {
        Request request = new Request.Builder()
                .url(BASE_URL + "mpesa/stkpush/v1/processrequest")
                .header("Authorization", "Bearer " + accessToken)
                .post(RequestBody.create(payload.toString(), JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("Payment initiation failed");
            }
            return response.body().string();
        }
    }

    public enum PaymentStatus {
        SUCCESS,
        FAILED,
        CANCELLED,
        PENDING,
        PIN_REQUIRED
    }
}