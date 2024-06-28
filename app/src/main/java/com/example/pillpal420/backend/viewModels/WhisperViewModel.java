package com.example.pillpal420.backend.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pillpal420.backend.Parser;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.repository.PatientRepository;
import com.example.pillpal420.backend.repository.WhisperRepository;
import com.example.pillpal420.documentation.LogTag;

public class WhisperViewModel {

    private MutableLiveData<PatientDataModel> patientLiveData;
    private WhisperRepository repository;

    public WhisperViewModel() {
        patientLiveData = new MutableLiveData<>();
        repository = new WhisperRepository();
    }

    public LiveData<PatientDataModel> getPatientLiveData() {
        return patientLiveData;
    }

    public void fetchPatientData(String familyNameForFHIRSearch) {

       // repository.searchPatientWithNameGetBackPatientObject(familyNameForFHIRSearch,WhisperRepository.WhisperCallback)

        repository.searchPatientWithNameGetBackPatientObject(familyNameForFHIRSearch, new WhisperRepository.WhisperCallback() {
            @Override
            public void onResponse(PatientDataModel patientDataModel) {
                patientLiveData.postValue(patientDataModel);

                // Hier könnte man direkt den neuen Patienten zum Server Posten practitioner muss seine DienstNummer -> "Practioner/ + idfromVoice" string sagen
                // Login nur mit practitioner -> nur whisper sichtabr
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(LogTag.WHISPER.getTag(), "Error while fetchingPatientDataToFindID");
                e.printStackTrace();
            }
        });

    }

    public LiveData<PatientDataModel> postPatientRessource(PatientDataModel patientDataModel){
        Log.d(LogTag.WHISPER.getTag(), "Preparing to post new Patient");

        return null;
        //repository.postPatientRessource(patientDataModel);
    }
}
