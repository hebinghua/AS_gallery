package com.miui.gallery.util;

import android.os.Build;
import com.miui.gallery.net.library.LibraryStrategyUtils;
import com.miui.mediaeditor.utils.MediaEditorUtils;

/* loaded from: classes2.dex */
public class VideoPostEntranceUtils {
    public static boolean sLoaded;
    public static final String[] sWhiteList;

    static {
        String[] strArr = {"venus", "star", "mars", "haydn", "haydnin", "odin", "vili", "zeus", "cupid", "psyche", "ingres", "poussin", "munch", "thor", "loki", "zizhan", "mayfly", "unicorn", "diting"};
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
        return sLoaded && isPlatformSupport();
    }

    public static boolean isPlatformSupport() {
        if (LibraryStrategyUtils.is8450()) {
            return MediaEditorUtils.isMediaEditorAvailable() && MediaEditorUtils.isMediaEditorSupportVideoPostIn8450();
        }
        return true;
    }
}
