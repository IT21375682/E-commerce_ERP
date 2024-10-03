package com.example.eadecommerce.adapter;

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

/**
 * The CanceledOrderAdapter class is a RecyclerView adapter for displaying canceled orders.
 * It binds order data to the views and handles item click events to navigate to order details.
 */
public class CanceledOrderAdapter extends RecyclerView.Adapter<CanceledOrderAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private Context context;

    /**
     * Constructor to initialize the adapter with a list of orders and context.
     * @param orderList The list of canceled orders.
     * @param context The context of the activity.
     */
    public CanceledOrderAdapter(List<Order> orderList, Context context) {
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
        holder.totalTextView.setText(String.format("%.2f", order.getTotal()));

        holder.orderLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, OrderDetailActivity.class);
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

    /**
     * Formats the order date from the API to a user-friendly format.
     * @param dateStr The date string in ISO 8601 format.
     * @return The formatted date string.
     */
    private String formatOrderDate(String dateStr) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd | HH:mm", Locale.getDefault());

        Date date = null;
        try {
            date = inputFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (date != null) ? outputFormat.format(date) : dateStr;
    }
}
