package com.example.eadecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class FullScreenImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView fullScreenImageView = findViewById(R.id.fullScreenImageView);
        ImageView closeButton = findViewById(R.id.closeButton);

        // Get image URL from intent
        Intent intent = getIntent();
        String productImage = intent.getStringExtra("productImage");

        // Load image using Picasso
        Picasso.get()
                .load(productImage)
                .into(fullScreenImageView);

        // Set click listener for the close button
        closeButton.setOnClickListener(v -> finish());
    }
}