package com.example.pillpal420.backend.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.pillpal420.backend.Parser;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModel;
import com.example.pillpal420.documentation.LogTag;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * MedicationRequestRepository Klasse
 * Sie wird verwendet um HL7 FHIR R5 ELGA eMedikation konforme medication Request resourcen an den FHIR R5 server zu schicken.
 */
public class MedicationRequestRepository {
    String serverAddress = "192.168.0.2:8080";

    /**
     * Interface für MedicationRequestCallbacks.
     */
    public interface MedicationRequestCallback {
        /**
         * wird abgerufen wenn MedicationRequestDataModel erfolgreich gefetched wurde.
         *
         * @param medicationRequestDataModel List of MedicationRequestDataModels object.
         */
        void onResponse(List<MedicationRequestDataModel> medicationRequestDataModel);

        /**
         * wird abgerufen wenn ein error auftritt beim abrufen von MedicationRequestDataModels.
         *
         * @param e Exception encountered during the process.
         */
        void onFailure(Exception e);
    }

    /**
     * Fetched MedicationRequest Data vom FHIR R5 server um eine Patienten ID zu vergeben.
     *
     * @param patientId                 Identifier for the Patient on the FHIR R5 server.
     * @param medicationRequestCallback Callback to handle response or failure of the getRequest.
     */
    public void getMedicationRequest(String patientId, MedicationRequestCallback medicationRequestCallback) {
        OkHttpClient client = new OkHttpClient();
        String urlMedicationRequest = "http://" + serverAddress + "/hapi-fhir-jpaserver/fhir/MedicationRequest?subject=" + patientId;
        Request request = new Request.Builder()
                .url(urlMedicationRequest)
                .build();
        Log.d(LogTag.MEDICATION_REQUEST.name(), "Fetching MedicationRequest for patientId: " + patientId);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                medicationRequestCallback.onFailure(e);
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    medicationRequestCallback.onFailure(new IOException("unexpectedCode" + response));
                } else {
                    String responseBody = response.body().string();
                    List<MedicationRequestDataModel> medicationRequest = new ArrayList<>(new Parser().createMedicationRequest(responseBody));
                    medicationRequestCallback.onResponse(medicationRequest);
                }
            }
        });
    }

    /**
     * Posted einen FHIR R5 HL7 medicationRequest resource zum FHIR server und returniert ein LiveDataObjekt.
     *
     * @param medicationRequest MedicationRequestDataModel object that gets converted to json and posted to the FHIR server.
     * @return LiveData containing the response MedicationRequestDataModel.
     */
    public LiveData<MedicationRequestDataModel> postMedicationRequest(MedicationRequestDataModel medicationRequest) {
        MutableLiveData<MedicationRequestDataModel> liveData = new MutableLiveData<>();
        OkHttpClient client = new OkHttpClient();
        Parser parser = new Parser();
        JSONObject json = parser.createPostMedicationRequest(medicationRequest);
        Log.d(LogTag.MEDICATION_REQUEST.name(), "Raw json from server: " + json.toString());
        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/fhir+json"));
        String url = "http://" + serverAddress + "/hapi-fhir-jpaserver/fhir/MedicationRequest";
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                liveData.postValue(null);
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if (!response.isSuccessful()) {
                        liveData.postValue(null);
                        throw new IOException("Unexpected code " + response);
                    } else {
                        String responseBody = response.body().string();
                        Log.d(LogTag.MEDICATION_REQUEST.name(), "Inside of MedicationRequestRepo " + responseBody);
                        JSONObject jsonObjectMedicationResourceBody = new JSONObject(responseBody);
                        MedicationRequestDataModel result = parser.parseMedicationRequest(jsonObjectMedicationResourceBody);
                        Log.d(LogTag.MEDICATION_REQUEST.name(), "Successfully created medication RequestViewModel inside of MedReqRepository");
                        liveData.postValue(result);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return liveData;
    }
}
