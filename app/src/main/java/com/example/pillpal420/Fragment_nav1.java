package com.example.pillpal420;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.pillpal420.backend.aiInterface.Vision;
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

public class Fragment_nav1 extends Fragment {
    private Uri picUri;
    private ImageView scanImgView;
    private TextView scanTextView;
    private ActivityResultLauncher<Uri> scanPicLauncher;
    public static String finalString;

    /**
     * Inflated das "fragment_home.xml" layout
     * <p>
     * Funktion:
     * 1. Initialisierung des Buttons sowie einer ImageView und einer TextView
     * 2. Registrierung des ActivityResultLaunchers um ein Bild aufzunehmen
     * 3. Setzen eines onClickListeners für den Button der die Methode openCam() aufruft
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return scanView oder null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View scanView = inflater.inflate(R.layout.fragment_home, container, false);
        Button scanBtn = scanView.findViewById(R.id.scanBtn);
        scanImgView = scanView.findViewById(R.id.scanImgView);
        scanTextView = scanView.findViewById(R.id.scanTextView);
        scanPicLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) {
                processImg(picUri);
            } else {
                Toast.makeText(getActivity(), R.string.pic_nottaken, Toast.LENGTH_SHORT).show();
            }

        });

        scanBtn.setOnClickListener(v -> openCam());


        return scanView;
    }

    /**
     * Methode um die Kamera zu öffnen und ein Bild zu machen
     * <p>
     * Funktion:
     * 1. wir versuchen mithilfe der {@link #createImageFile()} Methode ein File zu erstellen
     * 2. tritt eine IOException auf wird diese im Log dargestellt
     * 3. wurde das File erfolgreich erstell --> ist nicht null, setzen wir eine Uri für das Bild
     * 4. dann übergeben wir diese Uri dem {@link #scanPicLauncher} um die Kamera zu öffnen und ein Bild im erstellten File speichern zu können
     */
    private void openCam() {
        File scanPicFile = null;
        try {
            scanPicFile = createImageFile();
        } catch (IOException e) {
            Log.e(LogTag.MLKIT.getTag(), "File create error", e);
        }
        if (scanPicFile != null) {
            picUri = FileProvider.getUriForFile(getActivity(), "com.example.pillpal420.fileprovider", scanPicFile);
            scanPicLauncher.launch(picUri);
        }
    }

    /**
     * Hier erstellen wir ein temporäres File um dann in {@link #openCam()} ein Bild darin speichern zu können
     * Funktion:
     * 1. Erstellung der Strings, "timeStamp" für ein SimpleDateFormat, scanFileName um den Namen des Files zu erstellen
     * 2. das File "storageDir" erhält ddas ExternalFilesDirectory für Bilder
     *
     * @return temporäres File mit dem Namen, dem Suffix und dem ExternalFilesDirectory
     * @throws IOException wird in {@link #openCam()} behandelt
     */
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String scanFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(scanFileName, ".jgp", storageDir);
    }

    /**
     * Hier verwenden wir das ML Kit Text Recognition API um den Text aus einem Bild, für die Weiterverarbeitung, zu extrahieren
     * <p>
     * Funktion:
     * 1. Erstellung eines InputImage mit dem FilePath der Uri des Bildes
     * 2. Initialisierung des TextRecognizer
     * 3. Verarbeitung des Bildes mithilfe des TextRecognizer
     * 4. onSuccessListener: der erkannte Text wird mithilfe der {@link #displayText(Text)} angezeigt
     * 5. zusätzlich wird die Methode {@link #getStringFromScan(Text)} aufgerufen
     * 6. onFailureListener: es wird ein Eintrag im Log erstellt und dem User/der Userin ein Toast gezeigt, dass kein Text erkannt werden konnte
     * 7. catch IOException die auftreten könnte
     *
     * @param uri ist die Uri des Bildes woraus wir den Text extrahieren wollen
     */
    private void processImg(Uri uri) {
        try {
            InputImage img = InputImage.fromFilePath(getContext(), uri);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            recognizer.process(img).addOnSuccessListener(visionText -> {
                displayText(visionText);
                getStringFromScan(visionText);
                scanImgView.setImageURI(uri);
            }).addOnFailureListener(e -> {
                Log.e(LogTag.MLKIT.getTag(), "Text nicht erkannt", e);
                Toast.makeText(getActivity(), R.string.text_notfound, Toast.LENGTH_SHORT).show();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zeigt den extrahierten Text in einer TextView
     * <p>
     * Die Textblöcke werden in einen StringBuilder gespeichert und anschließend in einen String gespeichert
     *
     * @param visionText ist das Text Objekt das in {@link #processImg(Uri)} erstellt wurde
     */
    private void displayText(Text visionText) {
        StringBuilder resultText = new StringBuilder();
        for (Text.TextBlock block : visionText.getTextBlocks()) {
            resultText.append(block.getText()).append("\n");
        }
        Log.d(LogTag.MLKIT.getTag(), "Before Processing" + resultText.toString());
        scanTextView.setText(resultText.toString());
    }

    /**
     * Wandelt die Textblöcke in einen String um, um weiterverarbeitet werden zu können
     * <p>
     * Die Textblöcke werden in einen StringBuilder gespeichert und anschließend in einen String gespeichert
     * <p>
     * Zusätzlich wird ein Vision Objekt initialisiert das den finalen String übergeben bekommt
     *
     * @param visionText ist das Text Objekt das in {@link #processImg(Uri)} erstellt wurde
     */

    private void getStringFromScan(Text visionText) {
        StringBuilder text = new StringBuilder();
        for (Text.TextBlock block : visionText.getTextBlocks()) {
            text.append(block.getText());
        }
        finalString = text.toString();
        Vision vision = new Vision(finalString);
    }
}