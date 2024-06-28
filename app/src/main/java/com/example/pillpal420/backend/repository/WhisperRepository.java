package com.example.pillpal420.backend.repository;

import android.util.Log;

import com.example.pillpal420.backend.Parser;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.documentation.LogTag;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WhisperRepository {


    public interface WhisperCallback {
        void onResponse(PatientDataModel patientDataModel);
        void onFailure(Exception e);
    }

    public void searchPatientWithNameGetBackPatientObject(String familyNameForFHIRSearch, WhisperCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String urlPatient = " http://192.168.0.2:8080/hapi-fhir-jpaserver/fhir/Patient?family=" + familyNameForFHIRSearch;

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
                    Log.d(LogTag.WHISPER.getTag(), responseBody.toString());
                    PatientDataModel patientDataModel = new Parser().createPatient(responseBody);
                    callback.onResponse(patientDataModel);
                }
            }
        });

    }
}
