package com.miui.gallery.util.deprecated;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.StaticContext;

/* loaded from: classes2.dex */
public class BaseDeprecatedPreference {
    public static SharedPreferences getDefaultPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences sGetDefaultPreferences() {
        return getDefaultPreferences(StaticContext.sGetAndroidContext());
    }

    public static SharedPreferences.Editor sGetDefaultEditor() {
        return sGetDefaultPreferences().edit();
    }

    public static boolean sCanConnectNetworkByImpunity() {
        return sGetDefaultPreferences().getBoolean(BaseGalleryPreferences.PrefKeys.CTA_CAN_CONNECT_NETWORK_BY_IMPUNITY, false);
    }
}
