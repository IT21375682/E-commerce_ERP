package com.example.eadecommerce;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.eadecommerce.fragments.CanceledOrderFragment;
import com.example.eadecommerce.fragments.CompletedOrderFragment;
import com.example.eadecommerce.fragments.PendingOrderFragment;

public class OrdersActivity extends AppCompatActivity {

    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        buttonBack = findViewById(R.id.buttonBack);
        // Set click listener for the back button
        buttonBack.setOnClickListener(v -> onBackPressed());

        LinearLayout linearNavbarItem1 = findViewById(R.id.linearNavbarItem1);
        LinearLayout linearNavbarItem2 = findViewById(R.id.linearNavbarItem2);
        LinearLayout linearNavbarItem3 = findViewById(R.id.linearNavbarItem3);

        // Set default fragment
        if (savedInstanceState == null) {
            setFragment(new PendingOrderFragment());
            updateUIForActiveFragment(1); // Active fragment is Pending
        }

        // Set up click listeners for the navigation items
        linearNavbarItem1.setOnClickListener(v -> {
            setFragment(new PendingOrderFragment());
            updateUIForActiveFragment(1);
        });

        linearNavbarItem2.setOnClickListener(v -> {
            setFragment(new CompletedOrderFragment());
            updateUIForActiveFragment(2);
        });

        linearNavbarItem3.setOnClickListener(v -> {
            setFragment(new CanceledOrderFragment());
            updateUIForActiveFragment(3);
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.cusTransactionMethodFragmentContainer, fragment);
        transaction.commit();
    }

    private void updateUIForActiveFragment(int activeFragment) {
        // Reset all fragment views
        resetFragmentViews();

        // Update the active fragment's UI
        if (activeFragment == 1) { // Pending
            ((ImageView) findViewById(R.id.imageFolder1)).setImageResource(R.drawable.pending_on);
            ((TextView) findViewById(R.id.txtMyparcels)).setTextColor(getResources().getColor(R.color.ordersOnTextColor));
        } else if (activeFragment == 2) { // Completed
            ((ImageView) findViewById(R.id.imageFolder2)).setImageResource(R.drawable.completed_on);
            ((TextView) findViewById(R.id.txtHome)).setTextColor(getResources().getColor(R.color.ordersOnTextColor));
        } else if (activeFragment == 3) { // Canceled
            ((ImageView) findViewById(R.id.imageFolder3)).setImageResource(R.drawable.cancel_on);
            ((TextView) findViewById(R.id.txtMyDeliveries)).setTextColor(getResources().getColor(R.color.ordersOnTextColor));
        }
    }

    private void resetFragmentViews() {
        // Reset Pending
        ((ImageView) findViewById(R.id.imageFolder1)).setImageResource(R.drawable.pending);
        ((TextView) findViewById(R.id.txtMyparcels)).setTextColor(getResources().getColor(R.color.ordersTextColor));

        // Reset Completed
        ((ImageView) findViewById(R.id.imageFolder2)).setImageResource(R.drawable.completed);
        ((TextView) findViewById(R.id.txtHome)).setTextColor(getResources().getColor(R.color.ordersTextColor));

        // Reset Canceled
        ((ImageView) findViewById(R.id.imageFolder3)).setImageResource(R.drawable.cancel);
        ((TextView) findViewById(R.id.txtMyDeliveries)).setTextColor(getResources().getColor(R.color.ordersTextColor));
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