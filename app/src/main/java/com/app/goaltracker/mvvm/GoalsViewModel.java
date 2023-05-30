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
import com.app.goaltracker.db.History;

import java.util.List;
import java.util.concurrent.Executors;

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

    public void addGoal(@NonNull String goalName,  List<String> hours) {
        AsyncTask.execute(() -> {
            appDatabase.goalDao().insertGoal(new Goal(goalName, hours));
            refreshGoalList();
        });
    }


public void deleteGoalById(int goalId) {
    AsyncTask.execute(() -> {
        appDatabase.goalDao().deleteById(goalId);
        refreshGoalList();
    });
}

    public void refreshGoalList() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Goal> updatedGoals = appDatabase.goalDao().getAllGoals();

            for (Goal goal : updatedGoals) {
                LiveData<List<History>> historyLiveData = appDatabase.historyDao().getHistoryByGoalId(goal.getGoalId());
                List<History> historyList = historyLiveData.getValue();

                if (historyList != null) {
                    int completedEventCount = 0;

                    for (History history : historyList) {
                        if (history.isResult()) {
                            completedEventCount++;
                        }
                    }

                    goal.setEventCount(historyList.size());
                    goal.setCompletedEventCount(completedEventCount);
                }
            }

            goals.postValue(updatedGoals);
        });
    }
    public void updateGoalProgress(int goalId, int progress) {
        AsyncTask.execute(() -> {
            Goal goal = appDatabase.goalDao().getGoalById(goalId);
            if (goal != null) {
                goal.setProgress(progress);
                appDatabase.goalDao().updateGoal(goal);
            }
            refreshGoalList();
        });
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
}
//    public void refreshGoalList() {
//        List<Goal> updateList = appDatabase.goalDao().getAllGoals();
//        goals.postValue(updateList);
//    }

//    public void deleteGoal(Goal goal) {
//        AsyncTask.execute(() -> {
//            appDatabase.goalDao().delete(goal);
//            refreshGoalList();
//        });
//    }
