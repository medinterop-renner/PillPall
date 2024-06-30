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

    /**
     * Konstruktor
     */
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

    /**
     * Getter für idRoomDB
     * @return idRoomDB
     */
    public int getIdRoomDB() {
        return idRoomDB;
    }

    /**
     * Setter für idRoomDB
     * @param idRoomDB
     */
    public void setIdRoomDB(int idRoomDB) {
        this.idRoomDB = idRoomDB;
    }

    /**
     * Getter für id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Setter für id
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter für oidPracticioner
     * @return oidPracticioner
     */
    public String getOidPractitioner() {
        return oidPractitioner;
    }

    /**
     * Setter für oidPracticioner
     * @param oidPractitioner
     */
    public void setOidPractitioner(String oidPractitioner) {
        this.oidPractitioner = oidPractitioner;
    }

    /**
     * Getter für family
     * @return family
     */
    public String getFamily() {
        return family;
    }

    /**
     * Setter für family
     * @param family
     */
    public void setFamily(String family) {
        this.family = family;
    }

    /**
     * Getter für given
     * @return given
     */
    public String getGiven() {
        return given;
    }

    /**
     * Setter für given
     * @param given
     */
    public void setGiven(String given) {
        this.given = given;
    }

    /**
     * Getter für suffix
     * @return suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Setter für suffix
     * @param suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    /**
     * Getter für telecom
     * @return telecom
     */
    public String getTelecom() {
        return telecom;
    }

    /**
     * Setter für telecom
     * @param telecom
     */
    public void setTelecom(String telecom) {
        this.telecom = telecom;
    }

    /**
     * Getter für line
     * @return line
     */
    public String getLine() {
        return line;
    }

    /**
     * Setter für line
     * @param line
     */
    public void setLine(String line) {
        this.line = line;
    }

    /**
     * Getter für city
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter für city
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter für postalCode
     * @return
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Setter für postalCode
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Getter für country
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Setter für country
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return Liefert einen zusammengesetzten String aus allen Strings zurück
     */
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