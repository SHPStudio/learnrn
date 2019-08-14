package com.learnrn1.util;

import com.squareup.okhttp.*;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;

public class GitPropertiesUtil {
    private static Properties loadPropertiesFromGithub(String file) {
        final String url = "https://api.github.com/repos/SHPStudio/document/contents/" + file;
        final Request request = new Request.Builder()
                .url(url)
                .header("Accept", "application/vnd.github.v3.raw")
                .header("Authorization", "")
                .build();
        OkHttpClient client = new OkHttpClient()
                .setFollowSslRedirects(true);
        final Call call = client.newCall(request);
        try {
            Response response = call.execute();
            if (!response.isSuccessful()) {
                System.out.println("信息: " + response.message() + "错误: " + new String(response.body().bytes(), StandardCharsets.UTF_8));
                return null;
            }
            try (InputStream is = response.body().byteStream()) {
                Properties properties = new Properties();
                properties.load(is);
                return properties;
            }
        } catch (Exception e) {
            System.out.println("请求信息错误:" + e);
            return null;
        }
    }

    public static void getProperties() {
        try {
            System.out.println("开始请求");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        loadProperties2Cls(loadPropertiesFromGithub(SystemUtil.getApplicationName() + ".properties"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
        }
    }

    private static void loadProperties2Cls(Properties properties) {
        Set<Object> keySet = properties.keySet();
        for (Object e : keySet) {
            try {
                String key = e.toString();
                Field field = AppConfig.class.getDeclaredField(key);
                if (Modifier.isStatic(field.getModifiers())) {
                    if (field.getType() == int.class) {
                        field.setInt(null, Integer.valueOf(properties.get(e).toString()));
                    }
                    if (field.getType() == boolean.class) {
                        field.setBoolean(null, Boolean.valueOf(properties.get(e).toString()));
                    }
                    if (field.getType() == String.class) {
                        field.set(null, properties.get(e));
                    }
                    if (field.getType() == double.class) {
                        field.setDouble(null, Double.valueOf(properties.get(e).toString()));
                    }
                    if (field.getType() == long.class) {
                        field.setLong(null, Long.valueOf(properties.get(e).toString()));
                    }
                }

            } catch (Exception e1) {
            }
        }
    }
}
