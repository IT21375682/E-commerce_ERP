package com.example.eadecommerce;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eadecommerce.adapter.OrderProductsAdapter;
import com.example.eadecommerce.model.CartItem;
import com.example.eadecommerce.model.SingleOrder;
import com.example.eadecommerce.model.SingleOrderProductItem;
import com.example.eadecommerce.model.SingleOrderStatus;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {

    private ImageButton buttonBack;
    private RelativeLayout cusMyBookingArrowUpLayout, cusMyBookingArrowDownLayout;
    private ImageView cusMyBookingArrowUp, cusMyBookingArrowDown;
    private RecyclerView recyclerParcelDetails;
    private OrderProductsAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView orderId1, orderId2, subtotalAmountTextView, deliveryFeeAmountTextView, totalAmountTextView;
    private ImageView paymentImage, receivedImage, partiallyDeliveredImage, departedImage;
    private View paymentImageLine, partiallyDeliveredImageLine, receivedImageLine;
    private TextView paymentImageButton, receivedImageText, partiallyDeliveredImageText, departedImageText, canceledTextView;
    private LinearLayout canceledLayout;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Get the order ID passed to this activity
        orderId = getIntent().getStringExtra("ORDER_ID");

        buttonBack = findViewById(R.id.buttonBack);
        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Find views
        cusMyBookingArrowUpLayout = findViewById(R.id.cusMyBookingArrowUpLayout);
        cusMyBookingArrowDownLayout = findViewById(R.id.cusMyBookingArrowDownLayout);
        cusMyBookingArrowUp = findViewById(R.id.cusMyBookingArrowUp);
        cusMyBookingArrowDown = findViewById(R.id.cusMyBookingArrowDown);
        recyclerParcelDetails = findViewById(R.id.recyclerParcelDetails);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        orderId1 = findViewById(R.id.orderId1);
        orderId2 = findViewById(R.id.orderId2);
        subtotalAmountTextView = findViewById(R.id.subtotalAmountTextView);
        deliveryFeeAmountTextView = findViewById(R.id.deliveryFeeAmountTextView);
        totalAmountTextView = findViewById(R.id.totalAmountTextView);

        paymentImage = findViewById(R.id.paymentImage);
        receivedImage = findViewById(R.id.receivedImage);
        partiallyDeliveredImage = findViewById(R.id.partiallyDeliveredImage);
        departedImage = findViewById(R.id.departedImage);
        paymentImageLine = findViewById(R.id.paymentImageLine);
        partiallyDeliveredImageLine = findViewById(R.id.partiallyDeliveredImageLine);
        receivedImageLine = findViewById(R.id.receivedImageLine);

        paymentImageButton = findViewById(R.id.paymentImageButton);
        receivedImageText = findViewById(R.id.receivedImageText);
        partiallyDeliveredImageText = findViewById(R.id.partiallyDeliveredImageText);
        departedImageText = findViewById(R.id.departedImageText);

        canceledLayout = findViewById(R.id.canceledLayout);
        canceledTextView = findViewById(R.id.canceledTextView);

        // Initially, hide the down layout and RecyclerView
        cusMyBookingArrowDownLayout.setVisibility(View.GONE);
        recyclerParcelDetails.setVisibility(View.GONE);

        // Set up click listener for arrow button
        cusMyBookingArrowUp.setOnClickListener(v -> {
            // Hide the up layout and show the down layout and RecyclerView
            cusMyBookingArrowUpLayout.setVisibility(View.GONE);
            cusMyBookingArrowDownLayout.setVisibility(View.VISIBLE);
            recyclerParcelDetails.setVisibility(View.VISIBLE);
        });

        cusMyBookingArrowDown.setOnClickListener(v -> {
            // Show the up layout and hide the down layout and RecyclerView
            cusMyBookingArrowUpLayout.setVisibility(View.VISIBLE);
            cusMyBookingArrowDownLayout.setVisibility(View.GONE);
            recyclerParcelDetails.setVisibility(View.GONE);
        });

        // Set up RecyclerView with mock data
        setUpRecyclerView();

        // Handle swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(() -> {
            setUpRecyclerView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setUpRecyclerView() {
        recyclerParcelDetails.setLayoutManager(new LinearLayoutManager(this));

        // Assuming you have an instance of Retrofit and ApiService
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Call the API to get order details
        Call<SingleOrder> call = apiService.getOrderDetails(orderId);
        call.enqueue(new Callback<SingleOrder>() {
            @Override
            public void onResponse(Call<SingleOrder> call, Response<SingleOrder> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SingleOrder order = response.body();

                    // Log order details
                    Log.d("OrderDetailActivity", "Order ID: " + order.getId());
                    Log.d("OrderDetailActivity", "User ID: " + order.getUserId());
                    Log.d("OrderDetailActivity", "Order Date: " + order.getDate());
                    Log.d("OrderDetailActivity", "Total Price: " + order.getTotal());

                    // Log order status
                    SingleOrderStatus status = order.getStatus();
                    Log.d("OrderDetailActivity", "Order Status - Pending: " + status.isPending());
                    Log.d("OrderDetailActivity", "Order Status - Pending: " + status.getPendingDate());
                    Log.d("OrderDetailActivity", "Order Status - Processing: " + status.isProcessing());
                    Log.d("OrderDetailActivity", "Order Status - Processing: " + status.getProcessingDate());
                    Log.d("OrderDetailActivity", "Order Status - Dispatched: " + status.isDispatched());
                    Log.d("OrderDetailActivity", "Order Status - Dispatched: " + status.getDispatchedDate());
                    Log.d("OrderDetailActivity", "Order Status - Partially Delivered: " + status.isPartiallyDelivered());
                    Log.d("OrderDetailActivity", "Order Status - Partially Delivered: " + status.getPartiallyDeliveredDate());
                    Log.d("OrderDetailActivity", "Order Status - Delivered: " + status.isDelivered());
                    Log.d("OrderDetailActivity", "Order Status - Delivered: " + status.getDeliveredDate());

                    Log.d("OrderDetailActivity", "Order Status - Canceled: " + status.isCanceled());
                    Log.d("OrderDetailActivity", "Order Status - Canceled: " + status.getCanceledDate());

                    // Log cancellation note if available
                    if (order.getCancellationNote() != null) {
                        Log.d("OrderDetailActivity", "Cancellation Note: " + order.getCancellationNote());
                    }

                    // Log product items
                    List<SingleOrderProductItem> productItems = order.getProductItems();
                    for (SingleOrderProductItem product : productItems) {
                        Log.d("OrderDetailActivity", "Product ID: " + product.getProductId());
                        Log.d("OrderDetailActivity", "Product Name: " + product.getProductName());
                        Log.d("OrderDetailActivity", "Product Price: " + product.getProductPrice());
                        Log.d("OrderDetailActivity", "Product Count: " + product.getCount());
                        Log.d("OrderDetailActivity", "Product Delivered: " + product.isDelivered());
                    }

                    // Create and set adapter for the RecyclerView
                    adapter = new OrderProductsAdapter(productItems);
                    recyclerParcelDetails.setAdapter(adapter);

                    // Handle other order details like total price
                    orderId1.setText(order.getId());
                    orderId2.setText(order.getId());
                    double subTotal = order.getTotal() - 20;
                    String formattedSubTotal = String.format("%.2f", subTotal);
                    subtotalAmountTextView.setText(formattedSubTotal);
                    deliveryFeeAmountTextView.setText("20");
                    double total = order.getTotal();
                    String formattedTotal = String.format("%.2f", total);
                    totalAmountTextView.setText(formattedTotal);

                    updateDefaultOrderStatusUI(order);
                } else {
                    // Handle the case where the response was not successful
                    Log.e("OrderDetailActivity", "Failed to load order details: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<SingleOrder> call, Throwable t) {
                // Handle failure
                Log.e("OrderDetailActivity", "API call failed: " + t.getMessage());
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

    // Method to update UI based on order status
    private void updateDefaultOrderStatusUI(SingleOrder status) {
        paymentImage.setBackgroundResource(R.drawable.circle_border_background);
        paymentImage.setImageResource(R.drawable.payment);
        paymentImageLine.setBackgroundResource(R.color.ordersTextColor);
        receivedImage.setBackgroundResource(R.drawable.circle_border_background);
        receivedImage.setImageResource(R.drawable.pending);
        partiallyDeliveredImageLine.setBackgroundResource(R.color.ordersTextColor);
        partiallyDeliveredImage.setBackgroundResource(R.drawable.circle_border_background);
        partiallyDeliveredImage.setImageResource(R.drawable.partial);
        receivedImageLine.setBackgroundResource(R.color.ordersTextColor);
        departedImage.setBackgroundResource(R.drawable.circle_border_background);
        departedImage.setImageResource(R.drawable.completed);
        updateOrderStatusUI(status);
    }

    // Method to update UI based on order status
    private void updateOrderStatusUI(SingleOrder order) {

        SingleOrderStatus status = order.getStatus();

        // Change for payment
        if (status.isPending() || status.isProcessing()) {
            paymentImage.setBackgroundResource(R.drawable.circle_border_background_done);
            paymentImage.setImageResource(R.drawable.payment_done);
            if(status.isProcessing()){
                String formattedDate = formatOrderDate(status.getProcessingDate());
                paymentImageButton.setText(formattedDate);
            } else {
                String formattedDate = formatOrderDate(status.getPendingDate());
                paymentImageButton.setText(formattedDate);
            }
        }

        // Change for dispatched
        if (status.isDispatched()) {
            paymentImageLine.setBackgroundResource(R.color.primary);
            receivedImage.setBackgroundResource(R.drawable.circle_border_background_done);
            receivedImage.setImageResource(R.drawable.pending_done);
            if(status.getDispatchedDate()!=null) {
                Log.d("Order Detail Activity Dispatched Date", status.getDispatchedDate());
                String formattedDate = formatOrderDate(status.getDispatchedDate());
                receivedImageText.setText(formattedDate);
            }
        }

        // Change for partially delivered
        if (status.isPartiallyDelivered()) {
            partiallyDeliveredImageLine.setBackgroundResource(R.color.primary);
            partiallyDeliveredImage.setBackgroundResource(R.drawable.circle_border_background_done);
            partiallyDeliveredImage.setImageResource(R.drawable.partial_done);
            if(status.getPartiallyDeliveredDate()!=null) {
                String formattedDate = formatOrderDate(status.getPartiallyDeliveredDate());
                partiallyDeliveredImageText.setText(formattedDate);
            }
        }

        // Change for delivered
        if (status.isDelivered()) {
            receivedImageLine.setBackgroundResource(R.color.primary);
            departedImage.setBackgroundResource(R.drawable.circle_border_background_done);
            departedImage.setImageResource(R.drawable.completed_done);
            if(status.getDeliveredDate()!=null) {
                String formattedDate = formatOrderDate(status.getDeliveredDate());
                departedImageText.setText(formattedDate);
            }
        }

        // Change for Canceled
        if (status.isCanceled()) {
            canceledLayout.setVisibility(View.VISIBLE);
            if(status.getCanceledDate()!=null) {
                Log.d("Order Detail Activity Cancellation", order.getCancellationNote());
                paymentImageButton.setText(order.getCancellationNote());
            }
        }
    }

    // Method to format the date
    private String formatOrderDate(String dateStr) {
        // Define the input format from the API (ISO 8601 format)
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        // Define the output format you want
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd | HH:mm", Locale.getDefault());

        Date date = null;
        try {
            // Parse the input date string
            date = inputFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Return the formatted date or the original string if parsing fails
        return (date != null) ? outputFormat.format(date) : dateStr;
    }
}