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

public class HistoryViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
   // private MutableLiveData<List<History>> history;
   private LiveData<List<History>> history;
    private Integer goalId;


public HistoryViewModel(@NonNull Application application, int goalId) {
    super(application);
    appDatabase = DatabaseClient.getInstance(getApplication()).getAppDatabase();
    this.goalId = goalId;
    history = appDatabase.historyDao().getHistoryByGoalId(goalId);
}

    public LiveData<List<History>> getHistoryByGoalId(int goalId) {
        return appDatabase.historyDao().getHistoryByGoalId(goalId);
    }
    public LiveData<List<History>> getHistory() {
        return history;
    }



//public void addHistory(History history) {
//    AsyncTask.execute(() -> {
//        appDatabase.historyDao().insertHistory(history);
//        refreshHistoryList();
//    });
//}
public void addHistory(History history) {
    AsyncTask.execute(() -> {
        appDatabase.historyDao().insertHistory(history);
        Goal goal = appDatabase.goalDao().getGoalById(history.getGoalId());
        Integer eventCount = goal.getEventCount();
        Integer completedEventCount = goal.getCompletedEventCount();
        if (eventCount == null) {
            eventCount = 0;
        }
        if (completedEventCount == null) {
            completedEventCount = 0;
        }

        goal.setEventCount(eventCount);
        goal.setCompletedEventCount(completedEventCount);

        if (goal != null) {
            goal.setEventCount(eventCount + 1);
            if (history.isResult()) {
                goal.setCompletedEventCount(completedEventCount + 1);
            }else{
                goal.setCompletedEventCount(completedEventCount);
            }
            appDatabase.goalDao().updateGoal(goal);
        }

        refreshHistoryList();
    });
}
    public void deleteHistory(History history) {
        AsyncTask.execute(() -> {
            appDatabase.historyDao().deleteHistory(history);
            refreshHistoryList();
        });
    }

    public void refreshHistoryList() {
    List<History> updateList = appDatabase.historyDao().getAll();
    history = new MutableLiveData<>(updateList);
    }
}

