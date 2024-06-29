package com.example.pillpal420.documentation;

public enum LogTag {

    FULL_PRESCRIPTION("FullPrescriptionDataModel"),
    MEDICATION_REQUEST("MedicationRequestDataModel"),
    PRACTITIONER("PractitionerDataModel"),
    PATIENT("PatientDataModel"),
    ROOM_DB("DataBanking"),

    LOG_IN("LogIn"),

    VISION("Vision"),
    WHISPER("Whisper"),
    DOSAGE_INSTRUCTION("DosageInstructions");


    private final String tag;

    LogTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }
}
