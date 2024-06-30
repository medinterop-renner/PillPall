package com.example.pillpal420.backend.dataModels;

/**
 * Diese Klasse bildet alle Daten ab die für Ärztinnen gebraucht werden.
 * Diese Daten entsprechem dem FHIR HL7 Austria core implementation Guide
 */
public class PractitionerDataModel {
   private String id;
   private String oidPractitioner;
   private String family;
    private String given;
    private String suffix;
    private String telecom;
    private String line;
    private String city;
    private String country;
    private String postalCode;
    /**
     * Dies ist der Constructor der PractitionerDataModel Klasse.
     * Er wird gebracuht um Objekte dieser Klasse zu erstellen.
     *
     * @param id Speichert den Relativen Path für den server
     * @param oidPractitioner Kennnummer für Österreichische GDA
     * @param family Familienname
     * @param given Vorname
     * @param suffix Titel
     * @param telecom Telefonnummer
     * @param line Straße und Hausnummer
     * @param city Stadt
     * @param postalCode Postleizahl
     * @param country Land
     */
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
        return id;}
    public String getOidPractitioner(){return oidPractitioner;}
    public String getFamily() {
        return family;}
    public String getGiven() {
        return given;}
    public String getSuffix() {
        return suffix;}
    public String getTelecom() {
        return telecom;}
    public String getLine() {
        return line;}
    public String getCity() {
        return city;}
    public String getPostalCode(){return postalCode;}
    public String getCountry() {
        return country;}
    /**
     * Gibt einen schön formatierten String für Logging statements und gebrauch in der UI Wieder.
     * Die Standard toString methode wird somit überschrieben.
     *
     * @return returns a formatted PractionerDataModel for use in UI and Logging.
     */
    @Override
    public String toString() {
       return  "Name: " + suffix + " " + family + " " + given + "\n" +
                "Telefonnummer: " + telecom + "\n" +
                "Addresse: " + line + " " + city + " " + postalCode + " " + country;
    }
}


