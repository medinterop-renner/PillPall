package com.example.pillpal420;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pillpal420.backend.dataModels.FullPrescriptionDataModel;
import com.example.pillpal420.backend.dataModels.MedicationRequestDataModelForFullPrescription;

import java.util.List;

public class RezeptAdapter extends RecyclerView.Adapter<RezeptAdapter.ViewHolder> {

    private List<FullPrescriptionDataModel> rezepte;

    public RezeptAdapter(List<FullPrescriptionDataModel> rezepte) {
        this.rezepte = rezepte;
    }

    /**
     * Inflated das layout für jedes Item in der RecyclerView und gibt eine neue ViewHolder instance zurück
     * <p>
     * Die Methode wird aufgerufen wenn die RecyclerView einen neuen {@link RezeptAdapter.ViewHolder} benötigt um ein neues Item darstellen zu können
     * Hierfür wird das "rezept_items.xml" layout verwendet und ein neuer ViewHolder erstellt
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return neue Instanz von {@link RezeptAdapter.ViewHolder} mit Referenz zur neu erstellten View
     */
    @NonNull
    @Override
    public RezeptAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rezept_items, parent, false);
        return new RezeptAdapter.ViewHolder(view);
    }

    /**
     * Stellt die Daten zum ViewHolder für die Position in der RecyclerView an die sie gehören
     * <p>
     * Diese Methode wird von der RecyclerView aufgerufen um die Daten richtig darstellen zu können
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *               item at the given position in the data set.
     * @param pos    The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        FullPrescriptionDataModel rezept = rezepte.get(pos);

        holder.patientInfo.setText(rezept.getPatientDataModel().toString());
        holder.practitionerInfo.setText(rezept.getPractitionerDataModel().toString());


        StringBuilder medicationSB = new StringBuilder();
        for (MedicationRequestDataModelForFullPrescription medicationRequest : rezept.getMedicationRequestDataModelForFullPrescription()) {

            medicationSB.append(medicationRequest.toString()).append("\n");
        }
        holder.medicationInfo.setText(medicationSB.toString());
    }

    /**
     * @return die Größe der List
     */
    @Override
    public int getItemCount() {
        return rezepte.size();
    }

    /**
     * Die ViewHolder Klasse beschreibt eine ItemView mit zugehörigem Platz in der RecyclerView
     * Wir verwenden den ViewHolder um das Rezept darstellen zu können --> mit Patienten Info, Arzt Info und Medikamenten Info
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView patientInfo;
        TextView practitionerInfo;
        TextView medicationInfo;

        /**
         * Konstruktor für die Initialisierung der TextViews für die RecyclerView
         *
         * @param itemView ist die View des Items in der RecyclerView
         */
        public ViewHolder(View itemView) {

            super(itemView);
            patientInfo = itemView.findViewById(R.id.patientView);
            practitionerInfo = itemView.findViewById(R.id.practicionerView);
            medicationInfo = itemView.findViewById(R.id.medicationView);

        }
    }

}
