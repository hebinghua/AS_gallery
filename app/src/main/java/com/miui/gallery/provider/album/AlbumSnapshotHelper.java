package com.miui.gallery.provider.album;

import android.content.Context;
import android.database.Cursor;
import com.miui.gallery.dao.GalleryLiteEntityManager;
import com.miui.gallery.dao.base.EntityTransaction;
import com.miui.gallery.loader.AlbumConvertCallback;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumSnapshotHelper {
    public static void queryAndPersist(Context context) {
        Cursor cursor = null;
        try {
            try {
                cursor = context.getContentResolver().query(GalleryContract.Album.URI.buildUpon().appendQueryParameter("query_flags", String.valueOf(AlbumConstants.QueryScene.SCENE_ALBUM_TAB_PAGE)).build(), AlbumManager.QUERY_ALBUM_PROJECTION, null, null, null);
                persist(new AlbumConvertCallback().mo1129convert(cursor));
            } catch (Exception e) {
                DefaultLogger.e("AlbumSnapshotHelper", e);
            }
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }

    public static synchronized int persist(List<Album> list) {
        int i;
        synchronized (AlbumSnapshotHelper.class) {
            i = 0;
            if (list != null) {
                if (list != Collections.EMPTY_LIST) {
                    long currentTimeMillis = System.currentTimeMillis();
                    EntityTransaction transaction = GalleryLiteEntityManager.getInstance().getTransaction();
                    transaction.begin();
                    GalleryLiteEntityManager.getInstance().deleteAll(Album.class);
                    int i2 = 0;
                    for (Album album : list) {
                        if (album.getClass() == Album.class && album.isCacheable()) {
                            i2 += -1 != GalleryLiteEntityManager.getInstance().insert(album) ? 1 : 0;
                            if (i2 >= 250) {
                                break;
                            }
                        }
                    }
                    i = i2;
                    transaction.commit();
                    transaction.end();
                    long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                    DefaultLogger.d("AlbumSnapshotHelper", "Save snapshot for [%d] albums costs [%dms]", Integer.valueOf(i), Long.valueOf(currentTimeMillis2));
                    HashMap hashMap = new HashMap();
                    hashMap.put(MiStat.Param.COUNT, String.valueOf(i));
                    hashMap.put("cost_time", String.valueOf(currentTimeMillis2));
                    SamplingStatHelper.recordCountEvent("album", "album_snapshot_save_time", hashMap);
                }
            }
            GalleryLiteEntityManager.getInstance().deleteAll(Album.class);
        }
        return i;
    }
}
