package com.example.pillpal420.backend.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "CorePractitionerProfil")
public class CorePractitionerProfil {

    @ColumnInfo(name = "idRoomDB")
    @PrimaryKey(autoGenerate = true)
    private int idRoomDB;

    @ColumnInfo(name = "id")
    private String id;

    @ColumnInfo(name = "oidPractitioner")
    private String oidPractitioner;

    @ColumnInfo(name = "family")
    private String family;

    @ColumnInfo(name = "given")
    private String given;

    @ColumnInfo(name = "suffix")
    private String suffix;

    @ColumnInfo(name = "telecom")
    private String telecom;

    @ColumnInfo(name = "line")
    private String line;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "postalCode")
    private String postalCode;

    @ColumnInfo(name = "country")
    private String country;

    @Ignore
    public CorePractitionerProfil() {}

    public CorePractitionerProfil(int idRoomDB, String id, String oidPractitioner, String family, String given,
                                  String suffix, String telecom, String line, String city,
                                  String postalCode, String country) {
        this.idRoomDB = idRoomDB;
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

    public String getOidPractitioner() {
        return oidPractitioner;
    }

    public void setOidPractitioner(String oidPractitioner) {
        this.oidPractitioner = oidPractitioner;
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

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getTelecom() {
        return telecom;
    }

    public void setTelecom(String telecom) {
        this.telecom = telecom;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "idRoomDB=" + idRoomDB +
                ", id='" + id + '\'' +
                ", oidPractitioner='" + oidPractitioner + '\'' +
                ", family='" + family + '\'' +
                ", given='" + given + '\'' +
                ", suffix='" + suffix + '\'' +
                ", telecom='" + telecom + '\'' +
                ", line='" + line + '\'' +
                ", city='" + city + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}