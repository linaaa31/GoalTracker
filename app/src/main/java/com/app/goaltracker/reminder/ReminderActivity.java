package com.app.goaltracker.reminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.app.goaltracker.R;
import com.app.goaltracker.mvvm.GoalsViewModel;

public class ReminderActivity extends AppCompatActivity {
    private GoalsViewModel goalsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        goalsViewModel = new ViewModelProvider(this).get(GoalsViewModel.class);
        goalsViewModel.getLiveGoals().observe(this, goals -> {
            //1. filter goals to leave the ones that have reminder now
            //2. create event for each goal and put into HistoryAdapter
        });
    }
}
