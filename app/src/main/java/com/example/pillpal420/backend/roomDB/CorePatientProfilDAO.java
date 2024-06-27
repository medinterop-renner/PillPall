package com.example.pillpal420.backend.roomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
@Dao
public interface CorePatientProfilDAO {

    @Insert
    public void addCorePatientProfil(CorePatientProfil corePatientProfil);

    @Query("SELECT * FROM CorePatientProfil WHERE idRoomDB = :idRoomDB")
    CorePatientProfil getCorePatientProfil(int idRoomDB);



}
