package com.example.pillpal420.testingNotForRealUse;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    DataStorage dataStorage = new DataStorage();
    String[] patientDataJson;

    private PatientViewModel patientViewModel;

    private List<MedicationRequestViewModel> medicationRequestViewModels;

    private PractitionerViewModel practitionerViewModel;

    private FullPrescriptionViewModel fullPrescriptionViewModel;


    // ROOM ANBINDUNG

    CorePatientProfileDatabase corePatientProfileDatabase;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        // FullPrescription

        fullPrescriptionViewModel = new ViewModelProvider(this).get(FullPrescriptionViewModel.class);

        fullPrescriptionViewModel.getFullPrescriptionLiveData().observe(this, new Observer<List<FullPrescriptionDataModel>>() {
            @Override
            public void onChanged(List<FullPrescriptionDataModel> fullPrescriptionDataModels) {
                if (fullPrescriptionDataModels != null) {
                    displayFullPrescriptions(fullPrescriptionDataModels);


                }
            }
        });

//getPatientOverview();

        patientViewModel = new ViewModelProvider(this).get(PatientViewModel.class);

        patientViewModel.getPatientLiveData().observe(this, new Observer<PatientDataModel>() {
            @Override
            public void onChanged(PatientDataModel patientDataModel) {
                if (patientDataModel != null) {
                    displayPatientData(patientDataModel);
                }
            }
        });

        // Initialize the list of MedicationRequestViewModel
        medicationRequestViewModels = new ArrayList<>();
// Adjust the number based on your needs
        for (int i = 0; i < 5; i++) {
            MedicationRequestViewModel viewModel = new ViewModelProvider(this).get(MedicationRequestViewModel.class);
            medicationRequestViewModels.add(viewModel);
        }

        for (MedicationRequestViewModel viewModel : medicationRequestViewModels) {
            viewModel.getMedicationRequestDataModelMutableLiveData().observe(this, new Observer<List<MedicationRequestDataModel>>() {
                @Override
                public void onChanged(List<MedicationRequestDataModel> medicationRequestDataModels) {
                    if (medicationRequestDataModels != null) {
                        displayMedicationRequestData(medicationRequestDataModels);
                    }
                }
            });
        }

        practitionerViewModel = new ViewModelProvider(this).get(PractitionerViewModel.class);
        practitionerViewModel.getPractitionerLiveData().observe(this, new Observer<PractitionerDataModel>() {
            @Override
            public void onChanged(PractitionerDataModel practitionerDataModel) {
                if (practitionerDataModel != null) {
                    Log.d("Practioner", practitionerDataModel.toString());
                    displayPractitionerData(practitionerDataModel);
                }
            }
        });

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
            @Override
            public void run() {
               // fetchPatientData();
              //  fetchMedicationRequestData();
              //  fetchPractitionerDataModel();
                Button postMedicationRessourceButton = findViewById(R.id.btn_post_prescription);
                postMedicationRessourceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<MedicationRequestDataModel.DosageInstruction> dosageInstructionList = new ArrayList<>();
                        MedicationRequestDataModel.DosageInstruction dosageInstruction0 = new MedicationRequestDataModel.DosageInstruction("Alternativ Tablette in Wasser Auflösen","2","MORN");
                        MedicationRequestDataModel.DosageInstruction dosageInstruction1 = new MedicationRequestDataModel.DosageInstruction("Alternativ Tablette in Wasser Auflösen","2","EVE");
                        dosageInstructionList.add(dosageInstruction0);
                        dosageInstructionList.add(dosageInstruction1);


                        MedicationRequestDataModel medicationRequest = new MedicationRequestDataModel(
                                "999", "eMedID111-000", "eMedID112", "51423", "Magnesium FTBL 500MG","Practitioner/1600", "Patient/1599"
                     ,  dosageInstructionList );
                        if (validateMedicationRequest(medicationRequest)) {
                            postMedicationRequest(medicationRequest);

                        } else {
                            Log.d("MedicationRequest", "MedicationRequest data is incomplete.");
                        }
                    }
                });

                Button postPatientRessourceButton = findViewById(R.id.post_new_Patient_Button);
                postPatientRessourceButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PatientDataModel patientDataModel = new PatientDataModel("100","1234010100","turbotoll","turboVorname","Dr"
                                ,"male","2000-01-01","Patientenstrasse 1","Graz","Stmk","8052","AUT");
                        if (validatePatientRessource(patientDataModel)){
                            postPatientRessource(patientDataModel);
                        }else {
                            Log.d("Patient","Patient data is incomplete");
                        }

                    }
                });
                Button postPractitionerButton = findViewById(R.id.btn_post_practitioner);

                postPractitionerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PractitionerDataModel practitionerDataModel = new PractitionerDataModel("1111","1.2.40.0.34.3.2.0","Tester","Tom",
                                "Dr.","144","Testerstraße","Testerstadt","8020","AUT");

                        if (validatePractitionerResource(practitionerDataModel)){
                            postPractitionerRessource(practitionerDataModel);

                        }else {
                            Log.d("Practitioner","PractitionerData not complete");
                        }

                    }
                });
            }
        });

        Button fetchAllPrescriptionsFromServer = findViewById(R.id.full_Prescritption_Button);

        fetchAllPrescriptionsFromServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here i want to fetch all the prescritption in order to store it in a LifeData model that will hold a List of FullPrescriptionDataModels

           fetchFullPrescriptions();

            }
        });


        // RoomDB Anbindung
        anbindungAnRoomDB();



    }
public void anbindungAnRoomDB(){
        // Nicht darauf vergessen das die DB ganz oben steht...
    corePatientProfileDatabase = CorePatientProfileDatabase.getDatabase(getApplicationContext());

    Button buttonRoomDB = findViewById(R.id.buttonRoomDB);
    buttonRoomDB.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CorePatientProfil patientRoomDB = new CorePatientProfil(1, "100", "1234010100", "turbotoll", "turboVorname", "Dr",
                    "male", "2000-01-01", "Patientenstrasse 1", "Graz", "Stmk", "8052", "AUT");
            addPersonInBackground(patientRoomDB);
            getPersonInBackground(patientRoomDB.getIdRoomDB());
        }
    });
}
    public void addPersonInBackground(CorePatientProfil patientRoomDB) {
        ExecutorService executorServiceDB = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorServiceDB.execute(new Runnable() {
            @Override
            public void run() {
                corePatientProfileDatabase.getCorePatientProfilDAO().addCorePatientProfil(patientRoomDB);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("RoomDB", "Added person to db");
                    }
                });
            }
        });
    }

    public void getPersonInBackground(int idRoomDB) {
        ExecutorService executorServiceDB = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorServiceDB.execute(new Runnable() {
            @Override
            public void run() {
                CorePatientProfil patientRoomDB = corePatientProfileDatabase.getCorePatientProfilDAO().getCorePatientProfil(idRoomDB);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (patientRoomDB != null) {
                            Log.d("RoomDB", "Retrieved person from db: " + patientRoomDB.toString());
                            Integer svnInt = Integer.parseInt(patientRoomDB.getIdentifierSocialSecurityNum());
                        } else {
                            Log.d("RoomDB", "No person found in db with id: " + idRoomDB);
                        }
                    }
                });
            }
        });
    }


    // Method to fetch full prescriptions
    private void fetchFullPrescriptions() {
        String patientId = "1599"; // Replace with the actual patient ID
        fullPrescriptionViewModel.fetchFullPrescriptions(patientId);
    }

    // Method to display the full prescriptions in the UI
    private void displayFullPrescriptions(List<FullPrescriptionDataModel> fullPrescriptions) {
        // Display the full prescriptions in the UI
        // For example, you could update a RecyclerView or other UI elements here
        for (FullPrescriptionDataModel fullPrescription : fullPrescriptions) {
            Log.d("FullPrescription", fullPrescription.toString());
            // Update UI elements with the full prescription data


          String SVN =   "SVNR: " + fullPrescription.getPatientDataModel().getIdentifierSocialSecurityNum();
          String family = fullPrescription.getPatientDataModel().getFamily();
          String given = fullPrescription.getPatientDataModel().getGiven();

          String line = fullPrescription.getPatientDataModel().getLine();
          String city = fullPrescription.getPatientDataModel().getCity();
          String postleizahl = fullPrescription.getPatientDataModel().getPostalCode();
          String state = fullPrescription.getPatientDataModel().getState();


          String suffix = fullPrescription.getPractitionerDataModel().getSuffix();
          String familyPractitioner = fullPrescription.getPractitionerDataModel().getFamily();
          String givenPractitioner = fullPrescription.getPractitionerDataModel().getGiven();

          String linePracttioner = fullPrescription.getPractitionerDataModel().getLine();
          String cityPractioner =  fullPrescription.getPractitionerDataModel().getCity();

          String medikaBeschreibung = fullPrescription.getMedicationRequestDataModelForFullPrescription().toString();




/*
         List<MedicationRequestDataModelForFullPrescription> allMedications = fullPrescription.getMedicationRequestDataModelForFullPrescription();

          List<String> medikas = new ArrayList<>();


            String medikamentname= null;
            String patientInstructions = null;
            String frequency = null;
            String when = null;


         for(MedicationRequestDataModelForFullPrescription medications: allMedications){

          medikamentname = medications.getDisplayMedication();
        List<MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription> alleDosageInstructions =  medications.getDosageInstructions();

        medikamentname = medications.getDisplayMedication();

        for (MedicationRequestDataModelForFullPrescription.DosageInstructionsForMedicationRequestDataModelForFullPrescription dosages: alleDosageInstructions){
            patientInstructions = dosages.getPatientInstruction();
            frequency = dosages.getFrequency();
            when = dosages.getWhen();

        }

        String ganzeAnleitungFürJedesMedika = medikamentname +"." + frequency +" oft am " +when +" einnehmen.";
        medikas.add(ganzeAnleitungFürJedesMedika);

        medikamentname = null;
        patientInstructions = null;
        frequency=null;
        when = null;


         }*/

         Log.d("Rezept","Rezept für:\n "+ SVN +" "+ family +" "+given+" "+line +" "+city+" "+postleizahl+" "+state +"\n "
                 +"Ausgestellt von:  "+ suffix +" "+familyPractitioner +" "+ givenPractitioner +" "+linePracttioner+" "+cityPractioner+"\n"+
                 " Bitte nehmen Sie folgende Medikamente wie folgt ein: "+ medikaBeschreibung);



        }
    }


    private void fetchPatientData() {
        String patientId = "1567"; // Replace with actual patient ID
        patientViewModel.fetchPatientData(patientId);
    }

    private void fetchMedicationRequestData() {
        String patientIdLinkedWithMedicationRequest = "1599";
        medicationRequestViewModels.get(0).fetchMedicationRequest(patientIdLinkedWithMedicationRequest);

        // achtung hier sollte es reichen einmal die View Models zu fetchen... Es sollte ja nur ein bundle zurückgeliefert werden.
       /* for (MedicationRequestViewModel viewModel : medicationRequestViewModels) {
            viewModel.fetchMedicationRequest(patientIdLinkedWithMedicationRequest);
        }*/
    }

    private void fetchPractitionerDataModel() {
        String practitionerId = "1554";
        practitionerViewModel.fetchPractitionerData(practitionerId);
    }

    private void displayPatientData(PatientDataModel patientDataModel) {
        TextView textView = findViewById(R.id.nameTextView);
        textView.setText(patientDataModel.toString());

    }

    private void displayMedicationRequestData(List<MedicationRequestDataModel> medicationRequestDataModels) {
        TextView textView = findViewById(R.id.textViewMedication);
        StringBuilder builder = new StringBuilder();
        for (MedicationRequestDataModel model : medicationRequestDataModels) {
            if (model != null) {
                builder.append(model.toString()).append("\n");
            }
        }
        textView.setText(builder.toString());
    }

    private void displayPostedMedicationRequestData(MedicationRequestDataModel medicationRequestDataModel){
        TextView textView = findViewById(R.id.textViewMedication);
        textView.setText(medicationRequestDataModel.toString());




    }

    private void displayPractitionerData(PractitionerDataModel practitioner) {
        TextView textView = findViewById(R.id.textViewPractitioner);
        textView.setText(practitioner.toString());
    }

    private boolean validateMedicationRequest(MedicationRequestDataModel medicationRequest) {
        return medicationRequest.getId() != null &&
                medicationRequest.getIdentifiereMedID() != null &&
                medicationRequest.getIdentifiereMedIDGroup() != null &&
                medicationRequest.getAspCode() != null &&
                medicationRequest.getDisplayMedication() != null &&
                medicationRequest.getRequester() != null &&
                medicationRequest.getSubject() != null &&
                medicationRequest.getDosageInstructions() != null &&
                !medicationRequest.getDosageInstructions().isEmpty();
    }

    private boolean validatePatientRessource(PatientDataModel patientDataModel){
        return patientDataModel.getId() != null &&
                patientDataModel.getFamily() != null &&
                patientDataModel.getGiven() != null &&
                patientDataModel.getPrefix() != null;
    }

    private boolean validatePractitionerResource(PractitionerDataModel practitioner){
        return practitioner.getId() != null &&
                practitioner.getLine() != null &&
                practitioner.getCity() != null &&
                practitioner.getPostalCode() != null &&
                practitioner.getFamily() != null &&
                practitioner.getGiven() != null &&
                practitioner.getSuffix() != null &&
                practitioner.getTelecom()!= null;

    }

    private void postMedicationRequest(MedicationRequestDataModel medicationRequest) {
        Log.d("MedicationRequest","posting Medication Request from main method");
        MedicationRequestViewModel viewModel = new ViewModelProvider(this).get(MedicationRequestViewModel.class);
        viewModel.postMedicationRequest(medicationRequest).observe(this, new Observer<MedicationRequestDataModel>() {
            @Override
            public void onChanged(MedicationRequestDataModel result) {
                if (result != null) {
                    Log.d("MedicationRequest", "MedicationRequest posted successfully: " + result.toString());
                    displayPostedMedicationRequestData(result);
                } else {
                    Log.d("MedicationRequest", "Failed to post MedicationRequest");
                }
            }
        });
    }

    private void postPatientRessource(PatientDataModel patientDataModel){
        Log.d("Patient", "Preparing to post new Patient from the main method");
        PatientViewModel viewModel = new ViewModelProvider(this).get(PatientViewModel.class);
        viewModel.postPatientRessource(patientDataModel).observe(this, new Observer<PatientDataModel>() {
            @Override
            public void onChanged(PatientDataModel result) {
                if (result != null){
                    Log.d("Patient", "Patient Ressource succesfully posted to server and response parsed." + result.toString());

                    displayPatientData(result);
                }
            }
        });

    }

    private void postPractitionerRessource(PractitionerDataModel practitionerDataModel){
        Log.d("Practitioner","Preparing to post new Practitioner from the main method");
        PractitionerViewModel viewModel = new ViewModelProvider(this).get(PractitionerViewModel.class);

        viewModel.postPractitionerRessource(practitionerDataModel).observe(this, new Observer<PractitionerDataModel>() {

            @Override
            public void onChanged(PractitionerDataModel result) {
                displayPractitionerData(result);
            }
        });
    }



}

/*

public void getPatientOverview(){
    final CountDownLatch latch = new CountDownLatch(1);
    ExecutorService service = Executors.newSingleThreadExecutor();
    service.execute(new Runnable() {

        @Override
        public void run() {

            DataStorage.getAllPatientsFromServer();
            latch.countDown();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


            runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                      /*  try {
                            latch.await();
                            patientDataJson = DataStorage.getPatientData();
                            Log.d("Testing", patientDataJson[0]);
                            displayData();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }

                        if (patientDataJson[0] != null) {


                        }
                    }
                });


        }
    });



}


public void displayData(){
Parser parser = new Parser();
    Log.d("Testing","Before creating Patient");
    Patient patient = parser.createPatient(patientDataJson[3]);


    TextView textView = findViewById(R.id.main);
    textView.setText(patient.getId() +" "+ patient.getFamily()+" " + patient.getGiven());
       for(int i = 0; i<patientDataJson.length;i++){
           Log.d("Testing",patientDataJson[i]);
       }



}
}
*/

