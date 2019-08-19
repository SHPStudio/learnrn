package com.learnrn1.util;

public class FileUtil {

    public static String getFileNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
