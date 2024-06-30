package com.example.pillpal420.backend.dataModels;


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

    @Override
    public String toString() {

        return
                "Sozialversicherungsnummer: " + identifierSocialSecurityNum + " \n" +
                        "Name: " + " " + gender + ". " + prefix + " " + family + " " + given + "\n" +
                        "Geboren am: " + birthDate + "\n" +
                        "Addresse: " + line + " " + city + " " + postalCode + " " + state + " " + country;

    }
}
