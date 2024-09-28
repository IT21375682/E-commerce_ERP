package com.example.eadecommerce;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
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

        if (validateInputs(email, password)) {
            cusLoginNoUsernamePassword.setVisibility(View.GONE);
            showLoading();
            // Navigate to Main activity after 2 seconds
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(Login.this, Main.class);
                finish();
                startActivity(intent);
            }, 2000);
        }
    });
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
}
