package com.example.eadecommerce.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Spinner;

import com.example.eadecommerce.R;
import com.example.eadecommerce.adapter.ProductAdapter;
import com.example.eadecommerce.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private ProductAdapter productAdapter;
    private RecyclerView recyclerViewProducts;
    private List<Product> productList;
    private List<Product> filteredProductList;

    private String currentCategory = "All";
    private String currentSearchQuery = "";
    private String currentSortOption = "Default";

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
        Spinner spinnerSort = view.findViewById(R.id.spinnerSort);
        SearchView searchViewProducts = view.findViewById(R.id.searchViewProducts);

        // Load product list
        loadProducts();

        // Setup the swipe refresh layout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Remove search and filters
            searchViewProducts.setQuery("", false);
            spinnerCategory.setSelection(0); // Assuming first item is "All"
            spinnerSort.setSelection(0); // Assuming first item is "Default"

            // Reload the products
            loadProducts();

            // Stop the refresh animation
            swipeRefreshLayout.setRefreshing(false);
        });

        // Handle category selection
        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentCategory = parentView.getItemAtPosition(position).toString();
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // No category selected, show all products
                currentCategory = "All";
                applyFilters();
            }
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

        // Set a touch listener on the parent view to clear focus when user clicks outside the SearchView
        view.setOnTouchListener((View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) -> {
            // Clear focus if the touch event is outside the SearchView
            searchViewProducts.clearFocus();
            return false;
        });

        return view;
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
//        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
//        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns
//
//        Spinner spinnerCategory = view.findViewById(R.id.spinnerCategory);
//        Spinner spinnerSort = view.findViewById(R.id.spinnerSort);
//        SearchView searchViewProducts = view.findViewById(R.id.searchViewProducts);
//
//        // Load product list
//        loadProducts();
//
//        // Setup the swipe refresh layout
//        swipeRefreshLayout.setOnRefreshListener(() -> requireActivity().runOnUiThread(() -> {
//            // Remove search and filters
//            searchViewProducts.setQuery("", false);
//            spinnerCategory.setSelection(0); // Assuming first item is "All"
//            spinnerSort.setSelection(0); // Assuming first item is "Default"
//
//            // Reload the products
//            loadProducts();
//
//            // Stop the refresh animation
//            swipeRefreshLayout.setRefreshing(false);
//        }));
//
//        // Handle category selection
//        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                currentCategory = parentView.getItemAtPosition(position).toString();
//                applyFilters();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                // No category selected, show all products
//                currentCategory = "All";
//                applyFilters();
//            }
//        });
//
//        // Handle sorting selection
//        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                currentSortOption = parentView.getItemAtPosition(position).toString();
//                applyFilters();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                currentSortOption = "Default";
//                applyFilters();
//            }
//        });
//
//        // Handle product search
//        searchViewProducts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                currentSearchQuery = query;
//                applyFilters();
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                currentSearchQuery = newText;
//                applyFilters();
//                return true;
//            }
//        });
//    }


    // This function applies the category, search, and sorting filters
    private void applyFilters() {
        // Step 1: Filter by category
        List<Product> tempFilteredList = new ArrayList<>();
        if (currentCategory.equals("All")) {
            tempFilteredList.addAll(productList);
        } else {
            for (Product product : productList) {
                if (product.getCategory().equals(currentCategory)) {
                    tempFilteredList.add(product);
                }
            }
        }

        // Step 2: Filter by search query
        if (!currentSearchQuery.isEmpty()) {
            List<Product> searchFilteredList = new ArrayList<>();
            for (Product product : tempFilteredList) {
                if (product.getName().toLowerCase().contains(currentSearchQuery.toLowerCase())) {
                    searchFilteredList.add(product);
                }
            }
            tempFilteredList = searchFilteredList;
        }

        // Step 3: Sort the filtered list
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

    // Method to load products
    private void loadProducts() {

        // Load product list
        productList = new ArrayList<>();
        productList.add(new Product("Product 1", 19.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 1"));
        productList.add(new Product("Product 2", 29.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 1"));
        productList.add(new Product("Product 3", 39.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 1"));
        productList.add(new Product("Product 4", 31.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 2"));
        productList.add(new Product("Product 5", 43.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 2"));
        productList.add(new Product("Product 6", 35.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 3"));
        productList.add(new Product("Apple", 19.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 1"));
        productList.add(new Product("Mango", 32.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 1"));
        productList.add(new Product("Orange", 14.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg", "Category 2"));

        // Initially, all products are displayed
        filteredProductList = new ArrayList<>(productList);
        productAdapter = new ProductAdapter(getContext(), filteredProductList);
        recyclerViewProducts.setAdapter(productAdapter);

        // Apply filters to the loaded products
        applyFilters();
    }
}
