package com.miui.gallery.video.editor.util;

import android.text.TextUtils;
import android.view.View;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ToolsUtil {
    public static void showView(View view) {
        if (view != null) {
            view.setVisibility(0);
        }
    }

    public static void hideView(View view) {
        if (view != null) {
            view.setVisibility(8);
        }
    }

    public static boolean isRTLDirection() {
        return TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
    }

    public static int parseIntFromStr(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return 0;
            }
            return Integer.parseInt(str.trim());
        } catch (Exception e) {
            DefaultLogger.d("ToolsUtil", "parseIntFromStr exception: %s ", e.getMessage());
            return 0;
        }
    }

    public static String getTrimedString(String str) {
        return TextUtils.isEmpty(str) ? "" : str.trim();
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x003b, code lost:
        r2 = r4.getInteger("frame-rate");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int getVideoFrameRate(java.lang.String r7) {
        /*
            java.lang.String r0 = "frame-rate"
            boolean r1 = android.text.TextUtils.isEmpty(r7)
            r2 = 0
            if (r1 == 0) goto La
            return r2
        La:
            android.media.MediaExtractor r1 = new android.media.MediaExtractor
            r1.<init>()
            r1.setDataSource(r7)     // Catch: java.lang.Throwable -> L44 java.io.IOException -> L46
            int r7 = r1.getTrackCount()     // Catch: java.lang.Throwable -> L44 java.io.IOException -> L46
            r3 = r2
        L17:
            if (r3 >= r7) goto L40
            android.media.MediaFormat r4 = r1.getTrackFormat(r3)     // Catch: java.lang.Throwable -> L44 java.io.IOException -> L46
            java.lang.String r5 = "mime"
            java.lang.String r5 = r4.getString(r5)     // Catch: java.lang.Throwable -> L44 java.io.IOException -> L46
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Throwable -> L44 java.io.IOException -> L46
            if (r6 != 0) goto L3d
            java.lang.String r6 = "video/"
            boolean r5 = r5.startsWith(r6)     // Catch: java.lang.Throwable -> L44 java.io.IOException -> L46
            if (r5 == 0) goto L3d
            boolean r5 = r4.containsKey(r0)     // Catch: java.lang.Throwable -> L44 java.io.IOException -> L46
            if (r5 == 0) goto L3d
            int r7 = r4.getInteger(r0)     // Catch: java.lang.Throwable -> L44 java.io.IOException -> L46
            r2 = r7
            goto L40
        L3d:
            int r3 = r3 + 1
            goto L17
        L40:
            r1.release()
            goto L53
        L44:
            r7 = move-exception
            goto L54
        L46:
            r7 = move-exception
            java.lang.String r0 = "ToolsUtil"
            java.lang.String r3 = "getVideoFrameRate: %s"
            java.lang.String r7 = r7.getMessage()     // Catch: java.lang.Throwable -> L44
            com.miui.gallery.util.logger.DefaultLogger.d(r0, r3, r7)     // Catch: java.lang.Throwable -> L44
            goto L40
        L53:
            return r2
        L54:
            r1.release()
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.video.editor.util.ToolsUtil.getVideoFrameRate(java.lang.String):int");
    }
}
