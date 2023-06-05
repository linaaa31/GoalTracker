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

//public class AlarmReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//       // Log.i("ALM", "Received alarm, use intent to pass data");
//        NotificationHelper.show(context, "Goal", "Notification from alarm");
//      //  MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.cartoon);
//       // mediaPlayer.setOnCompletionListener(MediaPlayer::release);
//       // mediaPlayer.start();
//    }
//}
//public class AlarmReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String goalName = intent.getStringExtra("goal_name");
//
//        // Display a notification with goal name and question
//        NotificationHelper notificationHelper = new NotificationHelper(context);
//        notificationHelper.showQuestionNotification(goalName);
//    }
//}