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

    @Nullable
    @ColumnInfo(name = "goal_description")
    private String goalDescription;


    @ColumnInfo(name = "period")
    private String period;

    @ColumnInfo(name = "status")
    private String statusColor;

    public Goal(@NonNull String goalName, @Nullable String goalDescription, String period) {
        this.goalName = goalName;
        this.goalDescription = goalDescription;
        this.period = period;
    }

    public String getPeriod() {
        return period;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setPeriod(String period) {
        this.period = period;
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

    @Nullable
    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(@Nullable String goalDescription) {
        this.goalDescription = goalDescription;
    }

}
