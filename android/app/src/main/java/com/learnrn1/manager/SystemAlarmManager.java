package com.learnrn1.manager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.learnrn1.R;
import com.learnrn1.domain.calendar.CalendarEvent;
import com.learnrn1.service.CalendarService;

import java.util.UUID;

/**
 * 系统提醒管理器
 */
public class SystemAlarmManager {
    private static SystemAlarmManager instance = new SystemAlarmManager();
    private CalendarManager calendarManager = CalendarManager.getInstance();

    private int systemAlartId = 10000;

    private SystemAlarmManager() {}

    public static SystemAlarmManager getInstance() {
        return instance;
    }

    public boolean setAlarm(String alartTime, Context context) {
        try {
            System.out.println("设置提醒。。。");
            CalendarEvent calendarEvent = calendarManager.getCalendarEventByAlarmTime(alartTime);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            Intent intent = new Intent(context, CalendarService.class);
            intent.putExtra("id", calendarEvent.getCalEventId());
            PendingIntent pi = PendingIntent.getService(context, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "0")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(calendarEvent.getTitle())
                    .setContentText(calendarEvent.getDescription())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)
                    .addAction(0, "我知道了", pi);
            notificationManager.notify((int)calendarEvent.getCalEventId(), builder.build());
            return true;
        } catch (Exception e) {
            Log.e("SystemAlarmManager", "设置闹钟失败,", e);
            return false;
        }
    }

    public void setProcessAlarm(String title,String text, float process, Context context, boolean finish) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "0");
        builder.setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_launcher_round)
                .setPriority(NotificationCompat.PRIORITY_LOW);

// Issue the initial notification with zero progress
        int PROGRESS_MAX = 100;
        builder.setProgress(PROGRESS_MAX, (int) process, false);
        if (finish) {
            builder.setProgress(0,0,false);
        }
        notificationManager.notify(systemAlartId, builder.build());
    }
}
