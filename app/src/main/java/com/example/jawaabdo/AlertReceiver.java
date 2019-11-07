package com.example.jawaabdo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String courseID = intent.getStringExtra("courseID");
        Log.d("SarthakAditya", "Course ID : "+courseID);
        Intent mainIntent = new Intent(context, Courses.class);
        PendingIntent contextIntent = PendingIntent.getActivity(context,0, mainIntent,0);


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(courseID)
                .setContentText("Quiz has started")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(contextIntent);

        notificationManager.notify(1,builder.build());

    }
}
