package com.example.pillpal420.backend.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pillpal420.backend.Parser;
import com.example.pillpal420.backend.dataModels.PractitionerDataModel;
import com.example.pillpal420.documentation.LogTag;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PractitionerRepository {




    public interface PractitionerCallback{
        void onResponse(PractitionerDataModel practitionerDataModel);
        void onFailure(Exception e);
    }

    public void getPractitioner(String practitionerID, PractitionerCallback callback){

        OkHttpClient client = new OkHttpClient();
        String urlPractitioner = "http://192.168.0.2:8080/hapi-fhir-jpaserver/fhir/Practitioner/"+ practitionerID;

        Request request = new Request.Builder()
                .url(urlPractitioner)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
            if(!response.isSuccessful()){
                callback.onFailure(new IOException("Unexpected Stuff happend "+response ));
            }else {
                String responseBody = response.body().string();
                PractitionerDataModel practitioner = new Parser().createPractitioner(responseBody);
                callback.onResponse(practitioner);
            }
            }
        });
    }

    public LiveData<PractitionerDataModel> postPractitionerRessource(PractitionerDataModel practitionerDataModel){
        MutableLiveData<PractitionerDataModel> liveData = new MutableLiveData<>();

        OkHttpClient client = new OkHttpClient();
        Parser parser = new Parser();

        JSONObject json = parser.createPostPractitionerResource(practitionerDataModel);
        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/fhir+json"));
        String url = "http://192.168.0.2:8080/hapi-fhir-jpaserver/fhir/Practitioner";

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
                throw new IOException("Got back Unexpected Code from Server " + response);
            }else{
                String responseBody = response.body().string();
                Log.d(LogTag.PRACTITIONER.getTag(), "Got this back after posting to Server: " + responseBody);
                PractitionerDataModel result = parser.createPractitioner(responseBody);
                Log.d(LogTag.PRACTITIONER.getTag(),"practitioner successfully created from server response");
                liveData.postValue(result);
            }
            }
        });

        return liveData;
    }
}
