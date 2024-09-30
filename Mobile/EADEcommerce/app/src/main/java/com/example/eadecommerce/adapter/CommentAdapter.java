package com.example.eadecommerce.adapter;

// CommentAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.R;
import com.example.eadecommerce.model.Comment;
import com.example.eadecommerce.model.ProductCommentData;

import java.util.List;

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
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        ProductCommentData comment = comments.get(position);
        holder.vendorNameTextView.setText(comment.getUsername());
        holder.commentTextView.setText(comment.getCommentText());
        holder.dateTextView.setText((CharSequence) comment.getDate());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView vendorNameTextView;
        TextView commentTextView;
        TextView dateTextView;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            vendorNameTextView = itemView.findViewById(R.id.vendorNameTextView);
            commentTextView = itemView.findViewById(R.id.commentTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }
    }

    // Method to update comments
    public void updateComments(List<ProductCommentData> newComments) {
        comments = newComments;
        notifyDataSetChanged();
    }
}
