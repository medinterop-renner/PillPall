package com.example.pillpal420.testingNotForRealUse;

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
            return patientInstruction + '\'' +
                    "Bitte so oft einnehmen: " + frequency + '\'' +
                    "am: " + when + '\'';
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
                .append('\'')
                .append("Bitte achten Sie bei der Einahme auf folgende Sachen: ");
        for (DosageInstructionsForMedicationRequestDataModelForFullPrescription dosageInstruction : dosageInstructions) {
            sb.append(dosageInstruction.toString()).append(", \n");
        }
        sb.delete(sb.length() - 2, sb.length()); // Remove the last comma and space

        return sb.toString();
    }
}