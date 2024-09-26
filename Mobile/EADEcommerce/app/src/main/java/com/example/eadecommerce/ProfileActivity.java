package com.example.eadecommerce;

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

import com.example.eadecommerce.R;
import com.example.eadecommerce.fragments.HomeFragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loadingProgressBar;
    private RelativeLayout toolbarLayout;
    private ConstraintLayout constraintLayout;

    private ImageView profileEditImage, cusAccountProfileImage;
    private EditText cusAccountCity1, cusAccountPostalCode1, cusAccountCountry1;
    private TextView cusAccountCity2, cusAccountPostalCode2, cusAccountCountry2;

    private EditText editTextFirstName, editTextLastName, editTextNIC, editTextPhoneNo;
    private EditText cusAccountAddressLine1, cusAccountAddressLine2, cusAccountAddressLine3;
    private TextView viewInputFirstName, viewInputLastName, viewInputNIC, viewInputPhoneNo;
    private TextView cusAccountAddressLine11, cusAccountAddressLine22, cusAccountAddressLine33;

    private static final int PICK_IMAGE_REQUEST = 1000;
    private Uri selectedImageUri;
    private Uri originalImageUri;
    private String imageData;
    private Drawable originalDrawable;

    // Other member variables
    private View cusAccountProfileImageEditFrame, cusAccountButtons;

    ImageButton buttonBack, buttonCart;
    ImageView clickcartHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

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

        cusAccountProfileImage = findViewById(R.id.cusAccountProfileImage);
        cusAccountProfileImageEditFrame = findViewById(R.id.cusAccountProfileImageEditFrame);

        editTextFirstName = findViewById(R.id.editTextFirstName);
        viewInputFirstName = findViewById(R.id.editTextFirstName2);
        editTextLastName = findViewById(R.id.viewInputLastName);
        viewInputLastName = findViewById(R.id.viewInputLastName2);
        editTextNIC = findViewById(R.id.viewInputNIC);
        viewInputNIC = findViewById(R.id.viewInputNIC2);
        editTextPhoneNo = findViewById(R.id.viewInputPhoneNo);
        viewInputPhoneNo = findViewById(R.id.viewInputPhoneNo2);
        cusAccountAddressLine1 = findViewById(R.id.cusAccountAddressLine1);
        cusAccountAddressLine2 = findViewById(R.id.cusAccountAddressLine2);
        cusAccountAddressLine3 = findViewById(R.id.cusAccountAddressLine3);
        cusAccountAddressLine11 = findViewById(R.id.cusAccountAddressLine11);
        cusAccountAddressLine22 = findViewById(R.id.cusAccountAddressLine22);
        cusAccountAddressLine33 = findViewById(R.id.cusAccountAddressLine33);
        cusAccountCity1 = findViewById(R.id.cusAccountCity1);
        cusAccountPostalCode1 = findViewById(R.id.cusAccountPostalCode1);
        cusAccountCountry1 = findViewById(R.id.cusAccountCountry1);
        cusAccountCity2 = findViewById(R.id.cusAccountCity2);
        cusAccountPostalCode2 = findViewById(R.id.cusAccountPostalCode2);
        cusAccountCountry2 = findViewById(R.id.cusAccountCountry2);

        cusAccountButtons = findViewById(R.id.cusAccountButtons);

        // Set click listener on the profile edit icon
        profileEditImage.setOnClickListener(v -> toggleEditMode());

        // Set click listener to open full screen image
        cusAccountProfileImage.setOnClickListener(v -> {
            Intent fullScreenIntent = new Intent(ProfileActivity.this, FullScreenImageActivity.class);
            fullScreenIntent.putExtra("productImage", selectedImageUri);
            startActivity(fullScreenIntent);
        });

        Button changeImageButton = cusAccountProfileImageEditFrame.findViewById(R.id.cusAccManageButton1);
        Button removeImageButton = cusAccountProfileImageEditFrame.findViewById(R.id.cusAccManageButton2);
        Button btnCusUpdate = cusAccountButtons.findViewById(R.id.btnCusUpdate);
        Button btnCusCancel = cusAccountButtons.findViewById(R.id.btnCusCancel);

        // Set click listeners
        btnCusCancel.setOnClickListener(v -> {
            toggleEditMode();
            removeImage();
        });

        changeImageButton.setOnClickListener(v -> openGallery());
        removeImageButton.setOnClickListener(v -> removeImage());
    }

    private void refreshContent() {
        // Simulate loading process
        loadingProgressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void toggleEditMode() {
        // Toggle visibility of edit options (update, edit buttons, etc.)
        toggleViewVisibility(profileEditImage);
        toggleViewVisibility(cusAccountButtons);
        toggleViewVisibility(cusAccountProfileImageEditFrame);

        // Toggle visibility of input fields (First Name, Last Name, NIC, etc.)
        toggleViewPair(editTextFirstName, viewInputFirstName);
        toggleViewPair(editTextLastName, viewInputLastName);
        toggleViewPair(editTextNIC, viewInputNIC);
        toggleViewPair(editTextPhoneNo, viewInputPhoneNo);

        // Toggle address lines
        toggleViewPair(cusAccountAddressLine1, cusAccountAddressLine11);
        toggleViewPair(cusAccountAddressLine2, cusAccountAddressLine22);
        toggleViewPair(cusAccountAddressLine3, cusAccountAddressLine33);

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

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedImageUri = data.getData();
                        if (selectedImageUri != null) {
                            if (originalDrawable == null) {
                                originalDrawable = cusAccountProfileImage.getDrawable();
                            }
                            cusAccountProfileImage.setImageURI(selectedImageUri);
                        }
                    }
                }
            });

    private void removeImage() {
        if (originalDrawable != null) {
            cusAccountProfileImage.setImageDrawable(originalDrawable);
            selectedImageUri = null;
        } else {
            cusAccountProfileImage.setImageResource(R.drawable.person);
        }
    }

    private void convertImageToBase64(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bytes.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = bytes.toByteArray();
            imageData = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Log.d("Base64ImageString", imageData);
        } catch (Exception e) {
            e.printStackTrace();
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
