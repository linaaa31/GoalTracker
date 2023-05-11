package com.app.goaltracker.mvvm;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.goaltracker.db.AppDatabase;
import com.app.goaltracker.db.DatabaseClient;
import com.app.goaltracker.db.History;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;
    private MutableLiveData<List<History>> history;
    private Integer goalId;

    public HistoryViewModel(@NonNull Application application, Integer goalId) {
        super(application);
        history = new MutableLiveData<>();
        appDatabase = DatabaseClient.getInstance(getApplication()).getAppDatabase();
        this.goalId = goalId;
        AsyncTask.execute(() -> {
            refreshHistoryList();
        });
    }

    public LiveData<List<History>> getHistory() {
        return history;
    }

    public void addHistory(boolean result) {
        AsyncTask.execute(() -> {
            appDatabase.historyDao().insertHistory(new History(goalId, result));
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
        history.postValue(updateList);
    }
}

