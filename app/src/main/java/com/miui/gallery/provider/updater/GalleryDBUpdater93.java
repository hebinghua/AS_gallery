package com.miui.gallery.provider.updater;

import android.database.ContentObserver;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.WhiteAlbumsStrategy;
import com.miui.gallery.loader.AlbumConvertCallback;
import com.miui.gallery.model.AlbumConstants;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.AlbumList;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.SQLiteView;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.share.ShareAlbumCacheManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class GalleryDBUpdater93 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        updateAlbumGroup(supportSQLiteDatabase);
        if (!updateResult.isRecreateTableCloud()) {
            fixSyncSwitchOfAlbumTurnOff(supportSQLiteDatabase);
        }
        SQLiteView.of("extended_cloud").createByVersion(supportSQLiteDatabase, 4);
        return UpdateResult.defaultResult();
    }

    public final void updateAlbumGroup(final SupportSQLiteDatabase supportSQLiteDatabase) {
        ThreadManager.execute(31, new Runnable() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater93.1
            @Override // java.lang.Runnable
            public void run() {
                Cursor queryShareAll;
                Cursor query;
                Cursor cursor = null;
                try {
                    queryShareAll = AlbumManager.queryShareAll(GalleryApp.sGetAndroidContext(), supportSQLiteDatabase, AlbumConstants.SHARED_ALBUM_PROJECTION, "count > 0", null, null);
                } catch (Exception e) {
                    e = e;
                }
                try {
                    ShareAlbumCacheManager.getInstance().putSharedAlbums(queryShareAll);
                    BaseMiscUtil.closeSilently(queryShareAll);
                    supportSQLiteDatabase.execSQL(String.format("UPDATE %s SET %s = (%s | %d) where %s", "cloud", "attributes", "attributes", 6144L, "(attributes & 16 <> 0 AND attributes & 32 = 0) AND (attributes & 64 <> 0 AND attributes & 128 =0 )"));
                    supportSQLiteDatabase.execSQL(String.format("UPDATE %s SET %s = (%s | %d) where %s", "cloud", "attributes", "attributes", 6144L, GalleryDBUpdater93.this.generateUpdateAlbumToRubbishSelection()));
                    query = supportSQLiteDatabase.query("SELECT _id, name, cover_id, cover_path, cover_sha1, cover_sync_state, media_count, face_people_id, baby_info, thumbnail_info, serverId, attributes, immutable, top_time, sortBy, baby_sharer_info, local_path, cover_size, sort_position, datemodified FROM (SELECT * FROM (SELECT _id AS _id, attributes AS attributes, fileName AS name, localFile AS local_path, localFlag AS flag, dateTaken AS top_time, peopleId AS face_people_id, babyInfoJson AS baby_info, NULL AS baby_sharer_info, serverId AS serverId, thumbnailInfo AS thumbnail_info, sort_position AS sort_position, CASE WHEN sortBy IS NULL THEN CASE WHEN dateTaken >0 THEN 9223372036854775807 - dateTaken ELSE dateTaken END ELSE sortBy END AS sortBy, CASE WHEN localFile LIKE '/%' THEN 1 ELSE 0 END AS immutable, datemodified FROM cloud WHERE (serverType=0) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))  ) LEFT JOIN (SELECT localGroupId AS _id, count(_id) AS media_count, -1 AS cover_id, NULL AS cover_path, NULL AS cover_sha1, 0 AS cover_sync_state, 0 AS cover_size, NULL AS latest_photo , 0 AS size FROM (SELECT * FROM cloud ORDER BY mixedDateTime DESC,dateModified DESC,_id DESC ) cloud WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) GROUP BY localGroupId) USING (_id)) WHERE ((attributes & 64 = 0) AND (attributes & 2048 = 0))", (Object[]) null);
                    AlbumList mo1129convert = new AlbumConvertCallback().mo1129convert(query);
                    if (mo1129convert != null && !mo1129convert.isEmpty()) {
                        WhiteAlbumsStrategy whiteAlbumsStrategy = CloudControlStrategyHelper.getWhiteAlbumsStrategy();
                        ArrayList arrayList = new ArrayList();
                        ArrayList arrayList2 = new ArrayList();
                        ArrayList arrayList3 = new ArrayList();
                        Iterator<Album> it = mo1129convert.iterator();
                        while (it.hasNext()) {
                            Album next = it.next();
                            if (next.isForceTypeTime() && !next.isOtherAlbum()) {
                                arrayList2.add(Long.valueOf(next.getAlbumId()));
                            }
                            if ((next.getAttributes() & 256) != 0) {
                                arrayList3.add(Long.valueOf(next.getAlbumId()));
                            }
                            if (!next.isSystemAlbum() && !next.isShareAlbum() && !next.isRawAlbum() && !next.isBabyAlbum() && !next.isUserCreateAlbum() && !next.isUserManualMoveToAlbumHome() && !next.isForceTypeTime() && (whiteAlbumsStrategy == null || !whiteAlbumsStrategy.isWhiteAlbum(next.getLocalPath()))) {
                                arrayList.add(Long.valueOf(next.getAlbumId()));
                            }
                        }
                        if (!arrayList3.isEmpty()) {
                            GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(Locale.US, "UPDATE %s SET %s = (%s & ~%d) | %d WHERE %s IN (%s)", "cloud", "attributes", "attributes", 256L, 1280L, j.c, TextUtils.join(",", arrayList3)));
                        }
                        if (!arrayList.isEmpty()) {
                            GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(Locale.US, "UPDATE %s SET %s = (%s & ~%d) | %d WHERE %s IN (%s)", "cloud", "attributes", "attributes", 64L, 64L, j.c, TextUtils.join(",", arrayList)));
                        }
                        if (!arrayList2.isEmpty()) {
                            GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(Locale.US, "UPDATE %s SET %s = %s | %d WHERE %s IN (%s)", "cloud", "attributes", "attributes", 128L, j.c, TextUtils.join(",", arrayList2)));
                        }
                    } else {
                        DefaultLogger.d("GalleryDBUpdater93", "数据升级至93版本,无相册移动至其他");
                    }
                    GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI, (ContentObserver) null, false);
                    DefaultLogger.d("GalleryDBUpdater93", "数据升级至93版本成功");
                } catch (Exception e2) {
                    e = e2;
                    cursor = query;
                    BaseMiscUtil.closeSilently(cursor);
                    DefaultLogger.e("GalleryDBUpdater93", "数据升级至93版本失败,原因：%s", e.getMessage());
                }
            }
        });
    }

    public final void fixSyncSwitchOfAlbumTurnOff(SupportSQLiteDatabase supportSQLiteDatabase) {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        try {
            acquire.append("serverType");
            acquire.append(" = ");
            acquire.append(0);
            acquire.append(" AND ");
            acquire.append("serverStatus");
            acquire.append("='");
            acquire.append("custom");
            acquire.append("' AND ");
            acquire.append("(");
            acquire.append("attributes");
            acquire.append(" & ");
            acquire.append(" (");
            acquire.append(1L);
            acquire.append("|");
            acquire.append(2L);
            acquire.append(")");
            acquire.append(" = 0");
            acquire.append(")");
            acquire.append(" AND ");
            acquire.append(" (");
            acquire.append("description");
            acquire.append(" NOT NULL ");
            acquire.append(" AND ");
            acquire.append("description");
            acquire.append(" !=''");
            acquire.append(" AND ");
            acquire.append("json_extract(description,'$.autoUpload') = 1");
            acquire.append(")");
            GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(Locale.US, "UPDATE %s SET %s = %s | %d WHERE %s", "cloud", "attributes", "attributes", 1L, acquire.toString()));
        } finally {
            Pools.getStringBuilderPool().release(acquire);
        }
    }

    public final String generateUpdateAlbumToRubbishSelection() {
        StringBuilder acquire = Pools.getStringBuilderPool().acquire();
        try {
            acquire.append("(");
            acquire.append("(");
            acquire.append("attributes");
            acquire.append(" & ");
            acquire.append(16L);
            acquire.append(" <> 0");
            acquire.append(" AND ");
            acquire.append("attributes");
            acquire.append(" & ");
            acquire.append(32L);
            acquire.append(" <> 0");
            acquire.append(")");
            acquire.append(" AND ");
            acquire.append("(");
            acquire.append("attributes");
            acquire.append(" & ");
            acquire.append(64L);
            acquire.append(" <> 0");
            acquire.append(" AND ");
            acquire.append("attributes");
            acquire.append(" & ");
            acquire.append(128L);
            acquire.append(" <>0 ");
            acquire.append(")");
            acquire.append(")");
            acquire.append(" AND ");
            acquire.append(" json_extract(description,'$.rubbish') = 1");
            return acquire.toString();
        } finally {
            Pools.getStringBuilderPool().release(acquire);
        }
    }
}
