package com.example.pillpal420.backend.dataModels;

import androidx.annotation.NonNull;

import java.util.List;

/**
 * Diese Klasse wird verwendet um einzele MedicationRequestDataModels an den FHIR R5 Server zu schicken.
 * Nachdem sie zu JSON Objects geparsed wurden.
 */
public class MedicationRequestDataModel {
    private String id;
    private String identifiereMedID;
    private String identifiereMedIDGroup;
    private String aspCode;
    private String displayMedication;
    private String requester;
    private String subjectReference;
    private List<DosageInstruction> dosageInstructions;

    /**
     * Constructor für die MedicationRequestDataModels.
     *
     * @param id                    ID that holds the relative Server Path.
     * @param identifiereMedID      Unique identifier for each medication in a prescription in austria.
     * @param identifiereMedIDGroup Unique identifier for a prescription in austria.
     * @param aspCode               Code by BASGK for identification of drugs.
     * @param displayMedication     Holds the name of the drug.
     * @param requester             Doctor that made the prescription
     * @param subjectReference      Subject that got the prescription
     * @param dosageInstructions    List of DosageInstructions for the patient.
     */
    public MedicationRequestDataModel(String id, String identifiereMedID, String identifiereMedIDGroup, String aspCode,
                                      String displayMedication, String requester, String subjectReference,
                                      List<DosageInstruction> dosageInstructions) {
        this.id = id;
        this.identifiereMedID = identifiereMedID;
        this.identifiereMedIDGroup = identifiereMedIDGroup;
        this.aspCode = aspCode;
        this.displayMedication = displayMedication;
        this.requester = requester;
        this.subjectReference = subjectReference;
        this.dosageInstructions = dosageInstructions;
    }

    public String getId() {
        return id;
    }

    public String getIdentifiereMedID() {
        return identifiereMedID;
    }

    public String getIdentifiereMedIDGroup() {
        return identifiereMedIDGroup;
    }

    public String getAspCode() {
        return aspCode;
    }

    public String getDisplayMedication() {
        return displayMedication;
    }

    public String getRequester() {
        return requester;
    }

    public String getSubject() {
        return subjectReference;
    }

    public List<DosageInstruction> getDosageInstructions() {
        return dosageInstructions;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubjectReference(String subjectReference) {
        this.subjectReference = subjectReference;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    /**
     * Methode die verwendet wird um MedicationRequestDataModels besser darzustellen in Code und UI.
     *
     * @return String representing the MedicationRequestDataModel.
     */
    @NonNull
    @Override
    public String toString() {
        StringBuilder dosageInstructionsStr = new StringBuilder();
        for (DosageInstruction instruction : dosageInstructions) {
            dosageInstructionsStr.append(instruction.toString()).append("\n");
        }
        return "MedicationRequestDataModel: " +
                " id= " + id +
                " identifiereMedID= " + identifiereMedID +
                " identifiereMedIDGroup= " + identifiereMedIDGroup +
                " aspCode= " + aspCode +
                " displayMedication= " + displayMedication +
                " requester= " + requester +
                " subjectReference= " + subjectReference +
                " dosageInstructions= " + dosageInstructionsStr.toString();
    }

    /**
     * Innere Klasse um FHIR R5 HL7 Austria eMedikamention medicationRequest besser darstellen zu können.
     */
    public static class DosageInstruction {
        private String patientInstruction;
        private String frequency;
        private String when;

        /**
         * Constructor für die Innere Klasse um FHIR R5 HL7 Austria eMedikamention medicationRequest besser darstellen zu können.
         *
         * @param patientInstruction String that contains patient Instructions
         * @param frequency          String that contains the frequency
         * @param when               String that contains the timing
         */
        public DosageInstruction(String patientInstruction, String frequency, String when) {
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

        /**
         * Reformatiert das  MedicationRequestDataModel um es besser für Logging und UI zu verwenden.
         *
         * @return A String representing the MedicationRequestDataModel.
         */
        @NonNull
        @Override
        public String toString() {
            return "DosageInstruction{" +
                    "patientInstruction='" + patientInstruction + '\'' +
                    ", frequency='" + frequency + '\'' +
                    ", when='" + when + '\'' +
                    '}';
        }
    }
}