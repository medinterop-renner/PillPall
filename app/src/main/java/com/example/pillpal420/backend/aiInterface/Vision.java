package com.example.pillpal420.backend.aiInterface;

import android.util.Log;

import com.example.pillpal420.documentation.LogTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Vision {

    private String scannedText;

    public Vision(String scannedText){
        this.scannedText = scannedText;
        sendTextToServer(scannedText);
    }


    private void sendTextToServer(String scannedText) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, scannedText);
        Request request = new Request.Builder()
                .url("http://192.168.0.2:8000/upload")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LogTag.VISION.getTag(), "Error sending text to server: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(LogTag.VISION.getTag(), "Server returned error: " + response.message());
                } else {
                    String fhirResourcePreParsing = response.body().string();
                    Log.d(LogTag.VISION.getTag(), "Server response: " + fhirResourcePreParsing);
                   parseAndVerifyMedicationRequestResourceFromPython(fhirResourcePreParsing);

                }
            }
        });
    }

    public void parseAndVerifyMedicationRequestResourceFromPython(String fhirResourcePreParsing) {
        try {
           /* JSONObject serverResponse = new JSONObject(fhirResourcePreParsing);

            JSONArray jsonArray = .getJSONArray("entry");
            JSONObject  jsonObject = jsonArray.getJSONObject(0);
            String resource = jsonObject.getString("resourceType");
            Log.d(LogTag.VISION.getTag(), resource);
           //JSONObject medicationRequestResource = new JSONObject(medicationRequestResource);


        //    String test = medicationRequestRecource.getString("resourceType");
           // Log.d(LogTag.VISION.getTag(), test);*/

            JSONObject jsonObject = new JSONObject(fhirResourcePreParsing);
            JSONObject messageObject = jsonObject.getJSONObject("message");
            JSONArray entryArray = messageObject.getJSONArray("entry");

            for (int i = 0; i < entryArray.length(); i++) {
                JSONObject entry = entryArray.getJSONObject(i);
                String resourceType = entry.getString("resourceType");
                JSONArray identifierArray = entry.getJSONArray("identifier");
                String identifierValue = identifierArray.getJSONObject(0).getString("value");
                String status = entry.getString("status");
                String intent = entry.getString("intent");

                JSONObject medication = entry.getJSONObject("medication");
                JSONObject concept = medication.getJSONObject("concept");
                JSONArray codingArray = concept.getJSONArray("coding");
                JSONObject coding = codingArray.getJSONObject(0);
                String system = coding.getString("system");
                String code = coding.getString("code");
                String display = coding.getString("display");

                String subjectReference = entry.getJSONObject("subject").getString("reference");
                String requesterReference = entry.getJSONObject("requester").getString("reference");
                String groupIdentifierValue = entry.getJSONObject("groupIdentifier").getString("value");

                JSONArray dosageInstructionArray = entry.getJSONArray("dosageInstruction");
                JSONObject dosageInstruction = dosageInstructionArray.getJSONObject(0);
                String patientInstruction = dosageInstruction.getString("patientInstruction");
                JSONObject timing = dosageInstruction.getJSONObject("timing");
                JSONObject repeat = timing.getJSONObject("repeat");
                String frequency = repeat.getString("frequency");
                JSONArray whenArray = repeat.getJSONArray("when");
                String when = whenArray.getString(0);

                Log.d(LogTag.VISION.getTag(), i +". Request" +subjectReference + " wurde " + display + " verschrieben"
                + " von " + requesterReference);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    }
