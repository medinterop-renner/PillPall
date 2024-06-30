package com.example.pillpal420.backend.dataModels;

import androidx.annotation.NonNull;

/**
 * Klasse die die FHIR R5 HL7 Austria core implementation Guide Patient resource repräsentiert.
 */
public class PatientDataModel {
    private String id;
    private String identifierSocialSecurityNum;
    private String family;
    private String given;
    private String prefix;
    private String gender;
    private String birthDate;
    private String line;
    private String city;
    private String state;
    private String country;
    private String postalCode;

    /**
     * Konstruktor für die PatientDataModel Class.
     * Er wird gebraucht um Objekte von PatientDataModels zu erstellen.
     *
     * @param id String with unique Identifier for the server.
     * @param identifierSocialSecurityNum socialsecuritynumber.
     * @param family family Name.
     * @param given given Name.
     * @param prefix titel of the patient.
     * @param gender gender of the patient.
     * @param birthDate birthdate of the patient.
     * @param line street name and number of the address.
     * @param city Name of the city.
     * @param state Name of the state.
     * @param postalCode PostalCode of the patient.
     * @param country Country of the patient.
     */
    public PatientDataModel(String id, String identifierSocialSecurityNum, String family, String given, String prefix, String gender, String birthDate,
                            String line, String city, String state, String postalCode, String country) {
        this.id = id;
        this.identifierSocialSecurityNum = identifierSocialSecurityNum;
        this.family = family;
        this.given = given;
        this.prefix = prefix;
        this.gender = gender;
        this.birthDate = birthDate;
        this.line = line;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getFamily() {
        return family;
    }

    public String getGiven() {
        return given;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getIdentifierSocialSecurityNum() {
        return identifierSocialSecurityNum;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getLine() {
        return line;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Gebraucht um PatientDataModels besser für UI und Logging zu visualisieren
     * Repräsentiert ein PatientDataModel.
     *
     * @return A String that holds the values of PatientDataModels.
     */
    @NonNull
    @Override
    public String toString() {
        String ansprech;
        if (gender.equals("female")) {
            ansprech = "Frau";
        } else if (gender.equals("male")) {
            ansprech = "Herr";
        } else {
            ansprech = "";
        }
        if (prefix == null) {
            prefix = "";
        }
        return
                "Sozialversicherungsnummer: " + identifierSocialSecurityNum + " \n" +
                        "Name: " + " " + ansprech + ". " + prefix + " " + family + " " + given + "\n" +
                        "Geboren am: " + birthDate + "\n" +
                        "Addresse: " + line + " " + city + " " + postalCode + " " + state + " " + country;
    }
}
