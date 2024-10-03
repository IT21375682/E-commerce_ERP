package com.example.eadecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eadecommerce.CategoryProductsActivity;
import com.example.eadecommerce.R;
import com.example.eadecommerce.model.Category;

import java.util.List;

/**
 * The CategoryAdapter class is a RecyclerView adapter for displaying categories.
 * It binds category data to the views and handles item click events to navigate to product listings.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private Context context;

    /**
     * Constructor to initialize the adapter with context and a list of categories.
     * @param context The context for launching activities.
     * @param categories The list of categories.
     */
    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.categoryName.setText(category.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryProductsActivity.class);
            intent.putExtra("categoryId", category.getId());
            intent.putExtra("categoryName", category.getName());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    /**
     * Method to update the list of categories and refresh the RecyclerView.
     * @param newCategories The new list of categories.
     */
    public void updateList(List<Category> newCategories) {
        categories = newCategories;
        notifyDataSetChanged();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, productCount;
        CardView layoutCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.textViewCategoryName);
            layoutCategory = itemView.findViewById(R.id.layoutCategory);
        }
    }
}
