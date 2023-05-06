package com.app.goaltracker.ui.goals;

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

import java.util.List;

public class GoalsViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private MutableLiveData<List<Goal>> goals;

    public GoalsViewModel(@NonNull Application application) {
        super(application);
        goals = new MutableLiveData<>();
        appDatabase = DatabaseClient.getInstance(getApplication()).getAppDatabase();
        AsyncTask.execute(() -> {
            refreshGoalList();
        });
    }

    public LiveData<List<Goal>> getGoals() {
        return goals;
    }

    public void addGoal(@NonNull String goalName, @Nullable String goalDescription, String period) {
        if (!TextUtils.isEmpty(goalName)) {
            AsyncTask.execute(() -> {
                appDatabase.goalDao().insert(new Goal(goalName, goalDescription, period));
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

    private void refreshGoalList() {
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