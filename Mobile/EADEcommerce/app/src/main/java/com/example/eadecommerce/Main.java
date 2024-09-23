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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.eadecommerce.fragments.CartFragment;
import com.example.eadecommerce.fragments.CategoriesFragment;
import com.example.eadecommerce.fragments.CommentsFragment;
import com.example.eadecommerce.fragments.OrdersFragment;
import com.example.eadecommerce.fragments.ProfileFragment;
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

        // Set OnClickListener for buttonDrawerMenuRight to load CartFragment
        buttonDrawerMenuRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load CartFragment when buttonDrawerMenuRight is clicked
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContent, new CartFragment());
                transaction.commit();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                Fragment selectedFragment = null;

                if (itemId == R.id.navHome) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.navProfile) {
                    selectedFragment = new ProfileFragment();
                } else if (itemId == R.id.navCart) {
                    selectedFragment = new CartFragment();
                } else if (itemId == R.id.navMyOrders) {
                    selectedFragment = new OrdersFragment();
                } else if (itemId == R.id.navMyComments) {
                    selectedFragment = new CommentsFragment();
                } else if (itemId == R.id.navCategories) {
                    selectedFragment = new CategoriesFragment();
                } else if (itemId == R.id.navLogout) {
                    Intent intent = new Intent(Main.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                if (selectedFragment != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.mainContent, selectedFragment);
                    transaction.commit();
                }

                drawerLayout.close();  // Close the drawer after selection
                return true;
            }
        });
    }
}
