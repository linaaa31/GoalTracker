package com.app.goaltracker.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "History", foreignKeys = @ForeignKey(entity = Goal.class,
        parentColumns = "goal_id",
        childColumns = "goal_id",
        onDelete = ForeignKey.CASCADE), indices = {@Index("goal_id")})
@TypeConverters(DateConverter.class)
public class History {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "history_id")
    public int historyId;

    @ColumnInfo(name = "goal_id")
    public int goalId;

    @ColumnInfo(name = "timestamp")
    public Date timestamp;

    @ColumnInfo(name = "result")
    public boolean result;

    public boolean isResult() {
        return result;
    }

    public History(int goalId, boolean result) {
        this.goalId = goalId;
        this.timestamp = new Date();
        this.result = result;
    }

    public int getGoalId() {
        return goalId;
    }
}
