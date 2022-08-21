package com.miui.gallery.gallerywidget.db;

import android.content.ContentValues;
import android.text.TextUtils;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: classes2.dex */
public class RecommendWidgetDBManager {
    public static final String[] CONVERT_PROJECTION = {"serverId", "sha1"};
    public static RecommendWidgetDBManager sInstance;
    public final String TAG = "RecommendWidgetDBManager";

    public static synchronized RecommendWidgetDBManager getInstance() {
        RecommendWidgetDBManager recommendWidgetDBManager;
        synchronized (RecommendWidgetDBManager.class) {
            if (sInstance == null) {
                sInstance = new RecommendWidgetDBManager();
            }
            recommendWidgetDBManager = sInstance;
        }
        return recommendWidgetDBManager;
    }

    public synchronized void add(RecommendWidgetDBEntity recommendWidgetDBEntity) {
        if (recommendWidgetDBEntity == null) {
            return;
        }
        boolean z = -1 != GalleryEntityManager.getInstance().insert(recommendWidgetDBEntity);
        DefaultLogger.d("RecommendWidgetDBManager", "add result %s", Boolean.valueOf(z));
        DefaultLogger.d("RecommendWidgetDBManager", "---log---add result %s>" + z);
    }

    public synchronized void delete(int[] iArr) {
        if (iArr == null) {
            return;
        }
        DefaultLogger.d("RecommendWidgetDBManager", "---log---delete result %s", Boolean.valueOf(GalleryEntityManager.getInstance().delete(RecommendWidgetDBEntity.class, String.format(Locale.US, "%s in ('%s')", "widgetId", TextUtils.join("','", Arrays.stream(iArr).boxed().toArray())), null)));
    }

    public synchronized void update(RecommendWidgetDBEntity recommendWidgetDBEntity) {
        if (recommendWidgetDBEntity == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        recommendWidgetDBEntity.onConvertToContents(contentValues);
        boolean z = false;
        if (GalleryEntityManager.getInstance().update(RecommendWidgetDBEntity.class, contentValues, String.format("%s = %s", "widgetId", Integer.valueOf(recommendWidgetDBEntity.getWidgetId())), null) > 0) {
            z = true;
        }
        DefaultLogger.d("RecommendWidgetDBManager", "---log---update result %s", Boolean.valueOf(z));
    }

    public synchronized RecommendWidgetDBEntity findWidgetEntity(long j) {
        return (RecommendWidgetDBEntity) GalleryEntityManager.getInstance().find(RecommendWidgetDBEntity.class, String.valueOf(j));
    }
}
