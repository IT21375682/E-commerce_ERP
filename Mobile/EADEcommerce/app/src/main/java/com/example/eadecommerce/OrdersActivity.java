package com.example.eadecommerce;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eadecommerce.fragments.CanceledOrderFragment;
import com.example.eadecommerce.fragments.CompletedOrderFragment;
import com.example.eadecommerce.fragments.PendingOrderFragment;
import com.example.eadecommerce.model.Order;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.JwtUtils;
import com.example.eadecommerce.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    private ImageButton buttonBack;
    private List<Order> pendingOrders = new ArrayList<>();
    private List<Order> completedOrders = new ArrayList<>();
    private List<Order> canceledOrders = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        buttonBack = findViewById(R.id.buttonBack);
        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        LinearLayout linearNavbarItem1 = findViewById(R.id.linearNavbarItem1);
        LinearLayout linearNavbarItem2 = findViewById(R.id.linearNavbarItem2);
        LinearLayout linearNavbarItem3 = findViewById(R.id.linearNavbarItem3);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Reload your data here
            reloadData();
        });

        // Initialize order lists
        initializeOrderLists();

        // Set up click listeners for the navigation items
        linearNavbarItem1.setOnClickListener(v -> {
            setFragment(new PendingOrderFragment(), pendingOrders);
            updateUIForActiveFragment(1);
        });

        linearNavbarItem2.setOnClickListener(v -> {
            setFragment(new CompletedOrderFragment(), completedOrders);
            updateUIForActiveFragment(2);
        });

        linearNavbarItem3.setOnClickListener(v -> {
            setFragment(new CanceledOrderFragment(), canceledOrders);
            updateUIForActiveFragment(3);
        });
    }

    private void initializeOrderLists() {
        // Assuming you have an instance of Retrofit and ApiService
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Get the JWT token from SharedPreferences and Decode the token and get the user ID
        String token = JwtUtils.getTokenFromSharedPreferences(this);
        String userId = JwtUtils.getUserIdFromToken(token);
        Log.d("userId",userId);

        // Call the API to fetch user orders
        Call<List<Order>> call = apiService.getUserOrders(userId);
        call.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Order> allOrders = response.body();

                    // Log the retrieved orders data
                    Log.d("OrdersActivity", "Retrieved Orders: " + allOrders.toString());

                    // Initialize the order lists
                    pendingOrders.clear();
                    completedOrders.clear();
                    canceledOrders.clear();

                    // Sort orders based on their statuses
                    for (Order order : allOrders) {
                        if (order.getStatus().isCanceled()) {
                            canceledOrders.add(order);
                        } else if (order.getStatus().isDelivered()) {
                            completedOrders.add(order);
                        } else {
                            pendingOrders.add(order);
                        }
                    }

                    // Set default fragment after orders are loaded
                    setFragment(new PendingOrderFragment(), pendingOrders);
                    updateUIForActiveFragment(1);

                } else {
                    // Handle the case where the response was not successful
                    Log.e("OrdersActivity", "Failed to load orders: " + response.message());
                }
                // Stop refreshing animation
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                // Handle failure to connect to the API
                Log.e("OrdersActivity", "Error: " + t.getMessage());
                // Stop refreshing animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    private void setFragment(Fragment fragment, List<Order> orderList) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("orderList", new ArrayList<>(orderList)); // Pass the order list to the fragment
        fragment.setArguments(bundle); // Set the arguments for the fragment

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cusTransactionMethodFragmentContainer, fragment);
        transaction.commit();
    }

    private void updateUIForActiveFragment(int activeFragment) {
        // Reset all fragment views
        resetFragmentViews();

        // Update the active fragment's UI
        if (activeFragment == 1) { // Pending
            ((ImageView) findViewById(R.id.imageFolder1)).setImageResource(R.drawable.pending_on);
            ((TextView) findViewById(R.id.txtMyparcels)).setTextColor(getResources().getColor(R.color.ordersOnTextColor));
        } else if (activeFragment == 2) { // Completed
            ((ImageView) findViewById(R.id.imageFolder2)).setImageResource(R.drawable.completed_on);
            ((TextView) findViewById(R.id.txtHome)).setTextColor(getResources().getColor(R.color.ordersOnTextColor));
        } else if (activeFragment == 3) { // Canceled
            ((ImageView) findViewById(R.id.imageFolder3)).setImageResource(R.drawable.cancel_on);
            ((TextView) findViewById(R.id.txtMyDeliveries)).setTextColor(getResources().getColor(R.color.ordersOnTextColor));
        }
    }

    private void resetFragmentViews() {
        // Reset Pending
        ((ImageView) findViewById(R.id.imageFolder1)).setImageResource(R.drawable.pending);
        ((TextView) findViewById(R.id.txtMyparcels)).setTextColor(getResources().getColor(R.color.ordersTextColor));

        // Reset Completed
        ((ImageView) findViewById(R.id.imageFolder2)).setImageResource(R.drawable.completed);
        ((TextView) findViewById(R.id.txtHome)).setTextColor(getResources().getColor(R.color.ordersTextColor));

        // Reset Canceled
        ((ImageView) findViewById(R.id.imageFolder3)).setImageResource(R.drawable.cancel);
        ((TextView) findViewById(R.id.txtMyDeliveries)).setTextColor(getResources().getColor(R.color.ordersTextColor));
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

    private void reloadData() {
        // Re-initialize order lists
        initializeOrderLists();

        // Check the currently active fragment and set it again
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.cusTransactionMethodFragmentContainer);
        if (currentFragment instanceof PendingOrderFragment) {
            setFragment(new PendingOrderFragment(), pendingOrders);
            updateUIForActiveFragment(1);
        } else if (currentFragment instanceof CompletedOrderFragment) {
            setFragment(new CompletedOrderFragment(), completedOrders);
            updateUIForActiveFragment(2);
        } else if (currentFragment instanceof CanceledOrderFragment) {
            setFragment(new CanceledOrderFragment(), canceledOrders);
            updateUIForActiveFragment(3);
        }

        // Stop the refreshing animation
        swipeRefreshLayout.setRefreshing(false);
    }

}