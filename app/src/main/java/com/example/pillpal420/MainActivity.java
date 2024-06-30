package com.example.pillpal420;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import android.view.MenuItem;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.pillpal420.documentation.LogTag;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    /**
     * Wird nach dem LoginScreen aufgerufen. Hier wird das Layout für die MainActivity verwaltet.
     * Zusätzlich konfigurieren wir unseren Navigation Drawer und fragen die nötigen Permissions (Camera, Record Audio, Storage) ab
     *
     *
     * Funktion:
     * 1. Wir setzen unsere Toolbar als ActionBar
     * 2. Wir initialisieren den Navigation Drawer und setzen die NavigationView
     * 3. Wir setzen den ActionBarDrawerToggle um den Navigation Drawer öffnen und schließen zu können
     * 4. Wir synchronisieren den Status des Navigation Drawers
     * 5. Wenn savedInstanceState = null (beim ersten Aufrufen) laden wir ein Fragment
     * 6. Wir fragen die Kamera und Mikrofon Berechtigungen ab
     * 7. Wir fragen die Speicher Berechtigungen ab
     */
    private static final int CAMMIC_REQUEST = 24;
    private static final int REQUEST_PERMISSIONS = 123;
    private DrawerLayout dLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TOOLBAR
        Toolbar tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        dLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dLayout, tBar, R.string.open_navdrawer, R.string.close_navdrawer);

        dLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            //nav1 = Home
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav1()).commit();
            navView.setCheckedItem(R.id.nav_home);
        }
            cammicPermission();
            if(!checkStoragePermissions()){
                reqStoragePermission();
            }
    }

    /**
     *Hier ist die Methode um die Kamera und Mikrofon Berechtigungen abzufragen
     *
     * Funktion:
     * 1. checkSelfPermission für Kamera und Mikrofon
     * 2. ist diese nicht gegeben wird die Berechtigung angefragt
     * 3. ist diese gegeben passiert nichts
     *
     * Wir rufen diese Methode bei jedem App-Start auf weil es sein kann, dass der User/die Userin die Berechtigungen händisch entfernt hat
     */
    //Kamera Permission
    private void cammicPermission(){

        if((ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)){

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO}, CAMMIC_REQUEST);

        }
    }

    /**
     * Hier wird die Speicher-Berechtigung einerseits für Android 11 und höher, und andererseits für unter Android 11 abgefragt
     * Android 11 und darüber: wir fragen ab ob die App Zugriff auf den ganzen externen Speicher hat oder nicht
     * Unter Android 11: hier wird überprüft ob die App Lese und Schreib Rechte für den Speicher besitzt
     *
     * @return {@code true} wenn die App die Speicher Berechtigungen hat
     *         {@code false} wenn die App die Berechtigungen noch nicht hat
     */
    public boolean checkStoragePermissions(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android 11 oder darüber
            return Environment.isExternalStorageManager();
        }else{
            //Unter Android 11
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED;
        }
    }

    /**
     * Hier wird die Berechtigung für den Speicher angefragt
     * Für Android 11 und darüber: Anfrage für MANAGE_ALL_FILES_ACCESS_PERMISSION (ganzer Speicherzugriff)
     * Unter Android 11: Anfrage für die Lese und Schreib Rechte für den Speicher
     *
     * Funktion:
     * Für Android 11 und darüber:
     * 1. Erstellen eines Intents --> setAction {@link Settings#ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION}
     * 2. Wir übergeben die Uri dem Intent um zu spezifizieren dass wir für genau das Package die Berechtigung anfragen
     * 3. Dann launchen wir den Intent mit {@code storageLauncher}
     * 4. Wenn dies fehlschlägt fangen wir die Exceptions ab und versuchen es ohne die Uri erneut
     *
     * Unter Android 11:
     * 1. über {@link ActivityCompat#requestPermissions(Activity, String[], int)} fragen wir die Berechtigungen für {@link Manifest.permission.WRITE_EXTERNAL_STORAGE} und
     *    {@link Manifest.permission.READ_EXTERNAL_STORAGE} ab.
     */
    private void reqStoragePermission(){
        //Android 11 und drüber
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(),null);
                intent.setData(uri);
                storageLauncher.launch(intent);
            }catch(Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageLauncher.launch(intent);
            }
        }else{
            //Unter Android 11
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                                                        Manifest.permission.READ_EXTERNAL_STORAGE}, 
                                                                                            REQUEST_PERMISSIONS);
        }
    }

    /**
     *Hier überprüfen wir ob die Berechtigung für Android 11 oder höher erteilt wurde oder nicht
     *
     * Wenn ja: keine weitern Schritte notwendig
     * Wenn nein: Lassen wir eine Toast-Message erscheinen die dem User/der Userin anzeigt dass Sie den Zugriff nicht gewährt hat
     */
    //lambda anstatt new ActivityResultCallback<ActivityResult>()
private ActivityResultLauncher<Intent> storageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), o -> {
                //über Android 11
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                    //External Storage granted
                    if(Environment.isExternalStorageManager()){

                    }else{
                        Toast.makeText(MainActivity.this, R.string.access_denied,Toast.LENGTH_SHORT).show();
                    }
                }
            });


    /**
     * Hier bearbeiten wir die item-Auswahl im Navigation Drawer
     *
     * Funktion:
     * 1. wir schauen welches Item ausgewählt wurde (anhand der ID)
     * 2. Dann ersetzen wir das bestehende Fragment mit dem ausgewählten Fragment:
     *      -R.id.nav_home: Fragment_nav1 wird ausgewählt (unser Scan Fragment)
     *      -R.id.nav_scan: Fragment_nav2 wird ausgewählt (Anzeige der Rezepte Fragment)
     *      -R.id.nav_inventur: Fragment_nav3 wird ausgewählt (Inventur Fragment)
     *      -R.id.nav_help: Fragment_nav4 wird ausgewählt (Hilfe Fragment)
     *      -R.id.nav_whisper: Whisper wird ausgewählt (Spracheingabe Fragment)
     *      -R.id.nav_f5: Fragment_nav5 wird ausgewählt (Chatbot Fragment)
     *      -R.id.nav_logout: der {@code logoutDialog} wird aufgerufen wobei der User/die Userin gefragt wird ob er/sie sich wirklich abmelden möchte
     * 3. der NavigationDrawer wird geschlossen
     * @param item das ausgewählte MenuItem in der UI
     * @return true um das ausgewählte Item anzuzeigen
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav1()).commit();

        } else if (item.getItemId() == R.id.nav_scan) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav2()).commit();

        } else if (item.getItemId() == R.id.nav_inventur) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav3()).commit();

        } else if (item.getItemId() == R.id.nav_help) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav4()).commit();

        }else if (item.getItemId() == R.id.nav_whisper){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Whisper()).commit();

        }else if(item.getItemId() == R.id.nav_f5){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav5()).commit();

        }else if(item.getItemId() == R.id.nav_logout){

            logoutDialog();

        }

        dLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Hier schreiben wir einen Dialog für die Bestätigung oder Ablehnung des Logouts
     *
     * Wir erstellen einen AlertDialog mit:
     * -Titel "Logout"
     * -Nachricht "Wollen Sie sich wirklich abmelden?
     * -Einem OK-Button der die LoginActivity aufruft und den User/die Userin vom aktuellen ActivityStack entfernt
     * -Einem Cancel-Button der das Dialogfeld schließt
     * -Einem Warning-Icon
     */
    private void logoutDialog(){

        new AlertDialog.Builder(this).setTitle("Logout").setMessage("Wollen Sie sich wirklich abmelden?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton(android.R.string.no,null).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}