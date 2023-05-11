package com.app.goaltracker.mvvm;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class HistoryViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private Integer goalId;


    public HistoryViewModelFactory(Application application, Integer goalId) {
        this.application = application;
        this.goalId = goalId;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new HistoryViewModel(application, goalId);
    }
}