package com.example.pillpal420.documentation;

/**
 * Enums für LogCat übersicht. Debugging.
 */
public enum LogTag {

    FULL_PRESCRIPTION("FullPrescriptionDataModel"),
    MEDICATION_REQUEST("MedicationRequestDataModel"),
    PRACTITIONER("PractitionerDataModel"),
    PATIENT("PatientDataModel"),
    ROOM_DB("DataBanking"),
    LOG_IN("LogIn"),
    VISION("Vision"),
    MLKIT("MLKit"),
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
