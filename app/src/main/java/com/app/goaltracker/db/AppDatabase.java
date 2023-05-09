package com.app.goaltracker.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Goal.class,History.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();
    public abstract HistoryDao historyDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDBInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "goal")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}