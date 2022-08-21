package com.miui.gallery.dao;

import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.dao.base.EntityManager;
import com.miui.gallery.model.HomeMedia;
import com.miui.gallery.model.HomeMediaHeader;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.face.PeopleItem;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class GalleryLiteEntityManager extends EntityManager {

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final GalleryLiteEntityManager INSTANCE = new GalleryLiteEntityManager();
    }

    @Override // com.miui.gallery.dao.base.EntityManager
    public int getTablesCount() {
        return 4;
    }

    public GalleryLiteEntityManager() {
        super(GalleryApp.sGetAndroidContext(), "gallery_lite.db", 12);
    }

    public static GalleryLiteEntityManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void warmUp() {
        long currentTimeMillis = System.currentTimeMillis();
        Cursor cursor = null;
        try {
            try {
                cursor = rawQuery(Album.class, new String[]{"count(*)"}, null, null, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    DefaultLogger.d("GalleryLiteEntityManager", "table Album has %d items", Integer.valueOf(cursor.getInt(0)));
                }
                DefaultLogger.i("GalleryLiteEntityManager", "warm up costs: %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            } catch (Exception e) {
                DefaultLogger.e("GalleryLiteEntityManager", e);
            }
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }

    @Override // com.miui.gallery.dao.base.EntityManager
    public void onInitTableList() {
        addTable(Album.class);
        addTable(PeopleItem.class);
        addTable(HomeMedia.class);
        addTable(HomeMediaHeader.class);
    }

    @Override // com.miui.gallery.dao.base.EntityManager
    public void onDatabaseUpgrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
        if (i < 8) {
            EntityManager.dropTable(supportSQLiteDatabase, Album.class);
            EntityManager.createTable(supportSQLiteDatabase, Album.class);
        }
        if (i < 9) {
            EntityManager.dropTable(supportSQLiteDatabase, Album.class);
            EntityManager.createTable(supportSQLiteDatabase, Album.class);
        }
        if (i < 12) {
            EntityManager.dropTable(supportSQLiteDatabase, Album.class);
            EntityManager.createTable(supportSQLiteDatabase, Album.class);
        }
        DefaultLogger.fd("GalleryLiteEntityManager", "onDatabaseUpgrade: from %d to %d", Integer.valueOf(i), Integer.valueOf(i2));
    }

    @Override // com.miui.gallery.dao.base.EntityManager
    public void onDatabaseDowngrade(SupportSQLiteDatabase supportSQLiteDatabase, int i, int i2) {
        DefaultLogger.w("GalleryLiteEntityManager", "onDatabaseDowngrade from %s to %s", Integer.valueOf(i), Integer.valueOf(i2));
    }
}
