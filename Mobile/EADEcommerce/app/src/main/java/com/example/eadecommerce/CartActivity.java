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
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.adapter.CartAdapter;
import com.example.eadecommerce.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView totalAmountTextView, clearCart;
    private Button checkoutButton;
    private List<CartItem> cartItems;
    private Button updateButton;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        checkoutButton = findViewById(R.id.checkoutButton);
        updateButton = findViewById(R.id.cartUpdateButton);
        updateButton.setVisibility(View.INVISIBLE);
        buttonBack = findViewById(R.id.buttonBack);

        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Populate your cartItems list with actual data
        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Item 1", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 10.0, 1));
        cartItems.add(new CartItem("Item 2", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 20.0, 2));
        cartItems.add(new CartItem("Item 3", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 10.0, 4));
        cartItems.add(new CartItem("Item 4", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 20.0, 1));
        cartItems.add(new CartItem("Item 5", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 10.0, 2));
        cartItems.add(new CartItem("Item 6", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 20.0, 1));
        cartItems.add(new CartItem("Item 4", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 20.0, 1));
        cartItems.add(new CartItem("Item 5", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 10.0, 2));
        cartItems.add(new CartItem("Item 6", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 20.0, 1));


        // Set up RecyclerView
        cartAdapter = new CartAdapter(cartItems);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartRecyclerView.setAdapter(cartAdapter);

        // Calculate and display total amount
        updateTotalAmount();

        clearCart = findViewById(R.id.clearCart);

        // Set click listener for clearCart to remove all items from cart
        clearCart.setOnClickListener(v -> {
            cartItems.clear();  // Clear all items from the cart
            cartAdapter.notifyDataSetChanged();  // Notify adapter about the change
            updateTotalAmount();  // Update the total to reflect the empty cart
            updateButton.setVisibility(View.GONE);  // Hide update button as no items exist
        });

        // Set up checkout button listener
        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent); // Start the CheckoutActivity
        });

        // Set up update button listener
        updateButton.setOnClickListener(v -> {
            // Loop through cart items to update counts and remove items with count 0
            for (int i = cartItems.size() - 1; i >= 0; i--) {
                CartItem item = cartItems.get(i);
                if (item.getCount() == 0) {
                    cartAdapter.removeItem(i); // Remove item from adapter if count is 0
                } else {
                    cartAdapter.updateItemCount(i, item.getCount()); // Update the count for items with count > 0
                }
            }
            updateTotalAmount(); // Update the total when clicked
            updateButton.setVisibility(View.GONE); // Hide the button after updating
        });

    }

    public void showUpdateButton() {
        updateButton.setVisibility(View.VISIBLE); // Show the update button
    }

    private void updateTotalAmount() {
        double total = calculateTotal();
        totalAmountTextView.setText(String.format("Total: $%.2f", total));
    }

    private double calculateTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getPrice() * item.getCount();
        }
        return total;
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
