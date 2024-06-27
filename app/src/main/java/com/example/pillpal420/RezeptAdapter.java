package com.example.pillpal420;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillpal420.Backend.FullPrescriptionDataModel;
import com.example.pillpal420.Backend.MedicationRequestDataModelForFullPrescription;

import java.util.List;
public class RezeptAdapter extends RecyclerView.Adapter<RezeptAdapter.ViewHolder>{

    private List<FullPrescriptionDataModel> rezepte;

    public RezeptAdapter(List<FullPrescriptionDataModel> rezepte){
            this.rezepte = rezepte;
    }
    @NonNull
    @Override
    public RezeptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rezept_items, parent, false);
        return new RezeptAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos){
        FullPrescriptionDataModel rezept = rezepte.get(pos);

        holder.patientInfo.setText(rezept.getPatientDataModel().toString());
        holder.practitionerInfo.setText(rezept.getPractitionerDataModel().toString());


        StringBuilder medicationSB = new StringBuilder();
        for(MedicationRequestDataModelForFullPrescription medicationRequest : rezept.getMedicationRequestDataModelForFullPrescription()){

            medicationSB.append(medicationRequest.toString()).append("\n");
        }
        holder.medicationInfo.setText(medicationSB.toString());
    }

    @Override
    public int getItemCount(){return rezepte.size();}

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView patientInfo;
        TextView practitionerInfo;
        TextView medicationInfo;

        public ViewHolder (View itemView){

            super(itemView);
            patientInfo = itemView.findViewById(R.id.patientView);
            practitionerInfo = itemView.findViewById(R.id.practicionerView);
            medicationInfo = itemView.findViewById(R.id.medicationView);

        }
    }

}
