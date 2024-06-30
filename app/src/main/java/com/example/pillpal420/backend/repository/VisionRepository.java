package com.example.pillpal420.backend.repository;

import android.util.Log;

import com.example.pillpal420.backend.Parser;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.dataModels.PractitionerDataModel;
import com.example.pillpal420.documentation.LogTag;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VisionRepository {
    String serverAddress = "192.168.0.2:8080";

    public interface VisionCallback {
        void onResponse(PractitionerDataModel practitionerDataModel);

        void onFailure(Exception e);
    }

    public void searchPatientWithNameGetBackPatientObject(String familyNameForFHIRSearch, VisionCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String urlPatient = " http://" + serverAddress + "/hapi-fhir-jpaserver/fhir/Practitioner?family=" + familyNameForFHIRSearch;
        Request request = new Request.Builder()
                .url(urlPatient)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(new IOException("Unexpected code " + response));
                } else {
                    String responseBody = response.body().string();
                    Log.d(LogTag.VISION.getTag(), responseBody.toString());
                    PractitionerDataModel practitionerDataModel = new Parser().createPractitioner(responseBody);
                    Log.d(LogTag.VISION.getTag(), practitionerDataModel.toString());
                    callback.onResponse(practitionerDataModel);
                }
            }
        });
    }
}
