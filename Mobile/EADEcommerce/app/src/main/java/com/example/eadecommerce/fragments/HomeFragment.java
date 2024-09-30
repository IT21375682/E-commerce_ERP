package com.example.eadecommerce.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eadecommerce.R;
import com.example.eadecommerce.adapter.ProductAdapter;
import com.example.eadecommerce.model.Product;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.RetrofitClient;
import com.example.eadecommerce.responses.LoginResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ProductAdapter productAdapter;
    private RecyclerView recyclerViewProducts;
    private List<Product> productList;
    private List<Product> filteredProductList;
    private DrawerLayout drawerLayout;
    private ImageButton arrowButton;

    private String currentCategory = "All";
    private String currentSearchQuery = "";
    private String currentSortOption = "Default";

    // New filter variables
    private EditText editTextMinPrice;
    private EditText editTextMaxPrice;
    private RadioGroup radioGroupRatings;
    private CheckBox checkBoxCategory1;
    private CheckBox checkBoxCategory2;
    private CheckBox checkBoxCategory3;
    private CheckBox checkBoxCategory4;
    private RadioButton radioRating1, radioRating2, radioRating3, radioRating4;
    private TextView textViewNoProducts;

    private Button buttonClearPriceFilter;
    private Button buttonClearRatingFilter;
    private Button buttonClearCategoryFilter;
    private LinearLayout categoryContainer, searchOutLayout;

    private int selectedRating = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the DrawerLayout
        drawerLayout = view.findViewById(R.id.drawer_layout);

        // Initialize arrow button and set onClickListener
        arrowButton = view.findViewById(R.id.arrowButton);
        arrowButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        // Initialize views
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
        textViewNoProducts = view.findViewById(R.id.textViewNoProducts);
        searchOutLayout = view.findViewById(R.id.searchOutLayout);
        Spinner spinnerSort = view.findViewById(R.id.spinnerSort);
        SearchView searchViewProducts = view.findViewById(R.id.searchViewProducts);
        editTextMinPrice = view.findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = view.findViewById(R.id.editTextMaxPrice);
        radioGroupRatings = view.findViewById(R.id.radioGroupRatings);
//        checkBoxCategory1 = view.findViewById(R.id.checkBoxCategory1);
//        checkBoxCategory2 = view.findViewById(R.id.checkBoxCategory2);
//        checkBoxCategory3 = view.findViewById(R.id.checkBoxCategory3);
//        checkBoxCategory4 = view.findViewById(R.id.checkBoxCategory4);

        radioRating1 = view.findViewById(R.id.radioRating1);
        radioRating2 = view.findViewById(R.id.radioRating2);
        radioRating3 = view.findViewById(R.id.radioRating3);
        radioRating4 = view.findViewById(R.id.radioRating4);

        // Set OnClickListeners for each RadioButton
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

        categoryContainer = view.findViewById(R.id.categoryContainer);

        // Add OnCheckedChangeListeners to category checkboxes
//        checkBoxCategory1.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());
//        checkBoxCategory2.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());
//        checkBoxCategory3.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());
//        checkBoxCategory4.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());


        // Initialize the buttons
        buttonClearPriceFilter = view.findViewById(R.id.buttonClearPriceFilter);
        buttonClearRatingFilter = view.findViewById(R.id.buttonClearRatingFilter);
        buttonClearCategoryFilter = view.findViewById(R.id.buttonClearCategoryFilter);

        // Clear Price Filter Button
        buttonClearPriceFilter.setOnClickListener(v -> {
            editTextMinPrice.setText("");
            editTextMaxPrice.setText("");
            applyFilters(); // Trigger apply filters
        });

        // Clear Rating Filter Button
        buttonClearRatingFilter.setOnClickListener(v -> {
            radioGroupRatings.clearCheck();
            selectedRating = 0; // Reset selected rating
            applyFilters(); // Trigger apply filters
        });

        // Clear Category Filter Button
        buttonClearCategoryFilter.setOnClickListener(v -> {
            clearCategoryFilters(); // Uncheck all category checkboxes
            applyFilters(); // Trigger apply filters
        });

        // Load product list
        loadProducts();

        // Setup the swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Remove search and filters
            searchViewProducts.setQuery("", false);
            spinnerSort.setSelection(0); // Assuming first item is "Default"
            selectedRating = 0;
            editTextMinPrice.setText("");
            editTextMaxPrice.setText("");
            radioGroupRatings.clearCheck();
            clearCategoryFilters();
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

                // Clear focus from the SearchView after submitting the query
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
        editTextMinPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                applyFilters();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        editTextMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                applyFilters();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        // Set a touch listener on the parent view to clear focus when user clicks outside the SearchView
        view.setOnTouchListener((View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) -> {
            // Clear focus if the touch event is outside the SearchView
            searchViewProducts.clearFocus();
            return false;
        });
        recyclerViewProducts.setOnTouchListener((View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) -> {
            // Clear focus if the touch event is outside the SearchView
            searchViewProducts.clearFocus();
            return false;
        });
        searchOutLayout.setOnTouchListener((View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) -> {
            // Clear focus if the touch event is outside the SearchView
            searchViewProducts.clearFocus();
            return false;
        });

        return view;
    }

    // This function applies the category, search, price, rating, and sorting filters
    private void applyFilters() {
        if (productList == null || productList.isEmpty()) {
            // Product list is not yet loaded, so return early
            return;
        }

        List<Product> tempFilteredList = new ArrayList<>(productList);

        // Step 1: Filter by search query
        if (!currentSearchQuery.isEmpty()) {
            List<Product> searchFilteredList = new ArrayList<>();
            for (Product product : tempFilteredList) {
                if (product.getName().toLowerCase().contains(currentSearchQuery.toLowerCase()) || product.getVendorName().toLowerCase().contains(currentSearchQuery.toLowerCase())) {
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
                if (product.getRating() > selectedRating) {
                    ratingFilteredList.add(product);
                }
            }
            tempFilteredList = ratingFilteredList;
        }

        // Step 4: Filter by categories
        List<String> selectedCategories = getSelectedCategories();
        if (!selectedCategories.isEmpty()) {
            List<Product> categoryFilteredList = new ArrayList<>();
            for (Product product : tempFilteredList) {
                if (selectedCategories.contains(product.getProductCategoryName())) {
                    categoryFilteredList.add(product);
                }
            }
            tempFilteredList = categoryFilteredList;
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

        // Check if there are no filtered products, and display the "No Products" message
        if (filteredProductList.isEmpty()) {
            textViewNoProducts.setVisibility(View.VISIBLE);
            recyclerViewProducts.setVisibility(View.GONE);
        } else {
            textViewNoProducts.setVisibility(View.GONE);
            recyclerViewProducts.setVisibility(View.VISIBLE);
        }
    }

    private double getMinPrice() {
        String minPriceStr = editTextMinPrice.getText().toString();
        if (!minPriceStr.isEmpty()) {
            return Double.parseDouble(minPriceStr);
        }
        return -1; // Indicate no minimum price filter
    }

    private double getMaxPrice() {
        String maxPriceStr = editTextMaxPrice.getText().toString();
        if (!maxPriceStr.isEmpty()) {
            return Double.parseDouble(maxPriceStr);
        }
        return -1; // Indicate no maximum price filter
    }


    //    private List<String> getSelectedCategories() {
//        List<String> selectedCategories = new ArrayList<>();
//        if (checkBoxCategory1.isChecked()) selectedCategories.add("Category 1");
//        if (checkBoxCategory2.isChecked()) selectedCategories.add("Category 2");
//        if (checkBoxCategory3.isChecked()) selectedCategories.add("Category 3");
//        if (checkBoxCategory4.isChecked()) selectedCategories.add("Category 4");
//        return selectedCategories;
//    }
//
//    private void clearCategoryFilters() {
//        checkBoxCategory1.setChecked(false);
//        checkBoxCategory2.setChecked(false);
//        checkBoxCategory3.setChecked(false);
//        checkBoxCategory4.setChecked(false);
//    }

    private List<String> getSelectedCategories() {
        List<String> selectedCategories = new ArrayList<>();

        // Iterate through all child views in the checkbox container
        for (int i = 0; i < categoryContainer.getChildCount(); i++) {
            View child = categoryContainer.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) child;
                // If the checkbox is checked, add its text to the selected categories
                if (checkBox.isChecked()) {
                    selectedCategories.add(checkBox.getText().toString());
                }
            }
        }

        return selectedCategories;
    }

    private void clearCategoryFilters() {
        // Assuming you have a reference to the checkbox container
        for (int i = 0; i < categoryContainer.getChildCount(); i++) {
            View child = categoryContainer.getChildAt(i);
            if (child instanceof CheckBox) {
                ((CheckBox) child).setChecked(false);
            }
        }
    }


    // Method to load products
    private void loadProducts() {
        Log.d("ProductLoading", "Called Api");

        // Clear previous filtered product list
        if (filteredProductList != null) {
            filteredProductList.clear();
        }

        // Call the API
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<Product>> call = apiService.getAllProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log the response success
                    Log.d("ProductLoading", "Products loaded successfully. Total products: " + response.body().size());

                    // Get products from the API response
                    productList = new ArrayList<>(response.body());

                    // Log product IDs
//                    for (Product product : productList) {
//                        Log.d("ProductDetailActivity", "Product ID: " + product.getId());
//                        Log.d("ProductDetailActivity", "Product Name: " + product.getName());
//                        Log.d("ProductDetailActivity", "Product Image: " + product.getProductImage());
//                        Log.d("ProductDetailActivity", "Category ID: " + product.getCategoryId());
//                        Log.d("ProductDetailActivity", "Description: " + product.getDescription());
//                        Log.d("ProductDetailActivity", "Price: $" + product.getPrice());
//                        Log.d("ProductDetailActivity", "Available Stock: " + product.getAvailableStock());
//                        Log.d("ProductDetailActivity", "Is Active: " + product.getIsActive());
//                        Log.d("ProductDetailActivity", "Vendor ID: " + product.getVendor());
//                        Log.d("ProductDetailActivity", "Created At: " + product.getCreatedAt());
//                        Log.d("ProductDetailActivity", "Stock Last Updated: " + product.getStockLastUpdated());
//                        Log.d("ProductDetailActivity", "Product Category Name: " + product.getProductCategoryName());
//                        Log.d("ProductDetailActivity", "Vendor Name: " + product.getVendorName());
//                        Log.d("ProductDetailActivity", "Average Rating: " + product.getRating());
//                    }

                    // Initially, all products are displayed
                    filteredProductList = new ArrayList<>(productList);
                    productAdapter = new ProductAdapter(getContext(), filteredProductList);
                    recyclerViewProducts.setAdapter(productAdapter);

                    // Extract distinct categories and create checkboxes
                    createCategoryCheckboxes(getDistinctCategories(productList));

                    // Log the successful setting of adapter
                    Log.d("ProductLoading", "Product adapter set with " + filteredProductList.size() + " items.");

                    // Apply filters to the loaded products
                    applyFilters();
                } else {
                    // Log the failure response
                    Log.e("ProductLoading", "Failed to load products. Response code: " + response.code());

                    // Handle the error response (e.g., show a message)
                    Toast.makeText(getContext(), "Failed to load products", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                // Log the error
                Log.e("ProductLoading", "API call failed: " + t.getMessage());
                // Handle the failure (e.g., show a message)
                Toast.makeText(getContext(), "Failed to load products: " + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private List<String> getDistinctCategories(List<Product> products) {
        Set<String> categories = new HashSet<>();
        for (Product product : products) {
            categories.add(product.getProductCategoryName()); // Assume categoryId represents the category
        }
        return new ArrayList<>(categories);
    }

    private void createCategoryCheckboxes(List<String> categories) {
        // Assuming you have a LinearLayout to hold the checkboxes
        categoryContainer.removeAllViews(); // Clear previous checkboxes if any

        for (String category : categories) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(category);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> applyFilters());
            categoryContainer.addView(checkBox);
        }
    }

}
