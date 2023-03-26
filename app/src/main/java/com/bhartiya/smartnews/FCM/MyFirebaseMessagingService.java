package com.bhartiya.smartnews.FCM;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bhartiya.smartnews.R;
import com.bhartiya.smartnews.SplashScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    String value, type;
    int count = 0;
    NotificationManager notificationManager;
    String placeImage, placeId, placeTitle, placeMessage;
    String random_id;
    public static int NOTIFICATION_ID = 0;
    private NotificationUtils mNotificationUtils;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            handleDataMessage(remoteMessage.getData());
        } else if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

    }

    private void handleNotification(String message) {
        Intent resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
        resultIntent.putExtra("id", "");
        // check for image attachment
        sendNotification("", message, "", resultIntent);
    }

    private void handleDataMessage(Map<String, String> data) {

        placeImage = data.get("image");
        placeTitle = data.get("title");
        placeMessage = data.get("message");
        placeId = data.get("news_id");
        random_id = data.get("random_id");
        //Log.d("placeId", placeId);
        if (NOTIFICATION_ID != Integer.parseInt(random_id)) {
            NOTIFICATION_ID = Integer.parseInt(random_id);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                mNotificationUtils = new NotificationUtils(this);
                Notification.Builder nb = mNotificationUtils
                        .getAndroidChannelNotification(placeTitle, placeMessage, placeImage, placeId);
                mNotificationUtils.getManager().notify(102, nb.build());
            } else {
                handleMessage(getApplicationContext());
            }

        }
    }


    private void sendNotification(String placeMessage, String title, String placeImage, Intent resultIntent) {
        int requestID = (int) System.currentTimeMillis();


        PendingIntent intent =
                PendingIntent.getActivity(getApplicationContext(), requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mNotifyBuilder;
        NotificationManager mNotificationManager;

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setPriority(1);
            mNotifyBuilder.setSmallIcon(R.drawable.app_icon);

        } else {
            // Lollipop specific setColor method goes here.
            mNotifyBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle(title)
                    .setColor(Color.WHITE)
                    .setPriority(1);

            mNotifyBuilder.setSmallIcon(R.drawable.app_icon);
            mNotifyBuilder.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.app_icon)).getBitmap());

        }


        // Set pending intent

        // Set Vibrate, Sound and Light
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        mNotifyBuilder.setDefaults(defaults);
        // Set autocancel
        mNotifyBuilder.setAutoCancel(true);
        mNotifyBuilder.setContentIntent(intent);
        // Post a notification
        mNotificationManager.notify(requestID, mNotifyBuilder.build());
    }


    @SuppressWarnings("deprecation")
    private void handleMessage(Context mContext) {
        Bitmap remote_picture = null;
        int icon = R.drawable.app_icon;
        //if message and image url
        if (placeMessage != null && placeImage != null) {
            try {


                Log.v("TAG_MESSAGE", "" + placeMessage);
                Log.v("TAG_IMAGE", "" + placeImage);
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                notificationManager.cancel(NOTIFICATION_ID);

                NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
                notiStyle.setSummaryText(placeMessage);

                try {
                    remote_picture = BitmapFactory.decodeStream((InputStream) new URL(placeImage).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notiStyle.bigPicture(remote_picture);
                notificationManager = (NotificationManager) mContext
                        .getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent contentIntent = null;

                Intent gotoIntent = new Intent();
                gotoIntent.putExtra("id", placeId);
                gotoIntent.setClassName(mContext, "com.bhartiya.smartnews.SplashScreen");//Start activity when user taps on notification.
                contentIntent = PendingIntent.getActivity(mContext,
                        (int) (Math.random() * 100), gotoIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                        mContext);
                Notification notification = mBuilder.setSmallIcon(icon).setTicker(placeTitle).setWhen(0)
                        .setLargeIcon(((BitmapDrawable) getResources().getDrawable(icon)).getBitmap())
                        .setAutoCancel(true)
                        .setContentTitle(placeTitle)
                        .setPriority(1)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(placeMessage))
                        .setContentIntent(contentIntent)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

                        .setContentText(placeMessage)
                        .setStyle(notiStyle).build();


                notification.flags = Notification.FLAG_AUTO_CANCEL;
                count++;
                notificationManager.notify(count, notification);//This will generate seperate notification each time server sends.

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}