package com.example.eadecommerce.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.eadecommerce.R;
import com.example.eadecommerce.adapter.CanceledOrderAdapter;
import com.example.eadecommerce.model.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fragment class for displaying canceled orders with filtering options.
 */
public class CanceledOrderFragment extends Fragment {

    private RecyclerView ordersRecyclerView;
    private CanceledOrderAdapter orderAdapter;
    private List<Order> orderList;
    private Spinner filterSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment and initialize views
        View view = inflater.inflate(R.layout.fragment_canceled_order, container, false);
        ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        filterSpinner = view.findViewById(R.id.filterSpinner);

        // Retrieve the order list from arguments
        if (getArguments() != null) {
            orderList = getArguments().getParcelableArrayList("orderList");
        }

        // Set up RecyclerView
        orderAdapter = new CanceledOrderAdapter(orderList, getContext());
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersRecyclerView.setAdapter(orderAdapter);

        // Set up Spinner functionality
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.date_filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);

        // Handle Spinner selection
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = parent.getItemAtPosition(position).toString();
                sortOrderList(selectedOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed
            }
        });

        return view; // Return the inflated view
    }

    private void sortOrderList(String sortOption) {
        // Sort the order list based on the selected option
        if (sortOption.equals("Sort by Newest")) {
            // Sort by newest (descending order)
            Collections.sort(orderList, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        } else if (sortOption.equals("Sort by Oldest")) {
            // Sort by oldest (ascending order)
            Collections.sort(orderList, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        }
        orderAdapter.notifyDataSetChanged(); // Notify the adapter of data changes
    }
}
