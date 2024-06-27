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

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Fragment_nav3.BtnClickListener{


    private static final int CAM_REQUEST = 24;
    private static final int STORAGE_PERMISSION_CODE = 23;
    private static final int REQUEST_PERMISSIONS = 123;
    private static final int REQUEST_IMAGE_EDIT = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 69;
    private DrawerLayout dLayout;
    //private List<InvItem> invList;
    //private InvAdapter invAdapter;
    private String currentPicPath;
    Fragment_nav3 frag3 = new Fragment_nav3();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inventur Fragment wenn diese geht ich fress an Besen
        RecyclerView recView = frag3.getRecView();

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
            camPermission();
            reqStoragePermission();
    }
    //Kamera Permission
    private void camPermission(){

        if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAM_REQUEST);

        }

    }
    //Permission Request weil Android 14 mich nicht mag :(
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
private ActivityResultLauncher<Intent> storageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    //über Android 11
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        //External Storage granted
                        if(Environment.isExternalStorageManager()){

                        }else{
                            Toast.makeText(MainActivity.this, "Zugriff nicht gewährt",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    @Override
    public void onRequestPermissionsResult(int reqCode, String[] perm, int[] grantRes){
            super.onRequestPermissionsResult(reqCode, perm, grantRes);

            if(reqCode == STORAGE_PERMISSION_CODE){
                if(grantRes.length > 0){
                    boolean write = grantRes[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read = grantRes[1] == PackageManager.PERMISSION_GRANTED;

                    if(read && write){
                        Toast.makeText(this, "Zugriff erhalten", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(this, "Zugriff nicht gewährt", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    }

    @Override
    public void onButtonClicked() {

            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(picIntent.resolveActivity(getPackageManager()) != null){
                File picFile = null;
                try{
                    picFile = createImageFile();
                }catch (IOException e){
                    e.printStackTrace();
                }
                if(picFile != null){
                    Uri picUri = FileProvider.getUriForFile(this, "com.example.pillpal420.fileprovider", picFile);
                    picIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                    startActivityForResult(picIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String picName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File pic = File.createTempFile(picName, ".jpg", storageDir);

        currentPicPath = pic.getAbsolutePath();
        return pic;
    }


    @Override
    protected void onActivityResult(int reqCode, int resCode, @Nullable Intent data){
        super.onActivityResult(reqCode, resCode, data);

        if(reqCode == REQUEST_IMAGE_EDIT && resCode == RESULT_OK){
            File file = new File(currentPicPath);

            if(file.exists()){
                frag3.getInvList().add(new InvItem(currentPicPath, "Kein Name","Kein Datum"));
                frag3.getInvAdapter().notifyDataSetChanged();
            }

        }else if(reqCode == REQUEST_IMAGE_EDIT && resCode == RESULT_OK && data != null){
            String picPath = data.getStringExtra("picPath");
            String name = data.getStringExtra("name");
            String date = data.getStringExtra("date");

            for (InvItem item : frag3.getInvList()){
                if(item.getPicPath().equals(picPath)){
                    item.setName(name);
                    item.setDate(date);
                    frag3.getInvAdapter().notifyDataSetChanged();
                    break;

                }
            }
        }
    }


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