package com.miui.gallery.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import androidx.tracing.Trace;
import com.android.internal.MediaCodecCompat;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class IncompatibleMediaType {
    public static final LazyValue<Void, Boolean> SUPPORT_HEVC = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.IncompatibleMediaType.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r1) {
            Trace.beginSection("checkHEVCCapability");
            try {
                return Boolean.valueOf(MediaCodecCompat.isSupportedHEVC());
            } finally {
                Trace.endSection();
            }
        }
    };
    public static final LazyValue<Void, Boolean> SUPPORT_8K = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.IncompatibleMediaType.2
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r1) {
            Trace.beginSection("check8KCapability");
            try {
                return Boolean.valueOf(MediaCodecCompat.isSupported8k());
            } finally {
                Trace.endSection();
            }
        }
    };

    public static boolean isUnsupportedMediaType(String str) {
        if (BaseFileMimeUtil.isRawFromMimeType(str)) {
            return Build.VERSION.SDK_INT < 23;
        } else if (!BaseFileMimeUtil.isHeifMimeType(str)) {
            return false;
        } else {
            return !SUPPORT_HEVC.get(null).booleanValue();
        }
    }

    public static boolean isUnsupported8kVideo() {
        return !SUPPORT_8K.get(null).booleanValue();
    }

    public static boolean isAutoConvertMediaType(String str) {
        return BaseFileMimeUtil.isHeifMimeType(str);
    }

    public static String getUnsupportedMediaDownloadingTip(Context context, String str) {
        String unsupportedMediaTypeDesc = unsupportedMediaTypeDesc(str);
        if (TextUtils.isEmpty(unsupportedMediaTypeDesc)) {
            return context.getString(R.string.unsupported_media_downloading_tip_common);
        }
        return context.getString(R.string.unsupported_media_downloading_tip_format, unsupportedMediaTypeDesc);
    }

    public static String getUnsupporedMediaViewTip(Context context, String str) {
        String unsupportedMediaTypeDesc = unsupportedMediaTypeDesc(str);
        return !TextUtils.isEmpty(unsupportedMediaTypeDesc) ? context.getString(R.string.unsupported_media_view_tip, unsupportedMediaTypeDesc) : "";
    }

    public static String unsupportedMediaTypeDesc(String str) {
        if (BaseFileMimeUtil.isRawFromMimeType(str)) {
            return "RAW";
        }
        if (!BaseFileMimeUtil.isHeifMimeType(str)) {
            return null;
        }
        return "HEIF";
    }
}
