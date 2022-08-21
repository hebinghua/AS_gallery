package com.miui.gallery.util;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.google.common.base.Splitter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes2.dex */
public class AccessibilityUtils {
    public static final ComponentName TALK_BACK = new ComponentName("com.google.android.marvin.talkback", "com.google.android.marvin.talkback.TalkBackService");
    public static final Splitter A11Y_SERVICES_SPLITTER = Splitter.on((char) CoreConstants.COLON_CHAR).omitEmptyStrings().trimResults();

    public static boolean isTalkBackEnabled(Context context) {
        return getEnabledA11yServices(context).contains(TALK_BACK);
    }

    public static Set<ComponentName> getEnabledA11yServices(Context context) {
        String string = Settings.Secure.getString(context.getContentResolver(), "enabled_accessibility_services");
        if (TextUtils.isEmpty(string)) {
            return Collections.emptySet();
        }
        HashSet hashSet = new HashSet();
        for (String str : A11Y_SERVICES_SPLITTER.split(string)) {
            ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
            if (unflattenFromString != null) {
                hashSet.add(unflattenFromString);
            }
        }
        return hashSet;
    }
}
