package com.example.eadecommerce;

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

import com.example.eadecommerce.adapter.CheckoutAdapter;
import com.example.eadecommerce.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CheckoutAdapter checkoutAdapter;
    private TextView totalAmountTextView;
    private Button payNowButton;
    private List<CartItem> cartItems;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        buttonBack = findViewById(R.id.buttonBack);
        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        payNowButton = findViewById(R.id.payNowButton);

        // Populate your cartItems list with actual data
        cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Item 1", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 10.0, 1));
        cartItems.add(new CartItem("Item 2", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 20.0, 2));
        cartItems.add(new CartItem("Item 3", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 12.0, 1));
        cartItems.add(new CartItem("Item 4", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 24.0, 1));

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkoutAdapter = new CheckoutAdapter(cartItems);
        cartRecyclerView.setAdapter(checkoutAdapter);

        // Calculate and display total amount
        double total = calculateTotal();
        totalAmountTextView.setText(String.format("Total: $%.2f", total));

        // Set up checkout button listener
        payNowButton.setOnClickListener(v -> {
            // Handle checkout logic
        });
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
