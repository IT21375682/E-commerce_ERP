package com.example.eadecommerce.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.eadecommerce.R;
import com.example.eadecommerce.adapter.ProductAdapter;
import com.example.eadecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2)); // 2 columns

        // Load product list
        productList = new ArrayList<>();
        productList.add(new Product("Product 1", 19.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg"));
        productList.add(new Product("Product 2", 29.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg"));
        productList.add(new Product("Product 3", 39.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg"));
        productList.add(new Product("Product 4", 19.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg"));
        productList.add(new Product("Product 5", 29.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg"));
        productList.add(new Product("Product 6", 39.99, "https://assets.vogue.in/photos/64be31695ad7ce31037005c8/3:4/w_2560%2Cc_limit/Snapinsta.app_362218118_18390206827011278_5161897771700955906_n_1080.jpg"));

        productAdapter = new ProductAdapter(getContext(), productList);
        recyclerViewProducts.setAdapter(productAdapter);

        return view;
    }
}
