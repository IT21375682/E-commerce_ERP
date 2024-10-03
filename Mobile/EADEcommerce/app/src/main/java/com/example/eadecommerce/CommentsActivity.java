package com.example.eadecommerce;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eadecommerce.adapter.CommentAdapter;
import com.example.eadecommerce.adapter.ProductCommentAdapter;
import com.example.eadecommerce.model.Comment;
import com.example.eadecommerce.model.ProductCommentData;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.JwtUtils;
import com.example.eadecommerce.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<ProductCommentData> comments = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton buttonBack;
    private String userId;
    private TextView noCommentsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        // Get the JWT token from SharedPreferences and Decode the token to get the user ID
        String token = JwtUtils.getTokenFromSharedPreferences(this);
        userId = JwtUtils.getUserIdFromToken(token);
        Log.d("userId", userId);

        buttonBack = findViewById(R.id.buttonBack);
        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noCommentsTextView = findViewById(R.id.noCommentsTextView);

        // Initialize adapter with an empty list and set it to RecyclerView
        commentAdapter = new CommentAdapter(this, comments);
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
                if (comments != null && !comments.isEmpty()) {  // Check for null or empty list
                    if (position == 0) {
                        // Sort by Newest
                        Collections.sort(comments, (c1, c2) -> c2.getDate().compareTo(c1.getDate()));
                    } else {
                        // Sort by Oldest
                        Collections.sort(comments, Comparator.comparing(ProductCommentData::getDate));
                    }
                    commentAdapter.updateComments(comments);  // Notify adapter of the change
                }
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
            fetchComments(userId);
            swipeRefreshLayout.setRefreshing(false);
        });

        // Fetch comments when the activity is created
        fetchComments(userId);
    }

    // Fetch comments from the API
    private void fetchComments(String userId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<ProductCommentData>> call = apiService.getCommentsByUserId(userId);

        call.enqueue(new Callback<List<ProductCommentData>>() {
            @Override
            public void onResponse(Call<List<ProductCommentData>> call, Response<List<ProductCommentData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    comments.clear(); // Clear the existing comments
                    comments.addAll(response.body()); // Add the new comments

                    if (comments.isEmpty()) {
                        // If no comments, show "No comments yet" and hide the RecyclerView
                        noCommentsTextView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        // Show the RecyclerView and hide the "No comments yet" message
                        noCommentsTextView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        commentAdapter.updateComments(comments);  // Update the adapter with new data
                    }

                } else {
                    Log.e("CommentsActivity", "Error fetching comments: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductCommentData>> call, Throwable t) {
                Log.e("CommentsActivity", "Failed to load comments", t);
            }
        });
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
