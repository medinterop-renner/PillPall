package com.example.pillpal420.backend.dataModels;

import androidx.annotation.NonNull;

import java.util.List;


/**
 * Diese Klasse stellt ein komplettes Rezept da.
 * Sie enthält Informationen über die PatientInnen, die ÄrztInnen und die verschiedenen verschriebenen Medikamente.
 */
public class FullPrescriptionDataModel {

    private PatientDataModel patientDataModel;
    private PractitionerDataModel practitionerDataModel;
    private List<MedicationRequestDataModelForFullPrescription> medicationRequestDataModelForFullPrescription;
    /**
     * Der Constructior der Klasse. Er wird verwendet um ein vollständiges FullPrescriptionDataModel zu erstellen.
     *
     * @param patientDataModel                             the data model representing the patient.
     * @param practitionerDataModel                        the data model representing the practitioner.
     * @param medicationRequestDataModelForFullPrescription the list of medication requests associated with this prescription.
     */
    public FullPrescriptionDataModel(PatientDataModel patientDataModel, PractitionerDataModel practitionerDataModel, List<MedicationRequestDataModelForFullPrescription> medicationRequestDataModelForFullPrescription) {
        this.patientDataModel = patientDataModel;
        this.practitionerDataModel = practitionerDataModel;
        this.medicationRequestDataModelForFullPrescription = medicationRequestDataModelForFullPrescription;
    }

    public PatientDataModel getPatientDataModel() {
        return patientDataModel;
    }

    public PractitionerDataModel getPractitionerDataModel() {
        return practitionerDataModel;
    }

    public List<MedicationRequestDataModelForFullPrescription> getMedicationRequestDataModelForFullPrescription() {
        return medicationRequestDataModelForFullPrescription;
    }

    /**
     * Returned einen String der die Klasse Textuell beschreibt.
     * Diese Funktion wird verwendet um ein Rezept in der UI anzeigen zu lassen.
     * @return a string representation of the FullPrescriptionDataModel object.
     */
    @NonNull
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FullPrescriptionDataModel{")
                .append("patientDataModel=").append(patientDataModel.toString())
                .append(", practitionerDataModel=").append(practitionerDataModel.toString())
                .append(", medicationRequestDataModelForFullPrescription=");
        for (MedicationRequestDataModelForFullPrescription medicationRequest : medicationRequestDataModelForFullPrescription) {
            sb.append(medicationRequest.toString()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()); // Remove the last comma and space
        sb.append('}');
        return sb.toString();
    }
}