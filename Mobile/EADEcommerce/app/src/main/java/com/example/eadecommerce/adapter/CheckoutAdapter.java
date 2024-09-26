package com.example.eadecommerce.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.CheckoutActivity;
import com.example.eadecommerce.ProductDetailActivity;
import com.example.eadecommerce.R;
import com.example.eadecommerce.model.CartItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CartViewHolder> {
    private List<CartItem> cartItems;

    public CheckoutAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.itemNameTextView.setText(item.getName());
        holder.itemPriceTextView.setText(String.format("Price: $%.2f", item.getPrice()));
        holder.itemCountTextView.setText("Count: " + item.getCount());
        // Load image using an image loading library like Picasso
        Picasso.get()
                .load(item.getImageUrl())
                .placeholder(R.drawable.logo_dark)
                .error(R.drawable.logo_dark)
                .into(holder.itemImageView);

        // Handle item click to open ProductDetailActivity
        holder.itemLayout.setOnClickListener(v -> {
            Log.d("Checkout Adapter", "Item clicked");

            // Create an Intent to start ProductDetailActivity
            Intent intent = new Intent(holder.itemLayout.getContext(), ProductDetailActivity.class);

            // Pass product details to ProductDetailActivity
            intent.putExtra("productName", item.getName());
            intent.putExtra("productPrice", item.getPrice());
            intent.putExtra("productImageUrl", item.getImageUrl());
            intent.putExtra("productCategory", "Default");
            intent.putExtra("productCount", item.getCount());

            // Start the ProductDetailActivity
            holder.itemLayout.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView;
        public TextView itemPriceTextView;
        public TextView itemCountTextView;
        public ImageView itemImageView;
        public LinearLayout itemLayout;

        public CartViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            itemCountTextView = itemView.findViewById(R.id.itemCountTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }
}
