package com.example.eadecommerce.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.CartActivity;
import com.example.eadecommerce.ProductDetailActivity;
import com.example.eadecommerce.R;
import com.example.eadecommerce.model.CartItem;
import com.example.eadecommerce.model.CartProductResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartProductResponse> cartItems;

    public CartAdapter(List<CartProductResponse> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        CartProductResponse item = cartItems.get(position);
        holder.itemNameTextView.setText(item.getProductName());
        holder.itemPriceTextView.setText(String.format("Price: $%.2f", item.getPrice()));
        holder.itemCountTextView.setText(String.valueOf(item.getCount()));
        holder.itemCountChangeTextView.setText(String.valueOf(item.getCount()));

        // Decode the base64 image string and set it to the ImageView
        if (item.getProductImage() != null && !item.getProductImage().isEmpty()) {
            byte[] decodedString = Base64.decode(item.getProductImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.itemImageView.setImageBitmap(decodedByte);
        } else {
            // Set a placeholder image or clear the ImageView if no image is available
            holder.itemImageView.setImageResource(R.drawable.placeholder); // Replace with your placeholder image
        }

        // Set click listeners for the + and - buttons
        holder.plusButton.setOnClickListener(v -> {
            item.setCount(item.getCount() + 1);
            holder.itemCountChangeTextView.setText(String.valueOf(item.getCount()));
            // Notify activity to show the update button
            ((CartActivity) holder.itemView.getContext()).showUpdateButton();
        });

        holder.minusButton.setOnClickListener(v -> {
            if (item.getCount() > 0) {
                item.setCount(item.getCount() - 1);
                holder.itemCountChangeTextView.setText(String.valueOf(item.getCount()));
                // Notify activity to show the update button
                ((CartActivity) holder.itemView.getContext()).showUpdateButton();
            }
        });

        // Handle item click to open ProductDetailActivity
        holder.itemLayout.setOnClickListener(v -> {
            Log.d("Cart Adapter", "Item clicked");
            // Create an Intent to start ProductDetailActivity
            Intent intent = new Intent(holder.itemLayout.getContext(), ProductDetailActivity.class);

            // Pass product details to ProductDetailActivity
            intent.putExtra("productId", item.getProductId());

            // Start the ProductDetailActivity
            holder.itemLayout.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void removeItem(int position) {
        cartItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartItems.size());
    }

    public void updateItemCount(int position, int count) {
        cartItems.get(position).setCount(count);
        notifyItemChanged(position);
    }


    public static class CartViewHolder extends RecyclerView.ViewHolder {
        public TextView itemNameTextView;
        public TextView itemPriceTextView;
        public TextView itemCountTextView;
        public TextView itemCountChangeTextView;
        public ImageView itemImageView;
        public Button plusButton;
        public Button minusButton;
        public LinearLayout itemLayout;

        public CartViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);
            itemPriceTextView = itemView.findViewById(R.id.itemPriceTextView);
            itemCountTextView = itemView.findViewById(R.id.itemCountTextView);
            itemCountChangeTextView = itemView.findViewById(R.id.itemCountChangeTextView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            plusButton = itemView.findViewById(R.id.plusButton);
            minusButton = itemView.findViewById(R.id.minusButton);
            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }
}
