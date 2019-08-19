package com.learnrn1.util;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.learnrn1.MainApplication;

/**
 * 系统工具类
 */
public class SystemUtil {
    
    /**
     * 获取系统多线程合适的线程数
     * @return
     */
    public static int getSuitaleProcessors() {
        return (Runtime.getRuntime().availableProcessors() * 2) + 1;
    }

    /**
     * 获取应用名称
     * @return
     */
    public static String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo;
        try {
            packageManager = MainApplication.getContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(MainApplication.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }
}
