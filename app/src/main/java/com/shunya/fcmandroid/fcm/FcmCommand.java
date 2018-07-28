package com.shunya.fcmandroid.fcm;

import android.content.Context;

/**
 * Represents the client response when an FCM ping is received. Each type of FCM ping should have
 * an FcmCommand implementation associated with it.
 */
public abstract class FcmCommand {

    /**
     * Defines behavior when FCM is received.
     */
    public abstract void execute(Context context, String type, String payload);
}