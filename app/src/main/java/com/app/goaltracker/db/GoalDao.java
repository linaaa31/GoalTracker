package com.app.goaltracker.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalDao {

    @Transaction
    @Query("SELECT * FROM goals WHERE archived != 1")
    List<GoalWithHistory> getLiveGoalsWithHistory();

    @Transaction
    @Query("SELECT * FROM goals WHERE archived = 1")
    List<GoalWithHistory> getArchivedGoalsWithHistory();

    @Transaction
    @Query("SELECT * FROM Goals WHERE goal_id = :goalId")
    GoalWithHistory getGoalById(int goalId);

    @Insert
    void insertGoal(Goal goal);

    @Update
    void updateGoal(Goal goal);

    @Delete
    void delete(Goal goal);
    @Query("SELECT * FROM Goals WHERE archived = 1")
    LiveData<List<Goal>> getArchivedGoals();

    @Query("DELETE FROM Goals WHERE goal_id = :goalId")
    void deleteById(int goalId);
}