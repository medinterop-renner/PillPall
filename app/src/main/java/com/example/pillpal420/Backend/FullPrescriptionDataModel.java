package com.example.pillpal420.Backend;

import androidx.annotation.NonNull;

import java.util.List;

public class FullPrescriptionDataModel {

    private PatientDataModel patientDataModel;
    private PractitionerDataModel practitionerDataModel;
    private List<MedicationRequestDataModelForFullPrescription> medicationRequestDataModelForFullPrescription;

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