package com.miui.gallery.analytics;

import android.content.Context;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.xiaomi.onetrack.Configuration;
import com.xiaomi.onetrack.OneTrack;
import java.util.Map;

/* loaded from: classes.dex */
public class OneTrackHelper implements ITrackHelper {
    public static final Configuration CONFIGURATION = new Configuration.Builder().setAppId("31000000288").setChannel("com.miui.gallery").setMode(OneTrack.Mode.APP).setExceptionCatcherEnable(true).setUseCustomPrivacyPolicy(false).build();
    public static OneTrack mOneTrack;

    @Override // com.miui.gallery.analytics.ITrackHelper
    public void init(Context context) {
        mOneTrack = OneTrack.createInstance(context, CONFIGURATION);
        OneTrack.setDebugMode(false);
        OneTrack.setTestMode(false);
        OneTrack.registerCrashHook(context);
        OneTrack.setAccessNetworkEnable(context, BaseGalleryPreferences.CTA.canConnectNetwork());
    }

    @Override // com.miui.gallery.analytics.ITrackHelper
    public void track(String str, Map<String, Object> map) {
        mOneTrack.track(str, map);
    }
}
