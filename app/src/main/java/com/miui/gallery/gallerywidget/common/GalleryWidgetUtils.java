package com.miui.gallery.gallerywidget.common;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.SystemClock;
import android.text.TextUtils;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.gallerywidget.CustomWidgetProvider2_2;
import com.miui.gallery.gallerywidget.CustomWidgetProvider2_3;
import com.miui.gallery.gallerywidget.CustomWidgetProvider4_2;
import com.miui.gallery.gallerywidget.CustomWidgetProvider4_4;
import com.miui.gallery.gallerywidget.RecommendWidgetProvider2_2;
import com.miui.gallery.gallerywidget.RecommendWidgetProvider2_3;
import com.miui.gallery.gallerywidget.RecommendWidgetProvider4_2;
import com.miui.gallery.gallerywidget.RecommendWidgetProvider4_4;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.util.GlideLoadingUtils;
import com.miui.gallery.picker.PickGalleryActivity;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.MemoryPreferenceHelper;
import com.miui.gallery.sdk.download.DownloadOptions;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.sdk.download.ImageDownloader;
import com.miui.gallery.sdk.download.assist.DownloadedItem;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class GalleryWidgetUtils {
    public static final Map<String, Long> mLastPictureUpdateTimeMap = new HashMap();
    public static final Map<Integer, Long> mLastServiceRequestTimeMap = new HashMap();

    public static String convertArrayToString(Iterable iterable) {
        return iterable == null ? "" : TextUtils.join(",GALLERY,", iterable);
    }

    public static Long[] convertStringToLongArray(String str) {
        Long[] lArr = null;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            String[] split = str.split(",GALLERY,");
            if (split != null) {
                lArr = new Long[split.length];
                for (int i = 0; i < split.length; i++) {
                    lArr[i] = Long.valueOf(Long.parseLong(split[i]));
                }
            }
        } catch (Exception e) {
            DefaultLogger.e("GalleryWidgetUtils", "convertStringToLongArray error:" + e.getMessage());
        }
        return lArr;
    }

    public static float[] convertStringToFloatArray(String str, String str2) {
        float[] fArr = null;
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            try {
                String[] split = str.split(str2);
                if (split != null) {
                    fArr = new float[split.length];
                    for (int i = 0; i < split.length; i++) {
                        fArr[i] = Float.parseFloat(split[i]);
                    }
                }
            } catch (Exception e) {
                DefaultLogger.e("GalleryWidgetUtils", "convertStringToFloatArray error:" + e.getMessage());
            }
        }
        return fArr;
    }

    public static String[] convertStringToStringArray(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return str.split(",GALLERY,");
    }

    public static List<String> floatArrayToList(float[] fArr) {
        ArrayList arrayList = new ArrayList();
        if (fArr != null) {
            for (float f : fArr) {
                arrayList.add(String.valueOf(f));
            }
        }
        return arrayList;
    }

    public static boolean isStringInvalid(String str) {
        return TextUtils.isEmpty(str) || "null".equals(str);
    }

    public static String getDataListString(List<String> list) {
        return list == null ? "" : TextUtils.join(",GALLERY,", list);
    }

    public static List<String> getDataList(String str) {
        String[] split;
        ArrayList arrayList = new ArrayList();
        if (!TextUtils.isEmpty(str) && (split = str.split(",GALLERY,")) != null) {
            arrayList.addAll(Arrays.asList(split));
        }
        return arrayList;
    }

    public static String getCropInfoString(float[] fArr) {
        return (fArr == null || fArr.length != 4) ? "null" : TextUtils.join("_GALLERY_", floatArrayToList(fArr));
    }

    public static float[] getCropInfo(String str) {
        if (isStringInvalid(str)) {
            return new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        }
        float[] fArr = null;
        try {
            String[] split = str.split("_GALLERY_");
            if (split != null && split.length == 4) {
                fArr = new float[split.length];
                for (int i = 0; i < split.length; i++) {
                    fArr[i] = Float.parseFloat(split[i]);
                }
            }
        } catch (Exception e) {
            DefaultLogger.e("GalleryWidgetUtils", "getCropInfo error:" + e.getMessage());
        }
        return fArr;
    }

    public static String getMatrixValueString(Matrix matrix) {
        if (matrix == null) {
            return "null";
        }
        float[] fArr = new float[9];
        matrix.getValues(fArr);
        return TextUtils.join("_GALLERY_", floatArrayToList(fArr));
    }

    public static Matrix getMatrix(String str) {
        float[] convertStringToFloatArray;
        if (!isStringInvalid(str) && (convertStringToFloatArray = convertStringToFloatArray(str, "_GALLERY_")) != null && convertStringToFloatArray.length == 9) {
            Matrix matrix = new Matrix();
            matrix.setValues(convertStringToFloatArray);
            return matrix;
        }
        return null;
    }

    public static String downloadImage(long j, DownloadType downloadType) {
        if (!BaseNetworkUtils.isNetworkConnected() || (downloadType == DownloadType.ORIGIN && BaseNetworkUtils.isActiveNetworkMetered())) {
            DefaultLogger.e("GalleryWidgetUtils", "network invalid.");
            return null;
        } else if (AccountCache.getAccount() == null) {
            DefaultLogger.e("GalleryWidgetUtils", "no account.");
            return null;
        } else {
            DownloadedItem loadSync = ImageDownloader.getInstance().loadSync(CloudUriAdapter.getDownloadUri(j), downloadType, new DownloadOptions.Builder().setManual(true).setRequireWLAN(false).setRequirePower(false).setRequireDeviceStorage(false).setRequireCharging(false).build(), null);
            if (loadSync == null) {
                return null;
            }
            return loadSync.getFilePath();
        }
    }

    public static void updateRecommendWidgetStatus(String str, long j) {
        ComponentName componentName = new ComponentName(StaticContext.sGetAndroidContext(), RecommendWidgetProvider2_2.class);
        ComponentName componentName2 = new ComponentName(StaticContext.sGetAndroidContext(), RecommendWidgetProvider2_3.class);
        ComponentName componentName3 = new ComponentName(StaticContext.sGetAndroidContext(), RecommendWidgetProvider4_2.class);
        ComponentName componentName4 = new ComponentName(StaticContext.sGetAndroidContext(), RecommendWidgetProvider4_4.class);
        int[] appWidgetIds = AppWidgetManager.getInstance(StaticContext.sGetAndroidContext()).getAppWidgetIds(componentName);
        int[] appWidgetIds2 = AppWidgetManager.getInstance(StaticContext.sGetAndroidContext()).getAppWidgetIds(componentName2);
        int[] appWidgetIds3 = AppWidgetManager.getInstance(StaticContext.sGetAndroidContext()).getAppWidgetIds(componentName3);
        int[] appWidgetIds4 = AppWidgetManager.getInstance(StaticContext.sGetAndroidContext()).getAppWidgetIds(componentName4);
        if (appWidgetIds.length > 0 || appWidgetIds2.length > 0 || appWidgetIds3.length > 0 || appWidgetIds4.length > 0) {
            Intent intent = new Intent();
            intent.putExtra("from_card_status_change", true);
            intent.putExtra("card_status_change_type", str);
            intent.putExtra("card_status_change_card_id", j);
            intent.setAction("miui.appwidget.action.APPWIDGET_UPDATE");
            intent.setPackage(StaticContext.sGetAndroidContext().getPackageName());
            StaticContext.sGetAndroidContext().sendBroadcast(intent);
            DefaultLogger.d("GalleryWidgetUtils", "---log---updateRecommendWidgetStatus sendBroadcast finish cardId> %d", Long.valueOf(j));
        }
    }

    public static void updateCustomWidgetStatus(long[] jArr) throws IllegalArgumentException {
        if (jArr == null) {
            return;
        }
        if (jArr.length > 20) {
            throw new IllegalArgumentException("localPicIds over limit for sendBroadcast !");
        }
        ComponentName componentName = new ComponentName(StaticContext.sGetAndroidContext(), CustomWidgetProvider2_2.class);
        ComponentName componentName2 = new ComponentName(StaticContext.sGetAndroidContext(), CustomWidgetProvider2_3.class);
        ComponentName componentName3 = new ComponentName(StaticContext.sGetAndroidContext(), CustomWidgetProvider4_2.class);
        ComponentName componentName4 = new ComponentName(StaticContext.sGetAndroidContext(), CustomWidgetProvider4_4.class);
        int[] appWidgetIds = AppWidgetManager.getInstance(StaticContext.sGetAndroidContext()).getAppWidgetIds(componentName);
        int[] appWidgetIds2 = AppWidgetManager.getInstance(StaticContext.sGetAndroidContext()).getAppWidgetIds(componentName2);
        int[] appWidgetIds3 = AppWidgetManager.getInstance(StaticContext.sGetAndroidContext()).getAppWidgetIds(componentName3);
        int[] appWidgetIds4 = AppWidgetManager.getInstance(StaticContext.sGetAndroidContext()).getAppWidgetIds(componentName4);
        if (appWidgetIds.length <= 0 && appWidgetIds2.length <= 0 && appWidgetIds3.length <= 0 && appWidgetIds4.length <= 0) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("miui.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("from_pic_status_change", true);
        intent.putExtra("pic_status_change_pic_ids", jArr);
        intent.setPackage(StaticContext.sGetAndroidContext().getPackageName());
        StaticContext.sGetAndroidContext().sendBroadcast(intent);
        DefaultLogger.d("GalleryWidgetUtils", "---log---updateCustomWidgetStatus sendBroadcast finish");
    }

    /* renamed from: com.miui.gallery.gallerywidget.common.GalleryWidgetUtils$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize;

        static {
            int[] iArr = new int[IWidgetProviderConfig.WidgetSize.values().length];
            $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize = iArr;
            try {
                iArr[IWidgetProviderConfig.WidgetSize.SIZE_2_2.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_2_3.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_4_2.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_4_4.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public static void updateCustomWidgetStatus(int i, IWidgetProviderConfig.WidgetSize widgetSize) {
        ComponentName componentName;
        int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[widgetSize.ordinal()];
        if (i2 == 1) {
            componentName = new ComponentName(StaticContext.sGetAndroidContext(), CustomWidgetProvider2_2.class);
        } else if (i2 == 2) {
            componentName = new ComponentName(StaticContext.sGetAndroidContext(), CustomWidgetProvider2_3.class);
        } else if (i2 == 3) {
            componentName = new ComponentName(StaticContext.sGetAndroidContext(), CustomWidgetProvider4_2.class);
        } else {
            componentName = i2 != 4 ? null : new ComponentName(StaticContext.sGetAndroidContext(), CustomWidgetProvider4_4.class);
        }
        if (componentName == null || i < 0) {
            return;
        }
        Intent intent = new Intent();
        intent.setAction("miui.appwidget.action.APPWIDGET_UPDATE");
        intent.putExtra("appWidgetId", i);
        intent.putExtra("from_custom_editor", true);
        intent.setPackage(StaticContext.sGetAndroidContext().getPackageName());
        intent.setComponent(componentName);
        StaticContext.sGetAndroidContext().sendBroadcast(intent);
        DefaultLogger.d("GalleryWidgetUtils", "---log---updateCustomWidgetStatus FROM_CUSTOM_EDITOR sendBroadcast finish");
    }

    public static int getWidgetCount(Class<? extends AppWidgetProvider> cls) {
        int[] appWidgetIds = AppWidgetManager.getInstance(StaticContext.sGetAndroidContext()).getAppWidgetIds(new ComponentName(StaticContext.sGetAndroidContext(), cls));
        if (appWidgetIds == null) {
            return 0;
        }
        return appWidgetIds.length;
    }

    public static int getPicOrientation(String str) {
        try {
            return new ExifInterface(str).getAttributeInt("Orientation", 1);
        } catch (IOException e) {
            e.printStackTrace();
            return 1;
        }
    }

    public static RectF getRectWithOrientation(RectF rectF, int i) {
        return ExifUtil.adjustRectOrientation(1, 1, rectF, i, false);
    }

    public static RequestOptions getWidgetGlideOptions(String str, RectF rectF, int i) {
        return GlideOptions.bigPhotoOf().decodeRegion(RegionConfig.of(getRectWithOrientation(rectF, getPicOrientation(str)))).mo970override(i).mo952downsample(DownsampleStrategy.CENTER_INSIDE).mo950diskCacheStrategy(DiskCacheStrategy.RESOURCE);
    }

    public static Bitmap getBitmapFitOrientation(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return null;
        }
        return TransformationUtils.rotateImageExif(Glide.get(GalleryApp.sGetAndroidContext()).getBitmapPool(), bitmap, i);
    }

    public static Bitmap getCropBitmapFitOrientation(String str, RectF rectF, int i) {
        Bitmap blockingLoad = GlideLoadingUtils.blockingLoad(Glide.with(GalleryApp.sGetAndroidContext()), GalleryModel.of(str), getWidgetGlideOptions(str, rectF, i));
        if (blockingLoad != null) {
            DefaultLogger.d("GalleryWidgetUtils", "---log---getCropBitmap blockingLoad  width:%d , height:%d>", Integer.valueOf(blockingLoad.getWidth()), Integer.valueOf(blockingLoad.getHeight()));
        }
        Bitmap bitmapFitOrientation = getBitmapFitOrientation(blockingLoad, getPicOrientation(str));
        if (bitmapFitOrientation != null) {
            DefaultLogger.d("GalleryWidgetUtils", "---log---getCropBitmap getBitmapFitOrientation  width:%d , height:%d>", Integer.valueOf(bitmapFitOrientation.getWidth()), Integer.valueOf(bitmapFitOrientation.getHeight()));
        }
        return bitmapFitOrientation;
    }

    public static Bitmap getWidgetRoundedBitmap(Bitmap bitmap, float f, float f2, float f3) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float f4 = width / f;
        float f5 = height / f2;
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-16777216);
        canvas.drawRoundRect(rectF, f4 * f3, f3 * f5, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    public static Bitmap getFitWidgetRoundedBitmap(Bitmap bitmap, float f, float f2, float f3) {
        if (bitmap == null) {
            return null;
        }
        RectF rectF = new RectF(0.0f, 0.0f, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF2 = new RectF(0.0f, 0.0f, f, f2);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(rectF2, rectF, Matrix.ScaleToFit.CENTER);
        matrix.mapRect(rectF2);
        float width = rectF2.width() / f;
        float height = rectF2.height() / f2;
        Bitmap createBitmap = Bitmap.createBitmap((int) rectF2.width(), (int) rectF2.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Rect rect = new Rect(0, 0, (int) rectF2.width(), (int) rectF2.height());
        RectF rectF3 = new RectF(rect);
        Rect rect2 = new Rect((int) rectF2.left, (int) rectF2.top, (int) rectF2.right, (int) rectF2.bottom);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-16777216);
        canvas.drawRoundRect(rectF3, width * f3, f3 * height, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect2, rect, paint);
        return createBitmap;
    }

    public static IWidgetProviderConfig.WidgetSize getWidgetSize(int i) {
        IWidgetProviderConfig.WidgetSize[] values;
        for (IWidgetProviderConfig.WidgetSize widgetSize : IWidgetProviderConfig.WidgetSize.values()) {
            if (widgetSize.getValue() == i) {
                return widgetSize;
            }
        }
        return IWidgetProviderConfig.WidgetSize.SIZE_2_2;
    }

    public static int getGlideOverrideSize(IWidgetProviderConfig.WidgetSize widgetSize) {
        int max;
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[widgetSize.ordinal()];
        if (i == 1) {
            max = Math.max(GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.gallery_widget_2_2_min_width), GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.gallery_widget_2_2_min_height));
        } else if (i == 2) {
            max = Math.max(GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.gallery_widget_2_3_min_width), GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.gallery_widget_2_3_min_height));
        } else if (i == 3) {
            max = Math.max(GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.gallery_widget_4_2_min_width), GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.gallery_widget_4_2_min_height));
        } else {
            max = Math.max(GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.gallery_widget_4_4_min_width), GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(R.dimen.gallery_widget_4_4_min_height));
        }
        return max * 2;
    }

    public static Intent getPickGalleryIntent(Context context) {
        Intent intent = new Intent(context, PickGalleryActivity.class);
        intent.setType("image/*");
        intent.putExtra("pick-lower-bound", 1);
        intent.putExtra("pick-upper-bound", 6);
        intent.putExtra("extra_filter_media_type", new String[]{"image/gif", "image/x-adobe-dng"});
        intent.putExtra("pick-need-origin", true);
        intent.putExtra("extra_from_type", 1015);
        return intent;
    }

    public static String getPictureUpdateKey(int i, String str) {
        return i + "_" + str;
    }

    public static void setPictureUpdateTime(int i, int i2, String str) {
        String pictureUpdateKey = getPictureUpdateKey(i, str);
        if (i2 != -1) {
            Map<String, Long> map = mLastPictureUpdateTimeMap;
            map.remove(pictureUpdateKey);
            map.put(getPictureUpdateKey(i2, str), Long.valueOf(SystemClock.elapsedRealtime()));
        } else {
            mLastPictureUpdateTimeMap.put(pictureUpdateKey, Long.valueOf(SystemClock.elapsedRealtime()));
        }
        DefaultLogger.d("GalleryWidgetUtils", "---log---setPictureUpdateTime  key :%s", pictureUpdateKey);
    }

    public static boolean isPictureUpdateFrequent(int i, int i2, String str) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        String pictureUpdateKey = getPictureUpdateKey(i, str);
        Map<String, Long> map = mLastPictureUpdateTimeMap;
        boolean z = false;
        if (map.containsKey(pictureUpdateKey)) {
            if (elapsedRealtime - map.get(pictureUpdateKey).longValue() < AbstractComponentTracker.LINGERING_TIMEOUT) {
                z = true;
            }
            if (z) {
                DefaultLogger.d("GalleryWidgetUtils", "---log---isPictureUpdateFrequent ture key :%s", pictureUpdateKey);
            }
        }
        if (i2 != -1) {
            map.remove(pictureUpdateKey);
            map.put(getPictureUpdateKey(i2, str), Long.valueOf(elapsedRealtime));
        } else {
            map.put(pictureUpdateKey, Long.valueOf(elapsedRealtime));
        }
        return z;
    }

    public static void setServiceRequestTime(int i) {
        mLastServiceRequestTimeMap.put(Integer.valueOf(i), Long.valueOf(SystemClock.elapsedRealtime()));
    }

    public static boolean isServiceRequestFrequent(int i) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Map<Integer, Long> map = mLastServiceRequestTimeMap;
        boolean z = false;
        if (map.containsKey(Integer.valueOf(i))) {
            if (elapsedRealtime - map.get(Integer.valueOf(i)).longValue() < 500) {
                z = true;
            }
            if (z) {
                DefaultLogger.d("GalleryWidgetUtils", "---log---isServiceRequestFrequent ture key :%d", Integer.valueOf(i));
            }
        }
        return z;
    }

    public static boolean isStoryFunctionOn() {
        if (MemoryPreferenceHelper.getInt("widget_story_function_status", -1) == -1) {
            MemoryPreferenceHelper.putInt("widget_story_function_status", GalleryPreferences.Assistant.isStoryFunctionOn() ? 1 : 0);
        }
        return MemoryPreferenceHelper.getInt("widget_story_function_status", -1) == 1;
    }

    public static boolean isMiuiHomeSupported(Context context) {
        try {
            return context.getPackageManager().getPackageInfo("com.miui.home", 0).versionCode > 424000000;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
