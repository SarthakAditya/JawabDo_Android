package com.example.jawaabdo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String courseID = intent.getStringExtra("courseID");
        String Message = intent.getStringExtra("Message");
        String channel = intent.getStringExtra("channel");
        Log.d("SarthakAditya", "Course ID : "+courseID);
        Log.d("SarthakAditya", "Message ID : "+Message);

        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(courseID,Message+" is active",channel);
        notificationHelper.getmManager().notify(1,nb.build());
    }
}
