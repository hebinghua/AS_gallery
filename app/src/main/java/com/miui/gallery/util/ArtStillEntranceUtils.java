package com.miui.gallery.util;

import android.os.Build;

/* loaded from: classes2.dex */
public class ArtStillEntranceUtils {
    public static boolean sLoaded;
    public static final String[] sWhiteList;

    static {
        String[] strArr = {"venus", "star", "mars", "haydn", "haydnin", "odin", "vili", "polaris", "dipper", "beryllium", "equuleus", "perseus", "ursa", "cepheus", "crux", "raphael", "cas", "umi", "cmi", "apollo", "lmipro", "lmiin", "lmiinpro", "alioth", "andromeda", "lmi", "aliothin", "thyme", "vayu", "bhima", "zeus", "cupid", "psyche", "enuma", "elish", "nabu", "ingres", "poussin", "munch", "thor", "loki", "zizhan", "zijin", "taoyao", "renoir", "mayfly", "unicorn", "diting"};
        sWhiteList = strArr;
        sLoaded = false;
        for (String str : strArr) {
            if (Build.DEVICE.equals(str)) {
                sLoaded = true;
                return;
            }
        }
    }

    public static boolean isAvailable() {
        return sLoaded;
    }
}
