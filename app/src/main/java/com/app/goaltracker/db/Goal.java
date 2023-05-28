package com.app.goaltracker.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "Goals")
@TypeConverters({DateConverter.class,ListStringConverter.class})
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

    @NonNull
    @ColumnInfo(name = "archived")
    public Boolean archived;

    @ColumnInfo(name = "event_count")
    public Integer eventCount;

    @ColumnInfo(name = "completed_event_count")
    public Integer completedEventCount;

    @ColumnInfo(name = "progress")
    public Integer progress;

    @NonNull
    @ColumnInfo(name = "hours")
    public List<String> hours;

    public Goal(@NonNull String goalName,  List<String> hours) {
        this.creationDate = new Date();
        this.goalName = goalName;
        this.archived = false;
        this.worstResult = 0;
        this.bestResult = 100;
        this.hours= hours;
    }

    public List<String> getHours() {
        return hours;
    }
    @NonNull
    public Integer getEventCount() {
        return eventCount;
    }

    @NonNull
    public Integer getCompletedEventCount() {
        return completedEventCount;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }

    public void setEventCount(@NonNull Integer eventCount) {
        this.eventCount = eventCount;
    }

    public void setCompletedEventCount(@NonNull Integer completedEventCount) {
        this.completedEventCount = completedEventCount;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public void addHour(String hour) {
        if (hours == null) {
            hours = new ArrayList<>();
        }
        hours.add(hour);
    }

    public void removeHour(String hour) {
        if (hours != null) {
            hours.remove(hour);
        }
    }

}
