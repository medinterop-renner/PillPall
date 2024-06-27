package com.example.pillpal420.testingNotForRealUse;


import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class NameActivity extends AppCompatActivity {

    TextView nameTextView = findViewById(R.id.nameTextView);
    private NameViewModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Other code to setup the activity...
        // Get the ViewModel.
        model = new ViewModelProvider(this).get(NameViewModel.class);
        // Create the observer which updates the UI.
        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.

                nameTextView.setText(newName);
            } };

// Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.getCurrentName().observe(this, nameObserver);
    }
}