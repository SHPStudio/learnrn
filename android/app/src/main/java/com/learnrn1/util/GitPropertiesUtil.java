package com.learnrn1.util;

import com.squareup.okhttp.*;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.Set;

public class GitPropertiesUtil {
    private static Properties loadPropertiesFromGithub(String file) {
        final String url = "https://api.github.com/repos" + file;
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
                System.out.println("信息: " + response.message() + "错误: " + new String(response.body().bytes(), "UTF-8"));
                return null;
            }
            try(InputStream is = response.body().byteStream(); FileOutputStream fos = new FileOutputStream(file)) {
                Properties properties = new Properties();
                properties.load(is);
                return properties;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static void getProperties(String file, Class cls) {
        try {
            loadProperties2Cls(loadPropertiesFromGithub(file), cls);
        } catch (Exception e) {
        }
    }

    private static void loadProperties2Cls(Properties properties, Class cls) {
        Set<Object> keySet = properties.keySet();
        for(Object e: keySet) {
            try {
                String key = e.toString();
                Field field = cls.getDeclaredField(key);
                if (field != null) {
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
                }
            } catch (Exception e1) {
            }
        }
    }
}
