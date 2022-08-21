package com.miui.gallery.gallerywidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.common.WidgetServiceHelper;
import com.miui.gallery.gallerywidget.common.WidgetStatisticsHelper;
import com.miui.gallery.gallerywidget.service.CustomWidgetService;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;

/* loaded from: classes2.dex */
public abstract class CustomWidgetProviderBase extends AppWidgetProvider implements IWidgetProviderConfig {
    public final String TAG = getClass().getSimpleName();
    public boolean mIsFromCustomEditor;
    public boolean mIsFromPicStatusChange;
    public long[] mPicIds;

    @Override // android.appwidget.AppWidgetProvider, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        int[] intArrayExtra;
        String str = this.TAG;
        DefaultLogger.d(str, "---log---onReceive >" + intent.getAction());
        this.mIsFromPicStatusChange = false;
        this.mIsFromCustomEditor = false;
        if (intent.getBooleanExtra("from_pic_status_change", false)) {
            this.mIsFromPicStatusChange = true;
            this.mPicIds = intent.getLongArrayExtra("pic_status_change_pic_ids");
            intArrayExtra = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, getClass().getName()));
        } else if (intent.getBooleanExtra("from_custom_editor", false)) {
            this.mIsFromCustomEditor = true;
            intArrayExtra = new int[]{intent.getIntExtra("appWidgetId", -1)};
        } else {
            intArrayExtra = intent.getIntArrayExtra("appWidgetIds");
        }
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
            WidgetStatisticsHelper.statisticsCustomWidgetCount(getWidgetType(), GalleryWidgetUtils.getWidgetCount(getClass()));
        }
        String str = this.TAG;
        DefaultLogger.d(str, "---log---onUpdate appWidgetIds>" + Arrays.toString(iArr));
        if (GalleryWidgetUtils.isServiceRequestFrequent(iArr[0])) {
            return;
        }
        GalleryWidgetUtils.setServiceRequestTime(iArr[0]);
        Intent intent = new Intent(context, CustomWidgetService.class);
        intent.putExtra("appWidgetIds", iArr);
        intent.putExtra("gallery_app_widget_size", getWidgetType());
        intent.putExtra("from_pic_status_change", this.mIsFromPicStatusChange);
        intent.putExtra("from_custom_editor", this.mIsFromCustomEditor);
        intent.putExtra("pic_status_change_pic_ids", this.mPicIds);
        WidgetServiceHelper.startForegroundServiceIfNeed(context, intent);
        DefaultLogger.d(this.TAG, "---log---onUpdate startForegroundServiceIfNeed>");
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onDeleted(Context context, int[] iArr) {
        if (iArr == null) {
            return;
        }
        String str = this.TAG;
        DefaultLogger.d(str, "---log---onDeleted>" + Arrays.toString(iArr));
        Intent intent = new Intent(context, CustomWidgetService.class);
        intent.putExtra("appWidgetIds", iArr);
        intent.putExtra("gallery_app_widget_size", getWidgetType());
        intent.putExtra("start_widget_service_for_widget_delete", true);
        WidgetServiceHelper.startForegroundServiceIfNeed(context, intent);
        DefaultLogger.d(this.TAG, "---log---onDeleted startForegroundServiceIfNeed>");
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onRestored(Context context, int[] iArr, int[] iArr2) {
        super.onRestored(context, iArr, iArr2);
        DefaultLogger.d(this.TAG, "---log---onRestored:oldWidgetIds=%s,newWidgetIds=%s", Arrays.toString(iArr), Arrays.toString(iArr2));
        migrateData(context, iArr, iArr2);
    }

    @Override // android.appwidget.AppWidgetProvider
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int i, Bundle bundle) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, i, bundle);
        DefaultLogger.d(this.TAG, "---log---onAppWidgetOptionsChanged");
        if (!bundle.getBoolean("miuiIdChanged") || bundle.getBoolean("miuiIdChangedComplete")) {
            return;
        }
        int[] intArray = bundle.getIntArray("miuiNewIds");
        int[] intArray2 = bundle.getIntArray("miuiOldIds");
        DefaultLogger.d(this.TAG, "---log---miuiIdChanged:oldWidgetIds=%s,newWidgetIds=%s", Arrays.toString(intArray2), Arrays.toString(intArray));
        migrateData(context, intArray2, intArray);
    }

    public final void migrateData(Context context, int[] iArr, int[] iArr2) {
        Intent intent = new Intent(context, CustomWidgetService.class);
        intent.putExtra("appWidgetIds", iArr);
        intent.putExtra("gallery_app_restore_widget_id", iArr2);
        intent.putExtra("gallery_app_widget_size", getWidgetType());
        WidgetServiceHelper.startForegroundServiceIfNeed(context, intent);
        DefaultLogger.d(this.TAG, "---log---migrateData: startForegroundServiceIfNeed>");
    }
}
