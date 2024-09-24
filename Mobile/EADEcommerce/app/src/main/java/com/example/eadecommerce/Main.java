package com.example.eadecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.example.eadecommerce.fragments.HomeFragment;  // Import HomeFragment
import com.google.android.material.navigation.NavigationView;

public class Main extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton buttonDrawerToggle;
    ImageButton buttonDrawerMenuRight;
    NavigationView navigationView;
    ImageView clickcart_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        drawerLayout = findViewById(R.id.navigatorDrawer);
        buttonDrawerToggle = findViewById(R.id.buttonDrawerToggle);
        buttonDrawerMenuRight = findViewById(R.id.buttonDrawerMenuRight);
        navigationView = findViewById(R.id.navigationView);
        clickcart_logo = findViewById(R.id.clickcart_logo);

        // Load HomeFragment by default when activity is created
        if (savedInstanceState == null) {
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
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContent, new HomeFragment());  // Switch to HomeFragment
                transaction.commit();
            }
        });

        View headerView = navigationView.getHeaderView(0);
        ImageView useImage = headerView.findViewById(R.id.userImage);
        TextView textUsername = headerView.findViewById(R.id.textUserName);

        useImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Main.this, textUsername.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set OnClickListener for buttonDrawerMenuRight to load CartActivity
        buttonDrawerMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch CartActivity when buttonDrawerMenuRight is clicked
                Intent intent = new Intent(Main.this, CartActivity.class);
                startActivity(intent);
            }
        });

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
                    // Handle logout
                    Intent intent = new Intent(Main.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                drawerLayout.close();  // Close the drawer after selection
                return true;
            }
        });
    }
}

