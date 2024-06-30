package com.example.pillpal420;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    /**
     * Wir beim Start der App als "Ladebildschirm" aufgerufen um unseren Servern Zeit zum Startup zu geben
     * <p>
     * Variablen:
     * - SPLASH_DISPLAY_LENGTH: lässt die Zeit einstellen die der SplashScreen sichtbar ist
     * <p>
     * Funktion:
     * 1. setzt die ContentView auf unseren SplashScreen der in activity_splash.xml definiert wurde
     * 2. dann erstellen wir einen neuen Handler der ein "Runnable" auf die message queue setzt
     * 3. durch SPLASH_DISPLAY_LENGTH wird die Dauer des SplashScreens definiert (in unserem Fall 2000ms)
     * 4. wir starten einen Intent der nach dem SplashScreen die LoginActivity startet
     * 5. SplashScreen wird geschlossen und vom stack entfernt
     */
    private static final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
