package com.miui.gallery.push;

import android.text.TextUtils;

/* loaded from: classes2.dex */
public enum PushConstants$MessageType {
    SYNC("sync"),
    DIRECT("direct");
    
    private final String value;

    PushConstants$MessageType(String str) {
        this.value = str;
    }

    public static PushConstants$MessageType getType(String str) {
        PushConstants$MessageType[] values;
        if (!TextUtils.isEmpty(str)) {
            for (PushConstants$MessageType pushConstants$MessageType : values()) {
                if (str.equalsIgnoreCase(pushConstants$MessageType.value)) {
                    return pushConstants$MessageType;
                }
            }
            return null;
        }
        return null;
    }
}
