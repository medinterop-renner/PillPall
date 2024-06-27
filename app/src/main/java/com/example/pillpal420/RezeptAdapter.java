package com.example.pillpal420;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillpal420.Backend.FullPrescriptionDataModel;

import java.util.List;
public class RezeptAdapter extends RecyclerView.Adapter<RezeptAdapter.ViewHolder>{

    private List<FullPrescriptionDataModel> rezepte;

    public RezeptAdapter(List<FullPrescriptionDataModel> rezepte){


    }

}
