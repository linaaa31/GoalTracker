package com.app.goaltracker.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GoalDao {
    @Query("SELECT * FROM Goals")
    List<Goal> getAllGoals();

    @Insert
    void insertGoal(Goal goal);

    @Update
    void updateGoal(Goal goal);

    @Delete
    void delete(Goal goal);

    @Query("SELECT * FROM Goals WHERE goal_id = :goalId")
    Goal getGoalById(int goalId);
}