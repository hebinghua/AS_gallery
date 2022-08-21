package com.miui.gallery.util;

import android.content.Context;
import android.os.Build;
import android.text.BidiFormatter;
import android.text.TextUtils;
import com.miui.gallery.R;
import java.util.Locale;

/* loaded from: classes2.dex */
public final class FileSizeFormatter {

    /* loaded from: classes2.dex */
    public static class BytesResult {
        public final long roundedBytes;
        public final String units;
        public final String value;

        public BytesResult(String str, String str2, long j) {
            this.value = str;
            this.units = str2;
            this.roundedBytes = j;
        }
    }

    public static Locale localeFromContext(Context context) {
        if (Build.VERSION.SDK_INT >= 24) {
            return context.getResources().getConfiguration().getLocales().get(0);
        }
        return context.getResources().getConfiguration().locale;
    }

    public static String bidiWrap(Context context, String str) {
        return TextUtils.getLayoutDirectionFromLocale(localeFromContext(context)) == 1 ? BidiFormatter.getInstance(true).unicodeWrap(str) : str;
    }

    public static String formatShortFileSize(Context context, long j) {
        if (context == null) {
            return "";
        }
        BytesResult formatBytes = formatBytes(context.getResources(), j, 9);
        return bidiWrap(context, context.getString(R.string.fileSizeSuffix, formatBytes.value, formatBytes.units));
    }

    /* JADX WARN: Code restructure failed: missing block: B:42:0x0083, code lost:
        if ((r19 & 1) != 0) goto L61;
     */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00a1  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00c0  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x00d8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.util.FileSizeFormatter.BytesResult formatBytes(android.content.res.Resources r16, long r17, int r19) {
        /*
            Method dump skipped, instructions count: 238
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.FileSizeFormatter.formatBytes(android.content.res.Resources, long, int):com.miui.gallery.util.FileSizeFormatter$BytesResult");
    }
}
