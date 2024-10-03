package com.example.eadecommerce;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.adapter.CartAdapter;
import com.example.eadecommerce.adapter.CheckoutAdapter;
import com.example.eadecommerce.model.Address;
import com.example.eadecommerce.model.CartItem;
import com.example.eadecommerce.model.CartProduct;
import com.example.eadecommerce.model.CartProductResponse;
import com.example.eadecommerce.model.CartResponse;
import com.example.eadecommerce.model.OrderRequest;
import com.example.eadecommerce.model.UserResponse;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.JwtUtils;
import com.example.eadecommerce.network.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CheckoutAdapter checkoutAdapter;
    private TextView totalAmountTextView, subtotalAmountTextView, deliveryFeeAmountTextView;
    private TextView userAddressTextView, userPhoneTextView, userNameTextView;
    private Button payNowButton;
    private ImageButton buttonBack;
    private String userId, cartId;
    private List<CartProductResponse> cartItems;
    private RadioGroup paymentOptionsGroup;
    private double total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Get the JWT token from SharedPreferences and Decode the token and get the user ID
        String token = JwtUtils.getTokenFromSharedPreferences(this);
        userId = JwtUtils.getUserIdFromToken(token);
        Log.d("userId", userId);

        buttonBack = findViewById(R.id.buttonBack);
        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        userAddressTextView = findViewById(R.id.userAddressTextView);
        userPhoneTextView = findViewById(R.id.userPhoneTextView);
        userNameTextView = findViewById(R.id.userNameTextView);
        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        subtotalAmountTextView = findViewById(R.id.subtotalAmountTextView);
        deliveryFeeAmountTextView = findViewById(R.id.deliveryFeeAmountTextView);
        payNowButton = findViewById(R.id.payNowButton);

        // Initialize the payment options RadioGroup
        paymentOptionsGroup = findViewById(R.id.paymentOptionsGroup);

        // Set default selected option
        ((RadioButton) findViewById(R.id.radioCOD)).setChecked(true); // Set COD as default selected

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Call API to get user details
        getUserDetails(userId);

        // Call API to get cart details
        getCartDetails(userId);

        // Set up checkout button listener
        payNowButton.setOnClickListener(v -> handleCheckout());
    }

    // Method to call API and retrieve cart details
    private void getCartDetails(String userId) {

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<CartResponse> call = apiService.getCartDetails(userId);

        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()) {
                    CartResponse cartResponse = response.body();
                    cartItems = cartResponse.getProducts();

                    cartId = cartResponse.getCartId();

                    // Set data to adapter
                    checkoutAdapter = new CheckoutAdapter(cartItems);
                    cartRecyclerView.setAdapter(checkoutAdapter);

                    // Calculate and display total amount
                    updateTotalAmount();

                } else if (response.code() == 404) {
                    // Handle 404 response (cart not found)
                    updateTotalAmount();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                // Handle failure
                Snackbar.make(findViewById(android.R.id.content), "Couldn't Retrieve Cart!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the total amount
    private void updateTotalAmount() {
        if (cartItems == null) {
            subtotalAmountTextView.setText("Total: LKR 0.00");
            return;
        }
        ;

        double subTotal = 0;
        double deliveryFee = 20;
        for (CartProductResponse product : cartItems) {
            subTotal += product.getPrice() * product.getCount();
        }
        total = subTotal + deliveryFee;
        subtotalAmountTextView.setText(String.format("%.2f", subTotal));
        deliveryFeeAmountTextView.setText(String.format("%.2f", deliveryFee));
        totalAmountTextView.setText(String.format("LKR %.2f", total));
    }

    private void getUserDetails(String userId) {

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<UserResponse> call = apiService.getUserById(userId);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();
                    updateUserDetails(user);
                } else {
                    Log.e("CheckoutActivity", "Failed to fetch user details");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("CheckoutActivity", "Error: " + t.getMessage());
            }
        });
    }

    private void updateUserDetails(UserResponse user) {
        // Update user name
        userNameTextView.setText(user.getName() != null ? user.getName() : "N/A");

        // Update user phone
        String phone = user.getPhone();
        userPhoneTextView.setText(phone != null && !phone.isEmpty() ? phone : "N/A");

        // Update user address
        Address address = user.getAddress();
        if (address != null) {
            List<String> addressComponents = getStrings(address);

            // Check if there are any components to display
            if (!addressComponents.isEmpty()) {
                String formattedAddress = TextUtils.join(", ", addressComponents);
                userAddressTextView.setText(formattedAddress);
            } else {
                userAddressTextView.setText("N/A");
            }
        } else {
            userAddressTextView.setText("N/A"); // Show "N/A" if address is null
        }
    }

    private static @NonNull List<String> getStrings(Address address) {
        String street = address.getStreet() != null && !address.getStreet().isEmpty() ? address.getStreet() : null;
        String city = address.getCity() != null && !address.getCity().isEmpty() ? address.getCity() : null;
        String postalCode = address.getPostalCode() != null && !address.getPostalCode().isEmpty() ? address.getPostalCode() : null;
        String country = address.getCountry() != null && !address.getCountry().isEmpty() ? address.getCountry() : null;

        // Construct the formatted address only with non-null components
        List<String> addressComponents = new ArrayList<>();
        if (street != null) addressComponents.add(street);
        if (city != null) addressComponents.add(city);
        if (postalCode != null) addressComponents.add(postalCode);
        if (country != null) addressComponents.add(country);
        return addressComponents;
    }


    private void handleCheckout() {
        String name = userNameTextView.getText().toString().trim();
        String address = userAddressTextView.getText().toString().trim();
        String phone = userPhoneTextView.getText().toString().trim();

        // Log the retrieved values
        Log.d("Checkout Activity", "Name: " + name);
        Log.d("Checkout Activity", "Address: " + address);
        Log.d("Checkout Activity", "Phone: " + phone);

        // Check for empty fields or "N/A" values
        boolean isNameValid = !name.isEmpty() && !name.equals("N/A");
        boolean isAddressValid = !address.isEmpty() && !address.equals("N/A") && !address.equals(", , ,");
        boolean isPhoneValid = !phone.isEmpty() && !phone.equals("N/A");

        // Log the validation results
        Log.d("Checkout", "Is Name Valid: " + isNameValid);
        Log.d("Checkout", "Is Address Valid: " + isAddressValid);
        Log.d("Checkout", "Is Phone Valid: " + isPhoneValid);

        // Check for empty fields or "N/A" values
        if (name.isEmpty() || name.equals("N/A") ||
                address.isEmpty() || address.equals("N/A") ||
                phone.isEmpty() || phone.equals("N/A")) {
            showProfileInfoDialog();
        } else {
            showConfirmationDialog();
        }
    }

    private void showProfileInfoDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Missing Information")
                .setMessage("Please fill in your profile information.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(CheckoutActivity.this, ProfileActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Order")
                .setMessage("Do you want to confirm this order?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Create the order object
                    createOrder();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void createOrder() {
        // Prepare product items list from cartItems
        OrderRequest orderRequest = getOrderRequest();

        // Call the API to submit the order
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.createOrder(orderRequest);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Handle success (e.g., navigate to an order success page)
                    Snackbar.make(findViewById(android.R.id.content), "Order placed successfully!", Snackbar.LENGTH_SHORT).show();
                    clearCartItems();
                } else {
                    // Handle failure (e.g., display error message)
                    Snackbar.make(findViewById(android.R.id.content), "CFailed to place order.", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle request failure
                Snackbar.make(findViewById(android.R.id.content), "Error: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private @NonNull OrderRequest getOrderRequest() {
        List<CartProduct> productItems = new ArrayList<>();
        for (CartProductResponse cartProduct : cartItems) {
            CartProduct product = new CartProduct(cartProduct.getProductId(), cartProduct.getCount());
            productItems.add(product);
        }

        // Create the order object
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId(userId);
        orderRequest.setTotal(total);
        orderRequest.setProductItems(productItems);
        return orderRequest;
    }

    private void clearCartItems() {
        // Call DELETE API to clear the cart
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.clearCart(cartId); // Assuming userId corresponds to the cartId

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(CheckoutActivity.this, Main.class);
                    startActivity(intent);
                    finish();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Failed to clear cart!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Handle failure
                Snackbar.make(findViewById(android.R.id.content), "Error: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("TAG", "Debug message");
        // Check if the fragment manager has a back stack
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("TAG", "If Debug message");
            getSupportFragmentManager().popBackStack();
        } else {
            Log.d("TAG", "Else Debug message");
            finish(); // Close activity if no fragments are in the stack
        }
    }
}
