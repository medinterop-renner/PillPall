package com.example.pillpal420.backend.roomDB;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {CorePractitionerProfil.class}, version = 1, exportSchema = false)
public abstract class CorePractitionerProfileDatabase extends RoomDatabase {
    public abstract CorePractitionerProfilDAO getCorePractitionerProfilDAO();

    private static volatile CorePractitionerProfileDatabase INSTANCE;

    public static CorePractitionerProfileDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CorePractitionerProfileDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CorePractitionerProfileDatabase.class, "CorePractitionerProfil")
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