package com.miui.gallery.video.editor.manager;

import android.os.Build;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class SmartVideoJudgeManager {
    public static boolean sLoaded;
    public static final String[] sWhiteList;

    static {
        String[] strArr = {"merlin", "merlinin", "merlinnfc", "monet", "monetin", "vangogh", "joyeuse", "excalibur", "durandal", "curtanacn", "curtana", "picassoin", "picasso", "lmi", "lmipro", "lmiin", "lmiinpro", "umi", "cmi", "dipper", "ursa", "equuleus", "perseus", "sirius", "lavender", "cepheus", "violet", "onc", "grus", "pyxis", "raphael", "davinci", "davinciin", "raphaelin", "vela", "crux", "tucana", "ginkgo", "willow", "draco", "phoenix", "phoenixin", "toco", "tocoin", "lancelot", "atom", "bomb", "gram", "galahad", "apollo", "shiva", "cas"};
        sWhiteList = strArr;
        sLoaded = false;
        String str = Build.DEVICE;
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("_");
            if (split != null && split.length > 0) {
                str = split[0];
            }
            for (String str2 : strArr) {
                if (str2.equalsIgnoreCase(str)) {
                    sLoaded = true;
                    return;
                }
            }
        }
    }

    public static boolean isAvailable() {
        return sLoaded;
    }
}
