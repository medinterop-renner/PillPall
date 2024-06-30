package com.example.pillpal420.backend;


import com.example.pillpal420.backend.roomDB.CorePractitionerProfil;

/**
 * Callback interface für handling von CorePractitionerProfil data operations in roomdb.
 * implementations von CorePractitionerDataCallback werden dazu verwendet um die Ergebnisse von asynchronous db operations zu handlen. Dieses interface wird
 * momentan noch nicht verwendet da die funktionalität in der App noch fehlt. es wird in einem zukünftigen update implementiert.
 */
public interface CorePractitionerDataCallback {
    /**
     * wrid aufgerufen wenn practitioner data erfolgreich in roomdb gespeichert wird.
     *
     * @param practitionerProfil The loaded practitioner profile.
     */
    void onPractitionerDataLoaded(CorePractitionerProfil practitionerProfil);

    /**
     * wird gecalled wenn corepractitonerprofile in der roomdb nicht available ist.
     */
    void onDataNotAvailable();
}
