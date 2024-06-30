package com.example.pillpal420.backend.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pillpal420.backend.dataModels.FullPrescriptionDataModel;
import com.example.pillpal420.backend.repository.FullPrescriptionRepository;
import com.example.pillpal420.documentation.LogTag;

import java.util.List;

public class FullPrescriptionViewModel extends AndroidViewModel {
    private FullPrescriptionRepository repository;
    private MutableLiveData<List<FullPrescriptionDataModel>> fullPrescriptionLiveData;


    public FullPrescriptionViewModel(@NonNull Application application) {
        super(application);
        repository = new FullPrescriptionRepository();
        fullPrescriptionLiveData = new MutableLiveData<>();
    }

    /**
     * Gibt ein LiveData Objekt mit einer Liste von FullPrescriptionDataModel Objekten zurück
     *
     * @return LiveData Objekt mit einer Liste von FullPrescriptionDataModel Objekten
     */
    public LiveData<List<FullPrescriptionDataModel>> getFullPrescriptionLiveData() {
        return fullPrescriptionLiveData;
    }

    /**
     * Erhält das FullPrescription Obkelt für eine gegebene patientId und aktualisiert das LiveData Objekt mit dem Ergebnis
     * <p>
     * Funktion:
     * 1. Erhält sich die fullMedicationRequest für die gegebene patientId
     * 2. Callback:
     * --> erfolgreich: aktualisiert fullPrescriptionLiveData mit der erhaltenen Liste der FullPrescriptionDataModel Objekte
     * --> nicht erfolgreich: Log und printStackTrace, setzt fullPrescriptionLiveData auf nullgit
     *
     * @param patientId id des Patienten dessen FullPrescription erhalten werden soll
     */
    public void fetchFullPrescriptions(String patientId) {
        repository.getFullMedicationRequests(patientId, new FullPrescriptionRepository.FullPrescriptionRepositoryCallback() {
            @Override
            public void onResponse(List<FullPrescriptionDataModel> fullPrescriptionDataModels) {
                Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "successfully posted to live data");
                fullPrescriptionLiveData.postValue(fullPrescriptionDataModels);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Error in view model");
                fullPrescriptionLiveData.postValue(null);
            }
        });
    }
}