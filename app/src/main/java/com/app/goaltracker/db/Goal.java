package com.app.goaltracker.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Goals")
public class Goal {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goal_id")
    private int goalId;

    @NonNull
    @ColumnInfo(name = "goal_name")
    private String goalName;

    @ColumnInfo(name = "period")
    private Integer periodHours;


    public Goal(@NonNull String goalName, Integer periodHours) {
        this.goalName = goalName;
        this.periodHours = periodHours;
    }


    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    @NonNull
    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(@NonNull String goalName) {
        this.goalName = goalName;
    }

    public Integer getPeriodHours() {
        return periodHours;
    }

    public void setPeriodHours(Integer periodHours) {
        this.periodHours = periodHours;
    }
}
