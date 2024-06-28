package com.example.pillpal420;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


//Inventur Fragment
public class Fragment_nav3 extends Fragment {

    public static final String TAG = "CamFragment";
    private Uri picUri;
    private ActivityResultLauncher<Uri> picLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View invView = inflater.inflate(R.layout.fragment_inventur, container, false);

        Button picBtn = invView.findViewById(R.id.invPicBtn);
        ImageView invImgView = invView.findViewById(R.id.invImgView);

        picLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result ->{

            if(result){
                displayPicture(invImgView, picUri);
                Toast.makeText(getActivity(), "Foto wurde erfolgreich gespeichert", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "Bild wurde nicht gespeichert", Toast.LENGTH_SHORT).show();
            }
        });

        picBtn.setOnClickListener(v -> openCam());

        return invView;
    }

    public void openCam(){
        File picFile = null;

        try{
            picFile = createImageFile();
        }catch(IOException e){
            Log.e(TAG, "Fehler beim File erstellen", e);
        }
        if(picFile != null){
            picUri = FileProvider.getUriForFile(getActivity(),"com.example.pillpal420.fileprovider", picFile);
            picLauncher.launch(picUri);
        }
    }

    private void displayPicture(ImageView imgView, Uri imgUri  ) {
        if (imgUri != null) {
            imgView.setImageURI(imgUri);
        } else {
            Toast.makeText(getActivity(), "Uri is null", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String picFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File picture = File.createTempFile(picFileName, ".jpg",storageDir);

        return picture;
    }

    private void showSavedPics(){
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File[] pics = storageDir.listFiles();
        if(pics != null){
            for (File file : pics){
                Log.d(TAG, "Bilder " + file.getAbsolutePath());
            }
        }else{
            Log.d(TAG, "Keine Bilder vorhanden");
        }
    }

}