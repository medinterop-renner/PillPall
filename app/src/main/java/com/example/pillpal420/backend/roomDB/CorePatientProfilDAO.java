package com.example.pillpal420.backend.roomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * DataAccessObject um CorePatientProfil zu erhalten und bearbeiten
 */
@Dao
public interface CorePatientProfilDAO {

    /**
     * Fügt ein corePatientProfil Objekt zur Datenbank hinzu
     *
     * @param corePatientProfil einzufügende Eintität
     */
    @Insert
    public void addCorePatientProfil(CorePatientProfil corePatientProfil);

    /**
     * Entnimmt ein CorePatientProfil aus der Datenbank durch den PrimaryKEy
     *
     * @param idRoomDB primaryKey
     * @return CorePatientProfile
     */
    @Query("SELECT * FROM CorePatientProfil WHERE idRoomDB = :idRoomDB")
    CorePatientProfil getCorePatientProfil(int idRoomDB);


}
