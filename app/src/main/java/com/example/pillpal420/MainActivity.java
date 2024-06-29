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
    //backend

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
    //Kamera Permission
    private void cammicPermission(){

        if((ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)){

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO}, CAMMIC_REQUEST);

        }
    }
    //Permission Request weil Android 14 mich nicht mag :( add Mic Permission!!!
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



    //Navigation Drawer zafetzung (leider ohne switch/case :(
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