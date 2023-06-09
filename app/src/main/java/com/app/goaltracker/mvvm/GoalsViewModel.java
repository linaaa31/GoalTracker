package com.app.goaltracker.mvvm;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.goaltracker.MainActivity;
import com.app.goaltracker.db.AppDatabase;
import com.app.goaltracker.db.DatabaseClient;
import com.app.goaltracker.db.Goal;
import com.app.goaltracker.db.GoalWithHistory;
import com.app.goaltracker.db.History;
import com.app.goaltracker.reminder.AlarmHelper;
import com.app.goaltracker.reminder.AlarmReceiver;
import com.app.goaltracker.reminder.NotificationHelper;
import com.app.goaltracker.reminder.ReminderActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class GoalsViewModel extends AndroidViewModel {
    private AppDatabase appDatabase;

    private MutableLiveData<List<GoalWithHistory>> liveGoals;
    private MutableLiveData<List<GoalWithHistory>> archivedGoals;
    private MutableLiveData<GoalWithHistory> selectedGoal;
    private MutableLiveData<List<GoalWithHistory>> currentGoals;
    private String filterText = "";
    Context context;
    public GoalsViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        appDatabase = DatabaseClient.getInstance(getApplication()).getAppDatabase();
        liveGoals = new MutableLiveData<>();
        archivedGoals = new MutableLiveData<>();
        selectedGoal = new MutableLiveData<>();
        currentGoals = new MutableLiveData<>();
        AsyncTask.execute(this::refreshGoalList);
    }

    public LiveData<List<GoalWithHistory>> getLiveGoals() {
        return liveGoals;
    }

    public LiveData<List<GoalWithHistory>> getArchivedGoals() {
        return archivedGoals;
    }
    public LiveData<List<GoalWithHistory>> getCurrentGoals() {return currentGoals;}

    public void setFilterText(String txt) {
        filterText = txt;
        refreshGoalList();
    }

    public void setReminder(LiveData<List<GoalWithHistory>> liveGoals) {
        Context context = getApplication();
        Intent intent = new Intent(context, AlarmReceiver.class);
        AlarmHelper.cancelAlarm(context, intent);

        Calendar currentCalendar = Calendar.getInstance();
        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);

        int nextHour = currentHour + 1;
        nextHour = nextHour % 24;

        List<GoalWithHistory> goalList = liveGoals.getValue();

        if (goalList != null) {
            for (GoalWithHistory goalWithHistory : goalList) {
                Goal goal = goalWithHistory.goal;
                List<String> hours = goal.getHours();

                if (goal != null && hours != null && !hours.isEmpty()) {
                    for (String hour : hours) {
                        String[] timeParts = hour.split(":");
                        int goalHour = Integer.parseInt(timeParts[0]);

                        if (goalHour == nextHour) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            Calendar triggerCalendar = Calendar.getInstance();
                            triggerCalendar.set(Calendar.HOUR_OF_DAY, nextHour);
                            triggerCalendar.set(Calendar.MINUTE, 0);
                            triggerCalendar.set(Calendar.SECOND, 0);
                            long triggerTimeInMillis = triggerCalendar.getTimeInMillis();

                            AlarmHelper.setAlarm(context, triggerTimeInMillis, intent);
                        }
                    }
                }
            }
        }
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


public void refreshGoalList() {
    Executors.newSingleThreadExecutor().execute(() -> {
        List<GoalWithHistory> allGoals = appDatabase.goalDao().getLiveGoalsWithHistory();
        liveGoals.postValue(allGoals);

        List<GoalWithHistory> goalsWithCurrentHour = new ArrayList<>();
        Calendar currentCalendar = Calendar.getInstance();
        int currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY);

        for (GoalWithHistory goalWithHistory : allGoals) {
            Goal goal = goalWithHistory.goal;
            List<String> hours = goal.getHours();

            if (goal != null && hours != null && !hours.isEmpty()) {
                for (String hour : hours) {
                    String[] timeParts = hour.split(":");
                    int goalHour = Integer.parseInt(timeParts[0]);

                    if (goalHour == currentHour) {
                        goalsWithCurrentHour.add(goalWithHistory);
                        break;
                    }
                }
            }
        }

        currentGoals.postValue(goalsWithCurrentHour);

        List<GoalWithHistory> allArchived = appDatabase.goalDao().getArchivedGoalsWithHistory();
        archivedGoals.postValue(allArchived.stream().filter(e -> e.goal.goalName.toLowerCase().startsWith(filterText.toLowerCase())).collect(Collectors.toList()));
        Log.i("UWC", "Refreshing goal list. Number of goals: " + allGoals.size());
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> setReminder(liveGoals));
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


