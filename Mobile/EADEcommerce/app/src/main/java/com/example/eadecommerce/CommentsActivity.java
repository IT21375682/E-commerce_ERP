package com.example.eadecommerce;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eadecommerce.adapter.CommentAdapter;
import com.example.eadecommerce.model.Comment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        buttonBack = findViewById(R.id.buttonBack);
        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentList = getMockComments();
        commentAdapter = new CommentAdapter(commentList);
        recyclerView.setAdapter(commentAdapter);

        // Setup Spinner
        Spinner spinner = findViewById(R.id.spinnerDateFilter);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.date_filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position == 0) {
                    // Sort by Newest
                    Collections.sort(commentList, (c1, c2) -> c2.getDate().compareTo(c1.getDate()));
                } else {
                    // Sort by Oldest
                    Collections.sort(commentList, Comparator.comparing(Comment::getDate));
                }
                commentAdapter.updateComments(commentList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });

        // Setup SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Reload the list
            commentList = getMockComments(); // This can be modified to fetch from an API
            commentAdapter.updateComments(commentList);
            swipeRefreshLayout.setRefreshing(false); // Stop refreshing
        });
    }

    private List<Comment> getMockComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("User1", "This is a comment from User1", "2024-09-24"));
        comments.add(new Comment("User2", "Comment from User2", "2024-09-23"));
        comments.add(new Comment("User3", "User3's comment here", "2024-09-22"));
        comments.add(new Comment("User4", "Another comment from User4", "2024-09-21"));
        comments.add(new Comment("User5", "User5 commenting", "2024-09-20"));
        return comments;
    }

    @Override
    public void onBackPressed() {
        Log.d("TAG", "Debug message");
        // Check if the fragment manager has a back stack
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            Log.d("TAG", "If Debug message");
            getSupportFragmentManager().popBackStack(); // Go back to previous fragment
        } else {
            Log.d("TAG", "Else Debug message");
            finish(); // Close activity if no fragments are in the stack
        }
    }

}
