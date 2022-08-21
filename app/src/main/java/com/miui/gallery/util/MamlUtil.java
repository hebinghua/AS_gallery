package com.miui.gallery.util;

import android.content.Context;
import com.miui.gallery.preference.GalleryPreferences;

/* loaded from: classes2.dex */
public class MamlUtil {
    public static boolean isSupportMaml() {
        return GalleryPreferences.Maml.getMamlAssertsVersion() > 0;
    }

    public static void initMamlAssert(Context context) {
        if (!shouldUpdateAssertFile()) {
            return;
        }
        copyMamlResourcesFromAssets(context, "maml", context.getFilesDir().getAbsolutePath() + "/maml_resources");
    }

    public static boolean shouldUpdateAssertFile() {
        return 1 > GalleryPreferences.Maml.getMamlAssertsVersion();
    }

    /* JADX WARN: Removed duplicated region for block: B:61:0x00c4 A[Catch: IOException -> 0x00c0, TRY_LEAVE, TryCatch #7 {IOException -> 0x00c0, blocks: (B:57:0x00bc, B:61:0x00c4), top: B:70:0x00bc }] */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00bc A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void copyMamlResourcesFromAssets(android.content.Context r10, java.lang.String r11, java.lang.String r12) {
        /*
            Method dump skipped, instructions count: 207
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.MamlUtil.copyMamlResourcesFromAssets(android.content.Context, java.lang.String, java.lang.String):void");
    }
}
