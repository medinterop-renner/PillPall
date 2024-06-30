package com.example.pillpal420.backend.repository;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pillpal420.backend.Parser;
import com.example.pillpal420.backend.dataModels.PatientDataModel;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * Patient Repository hier werden PatientDataModel - Patient core IG resourcen vom und zum Server geschickt.
 */
public class PatientRepository {
    String serverAddress = "192.168.0.2:8080";

    /**
     * Interface für PatientCallbacks
     */
    public interface PatientCallback {
        /**
         * Wird abgerufen wenn ein Patienent DataModel erfolgreich abgerufen wird.
         *
         * @param patientDataModel PatientDataModel object.
         */
        void onResponse(PatientDataModel patientDataModel);

        /**
         * Wird gecalled wenn ein error während dem abrufen von einer Patient Resource am FHIR server passiert.
         *
         * @param e Exception encountered during the process.
         */
        void onFailure(Exception e);
    }

    /**
     * Fetches Patienten resourcen vom FHIR R5 server.
     *
     * @param patientId Patient ID representing the relative path on the server.
     * @param callback  Callback to handle failures of the response.
     */
    public void getPatient(String patientId, PatientCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String urlPatient = "http://" + serverAddress + "/hapi-fhir-jpaserver/fhir/Patient/" + patientId;
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
                    PatientDataModel patientDataModel = new Parser().createPatient(responseBody);
                    callback.onResponse(patientDataModel);
                }
            }
        });
    }

    /**
     * Posts eine Patienten coreIG Elga fhir r5 hl7 austria resource zum FHIR R5 server und returniert ein LiveDataObjekt.
     *
     * @param patientDataModel PatientDataModel to be posted.
     * @return LiveData that contains the PatientDataModel.
     */
    public LiveData<PatientDataModel> postPatientRessource(PatientDataModel patientDataModel) {
        MutableLiveData<PatientDataModel> liveData = new MutableLiveData<>();
        OkHttpClient client = new OkHttpClient();
        Parser parser = new Parser();
        JSONObject json = parser.createPostPatientResource(patientDataModel);
        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/fhir+json"));
        String url = "http://" + serverAddress + "/hapi-fhir-jpaserver/fhir/Patient";//id
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                liveData.postValue(null);
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    liveData.postValue(null);
                    throw new IOException("Unexpected Code somthing went terribly wrong posting to server" + response);
                } else {
                    String responseBody = response.body().string();
                    Log.d("Patient", "Inside of PatientRepo got this succesful resource back from server" + responseBody);
                    PatientDataModel result = parser.createPatient(responseBody);
                    Log.d("Patient", "New patient successfully created from the post server request");
                    liveData.postValue(result);
                }
            }
        });
        return liveData;
    }
}