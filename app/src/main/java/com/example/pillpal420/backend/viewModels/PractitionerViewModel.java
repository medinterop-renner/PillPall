package com.example.pillpal420.backend.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pillpal420.backend.dataModels.PractitionerDataModel;
import com.example.pillpal420.backend.repository.PractitionerRepository;

/**
 * Diese Klasse ist für ein späteres Feature in der App gedacht und soll deshalb nicht gelöscht werden!
 */
public class PractitionerViewModel extends ViewModel {

    private MutableLiveData<PractitionerDataModel> practitionerLiveData;
    private PractitionerRepository practitionerRepository;

    public PractitionerViewModel(){
        practitionerLiveData = new MutableLiveData<>();
        practitionerRepository = new PractitionerRepository();
    }

    public LiveData<PractitionerDataModel> getPractitionerLiveData(){
        return practitionerLiveData;
    }
    public void fetchPractitionerData(String practitionerId){
        practitionerRepository.getPractitioner(practitionerId, new PractitionerRepository.PractitionerCallback() {
            @Override
            public void onResponse(PractitionerDataModel practitionerDataModel) {

                practitionerLiveData.postValue(practitionerDataModel);
                Log.d("Practitioner","Practitioner added to Live Data");
            }

            @Override
            public void onFailure(Exception e) {
                Log.d("Practitioner","Error while fetching Practitioner");
                e.printStackTrace();

            }
        });
    }

    public LiveData<PractitionerDataModel> postPractitionerRessource(PractitionerDataModel practitionerDataModel){
        Log.d("Practitioner","Preparing to post new Practitioner to server");

        return practitionerRepository.postPractitionerRessource(practitionerDataModel);
    }
}
