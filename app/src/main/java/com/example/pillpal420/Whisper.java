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

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Whisper extends Fragment {

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
}