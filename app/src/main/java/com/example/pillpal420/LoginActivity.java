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

public class LoginActivity extends AppCompatActivity {

    // ROOM DB Anbindung
    private CorePatientProfileDatabase corePatientProfileDatabase;
    private CorePatientProfil corePatientProfil;

    private EditText vorname;
    private EditText svnNummer;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        vorname = findViewById(R.id.editPatientId);
        svnNummer = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.loginButton);

        corePatientProfileDatabase = CorePatientProfileDatabase.getDatabase(getApplicationContext());
        //derweil nicht wichtig
        /*CorePatientProfil patientRoomDB = new CorePatientProfil(1, "100", "1234", "turbotoll", "turboVorname", "Dr",
                "male", "2000-01-01", "Patientenstrasse 1", "Graz", "Stmk", "8052", "AUT");
        addPersonInBackground(patientRoomDB);*/
        // Fetch patient login information
        fetchPatientLogInInformation(1); // Assuming the patient ID is 1 for testing

        loginButton.setOnClickListener(v -> {
            String vornameCheck = vorname.getText().toString();
            String svnCheck = svnNummer.getText().toString();

            if (corePatientProfil != null) {
                if (validateLogin(vornameCheck, svnCheck)) {

                    Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                    loginIntent.putExtra("USER_ID",svnCheck );
                    startActivity(loginIntent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Fehler: falscher Benutzername oder Passwort", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Patientendaten nicht verfügbar", Toast.LENGTH_SHORT).show();
            }
        });


// back end
// pass id to main activity




    }

    private boolean validateLogin(String vornameCheck, String svnCheck) {
        return vornameCheck.equals(corePatientProfil.getFamily()) && svnCheck.equals(corePatientProfil.getIdentifierSocialSecurityNum());
    }

    private void fetchPatientLogInInformation(int idRoomDB) {
        getPatientFromDB(idRoomDB, new CorePatientDataCallback() {
            @Override
            public void onPatientDataLoaded(CorePatientProfil patient) {
                corePatientProfil = patient;
                Log.d(LogTag.ROOM_DB.getTag(), "Retrieved person from db: " + patient.toString());
            }

            @Override
            public void onDataNotAvailable() {
                Log.d(LogTag.ROOM_DB.getTag(), "No person found in db with id: " + idRoomDB);
            }
        });
    }

    private void getPatientFromDB(int idRoomDB, CorePatientDataCallback callback) {
        ExecutorService executorServiceDB = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executorServiceDB.execute(() -> {
            CorePatientProfil patient = corePatientProfileDatabase.getCorePatientProfilDAO().getCorePatientProfil(idRoomDB);
            handler.post(() -> {
                if (patient != null) {
                    Log.d(LogTag.ROOM_DB.getTag(), "Request Succesfull");
                    callback.onPatientDataLoaded(patient);
                } else {
                    callback.onDataNotAvailable();
                }
            });
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


}