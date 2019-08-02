package com.learnrn1.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.learnrn1.MainActivity;
import com.learnrn1.MainApplication;
import com.learnrn1.R;

/**
 * 系统闹钟管理器
 */
public class SystemAlarmManager {
    private static SystemAlarmManager instance = new SystemAlarmManager();

    private SystemAlarmManager() {}

    public static SystemAlarmManager getInstance() {
        return instance;
    }

    public boolean setAlarm(String title, String content, Context context) {
        try {
            System.out.println("设置提醒。。。");
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "0")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)
                    .addAction(R.mipmap.ic_launcher, "我知道了", pi);
            notificationManager.notify(1, builder.build());
            return true;
        } catch (Exception e) {
            Log.e("SystemAlarmManager", "设置闹钟失败,", e);
            return false;
        }
    }
}
