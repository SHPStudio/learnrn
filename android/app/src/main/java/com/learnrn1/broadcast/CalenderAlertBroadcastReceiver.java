package com.learnrn1.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.learnrn1.alarm.SystemAlarmManager;

import java.util.Date;
import java.util.Calendar;

/**
 * 日历提醒广播接收器
 */
public class CalenderAlertBroadcastReceiver extends BroadcastReceiver {
    private SystemAlarmManager systemAlarmManager = SystemAlarmManager.getInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "接收到提醒", Toast.LENGTH_SHORT).show();
        systemAlarmManager.setAlarm("测试", "天天", context);
        System.out.println("设置闹钟完成");
    }
}
