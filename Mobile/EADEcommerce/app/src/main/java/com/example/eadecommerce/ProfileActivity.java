package com.example.eadecommerce;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.auth0.android.jwt.JWT;
import com.example.eadecommerce.R;
import com.example.eadecommerce.fragments.HomeFragment;
import com.example.eadecommerce.model.Address;
import com.example.eadecommerce.model.UserResponse;
import com.example.eadecommerce.model.UserUpdate;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.JwtUtils;
import com.example.eadecommerce.network.RetrofitClient;
import com.example.eadecommerce.responses.LoginResponse;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loadingProgressBar;
    private RelativeLayout toolbarLayout;
    private ConstraintLayout constraintLayout;

    private ImageView profileEditImage, cusAccountProfileImage;
    private EditText cusAccountCity1, cusAccountPostalCode1, cusAccountCountry1;
    private TextView cusAccountCity2, cusAccountPostalCode2, cusAccountCountry2;
    private Button btnCusDeActivate;

    private EditText editTextFirstName, editTextNIC, editTextPhoneNo;
    private EditText cusAccountAddressLine1;
    private TextView viewInputFirstName, viewInputNIC, viewInputPhoneNo;
    private TextView cusAccountAddressLine11;

    private static final int PICK_IMAGE_REQUEST = 1000;
    private String userId;

    private View cusAccountProfileImageEditFrame, cusAccountButtons;

    ImageButton buttonBack, buttonCart;
    ImageView clickcartHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Get the JWT token from SharedPreferences and Decode the token and get the user ID
        String token = JwtUtils.getTokenFromSharedPreferences(this);
        userId = JwtUtils.getUserIdFromToken(token);
        Log.d("userId", userId);

        buttonBack = findViewById(R.id.buttonBack);
        clickcartHome = findViewById(R.id.clickcartHome);
        buttonCart = findViewById(R.id.buttonCart);


        // Handle Home click to load HomeFragment
        buttonCart.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(ProfileActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        // Initialize views
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        loadingProgressBar = findViewById(R.id.loadingAccManProgressBar);
        toolbarLayout = findViewById(R.id.cusAccManagementHeading);
        constraintLayout = findViewById(R.id.commonHomeConstraint1);

        // Set swipe refresh listener (if needed)
        swipeRefreshLayout.setOnRefreshListener(this::refreshContent);

        profileEditImage = findViewById(R.id.profileEditImage);
        btnCusDeActivate = findViewById(R.id.btnCusDeActivate);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        viewInputFirstName = findViewById(R.id.editTextFirstName2);

        editTextNIC = findViewById(R.id.viewInputNIC);
        viewInputNIC = findViewById(R.id.viewInputNIC2);

        editTextPhoneNo = findViewById(R.id.viewInputPhoneNo);
        viewInputPhoneNo = findViewById(R.id.viewInputPhoneNo2);

        cusAccountAddressLine1 = findViewById(R.id.cusAccountAddressLine1);
        cusAccountAddressLine11 = findViewById(R.id.cusAccountAddressLine11);

        cusAccountCity1 = findViewById(R.id.cusAccountCity1);
        cusAccountCity2 = findViewById(R.id.cusAccountCity2);

        cusAccountPostalCode1 = findViewById(R.id.cusAccountPostalCode1);
        cusAccountPostalCode2 = findViewById(R.id.cusAccountPostalCode2);

        cusAccountCountry1 = findViewById(R.id.cusAccountCountry1);
        cusAccountCountry2 = findViewById(R.id.cusAccountCountry2);

        cusAccountButtons = findViewById(R.id.cusAccountButtons);

        // Set click listener on the profile edit icon
        profileEditImage.setOnClickListener(v -> toggleEditMode());

        Button btnCusUpdate = cusAccountButtons.findViewById(R.id.btnCusUpdate);
        Button btnCusCancel = cusAccountButtons.findViewById(R.id.btnCusCancel);

        // Set click listeners for update and cancel buttons
        btnCusUpdate.setOnClickListener(v -> {
            // Check if the name is empty or only contains whitespace
            String updatedName = editTextFirstName.getText().toString().trim();
            if (updatedName.isEmpty()) {
                // Show an error message to the user
                Snackbar.make(findViewById(android.R.id.content), "Name cannot be empty", Snackbar.LENGTH_SHORT).show();
                return; // Exit the method if the name is invalid
            }
            // Call the method to update user details
            updateUserDetails(userId);
        });

        // Set click listeners
        btnCusCancel.setOnClickListener(v -> {
            toggleEditMode();
            getUserDetails(userId);
        });

        getUserDetails(userId);
    }

    private void getUserDetails(String userId) {

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<UserResponse> call = apiService.getUserById(userId);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();

                    // Set user details into the EditText and TextView fields
                    editTextFirstName.setText(user.getName());
                    viewInputFirstName.setText(user.getName());

                    editTextNIC.setText(user.getEmail());
                    viewInputNIC.setText(user.getEmail());

                    editTextPhoneNo.setText(user.getPhone());
                    viewInputPhoneNo.setText(user.getPhone());

                    Address address = user.getAddress();
                    if (address != null) {
                        cusAccountAddressLine1.setText(address.getStreet());
                        cusAccountAddressLine11.setText(address.getStreet());

                        cusAccountCity1.setText(address.getCity());
                        cusAccountCity2.setText(address.getCity());

                        cusAccountPostalCode1.setText(address.getPostalCode());
                        cusAccountPostalCode2.setText(address.getPostalCode());

                        cusAccountCountry1.setText(address.getCountry());
                        cusAccountCountry2.setText(address.getCountry());
                    }
                } else {
                    Log.e("CheckoutActivity", "Failed to fetch user details");
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("CheckoutActivity", "Error: " + t.getMessage());
            }
        });
    }

    // Method to update user details
    private void updateUserDetails(String userId) {
        // Get the updated data from the EditTexts
        String updatedName = editTextFirstName.getText().toString();
        String updatedPhone = editTextPhoneNo.getText().toString();
        String updatedStreet = cusAccountAddressLine1.getText().toString();
        String updatedCity = cusAccountCity1.getText().toString();
        String updatedPostalCode = cusAccountPostalCode1.getText().toString();
        String updatedCountry = cusAccountCountry1.getText().toString();

        // Create an Address object
        Address updatedAddress = new Address(updatedStreet, updatedCity, updatedPostalCode, updatedCountry);
        // Create a User object to hold the updated data
        UserUpdate updatedUser = new UserUpdate(updatedName, updatedPhone, updatedAddress);

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<LoginResponse> call = apiService.updateUser(userId, updatedUser);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Get the new JWT token
                    String token = response.body().getToken();
                    Log.d("ProfileActivity", "Token received: " + token);

                    // Decode the JWT
                    JWT jwt = new JWT(token);
                    String email = jwt.getClaim("email").asString();
                    String role = jwt.getClaim("role").asString();
                    String name = jwt.getClaim("Name").asString();
                    String userId = jwt.getClaim("id").asString();

                    // Log the extracted claims
                    Log.d("Decoded JWT","Email: "+email);
                    Log.d("Decoded JWT","Role: "+role);
                    Log.d("Decoded JWT","Name: "+name);
                    Log.d("Decoded JWT","User ID: "+userId);

                    // Save the token in SharedPreferences
                    SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("jwt_token", token);
                    editor.apply();

                    // Notify user of success and refresh user details
                    Snackbar.make(findViewById(android.R.id.content), "User updated successfully!", Snackbar.LENGTH_SHORT).show();
                    toggleEditMode();
                    getUserDetails(userId);
                } else {
                    Log.e("ProfileActivity", "Failed to update user details");
                    Snackbar.make(findViewById(android.R.id.content), "Failed to update user", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("ProfileActivity", "Error: " + t.getMessage());
                Snackbar.make(findViewById(android.R.id.content), "Error occurred while updating user", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshContent() {
        swipeRefreshLayout.setRefreshing(true);
        // Restart the activity
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void toggleEditMode() {
        // Toggle visibility of edit options (update, edit buttons, etc.)
        toggleViewVisibility(profileEditImage);
        toggleViewVisibility(btnCusDeActivate);
        toggleViewVisibility(cusAccountButtons);

        // Toggle visibility of input fields (First Name, Phone)
        toggleViewPair(editTextFirstName, viewInputFirstName);
        toggleViewPair(editTextPhoneNo, viewInputPhoneNo);

        // Toggle address lines
        toggleViewPair(cusAccountAddressLine1, cusAccountAddressLine11);

        // Toggle city, postal code, and country fields
        toggleViewPair(cusAccountCity1, cusAccountCity2);
        toggleViewPair(cusAccountPostalCode1, cusAccountPostalCode2);
        toggleViewPair(cusAccountCountry1, cusAccountCountry2);
    }

    private void toggleViewVisibility(View view) {
        if (view.getVisibility() == View.GONE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void toggleViewPair(View editView, View displayView) {
        if (editView.getVisibility() == View.GONE) {
            editView.setVisibility(View.VISIBLE);
            displayView.setVisibility(View.GONE);
        } else {
            editView.setVisibility(View.GONE);
            displayView.setVisibility(View.VISIBLE);
        }
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

}
