package com.example.pillpal420;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pillpal420.Backend.FullPrescriptionDataModel;
import com.example.pillpal420.Backend.MedicationRequestDataModelForFullPrescription;
import com.example.pillpal420.Backend.PatientDataModel;
import com.example.pillpal420.Backend.PractitionerDataModel;
import com.example.pillpal420.Documentation.LogTag;

import java.util.ArrayList;
import java.util.List;

//Scan Fragment
public class Fragment_nav2 extends Fragment {
    private String[] patient;
    //Familyname, Givenname, Prefix/Suffix
    private String[] name;
    //Straßenname, Hausnummer, Stadt, Staat, Postleitzahl
    private String[] address;
    //male, female, other, unknown
    private String gender;
    //YYYY:MM:DD
    private String birthdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //fullPrescriptionDataModels --> alles
        //fullPrescriptionDataModels.get(0).toString() ganzes Rezeot (Arzt, Patient, Medikation)
        //fullPrescriptionDataModels.get(0).patientDataModel.toString()
        //fullPrescriptionDataModels.get(0).practicionerDataModel.toString()
        //fullPrescriptionDataModels.get(0).medicationRequestDataModelForFullPrescription.toString()
        List<FullPrescriptionDataModel> fullPrescriptionDataModels = new ArrayList<>();
        fullPrescriptionDataModels = createTestFullPrescriptionDataModels();
        Log.d("Rezept", fullPrescriptionDataModels.get(0).toString());
        Log.d("Rezept", fullPrescriptionDataModels.get(1).toString());
        Log.d(LogTag.FULL_PRESCRIPTION.getTag(),"logging full prescription");







        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false);



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


}