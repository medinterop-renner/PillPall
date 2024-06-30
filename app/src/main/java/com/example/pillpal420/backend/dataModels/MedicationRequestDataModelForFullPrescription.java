package com.example.pillpal420.backend.dataModels;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Klasse die HL7 AUSTRIA FHIR R5 eMedikation  MedicationRequest resource darstellt.
 */
public class MedicationRequestDataModelForFullPrescription {
    private String displayMedication;
    private List<DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions;

    /**
     * Konstrukor für die MedicationRequestDataModelForFullPrescription Klasse.
     *
     * @param displayMedication  Holds the name of the medication.
     * @param dosageInstructions dosage instructions for the patient.
     */
    public MedicationRequestDataModelForFullPrescription(String displayMedication, List<DosageInstructionsForMedicationRequestDataModelForFullPrescription> dosageInstructions) {
        this.displayMedication = displayMedication;
        this.dosageInstructions = dosageInstructions;
    }

    /**
     * Klasse für ein besseres Datamodeling um FHIR R5 MedicationRequest Resourcen darzustellen.
     */
    public static class DosageInstructionsForMedicationRequestDataModelForFullPrescription {
        private String patientInstruction;
        private String frequency;
        private String when;

        /**
         * Konstruktor von der DosageInstructionsForMedicationRequestDataModelForFullPrescription Klasse.
         *
         * @param patientInstruction Instructions for the patient
         * @param frequency          frequency of intake of drugs
         * @param when               timing of intake
         */
        public DosageInstructionsForMedicationRequestDataModelForFullPrescription(String patientInstruction, String frequency, String when) {
            this.patientInstruction = patientInstruction;
            this.frequency = frequency;
            this.when = when;
        }

        /**
         * Formatiert die DosageInstructionsForMedicationRequestDataModelForFullPrescription Klasse für eine besser UI und Logging UX.
         *
         * @return A String representing the values of the DosageInstructionsForMedicationRequestDataModelForFullPrescription class.
         */
        @NonNull
        @Override
        public String toString() {
            String timing;
            if (when.equals("EVE")) {
                timing = "Abend";
            } else {
                timing = "Morgen";
            }
            return patientInstruction + ".\nEinnahmeplan: Medikament " + frequency + "." + timing + " einnehmen." + "\n";
        }
    }

    /**
     * Formatiert die MedicationRequestDataModelForFullPresciption für eine bessere logging und UI UX
     *
     * @return A String that represents the MedicationRequestDataModelForFullPrescription.
     */
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