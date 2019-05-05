package com.example.todolist;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class MyAlarm extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.notificationManager = NotificationManagerCompat.from(context);
        Notification notification=new NotificationCompat.Builder(context,AppNoti.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_android_black_24dp)
                .setContentTitle("Task Alarm - Ends In Less Then Hour from Now")
                .setContentText(intent.getStringExtra("msg"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        MainActivity.notificationManager.notify(intent.getIntExtra("id",0),notification);
    }
    public static void RemoveAlarm(Tasks task)
    {
        MainActivity.notificationManager.cancel(task.getId());
    }
}
