package com.learnrn1.module.calendar;

import android.database.Cursor;
import android.net.Uri;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class CalendarManage extends ReactContextBaseJavaModule {

    private static String CALANDER_URL = "content://com.android.calendar/calendars";
    private static String CALANDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALANDER_REMIDER_URL = "content://com.android.calendar/reminders";

    public CalendarManage(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CalendarManage";
    }

    @ReactMethod
    public void getAllAccount(Promise promise) {
        Cursor userCursor = getReactApplicationContext().getContentResolver().query(Uri.parse(CALANDER_URL), null, null, null, null);
        if (userCursor == null) {
            promise.resolve(null);
            return;
        }
        int count = userCursor.getCount();
        System.out.println(String.format("用户有: %s个", count));
        promise.resolve(count);
    }
}
