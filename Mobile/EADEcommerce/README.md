# ClickCart - E-Commerce Android Mobile Application

## Project Overview

This Android mobile application allows customers to browse and shop for products through an intuitive and feature-rich interface. It enables customers to search, filter, and explore products by category, manage their carts, place orders, track order status, and leave feedback for products and vendors. The app is built to communicate with a Web API to ensure seamless and efficient data handling, providing real-time updates and order tracking.

## Features

- **Product Browsing**: Search for products, apply category filters, and view product details.
- **Cart Management**: Add products to the cart, update product quantities, and remove items as needed.
- **Order Management**: Place orders directly from the cart, view order history, and track the current status of orders.
- **User Profile**: Manage personal information and account details.
- **Feedback**: Leave comments and ratings for products and vendors based on purchase experiences.

## System Requirements

- **Minimum SDK**: Android 30
- **Target SDK**: Android 34
- **Compile SDK**: Android 34
- **IDE**: Android Studio
- **Backend**: Hosted Web API

## How to Set Up

1. **Open the Project in Android Studio**:
   - Open Android Studio and select **Open an Existing Project**.
   - Navigate to the project directory and click **OK** to load the project.

2. **Set Up the Retrofit Client**:
   - Ensure the `BASE_URL` in the `RetrofitClient` class points to your backend Web API:
     ```java
     private static final String BASE_URL = "http://192.168.1.73/ClickCart/";
     ```
   - Replace the IP address with the appropriate IP or domain name of your Web API if needed.

3. **Sync Dependencies**:
   - The project utilizes Gradle for dependency management. Ensure your `build.gradle` file includes all necessary dependencies, then click on **Sync Now** in Android Studio.

4. **Run the App**:
   - Connect your Android device or use an emulator.
   - Click on the **Run** button to build and deploy the app to your device.

## Additional Information

- **Web API Requirements**: This app relies on a Web API for data management and communication. Ensure the Web API is running and accessible at the specified `BASE_URL`.
- **User Authentication**: Account creation and activation are handled by CSR or Administrator, so customers must have their accounts activated before logging in.
- **Data Handling**: The app uses Retrofit for API calls, making it easy to extend and modify as required by the project.