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

    @POST("/api/User/login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

    @POST("api/User")
    Call<User> createUser(@Body User newUser);

    @GET("api/Product/active-details")
    Call<List<Product>> getAllProducts();

    @GET("api/Product/active-details/{productId}")
    Call<Product> getProductDetails(@Path("productId") String productId);

    @GET("api/Comment/product/{productId}") // Replace with your actual endpoint
    Call<List<ProductCommentData>> getCommentsByProductId(@Path("productId") String productId);

    @POST("api/Comment/mycomment")
    Call<Comment> addOrUpdateComment(@Body Comment comment);

    @GET("/api/Cart/{userId}")
    Call<Cart> getCartByUserId(@Path("userId") String userId);

    @PATCH("/api/Cart/{cartId}/product")
    Call<Void> updateCartProduct(@Path("cartId") String cartId, @Body CartProduct cartProduct);

    @POST("/api/Cart")
    Call<Void> createCart(@Body Cart cart);

    @GET("api/Cart/{userId}/product-details")
    Call<CartResponse> getCartDetails(@Path("userId") String userId);

    @PUT("api/Cart/update-remove/{cartId}")
    Call<Void> updateCart(@Path("cartId") String cartId, @Body Cart cart);

    @DELETE("/api/Cart/{cartId}")
    Call<Void> clearCart(@Path("cartId") String cartId);

    @GET("api/User/{id}")
    Call<UserResponse> getUserById(@Path("id") String userId);

    @POST("/api/Order")
    Call<Void> createOrder(@Body OrderRequest orderRequest);

    @PATCH("/api/User/UpdateUser/{id}")
    Call<LoginResponse> updateUser(@Path("id") String id, @Body UserUpdate user);

    @GET("/api/Order/user/{userId}")
    Call<List<Order>> getUserOrders(@Path("userId") String userId);

    @GET("/api/Order/single-order-detail/{id}")
    Call<SingleOrder> getOrderDetails(@Path("id") String orderId);

    @GET("api/Category/active-categories")
    Call<List<Category>> getActiveCategories();

    @GET("api/Product/active-details/category/{categoryId}")
    Call<List<Product>> getCategoryProducts(@Path("categoryId") String categoryId);

    @GET("api/Comment/user/{userId}") // Replace with your actual endpoint
    Call<List<ProductCommentData>> getCommentsByUserId(@Path("userId") String userId );
}
