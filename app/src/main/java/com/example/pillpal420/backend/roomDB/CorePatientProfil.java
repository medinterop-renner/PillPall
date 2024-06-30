package com.example.pillpal420.backend.roomDB;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * definiert den table name in dem CorePatientProfils objects gespeichert werden.
 */
@Entity(tableName = "CorePatientProfil")
public class CorePatientProfil {
    /**
     * definiert idRoomDB als primaryKey für den table CorePatientProfil
     */
    @ColumnInfo(name = "idRoomDB")
    @PrimaryKey(autoGenerate = true)
    private int idRoomDB;
    @ColumnInfo(name = "id")
    private String id;
    @ColumnInfo(name = "identifierSocialSecurityNum")
    private String identifierSocialSecurityNum;
    @ColumnInfo(name = "family")
    private String family;
    @ColumnInfo(name = "given")
    private String given;
    @ColumnInfo(name = "prefix")
    private String prefix;
    @ColumnInfo(name = "gender")
    private String gender;
    @ColumnInfo(name = "birthDate")
    private String birthDate;
    @ColumnInfo(name = "line")
    private String line;
    @ColumnInfo(name = "city")
    private String city;
    @ColumnInfo(name = "state")
    private String state;
    @ColumnInfo(name = "country")
    private String country;
    @ColumnInfo(name = "postalCode")

    private String postalCode;

    @Ignore
    public CorePatientProfil() {

    }


    /**
     * Konstruktor
     */
    public CorePatientProfil(int idRoomDB, String id, String identifierSocialSecurityNum, String family, String given,
                             String prefix, String gender, String birthDate,
                             String line, String city, String state, String postalCode, String country) {
        this.idRoomDB = idRoomDB;
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

    /**
     * Getter für idRoomDB
     *
     * @return idRoomDB
     */
    public int getIdRoomDB() {
        return idRoomDB;
    }

    /**
     * Setter für idRoomDB
     *
     * @param idRoomDB
     */
    public void setIdRoomDB(int idRoomDB) {
        this.idRoomDB = idRoomDB;
    }

    /**
     * Getter für ID
     *
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter für id
     *
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter für identifierSocialSecurityNum
     *
     * @return identifierSocialSecurityNum
     */
    public String getIdentifierSocialSecurityNum() {
        return identifierSocialSecurityNum;
    }

    /**
     * Setter für identifierSocialSecurityNum
     *
     * @param identifierSocialSecurityNum
     */
    public void setIdentifierSocialSecurityNum(String identifierSocialSecurityNum) {
        this.identifierSocialSecurityNum = identifierSocialSecurityNum;
    }

    /**
     * Getter für family
     *
     * @return family
     */
    public String getFamily() {
        return family;
    }

    /**
     * Setter für family
     *
     * @param family
     */
    public void setFamily(String family) {
        this.family = family;
    }

    /**
     * Getter für given
     *
     * @return given
     */
    public String getGiven() {
        return given;
    }

    /**
     * Setter für given
     *
     * @param given
     */
    public void setGiven(String given) {
        this.given = given;
    }

    /**
     * Getter für prefix
     *
     * @return prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Setter für prefix
     *
     * @param prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Getter für gender
     *
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Setter für gender
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Getter für birthDate
     *
     * @return birthDate
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Setter für birthDate
     *
     * @param birthDate
     */
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    /**
     * Getter für line
     *
     * @return line
     */
    public String getLine() {
        return line;
    }

    /**
     * Setter für line
     *
     * @param line
     */
    public void setLine(String line) {
        this.line = line;
    }

    /**
     * Getter für city
     *
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter für city
     *
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter für state
     *
     * @return state
     */
    public String getState() {
        return state;
    }

    /**
     * Setter für state
     *
     * @param state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter für country
     *
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter für country
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Getter für postalCode
     *
     * @return postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Setter für postalCode
     *
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Hier wird ein String aus allen Strings gebildet und ausgegeben
     *
     * @return String des Objekts
     */
    @NonNull
    @Override
    public String toString() {
        return "{" +
                "primarKey=" + idRoomDB +
                "id='" + id + '\'' +
                ", Sozialversicherungsnummer='" + identifierSocialSecurityNum + '\'' +
                ", family='" + family + '\'' +
                ", given='" + given + '\'' +
                ", prefix='" + prefix + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", line='" + line + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", postalCode='" + postalCode + '\'' +
                '}';
    }
}
