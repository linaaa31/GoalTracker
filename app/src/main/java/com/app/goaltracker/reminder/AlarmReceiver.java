package com.app.goaltracker.reminder;

import static com.app.goaltracker.reminder.NotificationHelper.CHANNEL_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;

import com.app.goaltracker.R;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       // Log.i("ALM", "Received alarm, use intent to pass data");
        NotificationHelper.show(context, "Goal", "Notification from alarm");
      //  MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.cartoon);
       // mediaPlayer.setOnCompletionListener(MediaPlayer::release);
       // mediaPlayer.start();
    }
}