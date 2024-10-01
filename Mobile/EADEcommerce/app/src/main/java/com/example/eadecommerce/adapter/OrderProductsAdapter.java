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
import com.example.eadecommerce.model.CartItem;
import com.example.eadecommerce.model.SingleOrderProductItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.CartViewHolder> {
    private List<SingleOrderProductItem> cartItems;

    public OrderProductsAdapter(List<SingleOrderProductItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        SingleOrderProductItem item = cartItems.get(position);
        holder.itemNameTextView.setText(item.getProductName());
        holder.itemPriceTextView.setText(String.format("Price: $%.2f", item.getProductPrice()));
        holder.itemCountTextView.setText("Count: " + item.getCount());

        // Decode the base64 image string and set it to the ImageView
        if (item.getProductImg() != null && !item.getProductImg().isEmpty()) {
            byte[] decodedString = Base64.decode(item.getProductImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.itemImageView.setImageBitmap(decodedByte);
        } else {
            // Set a placeholder image or clear the ImageView if no image is available
            holder.itemImageView.setImageResource(R.drawable.placeholder);
        }

        // Handle item click to open ProductDetailActivity
        holder.itemLayout.setOnClickListener(v -> {
            Log.d("Checkout Adapter", "Item clicked");

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
