package com.example.pillpal420.backend.dataModels;

import androidx.annotation.NonNull;
import java.util.List;

public class MedicationRequestDataModelForFullPrescription {

    private String displayMedication;
    private List<DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions;

    public MedicationRequestDataModelForFullPrescription(String displayMedication, List<DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions) {
        this.displayMedication = displayMedication;
        this.dosageInstructions = dosageInstructions;
    }

    public static class DosageInstructionsForMedicationRequestDataModelForFullPrescription {
        private String patientInstruction;
        private String frequency;
        private String when;

        public DosageInstructionsForMedicationRequestDataModelForFullPrescription(String patientInstruction, String frequency, String when) {
            this.patientInstruction = patientInstruction;
            this.frequency = frequency;
            this.when = when;
        }

        public String getPatientInstruction() {
            return patientInstruction;
        }

        public String getFrequency() {
            return frequency;
        }

        public String getWhen() {
            return when;
        }

        @NonNull
        @Override
        public String toString() {
            String timing;
            if (when.equals("EVE")){
                timing = "Abend";
            }else{
                timing = "Morgen";
            }
            return patientInstruction + ".\nEinnahmeplan: Medikament " + frequency + "." + timing + " einnehmen."+"\n";

        }
    }

    public String getDisplayMedication() {
        return displayMedication;
    }

    public List<DosageInstructionsForMedicationRequestDataModelForFullPrescription> getDosageInstructions() {
        return dosageInstructions;
    }
    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Medikament: ")
                .append(displayMedication)
                .append("\nBesondere Hinweise: ");
        for (DosageInstructionsForMedicationRequestDataModelForFullPrescription dosageInstruction : dosageInstructions) {
            sb.append(dosageInstruction.toString());
        }
        return sb.toString();
    }
}