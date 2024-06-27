package com.example.pillpal420.backend;

import com.example.pillpal420.backend.roomDB.CorePatientProfil;

public interface CorePatientDataCallback {

    void onPatientDataLoaded(CorePatientProfil patient);
    void onDataNotAvailable();
}
