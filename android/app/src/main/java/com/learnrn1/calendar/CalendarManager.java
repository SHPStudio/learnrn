package com.learnrn1.calendar;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.learnrn1.MainApplication;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 日历管理器
 */
public class CalendarManager {
    /**
     * 日历管理器单例实例
     */
    private static CalendarManager instance = new CalendarManager();

    /**
     * 日历ContentProvider Url
     */
    private static String CALANDER_URL = "content://com.android.calendar/calendars";
    /**
     * 日历事件ContentProvider Url
     */
    private static String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    /**
     * 日历提醒ContentProvider Url
     */
    private static String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";

    /**
     * 默认日历账户类型
     */
    private static String CALENDARS_ACCOUNT_TYPE = "com.android.exchange";

    /**
     * 禁止外部初始化
     */
    private CalendarManager() {

    }

    /**
     * 获取ContentResolver
     * @return
     */
    private ContentResolver getContentResolver() {
        return MainApplication.getContext().getContentResolver();
    }

    /**
     * 外部统一使用该方法获取单一实例
     * @return
     */
    public static CalendarManager getInstance() {
        return instance;
    }

    /**
     * 检查是否存在日历账户
     * 如果存在返回第一个账户
     * @return
     */
    private int checkCalendarAccount() {
        try (Cursor userCursor = getContentResolver().query(Uri.parse(CALANDER_URL), null, null, null, null)) {
            if (userCursor == null) {
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0) {
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            }else {
                return -1;
            }
        }
    }

    public int getAllAccountCount() {
        Cursor userCursor = getContentResolver().query(Uri.parse(CALANDER_URL), null, null, null, null);
        if (userCursor == null) {
            return 0;
        }
        return userCursor.getCount();
    }

    /**
     * 添加日历用户
     * @param name 日历用户名称
     * @param email 用户邮箱
     * @param calendarDisplayName 用户昵称
     * @return 成功返回新增日历用户id 否则返回-1表示失败
     */
    public long addAccount(String name, String email, String calendarDisplayName) {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, name);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, email);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, calendarDisplayName);
        // 显示日历上绑定的事件
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        // 允许访问日历全部功能
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        // 保存事件到本地
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, email);

        Uri calendarUri = Uri.parse(CALANDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, email)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    public int addCalendarEvent(CalendarEvent calendarEvent) {
        Calendar calendar = Calendar.getInstance();
        // 检查是否有日历账户
        int calId = checkCalendarAccount();
        System.out.println("检查账户: " + calId);
        if (calId < 0) {
            // 创建日历账户
            calId = (int)addAccount("calendar", "calendar@test.com", "测试日历账户");
            System.out.println("创建账户: " + calId);
            if (calId < 0) {
                // 创建失败
                return -1;
            }
        }
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.TITLE, calendarEvent.getTitle());
        event.put(CalendarContract.Events.CALENDAR_ID, calId);
        event.put(CalendarContract.Events.DESCRIPTION, calendarEvent.getDescription());
        event.put(CalendarContract.Events.DTSTART, calendarEvent.getStartTime());
        event.put(CalendarContract.Events.DTEND, calendarEvent.getEndTime());
        event.put(CalendarContract.Events.HAS_ALARM, 1);
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");
        event.put(CalendarContract.Events.CUSTOM_APP_PACKAGE, "com.shape");
        Uri newEvent = getContentResolver().insert(Uri.parse(CALANDER_EVENT_URL), event);
        if (newEvent == null) {
            Log.w("leanrn", String.format("CalendarManager.addCalendarEvent 添加日历事件失败, event:%s", JSON.toJSONString(calendarEvent)));
            return -2;
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        values.put(CalendarContract.Reminders.MINUTES, 0);
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = getContentResolver().insert(Uri.parse(CALANDER_REMIDER_URL), values);
        if (uri == null) {
            Log.w("leanrn", String.format("CalendarManager.addCalendarEvent 添加日历事件提醒失败, event:%s", values.toString()));
            return -3;
        }
        return 0;
    }

    public CalendarEvent getCalendarEventByAlarmTime(String alarmTime) {
        Cursor cursor = CalendarContract.Instances.query(getContentResolver(),null, Long.valueOf(alarmTime), Long.valueOf(alarmTime));
        if (cursor != null) {
            cursor.moveToFirst();
            String title = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.TITLE));
            String description = cursor.getString(cursor.getColumnIndex(CalendarContract.Instances.DESCRIPTION));
            int calId = cursor.getInt(cursor.getColumnIndex(CalendarContract.Instances.EVENT_ID));
            CalendarEvent event = new CalendarEvent();
            event.setCalEventId(calId);
            event.setDescription(description);
            event.setTitle(title);
            return event;
        }
        return null;
    }

    public void deleteOutDateCalendarEvent() {
//        Cursor cursor = CalendarContract.Instances.query(getContentResolver(), null, 0, Calendar.getInstance().getTimeInMillis());
        Cursor cursor = getContentResolver().query(Uri.parse(CALANDER_EVENT_URL), null, CalendarContract.Events.DTEND + "<" + Calendar.getInstance().getTimeInMillis() + " AND " + CalendarContract.Events.CUSTOM_APP_PACKAGE + "='com.shape'",null,null);
        if (cursor != null && cursor.getCount() > 0) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                int eventId = cursor.getInt(cursor.getColumnIndex(CalendarContract.Events._ID));
                deleteEventById(eventId);
                String custom = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.CUSTOM_APP_PACKAGE));
                System.out.println("event: " + eventId + " custom: " + custom);
            }
        }
    }

    private void deleteEventById(int id) {
        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALANDER_EVENT_URL), id);
        int row=  getContentResolver().delete(deleteUri, null, null);
        if (row <= 0) {
            System.out.println("删除事件: id" + id + "失败");
        }
    }

}
