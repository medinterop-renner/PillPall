package com.example.pillpal420.backend.roomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * DataAccessObject um CorePracticionerProfil zu öffnen und bearbeiten
 */
@Dao
public interface CorePractitionerProfilDAO {

    /**
     * Einfügen eines CorePracticionerProfil Obejekts in die Datenbank
     *
     * @param corePractitionerProfil einzufügendes CorePracticionerProfil Obejekts
     */
    @Insert
    void addCorePractitionerProfil(CorePractitionerProfil corePractitionerProfil);

    /**
     * Erhalten eines CorePracticionerProfil Obejekts von der Datenbank über den primaryKey
     *
     * @param idRoomDB primaryKey
     * @return CorePracticionerProfil Obejekt für den passenden primaryKey
     */
    @Query("SELECT * FROM CorePractitionerProfil WHERE idRoomDB = :idRoomDB")
    CorePractitionerProfil getCorePractitionerProfil(int idRoomDB);
}