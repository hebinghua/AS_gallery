package com.miui.gallery.gallerywidget.common;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.widget.RemoteViews;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.ui.CustomDispatchActivity;
import com.miui.gallery.gallerywidget.ui.RecommendDispatchActivity;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes2.dex */
public final class WidgetInstallManager {
    public static boolean isUseSmallLayout() {
        return !GalleryWidgetUtils.isMiuiHomeSupported(GalleryApp.sGetAndroidContext());
    }

    /* renamed from: com.miui.gallery.gallerywidget.common.WidgetInstallManager$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize;

        static {
            int[] iArr = new int[IWidgetProviderConfig.WidgetSize.values().length];
            $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize = iArr;
            try {
                iArr[IWidgetProviderConfig.WidgetSize.SIZE_4_4.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_4_2.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_2_3.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_2_2.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public static Pair<Float, Float> getSmallLayoutSize(Context context, IWidgetProviderConfig.WidgetSize widgetSize) {
        float dimension;
        float dimension2;
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[widgetSize.ordinal()];
        if (i == 1) {
            dimension = context.getResources().getDimension(R.dimen.gallery_widget_small_4_4_min_width);
            dimension2 = context.getResources().getDimension(R.dimen.gallery_widget_small_4_4_min_height);
        } else if (i == 2) {
            dimension = context.getResources().getDimension(R.dimen.gallery_widget_small_4_2_min_width);
            dimension2 = context.getResources().getDimension(R.dimen.gallery_widget_small_4_2_min_height);
        } else if (i == 3) {
            dimension = context.getResources().getDimension(R.dimen.gallery_widget_small_2_3_min_width);
            dimension2 = context.getResources().getDimension(R.dimen.gallery_widget_small_2_3_min_height);
        } else {
            dimension = context.getResources().getDimension(R.dimen.gallery_widget_small_2_2_min_width);
            dimension2 = context.getResources().getDimension(R.dimen.gallery_widget_small_2_2_min_height);
        }
        return new Pair<>(Float.valueOf(dimension), Float.valueOf(dimension2));
    }

    public static int getRecommendWidgetReplaceId(IWidgetProviderConfig.WidgetSize widgetSize) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[widgetSize.ordinal()];
        return i != 1 ? i != 2 ? i != 3 ? R.drawable.widget_recommend_replace_2_2 : R.drawable.widget_recommend_replace_2_3 : R.drawable.widget_recommend_replace_4_2 : R.drawable.widget_recommend_replace_4_4;
    }

    public static void setCustomWidgetPlaceView(RemoteViews remoteViews, IWidgetProviderConfig.WidgetSize widgetSize) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[widgetSize.ordinal()];
        if (i == 2) {
            remoteViews.setImageViewResource(R.id.image_view, R.drawable.widget_custom_replace_4_2);
        } else if (i == 3) {
            remoteViews.setImageViewResource(R.id.image_view, R.drawable.widget_custom_replace_2_3);
        } else if (i == 4) {
            remoteViews.setImageViewResource(R.id.image_view, R.drawable.widget_custom_replace_2_2);
        } else {
            remoteViews.setImageViewResource(R.id.image_view, R.drawable.widget_custom_replace_4_4);
        }
    }

    public static RemoteViews getCustomWidgetRemoteViews(Context context, IWidgetProviderConfig.WidgetSize widgetSize) {
        boolean isUseSmallLayout = isUseSmallLayout();
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[widgetSize.ordinal()];
        return new RemoteViews(context.getPackageName(), i != 2 ? i != 3 ? i != 4 ? isUseSmallLayout ? R.layout.custom_widget_content_small_4_4 : R.layout.custom_widget_content_4_4 : isUseSmallLayout ? R.layout.custom_widget_content_small_2_2 : R.layout.custom_widget_content_2_2 : isUseSmallLayout ? R.layout.custom_widget_content_small_2_3 : R.layout.custom_widget_content_2_3 : isUseSmallLayout ? R.layout.custom_widget_content_small_4_2 : R.layout.custom_widget_content_4_2);
    }

    public static RemoteViews getRecommendRemoteViews(Context context, IWidgetProviderConfig.WidgetSize widgetSize) {
        boolean isUseSmallLayout = isUseSmallLayout();
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[widgetSize.ordinal()];
        return new RemoteViews(context.getPackageName(), i != 2 ? i != 3 ? i != 4 ? isUseSmallLayout ? R.layout.recommend_widget_small_content4_4 : R.layout.recommend_widget_content4_4 : isUseSmallLayout ? R.layout.recommend_widget_small_content2_2 : R.layout.recommend_widget_content2_2 : isUseSmallLayout ? R.layout.recommend_widget_small_content2_3 : R.layout.recommend_widget_content2_3 : isUseSmallLayout ? R.layout.recommend_widget_small_content4_2 : R.layout.recommend_widget_content4_2);
    }

    public static void setCustomWidgetEmpty(Context context, int i, int i2, IWidgetProviderConfig.WidgetSize widgetSize) {
        DefaultLogger.d("WidgetInstallManager", "---log---setCustomWidgetEmpty appWidgetId > %d", Integer.valueOf(i));
        if (i2 != -1) {
            i = i2;
        }
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews customWidgetRemoteViews = getCustomWidgetRemoteViews(context, widgetSize);
        setCustomWidgetPlaceView(customWidgetRemoteViews, widgetSize);
        Intent intent = new Intent(context, CustomDispatchActivity.class);
        intent.putExtra("gallery_app_widget_id", i);
        intent.putExtra("gallery_app_widget_size_value", widgetSize.getValue());
        int i3 = Box.MAX_BOX_SIZE;
        if (BaseBuildUtil.isAboveAndroidS()) {
            i3 = 167772160;
        }
        customWidgetRemoteViews.setOnClickPendingIntent(R.id.layout_root, PendingIntent.getActivity(context, i, intent, i3));
        Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(i);
        appWidgetOptions.putString("miuiEditUri", getCustomOptionsUriString(i, widgetSize));
        if (appWidgetOptions.getBoolean("miuiLargeScreenDevice", false)) {
            DefaultLogger.d("WidgetInstallManager", "---log---large screen device");
            Bundle bundle = new Bundle();
            bundle.putInt("miuiWidgetId", i);
            customWidgetRemoteViews.setBundle(R.id.layout_root, "supportLargeScreenEditPreviewMode", bundle);
        }
        if (i2 != -1) {
            appWidgetOptions.putBoolean("miuiIdChangedComplete", true);
        }
        appWidgetManager.updateAppWidgetOptions(i, appWidgetOptions);
        appWidgetManager.updateAppWidget(i, customWidgetRemoteViews);
        WidgetStatisticsHelper.statisticsCustomWidgetStatus(i, widgetSize, "selectedimages_null", 0);
    }

    public static void setCustomWidget(Context context, int i, int i2, IWidgetProviderConfig.WidgetSize widgetSize, Bitmap bitmap, String str, String str2, String str3, int i3, int i4, int i5) {
        if (i2 != -1) {
            i = i2;
        }
        RemoteViews customWidgetRemoteViews = getCustomWidgetRemoteViews(context, widgetSize);
        customWidgetRemoteViews.setImageViewBitmap(R.id.image_view, bitmap);
        Intent intent = new Intent(context, CustomDispatchActivity.class);
        intent.putExtra("gallery_app_widget_id", i);
        intent.putExtra("gallery_app_widget_size_value", widgetSize.getValue());
        intent.putExtra("selected_pic_path", str);
        intent.putExtra("selected_pic_path_list", str2);
        intent.putExtra("selected_pic_id", str3);
        intent.putExtra("selected_photo_count", i3);
        intent.putExtra("selected_current_index", i4);
        int i6 = Box.MAX_BOX_SIZE;
        if (BaseBuildUtil.isAboveAndroidS()) {
            i6 = 167772160;
        }
        customWidgetRemoteViews.setOnClickPendingIntent(R.id.layout_root, PendingIntent.getActivity(context, i, intent, i6));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(i);
        appWidgetOptions.putString("miuiEditUri", getCustomOptionsUriString(i, widgetSize));
        if (appWidgetOptions.getBoolean("miuiLargeScreenDevice", false)) {
            DefaultLogger.d("WidgetInstallManager", "---log---large screen device");
            Bundle bundle = new Bundle();
            bundle.putInt("miuiWidgetId", i);
            customWidgetRemoteViews.setBundle(R.id.layout_root, "supportLargeScreenEditPreviewMode", bundle);
        }
        if (i2 != -1) {
            appWidgetOptions.putBoolean("miuiIdChangedComplete", true);
        }
        appWidgetManager.updateAppWidgetOptions(i, appWidgetOptions);
        appWidgetManager.updateAppWidget(i, customWidgetRemoteViews);
        WidgetStatisticsHelper.statisticsCustomWidgetStatus(i, widgetSize, "selectedimages_ture", i5);
    }

    public static String getCustomOptionsUriString(int i, IWidgetProviderConfig.WidgetSize widgetSize) {
        return new Uri.Builder().scheme("custom").authority("com.miui.gallery").path("dispatch").appendQueryParameter("appWidgetId", String.valueOf(i)).appendQueryParameter("gallery_app_widget_size", String.valueOf(widgetSize.getValue())).appendQueryParameter("miuiWidgetId", String.valueOf(i)).build().toString();
    }

    public static void initRecommendWidget(Context context, AppWidgetManager appWidgetManager, int i, int i2, IWidgetProviderConfig.WidgetSize widgetSize, boolean z, boolean z2) {
        if (!z || z2) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), i2);
            remoteViews.setImageViewResource(R.id.image_view, getRecommendWidgetReplaceId(widgetSize));
            if (z) {
                remoteViews.setTextViewText(R.id.text_title, context.getResources().getString(R.string.widget_recommend_default_title_on_story));
                remoteViews.setTextViewText(R.id.text_desc, context.getResources().getString(R.string.widget_recommend_default_description_shop));
            } else {
                remoteViews.setTextViewText(R.id.text_title, context.getResources().getString(R.string.widget_recommend_default_title_open_story));
                remoteViews.setTextViewText(R.id.text_desc, context.getResources().getString(R.string.widget_recommend_default_description_open_story));
                WidgetStatisticsHelper.statisticsRecommendWidgetStatus(i, widgetSize, "memories_false");
            }
            Intent intent = new Intent(context, RecommendDispatchActivity.class);
            intent.putExtra("gallery_app_widget_id", i);
            remoteViews.setOnClickPendingIntent(R.id.layout_root, PendingIntent.getActivity(context, i, intent, 201326592));
            appWidgetManager.updateAppWidget(i, remoteViews);
            DefaultLogger.d("WidgetInstallManager", "---log---initRecommendWidget>");
        }
    }
}
