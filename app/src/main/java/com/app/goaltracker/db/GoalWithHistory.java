package com.app.goaltracker.db;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Relation;

import java.util.Date;
import java.util.List;

public class GoalWithHistory {
    public GoalWithHistory() {
    }

    @Embedded
    public Goal goal;

    @Relation(parentColumn = "goal_id", entityColumn = "goal_id")
    public List<History> historyList;
}
