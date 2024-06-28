package com.example.pillpal420.backend.roomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CorePractitionerProfilDAO {

    @Insert
    void addCorePractitionerProfil(CorePractitionerProfil corePractitionerProfil);

    @Query("SELECT * FROM CorePractitionerProfil WHERE idRoomDB = :idRoomDB")
    CorePractitionerProfil getCorePractitionerProfil(int idRoomDB);
}