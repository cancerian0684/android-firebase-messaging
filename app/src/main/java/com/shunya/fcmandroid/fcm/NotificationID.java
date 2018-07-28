package com.shunya.fcmandroid.fcm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Harry Singh.
 */
public class NotificationID {
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);

    public static int getID() {
        return atomicInteger.incrementAndGet();
    }
}
