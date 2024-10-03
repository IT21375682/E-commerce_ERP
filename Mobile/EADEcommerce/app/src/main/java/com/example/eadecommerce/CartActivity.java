package com.example.eadecommerce;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.adapter.CartAdapter;
import com.example.eadecommerce.model.Cart;
import com.example.eadecommerce.model.CartItem;
import com.example.eadecommerce.model.CartProduct;
import com.example.eadecommerce.model.CartProductResponse;
import com.example.eadecommerce.model.CartResponse;
import com.example.eadecommerce.model.Product;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.JwtUtils;
import com.example.eadecommerce.network.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView totalAmountTextView, clearCart;
    private Button checkoutButton, updateButton;
    private ImageButton buttonBack;
    private String userId, cartId;
    private List<CartProductResponse> cartItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Get the JWT token from SharedPreferences and Decode the token and get the user ID
        String token = JwtUtils.getTokenFromSharedPreferences(this);
        userId = JwtUtils.getUserIdFromToken(token);
        Log.d("userId", userId);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        checkoutButton = findViewById(R.id.checkoutButton);
        updateButton = findViewById(R.id.cartUpdateButton);
        updateButton.setVisibility(View.INVISIBLE);
        buttonBack = findViewById(R.id.buttonBack);
        clearCart = findViewById(R.id.clearCart);

        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Call API to get cart details
        getCartDetails(userId);

        clearCart = findViewById(R.id.clearCart);

        // Set click listener for clearCart to remove all items from cart
        clearCart.setOnClickListener(v -> {
            clearCartItems();
        });

        // Set up checkout button listener
        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });

        // Set up update button listener
        updateButton.setOnClickListener(v -> updateCart());

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
                    cartItems = cartResponse.getProducts();  // Get the products from the response

                    cartId = cartResponse.getCartId();

                    // Set data to adapter
                    cartAdapter = new CartAdapter(cartItems);
                    cartRecyclerView.setAdapter(cartAdapter);

                    // Calculate and display total amount
                    updateTotalAmount();

                    // Show or hide empty cart message
                    if (cartItems.isEmpty()) {
                        findViewById(R.id.emptyCartMessage).setVisibility(View.VISIBLE);
                        cartRecyclerView.setVisibility(View.GONE); // Hide RecyclerView if empty

                        findViewById(R.id.clearCart).setVisibility(View.GONE);
                        checkoutButton.setEnabled(false); // Make checkout button unclickable

                    } else {
                        findViewById(R.id.emptyCartMessage).setVisibility(View.GONE);
                        cartRecyclerView.setVisibility(View.VISIBLE); // Show RecyclerView if not empty

                        findViewById(R.id.clearCart).setVisibility(View.VISIBLE);
                        checkoutButton.setEnabled(true); // Make checkout button clickable
                    }

                } else if (response.code() == 404) {
                    // Handle 404 response (cart not found)
                    updateTotalAmount();
                    findViewById(R.id.emptyCartMessage).setVisibility(View.VISIBLE);
                    cartRecyclerView.setVisibility(View.GONE);

                    findViewById(R.id.clearCart).setVisibility(View.GONE);
                    checkoutButton.setEnabled(false); // Make checkout button unclickable
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                // Handle failure
                Snackbar.make(findViewById(android.R.id.content), "Couldn't Retrieve Cart!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    // Method to update the cart by calling the API
    private void updateCart() {
        if (cartItems == null) return;

        // Create a new Cart object
        Cart cart = new Cart();
        cart.setCartId(cartId);
        cart.setUserId(userId);

        // Prepare the products for the Cart
        List<CartProduct> productsToUpdate = new ArrayList<>();
        for (CartProductResponse item : cartItems) {
            // Create CartProduct for each item
            CartProduct cartProduct = new CartProduct(item.getProductId(), item.getCount());
            productsToUpdate.add(cartProduct);
        }

        // Set the updated product list to the Cart
        cart.setProducts(productsToUpdate);

        // Call the API to update the cart
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.updateCart(cartId, cart);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Snackbar.make(findViewById(android.R.id.content), "Cart Updated Successfully!", Snackbar.LENGTH_SHORT).show();
                    // Optionally refresh cart details if needed
                    getCartDetails(userId);
                    updateButton.setVisibility(View.GONE);
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Failed to Update Cart!", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), "Update Failed!", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void clearCartItems() {
        // Call DELETE API to clear the cart
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Void> call = apiService.clearCart(cartId); // Assuming userId corresponds to the cartId

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Clear all items from the cart in the local list
                    if (cartItems != null) {
                        cartItems.clear();  // Clear all items from the cart
                        cartAdapter.notifyDataSetChanged();  // Notify adapter about the change
                        getCartDetails(userId);
                        updateButton.setVisibility(View.GONE);  // Hide update button as no items exist

                        // Show empty cart message
                        findViewById(R.id.emptyCartMessage).setVisibility(View.VISIBLE);
                        cartRecyclerView.setVisibility(View.GONE); // Hide RecyclerView
                    }
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


    public void showUpdateButton() {
        updateButton.setVisibility(View.VISIBLE); // Show the update button
    }

    // Method to update the total amount
    private void updateTotalAmount() {
        if (cartItems == null) {
            totalAmountTextView.setText("Total: LKR 0.00");
            return;
        }
        ;

        double total = 0;
        for (CartProductResponse product : cartItems) {
            total += product.getPrice() * product.getCount();
        }
        totalAmountTextView.setText(String.format("Total: LKR %.2f", total));
    }

    @Override
    public void onBackPressed() {
        Log.d("TAG", "Debug message");
        // Check if the fragment manager has a back stack
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("TAG", "If Debug message");
            getSupportFragmentManager().popBackStack(); // Go back to previous fragment
        } else {
            Log.d("TAG", "Else Debug message");
            finish(); // Close activity if no fragments are in the stack
        }
    }

}
