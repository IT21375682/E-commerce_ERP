package com.example.eadecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eadecommerce.adapter.CommentAdapter;
import com.example.eadecommerce.fragments.HomeFragment;
import com.example.eadecommerce.model.CommentData;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    ImageButton buttonBack, buttonCart;
    ImageView clickcartHome;
    private RecyclerView commentsRecyclerView;

    private TextView quantityTextView;
    private Button buttonMinus, buttonPlus;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        buttonBack = findViewById(R.id.buttonBack);
        clickcartHome = findViewById(R.id.clickcartHome);
        buttonCart = findViewById(R.id.buttonCart);


        // Handle Home click to load HomeFragment
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load HomeFragment when clickcart_logo is clicked
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContent, new HomeFragment());  // Switch to HomeFragment
                transaction.commit();
            }
        });

        // Handle Cart click to load Cart
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);

//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear previous activities
//                finish();
            }
        });

        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Initialize views
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        TextView nameTextView = findViewById(R.id.productNameTextView);
        TextView priceTextView = findViewById(R.id.productPriceTextView);
        ImageView productImageView = findViewById(R.id.productImageView);
        TextView categoryTextView = findViewById(R.id.productCategoryTextView);

        // Get data from Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        double productPrice = intent.getDoubleExtra("productPrice", 0.0);
        String productImage = intent.getStringExtra("productImage");
        String productCategory = intent.getStringExtra("productCategory");

        // Bind data to views
        nameTextView.setText(productName);
        priceTextView.setText(String.format("$%.2f", productPrice));
        categoryTextView.setText(productCategory);

        // Load product image using Picasso
        loadProductImage(productImage, productImageView);

        // Set click listener to open full screen image
        productImageView.setOnClickListener(v -> {
            Intent fullScreenIntent = new Intent(ProductDetailActivity.this, FullScreenImageActivity.class);
            fullScreenIntent.putExtra("productImage", productImage);
            startActivity(fullScreenIntent);
        });

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Reload product details
                refreshProductDetails(productName, productPrice, productImage, productCategory);
                // Stop the refreshing indicator once done
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);

        // Create mock comments
        List<CommentData> comments = new ArrayList<>();
        comments.add(new CommentData("Great product!", "user1@example.com", "user1"));
        comments.add(new CommentData("I love this!", "user2@example.com", "user2"));
        comments.add(new CommentData("Could be better.", "user3@example.com", "user3"));
        comments.add(new CommentData("Amazing quality!", "user4@example.com", "user4"));

        // Log mock comments
        for (CommentData commentData : comments) {
            Log.d("CommentList", "Comment: " + commentData.getComment());
            Log.d("CommentList", "Comment Gmail: " + commentData.getCommentGmail());
            Log.d("CommentList", "Comment User: " + commentData.getCommentId());
        }

        // Set up the adapter
        CommentAdapter commentAdapter = new CommentAdapter(this, comments);
        commentsRecyclerView.setAdapter(commentAdapter);

        quantityTextView = findViewById(R.id.quantityTextView);
        buttonMinus = findViewById(R.id.buttonMinus);
        buttonPlus = findViewById(R.id.buttonPlus);

        // Set up button listeners
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) { // Ensure quantity does not go below 1
                    quantity--;
                    quantityTextView.setText(String.valueOf(quantity));
                }
            }
        });

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

    }

    private void loadProductImage(String productImage, ImageView productImageView) {
        Picasso.get()
                .load(productImage)
                .placeholder(R.drawable.person) // Placeholder image
                .error(R.drawable.logo_dark) // Error image
                .into(productImageView);
    }

    private void refreshProductDetails(String productName, double productPrice, String productImage, String productCategory) {
        // Optionally, you can add logic here to fetch updated product details
        // For now, we'll just reset the views with the same data
        TextView nameTextView = findViewById(R.id.productNameTextView);
        TextView priceTextView = findViewById(R.id.productPriceTextView);
        TextView categoryTextView = findViewById(R.id.productCategoryTextView);
        ImageView productImageView = findViewById(R.id.productImageView);

        nameTextView.setText(productName);
        priceTextView.setText(String.format("$%.2f", productPrice));
        categoryTextView.setText(productCategory);

        // Reload the product image (you may want to fetch the image again in a real app)
        loadProductImage(productImage, productImageView);
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

//    Intent intent = new Intent(ProductDetailActivity.this, Main.class);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear previous activities
//    startActivity(intent);
//    finish();
}

