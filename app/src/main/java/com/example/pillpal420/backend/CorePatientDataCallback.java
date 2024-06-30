package com.example.pillpal420.backend;

import com.example.pillpal420.backend.roomDB.CorePatientProfil;

/**
 * Callback interface für das handling von corepatientdata operations in roomdb.
 * Implementations von diesem interface werden dazu verwendet um asychronous database operations zu handlen im bezug auf corepatientprofiles.
 */
public interface CorePatientDataCallback {
    /**
     * wird aufgerufen wenn corepatientprofile data erfolgreich aus der DB retrieved wird.
     *
     * @param patient loaded corepatientprofil
     */
    void onPatientDataLoaded(CorePatientProfil patient);

    /**
     * wird aufgerufen wenn corepatientprofil data nicht in room db available ist.
     */
    void onDataNotAvailable();
}
