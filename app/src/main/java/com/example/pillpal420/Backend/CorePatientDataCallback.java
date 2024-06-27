package com.example.pillpal420.Backend;

import com.example.pillpal420.Backend.RoomDB.CorePatientProfil;

public interface CorePatientDataCallback {

    void onPatientDataLoaded(CorePatientProfil patient);
    void onDataNotAvailable();
}
