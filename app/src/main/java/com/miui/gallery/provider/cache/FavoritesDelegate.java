package com.miui.gallery.provider.cache;

import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.tracing.Trace;
import com.google.common.collect.Sets;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class FavoritesDelegate {
    public static final Object INITIALIZATION_LOCK = new Object();
    public static final String[] PROJECTION = {"cloud_id"};
    public Set<Long> mCloudIdSet = Sets.newConcurrentHashSet();

    public void load(SupportSQLiteDatabase supportSQLiteDatabase) {
        synchronized (INITIALIZATION_LOCK) {
            Trace.beginSection("FavoritesDelegate#load");
            long currentTimeMillis = System.currentTimeMillis();
            try {
                Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("favorites").columns(PROJECTION).selection("(isFavorite NOT NULL  AND isFavorite > 0) AND source = ? AND cloud_id NOT NULL", new String[]{String.valueOf(1)}).create());
                if (query != null) {
                    this.mCloudIdSet = Collections.newSetFromMap(new ConcurrentHashMap(query.getCount()));
                    query.moveToFirst();
                    while (!query.isAfterLast()) {
                        this.mCloudIdSet.add(Long.valueOf(query.getLong(0)));
                        query.moveToNext();
                    }
                    DefaultLogger.d(".provider.cache.FavoritesDelegate", "loaded %d favorite cloud id from cursor [%d]", Integer.valueOf(this.mCloudIdSet.size()), Integer.valueOf(query.getCount()));
                } else {
                    DefaultLogger.e(".provider.cache.FavoritesDelegate", "fill provider failed with a null cursor");
                }
                if (query != null) {
                    query.close();
                }
                DefaultLogger.d(".provider.cache.FavoritesDelegate", "load favorite cloud id costs [%d]", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                Trace.endSection();
            } catch (SQLiteException e) {
                HashMap hashMap = new HashMap();
                hashMap.put("version", String.valueOf(supportSQLiteDatabase.getVersion()));
                hashMap.put("error", e.getMessage() + " : " + e.getCause());
                SamplingStatHelper.recordCountEvent("db_helper", "db_mediamanager_load", hashMap);
                DebugUtil.exportDB(false);
                GalleryDBHelper.getInstance().deleteDatabase();
                throw e;
            }
        }
    }

    public boolean isFavorite(Long l) {
        return this.mCloudIdSet.contains(l);
    }

    public boolean insertToFavorites(Long l) {
        if (l == null) {
            return false;
        }
        DefaultLogger.d(".provider.cache.FavoritesDelegate", "insert cloud id [%d].", l);
        return this.mCloudIdSet.add(l);
    }

    public boolean removeFromFavorites(Long l) {
        if (l == null) {
            return false;
        }
        DefaultLogger.d(".provider.cache.FavoritesDelegate", "remove cloud id [%d].", l);
        return this.mCloudIdSet.remove(l);
    }
}
