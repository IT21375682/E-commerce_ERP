package com.example.eadecommerce;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eadecommerce.adapter.ProductAdapter;
import com.example.eadecommerce.fragments.HomeFragment;
import com.example.eadecommerce.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CategoryProductsActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private RecyclerView recyclerViewProducts;
    private List<Product> productList;
    private List<Product> filteredProductList;
    private DrawerLayout drawerLayout;
    private ImageButton arrowButton, buttonBack, buttonCart;
    private ImageView clickcartHome;

    private String currentSearchQuery = "";
    private String currentSortOption = "Default";

    // New filter variables
    private EditText editTextMinPrice;
    private EditText editTextMaxPrice;
    private RadioGroup radioGroupRatings;
    private TextView cartTitleTextView;

    private RadioButton radioRating1, radioRating2, radioRating3, radioRating4;

    private Button buttonClearPriceFilter, buttonClearRatingFilter;

    private int selectedRating = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_products);

        // Retrieve the category name passed from the adapter
        String categoryName = getIntent().getStringExtra("categoryName");
        cartTitleTextView = findViewById(R.id.cartTitleTextView);
        cartTitleTextView.setText(categoryName);

        buttonBack = findViewById(R.id.buttonBack);
        clickcartHome = findViewById(R.id.clickcartHome);
        buttonCart = findViewById(R.id.buttonCart);

        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        clickcartHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Main activity (Home)
                Intent intent = new Intent(CategoryProductsActivity.this, Main.class);
                startActivity(intent);
            }
        });

        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Cart activity
                Intent intent = new Intent(CategoryProductsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // Initialize the DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Initialize arrow button and set onClickListener
        arrowButton = findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // Initialize views
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns

        Spinner spinnerSort = findViewById(R.id.spinnerSort);
        SearchView searchViewProducts = findViewById(R.id.searchViewProducts);
        editTextMinPrice = findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = findViewById(R.id.editTextMaxPrice);
        radioGroupRatings = findViewById(R.id.radioGroupRatings);

        radioRating1 = findViewById(R.id.radioRating1);
        radioRating2 = findViewById(R.id.radioRating2);
        radioRating3 = findViewById(R.id.radioRating3);
        radioRating4 = findViewById(R.id.radioRating4);

        // Set OnClickListeners for each RadioButton
        setRatingListeners();

        // Initialize the buttons
        buttonClearPriceFilter = findViewById(R.id.buttonClearPriceFilter);
        buttonClearRatingFilter = findViewById(R.id.buttonClearRatingFilter);

        // Clear filters button setup
        setClearFilterButtons();

        // Load product list
        loadProducts();

        // Setup the swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Clear search and filters
            clearFilters(searchViewProducts, spinnerSort);

            // Reload the products
            loadProducts();

            // Stop the refresh animation
            swipeRefreshLayout.setRefreshing(false);
        });

        // Handle sorting selection
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentSortOption = parentView.getItemAtPosition(position).toString();
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                currentSortOption = "Default";
                applyFilters();
            }
        });

        // Handle product search
        searchViewProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currentSearchQuery = query;
                applyFilters();
                searchViewProducts.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchQuery = newText;
                applyFilters();
                return true;
            }
        });

        // Set listeners for price edit texts to trigger applyFilters when text changes
        setPriceEditTextListeners();

        // Set a touch listener on the parent view to clear focus when user clicks outside the SearchView
        findViewById(R.id.swipeRefreshLayout).setOnTouchListener((v, event) -> {
            // Clear focus if the touch event is outside the SearchView
            searchViewProducts.clearFocus();
            return false;
        });
    }

    // Method to set listeners for radio buttons for ratings
    private void setRatingListeners() {
        radioRating1.setOnClickListener(v -> {
            selectedRating = 1;
            applyFilters();
        });
        radioRating2.setOnClickListener(v -> {
            selectedRating = 2;
            applyFilters();
        });
        radioRating3.setOnClickListener(v -> {
            selectedRating = 3;
            applyFilters();
        });
        radioRating4.setOnClickListener(v -> {
            selectedRating = 4;
            applyFilters();
        });
    }

    // Method to set listeners for clear filter buttons
    private void setClearFilterButtons() {
        buttonClearPriceFilter.setOnClickListener(v -> {
            editTextMinPrice.setText("");
            editTextMaxPrice.setText("");
            applyFilters(); // Trigger apply filters
        });

        buttonClearRatingFilter.setOnClickListener(v -> {
            radioGroupRatings.clearCheck();
            selectedRating = 0; // Reset selected rating
            applyFilters(); // Trigger apply filters
        });
    }

    // Method to clear filters
    private void clearFilters(SearchView searchView, Spinner spinner) {
        searchView.setQuery("", false);
        spinner.setSelection(0); // Assuming first item is "Default"
        editTextMinPrice.setText("");
        editTextMaxPrice.setText("");
        radioGroupRatings.clearCheck();
    }

    // Set listeners for price edit texts
    private void setPriceEditTextListeners() {
        editTextMinPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                applyFilters();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        editTextMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                applyFilters();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    // Method to apply the filters
    private void applyFilters() {
        List<Product> tempFilteredList = new ArrayList<>(productList);

        // Step 1: Filter by search query
        if (!currentSearchQuery.isEmpty()) {
            List<Product> searchFilteredList = new ArrayList<>();
            for (Product product : tempFilteredList) {
                if (product.getName().toLowerCase().contains(currentSearchQuery.toLowerCase()) || product.getVendor().toLowerCase().contains(currentSearchQuery.toLowerCase())) {
                    searchFilteredList.add(product);
                }
            }
            tempFilteredList = searchFilteredList;
        }

        // Step 2: Filter by price range
        double minPrice = getMinPrice();
        double maxPrice = getMaxPrice();
        if (minPrice >= 0 || maxPrice >= 0) {
            List<Product> priceFilteredList = new ArrayList<>();
            for (Product product : tempFilteredList) {
                if ((minPrice < 0 || product.getPrice() >= minPrice) && (maxPrice < 0 || product.getPrice() <= maxPrice)) {
                    priceFilteredList.add(product);
                }
            }
            tempFilteredList = priceFilteredList;
        }

        // Step 3: Filter by rating
        if (selectedRating > 0) {
            List<Product> ratingFilteredList = new ArrayList<>();
            for (Product product : tempFilteredList) {
                if (product.getRating() >= selectedRating) {
                    ratingFilteredList.add(product);
                }
            }
            tempFilteredList = ratingFilteredList;
        }

        // Step 5: Sort the filtered list
        switch (currentSortOption) {
            case "A-Z":
                Collections.sort(tempFilteredList, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
                break;
            case "Z-A":
                Collections.sort(tempFilteredList, (p1, p2) -> p2.getName().compareToIgnoreCase(p1.getName()));
                break;
            case "Price: Low to High":
                Collections.sort(tempFilteredList, Comparator.comparingDouble(Product::getPrice));
                break;
            case "Price: High to Low":
                Collections.sort(tempFilteredList, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
                break;
            case "Default":
                break; // No sorting if default
        }


        // Update the filtered list and notify the adapter
        filteredProductList.clear();
        filteredProductList.addAll(tempFilteredList);
        productAdapter.notifyDataSetChanged();
    }

    // Helper method to get the minimum price
    private double getMinPrice() {
        String minPriceText = editTextMinPrice.getText().toString();
        return minPriceText.isEmpty() ? -1 : Double.parseDouble(minPriceText);
    }

    // Helper method to get the maximum price
    private double getMaxPrice() {
        String maxPriceText = editTextMaxPrice.getText().toString();
        return maxPriceText.isEmpty() ? -1 : Double.parseDouble(maxPriceText);
    }

    // Method to load products
    private void loadProducts() {

        // Load product list
        productList = new ArrayList<>();
        productList.add(new Product("Product 1", 19.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 1",0, "Test"));
        productList.add(new Product("Product 2", 29.99, "https://wallpapers.com/images/hd/shin-chan-in-black-eyz87euqvvyrlihs.jpg", "Category 1", 5, "Test"));
        productList.add(new Product("Product 3", 39.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 1", 3, "Test"));
        productList.add(new Product("Product 4", 31.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 2", 3, "Test"));
        productList.add(new Product("Product 5", 43.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 2",5, "Dummy"));
        productList.add(new Product("Product 6", 35.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 3",2, "Dummy"));

        // Initially, all products are displayed
        filteredProductList = new ArrayList<>(productList);
        productAdapter = new ProductAdapter(this, filteredProductList);
        recyclerViewProducts.setAdapter(productAdapter);

        // Apply filters to the loaded products
        applyFilters();
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

