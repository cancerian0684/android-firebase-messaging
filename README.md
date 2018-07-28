# android-firebase-messaging

Android Firebase Messaging implementation using Command Design Pattern.


```java
public class ShunyaFcmService extends FirebaseMessagingService {
    
     @Override
     public void onMessageReceived(RemoteMessage remoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            
        } 
     }
     
     @Override
     public void onNewToken(String refreshedToken) {
         super.onNewToken(refreshedToken);
         Log.e("NEW_TOKEN",refreshedToken);
         // Get updated InstanceID token.
         // If you want to send messages to this application instance or
         // manage this apps subscriptions on the server side, send the
         // Instance ID token to your app server.
 
         //TODO: Send Token to Server
         //sendRegistrationToServer(getApplicationContext(), refreshedToken);
     }
}
```

Command Design Pattern, FCM Command defines behavior when FCM is received.

```java
public abstract class FcmCommand {

    /**
     * Defines behavior when FCM is received.
     */
    public abstract void execute(Context context, String type, String payload);
}
```

