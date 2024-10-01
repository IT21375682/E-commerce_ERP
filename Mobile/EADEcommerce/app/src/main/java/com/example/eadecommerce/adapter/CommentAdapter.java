package com.example.eadecommerce.adapter;

// CommentAdapter.java
import android.annotation.SuppressLint;
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

import com.example.eadecommerce.R;
import com.example.eadecommerce.model.Comment;
import com.example.eadecommerce.model.ProductCommentData;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<ProductCommentData> comments;

    public CommentAdapter(List<ProductCommentData> comments) {
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductCommentData comment = comments.get(position);
        holder.productNameTextView.setText(comment.getProductName());
        holder.vendorNameTextView.setText(comment.getVendorName());
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

        // Handle profileEditImage click to toggle visibility
        holder.profileEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the TextView and profileEditImage, show EditText and profileupdateImage
                holder.commentTextView.setVisibility(View.GONE);
                holder.profileEditImage.setVisibility(View.GONE);
                holder.commentEditTextInput.setVisibility(View.VISIBLE);
                holder.profileupdateImage.setVisibility(View.VISIBLE);
            }
        });

        // Handle profileupdateImage click to save the comment and toggle views back
        holder.profileupdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update the TextView with new comment from EditText
                String updatedComment = holder.commentEditTextInput.getText().toString();
                holder.commentTextView.setText(updatedComment);

                // Hide the EditText and profileupdateImage, show TextView and profileEditImage again
                holder.commentTextView.setVisibility(View.VISIBLE);
                holder.profileEditImage.setVisibility(View.VISIBLE);
                holder.commentEditTextInput.setVisibility(View.GONE);
                holder.profileupdateImage.setVisibility(View.GONE);

                // Optionally, update the comment in the data model or send the update to the server
                comments.get(position).setCommentText(updatedComment);
                notifyItemChanged(position);  // Notify adapter about the change
            }
        });
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView vendorNameTextView;
        TextView commentTextView;
        TextView dateTextView;
        TextView productNameTextView;
        RatingBar ratingBar;
        ImageView profileEditImage;
        ImageView profileupdateImage;  // Added this to the ViewHolder
        EditText commentEditTextInput;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            vendorNameTextView = itemView.findViewById(R.id.vendorNameTextView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            profileEditImage = itemView.findViewById(R.id.profileEditImage);
            profileupdateImage = itemView.findViewById(R.id.profileupdateImage);  // Make sure this is initialized
            commentEditTextInput = itemView.findViewById(R.id.commentEditTextInput);
        }
    }

    // Method to update comments
    public void updateComments(List<ProductCommentData> newComments) {
        comments = newComments;
        notifyDataSetChanged();
    }
}
