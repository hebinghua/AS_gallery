package com.miui.gallery.gallerywidget.db;

import android.content.ContentValues;
import android.text.TextUtils;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class CustomWidgetDBManager {
    public static CustomWidgetDBManager sInstance;
    public final String TAG = "CustomWidgetDBManager";
    public boolean mWidgetPicHasChanged = false;
    public List<Long> mWidgetPicIdCache;

    public static /* synthetic */ boolean $r8$lambda$R3EhaQB_eJAjemgKPHr0EsnPGdM(List list, Long l) {
        return list.contains(l);
    }

    public static /* synthetic */ boolean $r8$lambda$mJ3yhcPyERqEtxXVePXWBFUCzb4(List list, Long l) {
        return list.contains(l);
    }

    public static synchronized CustomWidgetDBManager getInstance() {
        CustomWidgetDBManager customWidgetDBManager;
        synchronized (CustomWidgetDBManager.class) {
            if (sInstance == null) {
                sInstance = new CustomWidgetDBManager();
            }
            customWidgetDBManager = sInstance;
        }
        return customWidgetDBManager;
    }

    public synchronized void add(CustomWidgetDBEntity customWidgetDBEntity) {
        if (customWidgetDBEntity == null) {
            return;
        }
        boolean z = -1 != GalleryEntityManager.getInstance().insert(customWidgetDBEntity);
        if (z) {
            this.mWidgetPicHasChanged = true;
        }
        DefaultLogger.d("CustomWidgetDBManager", "---log---add result %s>" + z);
    }

    public synchronized void delete(int[] iArr) {
        if (iArr == null) {
            return;
        }
        boolean delete = GalleryEntityManager.getInstance().delete(CustomWidgetDBEntity.class, String.format(Locale.US, "%s in ('%s')", "widgetId", TextUtils.join("','", Arrays.stream(iArr).boxed().toArray())), null);
        if (delete) {
            this.mWidgetPicHasChanged = true;
        }
        DefaultLogger.d("CustomWidgetDBManager", "---log---delete result %s", Boolean.valueOf(delete));
    }

    public synchronized boolean update(CustomWidgetDBEntity customWidgetDBEntity, int i) {
        boolean z = false;
        if (customWidgetDBEntity == null) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        customWidgetDBEntity.onConvertToContents(contentValues);
        if (GalleryEntityManager.getInstance().update(CustomWidgetDBEntity.class, contentValues, String.format("%s = %s", "widgetId", Integer.valueOf(i)), null) > 0) {
            z = true;
        }
        if (z) {
            this.mWidgetPicHasChanged = true;
        }
        DefaultLogger.d("CustomWidgetDBManager", "---log---update result %s", Boolean.valueOf(z));
        return z;
    }

    public synchronized CustomWidgetDBEntity findWidgetEntity(long j) {
        return (CustomWidgetDBEntity) GalleryEntityManager.getInstance().find(CustomWidgetDBEntity.class, String.valueOf(j));
    }

    public synchronized List<CustomWidgetDBEntity> findAllCustomWidgets() {
        return GalleryEntityManager.getInstance().query(CustomWidgetDBEntity.class, null, null, null, null);
    }

    public synchronized List<Long> widgetShouldUpdateIds(final List<Long> list) {
        List<Long> list2;
        if (!BaseMiscUtil.isValid(list)) {
            return Collections.emptyList();
        }
        if (!this.mWidgetPicHasChanged && (list2 = this.mWidgetPicIdCache) != null) {
            return (List) list2.stream().filter(new Predicate() { // from class: com.miui.gallery.gallerywidget.db.CustomWidgetDBManager$$ExternalSyntheticLambda4
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return CustomWidgetDBManager.$r8$lambda$mJ3yhcPyERqEtxXVePXWBFUCzb4(list, (Long) obj);
                }
            }).collect(Collectors.toList());
        }
        List<CustomWidgetDBEntity> findAllCustomWidgets = findAllCustomWidgets();
        if (BaseMiscUtil.isValid(findAllCustomWidgets)) {
            try {
                List list3 = (List) findAllCustomWidgets.stream().map(CustomWidgetDBManager$$ExternalSyntheticLambda0.INSTANCE).flatMap(CustomWidgetDBManager$$ExternalSyntheticLambda2.INSTANCE).map(CustomWidgetDBManager$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
                if (this.mWidgetPicIdCache == null) {
                    this.mWidgetPicIdCache = new ArrayList();
                }
                if (BaseMiscUtil.isValid(list3)) {
                    this.mWidgetPicIdCache.clear();
                    this.mWidgetPicIdCache.addAll(list3);
                    List<Long> list4 = (List) list3.stream().filter(new Predicate() { // from class: com.miui.gallery.gallerywidget.db.CustomWidgetDBManager$$ExternalSyntheticLambda3
                        @Override // java.util.function.Predicate
                        public final boolean test(Object obj) {
                            return CustomWidgetDBManager.$r8$lambda$R3EhaQB_eJAjemgKPHr0EsnPGdM(list, (Long) obj);
                        }
                    }).collect(Collectors.toList());
                    if (BaseMiscUtil.isValid(list4)) {
                        this.mWidgetPicHasChanged = true;
                    } else {
                        this.mWidgetPicHasChanged = false;
                    }
                    return list4;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        this.mWidgetPicHasChanged = false;
        return Collections.emptyList();
    }

    public static /* synthetic */ List lambda$widgetShouldUpdateIds$1(CustomWidgetDBEntity customWidgetDBEntity) {
        return GalleryWidgetUtils.getDataList(customWidgetDBEntity.getPicIDList());
    }
}
