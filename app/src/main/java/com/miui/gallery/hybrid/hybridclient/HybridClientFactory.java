package com.miui.gallery.hybrid.hybridclient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.request.HostManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes2.dex */
public class HybridClientFactory {
    public static Set<String> sSupportedIntentSchemes;

    static {
        HashSet hashSet = new HashSet();
        sSupportedIntentSchemes = hashSet;
        hashSet.add("tel");
        sSupportedIntentSchemes.add("sip");
        sSupportedIntentSchemes.add("sms");
        sSupportedIntentSchemes.add("smsto");
        sSupportedIntentSchemes.add("mailto");
        sSupportedIntentSchemes.add("micloud");
    }

    public static Set<String> getSupportedIntentSchemes() {
        return Collections.unmodifiableSet(sSupportedIntentSchemes);
    }

    public static boolean isSupportedUrl(String str) {
        if (HostManager.isInternalUrl(str)) {
            return true;
        }
        return sSupportedIntentSchemes.contains(Uri.parse(str).getScheme());
    }

    public static HybridClient createHybridClient(Context context, Intent intent) {
        HybridClient hybridClient;
        String str;
        if (context != null && intent != null) {
            String action = intent.getAction();
            if ("android.intent.action.VIEW".equals(action)) {
                str = intent.getData().getQueryParameter(MapBundleKey.MapObjKey.OBJ_URL);
                if (TextUtils.isEmpty(str)) {
                    str = intent.getDataString();
                }
                hybridClient = new GalleryHybridClient(context, str);
            } else if ("com.miui.gallery.action.VIEW_WEB".equals(action)) {
                str = intent.getStringExtra(MapBundleKey.MapObjKey.OBJ_URL);
                hybridClient = new GalleryHybridClient(context, str);
            } else if ("com.miui.gallery.action.VIEW_WEB_RECOMMEND".equals(action)) {
                str = intent.getStringExtra(MapBundleKey.MapObjKey.OBJ_URL);
                hybridClient = new RecommendHybridClient(context, str);
            } else if ("com.miui.gallery.action.VIEW_WEB_DEVICE_ID".equals(action)) {
                str = intent.getStringExtra(MapBundleKey.MapObjKey.OBJ_URL);
                hybridClient = new DeviceIdHybridClient(context, str);
            } else if ("com.miui.gallery.action.VIEW_WEB_LOGIN".equals(action)) {
                str = intent.getStringExtra(MapBundleKey.MapObjKey.OBJ_URL);
                hybridClient = new LoginHybridClient(context, str);
            } else if ("com.miui.gallery.action.VIEW_WEB_OPERATION_STORY".equals(action)) {
                str = intent.getStringExtra(MapBundleKey.MapObjKey.OBJ_URL);
                hybridClient = new OperationHybridClient(context, str);
            } else {
                DefaultLogger.e("HybridClientFactory", "Not supported action %s", action);
                hybridClient = null;
                str = null;
            }
            if (str != null && isSupportedUrl(str)) {
                return hybridClient;
            }
            DefaultLogger.e("HybridClientFactory", "unsupported url: %s", str);
        }
        return null;
    }
}
