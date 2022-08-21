package com.miui.gallery.push.messagehandler;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.push.GalleryPushMessage;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class FeatureRedDotMessageHandler extends MessageHandler {
    @Override // com.miui.gallery.push.messagehandler.MessageHandler
    public void handlePull(Context context, GalleryPushMessage galleryPushMessage) {
    }

    @Override // com.miui.gallery.push.messagehandler.MessageHandler
    public void handleDirect(Context context, GalleryPushMessage galleryPushMessage) {
        if (context == null) {
            DefaultLogger.d("FeatureRedDotMessageHandler", "context is null");
            return;
        }
        String data = galleryPushMessage.getData();
        if (data == null) {
            DefaultLogger.e("FeatureRedDotMessageHandler", "message data is null");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(galleryPushMessage.getData());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String optString = jSONObject.optString("start_time");
            String optString2 = jSONObject.optString("end_time");
            long time = TextUtils.isEmpty(optString) ? 0L : simpleDateFormat.parse(optString).getTime();
            long time2 = TextUtils.isEmpty(optString2) ? Long.MAX_VALUE : simpleDateFormat.parse(optString2).getTime();
            String optString3 = jSONObject.optString("feature_name");
            if (System.currentTimeMillis() > time2 || TextUtils.isEmpty(optString3)) {
                return;
            }
            GalleryPreferences.FeatureRedDot.setFeatureRedDotValidTime(optString3, time, time2);
            HashMap hashMap = new HashMap();
            hashMap.put("featureName", optString3);
            StatHelper.recordCountEvent("feature_red_dot", "push_red_dot_arrive", hashMap);
        } catch (Exception e) {
            DefaultLogger.e("FeatureRedDotMessageHandler", "fail to parse feature red dot data from %s", data, e);
        }
    }
}
