package com.example.pillpal420.testingNotForRealUse;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NameViewModel extends ViewModel {
    // Create a LiveData with a String
    private MutableLiveData<String> currentName;
    public MutableLiveData<String> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<String>();
        }
        return currentName;
    }
    // Rest of the ViewModel...
}
