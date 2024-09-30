package com.example.eadecommerce.network;

import com.example.eadecommerce.model.Cart;
import com.example.eadecommerce.model.CartProduct;
import com.example.eadecommerce.model.Comment;
import com.example.eadecommerce.model.Product;
import com.example.eadecommerce.model.ProductCommentData;
import com.example.eadecommerce.model.User;
import com.example.eadecommerce.responses.LoginRequest;
import com.example.eadecommerce.responses.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.GET;
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


}
