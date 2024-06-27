
package com.example.pillpal420.backend.repository;

import androidx.annotation.NonNull;

import com.example.pillpal420.backend.Parser;
import com.example.pillpal420.backend.dataModels.FullPrescriptionDataModel;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModel;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModelForFullPrescription;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.dataModels.PractitionerDataModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FullPrescriptionRepository {

    private String serverAddress = "10.0.2.2:8080";
    private String urlFetchBundleForOnePatientWithAllMedicationDataRequests = "http://" + serverAddress + "/hapi-fhir-jpaserver/fhir/MedicationRequest?subject=";
    private OkHttpClient client = new OkHttpClient();
    private Parser parser = new Parser();

    public interface FullPrescriptionRepositoryCallback {
        void onResponse(List<FullPrescriptionDataModel> fullPrescriptionDataModels);
        void onFailure(Exception e);
    }

    public void getFullMedicationRequests(String patientId, FullPrescriptionRepositoryCallback fullPrescriptionRepositoryCallback) {
        String urlFullPrescription = urlFetchBundleForOnePatientWithAllMedicationDataRequests + patientId;

        Request request = new Request.Builder()
                .url(urlFullPrescription)
                .build();

        client.newCall(request)
                .enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                fullPrescriptionRepositoryCallback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    fullPrescriptionRepositoryCallback.onFailure(new IOException("Unexpected code " + response));
                    return;
                }

                // Bekomme vom Server ein Bundle zurück.
                String responseBody = response.body().string();
                // Hier wird es in unterschiedliche RequestData models aufgeteilt -> jeweils mit Metadaten /pat, pract, group id etc...
                List<MedicationRequestDataModel> medicationRequests = parser.createMedicationRequest(responseBody);

                // Group medication requests by group identifier
                // Hier werden die einzelnen MedicationRequest nach GroupIdentifier sortiert.
                Map<String, List<MedicationRequestDataModel>> groupedRequests = groupByGroupIdentifier(medicationRequests);

                List<FullPrescriptionDataModel> fullPrescriptions = new ArrayList<>();

                for (String groupIdentifier : groupedRequests.keySet()) {
                    List<MedicationRequestDataModel> requests = groupedRequests.get(groupIdentifier);

                    // Fetch patient and practitioner details (synchronously for simplicity)
                    String patientReference = requests.get(0).getSubject();
                    String practitionerReference = requests.get(0).getRequester();

                    PatientDataModel patient = fetchPatient(patientReference);
                    PractitionerDataModel practitioner = fetchPractitioner(practitionerReference);

                    List<MedicationRequestDataModelForFullPrescription> medicationRequestModels = new ArrayList<>();
                    for (MedicationRequestDataModel request : requests) {
                        medicationRequestModels.add(convertToFullPrescriptionModel(request));
                    }

                    FullPrescriptionDataModel fullPrescription = new FullPrescriptionDataModel(patient, practitioner, medicationRequestModels);
                    fullPrescriptions.add(fullPrescription);
                }

                fullPrescriptionRepositoryCallback.onResponse(fullPrescriptions);
            }
        });
    }

    private Map<String, List<MedicationRequestDataModel>> groupByGroupIdentifier(List<MedicationRequestDataModel> requests) {

        // Neue
        Map<String, List<MedicationRequestDataModel>> groupedRequests = new HashMap<>();
        for (MedicationRequestDataModel request : requests) {
            String groupIdentifier = request.getIdentifiereMedIDGroup();
            if (!groupedRequests.containsKey(groupIdentifier)) {
                groupedRequests.put(groupIdentifier, new ArrayList<>());
            }
            groupedRequests.get(groupIdentifier).add(request);
        }
        return groupedRequests;
    }

    private PatientDataModel fetchPatient(String patientReference) throws IOException {
        String urlPatient = "http://10.0.2.2:8080/hapi-fhir-jpaserver/fhir/" + patientReference;

        Request request = new Request.Builder().url(urlPatient).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseBody = response.body().string();
        return parser.createPatient(responseBody);
    }

    private PractitionerDataModel fetchPractitioner(String practitionerReference) throws IOException {
        String urlPractitioner = "http://10.0.2.2:8080/hapi-fhir-jpaserver/fhir/" + practitionerReference;

        Request request = new Request.Builder().url(urlPractitioner).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseBody = response.body().string();
        return parser.createPractitioner(responseBody);
    }

    private MedicationRequestDataModelForFullPrescription convertToFullPrescriptionModel(MedicationRequestDataModel request) {
        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions = new ArrayList<>();
        for (MedicationRequestDataModel.DosageInstruction instruction : request.getDosageInstructions()) {
            dosageInstructions.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription(
                    instruction.getPatientInstruction(),
                    instruction.getFrequency(),
                    instruction.getWhen()
            ));
        }
        return new MedicationRequestDataModelForFullPrescription(request.getDisplayMedication(), dosageInstructions);
    }
}
