package com.miui.extraphoto.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.uil.SpecialPhotoLoadUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class ExtraPhotoSDK {
    public static final DeviceSupportRefocus DEVICE_SUPPORT_REFOCUS = new DeviceSupportRefocus();
    public static final DeviceSupportMotionPhoto DEVICE_SUPPORT_MOTION_PHOTO = new DeviceSupportMotionPhoto();
    public static final DeviceSupportDocPhoto DEVICE_SUPPORT_DOC_PHOTO = new DeviceSupportDocPhoto();
    public static final DeviceSupportCorrectDocument DEVICE_SUPPORT_CORRECT_DOCUMENT = new DeviceSupportCorrectDocument();
    public static final DeviceSupportWatermark DEVICE_SUPPORT_WATERMARK = new DeviceSupportWatermark();

    public static String getType(long j) {
        if ((32 & j) > 0) {
            return "motion";
        }
        if ((1 & j) > 0) {
            return "refocus";
        }
        if ((16384 & j) > 0) {
            return "subtitle";
        }
        if ((32768 & j) > 0) {
            return "tags";
        }
        if ((65536 & j) > 0) {
            return "doc_photo";
        }
        if ((j & 17592186044416L) <= 0) {
            return null;
        }
        return "watermark";
    }

    public static void sendResultStatic(Intent intent) {
        HashMap hashMap = new HashMap();
        if (intent == null) {
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "NONE");
            sendResultStatic(hashMap);
            return;
        }
        String stringExtra = intent.getStringExtra("save_type");
        hashMap.put(stringExtra, intent.getStringExtra("save_explain"));
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, stringExtra);
        sendResultStatic(hashMap);
    }

    public static void sendResultStatic(Map<String, String> map) {
        SamplingStatHelper.recordCountEvent("photo_extra", "refocus_save", map);
    }

    public static void sendExposureStatic() {
        SamplingStatHelper.recordCountEvent("photo_extra", "refocus_exposure");
    }

    public static void sendRefocusEnterStatic() {
        SamplingStatHelper.recordCountEvent("photo_extra", "refocus_enter");
    }

    public static void sendEnterStatic(long j) {
        sendTypedPhotoStatic("extra_photo_enter", j);
    }

    public static void sendDeletePhotoStatic(long j) {
        sendTypedPhotoStatic("extra_photo_delete", j);
    }

    public static void sendSharePhotoStatic(long j) {
        sendTypedPhotoStatic("extra_photo_share", j);
    }

    public static void sendNewPhotoStatic(long j) {
        sendTypedPhotoStatic("extra_photo_new", j);
    }

    public static void sendTypedPhotoStatic(String str, long j) {
        String type;
        if (j <= 0 || TextUtils.isEmpty(str) || (type = getType(j)) == null) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, type);
        SamplingStatHelper.recordCountEvent("photo_extra", str, hashMap);
    }

    public static float[] calculateGifMatrixForMotionPhoto(ImageView imageView) {
        Drawable drawable;
        int i;
        int i2;
        if (imageView == null || (drawable = imageView.getDrawable()) == null) {
            return null;
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
            return null;
        }
        float max = 500.0f / Math.max(intrinsicWidth, intrinsicHeight);
        float f = intrinsicWidth;
        int i3 = (int) (max * f);
        int i4 = (int) (max * intrinsicHeight);
        int width = imageView.getWidth();
        int height = imageView.getHeight();
        float followOriginalResolutionBaseScale = SpecialPhotoLoadUtil.getFollowOriginalResolutionBaseScale("image/gif", i3, i4);
        float f2 = i3;
        float f3 = i4;
        float f4 = width;
        float f5 = height;
        if (f2 / f3 >= f4 / f5) {
            i2 = (int) (followOriginalResolutionBaseScale * f4);
            i = (int) ((i4 * i2) / f2);
        } else {
            i = (int) (followOriginalResolutionBaseScale * f5);
            i2 = (int) ((i3 * i) / f3);
        }
        DefaultLogger.d("ExtraPhotoSDK", "gif display width: %d,display height: %d", Integer.valueOf(i2), Integer.valueOf(i));
        Matrix matrix = new Matrix();
        float f6 = i2 / f;
        matrix.postScale(f6, f6);
        matrix.postTranslate((width - i2) / 2.0f, (height - i) / 2.0f);
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        return fArr;
    }

    public static boolean isDeviceSupportRefocus(Context context) {
        return !BuildUtil.isBlackShark() && DEVICE_SUPPORT_REFOCUS.get(context).booleanValue();
    }

    public static boolean isDeviceSupportMotionPhoto(Context context) {
        return !BaseBuildUtil.isInternational() && DEVICE_SUPPORT_MOTION_PHOTO.get(context).booleanValue();
    }

    public static boolean isDeviceSupportDocPhoto(Context context) {
        return DEVICE_SUPPORT_DOC_PHOTO.get(context).booleanValue();
    }

    public static boolean isDeviceSupportCorrectDocument(Context context) {
        return !BuildUtil.isBlackShark() && DEVICE_SUPPORT_CORRECT_DOCUMENT.get(context).booleanValue();
    }

    public static boolean isDeviceSupportWatermark(Context context) {
        return DEVICE_SUPPORT_WATERMARK.get(context).booleanValue();
    }

    /* loaded from: classes.dex */
    public static class DeviceSupportRefocus extends LazyValue<Context, Boolean> {
        public DeviceSupportRefocus() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            Intent intent = new Intent("com.miui.extraphoto.action.VIEW_ADVANCED_REFOCUS");
            intent.setPackage("com.miui.extraphoto");
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 65536);
            return Boolean.valueOf(queryIntentActivities != null && !queryIntentActivities.isEmpty());
        }
    }

    /* loaded from: classes.dex */
    public static class DeviceSupportMotionPhoto extends LazyValue<Context, Boolean> {
        public DeviceSupportMotionPhoto() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            Intent intent = new Intent("com.miui.extraphoto.action.MOTION_PHOTO_REPICK");
            intent.setPackage("com.miui.extraphoto");
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 65536);
            return Boolean.valueOf(queryIntentActivities != null && !queryIntentActivities.isEmpty());
        }
    }

    /* loaded from: classes.dex */
    public static class DeviceSupportDocPhoto extends LazyValue<Context, Boolean> {
        public DeviceSupportDocPhoto() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            Intent intent = new Intent("com.miui.extraphoto.action.VIEW_DOCUMENT_PHOTO");
            intent.setPackage("com.miui.extraphoto");
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 65536);
            return Boolean.valueOf(queryIntentActivities != null && !queryIntentActivities.isEmpty());
        }
    }

    /* loaded from: classes.dex */
    public static class DeviceSupportCorrectDocument extends LazyValue<Context, Boolean> {
        public DeviceSupportCorrectDocument() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            Intent intent = new Intent("com.miui.extraphoto.action.CORRECT_DOCUMENT");
            intent.setPackage("com.miui.extraphoto");
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 65536);
            boolean z = queryIntentActivities != null && !queryIntentActivities.isEmpty();
            DefaultLogger.d("ExtraPhotoSDK", "[ExtraPhoto] [Doc] support correct document: %b", Boolean.valueOf(z));
            return Boolean.valueOf(z);
        }
    }

    /* loaded from: classes.dex */
    public static class DeviceSupportWatermark extends LazyValue<Context, Boolean> {
        public DeviceSupportWatermark() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            return Boolean.TRUE;
        }
    }
}
