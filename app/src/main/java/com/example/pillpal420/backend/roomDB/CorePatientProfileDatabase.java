package com.example.pillpal420.backend.roomDB;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CorePatientProfil.class}, version = 1, exportSchema = false)
public abstract class CorePatientProfileDatabase extends RoomDatabase {
    public abstract CorePatientProfilDAO getCorePatientProfilDAO();

    private static volatile CorePatientProfileDatabase INSTANCE;

    /**
     * Erhält ein Element aus der CorePatientProfileDatabase
     * <p>
     * Erhält ein Element aus der CorePatientProfileDatabase. Hier wird das SINGLETON pattern defined.
     *      * Das singleton pattern stellt sicher das nur eine instanz von der CorePatientProfileDB erstellt wird.
     *
     * Funktion:
     * 1. Überprüfen ob die Instance null ist
     * --> Instance ist null: synchronisieren der CorePatientProfilDatabase Klasse
     * erneutes Überprufen der Instanz
     * --> Instanz ist immmernoch null: Initialisieren der Datenbank mit Name und Klasse
     * 2. Hinzufügen eines Callbacks zum Datenbank-Builder
     * 3. Erstellen der Datenbank und hinzufügen zu Instance
     *
     * @param context Context um die Datenbank zu Initialisieren
     * @return ein Element der CorePatientProfilDatabase
     */
    public static CorePatientProfileDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CorePatientProfileDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CorePatientProfileDatabase.class, "CorePatientProfil")
                            .addCallback(myRoomCallBack)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Callback für die Erstellung und Öffnung der Datenbank
     */
    private static final RoomDatabase.Callback myRoomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}