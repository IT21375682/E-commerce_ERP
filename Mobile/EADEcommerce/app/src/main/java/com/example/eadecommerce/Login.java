package com.example.eadecommerce;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.auth0.android.jwt.JWT;
import com.example.eadecommerce.responses.LoginRequest;
import com.example.eadecommerce.responses.LoginResponse;
import com.example.eadecommerce.network.ApiService;
import com.example.eadecommerce.network.RetrofitClient;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private ConstraintLayout layoutPassword;
    private ImageView worldImageView;
    private FrameLayout cusWalletProgressBarLayout;
    private Button btnLogin;
    private TextView txtSignup;
    private TextView cusLoginNoUsernamePassword;
    private ImageView pwdVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        edtEmail = findViewById(R.id.edt_email);
        pwdVisible = findViewById(R.id.imgPasswordVisibility);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.text_register);
        cusLoginNoUsernamePassword = findViewById(R.id.cus_login_no_username_password);
        layoutPassword = findViewById(R.id.layout_password);

        // Underline "Register" text
        String registerString = "Register";
        SpannableString mSpannableString = new SpannableString(registerString);
        mSpannableString.setSpan(new UnderlineSpan(), 0, mSpannableString.length(), 0);
        txtSignup.setText(mSpannableString);

        // Toggle password visibility
        pwdVisible.setOnClickListener(v -> togglePasswordVisibility());

        // Handle sign-up text click
        txtSignup.setOnClickListener(v -> {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
    });

        // Handle login button click
        btnLogin.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // Log the start of the login process
            Log.d("LoginActivity", "Login button clicked");

            if (validateInputs(email, password)) {
                cusLoginNoUsernamePassword.setVisibility(View.GONE);
                showLoading();

                // Log input values
                Log.d("LoginActivity", "Email: " + email);
                Log.d("LoginActivity", "Password: " + password);

                // Create the login request object
                LoginRequest loginRequest = new LoginRequest(email, password);

                // Call the API
                ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
                Call<LoginResponse> call = apiService.loginUser(loginRequest);

                // Handle the API response
                call.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        hideLoading();
                        // Log the API response
                        Log.d("LoginActivity", "API call successful. Response code: " + response.code());

                        if (response.isSuccessful() && response.body() != null) {
                            // Save token (use SharedPreferences or other storage)
                            String token = response.body().getToken();
                            Log.d("LoginActivity", "Token received: " + token);

                            // Decode the JWT
                            JWT jwt = new JWT(token);
                            String email = jwt.getClaim("email").asString();
                            String role = jwt.getClaim("role").asString();
                            String name = jwt.getClaim("Name").asString();
                            String userId = jwt.getClaim("id").asString();

                            // Log the extracted claims
                            Log.d("Decoded JWT", "Email: " + email);
                            Log.d("Decoded JWT", "Role: " + role);
                            Log.d("Decoded JWT", "Name: " + name);
                            Log.d("Decoded JWT", "User ID: " + userId);

                            SharedPreferences preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("jwt_token", token);
                            editor.apply();
                            Log.d("My jwt_token",token);

                            // Navigate to the main activity
                            Intent intent = new Intent(Login.this, Main.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Handle different error cases
                            // Handle different error cases
                            try {
                                JSONObject errorBody = new JSONObject(response.errorBody().string());
                                String message = errorBody.getString("message"); // Change "Message" to "message"

                                if (message.equals("Your account is not active. Please contact support.")) {
                                    cusLoginNoUsernamePassword.setVisibility(View.VISIBLE);
                                    cusLoginNoUsernamePassword.setText("Your account is not active. Please contact support.");
                                } else {
                                    cusLoginNoUsernamePassword.setVisibility(View.VISIBLE);
                                    cusLoginNoUsernamePassword.setText("Invalid email or password");
                                }

                                Log.e("LoginActivity", "Invalid login attempt. Message: " + message);
                            } catch (Exception e) {
                                Log.e("LoginActivity", "Error parsing error response: " + e.getMessage());
                                cusLoginNoUsernamePassword.setVisibility(View.VISIBLE);
                                cusLoginNoUsernamePassword.setText("An error occurred. Please try again.");
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        hideLoading();
                        cusLoginNoUsernamePassword.setVisibility(View.VISIBLE);
                        cusLoginNoUsernamePassword.setText("Login failed. Please try again.");
                        // Log the error in case the call fails
                        Log.e("LoginActivity", "Login API call failed: " + t.getMessage());
                    }
                });
            }
        });
//        btnLogin.setOnClickListener(v -> {
//        String email = edtEmail.getText().toString().trim();
//        String password = edtPassword.getText().toString().trim();
//
//        if (validateInputs(email, password)) {
//            cusLoginNoUsernamePassword.setVisibility(View.GONE);
//            showLoading();
//            // Navigate to Main activity after 2 seconds
//            new Handler().postDelayed(() -> {
//                Intent intent = new Intent(Login.this, Main.class);
//                finish();
//                startActivity(intent);
//            }, 2000);
//        }
//    });
    }

    // Function to toggle password visibility
    private void togglePasswordVisibility() {
        if (edtPassword.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            pwdVisible.setImageResource(R.drawable.visibility_off);
        } else {
            edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            pwdVisible.setImageResource(R.drawable.visibility);
        }
        // Move the cursor to the end of the text
        edtPassword.setSelection(edtPassword.getText().length());
    }

    // Validate email and password
    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            showError("Enter your email");
            return false;
        }
        if (password.isEmpty()) {
            showError("Enter your password");
            return false;
        }
        return true;
    }

    // Show error message in the UI
    private void showError(String message) {
        cusLoginNoUsernamePassword.setVisibility(View.VISIBLE);
        cusLoginNoUsernamePassword.setText(message);
        Animation blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink_message_box);
        edtEmail.setBackground(ContextCompat.getDrawable(this, R.drawable.edt_background));
        layoutPassword.setBackground(ContextCompat.getDrawable(this, R.drawable.edt_background));
        edtEmail.startAnimation(blinkAnimation);
        layoutPassword.startAnimation(blinkAnimation);
    }

    // Show loading spinner
    private void showLoading() {
        cusWalletProgressBarLayout = findViewById(R.id.cusWalletProgressBarLayout);
        worldImageView = findViewById(R.id.worldImageView);
        cusWalletProgressBarLayout.setVisibility(View.VISIBLE);
        cusWalletProgressBarLayout.setClickable(true);
        cusWalletProgressBarLayout.setFocusable(true);
//        RotateAnimation horizontalRotationAnimation = createHorizontalRotationAnimation();
        TranslateAnimation horizontalMoveAnimation = createHorizontalMoveAnimation();
        worldImageView.startAnimation(horizontalMoveAnimation);
    }

    // Create the horizontal rotation animation for the world image
    private RotateAnimation createHorizontalRotationAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(
            0.0f,
            360.0f,
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f,
            RotateAnimation.RELATIVE_TO_SELF,
            0.5f
        );
        rotateAnimation.setDuration(1000); // Animation duration in milliseconds
        return rotateAnimation;
    }

    // Create the horizontal move animation for the world image (left to right)
    private TranslateAnimation createHorizontalMoveAnimation() {
        // Translate from off-screen left (-100%) to off-screen right (100%)
        TranslateAnimation moveAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f, // Start off-screen to the left
                Animation.RELATIVE_TO_PARENT, 1.0f,  // End off-screen to the right
                Animation.RELATIVE_TO_PARENT, 0.0f,  // No vertical movement
                Animation.RELATIVE_TO_PARENT, 0.0f
        );
        moveAnimation.setDuration(2000); // Animation duration in milliseconds
        moveAnimation.setRepeatCount(Animation.INFINITE); // Repeat indefinitely
        return moveAnimation;
    }

    // Hide loading spinner
    private void hideLoading() {
        if (cusWalletProgressBarLayout != null) {
            cusWalletProgressBarLayout.setVisibility(View.GONE);
            cusWalletProgressBarLayout.setClickable(false);
            cusWalletProgressBarLayout.setFocusable(false);
        }
    }
}
