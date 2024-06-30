package com.example.pillpal420;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pillpal420.backend.CorePatientDataCallback;
import com.example.pillpal420.backend.roomDB.CorePatientProfil;
import com.example.pillpal420.backend.roomDB.CorePatientProfileDatabase;
import com.example.pillpal420.documentation.LogTag;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LogInActivity handled den user login. Es überprüft ob bereits ein Patienten- User profil (CorePatientProfil) in der RoomDB erstellt wurde.
 */
public class LoginActivity extends AppCompatActivity {
    private CorePatientProfileDatabase corePatientProfileDatabase;
    private CorePatientProfil corePatientProfil;
    private EditText vorname;
    private EditText svnNummer;
    private Button loginButton;

    /**
     * Diese Methode wird das erste mal aufgerufen wenn die Activity created wird. Es initialisiert die UI Components und
     * erstellt die ROOMDB conneciton.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vorname = findViewById(R.id.editPatientId);
        svnNummer = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.loginButton);

        corePatientProfileDatabase = CorePatientProfileDatabase.getDatabase(getApplicationContext());

        Log.d(LogTag.LOG_IN.getTag(), "Checking if the test patient needs to be created");
        checkAndCreateTestPatient();
        loginButton.setOnClickListener(v -> {
            String vornameCheck = vorname.getText().toString();
            String svnCheck = svnNummer.getText().toString();

            Log.d(LogTag.LOG_IN.getTag(), "Attempting to fetch patient login information");
            fetchPatientLogInInformation(1, new CorePatientDataCallback() {
                @Override
                public void onPatientDataLoaded(CorePatientProfil patient) {
                    corePatientProfil = patient;
                    Log.d(LogTag.LOG_IN.getTag(), "Patient data loaded from database: " + patient.toString());

                    if (validateLogin(vornameCheck, svnCheck)) {
                        Log.d(LogTag.LOG_IN.getTag(), "Login validated successfully. Navigating to MainActivity.");
                        Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(loginIntent);
                        finish();
                    } else {
                        Log.d(LogTag.LOG_IN.getTag(), "Login validation failed. Incorrect username or password.");
                        Toast.makeText(LoginActivity.this, "Fehler: falscher Benutzername oder Passwort", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onDataNotAvailable() {
                    Log.d(LogTag.LOG_IN.getTag(), "Patient data not available in the database.");
                    Toast.makeText(LoginActivity.this, "Patientendaten nicht verfügbar", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    /**
     *
     */
    private void checkAndCreateTestPatient() {
        fetchPatientLogInInformation(1, new CorePatientDataCallback() {
            @Override
            public void onPatientDataLoaded(CorePatientProfil patient) {
                corePatientProfil = patient;  // Patient exists, no need to create
                Log.d(LogTag.LOG_IN.getTag(), "Test patient already exists in the database.");
            }

            @Override
            public void onDataNotAvailable() {
                // Patient does not exist, create a test patient
                Log.d(LogTag.LOG_IN.getTag(), "Test patient not found in the database. Creating a new test patient.");
                createTestPatientForLogInOnlyOnce();
            }
        });
    }

    /**
     *
     * @param vornameCheck
     * @param svnCheck
     * @return
     */
    private boolean validateLogin(String vornameCheck, String svnCheck) {
        boolean isValid = vornameCheck.equals(corePatientProfil.getFamily()) && svnCheck.equals(corePatientProfil.getIdentifierSocialSecurityNum());
        Log.d(LogTag.LOG_IN.getTag(), "Login validation result: " + isValid);
        return isValid;
    }

    /**
     *
     * @param patientRoomDB
     */
    public void addPersonInBackground(CorePatientProfil patientRoomDB) {
        ExecutorService executorServiceDB = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorServiceDB.execute(() -> {
            corePatientProfileDatabase.getCorePatientProfilDAO().addCorePatientProfil(patientRoomDB);
            handler.post(() -> Log.d(LogTag.LOG_IN.getTag(), "Added Patient to DB"));
        });
    }

    private void fetchPatientLogInInformation(int idRoomDB, CorePatientDataCallback callback) {
        ExecutorService executorServiceDB = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorServiceDB.execute(() -> {
            CorePatientProfil patient = corePatientProfileDatabase.getCorePatientProfilDAO().getCorePatientProfil(idRoomDB);
            handler.post(() -> {
                if (patient != null) {
                    Log.d(LogTag.LOG_IN.getTag(), "Successfully fetched patient data from database.");
                    callback.onPatientDataLoaded(patient);
                } else {
                    Log.d(LogTag.LOG_IN.getTag(), "No patient data found in database for id: " + idRoomDB);
                    callback.onDataNotAvailable();
                }
            });
        });
    }

    /**
     *
     */
    public void createTestPatientForLogInOnlyOnce() {
        CorePatientProfil patientRoomDB0 = new CorePatientProfil(1, "1599", "0", "Tom", "turbo", "Dr",
                "male", "2000-01-01", "Patientenstrasse 1", "Graz", "Stmk", "8052", "AUT");
        addPersonInBackground(patientRoomDB0);
        Log.d(LogTag.LOG_IN.getTag(), "Test patient created and added to database.");
    }
}