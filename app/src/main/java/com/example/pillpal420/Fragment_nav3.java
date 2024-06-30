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
import android.text.Editable;
import android.text.TextWatcher;
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

    /**
     * Hier wird das "fragment_inventur.xml" layout inflated
     * <p>
     * Funktion:
     * 1. Button und LinearLayout werden initialisiert
     * 2. Ein ActivityResultLauncher wird erstellt um ein Bild machen zu können
     * --> wird ein Bild erfolgreich aufgenommen wird {@link #addPicTags(Uri)} aufgerufen um dem Bild Tags hinzufügen zu können,
     * zusätzlich wird {@link #savePics()} aufgerufen um das Bild in der View zu speichern
     * --> wird kein Bild aufgenommen wird ein Toast erstellt der dem User/der Userin mitteilt, dass ein Fehler aufgetreten ist
     * 3. ein setOnClickListener wird erstellt der die {@link #openCam()} Methode aufruft
     * 4. {@link #loadPics()} wird aufgerufen um die vorhandenen Bilder zu laden
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return invView oder null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View invView = inflater.inflate(R.layout.fragment_inventur, container, false);
        Button invPicBtn = invView.findViewById(R.id.invPicBtn);
        invLinLayout = invView.findViewById(R.id.invLinLayout);
        invPicLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                addPicTags(picUri);
                savePics();
            } else {
                Toast.makeText(getActivity(), R.string.pic_error, Toast.LENGTH_SHORT).show();
            }
        });
        invPicBtn.setOnClickListener(v -> openCam());
        loadPics();
        return invView;
    }

    /**
     * Hier wird die Kamera geöffnet um ein Bild zu machen
     * <p>
     * Funktion:
     * 1. die Methode {@link #savePics()} wird aufgerufen um die bestehenden Bilder zu speichern
     * 2. Es wird ein File für die Speicherung des Bilds mithilfe der {@link #createPicFile()} Methode erstellt
     * 3. Entsteht eine IOException wird diese im Log ausgegebn
     * 4. Die Kamera wird geöffnet und das gemachte Bild wird im erstellten File gespeichert
     */

    private void openCam() {
        savePics();
        File picFile = null;
        try {
            picFile = createPicFile();
        } catch (IOException e) {
            Log.e(invTAG, "File error", e);
        }
        if (picFile != null) {
            picUri = FileProvider.getUriForFile(getActivity(), "com.example.pillpal420.fileprovider", picFile);
            invPicLauncher.launch(picUri);
        }
    }

    /**
     * Hier erstellen wir ein temporäres File um dann in {@link #openCam()} ein Bild darin speichern zu können
     * <p>
     * Funktion:
     * 1. Erstellung der Strings, "timeStamp" für ein SimpleDateFormat, scanFileName um den Namen des Files zu erstellen
     * 2. das File "storageDir" erhält das ExternalFilesDirectory für Bilder
     *
     * @return temporäres File mit dem Namen, dem Suffix und dem ExternalFilesDirectory
     * @throws IOException wird in {@link #openCam()} behandelt
     */

    private File createPicFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String picFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(picFileName, ".jpg", storageDir);
    }

    /**
     * Hinzufügen von einer neuen View mit Bild und Tags zum layout
     * <p>
     * Funktion:
     * 1. Initialisierung der ImageView, 2 EditText und eines Buttons
     * 2. Hinzufügen der Uri zur ImageView und taggen der ImageView mit dem Uri String
     * 3. onClickListener für den deleteBtn um Bild+Tags vom LinearLayout löschen zu können + erneutes speichern durch {@link #savePics()}
     * 4. TextWatcher hinzufügen um die Texte in den EditText durch {@link #savePics()} speichern zu können, wenn sie verändert werden
     * 5. Hinzufügen der View zum Layout
     *
     * @param picUri Uri des Bildes was zur View hinzugefügt werden soll
     */
    private void addPicTags(Uri picUri) {
        View addTagView = getLayoutInflater().inflate(R.layout.inventory_item, invLinLayout, false);
        ImageView imgView = addTagView.findViewById(R.id.imgView);
        EditText editName = addTagView.findViewById(R.id.editName);
        EditText editExpiryDate = addTagView.findViewById(R.id.editExpiryDate);
        Button deleteBtn = addTagView.findViewById(R.id.deleteBtn);
        imgView.setImageURI(picUri);
        imgView.setTag(picUri.toString());
        deleteBtn.setOnClickListener(v -> {
            invLinLayout.removeView(addTagView);
            savePics();
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                savePics();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        editName.addTextChangedListener(textWatcher);
        editExpiryDate.addTextChangedListener(textWatcher);
        invLinLayout.addView(addTagView);
    }

    /**
     * Hier speichern wir die Bilder und zugehörigen Tags in einer SharedPreference als JSON string
     * <p>
     * Funktion:
     * 1. Erstellung einer SharedPreference um die Daten speichern zu können
     * 2. Erstellung eines SharedPreferences.Editor um Änderungen vornehmen zu können
     * 3. durch eine for-Schleife wird:
     * - Die ImageView sowie die Tags für jede View geholt
     * - Uri der ImageView wird geholt
     * - Wandelt die EditText in Strings
     * - speichert jedes JSON Objekt im JSON Array
     * - speichert das JSON Array als String in der Shared Preference
     */
    private void savePics() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        JSONArray jsonArr = new JSONArray();
        for (int i = 0; i < invLinLayout.getChildCount(); i++) {
            View view = invLinLayout.getChildAt(i);
            ImageView imgView = view.findViewById(R.id.imgView);
            EditText editName = view.findViewById(R.id.editName);
            EditText editExpiryDate = view.findViewById(R.id.editExpiryDate);
            String imgUriString = (String) imgView.getTag();
            Uri picUri = Uri.parse(imgUriString);
            String editName22 = editName.getText().toString();
            String editExpiryDate22 = editExpiryDate.getText().toString();
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("picUri", picUri.toString());
                jsonObj.put("editName", editName22);
                jsonObj.put("editExpiryDate", editExpiryDate22);
                jsonArr.put(jsonObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        editor.putString(KEY_PICS, jsonArr.toString());
        editor.apply();
    }

    /**
     * Lädt die gespeicherten Daten aus der SharedPreference und zeigt sie im Layout an
     * <p>
     * Funktion:
     * 1. holt die SharedPreferences Instanz um auf die gespeicherten Daten zugreifen zu können
     * 2. holt den JSON string der die Referenz zu den Bildern und Tags darstellt
     * 3. Überprüft ob der JSON String leer ist oder nicht
     * --> wenn JSON String nicht leer ist:
     * - bekommt das JSON Array durch den JSON String
     * - extrahiert die Uri des Bildes für jedes JSON Objekt
     * - durch {@link #addPicTags(Uri)} werden die Bilder und Tags zum Layout hinzugefügt
     * - Zeigt den extrahierten Text in den EditText an
     * 4. Verarbeitung aller JSONExceptions
     */
    private void loadPics() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonPics = sharedPref.getString(KEY_PICS, "");
        if (!jsonPics.isEmpty()) {
            try {
                JSONArray jsonArr = new JSONArray(jsonPics);
                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonObj = jsonArr.getJSONObject(i);
                    String imgUriString = jsonObj.getString("picUri");
                    String editName = jsonObj.getString("editName");
                    String editExpiryDate = jsonObj.getString("editExpiryDate");
                    Uri picUri = Uri.parse(imgUriString);
                    addPicTags(picUri);
                    View view = invLinLayout.getChildAt(invLinLayout.getChildCount() - 1);
                    EditText editName1 = view.findViewById(R.id.editName);
                    EditText editExpiryDate1 = view.findViewById(R.id.editExpiryDate);
                    editName1.setText(editName);
                    editExpiryDate1.setText(editExpiryDate);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}