package com.example.eadecommerce.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.ProductDetailActivity;
import com.example.eadecommerce.R;
import com.example.eadecommerce.model.CartProductResponse;

import java.util.List;

/**
 * The CheckoutAdapter class is a RecyclerView adapter for displaying cart items during checkout.
 * It binds cart item data to the views and handles item click events to navigate to product details.
 */
public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.CartViewHolder> {
    private List<CartProductResponse> cartItems;

    /**
     * Constructor to initialize the adapter with a list of cart items.
     * @param cartItems The list of cart items.
     */
    public CheckoutAdapter(List<CartProductResponse> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartProductResponse item = cartItems.get(position);
        holder.itemNameTextView.setText(item.getProductName());
        holder.itemPriceTextView.setText(String.format("Price: LKR %.2f", item.getPrice()));
        holder.itemCountTextView.setText("Count: " + item.getCount());

        // Decode the base64 image string and set it to the ImageView
        if (item.getProductImage() != null && !item.getProductImage().isEmpty()) {
            byte[] decodedString = Base64.decode(item.getProductImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.itemImageView.setImageBitmap(decodedByte);
        } else {
            holder.itemImageView.setImageResource(R.drawable.placeholder);
        }

        holder.itemLayout.setOnClickListener(v -> {
            Log.d("Checkout Adapter", "Item clicked");
            Intent intent = new Intent(holder.itemLayout.getContext(), ProductDetailActivity.class);
            intent.putExtra("productId", item.getProductId());
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
