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

public class PatientRepository {
    public interface PatientCallback {
        void onResponse(PatientDataModel patientDataModel);
        void onFailure(Exception e);
    }

    public void getPatient(String patientId, PatientCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String urlPatient = "http://192.168.0.2:8080/hapi-fhir-jpaserver/fhir/Patient/" + patientId;

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

    public LiveData<PatientDataModel> postPatientRessource(PatientDataModel patientDataModel){
        MutableLiveData<PatientDataModel> liveData = new MutableLiveData<>();

        OkHttpClient client = new OkHttpClient();

        Parser parser = new Parser();

        JSONObject json = parser. createPostPatientResource(patientDataModel);
        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/fhir+json"));

        String url = "http://192.168.0.2:8080/hapi-fhir-jpaserver/fhir/Patient";//id

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
                if (!response.isSuccessful()){
                    liveData.postValue(null);
                    throw new IOException("Unexpected Code somthing went terribly wrong posting to server" + response);

                }else {
                    String responseBody = response.body().string();
                    Log.d("Patient","Inside of PatientRepo got this succesful resource back from server" + responseBody);
                    PatientDataModel result = parser.createPatient(responseBody);
                    Log.d("Patient","New patient successfully created from the post server request");
                    liveData.postValue(result);
                }
            }
        });

        return liveData;

    }
}