package com.example.pillpal420.Backend.RoomDB;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "CorePatientProfil")
public class CorePatientProfil {

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
    public CorePatientProfil(){

    }



    public CorePatientProfil(int idRoomDB,String id, String identifierSocialSecurityNum, String family, String given,
                             String prefix, String gender, String birthDate,
                            String line, String city, String state, String postalCode, String country){
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
// Getters and setters
    public int getIdRoomDB() {
        return idRoomDB;
    }

    public void setIdRoomDB(int idRoomDB) {
        this.idRoomDB = idRoomDB;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentifierSocialSecurityNum() {
        return identifierSocialSecurityNum;
    }

    public void setIdentifierSocialSecurityNum(String identifierSocialSecurityNum) {
        this.identifierSocialSecurityNum = identifierSocialSecurityNum;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getGiven() {
        return given;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "primarKey="  + idRoomDB +
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
