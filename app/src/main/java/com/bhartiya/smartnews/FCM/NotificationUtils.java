package com.bhartiya.smartnews.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class NotificationUtils extends ContextWrapper {

    private NotificationManager mManager;
    public static final String ANDROID_CHANNEL_ID = "com.bhartiya.smartnews.ANDROID";
    public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationUtils(Context base) {
        super(base);
        createChannels();
    }

    /*
    * Create a notification channel
    * */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels() {

        // create android channel
        NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        // Sets whether notifications posted to this channel should display notification lights
        androidChannel.enableLights(true);
        // Sets whether notification posted to this channel should vibrate.
        androidChannel.enableVibration(true);
        // Sets the notification light color for notifications posted to this channel
        androidChannel.setLightColor(Color.GREEN);
        // Sets whether notifications posted to this channel appear on the lockscreen or not
        androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(androidChannel);
    }


    /*
    * Create push for android O device
    * */

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getAndroidChannelNotification(String title, String message, String image, String productId) {
        Bitmap remote_picture = null;
        NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
        notiStyle.setSummaryText(message);
        try {
            remote_picture = BitmapFactory.decodeStream((InputStream) new URL(image).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        notiStyle.bigPicture(remote_picture);
        PendingIntent contentIntent = null;
        Intent gotoIntent = new Intent();
        gotoIntent.putExtra("id", productId);
        gotoIntent.setClassName(getApplicationContext(), "com.bhartiya.smartnews.SplashScreen");//Start activity when user taps on notification.
        contentIntent = PendingIntent.getActivity(getApplicationContext(),
                (int) (Math.random() * 100), gotoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(android.R.drawable.stat_notify_more)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(contentIntent)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(remote_picture)
                        .setBigContentTitle(title))
                ;
    }


    /*
    * Method to get notification manager
    * */
    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }


}