package com.example.pillpal420;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.pillpal420.documentation.LogTag;
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

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//Scan Fragment Magic
public class Fragment_nav1 extends Fragment {

    private static final String TAG = "Fragment_nav1";
    private Uri picUri;
    private ImageView scanImgView;
    private TextView scanTextView;
    private ActivityResultLauncher<Uri> scanPicLauncher;

    public static String finalString;
    //Consti this urs ^

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
                Toast.makeText(getActivity(), R.string.pic_nottaken, Toast.LENGTH_SHORT).show();
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

                //mach diese String
                getStringFromScan(visionText);
                //zeig String in TextView
                scanImgView.setImageURI(uri);

            }).addOnFailureListener(e -> {
                Log.e(TAG, "Text nicht erkannt", e);
                Toast.makeText(getActivity(),R.string.text_notfound, Toast.LENGTH_SHORT).show();
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
        Log.d(LogTag.VISION.getTag(), resultText.toString());
        scanTextView.setText(resultText.toString());
    }

    //CONSTI DA IS DEIN STRING LES GOOOOOOOO
    private void getStringFromScan(Text visionText){
        StringBuilder text = new StringBuilder();

        for (Text.TextBlock block : visionText.getTextBlocks()){
            text.append(block.getText());
        }
        //!!!! EINZIGER STRING SONST NIX ANGREIFEN/VERWENDEN !!!!!
        finalString = text.toString();
        Log.d("Testing",finalString);

        sendTextToServer(finalString);
    //-------------------------------------------------------------
    }


    private void sendTextToServer(String text) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, text);
        Request request = new Request.Builder()
                .url("http://192.168.0.2:8000/upload")
                .post(body)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error sending text to server: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Server returned error: " + response.message());
                } else {
                    Log.d(TAG, "Server response: " + response.body().string());
                }
            }
        });
    }
}