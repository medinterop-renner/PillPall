package com.example.pillpal420;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//Scan Fragment Magic
public class Fragment_nav1 extends Fragment {

    private static final String TAG = "Fragment_nav1";
    private Uri picUri;
    private ImageView scanImgView;
    private TextView scanTextView;
    private ActivityResultLauncher<Uri> scanPicLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View scanView =  inflater.inflate(R.layout.fragment_home, container, false);

        Button scanBtn = scanView.findViewById(R.id.scanBtn);
        scanImgView = scanView.findViewById(R.id.scanImgView);
        scanTextView = scanView.findViewById(R.id.scanTextView);

        scanPicLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result ->{

            if(result){
                processImg(picUri);
            }else{
                Toast.makeText(getActivity(), "Bild nicht gemacht", Toast.LENGTH_SHORT).show();
            }

        });

        scanBtn.setOnClickListener(v -> openCam());


        return scanView;
    }

    private void openCam(){
        File scanPicFile = null;
        try{
            scanPicFile = createImageFile();
        }catch(IOException e){
            Log.e(TAG, "File create error", e);
        }
        if(scanPicFile != null){
            picUri = FileProvider.getUriForFile(getActivity(), "com.example.pillpal420.fileprovider", scanPicFile);
            scanPicLauncher.launch(picUri);
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String scanFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(scanFileName, ".jgp",storageDir);
    }

    private void processImg(Uri uri){
        try{
            InputImage img = InputImage.fromFilePath(getContext(),uri);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

            recognizer.process(img).addOnSuccessListener(visionText -> {
                displayText(visionText);
                //diese Methode brauchen wir
                //getStringFromScan(visionText);
                scanImgView.setImageURI(uri);

            }).addOnFailureListener(e -> {
                Log.e(TAG, "Text nicht erkannt", e);
                Toast.makeText(getActivity(),"Text nicht erkannt", Toast.LENGTH_SHORT).show();
            });

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private void displayText(Text visionText){
        StringBuilder resultText = new StringBuilder();
        for(Text.TextBlock block : visionText.getTextBlocks()){
            resultText.append(block.getText()).append("\n");
        }
        scanTextView.setText(resultText.toString());
    }

    //CONSTI DA IS DEIN STRING LES GOOOOOOOO
    private void getStringFromScan(Text visionText){
        StringBuilder text = new StringBuilder();
        String finalString;

        for (Text.TextBlock block : visionText.getTextBlocks()){
            text.append(block.getText());
        }
        //!!!! EINZIGER STRING SONST NIX ANGREIFEN/VERWENDEN !!!!!
        finalString = text.toString();
    //-------------------------------------------------------------
    }

}