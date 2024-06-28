package com.example.pillpal420;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.pillpal420.backend.dataModels.FullPrescriptionDataModel;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModel;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.viewModels.FullPrescriptionViewModel;
import com.example.pillpal420.backend.viewModels.WhisperViewModel;
import com.example.pillpal420.documentation.LogTag;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Whisper extends Fragment {

    static String speechToFHIRString;

    private MedicationRequestDataModel medicationRequestDataModel;
 private WhisperViewModel whisperViewModel;


    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_STORAGE_PERMISSION = 201;
    private static final String LOG_TAG = "AudioRecordTest";
    private static final String FILE_NAME = "recorded_audio.mp3";

    private Button startRecordButton;
    private Button stopRecordButton;
    private Button saveButton;
    private Button resetButton;
    private Button sendButton;
    private TextView statusText;
    private MediaRecorder recorder = null;
    private boolean isRecording = false;

    private String filePath;

    private OkHttpClient client = new OkHttpClient();
    public static final MediaType MEDIA_TYPE_MP3 = MediaType.parse("audio/mpeg");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View whispView = inflater.inflate(R.layout.fragment_whisper, container, false);

        startRecordButton = whispView.findViewById(R.id.startRecordButton);
        stopRecordButton = whispView.findViewById(R.id.stopRecordButton);
        saveButton = whispView.findViewById(R.id.saveButton);
        resetButton = whispView.findViewById(R.id.resetButton);
        sendButton = whispView.findViewById(R.id.sendButton);
        statusText = whispView.findViewById(R.id.statusText);

        filePath = getActivity().getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/" + FILE_NAME;

        startRecordButton.setOnClickListener(v -> {

                startRecording();

        });

        stopRecordButton.setOnClickListener(v -> stopRecording());

        saveButton.setOnClickListener(v -> saveRecording());

        resetButton.setOnClickListener(v -> resetRecording());

        sendButton.setOnClickListener(v -> sendFileToServer());


       whisperViewModel = new ViewModelProvider(this).get(WhisperViewModel.class);
        whisperViewModel.getPatientLiveData().observe(getViewLifecycleOwner(), new Observer<PatientDataModel>() {
            @Override
            public void onChanged(PatientDataModel patientDataModel) {
                Log.d("Testing",patientDataModel.toString());
            }
        });

/*
        fullPrescriptionViewModel = new ViewModelProvider(this).get(FullPrescriptionViewModel.class);

        fullPrescriptionViewModel.getFullPrescriptionLiveData().observe(getViewLifecycleOwner(), new Observer<List<FullPrescriptionDataModel>>() {
            @Override
            public void onChanged(List<FullPrescriptionDataModel> fullPrescriptionDataModels) {
                if (fullPrescriptionDataModels != null) {
                    displayFullPrescriptionDataModels(fullPrescriptionDataModels);


                }
            }
        });
*/

        return whispView;


    }



    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(filePath);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
        isRecording = true;
        startRecordButton.setEnabled(false);
        stopRecordButton.setEnabled(true);
        statusText.setText("Recording...");
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording = false;
        stopRecordButton.setEnabled(false);
        saveButton.setEnabled(true);
        statusText.setText("Recording stopped.");
    }

    private void saveRecording() {
        saveButton.setEnabled(false);
        resetButton.setEnabled(true);
        sendButton.setEnabled(true);
        statusText.setText("Recording saved.");
    }

    private void resetRecording() {
        startRecordButton.setEnabled(true);
        stopRecordButton.setEnabled(false);
        saveButton.setEnabled(false);
        resetButton.setEnabled(false);
        sendButton.setEnabled(false);
        statusText.setText("Ready to record again.");
    }

    private void sendFileToServer() {
        new Thread(() -> {
            try {
                String url = "http://192.168.0.2:8000/upload";

                File file = new File(filePath);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.getName(), RequestBody.create(file, MEDIA_TYPE_MP3))
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("MainActivity", "Error: " + e.getMessage());
                        getActivity().runOnUiThread(() -> statusText.setText("Upload failed: " + e.getMessage()));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String responseBody = response.body().string();
                            try {
                                JSONObject jsonResponse = new JSONObject(responseBody);
                                final String message = jsonResponse.getString("message");
                                 MedicationRequestDataModel med = parseToFHIRMedicationRequest(message);

                                 WhisperViewModel whisperViewModel1 = new WhisperViewModel();
                              whisperViewModel1.fetchPatientData(med.getSubject());


                                getActivity().runOnUiThread(() -> {
                                    // Hier wird der String übergeben ....
                                    statusText.setText(message);


                                });
                            } catch (Exception e) {
                                Log.e("MainActivity", "JSON Parsing error: " + e.getMessage());
                                getActivity().runOnUiThread(() -> statusText.setText("Upload failed: JSON Parsing error"));
                            }
                        } else {
                            Log.e("MainActivity", "Server Error: " + response.message());
                            getActivity().runOnUiThread(() -> statusText.setText("Upload failed: Server error"));
                        }
                    }
                });

            } catch (Exception e) {
                Log.e("MainActivity", "Exception: ", e);
                getActivity().runOnUiThread(() -> statusText.setText("Upload failed: Exception"));
            }
        }).start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Toast.makeText(getActivity(), "Permission to record audio is required", Toast.LENGTH_SHORT).show();

            }
        } else if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startRecording();
            } else {
                Toast.makeText(getActivity(), "Permission to access storage is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public MedicationRequestDataModel parseToFHIRMedicationRequest(String speechToTextString){

        // Define the regex to match valid characters (letters and numbers)
        Log.d("Test","before testing: " + speechToTextString );
       // String regex = "[a-zA-Z0-9]+";
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();

        // Iterate through each character in the string
        for (char c : speechToTextString.toCharArray()) {

            if (Character.isLetterOrDigit(c)) {
                // Append valid characters to the current part
                currentPart.append(c);
            } else {
                if (currentPart.length() > 0) {
                    // Add the current part to the list if it's non-empty
                    parts.add(currentPart.toString());
                    currentPart.setLength(0);
                }
            }
        }
        // Add the last part if it's non-empty
        if (currentPart.length() > 0) {
            parts.add(currentPart.toString());
        }
        Log.d("Test"," " + parts.size());
        for(int i = 0; i < parts.size(); i++){

            if (parts.get(i) !=null) {
                Log.d("Test", parts.get(i));
            }else {
                Log.d("Test","Last part was: " + parts.get(i-1));
            }
        }

        // Ensure we have exactly 9 parts after parsing
        if (parts.size() != 9) {
            Log.d("Test","failed");
            throw new IllegalArgumentException("Input string does not contain the correct number of values.");
        }

        // Extract values based on their positions
        String identifiereMedID = parts.get(0);
        String identifiereMedIDGroup = parts.get(1);
        String aspCode = parts.get(2);
        String displayMedication = parts.get(3);
        String requester = parts.get(4);
        String subjectReference = parts.get(5);
        String patientInstruction = parts.get(6);
        String frequency = parts.get(7);
        String when = parts.get(8);

        // Create the DosageInstruction object
        MedicationRequestDataModel.DosageInstruction dosageInstruction = new MedicationRequestDataModel.DosageInstruction(patientInstruction, frequency, when);
        List<MedicationRequestDataModel.DosageInstruction> dosageInstructionList = new ArrayList<>();
        dosageInstructionList.add(dosageInstruction);


        // Create and return the MedicationRequestDataModel object

        MedicationRequestDataModel medicationRequestDataModel = new MedicationRequestDataModel("3", identifiereMedID, identifiereMedIDGroup, aspCode,
                displayMedication, requester, subjectReference, dosageInstructionList);

        Log.d(LogTag.WHISPER.getTag(), medicationRequestDataModel.toString());
        return  medicationRequestDataModel;
    }



  }
