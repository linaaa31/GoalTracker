package com.app.goaltracker;

import android.app.Application;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.goaltracker.db.AppDatabase;
import com.app.goaltracker.db.DatabaseClient;
import com.app.goaltracker.db.Goal;
import com.app.goaltracker.db.History;

import java.util.List;

public class GoalsViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private MutableLiveData<List<Goal>> goals;
    private MutableLiveData<List<History>> history;

    public GoalsViewModel(@NonNull Application application) {
        super(application);
        goals = new MutableLiveData<>();
        history = new MutableLiveData<>();
        appDatabase = DatabaseClient.getInstance(getApplication()).getAppDatabase();
        AsyncTask.execute(() -> {
            refreshGoalList();
            refreshHistoryList();
        });
    }

    public LiveData<List<Goal>> getGoals() {
        return goals;
    }

    public LiveData<List<History>> getHistory() {
        return history;
    }

    public void addGoal(@NonNull String goalName, Integer period) {
        if (!TextUtils.isEmpty(goalName)) {
            AsyncTask.execute(() -> {
                appDatabase.goalDao().insertGoal(new Goal(goalName, period));
                refreshGoalList();
            });
        } else {
            Toast.makeText(getApplication(), "There are no goals", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteGoal(Goal goal) {
        AsyncTask.execute(() -> {
            appDatabase.goalDao().delete(goal);
            refreshGoalList();
        });
    }

    public void addHistory(int goalId, boolean result) {
        AsyncTask.execute(() -> {
            appDatabase.historyDao().insertHistory(new History(goalId, "", result));
            refreshHistoryList();
        });
    }



    public void deleteHistory(History history) {
        AsyncTask.execute(() -> {
            appDatabase.historyDao().deleteHistory(history);
            refreshHistoryList();
        });
    }
    private void refreshGoalList() {
        List<Goal> updateList = appDatabase.goalDao().getAllGoals();
        goals.postValue(updateList);
    }
    private void refreshHistoryList() {
        List<History> updateList = appDatabase.historyDao().getAll();
        history.postValue(updateList);
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