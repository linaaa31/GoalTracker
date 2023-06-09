package com.app.goaltracker.reminder;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.app.goaltracker.R;
import com.app.goaltracker.db.AppDatabase;
import com.app.goaltracker.db.DatabaseClient;
import com.app.goaltracker.db.Goal;
import com.app.goaltracker.db.GoalWithHistory;

import java.util.List;
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("UWC", "Alarm received");
        NotificationHelper.createChannel(context);
        NotificationHelper.show(context, "Goal Reminder", "Check goals");
    }
}
