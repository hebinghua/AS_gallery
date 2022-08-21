package com.miui.gallery.push;

import android.text.TextUtils;

/* loaded from: classes2.dex */
public enum PushConstants$MessageScope {
    DEBUG("debug"),
    RELEASE("release");
    
    private final String value;

    PushConstants$MessageScope(String str) {
        this.value = str;
    }

    public static PushConstants$MessageScope getScope(String str) {
        PushConstants$MessageScope[] values;
        if (!TextUtils.isEmpty(str)) {
            for (PushConstants$MessageScope pushConstants$MessageScope : values()) {
                if (str.equals(pushConstants$MessageScope.value)) {
                    return pushConstants$MessageScope;
                }
            }
            return null;
        }
        return null;
    }
}
