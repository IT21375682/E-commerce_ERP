package com.example.eadecommerce;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eadecommerce.adapter.OrderProductsAdapter;
import com.example.eadecommerce.model.CartItem;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageButton buttonBack;
    private RelativeLayout cusMyBookingArrowUpLayout, cusMyBookingArrowDownLayout;
    private ImageView cusMyBookingArrowUp, cusMyBookingArrowDown;
    private RecyclerView recyclerParcelDetails;
    private OrderProductsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        buttonBack = findViewById(R.id.buttonBack);
        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Find views
        cusMyBookingArrowUpLayout = findViewById(R.id.cusMyBookingArrowUpLayout);
        cusMyBookingArrowDownLayout = findViewById(R.id.cusMyBookingArrowDownLayout);
        cusMyBookingArrowUp = findViewById(R.id.cusMyBookingArrowUp);
        cusMyBookingArrowDown = findViewById(R.id.cusMyBookingArrowDown);
        recyclerParcelDetails = findViewById(R.id.recyclerParcelDetails);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Initially, hide the down layout and RecyclerView
        cusMyBookingArrowDownLayout.setVisibility(View.GONE);
        recyclerParcelDetails.setVisibility(View.GONE);

        // Set up click listener for arrow button
        cusMyBookingArrowUp.setOnClickListener(v -> {
            // Hide the up layout and show the down layout and RecyclerView
            cusMyBookingArrowUpLayout.setVisibility(View.GONE);
            cusMyBookingArrowDownLayout.setVisibility(View.VISIBLE);
            recyclerParcelDetails.setVisibility(View.VISIBLE);
        });

        cusMyBookingArrowDown.setOnClickListener(v -> {
            // Show the up layout and hide the down layout and RecyclerView
            cusMyBookingArrowUpLayout.setVisibility(View.VISIBLE);
            cusMyBookingArrowDownLayout.setVisibility(View.GONE);
            recyclerParcelDetails.setVisibility(View.GONE);
        });

        // Set up RecyclerView with mock data
        setUpRecyclerView();

        // Handle swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            setUpRecyclerView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setUpRecyclerView() {
        recyclerParcelDetails.setLayoutManager(new LinearLayoutManager(this));

        // Mock data for CartItems
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem("Product 1", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 10.99, 2));
        cartItems.add(new CartItem("Product 2", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 24.99, 1));
        cartItems.add(new CartItem("Product 3", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 7.49, 5));
        cartItems.add(new CartItem("Product 4", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 10.99, 2));
        cartItems.add(new CartItem("Product 5", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 24.99, 1));
        cartItems.add(new CartItem("Product 6", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 7.49, 5));
        cartItems.add(new CartItem("Product 7", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 10.99, 2));
        cartItems.add(new CartItem("Product 8", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 24.99, 1));
        cartItems.add(new CartItem("Product 9", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 7.49, 5));
        cartItems.add(new CartItem("Product 10", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 10.99, 2));
        cartItems.add(new CartItem("Product 11", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 24.99, 1));
        cartItems.add(new CartItem("Product 12", "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", 7.49, 5));

        adapter = new OrderProductsAdapter(cartItems);
        recyclerParcelDetails.setAdapter(adapter);
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