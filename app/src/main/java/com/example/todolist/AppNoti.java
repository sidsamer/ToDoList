package com.example.todolist;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;

public class AppNoti extends Application {

    public static final String CHANNEL_ID="exempleChannel";
    private AlarmManager alarm;
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }
        private void createNotificationChannel(){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                NotificationChannel channel=new NotificationChannel(
                        CHANNEL_ID,
                        "Example Channel",
                        NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("This is a Channel");
                NotificationManager manager=getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }
        }

}




