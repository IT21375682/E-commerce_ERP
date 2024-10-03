package com.example.eadecommerce.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.ProductDetailActivity;
import com.example.eadecommerce.R;
import com.example.eadecommerce.model.Comment;
import com.example.eadecommerce.model.ProductCommentData;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Adapter class for displaying vendor comments in a RecyclerView.
 */
public class VendorCommentAdapter extends RecyclerView.Adapter<VendorCommentAdapter.CommentViewHolder> {
    private List<ProductCommentData> comments;
    private Context context;

    public VendorCommentAdapter(Context context, List<ProductCommentData> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each comment item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind the data for the current comment
        ProductCommentData comment = comments.get(position);
        holder.productNameTextView.setText(comment.getProductName());
        holder.vendorNameTextView.setText(comment.getUsername());
        holder.commentTextView.setText(comment.getCommentText());
        holder.commentEditTextInput.setText(comment.getCommentText());

        // Format the date before setting it to the TextView
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(comment.getDate());
        holder.dateTextView.setText(formattedDate);
        holder.ratingBar.setRating(comment.getRating());

        // Log for debugging
        Log.d("Adapter", comment.getUsername());
        Log.d("Adapter", comment.getCommentText());

        // Handle product name click to start ProductDetailActivity with productId
        holder.productNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start ProductDetailActivity
                Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                intent.putExtra("productId", comment.getProductId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comments.size(); // Return the total number of comments
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView vendorNameTextView;
        TextView commentTextView;
        TextView dateTextView;
        TextView productNameTextView;
        RatingBar ratingBar;
        EditText commentEditTextInput;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views for the comment item
            vendorNameTextView = itemView.findViewById(R.id.vendorNameTextView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            commentEditTextInput = itemView.findViewById(R.id.commentEditTextInput);
        }
    }

    // Method to update comments
    public void updateComments(List<ProductCommentData> newComments) {
        comments = newComments;
        notifyDataSetChanged();
    }

}
