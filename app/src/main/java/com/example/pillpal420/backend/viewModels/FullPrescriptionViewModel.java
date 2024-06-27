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

    public LiveData<List<FullPrescriptionDataModel>> getFullPrescriptionLiveData() {
        return fullPrescriptionLiveData;
    }

    public void fetchFullPrescriptions(String patientId) {
        repository.getFullMedicationRequests(patientId, new FullPrescriptionRepository.FullPrescriptionRepositoryCallback() {
            @Override
            public void onResponse(List<FullPrescriptionDataModel> fullPrescriptionDataModels) {
                Log.d(LogTag.FULL_PRESCRIPTION.getTag(),"successfully posted to live data");
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