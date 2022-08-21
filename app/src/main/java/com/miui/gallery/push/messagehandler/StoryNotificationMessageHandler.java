package com.miui.gallery.push.messagehandler;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.miui.gallery.R;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.card.Card;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.cloud.card.SyncCardFromServer;
import com.miui.gallery.push.GalleryPushMessage;
import com.miui.gallery.push.messagehandler.StoryNotificationMessageHandler;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.micloudsdk.request.utils.CloudUtils;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class StoryNotificationMessageHandler extends MessageHandler {
    public static long mLastTime;

    @Override // com.miui.gallery.push.messagehandler.MessageHandler
    public void handlePull(Context context, GalleryPushMessage galleryPushMessage) {
    }

    @Override // com.miui.gallery.push.messagehandler.MessageHandler
    public void handleDirect(final Context context, GalleryPushMessage galleryPushMessage) {
        if (context == null || !MediaFeatureManager.isStoryGenerateEnable()) {
            DefaultLogger.d("StoryNotificationMessageHandler", "context is null");
            return;
        }
        String data = galleryPushMessage.getData();
        if (data == null) {
            DefaultLogger.e("StoryNotificationMessageHandler", "message data is null");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(galleryPushMessage.getData());
            final int pushNotificationId = getPushNotificationId(jSONObject.optInt("id", 0));
            if (jSONObject.optBoolean("isCancel", false)) {
                NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
                if (notificationManager == null) {
                    return;
                }
                notificationManager.cancel(pushNotificationId);
                DefaultLogger.d("StoryNotificationMessageHandler", "is cancel message");
                return;
            }
            long currentTimeMillis = System.currentTimeMillis();
            long j = currentTimeMillis - mLastTime;
            mLastTime = currentTimeMillis;
            if (j < 600000) {
                DefaultLogger.e("StoryNotificationMessageHandler", "too frequently to push notification, time duration is %d", Long.valueOf(j));
                return;
            }
            int optInt = jSONObject.optInt("version", 0);
            if (optInt > 0) {
                DefaultLogger.d("StoryNotificationMessageHandler", "message version is not fit, server version is %d, local version is %d", Integer.valueOf(optInt), 0);
                return;
            }
            long optLong = jSONObject.optLong("story_id", 0L);
            HashMap hashMap = new HashMap();
            hashMap.put("story_id", "story_" + optLong);
            SamplingStatHelper.recordCountEvent("assistant", "assistant_operation_card_push_recieved", hashMap);
            Card cardByServerId = CardManager.getInstance().getCardByServerId(String.valueOf(optLong), true);
            if (cardByServerId != null) {
                if (cardByServerId.isOutOfDate()) {
                    return;
                }
                publishStoryNotification(context, pushNotificationId, cardByServerId);
                return;
            }
            new SyncCardFromServer(CloudUtils.getXiaomiAccount()).getOperationCards(String.valueOf(optLong), new SyncCardFromServer.OperationCardCallback() { // from class: com.miui.gallery.push.messagehandler.StoryNotificationMessageHandler.1
                {
                    StoryNotificationMessageHandler.this = this;
                }

                @Override // com.miui.gallery.cloud.card.SyncCardFromServer.OperationCardCallback
                public void onOperationCardGet(Card card) {
                    if (card == null || card.isOutOfDate()) {
                        return;
                    }
                    StoryNotificationMessageHandler.this.publishStoryNotification(context, pushNotificationId, card);
                }
            });
        } catch (Exception e) {
            DefaultLogger.e("StoryNotificationMessageHandler", "fail to parse notification data from" + data, e);
        }
    }

    @TargetApi(23)
    public final void publishStoryNotification(Context context, int i, Card card) {
        if (context == null || card == null) {
            DefaultLogger.d("StoryNotificationMessageHandler", "No valid context or card!");
            return;
        }
        String title = card.getTitle();
        String description = card.getDescription();
        if (card.getOperationInfo() != null && card.getOperationInfo().getIconUrl() != null) {
            Glide.with(context).mo985asBitmap().mo963load(card.getOperationInfo().getIconUrl()).mo959listener(new AnonymousClass2(context, i, title, description)).submit();
            return;
        }
        publishStoryNotificationInternal(context, i, title, description, null);
    }

    /* renamed from: com.miui.gallery.push.messagehandler.StoryNotificationMessageHandler$2 */
    /* loaded from: classes2.dex */
    public class AnonymousClass2 implements RequestListener<Bitmap> {
        public final /* synthetic */ Context val$context;
        public final /* synthetic */ String val$description;
        public final /* synthetic */ int val$notificationId;
        public final /* synthetic */ String val$title;

        /* renamed from: $r8$lambda$nPf3HLzpCo__nk5gyvGS5iTw8-c */
        public static /* synthetic */ void m1253$r8$lambda$nPf3HLzpCo__nk5gyvGS5iTw8c(AnonymousClass2 anonymousClass2, Context context, int i, String str, String str2) {
            anonymousClass2.lambda$onLoadFailed$0(context, i, str, str2);
        }

        public static /* synthetic */ void $r8$lambda$pe4ykcCNG9oZUjy9OtF3tmONq6c(AnonymousClass2 anonymousClass2, Context context, int i, String str, String str2, Bitmap bitmap) {
            anonymousClass2.lambda$onResourceReady$1(context, i, str, str2, bitmap);
        }

        public AnonymousClass2(Context context, int i, String str, String str2) {
            StoryNotificationMessageHandler.this = r1;
            this.val$context = context;
            this.val$notificationId = i;
            this.val$title = str;
            this.val$description = str2;
        }

        public /* synthetic */ void lambda$onLoadFailed$0(Context context, int i, String str, String str2) {
            StoryNotificationMessageHandler.this.publishStoryNotificationInternal(context, i, str, str2, null);
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
            final Context context = this.val$context;
            final int i = this.val$notificationId;
            final String str = this.val$title;
            final String str2 = this.val$description;
            ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.push.messagehandler.StoryNotificationMessageHandler$2$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    StoryNotificationMessageHandler.AnonymousClass2.m1253$r8$lambda$nPf3HLzpCo__nk5gyvGS5iTw8c(StoryNotificationMessageHandler.AnonymousClass2.this, context, i, str, str2);
                }
            });
            return true;
        }

        public /* synthetic */ void lambda$onResourceReady$1(Context context, int i, String str, String str2, Bitmap bitmap) {
            StoryNotificationMessageHandler.this.publishStoryNotificationInternal(context, i, str, str2, bitmap);
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(final Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
            final Context context = this.val$context;
            final int i = this.val$notificationId;
            final String str = this.val$title;
            final String str2 = this.val$description;
            ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.push.messagehandler.StoryNotificationMessageHandler$2$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    StoryNotificationMessageHandler.AnonymousClass2.$r8$lambda$pe4ykcCNG9oZUjy9OtF3tmONq6c(StoryNotificationMessageHandler.AnonymousClass2.this, context, i, str, str2, bitmap);
                }
            });
            return true;
        }
    }

    public final void publishStoryNotificationInternal(Context context, int i, String str, String str2, Bitmap bitmap) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager == null) {
            DefaultLogger.d("StoryNotificationMessageHandler", "notification manager is null");
            return;
        }
        Intent intent = new Intent("com.miui.gallery.action.VIEW_ALBUM");
        intent.putExtra("extra_start_page", 2);
        PendingIntent activity = PendingIntent.getActivity(context, 0, intent, 67108864);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setTicker(str);
        builder.setContentTitle(str);
        builder.setContentText(str2);
        if (bitmap != null) {
            builder.setLargeIcon(bitmap);
        }
        builder.setSmallIcon(R.mipmap.ic_launcher_gallery);
        NotificationHelper.setLowChannel(context, builder);
        builder.setContentIntent(activity);
        Notification build = builder.build();
        build.flags = 16;
        notificationManager.notify(i, build);
        DefaultLogger.d("StoryNotificationMessageHandler", "publish notification %d success", Integer.valueOf(i));
    }

    public final int getPushNotificationId(int i) {
        return NotificationHelper.getPushNotificationId(i);
    }
}
