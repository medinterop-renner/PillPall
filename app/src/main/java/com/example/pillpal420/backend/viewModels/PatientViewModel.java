package com.example.pillpal420.backend.viewModels;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.repository.PatientRepository;

public class PatientViewModel extends ViewModel {
    private MutableLiveData<PatientDataModel> patientLiveData;
    private PatientRepository repository;

    public PatientViewModel() {
        patientLiveData = new MutableLiveData<>();
        repository = new PatientRepository();
    }

    public LiveData<PatientDataModel> getPatientLiveData() {
        return patientLiveData;
    }

    public void fetchPatientData(String patientId) {
        repository.getPatient(patientId, new PatientRepository.PatientCallback() {
            @Override
            public void onResponse(PatientDataModel patientDataModel) {
                patientLiveData.postValue(patientDataModel);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Patient","Error while fetchingPatientData");
                e.printStackTrace();
            }
        });
    }

    public LiveData<PatientDataModel> postPatientRessource(PatientDataModel patientDataModel){
        Log.d("Patient","2. Preparing to post patient ressource from view Model");

       return repository.postPatientRessource(patientDataModel);
    }
}