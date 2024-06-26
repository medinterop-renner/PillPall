package com.example.pillpal420;

import androidx.activity.result.ActivityResultLauncher;
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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;
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

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Fragment_nav3.BtnClickListener{

    private DrawerLayout dLayout;
    private List<InvItem> invList;
    private InvAdapter invAdapter;
    private String currentPicPath;
    Fragment_nav3 frag3 = new Fragment_nav3();
    private static final int REQUEST_PERMISSIONS = 123;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inventur Fragment wenn diese geht ich fress an Besen
        RecyclerView recView = frag3.getRecView();
        invList = new ArrayList<>();
        invAdapter = new InvAdapter(invList);

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
        requestPermissions();
    }

    //Permission Request weil Android 14 mich nicht mag :(
    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                //Request für internal Storage
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        } else {
            //für ältere Android Versionen
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSIONS);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Berechtigungen erteilt
                Toast.makeText(this, "Berechtigungen erteilt", Toast.LENGTH_SHORT).show();
            } else {
                // Berechtigungen verweigert
                Toast.makeText(this, "Berechtigungen erforderlich, um fortzufahren", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Vorbereiten der Bilddatei
    private void prepImgFile(){
        try{
            createImageFile();
        }catch(IOException e){
            Toast.makeText(this, "Fml", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    //Inventur Fragment onButtonClicked, Kamera öffnen und Bild machen
    @Override
    public void onButtonClicked() {

        Toast.makeText(this, "Kamera auf", Toast.LENGTH_SHORT).show();

        if (currentPicPath == null || currentPicPath.isEmpty()) {
            prepImgFile();
        }
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (picIntent.resolveActivity(getPackageManager()) != null) {
            File picFile = new File(currentPicPath);
            if (picFile != null) {

                Uri picUri = FileProvider.getUriForFile(this, "com.example.pillpal420.fileprovider", picFile);
                picIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                picLauncher.launch(picIntent);
            }
        }else{
            Toast.makeText(this, "Keine Kamera gefunden", Toast.LENGTH_SHORT).show();
        }
    }

    //Erstellung Image File
    private File createImageFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String picName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File pic = File.createTempFile(picName, ".jpg", storageDir);

        //Dateipfad speichern
        currentPicPath = pic.getAbsolutePath();
        return pic;
    }
        //moderne Version von onActivityResult
    private final ActivityResultLauncher<Intent> picLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    File file = new File(currentPicPath);
                    if (file.exists()) {
                        Bitmap imageBitmap = BitmapFactory.decodeFile(currentPicPath);
                        invList.add(new InvItem(currentPicPath, imageBitmap, "Unbenannt", "Kein Ablaufdatum"));
                        invAdapter.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(this, "Fehler", Toast.LENGTH_SHORT).show();
                }
            });

    //Navigation Drawer zafetzung (leider ohne switch/case :(
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.nav_home){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav1()).commit();

        }else if(item.getItemId() == R.id.nav_scan){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav2()).commit();

        }else if(item.getItemId() == R.id.nav_inventur){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav3()).commit();

        }else if(item.getItemId() == R.id.nav_help){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_nav4()).commit();

        }else if(item.getItemId() == R.id.nav_logout){

            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();

        }

        dLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}