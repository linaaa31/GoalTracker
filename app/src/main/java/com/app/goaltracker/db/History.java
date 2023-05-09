package com.app.goaltracker.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "History",
        foreignKeys = @ForeignKey(entity = Goal.class,
                parentColumns = "goal_id",
                childColumns = "goal_id",
                onDelete = ForeignKey.CASCADE))
public class History {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "history_id")
    private int historyId;

    @ColumnInfo(name = "goal_id")
    private int goalId;

    @ColumnInfo(name = "hours")
    private String hours;

    @ColumnInfo(name = "result")
    private boolean result;

    public History(int goalId, String hours, boolean result) {
        this.goalId = goalId;
        this.hours = hours;
        this.result = result;
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
