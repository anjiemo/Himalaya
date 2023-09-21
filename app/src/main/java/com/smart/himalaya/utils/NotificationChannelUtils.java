package com.smart.himalaya.utils;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import java.util.Iterator;
import java.util.List;

public class NotificationChannelUtils {

    private static String sPlayerChannelVersion = "#1";
    public static String sPlayerChannelId;
    public static String sPlayerChannelName;
    public static int sPlayerChannelImportance;
    private static boolean hasCreatedChannel;

    public NotificationChannelUtils() {
    }

    public static NotificationCompat.Builder newNotificationBuilder(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            createPlayerNotificationChannel(context);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, sPlayerChannelId);
            builder.setVibrate(new long[]{0L});
            builder.setSound((Uri)null);
            builder.setDefaults(0);
            return builder;
        } else {
            return new NotificationCompat.Builder(context);
        }
    }

    @TargetApi(26)
    private static void createPlayerNotificationChannel(Context context) {
        if (context != null) {
            if (!hasCreatedChannel) {
                NotificationManager manager = (NotificationManager)context.getSystemService("notification");
                if (manager != null) {
                    List<NotificationChannel> channels = manager.getNotificationChannels();
                    NotificationChannel old = null;
                    if (channels != null && !channels.isEmpty()) {
                        Iterator var4 = channels.iterator();

                        while(var4.hasNext()) {
                            NotificationChannel nc = (NotificationChannel)var4.next();
                            if (nc == null) {
                                return;
                            }

                            if (TextUtils.equals(sPlayerChannelName, nc.getName())) {
                                old = nc;
                                break;
                            }
                        }
                    }

                    if (old == null || !TextUtils.equals(sPlayerChannelId, old.getId())) {
                        if (old != null && !TextUtils.equals(sPlayerChannelId, old.getId())) {
                            manager.deleteNotificationChannel(old.getId());
                        }

                        NotificationChannel playerChannel = new NotificationChannel(sPlayerChannelId, sPlayerChannelName, sPlayerChannelImportance);
                        playerChannel.enableVibration(false);
                        playerChannel.setVibrationPattern(new long[]{0L});
                        playerChannel.setSound((Uri)null, (AudioAttributes)null);
                        manager.createNotificationChannel(playerChannel);
                        hasCreatedChannel = true;
                    }
                }
            }
        }
    }

    static {
        sPlayerChannelId = "player" + sPlayerChannelVersion;
        sPlayerChannelName = "通知栏";
        sPlayerChannelImportance = 2;
        hasCreatedChannel = false;
    }
}
