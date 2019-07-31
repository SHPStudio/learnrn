package com.learnrn1.module.calendar;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.learnrn1.calendar.CalendarManager;

public class CalendarModule extends ReactContextBaseJavaModule {
    private CalendarManager calendarManager = CalendarManager.getInstance();


    public CalendarModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CalendarModule";
    }

    @ReactMethod
    public void getAllAccount(Promise promise) {
        int count = calendarManager.getAllAccountCount();
        System.out.println(String.format("用户有: %s个", count));
        promise.resolve(count);
    }

    @ReactMethod
    public void addAccount(String name, String email, Promise promise) {
        double id = calendarManager.addAccount(name, email);
        promise.resolve(id);
    }
}
