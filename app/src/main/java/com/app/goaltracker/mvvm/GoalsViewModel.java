package com.app.goaltracker.mvvm;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.goaltracker.db.AppDatabase;
import com.app.goaltracker.db.DatabaseClient;
import com.app.goaltracker.db.Goal;

import java.util.List;

public class GoalsViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private MutableLiveData<List<Goal>> goals;

    public GoalsViewModel(@NonNull Application application) {
        super(application);
        goals = new MutableLiveData<>();
        appDatabase = DatabaseClient.getInstance(getApplication()).getAppDatabase();
        AsyncTask.execute(this::refreshGoalList);
    }

    public LiveData<List<Goal>> getGoals() {
        return goals;
    }

    public void addGoal(@NonNull String goalName, Integer period) {
        AsyncTask.execute(() -> {
            appDatabase.goalDao().insertGoal(new Goal(goalName, period));
            refreshGoalList();
        });
    }

    public void deleteGoal(Goal goal) {
        AsyncTask.execute(() -> {
            appDatabase.goalDao().delete(goal);
            refreshGoalList();
        });
    }

    public void refreshGoalList() {
        List<Goal> updateList = appDatabase.goalDao().getAllGoals();
        goals.postValue(updateList);
    }
    public void readRequest() {
        AsyncTask.execute(() -> {
            refreshGoalList();
        });
    }

    public void updateGoal(Goal goal) {
        AsyncTask.execute(() -> {
            appDatabase.goalDao().updateGoal(goal);
            refreshGoalList();
        });
    }
}