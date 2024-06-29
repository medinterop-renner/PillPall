package com.example.pillpal420;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//Hilfe Fragment
public class Fragment_nav4 extends Fragment {

    private static final String EMAIL_STRING = "support@pillpal.at";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View helpView = inflater.inflate(R.layout.fragment_help, container, false);


        Button eMailBtn = helpView.findViewById(R.id.eMailBtn);

        eMailBtn.setOnClickListener(v -> openMail());

        return helpView;
    }

    private void openMail(){
        Intent mailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", EMAIL_STRING, null));
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
        startActivity(Intent.createChooser(mailIntent, "Anbieter wählen:"));
    }
}