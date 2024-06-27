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