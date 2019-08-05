package com.learnrn1.module.db;

import com.alibaba.fastjson.JSON;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.learnrn1.manager.UserDaoManager;
import com.learnrn1.model.User;

import java.util.List;

public class UserDaoModule extends ReactContextBaseJavaModule {
    private UserDaoManager userDaoManager = UserDaoManager.getInstance();

    public UserDaoModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "userDao";
    }

    @ReactMethod
    public void insertOrUpdateUser(String message, Promise promise) {
        System.out.println("message: " + message);
        User user = JSON.parseObject(message, User.class);
        long result = userDaoManager.insertOrReplace(user);
        promise.resolve(Double.longBitsToDouble(result));
    }

    @ReactMethod
    public void queryAll(Promise promise) {
        List<User> result = userDaoManager.queryAll();
        promise.resolve(JSON.toJSONString(result));
    }
}
