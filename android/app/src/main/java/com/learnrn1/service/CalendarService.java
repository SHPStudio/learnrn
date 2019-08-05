package com.learnrn1.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationManagerCompat;

import com.learnrn1.manager.CalendarManager;

public class CalendarService extends Service {
    private CalendarManager calendarManager = CalendarManager.getInstance();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        long id = intent.getLongExtra("id",  0);
        System.out.println("删除的通知id" + id);
        notificationManager.cancel((int)id);
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("服务已销毁");
        super.onDestroy();
    }
}
