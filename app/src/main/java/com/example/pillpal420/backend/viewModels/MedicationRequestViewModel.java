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

    /**
     * Konstruktor
     */
    public MedicationRequestViewModel(){
    medicationRequestDataModelMutableLiveData = new MutableLiveData<>();
    medicationRequestRepository = new MedicationRequestRepository();
}

    /**
     * Getter für medicationRequestDataModelMutableLiveData
     * @return medicationRequestDataModelMutableLiveData
     */
    public LiveData<List<MedicationRequestDataModel>> getMedicationRequestDataModelMutableLiveData() {
        return medicationRequestDataModelMutableLiveData;
    }

    /**
     * Erhält eine MedicationRequest basierend auf der übergebenen ID
     *
     * Funktion:
     * 1. Ruft die getMedicationRequest Methode basierend auf der patientId auf
     * 2. Callback:
     *    --> erfolgreich: aktualisiert die medicationRequestDataModelMutableLiveData mit den erhaltenen medicationRequestDataModel
     *    --> nicht erfolgreich: Log und printStackTrace
     *
     * @param patientId ID des Patienten dessen MedicationRequest erhalten werden soll
     */
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


    /**
     * 
     * @param medicationRequest
     * @return
     */

    public LiveData<MedicationRequestDataModel> postMedicationRequest(MedicationRequestDataModel medicationRequest) {
    Log.d("MedicationRequest","Posting Medication Request, in ViewModel class");



        return medicationRequestRepository.postMedicationRequest(medicationRequest);
    }




}
