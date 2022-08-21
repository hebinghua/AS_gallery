package com.miui.gallery.gallerywidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.common.WidgetInstallManager;
import com.miui.gallery.gallerywidget.common.WidgetServiceHelper;
import com.miui.gallery.gallerywidget.common.WidgetStatisticsHelper;
import com.miui.gallery.gallerywidget.service.RecommendWidgetService;
import com.miui.gallery.preference.MemoryPreferenceHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;

/* loaded from: classes2.dex */
public abstract class RecommendWidgetProviderBase extends AppWidgetProvider implements IWidgetProviderConfig {
    public final String TAG = getClass().getSimpleName();
    public boolean mIsStoryFunctionOn;
    public boolean mIsStoryFunctionStatusChange;
    public long mStoryChangeCardId;
    public String mStoryChangeType;

    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int[] intArrayExtra;
        String str = this.TAG;
        DefaultLogger.d(str, "---log---onReceive >" + intent.getAction());
        this.mIsStoryFunctionStatusChange = false;
        if (intent.getBooleanExtra("from_story_function_change", false)) {
            MemoryPreferenceHelper.putInt("widget_story_function_status", intent.getBooleanExtra("is_story_function_on", false) ? 1 : 0);
            intArrayExtra = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass().getName()));
            this.mIsStoryFunctionStatusChange = true;
        } else if (intent.getBooleanExtra("from_card_status_change", false)) {
            this.mStoryChangeCardId = intent.getLongExtra("card_status_change_card_id", -1L);
            this.mStoryChangeType = intent.getStringExtra("card_status_change_type");
            intArrayExtra = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass().getName()));
            DefaultLogger.d(this.TAG, "---log---onReceive mStoryChangeType=%s,mStoryChangeCardId=%d", this.mStoryChangeType, Long.valueOf(this.mStoryChangeCardId));
        } else {
            intArrayExtra = intent.getIntArrayExtra("appWidgetIds");
        }
        this.mIsStoryFunctionOn = GalleryWidgetUtils.isStoryFunctionOn();
        if ("miui.appwidget.action.APPWIDGET_UPDATE".equals(intent.getAction())) {
            onUpdate(context, AppWidgetManager.getInstance(context), intArrayExtra);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] iArr) {
        if (iArr == null || iArr.length <= 0) {
            return;
        }
        if (iArr.length == 1) {
            WidgetStatisticsHelper.statisticsRecommendWidgetCount(getWidgetType(), GalleryWidgetUtils.getWidgetCount(getClass()));
        }
        DefaultLogger.d(this.TAG, "---log---onUpdate appWidgetIds=%s,getRecommendWidgetType=%s", Arrays.toString(iArr), getWidgetType());
        if (GalleryWidgetUtils.isServiceRequestFrequent(iArr[0])) {
            return;
        }
        GalleryWidgetUtils.setServiceRequestTime(iArr[0]);
        for (int i : iArr) {
            WidgetInstallManager.initRecommendWidget(context, appWidgetManager, i, getRemoteViewID(), getWidgetType(), this.mIsStoryFunctionOn, this.mIsStoryFunctionStatusChange);
        }
        if (!this.mIsStoryFunctionOn) {
            return;
        }
        callUpdateService(context, iArr);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onDeleted(Context context, int[] iArr) {
        if (iArr == null) {
            return;
        }
        String str = this.TAG;
        DefaultLogger.d(str, "---log---onDeleted>" + Arrays.toString(iArr));
        Intent intent = new Intent(context, RecommendWidgetService.class);
        intent.putExtra("appWidgetIds", iArr);
        intent.putExtra("gallery_app_widget_size", getWidgetType());
        intent.putExtra("start_widget_service_for_widget_delete", true);
        WidgetServiceHelper.startForegroundServiceIfNeed(context, intent);
        DefaultLogger.d(this.TAG, "---log---onDeleted startForegroundServiceIfNeed>");
    }

    public final void callUpdateService(Context context, int[] iArr) {
        Intent intent = new Intent(context, RecommendWidgetService.class);
        intent.putExtra("appWidgetIds", iArr);
        intent.putExtra("gallery_app_widget_size", getWidgetType());
        intent.putExtra("card_status_change_card_id", this.mStoryChangeCardId);
        intent.putExtra("card_status_change_type", this.mStoryChangeType);
        WidgetServiceHelper.startForegroundServiceIfNeed(context, intent);
        DefaultLogger.d(this.TAG, "---log---callUpdateService startForegroundServiceIfNeed>");
    }
}
