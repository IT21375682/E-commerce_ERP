package com.example.eadecommerce.adapter;

// OrderAdapter.java
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.OrderDetailActivity;
import com.example.eadecommerce.R;
import com.example.eadecommerce.model.Order;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private Context context;

    public PendingOrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.orderIdTextView.setText(order.getOrderId());
        String formattedDate = formatOrderDate(order.getDate());
        holder.dateTextView.setText(formattedDate);
        holder.totalTextView.setText(String.valueOf(order.getTotal()));

        // Set an onClickListener for the orderLayout
        holder.orderLayout.setOnClickListener(v -> {
            // Create an Intent to navigate to OrderDetailActivity
            Intent intent = new Intent(context, OrderDetailActivity.class);
            // Pass the order details to the OrderDetailActivity
            intent.putExtra("ORDER_ID", order.getOrderId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdTextView;
        TextView dateTextView;
        TextView totalTextView;
        View orderLayout;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            totalTextView = itemView.findViewById(R.id.totalTextView);
            orderLayout = itemView.findViewById(R.id.orderLayout);
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
