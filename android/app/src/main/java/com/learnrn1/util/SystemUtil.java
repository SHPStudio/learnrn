package com.learnrn1.util;

public class SystemUtil {
    public static int getSuitaleProcessors() {
        return (Runtime.getRuntime().availableProcessors() * 2) + 1;
    }
}
