package com.miui.gallery.provider.updater;

import android.annotation.SuppressLint;
import android.database.ContentObserver;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.base_optimization.util.ExceptionUtils;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.preference.PreferenceHelper;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public class GalleryDBUpdater94 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        addVersion94ColumnsIfNeed(supportSQLiteDatabase, updateResult.isRecreateTableCloud(), updateResult.isRecreateTableShareImage());
        updateAlbumSort(supportSQLiteDatabase);
        return UpdateResult.defaultResult();
    }

    public final void addVersion94ColumnsIfNeed(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, boolean z2) {
        if (!z) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "cloud", new TableColumn.Builder().setName("sort_position").setType("REAL").build());
        }
        if (!z2) {
            GalleryDBHelper.addColumn(supportSQLiteDatabase, "shareAlbum", new TableColumn.Builder().setName("sort_position").setType("REAL").build());
        }
    }

    @SuppressLint({"CheckResult"})
    public final void updateAlbumSort(final SupportSQLiteDatabase supportSQLiteDatabase) {
        Flowable.fromCallable(new Callable<List<Album>>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater94.4
            @Override // java.util.concurrent.Callable
            public List<Album> call() throws Exception {
                Cursor query = supportSQLiteDatabase.query("SELECT _id,name, baby_info, serverId, attributes, sortBy,  local_path FROM (SELECT * FROM (SELECT _id AS _id, attributes AS attributes, fileName AS name, localFile AS local_path, localFlag AS flag, dateTaken AS top_time, peopleId AS face_people_id, babyInfoJson AS baby_info, NULL AS baby_sharer_info, serverId AS serverId, thumbnailInfo AS thumbnail_info, sort_position AS sort_position, CASE WHEN sortBy IS NULL THEN CASE WHEN dateTaken >0 THEN 9223372036854775807 - dateTaken ELSE dateTaken END ELSE sortBy END AS sortBy, CASE WHEN localFile LIKE '/%' THEN 1 ELSE 0 END AS immutable, datemodified FROM cloud WHERE (serverType=0) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))  ) LEFT JOIN (SELECT localGroupId AS _id, count(_id) AS media_count, -1 AS cover_id, NULL AS cover_path, NULL AS cover_sha1, 0 AS cover_sync_state, 0 AS cover_size, NULL AS latest_photo , 0 AS size FROM (SELECT * FROM cloud ORDER BY mixedDateTime DESC,dateModified DESC,_id DESC ) cloud WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) GROUP BY localGroupId) USING (_id) UNION SELECT * FROM (SELECT 2147383647+_id AS _id, attributes AS attributes, sharealbum.fileName AS name, NULL AS local_path, localFlag AS flag, dateTaken AS top_time, peopleId AS face_people_id, babyInfoJson AS baby_info, sharerInfo AS baby_sharer_info, serverId AS serverId, NULL AS thumbnail_info, sort_position AS sort_position, CASE WHEN sortBy IS NULL THEN CASE WHEN dateTaken >0 THEN 9223372036854775807 - dateTaken ELSE dateTaken END ELSE sortBy END AS sortBy, 0 AS immutable, datemodified AS datemodified FROM shareAlbum WHERE serverId IS NOT NULL ) LEFT JOIN (SELECT 2147383647+localGroupId AS _id, count(_id) AS media_count, 33554431+_id AS cover_id, ( CASE WHEN (microthumbfile NOT NULL and microthumbfile != '') THEN microthumbfile WHEN (thumbnailFile NOT NULL and thumbnailFile != '') THEN thumbnailFile ELSE localFile END ) AS cover_path, sha1 AS cover_sha1,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS cover_sync_state, size AS cover_size, max(dateModified) AS latest_photo , 0 AS size FROM shareImage WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) GROUP BY localGroupId) USING (_id)  UNION SELECT 2147483647 AS _id, 0 AS attributes, 'VIDEO' AS name, NULL AS local_path, 0 AS flag, -998 AS top_time, NULL AS face_people_id, NULL AS baby_info, NULL AS baby_sharer_info, '-2147483647' AS serverId, NULL AS thumbnail_info, -998 AS sort_position , -998 AS sortBy, 0 AS immutable,  -998 AS datemodified  , count(_id) AS media_count , _id AS cover_id, ( CASE WHEN (microthumbfile NOT NULL and microthumbfile != '') THEN microthumbfile WHEN (thumbnailFile NOT NULL and thumbnailFile != '') THEN thumbnailFile ELSE localFile END ) AS cover_path, sha1 AS cover_sha1,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS cover_sync_state, size AS cover_size, max(dateModified) AS latest_photo  , 0 AS size FROM (SELECT * FROM cloud ORDER BY mixedDateTime DESC,dateModified DESC,_id DESC ) cloud WHERE serverType=2 AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId!=-1000) AND (localGroupId in (SELECT _id FROM cloud WHERE  NOT (  ( attributes & 2048 <> 0)) ))   UNION SELECT 2147483644 AS _id, 0 AS attributes, 'ALL_PHOTOS' AS name, NULL AS local_path, 0 AS flag, -1001 AS top_time, NULL AS face_people_id, NULL AS baby_info, NULL AS baby_sharer_info, '-2147483644' AS serverId, NULL AS thumbnail_info, -1001 AS sort_position, -1001 AS sortBy, 0 AS immutable, -1001 AS datemodified  , count(_id) AS media_count , -1 AS cover_id, NULL AS cover_path, NULL AS cover_sha1, 0 AS cover_sync_state, 0 AS cover_size, NULL AS latest_photo  , 0 AS size FROM (SELECT _id AS _id, sha1, localFlag, dateModified, serverStatus, localGroupId, thumbnailFile, microthumbfile, localFile, serverType, size, babyInfoJson FROM (SELECT * FROM cloud ORDER BY dateModified DESC,mixedDateTime DESC,_id DESC ) cloud) WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId!=-1000) AND ( localGroupId NOT IN  ( select _id from cloud where  ( attributes & 2048 <> 0))) AND (localGroupId in (SELECT _id FROM cloud WHERE  NOT (  ( attributes & 2048 <> 0)) ))  UNION SELECT 2147483642 AS _id, 0 AS attributes, 'FAVORITES' AS name, NULL AS local_path, 0 AS flag, -1000 AS top_time, NULL AS face_people_id, NULL AS baby_info, NULL AS baby_sharer_info, '-2147483642' AS serverId, NULL AS thumbnail_info, -1000 AS sort_position, -1000 AS sortBy, 0 AS immutable, -1000 AS datemodified  , count(_id) AS media_count , _id AS cover_id, ( CASE WHEN (microthumbfile NOT NULL and microthumbfile != '') THEN microthumbfile WHEN (thumbnailFile NOT NULL and thumbnailFile != '') THEN thumbnailFile ELSE localFile END ) AS cover_path, sha1 AS cover_sha1,  CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS cover_sync_state, size AS cover_size, max(dateModified) AS latest_photo  , 0 AS size FROM extended_cloud WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId!=-1000) AND (serverType IN (1,2) AND isFavorite NOT NULL AND isFavorite > 0)  AND ((attributes & 64 = 0) AND (attributes & 2048 = 0)) )", (Object[]) null);
                if (!query.moveToFirst()) {
                    throw new IllegalStateException("no albums!");
                }
                LinkedList linkedList = new LinkedList();
                do {
                    linkedList.add(Upgrade94AlbumEntity.fromCursor(query));
                } while (query.moveToNext());
                BaseMiscUtil.closeSilently(query);
                linkedList.sort(new Comparator<Album>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater94.4.1
                    @Override // java.util.Comparator
                    public int compare(Album album, Album album2) {
                        int ordinal;
                        int ordinal2;
                        if (album.getAlbumType() != album2.getAlbumType()) {
                            Album.AlbumType albumType = album.getAlbumType();
                            Album.AlbumType albumType2 = Album.AlbumType.OTHERS_SHARE;
                            if (albumType == albumType2 || album.getAlbumType() == Album.AlbumType.NORMAL) {
                                ordinal = Album.AlbumType.USER_CREATE.ordinal();
                            } else {
                                ordinal = album.getAlbumType().ordinal();
                            }
                            if (album2.getAlbumType() == albumType2 || album2.getAlbumType() == Album.AlbumType.NORMAL) {
                                ordinal2 = Album.AlbumType.USER_CREATE.ordinal();
                            } else {
                                ordinal2 = album2.getAlbumType().ordinal();
                            }
                            if (ordinal != ordinal2) {
                                return Integer.compare(ordinal, ordinal2);
                            }
                        }
                        return Long.compare(album.getSortBy(), album2.getSortBy());
                    }
                });
                Album album = (Album) linkedList.get(0);
                if (album.isForceTypeTime()) {
                    PreferenceHelper.putString(GalleryPreferences.PrefKeys.SORT_POSITION_NEXT_TOP_ALBUM, Long.toString(album.getSortBy() - 1));
                }
                return linkedList;
            }
        }).doOnNext(new Consumer<List<Album>>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater94.3
            @Override // io.reactivex.functions.Consumer
            public void accept(List<Album> list) throws Exception {
                list.forEach(new java.util.function.Consumer<Album>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater94.3.1
                    @Override // java.util.function.Consumer
                    public void accept(Album album) {
                        String format;
                        String calculateSortPosition = GalleryDBUpdater94.this.calculateSortPosition(album);
                        if (album.isOtherShareAlbum()) {
                            format = String.format(Locale.US, r2, "shareAlbum", "sort_position", calculateSortPosition, j.c, Long.valueOf(album.getAlbumId() - 2147383647));
                        } else {
                            format = String.format(Locale.US, r2, "cloud", "sort_position", calculateSortPosition, j.c, Long.valueOf(album.getAlbumId()));
                        }
                        DefaultLogger.d("GalleryDBUpdater94", "albums name: %s Perform the upgrade,sortBy = [%s] , sortPosition = [%f]", album.getAlbumName(), Long.valueOf(album.getSortBy()), calculateSortPosition);
                        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, format);
                    }
                });
            }
        }).subscribe(new Consumer<List<Album>>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater94.1
            @Override // io.reactivex.functions.Consumer
            public void accept(List<Album> list) throws Exception {
                GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI, (ContentObserver) null, false);
            }
        }, new Consumer<Throwable>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater94.2
            @Override // io.reactivex.functions.Consumer
            public void accept(Throwable th) throws Exception {
                DefaultLogger.e("GalleryDBUpdater94", "数据升级至94版本失败,原因：%s", ExceptionUtils.getStackTraceString(th));
            }
        });
    }

    public final String calculateSortPosition(Album album) {
        if (album.isSystemAlbum() || album.isForceTypeTime()) {
            return Long.toString(album.getSortBy());
        }
        if (album.isBabyAlbum()) {
            return AlbumSortHelper.calculateSortPositionByBabyAlbum();
        }
        if (album.isOtherShareAlbum()) {
            return AlbumSortHelper.calculateSortPositionByNormalAlbum(album.getSortBy() == 0 ? album.getDateTaken() : Long.MAX_VALUE - album.getSortBy());
        } else if (album.isUserCreative()) {
            return AlbumSortHelper.calculateSortPositionByUserCreativeAlbum();
        } else {
            return AlbumSortHelper.calculateSortPositionByNormalAlbum(Long.MAX_VALUE - album.getSortBy());
        }
    }

    /* loaded from: classes2.dex */
    public static class Upgrade94AlbumEntity extends Album {
        public static Upgrade94AlbumEntity fromCursor(Cursor cursor) {
            Upgrade94AlbumEntity upgrade94AlbumEntity = null;
            if (cursor != null && !cursor.isClosed()) {
                int columnIndex = cursor.getColumnIndex(j.c);
                if (-1 == columnIndex) {
                    return null;
                }
                upgrade94AlbumEntity = new Upgrade94AlbumEntity();
                upgrade94AlbumEntity.setAlbumId(cursor.getLong(columnIndex));
                int columnIndex2 = cursor.getColumnIndex("name");
                if (!cursor.isNull(columnIndex2)) {
                    upgrade94AlbumEntity.setAlbumName(cursor.getString(columnIndex2));
                }
                int columnIndex3 = cursor.getColumnIndex("baby_info");
                if (!cursor.isNull(columnIndex3)) {
                    upgrade94AlbumEntity.setBabyInfo(cursor.getString(columnIndex3));
                }
                int columnIndex4 = cursor.getColumnIndex("serverId");
                if (!cursor.isNull(columnIndex4)) {
                    upgrade94AlbumEntity.setServerId(cursor.getString(columnIndex4));
                }
                int columnIndex5 = cursor.getColumnIndex("attributes");
                if (!cursor.isNull(columnIndex5)) {
                    upgrade94AlbumEntity.setAttributes(cursor.getLong(columnIndex5));
                }
                int columnIndex6 = cursor.getColumnIndex("sortBy");
                if (!cursor.isNull(columnIndex6)) {
                    upgrade94AlbumEntity.setSortBy(cursor.getLong(columnIndex6));
                }
                int columnIndex7 = cursor.getColumnIndex("local_path");
                if (!cursor.isNull(columnIndex7)) {
                    upgrade94AlbumEntity.setLocalPath(cursor.getString(columnIndex7));
                }
                upgrade94AlbumEntity.setAlbumType(parseAlbumType(upgrade94AlbumEntity));
            }
            return upgrade94AlbumEntity;
        }

        @Override // com.miui.gallery.model.dto.Album
        public boolean isForceTypeTime() {
            return isForceTopAlbumByTopTime(getSortBy());
        }

        public static boolean isForceTopAlbumByTopTime(long j) {
            return j >= PreferenceHelper.getLong(GalleryPreferences.PrefKeys.ALBUM_NEXT_FORCE_TOP_TIME, 1L) && j <= 1;
        }

        public void setBabyInfo(String str) {
            if (this.mExtraInfo == null) {
                this.mExtraInfo = new Album.ExtraInfo();
            }
            this.mExtraInfo.setBabyInfo(str);
        }

        public static Album.AlbumType parseAlbumType(Album album) {
            if (isForceTopAlbumByTopTime(album.getSortBy())) {
                return Album.AlbumType.PINNED;
            }
            if (Album.isSystemAlbum(album.getServerId())) {
                return Album.AlbumType.SYSTEM;
            }
            if (!TextUtils.isEmpty(album.getBabyInfo())) {
                return Album.AlbumType.BABY;
            }
            if (ShareAlbumHelper.isOtherShareAlbumId(album.getAlbumId())) {
                return Album.AlbumType.OTHERS_SHARE;
            }
            if (Album.isUserCreative(album.getLocalPath())) {
                return Album.AlbumType.CREATIVE;
            }
            if (Album.isUserCreateAlbum(album.getLocalPath())) {
                return Album.AlbumType.USER_CREATE;
            }
            return Album.AlbumType.NORMAL;
        }
    }
}
