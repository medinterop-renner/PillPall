package com.example.pillpal420;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

public class EditRecView extends AppCompatActivity {

    private String picPath;
    private EditText editName;
    private EditText editExpiryDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_rec_view);

        ImageView imgView = findViewById(R.id.imageView);
        editName = findViewById(R.id.editName);
        editExpiryDate = findViewById(R.id.editExpiryDate);
        Button saveBtn = findViewById(R.id.saveBtn);


        Intent intent = getIntent();
        picPath = intent.getStringExtra("imagePath");
        String name = intent.getStringExtra("name");
        String date = intent.getStringExtra("date");

        Uri picUri = Uri.fromFile(new File(picPath));
        imgView.setImageURI(picUri);
        editName.setText(name);
        editExpiryDate.setText(date);

        saveBtn.setOnClickListener(v -> {

            Intent intent2 = new Intent();
            intent2.putExtra("picPath", picPath);
            intent2.putExtra("name", editName.getText().toString());
            intent2.putExtra("date", editExpiryDate.getText().toString());
            finish();
        });

    }
}