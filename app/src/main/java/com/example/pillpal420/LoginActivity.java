package com.example.pillpal420;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

public class LoginActivity extends AppCompatActivity {

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

        loginButton.setOnClickListener(v -> {

            String vornameCheck = vorname.getText().toString();
            String svnCheck = svnNummer.getText().toString();

            if(validateLogin(vornameCheck, svnCheck)) {
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(loginIntent);
                finish();

            }else {
                Toast.makeText(LoginActivity.this, "Fehler: falscher Benutzername oder Passwort", Toast.LENGTH_SHORT).show();
            }


        });



    }

    private boolean validateLogin(String patientID, String password){

        if(patientID.equals("999") && password.equals("999")){
            return true;
        }else{
            return false;
        }

    }
}