package com.example.eadecommerce;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.eadecommerce.fragments.HomeFragment;  // Import HomeFragment
import com.example.eadecommerce.model.ProductCommentData;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.JwtUtils;
import com.example.eadecommerce.network.RetrofitClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    ImageButton buttonDrawerMenuRight;
    NavigationView navigationView;
    ImageView clickcart_logo;
    String userId, userName, userEmail;
    TextView textUsername, textUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // Get the JWT token from SharedPreferences and Decode the token to get the user ID
        String token = JwtUtils.getTokenFromSharedPreferences(this);
        userId = JwtUtils.getUserIdFromToken(token);
        userName = JwtUtils.getNameFromToken(token);
        userEmail = JwtUtils.getEmailFromToken(token);
        Log.d("userId", userId);
        Log.d("userId", userName);
        Log.d("userId", userEmail);

        // Fetch the product count for the user's cart
        fetchProductCount(userId);

        drawerLayout = findViewById(R.id.navigatorDrawer);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        buttonDrawerMenuRight = findViewById(R.id.buttonDrawerMenuRight);
        navigationView = findViewById(R.id.navigationView);
        clickcart_logo = findViewById(R.id.clickcart_logo);

        // Load HomeFragment by default when activity is created
        if (savedInstanceState == null) {
            fetchProductCount(userId);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.mainContent, new HomeFragment());  // Default to HomeFragment
            transaction.commit();
        }

        buttonDrawerToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        // Handle clickcart_logo click to load HomeFragment
        clickcart_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load HomeFragment when clickcart_logo is clicked
                fetchProductCount(userId);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContent, new HomeFragment());  // Switch to HomeFragment
                transaction.commit();
            }
        });

        View headerView = navigationView.getHeaderView(0);

        textUsername = headerView.findViewById(R.id.textUserName);
        textUserEmail = headerView.findViewById(R.id.textUserEmail);
        textUsername.setText(userName);
        textUserEmail.setText(userEmail);

        // Set OnClickListener for buttonDrawerMenuRight to load CartActivity
        buttonDrawerMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch CartActivity when buttonDrawerMenuRight is clicked
                Intent intent = new Intent(Main.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // Set up navigation item selected listener
        // Reference: Navigation Drawer Menu in Android Tutorial | How to Create Navigation Drawer in Android Studio
        // https://youtu.be/uY9iZiamyZs?si=08ygu9YGsyv2AHKt
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.navHome) {
                    // Load HomeFragment
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainContent, new HomeFragment());
                    transaction.commit();
                } else if (itemId == R.id.navProfile) {
                    // Launch ProfileActivity
                    Intent intent = new Intent(Main.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.navCart) {
                    // Launch CartActivity
                    Intent intent = new Intent(Main.this, CartActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.navMyOrders) {
                    // Launch OrdersActivity
                    Intent intent = new Intent(Main.this, OrdersActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.navMyComments) {
                    // Launch CommentsActivity
                    Intent intent = new Intent(Main.this, CommentsActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.navCategories) {
                    // Launch CategoriesActivity
                    Intent intent = new Intent(Main.this, CategoriesActivity.class);
                    startActivity(intent);
                } else if (itemId == R.id.navLogout) {
                    // Show confirmation dialog before logout
                    showLogoutConfirmationDialog();
                }

                drawerLayout.close();  // Close the drawer after selection
                return true;
            }
        });
    }

    // Method to show confirmation dialog
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle logout
                SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("jwt_token");
                editor.apply();

                Intent intent = new Intent(Main.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();  // Close the dialog
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void fetchProductCount(String userId) {

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Integer> call = apiService.getCartProductCount(userId);

        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int productCount = response.body();
                    // Display the product count
                    TextView cart_count = findViewById(R.id.cart_count);
                    cart_count.setText(String.valueOf(productCount));
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Failed to fetch product count", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Snackbar.make(findViewById(android.R.id.content), "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}

