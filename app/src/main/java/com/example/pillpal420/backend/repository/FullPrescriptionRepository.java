package com.example.pillpal420.backend.repository;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pillpal420.backend.Parser;
import com.example.pillpal420.backend.dataModels.FullPrescriptionDataModel;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModel;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModelForFullPrescription;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.dataModels.PractitionerDataModel;
import com.example.pillpal420.documentation.LogTag;

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

/**
 * Die FullPrescriptionRepository Klasse ist dafür da um daten von den Rezepten vom Server zu bekommen und sie zu processen.
 * Es fetched FHIR R5 MedicationRequests und gruppiert sie nach dem group identifier der von der ELGAGmbH vorgegeben wird. Es leitet die
 * raw json values an die Parser Klasse weiter die FullPrescriptionModels parsed. Hier werden Patient und Practitioner Daten processed.
 */
public class FullPrescriptionRepository {


    private final String serverAddress = "192.168.0.2:8080";
    private final String urlFetchBundleForOnePatientWithAllMedicationDataRequests = "http://" + serverAddress + "/hapi-fhir-jpaserver/fhir/MedicationRequest?subject=";
    private final OkHttpClient client = new OkHttpClient();
    private final Parser parser = new Parser();

    /**
     * Callback interface für das FullPrescriptionRepository
     */
    public interface FullPrescriptionRepositoryCallback {
        void onResponse(List<FullPrescriptionDataModel> fullPrescriptionDataModels);

        void onFailure(Exception e);
    }

    /**
     * Holt alle MedicationRequests vom FHIR R5 Server für einen Patienten und processed sie zu FullPrescriptionDataModels.
     *
     * @param patientId                          The ID of the patient whose medication requests are fetched
     * @param fullPrescriptionRepositoryCallback The callback to handle response or failure.
     */
    public void getFullMedicationRequests(String patientId, FullPrescriptionRepositoryCallback fullPrescriptionRepositoryCallback) {
        String urlFullPrescription = urlFetchBundleForOnePatientWithAllMedicationDataRequests + patientId;

        Request request = new Request.Builder().url(urlFullPrescription).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(LogTag.FULL_PRESCRIPTION.getTag(), "Failed to fetch medication requests: " + e.getMessage());
                fullPrescriptionRepositoryCallback.onFailure(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {

                    fullPrescriptionRepositoryCallback.onFailure(new IOException("Unexpected code " + response));
                    return;
                }


                String responseBody = response.body().string();
                Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Server response: " + responseBody);

                List<MedicationRequestDataModel> medicationRequests = parser.createMedicationRequest(responseBody);
                Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Parsed medication requests with size: " + medicationRequests.size());


                Map<String, List<MedicationRequestDataModel>> groupedRequests = groupByGroupIdentifier(medicationRequests);
                Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Grouped medication requests by the group identifier.");

                List<FullPrescriptionDataModel> fullPrescriptions = new ArrayList<>();

                for (String groupIdentifier : groupedRequests.keySet()) {
                    List<MedicationRequestDataModel> requests = groupedRequests.get(groupIdentifier);


                    String patientReference = requests.get(0).getSubject();
                    String practitionerReference = requests.get(0).getRequester();
                    Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Fetching details for patient reference: " + patientReference + " and practitioner reference: " + practitionerReference);

                    PatientDataModel patient = fetchPatient(patientReference);
                    PractitionerDataModel practitioner = fetchPractitioner(practitionerReference);
                    Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Fetched patient and practitioner resources from the FHIR R5 Server.");

                    List<MedicationRequestDataModelForFullPrescription> medicationRequestModels = new ArrayList<>();
                    for (MedicationRequestDataModel request : requests) {
                        medicationRequestModels.add(convertToFullPrescriptionModel(request));
                    }

                    FullPrescriptionDataModel fullPrescription = new FullPrescriptionDataModel(patient, practitioner, medicationRequestModels);
                    fullPrescriptions.add(fullPrescription);
                }

                fullPrescriptionRepositoryCallback.onResponse(fullPrescriptions);
                Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Full prescriptions fetched successfully.");
            }
        });
    }

    /**
     * Gruppiert medication requests anhand ihrer group identifier
     *
     * @param requests The list of medication request data models.
     * @return A map with key group identifier and the value list of medication requests.
     */
    private Map<String, List<MedicationRequestDataModel>> groupByGroupIdentifier(List<MedicationRequestDataModel> requests) {
        Map<String, List<MedicationRequestDataModel>> groupedRequests = new HashMap<>();
        for (MedicationRequestDataModel request : requests) {
            String groupIdentifier = request.getIdentifiereMedIDGroup();
            if (!groupedRequests.containsKey(groupIdentifier)) {
                groupedRequests.put(groupIdentifier, new ArrayList<>());
            }
            groupedRequests.get(groupIdentifier).add(request);
        }
        Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Grouped " + requests.size() + " medication requests into this number:  " + groupedRequests.size() + " of groups.");
        return groupedRequests;
    }

    /**
     * Holt die Patienten Resource die von FHIR als relativer path angegeben wird
     *
     * @param patientReference The reference ID of the patient/ the relative Path of the Patient resource on the FHIR R5 Server
     * @return A PatientDataModel object representing the patient's personal details.
     * @throws IOException if the request fails.
     */
    private PatientDataModel fetchPatient(String patientReference) throws IOException {
        String urlPatient = "http://192.168.0.2:8080/hapi-fhir-jpaserver/fhir/" + patientReference;


        Request request = new Request.Builder().url(urlPatient).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseBody = response.body().string();
        Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Fetched patient details from the FHIR R5 server");
        return parser.createPatient(responseBody);
    }

    /**
     * Fetched Practitioner details. über die id/ relative Path am FHIR Server
     *
     * @param practitionerReference The reference ID of the practitioner/ relative Path from the FHIR server.
     * @return A PractitionerDataModel object representing the practitioner's details.
     * @throws IOException if the request fails.
     */
    private PractitionerDataModel fetchPractitioner(String practitionerReference) throws IOException {
        String urlPractitioner = "http://192.168.0.2:8080/hapi-fhir-jpaserver/fhir/" + practitionerReference;

        Request request = new Request.Builder().url(urlPractitioner).build();
        String responseBody;
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            responseBody = response.body().string();
        }
        Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Fetched practitioner details from the FHIR R5 server.");
        return parser.createPractitioner(responseBody);
    }

    /**
     * Erstellt aus einem MedicationRequestDataModel Objekt ein MedicationDataModelForFullPrescriptionsModel
     *
     * @param request the MedicationRequestDataModel object that has to be converted
     * @return Returns a MedicationRequestDataModelForFullPrescriptions object for further use in the FullPrescriptionRepository.
     */
    private MedicationRequestDataModelForFullPrescription convertToFullPrescriptionModel(MedicationRequestDataModel request) {
        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions = new ArrayList<>();
        for (MedicationRequestDataModel.DosageInstruction instruction : request.getDosageInstructions()) {
            dosageInstructions.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription(instruction.getPatientInstruction(), instruction.getFrequency(), instruction.getWhen()));
        }
        Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Converted medication request to fullprescriptiondatamodel.");
        return new MedicationRequestDataModelForFullPrescription(request.getDisplayMedication(), dosageInstructions);
    }
}
