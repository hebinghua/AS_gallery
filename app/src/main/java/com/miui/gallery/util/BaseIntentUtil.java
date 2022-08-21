package com.miui.gallery.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class BaseIntentUtil {
    public static void selectLocalAudio(Activity activity, int i) {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        intent.setPackage("com.miui.player");
        try {
            activity.startActivityForResult(intent, i);
        } catch (ActivityNotFoundException unused) {
            DefaultLogger.e("BaseIntentUtil", "com.miui.player not found,try all picker");
            try {
                Intent intent2 = new Intent();
                intent2.setType("audio/*");
                intent2.setAction("android.intent.action.GET_CONTENT");
                activity.startActivityForResult(intent2, i);
            } catch (ActivityNotFoundException unused2) {
                DefaultLogger.e("BaseIntentUtil", "picker not found");
            }
        }
    }
}
