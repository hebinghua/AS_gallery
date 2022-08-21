package com.miui.gallery.push.messagehandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.push.GalleryPushMessage;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class NotificationMessageHandler extends MessageHandler {
    public static long mLastTime;

    @Override // com.miui.gallery.push.messagehandler.MessageHandler
    public void handlePull(Context context, GalleryPushMessage galleryPushMessage) {
    }

    @Override // com.miui.gallery.push.messagehandler.MessageHandler
    public void handleDirect(Context context, GalleryPushMessage galleryPushMessage) {
        String str;
        if (context == null) {
            DefaultLogger.d("NotificationMessageHandler", "context is null");
            return;
        }
        String data = galleryPushMessage.getData();
        if (data == null) {
            DefaultLogger.e("NotificationMessageHandler", "message data is null");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(galleryPushMessage.getData());
            int pushNotificationId = getPushNotificationId(jSONObject.optInt("id", 0));
            NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
            if (notificationManager == null) {
                DefaultLogger.d("NotificationMessageHandler", "notification manager is null");
            } else if (jSONObject.optBoolean("isCancel", false)) {
                notificationManager.cancel(pushNotificationId);
                DefaultLogger.d("NotificationMessageHandler", "is cancel message");
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                long j = currentTimeMillis - mLastTime;
                mLastTime = currentTimeMillis;
                if (j < 600000) {
                    DefaultLogger.e("NotificationMessageHandler", "too frequently to push notification, time duration is %d", Long.valueOf(j));
                    return;
                }
                int optInt = jSONObject.optInt("version", 0);
                if (optInt > 0) {
                    DefaultLogger.d("NotificationMessageHandler", "message version is not fit, server version is %d, local version is %d", Integer.valueOf(optInt), 0);
                    return;
                }
                String optString = jSONObject.optString(MapBundleKey.MapObjKey.OBJ_URL);
                if (TextUtils.isEmpty(optString)) {
                    DefaultLogger.e("NotificationMessageHandler", "empty url");
                    return;
                }
                Uri parse = Uri.parse(optString);
                Intent intent = new Intent("com.miui.gallery.action.PUSH_LANDING");
                if (parse.getBooleanQueryParameter("is_activity_action", false)) {
                    DefaultLogger.d("NotificationMessageHandler", "is activity action");
                    String optString2 = jSONObject.optString("intent_package_name");
                    if (!TextUtils.isEmpty(optString2)) {
                        intent.setPackage(optString2);
                    }
                    JSONObject optJSONObject = jSONObject.optJSONObject("intent_extra");
                    if (optJSONObject != null) {
                        Iterator<String> keys = optJSONObject.keys();
                        while (keys.hasNext()) {
                            String next = keys.next();
                            intent.putExtra(next, optJSONObject.optString(next));
                        }
                    }
                    if (intent.resolveActivity(context.getPackageManager()) == null) {
                        DefaultLogger.d("NotificationMessageHandler", "no activity to handle this uri");
                        return;
                    }
                }
                JSONObject optJSONObject2 = jSONObject.optJSONObject("title");
                JSONObject optJSONObject3 = jSONObject.optJSONObject("description");
                String str2 = null;
                if (optJSONObject2 == null || optJSONObject3 == null) {
                    str = null;
                } else {
                    String lowerCase = Locale.getDefault().toString().toLowerCase();
                    String optString3 = optJSONObject2.optString(lowerCase);
                    str = optJSONObject3.optString(lowerCase);
                    str2 = optString3;
                }
                if (!TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str)) {
                    Intent intent2 = new Intent("android.intent.action.VIEW", GalleryContract.Common.URI_PUSH_LANDING_PAGE);
                    intent2.setPackage("com.miui.gallery");
                    intent2.putExtra("notification_content_intent", intent);
                    intent2.putExtra("notification_content_id", pushNotificationId);
                    PendingIntent activity = PendingIntent.getActivity(context, 0, intent2, 201326592);
                    Notification.Builder builder = new Notification.Builder(context);
                    builder.setTicker(str2);
                    builder.setContentTitle(str2);
                    builder.setContentText(str);
                    builder.setSmallIcon(R.mipmap.ic_launcher_gallery);
                    builder.setContentIntent(activity);
                    NotificationHelper.setLowChannel(context, builder);
                    Notification build = builder.build();
                    build.flags = 16;
                    notificationManager.notify(pushNotificationId, build);
                    StatHelper.recordCountEvent("push_notification", "push_notification_arrive");
                    return;
                }
                DefaultLogger.e("NotificationMessageHandler", "no title or description");
            }
        } catch (Exception e) {
            DefaultLogger.e("NotificationMessageHandler", "fail to parse notification data from" + data, e);
        }
    }

    public final int getPushNotificationId(int i) {
        return NotificationHelper.getPushNotificationId(i);
    }
}
