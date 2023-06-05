package com.app.goaltracker.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.app.goaltracker.db.Goal;
//
//public class AlarmHelper {
//
//    private AlarmManager alarmManager;
//    private PendingIntent pendingIntent;
//
//    public void scheduleAlarm(Context context) {
//        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(context, 200, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//        alarmManager.cancel(pendingIntent);
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
//
//            //alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(msToOff, null), alarmIntent);
//            // alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
//        }
//    }
//    public void stopAlarm(Context context, int goalId) {
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, goalId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        alarmManager.cancel(alarmIntent);
//        alarmIntent.cancel();
//    }
//}
//    public void stopAlarm() {
//        if (alarmManager != null) {
//            alarmManager.cancel(alarmIntent);
//        }
//    }
//}
