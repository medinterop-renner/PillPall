package com.example.pillpal420;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pillpal420.backend.dataModels.FullPrescriptionDataModel;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModelForFullPrescription;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.dataModels.PractitionerDataModel;
import com.example.pillpal420.backend.viewModels.FullPrescriptionViewModel;
import com.example.pillpal420.documentation.LogTag;

import java.util.ArrayList;
import java.util.List;

//Scan Fragment
public class Fragment_nav2 extends Fragment {


    //  BACKEND !!

    // String patientId = "1599"; // Globale ID Einfügen!!!!
    private FullPrescriptionViewModel fullPrescriptionViewModel;


    String id;

    private RecyclerView rezeptRecView;
    private RezeptAdapter rezeptAdapter;
    private List<FullPrescriptionDataModel> fullPrescriptionDataModels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Login ID String von Login Activity holen
        Bundle arg = getArguments();
        if (arg != null){
             id = arg.getString("IDkey");

             Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "id passed to if statement"+ id);
        }else {
            Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Id did not pass to fragment");
        }


       // List<FullPrescriptionDataModel> fullPrescriptionDataModels = new ArrayList<>();
       // fullPrescriptionDataModels = createTestFullPrescriptionDataModels();
        //Log.d("Rezept", fullPrescriptionDataModels.get(0).toString());
       // Log.d("Rezept", fullPrescriptionDataModels.get(1).toString());
        //Log.d(LogTag.FULL_PRESCRIPTION.getTag(),"logging full prescription");


        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        rezeptRecView = view.findViewById(R.id.scanRecView);
        rezeptRecView.setLayoutManager(new LinearLayoutManager(getContext()));



        // backend
        fullPrescriptionViewModel = new ViewModelProvider(this).get(FullPrescriptionViewModel.class);

        fullPrescriptionViewModel.getFullPrescriptionLiveData().observe(getViewLifecycleOwner(), new Observer<List<FullPrescriptionDataModel>>() {
            @Override
            public void onChanged(List<FullPrescriptionDataModel> fullPrescriptionDataModels) {
                if (fullPrescriptionDataModels != null) {
                    displayFullPrescriptionDataModels(fullPrescriptionDataModels);


                }
            }
        });

        // Intent get intent -- string


        // Hier muss relativePathPatientIDServer abgefragt werden aus Intent und dann in der  fetchFullPrescriptions als Übergabeparameter übergeben werden.
        // UI Testing hier auscommenten
        fetchFullPrescriptions();

        // Inflate the layout for this fragment
        return view;

    }
// Liste aus methoden übergabe entfernen und in onCreateview aufrufen
    private void displayFullPrescriptionDataModels(List<FullPrescriptionDataModel> fullPrescriptionDataModels){
        //Hier Server Daten abfragen für Display unten


// Hier generator methode reingeben

        //Backend magic oben

        rezeptAdapter = new RezeptAdapter(fullPrescriptionDataModels);
        rezeptRecView.setAdapter(rezeptAdapter);

    }

    public List<FullPrescriptionDataModel> createTestFullPrescriptionDataModels() {
        List<FullPrescriptionDataModel> fullPrescriptionDataModels = new ArrayList<>();

        // Create PatientDataModel
        PatientDataModel patient = new PatientDataModel(
                "1", "1234010100", "Mustermann", "Max", "DI",
                "male", "1900-01-01", "Landstrasse 1 Stock 9 Tür 42",
                "Linz", "Oberösterreich", "4020", "AUT"
        );

        // Create PractitionerDataModel
        PractitionerDataModel practitioner = new PractitionerDataModel(
                "1554", "urn:oid:1.2.40.0.34.99.4613.4", "Blackwell",
                "Elizabeth", "Dr", "0123456789", "1stAve",
                "NY", "9101", "USA"
        );

        // Create MedicationRequestDataModelForFullPrescription - First Medication
        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions1 = new ArrayList<>();
        dosageInstructions1.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription("Tablette in Wasser Auflösen", "2", "MORN"));
        dosageInstructions1.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription("Tablette in Wasser Auflösen", "2", "EVE"));

        MedicationRequestDataModelForFullPrescription medicationRequest1 = new MedicationRequestDataModelForFullPrescription("Keta FTBL 500MG", dosageInstructions1);

        // Create MedicationRequestDataModelForFullPrescription - Second Medication
        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions2 = new ArrayList<>();
        dosageInstructions2.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription("Eine Pille einnehmen", "1", "NOON"));

        MedicationRequestDataModelForFullPrescription medicationRequest2 = new MedicationRequestDataModelForFullPrescription("Ibuprofen 200MG", dosageInstructions2);

        // Add Medication Requests to list
        List<MedicationRequestDataModelForFullPrescription> medicationRequests = new ArrayList<>();
        medicationRequests.add(medicationRequest1);
        medicationRequests.add(medicationRequest2);

        // Create FullPrescriptionDataModel
        FullPrescriptionDataModel fullPrescription = new FullPrescriptionDataModel(patient, practitioner, medicationRequests);

        // Add to list
        fullPrescriptionDataModels.add(fullPrescription);


        // Second FullPrescriptionDataModel with three medications
        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions5 = new ArrayList<>();
        dosageInstructions5.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription("Eine Pille einnehmen", "1", "NOON"));

        MedicationRequestDataModelForFullPrescription medicationRequest3 = new MedicationRequestDataModelForFullPrescription("Ibuprofen 200MG", dosageInstructions5);

        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions3 = new ArrayList<>();
        dosageInstructions3.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription("Tablette nach dem Essen einnehmen", "1", "EVENING"));

        MedicationRequestDataModelForFullPrescription medicationRequest4 = new MedicationRequestDataModelForFullPrescription("Paracetamol 500MG", dosageInstructions3);

        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions4 = new ArrayList<>();
        dosageInstructions4.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription("Kapsel auf nüchternen Magen einnehmen", "1", "MORNING"));

        MedicationRequestDataModelForFullPrescription medicationRequest5 = new MedicationRequestDataModelForFullPrescription("Aspirin 100MG", dosageInstructions4);

        List<MedicationRequestDataModelForFullPrescription> medicationRequests2 = new ArrayList<>();
        medicationRequests2.add(medicationRequest3);
        medicationRequests2.add(medicationRequest4);
        medicationRequests2.add(medicationRequest5);

        FullPrescriptionDataModel fullPrescription2 = new FullPrescriptionDataModel(patient, practitioner, medicationRequests2);
        fullPrescriptionDataModels.add(fullPrescription2);

        return fullPrescriptionDataModels;
    }
// Backend Magic wowowowowowoowowowowoowowowowo


    //triggermethode der fullmedreq auslöst
    private void fetchFullPrescriptions() {

        // Hier muss dieser String ankommen -> Also in übergabe parameter String relativePathPatientIDServer
        // dann String patientId = relativePathPatientIDServer



        LoginActivity loginActivity = new LoginActivity();
//        String relativePathPatientReq = loginActivity.getCorePatientProfil().getId();

        String testTestPatientRplacewithrelativePathPatientReq = "1599";


        fullPrescriptionViewModel.fetchFullPrescriptions(testTestPatientRplacewithrelativePathPatientReq);
    }



}