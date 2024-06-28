package com.example.pillpal420.backend;


import com.example.pillpal420.backend.roomDB.CorePractitionerProfil;

public interface CorePractitionerDataCallback {

    void onPractitionerDataLoaded(CorePractitionerProfil practitionerProfil);
    void onDataNotAvailable();
}
