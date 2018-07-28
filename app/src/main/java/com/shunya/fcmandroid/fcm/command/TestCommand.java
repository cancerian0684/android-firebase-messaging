package com.shunya.fcmandroid.fcm.command;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.shunya.fcmandroid.MainActivity;
import com.shunya.fcmandroid.NotificationID;
import com.shunya.fcmandroid.R;
import com.shunya.fcmandroid.fcm.FcmCommand;

import static com.shunya.fcmandroid.LogUtils.LOGI;
import static com.shunya.fcmandroid.LogUtils.makeLogTag;

public class TestCommand extends FcmCommand {
    private static final String TAG = makeLogTag("TestCommand");

    @Override
    public void execute(Context context, String type, String payload) {
        LOGI(TAG, "Received FCM message: type=" + type + ", extraData=" + payload);
        Gson gson = new Gson();
        TestCommandModel model = gson.fromJson(payload, TestCommandModel.class);
        showNotification(context, model);
    }

    private void showNotification(Context context, TestCommandModel model) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int notificationId = NotificationID.getID();

        Log.i("NotificationID", "Id: " + notificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationId, intent, /*PendingIntent.FLAG_ONE_SHOT*/ 0);
        String channelId = context.getString(R.string.default_notification_channel_id);
//        Uri soundUri = getSoundUri(context.getApplicationContext());
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setTicker("uptime notification")
                        .setContentTitle("uptime notification")
//                        .setContentTitle(context.getString(R.string.app_name))
                        .setContentText("uptime notification")
                        .setAutoCancel(true)
                        .setSound(soundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(notificationId /* ID of notification */, notificationBuilder.build());
    }

    static class TestCommandModel {
        String format;
        String title;
        String messageBody;
    }
}