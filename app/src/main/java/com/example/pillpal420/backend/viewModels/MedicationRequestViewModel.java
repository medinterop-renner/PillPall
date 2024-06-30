package com.example.pillpal420.backend.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pillpal420.backend.dataModels.MedicationRequestDataModel;
import com.example.pillpal420.backend.repository.MedicationRequestRepository;

import java.util.List;

public class MedicationRequestViewModel extends ViewModel {
    private MutableLiveData<List<MedicationRequestDataModel>> medicationRequestDataModelMutableLiveData;
private MedicationRequestRepository medicationRequestRepository;

public MedicationRequestViewModel(){
    medicationRequestDataModelMutableLiveData = new MutableLiveData<>();
    medicationRequestRepository = new MedicationRequestRepository();
}

    public LiveData<List<MedicationRequestDataModel>> getMedicationRequestDataModelMutableLiveData() {
        return medicationRequestDataModelMutableLiveData;
    }
public void fetchMedicationRequest(String patientId){
    medicationRequestRepository.getMedicationRequest(patientId, new MedicationRequestRepository.MedicationRequestCallback() {
        @Override
        public void onResponse(List<MedicationRequestDataModel> medicationRequestDataModel) {
            medicationRequestDataModelMutableLiveData.postValue(medicationRequestDataModel);
            Log.d("MedicationRequest","MedicationRequest posted to live Data");
        }
        @Override
        public void onFailure(Exception e) {
            Log.d("Testing","An Error occurred when getting the medicationRequest ");
                e.printStackTrace();
        }
    });
    }
    public LiveData<MedicationRequestDataModel> postMedicationRequest(MedicationRequestDataModel medicationRequest) {
    Log.d("MedicationRequest","Posting Medication Request, in ViewModel class");



        return medicationRequestRepository.postMedicationRequest(medicationRequest);
    }




}
