package com.example.eadecommerce;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.eadecommerce.adapter.ProductCommentAdapter;
import com.example.eadecommerce.fragments.HomeFragment;
import com.example.eadecommerce.model.Cart;
import com.example.eadecommerce.model.CartProduct;
import com.example.eadecommerce.model.Comment;
import com.example.eadecommerce.model.Product;
import com.example.eadecommerce.model.ProductCommentData;
import com.example.eadecommerce.model.ProductImageSingleton;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.JwtUtils;
import com.example.eadecommerce.network.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {

    ImageButton buttonBack, buttonCart;
    ImageView clickcartHome;
    private RecyclerView commentsRecyclerView;

    private Button buttonMinus, buttonPlus, updateReview, buttonAddToCart;
    private int quantity = 1;
    private double unitPrice;
    private int rating = 0;

    private TextView quantityTextView, productNameTextView, productCategoryTextView, productVendorTextView, productPriceTextView, productDescriptionTextView, textAddToCart;

    private RatingBar ratingBar;
    private ImageView productImageView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int MAX_QUANTITY = 10;
    private String productId, userId, vendorId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Get the JWT token from SharedPreferences and Decode the token and get the user ID
        String token = JwtUtils.getTokenFromSharedPreferences(this);
        userId = JwtUtils.getUserIdFromToken(token);
        Log.d("userId",userId);

        // Get data from Intent
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");

        buttonBack = findViewById(R.id.buttonBack);
        clickcartHome = findViewById(R.id.clickcartHome);
        buttonCart = findViewById(R.id.buttonCart);
        updateReview = findViewById(R.id.updateReview);
        RatingBar ratingBarInput = findViewById(R.id.ratingBarInput);
        EditText editTextComment = findViewById(R.id.commentEditTextInput);
        buttonAddToCart = findViewById(R.id.buttonAddToCart); // Initialize the button
        buttonAddToCart.setOnClickListener(v -> addToCart()); // Set click listener

        ratingBarInput.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                // Here you can get the updated rating value
                Log.d("ProductDetailActivity", "User rating: " + rating);
            }
        });

        updateReview.setOnClickListener(v -> {
            // Get the rating and comment text
            int ratingIntValue = (int) ratingBarInput.getRating(); // Cast to int
            String commentTextValue = editTextComment.getText().toString();

            // Check for empty comment if rating is 0
            if (ratingIntValue == 0) {
                editTextComment.setError("You must provide a rating before commenting.");
                return; // Exit if comment is empty
            }

            // Check for empty comment
            if (commentTextValue.isEmpty()) {
                editTextComment.setError("Comment is required.");
                return; // Exit if comment is empty
            }

            // Create a Comment object
            Comment comment = new Comment();
            comment.setUserId(userId); // Set userId from JWT
            comment.setVendorId(vendorId);
            comment.setProductId(productId); // Set productId from Intent
            comment.setRating(ratingIntValue); // Use the integer rating
            comment.setCommentText(commentTextValue);

            // Call your API to post the comment
            postComment(comment);
            // Remove focus and hide the cursor from the EditText
            editTextComment.clearFocus();

        });



        // Handle Home click to load HomeFragment
        clickcartHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load HomeFragment when clickcart_logo is clicked
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContent, new HomeFragment());  // Switch to HomeFragment
                transaction.commit();
            }
        });

        // Handle Cart click to load Cart
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);

//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear previous activities
//                finish();
            }
        });

        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Initialize views
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        productNameTextView = findViewById(R.id.productNameTextView);
        productCategoryTextView = findViewById(R.id.productCategoryTextView);
        productVendorTextView = findViewById(R.id.productVendorTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        productDescriptionTextView = findViewById(R.id.productDescriptionTextView);
        textAddToCart = findViewById(R.id.textAddToCart);

        ratingBar = findViewById(R.id.ratingBar);
        productImageView = findViewById(R.id.productImageView);
        quantityTextView = findViewById(R.id.quantityTextView);

        // Fetch product details
        fetchProductDetails(productId);

        // Set up SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Reload product details
                refreshProductDetails();
                quantity = 1;
                // Stop the refreshing indicator once done
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        commentsRecyclerView = findViewById(R.id.commentsRecyclerView);

        buttonMinus = findViewById(R.id.buttonMinus);
        buttonPlus = findViewById(R.id.buttonPlus);

        // Set up button listeners
        buttonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) { // Ensure quantity does not go below 1
                    quantity--;
                    quantityTextView.setText(String.valueOf(quantity));
                    updateTotalPrice();
                }
            }
        });

        buttonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (quantity < MAX_QUANTITY) { // Ensure quantity does not exceed max balance
                        quantity++;
                        quantityTextView.setText(String.valueOf(quantity));
                        updateTotalPrice();
                    }
            }
        });


    }

//    private void loadProductImage(String productImage, ImageView productImageView) {
//        Log.d("ProductDetailActivity", "Loading image: " + productImage);
//
//        Picasso.get()
//                .load(productImage)
//                .placeholder(R.drawable.person) // Placeholder image
//                .error(R.drawable.logo_dark) // Error image
//                .into(productImageView, new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
//                        Log.d("ProductDetailActivity", "Image loaded successfully: " + productImage);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Log.e("ProductDetailActivity", "Error loading image: " + productImage, e);
//                    }
//                });
//    }


    private void refreshProductDetails() {
        // Optionally, you can add logic here to fetch updated product details
        fetchProductDetails(productId);
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

    private void fetchProductDetails(String productId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Product> call = apiService.getProductDetails(productId);

        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product product = response.body();

                    // Log all the product details
                    Log.d("ProductDetailActivity", "Product ID: " + product.getId());
                    Log.d("ProductDetailActivity", "Product Name: " + product.getName());
                    Log.d("ProductDetailActivity", "Product Image: " + product.getProductImage());
                    Log.d("ProductDetailActivity", "Category ID: " + product.getCategoryId());
                    Log.d("ProductDetailActivity", "Description: " + product.getDescription());
                    Log.d("ProductDetailActivity", "Price: $" + product.getPrice());
                    Log.d("ProductDetailActivity", "Available Stock: " + product.getAvailableStock());
                    Log.d("ProductDetailActivity", "Is Active: " + product.getIsActive());
                    Log.d("ProductDetailActivity", "Vendor ID: " + product.getVendor());
                    Log.d("ProductDetailActivity", "Created At: " + product.getCreatedAt());
                    Log.d("ProductDetailActivity", "Stock Last Updated: " + product.getStockLastUpdated());
                    Log.d("ProductDetailActivity", "Product Category Name: " + product.getProductCategoryName());
                    Log.d("ProductDetailActivity", "Vendor Name: " + product.getVendorName());
                    Log.d("ProductDetailActivity", "Average Rating: " + product.getRating());

                    // You can also log the entire product object as a JSON string if you want
                    Log.d("ProductDetailActivity", "Product Object: " + new Gson().toJson(product));

                    // Update the UI with product details
                    productNameTextView.setText(product.getName());
                    productCategoryTextView.setText(product.getProductCategoryName());
                    productVendorTextView.setText(product.getVendorName());
                    productPriceTextView.setText(String.format("LKR %.2f", product.getPrice()));
                    productDescriptionTextView.setText(product.getDescription());
                    MAX_QUANTITY = product.getAvailableStock();
                    unitPrice = product.getPrice();
                    vendorId = product.getVendor();
                    // Set the rating bar
                    ratingBar.setRating((float) product.getRating());

                    // Decode the Base64 image and set it to the ImageView
                    String base64Image = product.getProductImage();
                    Bitmap decodedImage = decodeBase64ToBitmap(base64Image);
                    productImageView.setImageBitmap(decodedImage);

                    // Load product image using Picasso
//                    loadProductImage(product.getProductImage(), productImageView);

                    // Set click listener to open full screen image
                    productImageView.setOnClickListener(v -> {
                        ProductImageSingleton.getInstance().setProductImage(product.getProductImage());
                        Intent fullScreenIntent = new Intent(ProductDetailActivity.this, FullScreenImageActivity.class);
//                        fullScreenIntent.putExtra("productImage", product.getProductImage());
                        startActivity(fullScreenIntent);
                    });

                    updateTotalPrice();

                    // After updating product details, fetch comments
                    fetchComments(product.getId());
                } else {
                    Log.e("ProductDetailActivity", "Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("ProductDetailActivity", "Failed to load product details", t);
            }
        });
    }

    private Bitmap decodeBase64ToBitmap(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void updateTotalPrice() {
        // Calculate total price
        double totalPrice = quantity * unitPrice; // Calculate total price based on quantity and unit price
        textAddToCart.setText(String.format("Total: LKR %.2f", totalPrice)); // Update the TextView with total price
    }

    private void fetchComments(String productId) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<ProductCommentData>> call = apiService.getCommentsByProductId(productId);

        call.enqueue(new Callback<List<ProductCommentData>>() {
            @Override
            public void onResponse(Call<List<ProductCommentData>> call, Response<List<ProductCommentData>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ProductCommentData> comments = response.body();

                    if (comments.isEmpty()) {
                        // If no comments, show "No comments yet" and hide the RecyclerView
                        findViewById(R.id.noCommentsTextView).setVisibility(View.VISIBLE);
                        findViewById(R.id.commentsRecyclerView).setVisibility(View.GONE);
                    } else {
                        // Log and set up the adapter with the fetched comments
                        for (ProductCommentData comment : comments) {
                            Log.d("CommentList", "Comment: " + comment.getCommentText());
                            // Check if the comment belongs to the current user
                            if (comment.getUserId().equals(userId)) {
                                Log.d("CommentList", "This comment belongs to the current user.");
                                // Get references to the RatingBar and EditText
                                RatingBar ratingBarInput = findViewById(R.id.ratingBarInput);
                                EditText commentEditTextInput = findViewById(R.id.commentEditTextInput);

                                // Set the RatingBar and EditText based on the comment's data
                                ratingBarInput.setRating(comment.getRating());
                                commentEditTextInput.setText(comment.getCommentText());
                                rating = comment.getRating();
                                Log.d("Comment rating 1", String.valueOf(rating));

                                // Check if rating is not equal to 0 to set the RatingBar as non-interactive
                                if (rating != 0) {
                                    ratingBarInput.setIsIndicator(true); // Make the RatingBar non-interactive
                                } else {
                                    ratingBarInput.setIsIndicator(false); // Allow interaction
                                }
                            }
                        }

                        // Set up the adapter
                        ProductCommentAdapter productCommentAdapter = new ProductCommentAdapter(ProductDetailActivity.this, comments);
                        RecyclerView commentsRecyclerView = findViewById(R.id.commentsRecyclerView);
                        commentsRecyclerView.setAdapter(productCommentAdapter);

                        // Show RecyclerView and hide the "No comments yet" message
                        findViewById(R.id.noCommentsTextView).setVisibility(View.GONE);
                        commentsRecyclerView.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("ProductDetailActivity", "Error fetching comments: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ProductCommentData>> call, Throwable t) {
                Log.e("ProductDetailActivity", "Failed to load comments", t);
            }
        });
    }

    private void postComment(Comment comment) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Comment> call = apiService.addOrUpdateComment(comment); // Assuming you have this method in your ApiService

        call.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.isSuccessful()) {
                    Log.d("ProductDetailActivity", "Comment posted successfully");

                    // Show success message using Snackbar
                    Snackbar.make(findViewById(R.id.scrollView), "Comment updated successfully!", Snackbar.LENGTH_SHORT).show();

                    // Scroll to top of the activity
                    ScrollView scrollView = findViewById(R.id.scrollView);
                    scrollView.post(() -> scrollView.scrollTo(0, 0));

                    fetchProductDetails(productId);
                } else {
                    Log.e("ProductDetailActivity", "Failed to post comment: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Log.e("ProductDetailActivity", "Error posting comment", t);
            }
        });
    }

    // Method to add product to cart
    private void addToCart() {
        Log.d("ProductDetailActivity", "Attempting to add product to cart for user: " + userId);

        // Step 1: Check if the cart exists
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<Cart> cartCall = apiService.getCartByUserId(userId);

        cartCall.enqueue(new Callback<Cart>() {
            @Override
            public void onResponse(Call<Cart> call, Response<Cart> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("ProductDetailActivity", "Cart found for user: " + userId);
                    // Step 2: If the cart exists, update the product count
                    String cartId = response.body().getCartId();
                    Log.d("ProductDetailActivity", "Updating cart with ID: " + cartId);
                    updateCart(cartId, productId, quantity);
                } else {
                    Log.d("ProductDetailActivity", "Cart not found for user: " + userId + ". Creating a new cart.");
                    // Step 3: If the cart does not exist, create a new cart
                    createCart(userId, productId, quantity);
                }
            }

            @Override
            public void onFailure(Call<Cart> call, Throwable t) {
                Log.e("ProductDetailActivity", "Failed to get cart", t);
            }
        });
    }

    // Method to update the existing cart
    private void updateCart(String cartId, String productId, int quantity) {
        Log.d("ProductDetailActivity", "Updating product in cart. Cart ID: " + cartId + ", Product ID: " + productId + ", Quantity: " + quantity);

        // Prepare the request body
        CartProduct cartProduct = new CartProduct(productId, quantity);
        Call<Void> updateCall = RetrofitClient.getRetrofitInstance()
                .create(ApiService.class)
                .updateCartProduct(cartId, cartProduct);

        updateCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("ProductDetailActivity", "Cart updated successfully for cart ID: " + cartId);
                    Snackbar.make(findViewById(android.R.id.content), "Product Added to Cart!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.e("ProductDetailActivity", "Failed to update cart: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ProductDetailActivity", "Failed to update cart", t);
            }
        });
    }

    // Method to create a new cart
    private void createCart(String userId, String productId, int quantity) {
        Log.d("ProductDetailActivity", "Creating a new cart for user: " + userId + " with product ID: " + productId + " and quantity: " + quantity);

        // Prepare the request body
        CartProduct cartProduct = new CartProduct(productId, quantity);
        List<CartProduct> products = new ArrayList<>();
        products.add(cartProduct);
        Cart cart = new Cart(userId, products);

        Call<Void> createCall = RetrofitClient.getRetrofitInstance()
                .create(ApiService.class)
                .createCart(cart);

        createCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("ProductDetailActivity", "Cart created successfully for user: " + userId);
                    Snackbar.make(findViewById(android.R.id.content), "Product Added to Cart!", Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.e("ProductDetailActivity", "Failed to create cart: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ProductDetailActivity", "Failed to create cart", t);
            }
        });
    }


//    Intent intent = new Intent(ProductDetailActivity.this, Main.class);
//    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear previous activities
//    startActivity(intent);
//    finish();
}

