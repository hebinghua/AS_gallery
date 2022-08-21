package com.miui.gallery.video.editor.util;

import android.content.Intent;
import android.net.Uri;

/* loaded from: classes2.dex */
public class IntentUtil {
    public static Intent makeMarketIntent(String str, boolean z) {
        return new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + str + "&back=" + z));
    }
}
