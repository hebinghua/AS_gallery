package com.miui.gallery.security;

import android.text.TextUtils;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class RiskController {
    public static void handleDataDeleted(String str) {
        DefaultLogger.d("RiskController", "handle delete message %s", str);
        DataDeletedMessage convert = DataDeletedMessage.convert(str, false);
        if (convert == null) {
            DefaultLogger.w("RiskController", "delete message covert error");
        } else if (!convert.isValid()) {
            DefaultLogger.w("RiskController", "delete message has expired");
        } else {
            String str2 = GalleryPreferences.UUID.get();
            if (TextUtils.isEmpty(convert.getDeviceTag()) || TextUtils.equals(convert.getDeviceTag(), str2)) {
                return;
            }
            DefaultLogger.d("RiskController", "valid deleting trigger");
            GalleryPreferences.RiskControl.markDelete(str);
        }
    }

    public static void notify(GalleryActivity galleryActivity) {
        DataDeletedHelper.notifyDataDeleted(galleryActivity);
    }
}
