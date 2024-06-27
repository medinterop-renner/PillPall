package com.example.pillpal420;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

    private static final int REQUEST_IMAGE_CAPTURE = 69;
    private Uri picUri;
    private LinearLayout picLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View invView = inflater.inflate(R.layout.fragment_inventur, container, false);

        Button btnPic = invView.findViewById(R.id.picBtn);
        picLayout = invView.findViewById(R.id.linPicLayout);

        btnPic.setOnClickListener( v -> openCam());



        return invView;
    }

    private void openCam(){

        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(picIntent.resolveActivity(getActivity().getPackageManager()) != null){

            File picFile = null;

            try{
                picFile = createPicFile();
            }catch(IOException e){

                Log.e("Fragment_nav3", "Fehler bei File-Creation", e);
            }
            if(picFile != null){
                picUri = FileProvider.getUriForFile(getActivity(), "com.example.pillpal420.fileprovider", picFile);
                picIntent.putExtra(MediaStore.EXTRA_OUTPUT, picUri);
                startActivityForResult(picIntent, REQUEST_IMAGE_CAPTURE);

            }
        }
    }

    private File createPicFile() throws IOException{

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String picFileName = "JPEG_" + timeStamp +"_";
        File storageDir = getActivity().getExternalFilesDir(null);
        File pic = File.createTempFile(picFileName, ".jpg", storageDir);

        return pic;
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, @Nullable Intent data){
        super.onActivityResult(reqCode, resCode, data);

        if(reqCode == REQUEST_IMAGE_CAPTURE && resCode == Activity.RESULT_OK){
            galleryAddPic();
            displayPic();
        }
    }

    private void galleryAddPic(){
        Intent scanMedia = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanMedia.setData(picUri);
        getActivity().sendBroadcast(scanMedia);
    }

    private void displayPic(){
        View picItem = getLayoutInflater().inflate(R.layout.inventory_item, picLayout, false);
        ImageView imgView = picItem.findViewById(R.id.invImg);
        EditText editName = picItem.findViewById(R.id.invName);
        EditText editExpiryDate = picItem.findViewById(R.id.invexpiryDate);

        Button removeBtn = picItem.findViewById(R.id.invRemoveBtn);

        imgView.setImageURI(picUri);
        removeBtn.setOnClickListener(v -> picLayout.removeView(picItem));

        picLayout.addView(picItem, 0);



    }



}