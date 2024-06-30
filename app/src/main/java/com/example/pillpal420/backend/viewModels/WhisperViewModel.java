package com.example.pillpal420.backend.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.repository.WhisperRepository;
import com.example.pillpal420.documentation.LogTag;

public class WhisperViewModel extends ViewModel {

    private MutableLiveData<PatientDataModel> patientLiveData;
    private WhisperRepository repository;

    public WhisperViewModel() {
        patientLiveData = new MutableLiveData<>();
        repository = new WhisperRepository();
    }

    /**
     * Getter für patientLiveData
     *
     * @return patientLiveData
     */
    public LiveData<PatientDataModel> getPatientLiveData() {
        return patientLiveData;
    }

    /**
     * Hier wird die patiendData basierend auf dem Nachnamen geholt und das LiveData Objekt mit dem Ergebnis aktualisiert
     * <p>
     * Funktion:
     * 1. Aufrufen der repository Methode um einen Patienten anhand des Nachnamens zu suchen
     * 2. Callback für:
     * --> erfolgreich: aktualisieren der patientLiveData mit dem erhaltenen PatientDataModel
     * --> nicht erfolgreich: Log und printStackTrace
     *
     * @param familyNameForFHIRSearch familyName des zu suchenden Patienten
     */
    public void fetchPatientData(String familyNameForFHIRSearch) {


        repository.searchPatientWithNameGetBackPatientObject(familyNameForFHIRSearch, new WhisperRepository.WhisperCallback() {
            @Override
            public void onResponse(PatientDataModel patientDataModel) {
                patientLiveData.postValue(patientDataModel);


            }

            @Override
            public void onFailure(Exception e) {
                Log.d(LogTag.WHISPER.getTag(), "Error while fetchingPatientDataToFindID");
                e.printStackTrace();
            }
        });

    }
}
