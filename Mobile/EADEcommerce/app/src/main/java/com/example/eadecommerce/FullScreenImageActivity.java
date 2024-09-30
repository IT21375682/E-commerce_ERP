package com.example.eadecommerce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
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
//        Picasso.get()
//                .load(productImage)
//                .into(fullScreenImageView);

        Bitmap decodedImage = decodeBase64ToBitmap(productImage);
        fullScreenImageView.setImageBitmap(decodedImage);

        // Set click listener for the close button
        closeButton.setOnClickListener(v -> finish());
    }

    private Bitmap decodeBase64ToBitmap(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}