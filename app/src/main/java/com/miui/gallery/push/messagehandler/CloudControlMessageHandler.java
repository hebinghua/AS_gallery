package com.miui.gallery.push.messagehandler;

import android.content.Context;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.cloudcontrol.CloudControlDBHelper;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.CloudControlRequestHelper;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.push.GalleryPushMessage;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import miuix.net.ConnectivityHelper;

/* loaded from: classes2.dex */
public class CloudControlMessageHandler extends MessageHandler {
    public static final long ONE_WEEK_MILLIS = TimeUnit.DAYS.toMillis(7);

    @Override // com.miui.gallery.push.messagehandler.MessageHandler
    public void handlePull(Context context, GalleryPushMessage galleryPushMessage) {
        if (galleryPushMessage == null) {
            return;
        }
        if (ConnectivityHelper.getInstance(context).isUnmeteredNetworkConnected()) {
            new CloudControlRequestHelper(context).execRequestSync();
            DefaultLogger.d("CloudControlMessageHandler", "Pull data trigger by push");
            return;
        }
        if (ConnectivityHelper.getInstance(context).isNetworkConnected()) {
            if (System.currentTimeMillis() - GalleryPreferences.CloudControl.getLastRequestSucceedTime() >= ONE_WEEK_MILLIS) {
                new CloudControlRequestHelper(context).execRequestSync();
                DefaultLogger.d("CloudControlMessageHandler", "Force pull data after one week");
                return;
            }
        }
        DefaultLogger.d("CloudControlMessageHandler", "Post as pending task");
        PendingTaskManager.getInstance().postTask(3, galleryPushMessage);
    }

    @Override // com.miui.gallery.push.messagehandler.MessageHandler
    public void handleDirect(Context context, GalleryPushMessage galleryPushMessage) {
        long tag = galleryPushMessage.getTag();
        long pushTag = GalleryPreferences.CloudControl.getPushTag();
        boolean z = false;
        if (tag <= pushTag) {
            DefaultLogger.e("CloudControlMessageHandler", "Current push tag is less than last push tag, skip handle");
            HashMap hashMap = new HashMap();
            hashMap.put("name", galleryPushMessage.getBusinessModule());
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.format(Locale.US, "%d_%d", Long.valueOf(tag), Long.valueOf(pushTag)));
            SamplingStatHelper.recordCountEvent("mipush", "push_tag_is_invalid", hashMap);
            return;
        }
        GalleryPreferences.CloudControl.setPushTag(tag);
        String data = galleryPushMessage.getData();
        try {
            ArrayList arrayList = (ArrayList) new GsonBuilder().registerTypeAdapter(FeatureProfile.class, new FeatureProfile.Deserializer()).create().fromJson(data, new TypeToken<ArrayList<FeatureProfile>>() { // from class: com.miui.gallery.push.messagehandler.CloudControlMessageHandler.1
            }.getType());
            if (!BaseMiscUtil.isValid(arrayList)) {
                return;
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                FeatureProfile featureProfile = (FeatureProfile) it.next();
                CloudControlManager.getInstance().insertToCache(featureProfile);
                if (CloudControlDBHelper.tryInsertToDB(context, featureProfile) == 0) {
                    DefaultLogger.e("CloudControlMessageHandler", "Persist failed: %s", String.valueOf(featureProfile));
                    z = true;
                }
            }
            if (!z) {
                return;
            }
            HashMap hashMap2 = new HashMap();
            hashMap2.put("result", data);
            SamplingStatHelper.recordCountEvent("cloud_control", "cloud_control_persist_error", hashMap2);
        } catch (Exception e) {
            DefaultLogger.e("CloudControlMessageHandler", "Parse direct content [%s] failed: \n%s", data, e);
        }
    }
}
