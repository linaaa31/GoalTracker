package com.app.goaltracker.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface HistoryDao {


        @Query("SELECT * FROM History")
        List<History> getAll();

        @Insert
        void insertHistory(History history);

        @Update
        void updateHistory(History history);

        @Delete
        void deleteHistory(History history);

    }

