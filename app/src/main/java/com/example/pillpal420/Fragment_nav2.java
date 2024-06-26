package com.example.pillpal420;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
//Scan Fragment
public class Fragment_nav2 extends Fragment {
    private String[] patient;
    //Familyname, Givenname, Prefix/Suffix
    private String[] name;
    //Straßenname, Hausnummer, Stadt, Staat, Postleitzahl
    private String[] address;
    //male, female, other, unknown
    private String gender;
    //YYYY:MM:DD
    private String birthdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }
}