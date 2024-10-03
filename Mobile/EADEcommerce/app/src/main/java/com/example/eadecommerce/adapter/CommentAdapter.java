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
import com.example.eadecommerce.VendorCommentsActivity;
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
 * Adapter class for displaying comments in a RecyclerView.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<ProductCommentData> comments;
    private Context context;

    public CommentAdapter(Context context, List<ProductCommentData> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the comment item layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // Bind the comment data to the view holder
        ProductCommentData comment = comments.get(position);
        holder.productNameTextView.setText(comment.getProductName());
        holder.vendorNameTextView.setText(comment.getVendorName());
        holder.commentTextView.setText(comment.getCommentText());
        holder.commentEditTextInput.setText(comment.getCommentText());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = dateFormat.format(comment.getDate());
        holder.dateTextView.setText(formattedDate);
        holder.ratingBar.setRating(comment.getRating());

        Log.d("Adapter", comment.getUsername());
        Log.d("Adapter", comment.getCommentText());

        final String originalComment = comment.getCommentText();

        holder.profileEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of comment editing views
                holder.commentTextView.setVisibility(View.GONE);
                holder.profileEditImage.setVisibility(View.GONE);
                holder.commentEditTextInput.setVisibility(View.VISIBLE);
                holder.profileupdateImage.setVisibility(View.VISIBLE);
                holder.profileupdateCancelImage.setVisibility(View.VISIBLE);
            }
        });

        holder.profileupdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the updated comment
                String updatedComment = holder.commentEditTextInput.getText().toString();
                holder.commentTextView.setText(updatedComment);
                holder.commentTextView.setVisibility(View.VISIBLE);
                holder.profileEditImage.setVisibility(View.VISIBLE);
                holder.commentEditTextInput.setVisibility(View.GONE);
                holder.profileupdateImage.setVisibility(View.GONE);
                holder.profileupdateCancelImage.setVisibility(View.GONE);

                comments.get(position).setCommentText(updatedComment);
                notifyItemChanged(position);

                Comment updatedCommentObj = getComment(updatedComment);
                postComment(updatedCommentObj);
            }

            private @NonNull Comment getComment(String updatedComment) {
                // Create a Comment object for the updated comment
                Comment updatedCommentObj = new Comment();
                updatedCommentObj.setId(comment.getId());
                updatedCommentObj.setUserId(comment.getUserId());
                updatedCommentObj.setVendorId(comment.getVendorId());
                updatedCommentObj.setProductId(comment.getProductId());
                updatedCommentObj.setRating((int) holder.ratingBar.getRating());
                updatedCommentObj.setCommentText(updatedComment);
                return updatedCommentObj;
            }
        });

        holder.profileupdateCancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restore the original comment
                holder.commentTextView.setText(originalComment);
                holder.commentEditTextInput.setText(originalComment);
                holder.commentTextView.setVisibility(View.VISIBLE);
                holder.profileEditImage.setVisibility(View.VISIBLE);
                holder.commentEditTextInput.setVisibility(View.GONE);
                holder.profileupdateImage.setVisibility(View.GONE);
                holder.profileupdateCancelImage.setVisibility(View.GONE);
            }
        });

        holder.productNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ProductDetailActivity for the selected product
                Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                intent.putExtra("productId", comment.getProductId());
                v.getContext().startActivity(intent);
            }
        });

        holder.vendorNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start VendorCommentsActivity for the selected vendor
                Intent intent = new Intent(v.getContext(), VendorCommentsActivity.class);
                intent.putExtra("vendorId", comment.getVendorId());
                intent.putExtra("vendorName", comment.getVendorName());
                v.getContext().startActivity(intent);
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
        ImageView profileupdateImage;
        ImageView profileupdateCancelImage;
        EditText commentEditTextInput;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            vendorNameTextView = itemView.findViewById(R.id.vendorNameTextView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            profileEditImage = itemView.findViewById(R.id.profileEditImage);
            profileupdateImage = itemView.findViewById(R.id.profileupdateImage);
            profileupdateCancelImage = itemView.findViewById(R.id.profileupdateCancelImage);
            commentEditTextInput = itemView.findViewById(R.id.commentEditTextInput);
        }
    }

    // Method to update comments
    public void updateComments(List<ProductCommentData> newComments) {
        comments = newComments;
        notifyDataSetChanged();
    }

    private void postComment(Comment comment) {
        // Post or update the comment via API
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Comment> call = apiService.addOrUpdateComment(comment);

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Log.d("CommentAdapter", "Comment posted successfully");
                    View rootView = ((Activity) context).findViewById(android.R.id.content);
                    Snackbar.make(rootView, "Comment updated successfully!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.e("CommentAdapter", "Failed to post comment: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("CommentAdapter", "Error posting comment", t);
            }
        });
    }
}
