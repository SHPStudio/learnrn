package com.learnrn1.module.custom;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class CustomText extends ReactContextBaseJavaModule {
    public CustomText(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "CustomText";
    }

    @ReactMethod
    public void test(String key, String content, Promise promise) {
        System.out.println(String.format("key: %s, content: %s", key, content));
        promise.resolve(true);
    }
}
