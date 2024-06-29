package com.example.pillpal420.backend.dataModels;

import androidx.annotation.NonNull;
import java.util.List;

public class MedicationRequestDataModel {



    private String id;
    private String identifiereMedID;
    private String identifiereMedIDGroup;
    private String aspCode;
    private String displayMedication;
    private String requester;
    private String subjectReference;
    private List<DosageInstruction> dosageInstructions;

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

    @Override
    public String toString() {
        StringBuilder dosageInstructionsStr = new StringBuilder();
        for (DosageInstruction instruction : dosageInstructions) {
            dosageInstructionsStr.append(instruction.toString()).append("\n");
        }

        return "MedicationRequestDataModel{" +
                "id='" + id + '\'' +
                ", identifiereMedID='" + identifiereMedID + '\'' +
                ", identifiereMedIDGroup='" + identifiereMedIDGroup + '\'' +
                ", aspCode='" + aspCode + '\'' +
                ", displayMedication='" + displayMedication + '\'' +
                ", requester='" + requester + '\'' +
                ", subjectReference='" + subjectReference + '\'' +
                ", dosageInstructions=" + dosageInstructionsStr.toString() +
                '}';
    }

    public static class DosageInstruction {
        private String patientInstruction;
        private String frequency;
        private String when;

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