package com.shunya.fcmandroid.fcm;

import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shunya.fcmandroid.fcm.command.TestCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.shunya.fcmandroid.LogUtils.LOGD;
import static com.shunya.fcmandroid.LogUtils.LOGE;

public class MyFirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    private static final String ACTION = "action";
    private static final String EXTRA_DATA = "extraData";
    private static final Map<String, FcmCommand> MESSAGE_RECEIVERS;

    static {
        // Known messages and their FCM message receivers
        Map<String, FcmCommand> receivers = new HashMap<>();
        receivers.put("test", new TestCommand());
//      receivers.put("sync_schedule", new SyncCommand());
//      receivers.put("sync_user", new SyncUserCommand());
//      receivers.put("notification", new NotificationCommand());
//      receivers.put("feed_update", new FeedCommand());
        MESSAGE_RECEIVERS = Collections.unmodifiableMap(receivers);
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            // A map containing the action and extra data associated with that action.
            Map<String, String> data = remoteMessage.getData();
            // Handle received FCM data messages.
            String action = data.get(ACTION);
            String extraData = data.get(EXTRA_DATA);
            LOGD(TAG, "Got FCM message, " + ACTION + "=" + action + ", " + EXTRA_DATA + "=" + extraData);
            if (action == null) {
                LOGE(TAG, "Message received without command action");
                return;
            }
            //noinspection DefaultLocale
            action = action.toLowerCase();
            FcmCommand command = MESSAGE_RECEIVERS.get(action);
            if (command == null) {
                LOGE(TAG, "Unknown command received: " + action);
            } else {
                if (/* Check if data needs to be processed by long running job */ true) {
                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                    scheduleJob();
                } else {
                    // Handle message within 10 seconds
                    command.execute(this, action, extraData);
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    // [START on_new_token]
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(token);
    }
    // [END on_new_token]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }
}
