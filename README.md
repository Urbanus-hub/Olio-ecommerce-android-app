# Olio-ecommerce-android-app
Olio is an Android app designed for secure Mpesa payments and user account management. It offers intuitive UIs, real-time transaction feedback, and robust error handling, making it ideal for businesses or personal use.
# Olio Application Documentation

## Overview
Olio is a comprehensive Android application designed to facilitate seamless user interactions, including registration, login, and payment processes. The app employs robust backend mechanisms and user-friendly interfaces to ensure reliability and ease of use.

---

## Features

### 1. User Management
- **User Registration**: Allows users to create accounts with secure and validated input.
- **User Login**: Provides an interface for users to log in securely.

### 2. Payment Integration
- **Payment Dialog**: Implements an M-Pesa payment dialog for secure and convenient transactions.
- **Payment Status Handling**: Tracks payment progress and notifies users of success, failure, or cancellation.

---

## Components

### 1. LoginActivity
#### Description
Manages user login functionality, including input validation and authentication.
#### Key Methods:
- **initializeViews()**: Binds UI elements.
- **setupClickListeners()**: Configures actions for buttons.
- **attemptLogin()**: Validates input and checks user credentials.
- **validateInputs()**: Ensures email and password meet required standards.

---

### 2. PaymentDialog
#### Description
Handles payments using M-Pesa API, including STK push and real-time status updates.
#### Key Features:
- **Dynamic UI**: Displays payment progress and results.
- **Robust Status Checks**: Tracks payment success, failure, or cancellation.
- **Error Handling**: Provides user feedback for errors and retry options.

#### Key Methods:
- **initializePaymentView()**: Sets up the payment UI.
- **validateAndInitiatePayment()**: Ensures valid input and starts payment.
- **checkPaymentStatus()**: Monitors payment state in intervals.
- **handlePaymentSuccess()**: Processes successful transactions.
- **handlePaymentError()**: Handles errors with appropriate feedback.
- **dismiss()**: Clears resources when dialog is closed.

#### Interfaces
**OnPaymentListener**: Provides callback methods for payment outcomes.
- `onPaymentSuccess()`
- `onPaymentError(Exception e)`
- `onPaymentCancelled()`
- `onPaymentComplete()`

---

## Architecture
Olio follows an **MVC (Model-View-Controller)** architecture:

1. **Model**: DatabaseHelper for managing user credentials and data.
2. **View**: XML layouts for LoginActivity and PaymentDialog.
3. **Controller**: Java classes to handle business logic and interaction.

---

## Dependencies
Ensure the following dependencies are included in your project for full functionality:

```toml
[dependencies]
androidx.appcompat = "1.6.1"
androidx.constraintlayout = "2.1.4"
com.squareup.retrofit2 = "2.9.0"
com.squareup.okhttp3 = "4.10.0"
gson = "2.8.9"
```

---

## Installation & Setup

1. Clone the repository.
2. Open the project in Android Studio.
3. Sync Gradle files to install dependencies.
4. Configure your M-Pesa API credentials in the `MpesaHelper` class.
5. Build and run the app on an emulator or physical device.

---

## Future Enhancements
- Add support for other payment methods.
- Implement detailed user analytics.
- Introduce dark mode for enhanced user experience.

---

## Contributors
- **Urbanus Kioko**

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

