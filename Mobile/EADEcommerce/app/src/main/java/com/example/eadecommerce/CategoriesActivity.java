package com.example.eadecommerce;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eadecommerce.adapter.CategoryAdapter;
import com.example.eadecommerce.model.Category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private SearchView searchView;
    private Spinner spinnerSort;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton buttonBack;

    private List<Category> categoryList;
    private String currentSearchQuery = "";
    private String currentSortOption = "A-Z"; // Default sort option

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_categories);

        buttonBack = findViewById(R.id.buttonBack);
        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        recyclerView = findViewById(R.id.recyclerViewCategories);
        searchView = findViewById(R.id.searchView);
        spinnerSort = findViewById(R.id.spinnerCategoryFilter);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        // Set up mock data
        loadMockData();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(this, categoryList);
        recyclerView.setAdapter(categoryAdapter);

        // Handle sorting selection
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentSortOption = parentView.getItemAtPosition(position).toString();
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                currentSortOption = "A-Z"; // Default sort
                applyFilters();
            }
        });

        // Handle product search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                applyFilters();

                // Clear focus from the SearchView after submitting the query
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                applyFilters();
                return true;
            }
        });

        // Handle refresh action
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Reload mock data or fetch new data from server
            loadMockData();
            applyFilters();
            swipeRefreshLayout.setRefreshing(false); // Stop refresh animation
        });

        // Set a touch listener on the root layout to clear focus when user clicks outside the SearchView
        findViewById(R.id.parentLayout).setOnTouchListener((v, event) -> {
            // Clear focus if the touch event is outside the SearchView
            if (searchView.hasFocus()) {
                searchView.clearFocus();
            }
            return false; // Allow other touch events to be processed
        });
    }

    // Mock data function
    private void loadMockData() {
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Electronics", 100, "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg"));
        categoryList.add(new Category("Clothing", 200, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg"));
        categoryList.add(new Category("Books", 50, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg"));
        categoryList.add(new Category("Toys", 80, "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg"));
    }

    // Apply filters: search and sorting together
    private void applyFilters() {
        // Filter by search query
        List<Category> filteredList = new ArrayList<>();
        for (Category category : categoryList) {
            if (category.getName().toLowerCase().contains(currentSearchQuery.toLowerCase())) {
                filteredList.add(category);
            }
        }

        // Sort the filtered list
        if (currentSortOption.equals("A-Z")) {
            Collections.sort(filteredList, (c1, c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
        } else if (currentSortOption.equals("Z-A")) {
            Collections.sort(filteredList, (c1, c2) -> c2.getName().compareToIgnoreCase(c1.getName()));
        }

        // Update adapter with filtered and sorted list
        categoryAdapter.updateList(filteredList);
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

