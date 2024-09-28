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

public class SignUp extends AppCompatActivity {

    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private ImageView pwdVisible;
    private ImageView pwdConfirmVisible;
    private ConstraintLayout layoutPassword;
    private ConstraintLayout layoutConfirmPassword;
    private Button btnSignup;
    private TextView cusLoginNoUsernamePassword;
    private TextView cusMatchingPassword;
    private TextView txtLogin;
    private FrameLayout cusWalletProgressBarLayout;
    private ImageView worldImageView;
    private TextView worldImageViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        pwdVisible = findViewById(R.id.imgPasswordVisibility);
        pwdConfirmVisible = findViewById(R.id.imgConfirmPasswordVisibility);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_password);
        btnSignup = findViewById(R.id.btnSignup);
        txtLogin = findViewById(R.id.text_login);
        cusLoginNoUsernamePassword = findViewById(R.id.cus_login_no_username_password);
        cusMatchingPassword = findViewById(R.id.cus_matching_password);
        cusWalletProgressBarLayout = findViewById(R.id.cusWalletProgressBarLayout);
        worldImageView = findViewById(R.id.worldImageView);
        worldImageViewText = findViewById(R.id.worldImageViewText);
        layoutPassword = findViewById(R.id.layout_password);
        layoutConfirmPassword = findViewById(R.id.layout_confirm_password);

        // Underline "Login" text
        String registerString = "Login";
        SpannableString mSpannableString = new SpannableString(registerString);
        mSpannableString.setSpan(new UnderlineSpan(), 0, mSpannableString.length(), 0);
        txtLogin.setText(mSpannableString);

        // Toggle password visibility
        pwdVisible.setOnClickListener(v -> togglePasswordVisibility(edtPassword, pwdVisible));
        pwdConfirmVisible.setOnClickListener(v -> togglePasswordVisibility(edtConfirmPassword, pwdConfirmVisible));

        // Handle login text click
        txtLogin.setOnClickListener(v -> startActivity(new Intent(this, Login.class)));

        // Handle signup button click
        btnSignup.setOnClickListener(v -> {
            validateInputsAndProceed();
        });
    }

    private void togglePasswordVisibility(EditText editText, ImageView imageView) {
        if (editText.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imageView.setImageResource(R.drawable.visibility_off);
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imageView.setImageResource(R.drawable.visibility);
        }
        // Move the cursor to the end of the text
        editText.setSelection(editText.getText().length());
    }

    private void validateInputsAndProceed() {
        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String confirmPassword = edtConfirmPassword.getText().toString().trim();

        cusLoginNoUsernamePassword.setVisibility(View.GONE);
        cusMatchingPassword.setVisibility(View.GONE);

        boolean isValid = true;

        if (firstName.isEmpty()) {
            highlightField(edtFirstName);
            isValid = false;
        }
        if (lastName.isEmpty()) {
            highlightField(edtLastName);
            isValid = false;
        }
        if (email.isEmpty()) {
            highlightField(edtEmail);
            isValid = false;
        }
        if (password.isEmpty()) {
            highlightField(layoutPassword);
            isValid = false;
        }
        if (confirmPassword.isEmpty()) {
            highlightField(layoutConfirmPassword);
            isValid = false;
        }
        if (!password.equals(confirmPassword)) {
            cusMatchingPassword.setText("Passwords don't match");
            cusMatchingPassword.setVisibility(View.VISIBLE);
            highlightField(layoutConfirmPassword);
            isValid = false;
        }

        if (isValid) {
            showLoading();
            // Simulate starting the main activity
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(SignUp.this, Main.class);
                startActivity(intent);
                finish();
            }, 2000);
        }
    }

    private void highlightField(View view) {
        view.setBackground(ContextCompat.getDrawable(this, R.drawable.edt_background));
        view.startAnimation(AnimationUtils.loadAnimation(this, R.anim.blink_message_box));
    }

    private void showLoading() {
        cusWalletProgressBarLayout.setVisibility(View.VISIBLE);
        TranslateAnimation horizontalMoveAnimation = createHorizontalMoveAnimation();
        worldImageView.startAnimation(horizontalMoveAnimation);
    }

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
