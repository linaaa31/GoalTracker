package com.app.goaltracker.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "Goals")
@TypeConverters(DateConverter.class)
public class Goal {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goal_id")
    public int goalId;

    @NonNull
    @ColumnInfo(name = "creation_date")
    public Date creationDate;

    @ColumnInfo(name = "archive_date")
    public Date archiveDate;
    @NonNull
    @ColumnInfo(name = "goal_name")
    public String goalName;

    @NonNull
    @ColumnInfo(name = "worst_result")
    public Integer worstResult;

    @NonNull
    @ColumnInfo(name = "best_result")
    public Integer bestResult;

    @ColumnInfo(name = "period")
    public Integer periodHours;

    @NonNull
    @ColumnInfo(name = "archived")
    public Boolean archived;


    public Goal(@NonNull String goalName, Integer periodHours) {
        this.creationDate = new Date();
        this.goalName = goalName;
        this.periodHours = periodHours;
        this.archived = false;
        this.worstResult = 0;
        this.bestResult = 100;
    }

}
