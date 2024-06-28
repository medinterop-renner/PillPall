package com.example.pillpal420;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//Inventur Fragment
public class Fragment_nav3 extends Fragment {
    private static final String invTAG = "InvFragment";
    private static final String PREFS_NAME = "Preferences";
    private static final String KEY_PICS = "pics";
    private Uri picUri;
    private LinearLayout invLinLayout;
    private ActivityResultLauncher<Uri> invPicLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View invView = inflater.inflate(R.layout.fragment_inventur, container, false);
        Button invPicBtn = invView.findViewById(R.id.invPicBtn);
        invLinLayout = invView.findViewById(R.id.invLinLayout);

        invPicLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if(result){
                addPicTags(picUri);
                savePics();
            }else{
                Toast.makeText(getActivity(), R.string.pic_error, Toast.LENGTH_SHORT).show();
            }
        });

        invPicBtn.setOnClickListener(v -> openCam());

        loadPics();
        return invView;
    }

    private void openCam(){
        savePics();
        File picFile = null;
        try{
            picFile = createPicFile();
        }catch(IOException e){
            Log.e(invTAG, "File error", e);
        }
        if(picFile != null){
            picUri = FileProvider.getUriForFile(getActivity(), "com.example.pillpal420.fileprovider", picFile);
            invPicLauncher.launch(picUri);
        }
    }

    private File createPicFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String picFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(picFileName, ".jpg", storageDir);
    }

    private void addPicTags(Uri picUri){
        View addTagView = getLayoutInflater().inflate(R.layout.inventory_item, invLinLayout, false);

        ImageView imgView = addTagView.findViewById(R.id.imgView);
        EditText editName = addTagView.findViewById(R.id.editName);
        EditText editExpiryDate = addTagView.findViewById(R.id.editExpiryDate);
        Button deleteBtn  = addTagView.findViewById(R.id.deleteBtn);

        imgView.setImageURI(picUri);
        imgView.setTag(picUri.toString());
        deleteBtn.setOnClickListener(v -> {
            invLinLayout.removeView(addTagView);
            savePics();
        });

        invLinLayout.addView(addTagView);
    }

    private void savePics(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        JSONArray jsonArr = new JSONArray();
        for (int i = 0; i < invLinLayout.getChildCount(); i++){
            View view = invLinLayout.getChildAt(i);
            ImageView imgView = view.findViewById(R.id.imgView);
            EditText editName = view.findViewById(R.id.editName);
            EditText editExpiryDate = view.findViewById(R.id.editExpiryDate);

            // funktioniert nicht: Uri picUri = imgView.getStringURI(); -->
            String imgUriString = (String) imgView.getTag();
            Uri picUri = Uri.parse(imgUriString);
            String editName22 = editName.getText().toString();
            String editExpiryDate22 = editExpiryDate.getText().toString();

            JSONObject jsonObj = new JSONObject();
            try{
                jsonObj.put("picUri", picUri.toString());
                jsonObj.put("editName", editName22);
                jsonObj.put("editExpiryDate", editExpiryDate22);
                jsonArr.put(jsonObj);

            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        editor.putString(KEY_PICS, jsonArr.toString());
        editor.apply();
    }

    private void loadPics(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonPics = sharedPref.getString(KEY_PICS, "");

        if(!jsonPics.isEmpty()){
            try{
                JSONArray jsonArr = new JSONArray(jsonPics);
                for(int i = 0; i < jsonArr.length(); i++){
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    String imgUriString = jsonObj.getString("picUri");
                    String editName = jsonObj.getString("editName");
                    String editExpiryDate = jsonObj.getString("editExpiryDate");

                    Uri picUri = Uri.parse(imgUriString);
                    addPicTags(picUri);

                    View view = invLinLayout.getChildAt(invLinLayout.getChildCount() -1);
                    EditText editName1 = view.findViewById(R.id.editName);
                    EditText editExpiryDate1 = view.findViewById(R.id.editExpiryDate);

                    editName1.setText(editName);
                    editExpiryDate1.setText(editExpiryDate);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
}