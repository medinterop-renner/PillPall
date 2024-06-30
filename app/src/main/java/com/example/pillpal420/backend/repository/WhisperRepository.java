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

/**
 * Repository used to search for the patient by the family name and passes back data to the Whisper view model
 */
public class WhisperRepository {
    String serverAddresse = "192.168.0.2:8080";

    /**
     * Callback interface for WhisperRepository
     */
    public interface WhisperCallback {
        /**
         * wird abgerufen wenn erfolgreich patient datamodel erstellt wurde
         *
         * @param patientDataModel patientdatamodel object
         */
        void onResponse(PatientDataModel patientDataModel);

        /**
         * error während des get requests.
         *
         * @param e exception encountered during the process.
         */
        void onFailure(Exception e);
    }

    /**
     * sucht nach einem Patient auf dem FHIR server anhand des family namens und returniert ein PatientDataModel.
     *
     * @param familyNameForFHIRSearch Family name used for the FHIR search.
     * @param callback                callback to handle the response or failure.
     */
    public void searchPatientWithNameGetBackPatientObject(String familyNameForFHIRSearch, WhisperCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String urlPatient = " http://" + serverAddresse + "/hapi-fhir-jpaserver/fhir/Patient?family=" + familyNameForFHIRSearch;
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
                    Log.d(LogTag.WHISPER.getTag(), patientDataModel.toString());
                    callback.onResponse(patientDataModel);
                }
            }
        });

    }
}
