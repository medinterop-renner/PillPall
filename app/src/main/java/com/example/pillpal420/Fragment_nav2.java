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

/**
 * Rezept Fragt, Framnent_nav2, ist dafür zuständig alle Rezepte eines bestimmten Patienten anzeigen zu lassen.
 */
public class Fragment_nav2 extends Fragment {
    private FullPrescriptionViewModel fullPrescriptionViewModel;
    String id;
    private RecyclerView rezeptRecView;
    private RezeptAdapter rezeptAdapter;
    private List<FullPrescriptionDataModel> fullPrescriptionDataModels;

    /**
     * Wird abgerufen um den user interface view zu inflaten.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the view for the framents ui or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arg = getArguments();
        if (arg != null) {
            id = arg.getString("IDkey");
            Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "id passed to if statement" + id);
        } else {
            Log.d(LogTag.FULL_PRESCRIPTION.getTag(), "Id did not pass to fragment");
        }
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        rezeptRecView = view.findViewById(R.id.scanRecView);
        rezeptRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        fullPrescriptionViewModel = new ViewModelProvider(this).get(FullPrescriptionViewModel.class);
        fullPrescriptionViewModel.getFullPrescriptionLiveData().observe(getViewLifecycleOwner(), new Observer<List<FullPrescriptionDataModel>>() {
            @Override
            public void onChanged(List<FullPrescriptionDataModel> fullPrescriptionDataModels) {
                if (fullPrescriptionDataModels != null) {
                    displayFullPrescriptionDataModels(fullPrescriptionDataModels);
                }
            }
        });
        fetchFullPrescriptions();
        return view;
    }

    /**
     * Zeigt die FullPrescription Datamodels in dem Recycler View an.
     *
     * @param fullPrescriptionDataModels List of the FullPrescriptionDataModels.
     */
    private void displayFullPrescriptionDataModels(List<FullPrescriptionDataModel> fullPrescriptionDataModels) {
        rezeptAdapter = new RezeptAdapter(fullPrescriptionDataModels);
        rezeptRecView.setAdapter(rezeptAdapter);
    }

    /**
     * Diese Methode wird nur für das Testen des Prescriptions Fragment gebraucht.
     * Hier kann man Test PatientInnen/ Practitioners erstellen und die Dazugehörigen MedicationsRequests erstellen aus denen,
     * dann FullPrescriptionDataModels erzeugt werden.
     * Diese Methode wird für das weitere Development der App benötigt und darf nicht gelöscht werden.
     *
     * @return
     */
    public List<FullPrescriptionDataModel> createTestFullPrescriptionDataModels() {
        List<FullPrescriptionDataModel> fullPrescriptionDataModels = new ArrayList<>();
        PatientDataModel patient = new PatientDataModel(
                "1", "1234010100", "Mustermann", "Max", "DI",
                "male", "1900-01-01", "Landstrasse 1 Stock 9 Tür 42",
                "Linz", "Oberösterreich", "4020", "AUT"
        );
        PractitionerDataModel practitioner = new PractitionerDataModel(
                "1554", "urn:oid:1.2.40.0.34.99.4613.4", "Blackwell",
                "Elizabeth", "Dr", "0123456789", "1stAve",
                "NY", "9101", "USA"
        );
        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions1 = new ArrayList<>();
        dosageInstructions1.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription("Tablette in Wasser Auflösen", "2", "MORN"));
        dosageInstructions1.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription("Tablette in Wasser Auflösen", "2", "EVE"));
        MedicationRequestDataModelForFullPrescription medicationRequest1 = new MedicationRequestDataModelForFullPrescription("Keta FTBL 500MG", dosageInstructions1);
        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions2 = new ArrayList<>();
        dosageInstructions2.add(new MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription("Eine Pille einnehmen", "1", "NOON"));
        MedicationRequestDataModelForFullPrescription medicationRequest2 = new MedicationRequestDataModelForFullPrescription("Ibuprofen 200MG", dosageInstructions2);
        List<MedicationRequestDataModelForFullPrescription> medicationRequests = new ArrayList<>();
        medicationRequests.add(medicationRequest1);
        medicationRequests.add(medicationRequest2);
        FullPrescriptionDataModel fullPrescription = new FullPrescriptionDataModel(patient, practitioner, medicationRequests);
        fullPrescriptionDataModels.add(fullPrescription);
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

    /**
     * fetched FullPrescriptionData für einen bestimmten Patienten. -> Update durch LiveData.
     */
    private void fetchFullPrescriptions() {
        LoginActivity loginActivity = new LoginActivity();
        String testTestPatientRplacewithrelativePathPatientReq = "1599";
        fullPrescriptionViewModel.fetchFullPrescriptions(testTestPatientRplacewithrelativePathPatientReq);
    }
}