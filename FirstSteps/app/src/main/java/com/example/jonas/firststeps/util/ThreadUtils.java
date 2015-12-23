package com.example.jonas.firststeps.util;

import timber.log.Timber;

public class ThreadUtils {

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Timber.e(e.getMessage(), e);
        }
    }
}
