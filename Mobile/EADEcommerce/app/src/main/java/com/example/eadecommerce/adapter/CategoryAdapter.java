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
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categories;
    private Context context;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context; // Save the context
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
//        holder.productCount.setText(category.getProductCount() + " products");

        // Set click listener to start CategoryProductsActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryProductsActivity.class);
                intent.putExtra("categoryId", category.getId());
                intent.putExtra("categoryName", category.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    // Method to update the list
    public void updateList(List<Category> newCategories) {
        categories = newCategories;
        notifyDataSetChanged();  // Refresh the RecyclerView with new data
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName, productCount;
        CardView layoutCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
//            categoryImage = itemView.findViewById(R.id.imageViewCategory);
            categoryName = itemView.findViewById(R.id.textViewCategoryName);
//            productCount = itemView.findViewById(R.id.textViewProductCount);
            layoutCategory = itemView.findViewById(R.id.layoutCategory);
        }
    }
}


