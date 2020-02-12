package com.example.fest19_android.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fest19_android.data.dao.HealthDao;
import com.example.fest19_android.data.model.HealthEntry;

@Database(entities = {HealthEntry.class}, version = 1, exportSchema = false)  // version has to be incremenented every time database model changes.
public abstract class UserHealthDatabase extends RoomDatabase {
    private static UserHealthDatabase instance;

    public abstract HealthDao healthDao();

    public static UserHealthDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (UserHealthDatabase.class) {
                instance = Room.databaseBuilder(context.getApplicationContext(), UserHealthDatabase.class, "user_health_database")
                        .fallbackToDestructiveMigration().build();
                /* this destructive migration will simply delete database if you change the database model and increment version.*/
            }
        }
        return instance;
    }
}
