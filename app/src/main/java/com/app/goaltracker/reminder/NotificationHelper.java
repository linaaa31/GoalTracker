package com.app.goaltracker.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.goaltracker.MainActivity;
import com.app.goaltracker.R;

import java.util.Random;
//
//public class NotificationHelper {
//    final static String CHANNEL_ID = "reminder_channel_id";
//    public static void createChannel(Context context) {
//        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Goal Channel", NotificationManager.IMPORTANCE_HIGH);
//        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//
//        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
//        notificationManager.createNotificationChannel(channel);
//    }
//
//    public static void show(Context context, String title, String content) {
//        if (context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//
//            Intent intent = new Intent(context, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                    .setSmallIcon(R.drawable.ic_baseline_notification)
//                    .setContentTitle(title)
//                    .setContentText(content)
//                    .setContentIntent(pendingIntent)
//                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//            Random random = new Random();
//            int notificationId = random.nextInt(1000);
//            notificationManager.notify(notificationId, builder.build());
//        }
//    }
//}
//public class NotificationHelper {
//
//    // ...
//
//    public void showQuestionNotification(String goalName) {
//        // Create an intent for storing the result
//        Intent storeResultIntent = new Intent(context, StoreResultService.class);
//
//        // Create the PendingIntent for "Yes" button
//        PendingIntent yesPendingIntent = PendingIntent.getService(
//                context,
//                0,
//                storeResultIntent.putExtra("result", true),
//                PendingIntent.FLAG_UPDATE_CURRENT
//        );
//
//        // Create the PendingIntent for "No" button
//        PendingIntent noPendingIntent = PendingIntent.getService(
//                context,
//                0,
//                storeResultIntent.putExtra("result", false),
//                PendingIntent.FLAG_UPDATE_CURRENT
//        );
//
//        // Build the notification
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.notification_icon)
//                .setContentTitle("Goal Notification")
//                .setContentText("Did it go well?")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .addAction(R.drawable.ic_yes, "Yes", yesPendingIntent)
//                .addAction(R.drawable.ic_no, "No", noPendingIntent)
//                .setAutoCancel(true);
//
//        // Show the notification
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        notificationManager.notify(NOTIFICATION_ID, builder.build());
//    }
//}

