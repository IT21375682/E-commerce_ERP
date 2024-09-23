package com.example.eadecommerce.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.eadecommerce.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ProfileFragment extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        loadingProgressBar = view.findViewById(R.id.loadingAccManProgressBar);
        toolbarLayout = view.findViewById(R.id.cusAccManagementHeading);
        constraintLayout = view.findViewById(R.id.commonHomeConstraint1);

        // Set swipe refresh listener (if needed)
        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshContent();
        });

        profileEditImage = view.findViewById(R.id.profileEditImage);

        cusAccountProfileImage = view.findViewById(R.id.cusAccountProfileImage);
        cusAccountProfileImageEditFrame = view.findViewById(R.id.cusAccountProfileImageEditFrame);

        editTextFirstName = view.findViewById(R.id.editTextFirstName);
        viewInputFirstName = view.findViewById(R.id.editTextFirstName2);
        editTextLastName = view.findViewById(R.id.viewInputLastName);
        viewInputLastName = view.findViewById(R.id.viewInputLastName2);
        editTextNIC = view.findViewById(R.id.viewInputNIC);
        viewInputNIC = view.findViewById(R.id.viewInputNIC2);
        editTextPhoneNo = view.findViewById(R.id.viewInputPhoneNo);
        viewInputPhoneNo = view.findViewById(R.id.viewInputPhoneNo2);
        cusAccountAddressLine1 = view.findViewById(R.id.cusAccountAddressLine1);
        cusAccountAddressLine2 = view.findViewById(R.id.cusAccountAddressLine2);
        cusAccountAddressLine3 = view.findViewById(R.id.cusAccountAddressLine3);
        cusAccountAddressLine11 = view.findViewById(R.id.cusAccountAddressLine11);
        cusAccountAddressLine22 = view.findViewById(R.id.cusAccountAddressLine22);
        cusAccountAddressLine33 = view.findViewById(R.id.cusAccountAddressLine33);
        cusAccountCity1 = view.findViewById(R.id.cusAccountCity1);
        cusAccountPostalCode1 = view.findViewById(R.id.cusAccountPostalCode1);
        cusAccountCountry1 = view.findViewById(R.id.cusAccountCountry1);
        cusAccountCity2 = view.findViewById(R.id.cusAccountCity2);
        cusAccountPostalCode2 = view.findViewById(R.id.cusAccountPostalCode2);
        cusAccountCountry2 = view.findViewById(R.id.cusAccountCountry2);

        cusAccountButtons = view.findViewById(R.id.cusAccountButtons);

        // Set click listener on the profile edit icon
        profileEditImage.setOnClickListener(v -> {
            toggleEditMode();
        });

        Button changeImageButton = cusAccountProfileImageEditFrame.findViewById(R.id.cusAccManageButton1);
        Button removeImageButton = cusAccountProfileImageEditFrame.findViewById(R.id.cusAccManageButton2);
        Button btnCusUpdate = cusAccountButtons.findViewById(R.id.btnCusUpdate);
        Button btnCusCancel = cusAccountButtons.findViewById(R.id.btnCusCancel);


        // Set click listener on the profile edit icon
        btnCusCancel.setOnClickListener(v -> {
            toggleEditMode();
            removeImage();
        });

        changeImageButton.setOnClickListener(v -> openGallery());
        removeImageButton.setOnClickListener(v -> removeImage());

        return view;
    }

    private void refreshContent() {
        // Simulate loading process
        loadingProgressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void toggleEditMode() {

        // Toggle visibility of update
        if (profileEditImage.getVisibility() == View.GONE) {
            profileEditImage.setVisibility(View.VISIBLE);
        } else {
            profileEditImage.setVisibility(View.GONE);
        }


        // Toggle visibility of the update buttons
        if (cusAccountButtons.getVisibility() == View.GONE) {
            cusAccountButtons.setVisibility(View.VISIBLE);
        } else {
            cusAccountButtons.setVisibility(View.GONE);
        }

        // Toggle visibility of the image edit buttons
        if (cusAccountProfileImageEditFrame.getVisibility() == View.GONE) {
            cusAccountProfileImageEditFrame.setVisibility(View.VISIBLE);
        } else {
            cusAccountProfileImageEditFrame.setVisibility(View.GONE);
        }

        // Toggle visibility of First Name
        if (editTextFirstName.getVisibility() == View.GONE) {
            editTextFirstName.setVisibility(View.VISIBLE);
            viewInputFirstName.setVisibility(View.GONE);
        } else {
            editTextFirstName.setVisibility(View.GONE);
            viewInputFirstName.setVisibility(View.VISIBLE);
        }

        // Toggle visibility of PLast Name
        if (editTextLastName.getVisibility() == View.GONE) {
            editTextLastName.setVisibility(View.VISIBLE);
            viewInputLastName.setVisibility(View.GONE);
        } else {
            editTextLastName.setVisibility(View.GONE);
            viewInputLastName.setVisibility(View.VISIBLE);
        }

        // Toggle visibility of NIC
        if (editTextNIC.getVisibility() == View.GONE) {
            editTextNIC.setVisibility(View.VISIBLE);
            viewInputNIC.setVisibility(View.GONE);
        } else {
            editTextNIC.setVisibility(View.GONE);
            viewInputNIC.setVisibility(View.VISIBLE);
        }

        // Toggle visibility of Phone No
        if (editTextPhoneNo.getVisibility() == View.GONE) {
            editTextPhoneNo.setVisibility(View.VISIBLE);
            viewInputPhoneNo.setVisibility(View.GONE);
        } else {
            editTextPhoneNo.setVisibility(View.GONE);
            viewInputPhoneNo.setVisibility(View.VISIBLE);
        }

        // Toggle visibility of Address 1
        if (cusAccountAddressLine1.getVisibility() == View.GONE) {
            cusAccountAddressLine1.setVisibility(View.VISIBLE);
            cusAccountAddressLine11.setVisibility(View.GONE);
        } else {
            cusAccountAddressLine1.setVisibility(View.GONE);
            cusAccountAddressLine11.setVisibility(View.VISIBLE);
        }

        // Toggle visibility of Address 2
        if (cusAccountAddressLine2.getVisibility() == View.GONE) {
            cusAccountAddressLine2.setVisibility(View.VISIBLE);
            cusAccountAddressLine22.setVisibility(View.GONE);
        } else {
            cusAccountAddressLine2.setVisibility(View.GONE);
            cusAccountAddressLine22.setVisibility(View.VISIBLE);
        }

        // Toggle visibility of Address 3
        if (cusAccountAddressLine3.getVisibility() == View.GONE) {
            cusAccountAddressLine3.setVisibility(View.VISIBLE);
            cusAccountAddressLine33.setVisibility(View.GONE);
        } else {
            cusAccountAddressLine3.setVisibility(View.GONE);
            cusAccountAddressLine33.setVisibility(View.VISIBLE);
        }

        // Toggle visibility of City
        if (cusAccountCity1.getVisibility() == View.GONE) {
            cusAccountCity1.setVisibility(View.VISIBLE);
            cusAccountCity2.setVisibility(View.GONE);
        } else {
            cusAccountCity1.setVisibility(View.GONE);
            cusAccountCity2.setVisibility(View.VISIBLE);
        }

        // Toggle visibility of Postal Code
        if (cusAccountPostalCode1.getVisibility() == View.GONE) {
            cusAccountPostalCode1.setVisibility(View.VISIBLE);
            cusAccountPostalCode2.setVisibility(View.GONE);
        } else {
            cusAccountPostalCode1.setVisibility(View.GONE);
            cusAccountPostalCode2.setVisibility(View.VISIBLE);
        }

        // Toggle visibility of Country
        if (cusAccountCountry1.getVisibility() == View.GONE) {
            cusAccountCountry1.setVisibility(View.VISIBLE);
            cusAccountCountry2.setVisibility(View.GONE);
        } else {
            cusAccountCountry1.setVisibility(View.GONE);
            cusAccountCountry2.setVisibility(View.VISIBLE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    // Update your image picker launcher to save the original drawable
    private final ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        selectedImageUri = data.getData();
                        if (selectedImageUri != null) {
                            // Save the original drawable before changing it
                            if (originalDrawable == null) {
                                originalDrawable = cusAccountProfileImage.getDrawable(); // Save the current drawable
                            }
                            // Set the selected image to the ImageView
                            cusAccountProfileImage.setImageURI(selectedImageUri);
                        }
                    }
                }
            });

    private void removeImage() {
        if (originalDrawable != null) {
            // Reset the image to the original drawable
            cusAccountProfileImage.setImageDrawable(originalDrawable);
            // Optionally clear the selected image URI
            selectedImageUri = null;
        } else {
            // If there's no original drawable, you can set a default image
            cusAccountProfileImage.setImageResource(R.drawable.person);
        }
    }

    private void convertImageToBase64(Uri uri) {
        try {
            // Get the input stream
            InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                bytes.write(buffer, 0, bytesRead);
            }
            byte[] imageBytes = bytes.toByteArray();
            imageData = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            // Log the Base64-encoded image string
            Log.d("Base64ImageString", imageData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
