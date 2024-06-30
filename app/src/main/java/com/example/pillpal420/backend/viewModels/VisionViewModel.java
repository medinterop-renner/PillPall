package com.example.pillpal420.backend.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.pillpal420.backend.dataModels.PractitionerDataModel;
import com.example.pillpal420.backend.repository.VisionRepository;
import com.example.pillpal420.documentation.LogTag;

/**
 * Diese Klasse wird erst für ein späteres Feature in der App vorgedacht und soll daher nicht gelöscht werden!
 */
public class VisionViewModel extends ViewModel {

    private MutableLiveData<PractitionerDataModel> practitionerLiveData;
    private VisionRepository repository;

    public VisionViewModel(){
        practitionerLiveData = new MutableLiveData<>();
        repository = new VisionRepository();
    }

    public LiveData<PractitionerDataModel> getPractitionerLiveData(){return practitionerLiveData;}

    public void fetchPatientData(String familyNameForFHIRSearch){
        repository.searchPatientWithNameGetBackPatientObject(familyNameForFHIRSearch, new VisionRepository.VisionCallback() {
            @Override
            public void onResponse(PractitionerDataModel practitionerDataModel) {
                practitionerLiveData.postValue(practitionerDataModel);
                // we live
            }

            @Override
            public void onFailure(Exception e) {
            Log.d(LogTag.VISION.getTag(), "Error while fetching da Doktor");
            e.printStackTrace();
            }
        });
    }

}
