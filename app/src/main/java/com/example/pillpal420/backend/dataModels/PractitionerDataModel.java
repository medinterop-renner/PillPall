package com.example.pillpal420.backend.dataModels;

public class PractitionerDataModel {

   private String id;
   private String oidPractitioner; // 1.2.40.0.34.3.2.0 test person OID Browser
   private String family;
    private String given;
    private String suffix;
    private String telecom;
    private String line;
    private String city;
    private String country;

    private String postalCode;

    public PractitionerDataModel(String id,String oidPractitioner, String family, String given,
                                 String suffix, String telecom, String line,
                                 String city, String postalCode, String country) {
        this.id = id;
        this.oidPractitioner = oidPractitioner;
        this.family = family;
        this.given = given;
        this.suffix = suffix;
        this.telecom = telecom;
        this.line = line;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }

    public String getId() {
        return id;
    }
    public String getOidPractitioner(){return oidPractitioner;}

    public String getFamily() {
        return family;
    }

    public String getGiven() {
        return given;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getTelecom() {
        return telecom;
    }

    public String getLine() {
        return line;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode(){return postalCode;}

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
       return  "Name: " + suffix + " " + family + " " + given + "\n" +
                "Telefonnummer: " + telecom + "\n" +
                "Addresse: " + line + " " + city + " " + postalCode + " " + country;

    }
}


