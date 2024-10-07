package com.example.eadecommerce.network;

import com.example.eadecommerce.model.Cart;
import com.example.eadecommerce.model.CartProduct;
import com.example.eadecommerce.model.CartResponse;
import com.example.eadecommerce.model.Category;
import com.example.eadecommerce.model.Comment;
import com.example.eadecommerce.model.Order;
import com.example.eadecommerce.model.OrderRequest;
import com.example.eadecommerce.model.Product;
import com.example.eadecommerce.model.ProductCommentData;
import com.example.eadecommerce.model.SingleOrder;
import com.example.eadecommerce.model.User;
import com.example.eadecommerce.model.UserResponse;
import com.example.eadecommerce.model.UserUpdate;
import com.example.eadecommerce.responses.LoginRequest;
import com.example.eadecommerce.responses.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // Logs in a user and returns a LoginResponse
    @POST("api/User/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    // Creates a new user account
    @POST("api/User")
    Call<User> createUser(@Body User newUser);

    // Retrieves a list of all active products
    @GET("api/Product/active-details")
    Call<List<Product>> getAllProducts();

    // Retrieves details of a specific product by its ID
    @GET("api/Product/active-details/{productId}")
    Call<Product> getProductDetails(@Path("productId") String productId);

    // Retrieves comments associated with a specific product
    @GET("api/Comment/product/{productId}")
    Call<List<ProductCommentData>> getCommentsByProductId(@Path("productId") String productId);

    // Adds a new comment or updates an existing one
    @POST("api/Comment/mycomment")
    Call<Comment> addOrUpdateComment(@Body Comment comment);

    // Retrieves the cart associated with a specific user
    @GET("api/Cart/{userId}")
    Call<Cart> getCartByUserId(@Path("userId") String userId);

    // Updates the quantity or details of a product in the cart
    @PATCH("api/Cart/{cartId}/product")
    Call<Void> updateCartProduct(@Path("cartId") String cartId, @Body CartProduct cartProduct);

    // Creates a new cart for a user
    @POST("api/Cart")
    Call<Void> createCart(@Body Cart cart);

    // Retrieves detailed product information from the cart for a specific user
    @GET("api/Cart/{userId}/product-details")
    Call<CartResponse> getCartDetails(@Path("userId") String userId);

    // Updates or removes products from the cart
    @PUT("api/Cart/update-remove/{cartId}")
    Call<Void> updateCart(@Path("cartId") String cartId, @Body Cart cart);

    // Clears the entire cart for a specific user
    @DELETE("api/Cart/{cartId}")
    Call<Void> clearCart(@Path("cartId") String cartId);

    // Retrieves user information by their ID
    @GET("api/User/{id}")
    Call<UserResponse> getUserById(@Path("id") String userId);

    // Creates a new order
    @POST("api/Order")
    Call<Void> createOrder(@Body OrderRequest orderRequest);

    // Updates user information and returns a new JWT token
    @PATCH("api/User/UpdateUser/{id}")
    Call<LoginResponse> updateUser(@Path("id") String id, @Body UserUpdate user);

    // Retrieves a list of orders placed by a specific user
    @GET("api/Order/user/{userId}")
    Call<List<Order>> getUserOrders(@Path("userId") String userId);

    // Retrieves details of a specific order by its ID
    @GET("api/Order/single-order-detail/{id}")
    Call<SingleOrder> getOrderDetails(@Path("id") String orderId);

    // Retrieves a list of active categories
    @GET("api/Category/active-categories")
    Call<List<Category>> getActiveCategories();

    // Retrieves products belonging to a specific category
    @GET("api/Product/active-details/category/{categoryId}")
    Call<List<Product>> getCategoryProducts(@Path("categoryId") String categoryId);

    // Retrieves comments made by a specific user
    @GET("api/Comment/user/{userId}")
    Call<List<ProductCommentData>> getCommentsByUserId(@Path("userId") String userId );

    // Retrieves comments made by a specific vendor
    @GET("api/Comment/vendor/{vendorId}")
    Call<List<ProductCommentData>> getCommentsByVendorId(@Path("vendorId") String vendorId );

    // Retrieves the count of products in a user's cart
    @GET("api/Cart/{userId}/product-count")
    Call<Integer> getCartProductCount(@Path("userId") String userId);

    // Deactivate customer account
    @PUT("api/User/{userId}/my-account-deactivate")
    Call<Void> deactivateAccount(@Path("userId") String userId);
}
