package com.example.pillpal420;


import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModel;
import com.example.pillpal420.backend.dataModels.PatientDataModel;
import com.example.pillpal420.backend.viewModels.MedicationRequestViewModel;
import com.example.pillpal420.backend.viewModels.WhisperViewModel;
import com.example.pillpal420.documentation.LogTag;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    String serverAddress = "192.168.0.2:8000";
    private static MedicationRequestDataModel medicationRequestDataModel;
    private WhisperViewModel whisperViewModel;
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

    /**
     * Funktion:
     * 1. Initialisierung der Buttons und TextView sowie des filePaths um die Aufnahmen zu speichern
     * 2. setOnClickListener für alle Buttons um Aufzeichnung zu starten, stoppen, speichern, erneuern und schicken
     * 3. Observer für LiveData, der auf Änderungen der Patientendaten reagiert
     * --> bei Änderung wird ein Log geschrieben und ein medicationRequest durchgeführt
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return whispView oder null
     */
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
        startRecordButton.setOnClickListener(v -> startRecording());
        stopRecordButton.setOnClickListener(v -> stopRecording());
        saveButton.setOnClickListener(v -> saveRecording());
        resetButton.setOnClickListener(v -> resetRecording());
        sendButton.setOnClickListener(v -> sendFileToServer());
        whisperViewModel = new ViewModelProvider(this).get(WhisperViewModel.class);
        whisperViewModel.getPatientLiveData().observe(getViewLifecycleOwner(), new Observer<PatientDataModel>() {
            @Override
            public void onChanged(PatientDataModel patientDataModel) {
                Log.d("Testing", patientDataModel.toString());
                postMedReqtoServer(patientDataModel);
            }
        });
        return whispView;
    }

    /**
     * Hier wird die Tonaufnahme gestartet
     * <p>
     * Funktion:
     * 1. Initialisierung einer neuen MediaRecorder Instanz
     * 2. Handy-Mikrofon wird als Aufnahmequelle gesetzt
     * 3. Output-Format wird auf MPEG-4 gesetzt
     * 4. Audio-Encoder wird auf AAC gesetzt
     * 5. Pfad des Output-Files wird gesetzt
     * 6. Der Recorder wird vorbereitet und die Aufnahme gestarter
     * 7. Aktualisieren der UI Elemente
     */
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

    /**
     * Stoppt die Aufnahme
     * <p>
     * Funktion:
     * 1. Stoppt die MediaRecorder-Aufnahme
     * 2. Beendet den MediaRecorder
     * 3. setzt die Instanz des MediaRecorders zurück
     * 4. isRecording wird auf false gesetzt --> Aufnahme ist gestoppt
     * 5. Aktualisieren der UI Elemente
     */
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        isRecording = false;
        stopRecordButton.setEnabled(false);
        saveButton.setEnabled(true);
        statusText.setText("Recording stopped.");
    }

    /**
     * Hier wird die UI Aktualisiert um anzuzeigen dass die Aufnahme gespeichert wurde
     */
    private void saveRecording() {
        saveButton.setEnabled(false);
        resetButton.setEnabled(true);
        sendButton.setEnabled(true);
        statusText.setText("Recording saved.");
    }

    /**
     * Hier wird die UI aktualisiert um anzuzeigen dass eine neue Aufnahme möglich ist
     */
    private void resetRecording() {
        startRecordButton.setEnabled(true);
        stopRecordButton.setEnabled(false);
        saveButton.setEnabled(false);
        resetButton.setEnabled(false);
        sendButton.setEnabled(false);
        statusText.setText("Ready to record again.");
    }

    /**
     * Hier wird das die Aufnahme an den Server geschickt
     * <p>
     * Es wird ein neuer Thread erstellt um die Auhnahme an die Server URL zu schicken. Es wird eine multipart HTTP Request mit der Aufnahme erstellt
     * und gesendet.
     * <p>
     * Funktion:
     * 1. Erstellung eines Files für die Aufnahme
     * 2. Erstellen eines MultipartBody mit der Aufnahme
     * 3. Erstellen einer HTTP POST request
     * 4. Senden der Request über OKHttpClient
     * --> ist diese erfolgreich wird die UI mit den erhaltenen Patientendaten gefüllt
     * --> ist diese nicht erfolgreich wird ein Log und ein Toast erstellt der auf den Fehler hinweist
     */
    private void sendFileToServer() {
        new Thread(() -> {
            try {
                String url = "http://" + serverAddress + "/upload";
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
                                medicationRequestDataModel = parseToFHIRMedicationRequest(message);
                                getActivity().runOnUiThread(() -> {
                                    whisperViewModel.fetchPatientData(medicationRequestDataModel.getSubject());
                                    statusText.setText("Fetched patient data for subject: " + medicationRequestDataModel.getSubject());
                                });
                            } catch (Exception e) {
                                Log.e("MainActivity", "JSON Parsing error: " + e.getMessage());
                                getActivity().runOnUiThread(() -> statusText.setText(R.string.jsonparsingerror));
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

    /**
     * Hier wird der speech-to-text String an ein FHIR-konformes MedicationRequestDataModel übergeben
     * <p>
     * Funktion:
     * 1. Teilt den speechToTextString auf um ihn weiterverarbeiten zu können
     * 2. Überprüfung ob die List die vorgeschriebene Größe hat
     * 3. Extrahieren und Verarbeiten der einzelnen Teile um ein MedicationRequestDataModel zu bekommen
     * - Erstellt ein DosageInstruction Objekt und fügt es der Liste hinzu
     * - Erstellt MedicationRequestDataModel und fügt die einzelnen Teile und die DosageINstruction hinzu
     * 4. Log für das erstellte MedicationRequestDataModel
     *
     * @param speechToTextString String der von der Speech-To-Text Verarbeitung entsteht
     * @return MedicationRequestDataModel
     */
    public MedicationRequestDataModel parseToFHIRMedicationRequest(String speechToTextString) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        for (char c : speechToTextString.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                currentPart.append(c);
            } else {
                if (currentPart.length() > 0) {
                    parts.add(currentPart.toString());
                    currentPart.setLength(0);
                }
            }
        }
        if (currentPart.length() > 0) {
            parts.add(currentPart.toString());
        }
        if (parts.size() != 9) {
            Log.d("Test", "failed");
            throw new IllegalArgumentException("Input string does not contain the correct number of values.");
        }
        String identifiereMedID = parts.get(0);
        String identifiereMedIDGroup = parts.get(1);
        String aspCode = parts.get(2);
        String displayMedication = parts.get(3);
        String requester = parts.get(4).replaceAll("[^0-9]", "");
        String subjectReference = parts.get(5).replaceAll("[^a-zA-Z]", "");
        String patientInstruction = parts.get(6);
        String frequency = parts.get(7);
        frequency = "2";
        Log.d(LogTag.WHISPER.getTag(), frequency);
        if (parts.get(8).equalsIgnoreCase("morning")) {
            parts.set(8, "MORN");
        } else if (parts.get(8).equalsIgnoreCase("evening")) {
            parts.set(8, "EVE");
        }
        String when = parts.get(8);
        MedicationRequestDataModel.DosageInstruction dosageInstruction = new MedicationRequestDataModel.DosageInstruction(patientInstruction, frequency, when);
        List<MedicationRequestDataModel.DosageInstruction> dosageInstructionList = new ArrayList<>();
        dosageInstructionList.add(dosageInstruction);
        MedicationRequestDataModel medicationRequestDataModel = new MedicationRequestDataModel("3", identifiereMedID, identifiereMedIDGroup, aspCode, displayMedication, requester, subjectReference, dosageInstructionList);
        Log.d(LogTag.WHISPER.getTag(), medicationRequestDataModel.toString());
        return medicationRequestDataModel;
    }

    /**
     * Schickt die MedicationRequest zum Server
     * <p>
     * Funktion:
     * 1. Setzen der SubjectReference als Patienten ID
     * 2. Erstellen einer Instanz des MedicationRequestViewModel
     * 3. Schickt die MedicationRequest und kontrolliert das Ergebnis
     *
     * @param patientDataModel ist das Patienten Modell das die ID beinhaltet
     */
    public void postMedReqtoServer(PatientDataModel patientDataModel) {
        medicationRequestDataModel.setSubjectReference(patientDataModel.getId());
        Log.d(LogTag.WHISPER.getTag(), "shorty before posting to server: " + medicationRequestDataModel.toString());
        MedicationRequestViewModel viewModel = new ViewModelProvider(this).get(MedicationRequestViewModel.class);
        viewModel.postMedicationRequest(medicationRequestDataModel).observe(this, new Observer<MedicationRequestDataModel>() {
            @Override
            public void onChanged(MedicationRequestDataModel result) {
                if (result != null) {
                    Log.d("MedicationRequest", "MedicationRequest posted successfully: " + result.toString());
                    // Here kann man noch im scan dings das displayn
                    statusText.setText(medicationRequestDataModel.toString());
                } else {
                    Log.d("MedicationRequest", "Failed to post MedicationRequest");
                }
            }
        });
    }
}