package com.example.pillpal420;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class Fragment_nav5 extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View f5View = inflater.inflate(R.layout.fragment_nav5, container, false);
        //Knopf
        Button botBtn = f5View.findViewById(R.id.f5Button);
        //Output
        TextView botTextView = f5View.findViewById(R.id.f5TextView);
        //Input
        EditText botEditText = f5View.findViewById(R.id.f5editText);

        return f5View;
    }

}
