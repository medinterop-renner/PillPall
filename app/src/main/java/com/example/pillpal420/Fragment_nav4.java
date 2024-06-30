package com.example.pillpal420;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Fragment_nav4 extends Fragment {
    private static final String EMAIL_STRING = "support@pillpal.at";

    /**
     * Funktion:
     * 1. Initialisierung des Buttons
     * 2. setOnClickListener der die Methode {@link #openMail()} öffnet
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return helpView oder null
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View helpView = inflater.inflate(R.layout.fragment_help, container, false);
        Button eMailBtn = helpView.findViewById(R.id.eMailBtn);
        eMailBtn.setOnClickListener(v -> openMail());
        return helpView;
    }

    /**
     * Hier öffnen wir den E-Mail Client und fügen unsere E-Mail-Adresse automatisch als Empfänger ein
     * <p>
     * Funktion:
     * 1. Erstellen eines Intents mit {@link Intent#ACTION_SENDTO}
     * 2. putExtra um unsere E-Mail Adresse automatisch in das Empfänger Feld einzufügen
     * 3. Starten einer Activity die es dem User/der Userin erlaubt den Mail-Provider auszuwählen
     */
    private void openMail() {
        Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", EMAIL_STRING, null));
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
        startActivity(Intent.createChooser(mailIntent, "Anbieter wählen:"));
    }
}