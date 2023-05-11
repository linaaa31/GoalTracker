package com.app.goaltracker.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Goal.class, History.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract GoalDao goalDao();
    public abstract HistoryDao historyDao();
}