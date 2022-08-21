package com.miui.gallery.editor.photo.app.remover2.sdk;

import android.os.Build;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.editor.photo.utils.BigBitmapLoadUtils;

/* loaded from: classes2.dex */
public class Remover2CheckHelper {
    public static boolean sIsDeviceSupported;
    public static final String[] sWhiteList;

    static {
        String[] strArr = {"polaris", "dipper", "beryllium", "equuleus", "perseus", "ursa", "cepheus", "crux", "raphael", "cas", "umi", "cmi", "apollo", "lmipro", "lmiin", "lmiinpro", "star", "mars", "venus", "haydn", "alioth", "andromeda", "lmi", "cetus", "vili", "aliothin", "thyme", "vayu", "bhima", "argo", "enuma", "elish", "nabu", "zeus", "cupid", "psyche", "odin", "ingres", "poussin", "munch", "rubens", "matisse", "thor", "loki"};
        sWhiteList = strArr;
        sIsDeviceSupported = false;
        String str = Build.DEVICE;
        if (TextUtils.isEmpty(str) || BigBitmapLoadUtils.getPhoneTotalMem(GalleryApp.sGetAndroidContext()) <= 4509715660L) {
            return;
        }
        String[] split = str.split("_");
        if (split != null && split.length > 0) {
            str = split[0];
        }
        for (String str2 : strArr) {
            if (str2.equalsIgnoreCase(str)) {
                sIsDeviceSupported = true;
                return;
            }
        }
    }

    public static boolean isRemover2Support() {
        return sIsDeviceSupported;
    }
}
