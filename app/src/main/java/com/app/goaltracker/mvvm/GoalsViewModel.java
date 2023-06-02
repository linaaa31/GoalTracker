package com.app.goaltracker.mvvm;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.app.goaltracker.db.AppDatabase;
import com.app.goaltracker.db.DatabaseClient;
import com.app.goaltracker.db.Goal;
import com.app.goaltracker.db.GoalWithHistory;
import com.app.goaltracker.db.History;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class GoalsViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;

    private MutableLiveData<List<GoalWithHistory>> liveGoals;
    private MutableLiveData<List<GoalWithHistory>> archivedGoals;
    private MutableLiveData<GoalWithHistory> selectedGoal;
    private String filterText = "";

    public GoalsViewModel(@NonNull Application application) {
        super(application);
        appDatabase = DatabaseClient.getInstance(getApplication()).getAppDatabase();
        liveGoals = new MutableLiveData<>();
        archivedGoals = new MutableLiveData<>();
        selectedGoal = new MutableLiveData<>();
        AsyncTask.execute(this::refreshGoalList);
    }

    public LiveData<List<GoalWithHistory>> getLiveGoals() {
        return liveGoals;
    }

    public LiveData<List<GoalWithHistory>> getArchivedGoals() {
        return archivedGoals;
    }

    public void setFilterText(String txt) {
        filterText = txt;
        refreshGoalList();
    }

    public void addGoal(@NonNull String goalName, List<String> hours) {
        AsyncTask.execute(() -> {
            appDatabase.goalDao().insertGoal(new Goal(goalName, hours));
            refreshGoalList();
        });
    }

    public LiveData<GoalWithHistory> selectGoal(Integer goalId) {
        AsyncTask.execute(() -> {
            selectedGoal.postValue(appDatabase.goalDao().getGoalById(goalId));
        });
        return selectedGoal;
    }

    public void deleteGoalById(int goalId) {
        AsyncTask.execute(() -> {
            appDatabase.goalDao().deleteById(goalId);
            refreshGoalList();
        });
    }

    public void archiveGoal(int goalId) {
        AsyncTask.execute(() -> {
            GoalWithHistory goal = appDatabase.goalDao().getGoalById(goalId);
            if (goal != null) {
                goal.goal.setArchived(true);
                goal.goal.setArchiveDate(new Date());
                appDatabase.goalDao().updateGoal(goal.goal);
                refreshGoalList();
            }
        });
    }

    public void unarchiveGoal(int goalId) {
        AsyncTask.execute(() -> {
            GoalWithHistory goal = appDatabase.goalDao().getGoalById(goalId);
            if (goal != null) {
                goal.goal.setArchived(false);
                goal.goal.setArchiveDate(null);
                appDatabase.goalDao().updateGoal(goal.goal);
                refreshGoalList();
            }
        });
    }

    public void refreshGoalList() {
        Executors.newSingleThreadExecutor().execute(() -> {
            liveGoals.postValue(appDatabase.goalDao().getLiveGoalsWithHistory());
            List<GoalWithHistory> allArchived = appDatabase.goalDao().getArchivedGoalsWithHistory();
            archivedGoals.postValue(allArchived.stream().filter(e -> e.goal.goalName.contains(filterText)).collect(Collectors.toList()));
        });
    }

    public void updateGoalProgress(int goalId, int progress) {
        AsyncTask.execute(() -> {
            GoalWithHistory goal = appDatabase.goalDao().getGoalById(goalId);
            if (goal != null) {
                goal.goal.setProgress(progress);
                appDatabase.goalDao().updateGoal(goal.goal);
            }
            refreshGoalList();
        });
    }


    public void addHour(Goal goal, String hour) {
        AsyncTask.execute(() -> {
            goal.addHour(hour);
            appDatabase.goalDao().updateGoal(goal);
            refreshGoalList();
        });
    }

    public void removeHour(Goal goal, String hour) {
        AsyncTask.execute(() -> {
            goal.removeHour(hour);
            appDatabase.goalDao().updateGoal(goal);
            refreshGoalList();
        });
    }

    public void addHistory(History history) {
        AsyncTask.execute(() -> {
            appDatabase.historyDao().insertHistory(history);
            selectedGoal.postValue(appDatabase.goalDao().getGoalById(history.goalId));
            refreshGoalList();
        });
    }
}

