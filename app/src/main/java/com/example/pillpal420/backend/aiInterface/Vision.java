package com.example.pillpal420.backend.aiInterface;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.example.pillpal420.backend.Parser;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModel;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModelForFullPrescription;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.dataModels.PractitionerDataModel;
import com.example.pillpal420.backend.viewModels.VisionViewModel;
import com.example.pillpal420.backend.viewModels.WhisperViewModel;
import com.example.pillpal420.documentation.LogTag;

import org.json.JSONArray;
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

public class Vision {

    private static MedicationRequestDataModel medicationRequestDataModel;


    private String serverAddress = "192.168.0.2:8080";

    private Parser parser = new Parser();
    private OkHttpClient client = new OkHttpClient();


    private String scannedText;

    public Vision(String scannedText){
        this.scannedText = scannedText;
        sendTextToServer(scannedText);
    }

    /**
     * Hier wird der gescannte Text zur Verarbeitung an den Server geschickt
     *
     * Funktion:
     * 1. Erstellen einer OkHttpClient Instanz
     * @param scannedText
     */
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

                //need
                String identifierValue = identifierArray.getJSONObject(0).getString("value");


                //dont need
                String status = entry.getString("status");
                String intent = entry.getString("intent");
                // dont need
                JSONObject medication = entry.getJSONObject("medication");
                JSONObject concept = medication.getJSONObject("concept");
                JSONArray codingArray = concept.getJSONArray("coding");
                JSONObject coding = codingArray.getJSONObject(0);
                String system = coding.getString("system");


                // need
                String aspCode = coding.getString("code");
                String displayMedication = coding.getString("display");


                // need
                String subjectReference = entry.getJSONObject("subject").getString("reference");
                String requesterReference = entry.getJSONObject("requester").getString("reference");
                String groupIdentifierValue = entry.getJSONObject("groupIdentifier").getString("value");


                //dont need
                JSONArray dosageInstructionArray = entry.getJSONArray("dosageInstruction");
                JSONObject dosageInstruction = dosageInstructionArray.getJSONObject(0);
                String patientInstruction = dosageInstruction.getString("patientInstruction");
                JSONObject timing = dosageInstruction.getJSONObject("timing");



                JSONObject repeat = timing.getJSONObject("repeat");
                String frequency = repeat.getString("frequency");

                JSONArray whenArray = repeat.getJSONArray("when");
                String when = whenArray.getString(0);



                List<MedicationRequestDataModel.DosageInstruction> dosageInstructionsList = new ArrayList<>();

                MedicationRequestDataModel.DosageInstruction dosageInstructionDataModel = new MedicationRequestDataModel.DosageInstruction(patientInstruction,frequency,when);
                dosageInstructionsList.add(dosageInstructionDataModel);




                MedicationRequestDataModel medicationRequestDataModel = new MedicationRequestDataModel(
                        "1",identifierValue,groupIdentifierValue,aspCode,displayMedication,requesterReference,subjectReference,dosageInstructionsList);



                Log.d(LogTag.VISION.getTag(),medicationRequestDataModel.toString());

                createMedicationRequestWithAcurateData(medicationRequestDataModel);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    public void createMedicationRequestWithAcurateData(MedicationRequestDataModel medicationRequestDataModel){

        PatientDataModel patientDataModel;
        PractitionerDataModel practitionerDataModel;



        try {
            patientDataModel = fetchPatient(medicationRequestDataModel.getSubject());
            practitionerDataModel= fetchPractitioner(medicationRequestDataModel.getRequester());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        medicationRequestDataModel.setRequester("Practitioner/" + practitionerDataModel.getId());
        medicationRequestDataModel.setSubjectReference("Patient/" + patientDataModel.getId());
        Log.d(LogTag.VISION.getTag(), medicationRequestDataModel.toString());


        JSONObject json = parser.createPostMedicationRequest(medicationRequestDataModel);


        Log.d(LogTag.MEDICATION_REQUEST.name(),"Raw json from server: "+ json.toString());
        RequestBody body = RequestBody.create(json.toString(), MediaType.parse("application/fhir+json"));
        String url = "http://192.168.0.2:8080/hapi-fhir-jpaserver/fhir/MedicationRequest";

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.d(LogTag.VISION.getTag(), "new medication request posted");
            }
        });


    }
    private PatientDataModel fetchPatient(String patientReference) throws IOException {
        Log.d(LogTag.VISION.getTag(), "Fetching ID for Patient");
        String urlPatient = " http://"+ serverAddress +"/hapi-fhir-jpaserver/fhir/Patient?family=" + patientReference;

        Request request = new Request.Builder().url(urlPatient).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseBody = response.body().string();
        return parser.createPatient(responseBody);
    }

    private PractitionerDataModel fetchPractitioner(String practitionerReference) throws IOException {
        Log.d(LogTag.VISION.getTag(), "Fetching ID for Practitioner");
        String urlPractitioner = " http://"+ serverAddress +"/hapi-fhir-jpaserver/fhir/Practitioner?family=" + practitionerReference;

        Request request = new Request.Builder().url(urlPractitioner).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseBody = response.body().string();
        return parser.createPractitioner(responseBody);
    }


    }
