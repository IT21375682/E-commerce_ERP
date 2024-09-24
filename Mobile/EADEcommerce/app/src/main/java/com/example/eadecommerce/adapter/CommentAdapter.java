package com.example.eadecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.model.CommentData;
import com.example.eadecommerce.R;


import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context context;
    private List<CommentData> comments;
    private int currentCommentIndex = 0;

    public CommentAdapter(Context context, List<CommentData> comments) {
        this.context = context;
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
        CommentData item = comments.get(currentCommentIndex);
        holder.commentText.setText(item.getComment());
        holder.commentUserText.setText(item.getCommentGmail());

        holder.arrowLeft.setOnClickListener(v -> showPreviousComment());
        holder.arrowRight.setOnClickListener(v -> showNextComment());
    }

    private void showPreviousComment() {
        if (currentCommentIndex > 0) {
            currentCommentIndex--;
            notifyDataSetChanged();
        }
    }

    private void showNextComment() {
        if (currentCommentIndex < comments.size() - 1) {
            currentCommentIndex++;
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return 1; // Always display one comment at a time
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        public TextView commentText;
        public TextView commentUserText;
        public ImageButton arrowLeft;
        public ImageButton arrowRight;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentText = itemView.findViewById(R.id.commentText);
            commentUserText = itemView.findViewById(R.id.commentUserText);
            arrowLeft = itemView.findViewById(R.id.arrowLeft);
            arrowRight = itemView.findViewById(R.id.arrowRight);
        }
    }
}