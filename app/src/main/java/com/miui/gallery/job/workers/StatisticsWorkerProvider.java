package com.miui.gallery.job.workers;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteStatement;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkerParameters;
import ch.qos.logback.core.CoreConstants;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.gson.JsonObject;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.card.Card;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.job.IPeriodicWorkerProvider;
import com.miui.gallery.job.PeriodicWorkerProvider;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.InternalContract$Album;
import com.miui.gallery.provider.MediaSortDateHelper;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.RequirementHelper$MediaTypeGroup;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.keyguard.LockScreenHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Result;
import kotlin.ResultKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import miui.cloud.util.DeviceFeatureUtils;
import miui.cloud.util.SyncAutoSettingUtil;

/* compiled from: StatisticsWorkerProvider.kt */
@PeriodicWorkerProvider(unique = true, uniqueName = "com.miui.gallery.job.Statistics")
/* loaded from: classes2.dex */
public final class StatisticsWorkerProvider implements IPeriodicWorkerProvider {
    @Override // com.miui.gallery.job.IPeriodicWorkerProvider
    public PeriodicWorkRequest getWorkRequest() {
        PeriodicWorkRequest build = new PeriodicWorkRequest.Builder(StatisticsReportWorker.class, 7L, TimeUnit.DAYS).setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).setRequiresCharging(true).setRequiresDeviceIdle(true).build()).build();
        Intrinsics.checkNotNullExpressionValue(build, "PeriodicWorkRequestBuild…d()\n            ).build()");
        return build;
    }

    /* compiled from: StatisticsWorkerProvider.kt */
    /* loaded from: classes2.dex */
    public static final class StatisticsReportWorker extends TrackedWorker {
        public static final Companion Companion = new Companion(null);

        public final long formatProportion(float f) {
            return f * 100;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public StatisticsReportWorker(Context context, WorkerParameters workerParams) {
            super(context, workerParams);
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(workerParams, "workerParams");
        }

        public final void recordGallerySettings() {
            Account xiaomiAccount = AccountHelper.getXiaomiAccount(GalleryApp.sGetAndroidContext());
            boolean z = xiaomiAccount != null;
            SamplingStatHelper.recordStringPropertyEvent("has_xiaomi_account", String.valueOf(z));
            if (z) {
                boolean syncAutomatically = ContentResolver.getSyncAutomatically(xiaomiAccount, "com.miui.gallery.cloud.provider");
                boolean isOnlyShowLocalPhoto = GalleryPreferences.LocalMode.isOnlyShowLocalPhoto();
                boolean isAIAlbumEnabled = AIAlbumStatusHelper.isAIAlbumEnabled();
                boolean isFaceAlbumEnabled = AIAlbumStatusHelper.isFaceAlbumEnabled();
                boolean backupOnlyInWifi = GalleryPreferences.Sync.getBackupOnlyInWifi();
                boolean isLocalSearchOpen = AIAlbumStatusHelper.isLocalSearchOpen(true);
                boolean isCloudControlSearchBarOpen = AIAlbumStatusHelper.isCloudControlSearchBarOpen();
                boolean isCloudControlSearchAIAlbumOpen = AIAlbumStatusHelper.isCloudControlSearchAIAlbumOpen();
                boolean isAutoDownload = GalleryPreferences.Sync.isAutoDownload();
                DownloadType downloadType = GalleryPreferences.Sync.getDownloadType();
                SamplingStatHelper.recordStringPropertyEvent("is_cloud_backup_on_c", String.valueOf(syncAutomatically));
                SamplingStatHelper.recordStringPropertyEvent("is_local_mode_on_c", String.valueOf(isOnlyShowLocalPhoto));
                SamplingStatHelper.recordStringPropertyEvent("is_face_album_on_c", String.valueOf(isFaceAlbumEnabled));
                SamplingStatHelper.recordStringPropertyEvent("is_backup_only_wifi_on_c", String.valueOf(backupOnlyInWifi));
                SamplingStatHelper.recordStringPropertyEvent("is_ai_album_on_c", String.valueOf(isAIAlbumEnabled));
                SamplingStatHelper.recordStringPropertyEvent("is_search_ai_album_enabled_c", String.valueOf(isCloudControlSearchAIAlbumOpen));
                SamplingStatHelper.recordStringPropertyEvent("is_search_bar_enabled_c", String.valueOf(isCloudControlSearchBarOpen));
                if (isCloudControlSearchAIAlbumOpen) {
                    SamplingStatHelper.recordStringPropertyEvent("is_search_user_switch_on_c", String.valueOf(isLocalSearchOpen));
                }
                SamplingStatHelper.recordStringPropertyEvent("auto_download_on_c", String.valueOf(isAutoDownload));
                if (isAutoDownload) {
                    SamplingStatHelper.recordStringPropertyEvent("download_type_c", downloadType.toString());
                }
            }
            SamplingStatHelper.recordStringPropertyEvent("baby_lock_wallpaper_in_use", String.valueOf(Intrinsics.areEqual("com.miui.gallery.cloud.baby.wallpaper_provider", LockScreenHelper.getLockWallpaperProvider(GalleryApp.sGetAndroidContext().getContentResolver()))));
        }

        public final boolean isEverSynced() {
            SupportSQLiteDatabase db = GalleryDBHelper.getInstance().getReadableDatabase();
            Intrinsics.checkNotNullExpressionValue(db, "db");
            return safeQueryForLong(db, "SELECT COUNT(*)  FROM cloud WHERE serverType IN (1,2) AND (localFlag = 0 AND serverStatus = 'custom')", null) > 0;
        }

        public final void recordSyncState() {
            Account xiaomiAccount = AccountHelper.getXiaomiAccount(GalleryApp.sGetAndroidContext());
            JsonObject jsonObject = new JsonObject();
            boolean z = false;
            jsonObject.addProperty(MiStat.Event.LOGIN, String.valueOf(xiaomiAccount != null));
            if (xiaomiAccount != null) {
                boolean hasDeviceFeature = DeviceFeatureUtils.hasDeviceFeature("exempt_master_sync_auto");
                boolean xiaomiGlobalSyncAutomatically = SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically();
                boolean syncAutomatically = ContentResolver.getSyncAutomatically(xiaomiAccount, "com.miui.gallery.cloud.provider");
                if (!hasDeviceFeature) {
                    jsonObject.addProperty("master", String.valueOf(xiaomiGlobalSyncAutomatically));
                }
                if (hasDeviceFeature || xiaomiGlobalSyncAutomatically) {
                    jsonObject.addProperty("gallery", String.valueOf(syncAutomatically));
                }
                if (!syncAutomatically) {
                    z = isEverSynced();
                    jsonObject.addProperty("synced", String.valueOf(z));
                    SyncStateUtil.CloudSpaceInfo cloudSpaceInfo = SyncStateUtil.getCloudSpaceInfo(GalleryApp.sGetAndroidContext());
                    long total = cloudSpaceInfo.getTotal();
                    if (total > 0) {
                        jsonObject.addProperty("cloud_space", Integer.valueOf((int) ((((float) (total - cloudSpaceInfo.getUsed())) * 100.0f) / ((float) total))));
                    }
                } else {
                    z = true;
                }
            }
            SamplingStatHelper.recordStringPropertyEvent("sync_state_analysis_1", jsonObject.toString());
            JsonObject jsonObject2 = new JsonObject();
            if (!z) {
                SupportSQLiteDatabase readableDatabase = GalleryDBHelper.getInstance().getReadableDatabase();
                Intrinsics.checkNotNullExpressionValue(readableDatabase, "getInstance().readableDatabase");
                String DIRECTORY_CAMERA_PATH = MIUIStorageConstants.DIRECTORY_CAMERA_PATH;
                Intrinsics.checkNotNullExpressionValue(DIRECTORY_CAMERA_PATH, "DIRECTORY_CAMERA_PATH");
                jsonObject2.addProperty(MiStat.Param.COUNT, Long.valueOf(queryMediaCountByAlbumLocalFile$default(this, readableDatabase, DIRECTORY_CAMERA_PATH, false, false, 12, null)));
                String primaryStoragePath = StorageUtils.getPrimaryStoragePath();
                long totalBytes = StorageUtils.getTotalBytes(primaryStoragePath);
                if (totalBytes > 0) {
                    jsonObject2.addProperty("local_space", Integer.valueOf((int) ((((float) StorageUtils.getAvailableBytes(primaryStoragePath)) * 100.0f) / ((float) totalBytes))));
                }
            }
            jsonObject2.addProperty("downloaded_property", String.valueOf(GalleryPreferences.Sync.isEverAutoDownloaded()));
            SamplingStatHelper.recordStringPropertyEvent("sync_state_analysis_2", jsonObject2.toString());
        }

        public final void recordOtherProperties() {
            new JsonObject().addProperty("external_sd_card", Boolean.valueOf(StorageUtils.hasExternalSDCard(GalleryApp.sGetAndroidContext())));
            boolean isDeviceSupportStoryFunction = MediaFeatureManager.isDeviceSupportStoryFunction();
            if (isDeviceSupportStoryFunction) {
                SamplingStatHelper.recordStringPropertyEvent("assistant_card_function_status", String.valueOf(isDeviceSupportStoryFunction));
            }
        }

        public static /* synthetic */ long safeQueryForLong$default(StatisticsReportWorker statisticsReportWorker, SupportSQLiteDatabase supportSQLiteDatabase, String str, String[] strArr, int i, Object obj) {
            if ((i & 4) != 0) {
                strArr = null;
            }
            return statisticsReportWorker.safeQueryForLong(supportSQLiteDatabase, str, strArr);
        }

        public final long safeQueryForLong(SupportSQLiteDatabase supportSQLiteDatabase, String str, String[] strArr) {
            Object m2569constructorimpl;
            int length;
            try {
                Result.Companion companion = Result.Companion;
                SupportSQLiteStatement compileStatement = supportSQLiteDatabase.compileStatement(str);
                if (strArr != null && 1 <= (length = strArr.length)) {
                    while (true) {
                        int i = length - 1;
                        compileStatement.bindString(length, strArr[length - 1]);
                        if (1 > i) {
                            break;
                        }
                        length = i;
                    }
                }
                m2569constructorimpl = Result.m2569constructorimpl(Long.valueOf(compileStatement.simpleQueryForLong()));
            } catch (Throwable th) {
                Result.Companion companion2 = Result.Companion;
                m2569constructorimpl = Result.m2569constructorimpl(ResultKt.createFailure(th));
            }
            Throwable m2571exceptionOrNullimpl = Result.m2571exceptionOrNullimpl(m2569constructorimpl);
            if (m2571exceptionOrNullimpl == null) {
                return ((Number) m2569constructorimpl).longValue();
            }
            DefaultLogger.e("StatisticsReportWorker", m2571exceptionOrNullimpl);
            return -1L;
        }

        public final long queryMediaCountOfAllAlbums(SupportSQLiteDatabase supportSQLiteDatabase, boolean z) {
            return safeQueryForLong(supportSQLiteDatabase, Intrinsics.stringPlus("SELECT COUNT(*) FROM cloud WHERE serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", z ? "" : " AND (localGroupId NOT IN (SELECT _id FROM album WHERE (attributes & 16 != 0)))"), null);
        }

        public static /* synthetic */ long queryMediaCountOfThirdPartyAlbums$default(StatisticsReportWorker statisticsReportWorker, SupportSQLiteDatabase supportSQLiteDatabase, boolean z, boolean z2, int i, Object obj) {
            if ((i & 2) != 0) {
                z = false;
            }
            if ((i & 4) != 0) {
                z2 = false;
            }
            return statisticsReportWorker.queryMediaCountOfThirdPartyAlbums(supportSQLiteDatabase, z, z2);
        }

        public final long queryMediaCountOfThirdPartyAlbums(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, boolean z2) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT COUNT(*)  FROM cloud WHERE localGroupId in (SELECT _id FROM album WHERE (serverId NOT IN (");
            sb.append((Object) TextUtils.join(",", GalleryContract.Album.ALL_SYSTEM_ALBUM_SERVER_IDS));
            sb.append(") AND localPath NOT NULL AND ");
            sb.append((Object) InternalContract$Album.SELECTION_NON_USER_CREATE_ALBUM);
            sb.append(")) AND serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))");
            sb.append(z ? " AND serverType=2" : "");
            return safeQueryForLong(supportSQLiteDatabase, sb.toString(), null);
        }

        public static /* synthetic */ long queryMediaCountOfUserCreateAlbums$default(StatisticsReportWorker statisticsReportWorker, SupportSQLiteDatabase supportSQLiteDatabase, boolean z, boolean z2, int i, Object obj) {
            if ((i & 2) != 0) {
                z = false;
            }
            if ((i & 4) != 0) {
                z2 = false;
            }
            return statisticsReportWorker.queryMediaCountOfUserCreateAlbums(supportSQLiteDatabase, z, z2);
        }

        public final long queryMediaCountOfUserCreateAlbums(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, boolean z2) {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT COUNT(*)  FROM cloud WHERE localGroupId in (SELECT _id FROM album WHERE localPath like '%");
            sb.append((Object) StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM);
            sb.append("%') AND serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))");
            sb.append(z ? " AND serverType=2" : z2 ? " AND serverType=1" : "");
            return safeQueryForLong(supportSQLiteDatabase, sb.toString(), null);
        }

        public static /* synthetic */ long queryMediaCountByAlbumLocalFile$default(StatisticsReportWorker statisticsReportWorker, SupportSQLiteDatabase supportSQLiteDatabase, String str, boolean z, boolean z2, int i, Object obj) {
            if ((i & 4) != 0) {
                z = false;
            }
            if ((i & 8) != 0) {
                z2 = false;
            }
            return statisticsReportWorker.queryMediaCountByAlbumLocalFile(supportSQLiteDatabase, str, z, z2);
        }

        public final long queryMediaCountByAlbumLocalFile(SupportSQLiteDatabase supportSQLiteDatabase, String str, boolean z, boolean z2) {
            if (TextUtils.isEmpty(str)) {
                return -1L;
            }
            String stringPlus = Intrinsics.stringPlus("SELECT COUNT(*) FROM cloud WHERE localGroupId = (SELECT _id FROM album WHERE LOWER(localPath) = ? LIMIT 1) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", z ? " AND serverType=2" : z2 ? " AND serverType=1" : "");
            Locale US = Locale.US;
            Intrinsics.checkNotNullExpressionValue(US, "US");
            String lowerCase = str.toLowerCase(US);
            Intrinsics.checkNotNullExpressionValue(lowerCase, "this as java.lang.String).toLowerCase(locale)");
            return safeQueryForLong(supportSQLiteDatabase, stringPlus, new String[]{lowerCase});
        }

        public static /* synthetic */ long queryLocalStorageSpaceOfMedia$default(StatisticsReportWorker statisticsReportWorker, SupportSQLiteDatabase supportSQLiteDatabase, boolean z, boolean z2, int i, Object obj) {
            if ((i & 2) != 0) {
                z = false;
            }
            if ((i & 4) != 0) {
                z2 = false;
            }
            return statisticsReportWorker.queryLocalStorageSpaceOfMedia(supportSQLiteDatabase, z, z2);
        }

        public final long queryLocalStorageSpaceOfMedia(SupportSQLiteDatabase supportSQLiteDatabase, boolean z, boolean z2) {
            return safeQueryForLong$default(this, supportSQLiteDatabase, Intrinsics.stringPlus("SELECT SUM(realSize) FROM cloud WHERE (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", z ? " AND serverType=2" : z2 ? " AND serverType=1" : ""), null, 4, null);
        }

        public final long queryMediaCountOfFavoritesAlbum(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*) FROM extended_cloud WHERE (isFavorite NOT NULL AND isFavorite > 0) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", null);
        }

        public final long queryImageCountOf108MP(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*) FROM cloud WHERE serverType = 1 AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND ((exifImageWidth >= 8900  AND exifImageLength >= 11900  AND exifImageWidth <= 9100  AND exifImageLength <= 12100) OR (exifImageLength >= 8900  AND exifImageWidth >= 11900  AND exifImageLength <= 9100  AND exifImageWidth <= 12100))", null);
        }

        public final long queryVideoCountOfAll(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*)  FROM cloud WHERE serverType = 2 AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId NOT IN (SELECT _id FROM album WHERE (attributes & 16 != 0)))", null);
        }

        public final long queryImageCountOfAll(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*)  FROM cloud WHERE serverType = 1 AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId NOT IN (SELECT _id FROM album WHERE (attributes & 16 != 0)))", null);
        }

        public final long queryAlbumCountOfCloudBackup(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*) FROM album WHERE  (attributes & 1 != 0) AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", null);
        }

        public final long querySpecialMediaCountOfSortByModifyAlbum(SupportSQLiteDatabase supportSQLiteDatabase) {
            List<String> albumPathsBySortDate = MediaSortDateHelper.getSortDateProvider().getAlbumPathsBySortDate(MediaSortDateHelper.SortDate.CREATE_TIME);
            Intrinsics.checkNotNullExpressionValue(albumPathsBySortDate, "getSortDateProvider()\n  …per.SortDate.CREATE_TIME)");
            if (!BaseMiscUtil.isValid(albumPathsBySortDate)) {
                return -1L;
            }
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*) FROM cloud WHERE serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND STRFTIME('%Y%m%d',dateModified/1000, 'unixepoch', 'localtime')>STRFTIME('%Y%m%d', CASE WHEN mixedDateTime NOT NULL THEN mixedDateTime ELSE dateTaken END /1000, 'unixepoch', 'localtime') AND localGroupId IN (   SELECT _id   FROM album   WHERE    (attributes & 4 != 0)   AND " + ((Object) InternalContract$Album.ALIAS_NON_SYSTEM_ALBUM) + "   AND localPath COLLATE NOCASE NOT IN ('" + ((Object) TextUtils.join("', '", albumPathsBySortDate)) + "'))", null);
        }

        public final long queryAlbumCountOfRubbish(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*) FROM album WHERE  ( attributes & 2048 <> 0)", null);
        }

        public final long queryAlbumCountOfOwnerCreate(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*) FROM album WHERE localPath like '%" + ((Object) StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM) + "%' AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", null);
        }

        public final long queryAlbumCountOfThirdParty(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*) FROM album WHERE (serverId NOT IN (" + ((Object) TextUtils.join(",", GalleryContract.Album.ALL_SYSTEM_ALBUM_SERVER_IDS)) + ") AND localPath NOT NULL AND " + ((Object) InternalContract$Album.SELECTION_NON_USER_CREATE_ALBUM) + CoreConstants.RIGHT_PARENTHESIS_CHAR, null);
        }

        public final long queryAlbumCountOfOtherShare(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*)FROM shareAlbum WHERE serverId IS NOT NULL", null);
        }

        public final long queryAlbumCountOfUserShare(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(DISTINCT albumId) FROM cloudUser", null);
        }

        public final long queryAlbumCountShowInAlbumPage(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*) FROM album WHERE (attributes & 64 = 0) AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", null);
        }

        public final long queryAlbumCountShowInOtherAlbum(SupportSQLiteDatabase supportSQLiteDatabase) {
            return safeQueryForLong(supportSQLiteDatabase, "SELECT COUNT(*) FROM album WHERE (attributes & 64 <> 0) AND (localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", null);
        }

        public final void recordMediaTypes() {
            Cursor cursor = null;
            try {
                try {
                    cursor = GalleryDBHelper.getInstance().getReadableDatabase().query("SELECT mimeType, COUNT(*)  FROM cloud WHERE serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId NOT IN (SELECT _id FROM album WHERE (attributes & 16 != 0))) GROUP BY mimeType");
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            HashMap hashMap = new HashMap(1);
                            hashMap.put(MiStat.Param.COUNT, String.valueOf(cursor.getInt(1)));
                            SamplingStatHelper.recordCountEvent("media_type_count", Intrinsics.stringPlus("media_count_of_", cursor.getString(0)), hashMap);
                            TrackController.trackStats("403.32.0.1.15134", cursor.getString(0), cursor.getInt(1));
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                BaseMiscUtil.closeSilently(cursor);
            }
        }

        public final void recordLocationInfo() {
            HashSet hashSet;
            Cursor cursor = null;
            try {
                try {
                    cursor = GalleryDBHelper.getInstance().getReadableDatabase().query("SELECT DISTINCT location FROM cloud WHERE location NOT NULL AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))");
                    hashSet = new HashSet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        boolean z = false;
                        String city = LocationManager.getInstance().subToCity(cursor.getString(0));
                        if (city == null || city.length() == 0) {
                            z = true;
                        }
                        if (!z) {
                            Intrinsics.checkNotNullExpressionValue(city, "city");
                            hashSet.add(city);
                        }
                    } while (cursor.moveToNext());
                    TrackController.trackStats("403.32.0.1.12230", hashSet.size());
                }
                TrackController.trackStats("403.32.0.1.12230", hashSet.size());
            } finally {
                BaseMiscUtil.closeSilently(cursor);
            }
        }

        public final void recordTagsCount() {
            Cursor cursor = null;
            try {
                try {
                    cursor = GalleryEntityManager.getInstance().rawQuery("SELECT sceneTag, COUNT(*) FROM (SELECT DISTINCT sceneTag, mediaId FROM MediaScene WHERE mediaId > 0 AND version = 1 AND sceneTag != -1) GROUP BY sceneTag");
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            TrackController.trackStats("403.32.0.1.12231", String.valueOf(cursor.getInt(0)), cursor.getInt(1));
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                BaseMiscUtil.closeSilently(cursor);
            }
        }

        public final void recordMediaTypeCount() {
            try {
                ((AbstractCloudRepository) ModelManager.getInstance().getModel(AbstractCloudRepository.class)).queryMediaTypeCount(AlbumConstants.MedidTypeScene.SCENE_ALBUM_TAB_PAGE).subscribe(StatisticsWorkerProvider$StatisticsReportWorker$$ExternalSyntheticLambda0.INSTANCE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* renamed from: recordMediaTypeCount$lambda-8  reason: not valid java name */
        public static final void m1001recordMediaTypeCount$lambda8(PageResults pageResults) {
            if (pageResults.isFromMemory() || pageResults.isFromDB()) {
                ((Map) pageResults.getResult()).entrySet().forEach(StatisticsWorkerProvider$StatisticsReportWorker$$ExternalSyntheticLambda1.INSTANCE);
            }
        }

        /* renamed from: recordMediaTypeCount$lambda-8$lambda-7  reason: not valid java name */
        public static final void m1002recordMediaTypeCount$lambda8$lambda7(Map.Entry entry) {
            Object key = entry.getKey();
            Intrinsics.checkNotNullExpressionValue(key, "it.key");
            String typeNameByFlag = RequirementHelper$MediaTypeGroup.getTypeNameByFlag(((Number) key).longValue());
            if (!(typeNameByFlag == null || typeNameByFlag.length() == 0)) {
                Object value = entry.getValue();
                Intrinsics.checkNotNullExpressionValue(value, "it.value");
                TrackController.trackStats("403.32.1.1.16674", typeNameByFlag, ((Number) value).intValue());
            }
        }

        public final void recordThirdPartyMediaSource() {
            SupportSQLiteDatabase readableDatabase = GalleryDBHelper.getInstance().getReadableDatabase();
            Cursor cursor = null;
            try {
                try {
                    cursor = readableDatabase.query("SELECT COUNT(*), localPath FROM (album CROSS JOIN cloud ON cloud.localGroupId = album._id) WHERE (album.serverId NOT IN (" + ((Object) TextUtils.join(",", GalleryContract.Album.ALL_SYSTEM_ALBUM_SERVER_IDS)) + ") AND localPath NOT NULL AND " + ((Object) InternalContract$Album.SELECTION_NON_USER_CREATE_ALBUM) + ") GROUP BY localGroupId");
                    if (cursor != null && cursor.moveToFirst()) {
                        do {
                            HashMap hashMap = new HashMap();
                            hashMap.put("tip", "403.32.0.1.13725");
                            hashMap.put(MiStat.Param.COUNT, Integer.valueOf(cursor.getInt(0)));
                            String string = cursor.getString(1);
                            Intrinsics.checkNotNullExpressionValue(string, "it.getString(1)");
                            hashMap.put(nexExportFormat.TAG_FORMAT_PATH, string);
                            TrackController.trackStats(hashMap);
                        } while (cursor.moveToNext());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } finally {
                BaseMiscUtil.closeSilently(cursor);
            }
        }

        public final void recordAlbumsAndImagesCount() {
            SupportSQLiteDatabase db = GalleryDBHelper.getInstance().getReadableDatabase();
            Intrinsics.checkNotNullExpressionValue(db, "db");
            long queryMediaCountOfAllAlbums = queryMediaCountOfAllAlbums(db, false);
            long queryVideoCountOfAll = queryVideoCountOfAll(db);
            long queryImageCountOfAll = queryImageCountOfAll(db);
            String DIRECTORY_CAMERA_PATH = MIUIStorageConstants.DIRECTORY_CAMERA_PATH;
            Intrinsics.checkNotNullExpressionValue(DIRECTORY_CAMERA_PATH, "DIRECTORY_CAMERA_PATH");
            long queryMediaCountByAlbumLocalFile$default = queryMediaCountByAlbumLocalFile$default(this, db, DIRECTORY_CAMERA_PATH, false, false, 12, null);
            Intrinsics.checkNotNullExpressionValue(DIRECTORY_CAMERA_PATH, "DIRECTORY_CAMERA_PATH");
            long queryMediaCountByAlbumLocalFile$default2 = queryMediaCountByAlbumLocalFile$default(this, db, DIRECTORY_CAMERA_PATH, true, false, 8, null);
            Intrinsics.checkNotNullExpressionValue(DIRECTORY_CAMERA_PATH, "DIRECTORY_CAMERA_PATH");
            long queryMediaCountByAlbumLocalFile$default3 = queryMediaCountByAlbumLocalFile$default(this, db, DIRECTORY_CAMERA_PATH, false, true, 4, null);
            String DIRECTORY_SCREENSHOT_PATH = MIUIStorageConstants.DIRECTORY_SCREENSHOT_PATH;
            Intrinsics.checkNotNullExpressionValue(DIRECTORY_SCREENSHOT_PATH, "DIRECTORY_SCREENSHOT_PATH");
            long queryMediaCountByAlbumLocalFile$default4 = queryMediaCountByAlbumLocalFile$default(this, db, DIRECTORY_SCREENSHOT_PATH, false, false, 12, null);
            String DIRECTORY_SCREENRECORDER_PATH = MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH;
            Intrinsics.checkNotNullExpressionValue(DIRECTORY_SCREENRECORDER_PATH, "DIRECTORY_SCREENRECORDER_PATH");
            long queryMediaCountByAlbumLocalFile$default5 = queryMediaCountByAlbumLocalFile$default(this, db, DIRECTORY_SCREENRECORDER_PATH, false, false, 12, null);
            String DIRECTORY_DOWNLOADS = Environment.DIRECTORY_DOWNLOADS;
            Intrinsics.checkNotNullExpressionValue(DIRECTORY_DOWNLOADS, "DIRECTORY_DOWNLOADS");
            long queryMediaCountByAlbumLocalFile$default6 = queryMediaCountByAlbumLocalFile$default(this, db, DIRECTORY_DOWNLOADS, false, false, 12, null);
            long queryMediaCountOfThirdPartyAlbums$default = queryMediaCountOfThirdPartyAlbums$default(this, db, false, false, 6, null);
            long queryMediaCountOfThirdPartyAlbums$default2 = queryMediaCountOfThirdPartyAlbums$default(this, db, true, false, 4, null);
            long queryMediaCountOfThirdPartyAlbums$default3 = queryMediaCountOfThirdPartyAlbums$default(this, db, false, true, 2, null);
            long queryMediaCountOfUserCreateAlbums$default = queryMediaCountOfUserCreateAlbums$default(this, db, false, false, 6, null);
            long queryMediaCountOfUserCreateAlbums$default2 = queryMediaCountOfUserCreateAlbums$default(this, db, true, false, 4, null);
            long queryMediaCountOfUserCreateAlbums$default3 = queryMediaCountOfUserCreateAlbums$default(this, db, false, true, 2, null);
            long queryAlbumCountOfCloudBackup = queryAlbumCountOfCloudBackup(db);
            long queryAlbumCountOfOwnerCreate = queryAlbumCountOfOwnerCreate(db);
            long queryAlbumCountOfRubbish = queryAlbumCountOfRubbish(db);
            long queryAlbumCountOfOtherShare = queryAlbumCountOfOtherShare(db);
            long queryAlbumCountOfUserShare = queryAlbumCountOfUserShare(db);
            long queryAlbumCountShowInAlbumPage = queryAlbumCountShowInAlbumPage(db);
            long queryAlbumCountShowInOtherAlbum = queryAlbumCountShowInOtherAlbum(db);
            long queryAlbumCountOfThirdParty = queryAlbumCountOfThirdParty(db);
            long queryMediaCountOfFavoritesAlbum = queryMediaCountOfFavoritesAlbum(db);
            long querySpecialMediaCountOfSortByModifyAlbum = querySpecialMediaCountOfSortByModifyAlbum(db);
            long queryImageCountOf108MP = queryImageCountOf108MP(db);
            HashMap hashMap = new HashMap();
            if (queryMediaCountOfAllAlbums != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryMediaCountOfAllAlbums));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "image_count_of_all_albums", hashMap);
                TrackController.trackStats("403.32.0.1.12213", queryMediaCountOfAllAlbums);
            }
            if (queryVideoCountOfAll != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryVideoCountOfAll));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "video_count_of_all_albums", hashMap);
                TrackController.trackStats("403.32.0.1.12214", queryVideoCountOfAll);
            }
            if (queryImageCountOfAll != -1) {
                TrackController.trackStats("403.32.0.1.15130", queryImageCountOfAll);
            }
            if (queryMediaCountByAlbumLocalFile$default != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryMediaCountByAlbumLocalFile$default));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "image_count_of_camera_album", hashMap);
                TrackController.trackStats("403.32.0.1.12215", queryMediaCountByAlbumLocalFile$default);
            }
            if (queryMediaCountByAlbumLocalFile$default2 != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryMediaCountByAlbumLocalFile$default2));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "video_count_of_camera_album", hashMap);
                TrackController.trackStats("403.32.0.1.12216", queryMediaCountByAlbumLocalFile$default2);
            }
            if (queryMediaCountByAlbumLocalFile$default3 != -1) {
                TrackController.trackStats("403.32.0.1.15131", queryMediaCountByAlbumLocalFile$default3);
            }
            if (queryMediaCountByAlbumLocalFile$default4 != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryMediaCountByAlbumLocalFile$default4));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "image_count_of_screenshot_album", hashMap);
                TrackController.trackStats("403.32.0.1.12217", queryMediaCountByAlbumLocalFile$default4);
            }
            if (queryMediaCountByAlbumLocalFile$default5 != -1) {
                TrackController.trackStats("403.32.0.1.12218", queryMediaCountByAlbumLocalFile$default5);
            }
            if (queryMediaCountByAlbumLocalFile$default6 != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryMediaCountByAlbumLocalFile$default6));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "image_count_of_download_album", hashMap);
            }
            if (queryAlbumCountOfCloudBackup != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryAlbumCountOfCloudBackup));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "album_count_of_cloud_backup", hashMap);
                TrackController.trackStats("403.32.0.1.12228", queryAlbumCountOfCloudBackup);
            }
            if (queryAlbumCountOfOwnerCreate != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryAlbumCountOfOwnerCreate));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "album_count_of_owner_create", hashMap);
                TrackController.trackStats("403.32.0.1.12223", queryAlbumCountOfOwnerCreate);
            }
            if (queryAlbumCountOfRubbish != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryAlbumCountOfRubbish));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "album_count_of_owner_create", hashMap);
                TrackController.trackStats("403.39.1.1.15347", queryAlbumCountOfRubbish);
            }
            if (queryAlbumCountOfOtherShare != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryAlbumCountOfOtherShare));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "album_count_of_other_share", hashMap);
                TrackController.trackStats("403.32.0.1.12225", queryAlbumCountOfOtherShare);
            }
            if (queryAlbumCountOfUserShare != -1) {
                TrackController.trackStats("403.32.0.1.12229", queryAlbumCountOfUserShare);
            }
            if (queryAlbumCountShowInAlbumPage != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryAlbumCountShowInAlbumPage));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "album_count_show_in_album_page", hashMap);
            }
            if (queryAlbumCountShowInOtherAlbum != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryAlbumCountShowInOtherAlbum));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "album_count_show_in_other_album", hashMap);
                TrackController.trackStats("403.32.0.1.12227", queryAlbumCountShowInOtherAlbum);
            }
            if (queryAlbumCountOfThirdParty != -1) {
                TrackController.trackStats("403.32.0.1.12224", queryAlbumCountOfThirdParty);
            }
            if (queryMediaCountOfFavoritesAlbum != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryMediaCountOfFavoritesAlbum));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "image_count_of_favorites_album", hashMap);
                TrackController.trackStats("403.32.0.1.12226", queryMediaCountOfFavoritesAlbum);
            }
            if (querySpecialMediaCountOfSortByModifyAlbum != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(querySpecialMediaCountOfSortByModifyAlbum));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "special_image_count_of_sort_by_modify_and_show_in_home_album", hashMap);
            }
            if (queryImageCountOf108MP != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryImageCountOf108MP));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "image_count_of_108MP", hashMap);
            }
            if (queryMediaCountOfThirdPartyAlbums$default != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryMediaCountOfThirdPartyAlbums$default));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "media_count_of_third_party_album", hashMap);
                TrackController.trackStats("403.32.0.1.12219", queryMediaCountOfThirdPartyAlbums$default);
            }
            if (queryMediaCountOfThirdPartyAlbums$default2 != -1) {
                hashMap.put(MiStat.Param.COUNT, String.valueOf(queryMediaCountOfThirdPartyAlbums$default2));
                SamplingStatHelper.recordCountEvent("albums_and_images_count", "video_count_of_third_party_album", hashMap);
                TrackController.trackStats("403.32.0.1.12220", queryMediaCountOfThirdPartyAlbums$default);
            }
            if (queryMediaCountOfThirdPartyAlbums$default3 != -1) {
                TrackController.trackStats("403.32.0.1.15132", queryMediaCountOfThirdPartyAlbums$default3);
            }
            if (queryMediaCountOfUserCreateAlbums$default != -1) {
                TrackController.trackStats("403.32.0.1.12221", queryMediaCountOfUserCreateAlbums$default);
            }
            if (queryMediaCountOfUserCreateAlbums$default2 != -1) {
                TrackController.trackStats("403.32.0.1.12222", queryMediaCountOfUserCreateAlbums$default2);
            }
            if (queryMediaCountOfUserCreateAlbums$default3 != -1) {
                TrackController.trackStats("403.32.0.1.15133", queryMediaCountOfUserCreateAlbums$default3);
            }
        }

        public final long queryMediaSpace(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
            String str2 = "SELECT SUM(size) FROM cloud WHERE serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId NOT IN (SELECT _id FROM album WHERE attributes&64=0))";
            if (!TextUtils.isEmpty(str)) {
                str2 = str2 + " AND (" + ((Object) str) + CoreConstants.RIGHT_PARENTHESIS_CHAR;
            }
            return safeQueryForLong$default(this, supportSQLiteDatabase, str2, null, 4, null);
        }

        public final void recordLocalStorageOfMedia() {
            long j;
            long j2;
            SupportSQLiteDatabase db = GalleryDBHelper.getInstance().getReadableDatabase();
            long totalBytes = StorageUtils.getTotalBytes(StorageUtils.getPrimaryStoragePath());
            Intrinsics.checkNotNullExpressionValue(db, "db");
            long queryLocalStorageSpaceOfMedia$default = queryLocalStorageSpaceOfMedia$default(this, db, false, false, 6, null);
            long queryLocalStorageSpaceOfMedia$default2 = queryLocalStorageSpaceOfMedia$default(this, db, false, true, 2, null);
            long queryLocalStorageSpaceOfMedia$default3 = queryLocalStorageSpaceOfMedia$default(this, db, true, false, 4, null);
            if (queryLocalStorageSpaceOfMedia$default != -1) {
                HashMap hashMap = new HashMap();
                j = queryLocalStorageSpaceOfMedia$default3;
                hashMap.put("tip", "403.32.0.1.16274");
                hashMap.put(MiStat.Param.COUNT, Long.valueOf(queryLocalStorageSpaceOfMedia$default));
                hashMap.put("value", String.valueOf(((queryLocalStorageSpaceOfMedia$default * 1.0d) / (totalBytes * 1.0d)) * 100));
                TrackController.trackStats(hashMap);
                j2 = -1;
            } else {
                j = queryLocalStorageSpaceOfMedia$default3;
                j2 = -1;
            }
            if (queryLocalStorageSpaceOfMedia$default2 != j2) {
                HashMap hashMap2 = new HashMap();
                hashMap2.put("tip", "403.32.0.1.16275");
                hashMap2.put(MiStat.Param.COUNT, Long.valueOf(queryLocalStorageSpaceOfMedia$default2));
                hashMap2.put("value", String.valueOf(((queryLocalStorageSpaceOfMedia$default2 * 1.0d) / (totalBytes * 1.0d)) * 100));
                TrackController.trackStats(hashMap2);
            }
            if (j != -1) {
                HashMap hashMap3 = new HashMap();
                long j3 = j;
                hashMap3.put("tip", "403.32.0.1.16276");
                hashMap3.put(MiStat.Param.COUNT, Long.valueOf(j3));
                hashMap3.put("value", String.valueOf(((j3 * 1.0d) / (totalBytes * 1.0d)) * 100));
                TrackController.trackStats(hashMap3);
            }
        }

        public final void recordCardsCount() {
            List query = GalleryEntityManager.getInstance().query(Card.class, "ignored = 0 AND localFlag NOT IN (1,-2,-1,4)", null, null, null);
            TrackController.trackStats("403.32.0.1.16457", query == null ? 0L : query.size());
        }

        public final long queryAlbumAttributes(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
            return safeQueryForLong$default(this, supportSQLiteDatabase, "SELECT attributes FROM album WHERE localPath LIKE '" + str + CoreConstants.SINGLE_QUOTE_CHAR, null, 4, null);
        }

        public final void recordMediasSpace() {
            SupportSQLiteDatabase supportSQLiteDatabase;
            SupportSQLiteDatabase db = GalleryDBHelper.getInstance().getReadableDatabase();
            Intrinsics.checkNotNullExpressionValue(db, "db");
            long queryMediaSpace = queryMediaSpace(db, null);
            if (queryMediaSpace == -1) {
                return;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, formatSize(queryMediaSpace));
            SamplingStatHelper.recordCountEvent("medias_space", "medias_space_of_all", hashMap);
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            Locale locale = Locale.US;
            String format = String.format(locale, "localGroupId IN (SELECT _id FROM album WHERE localPath LIKE '%s')", Arrays.copyOf(new Object[]{"DCIM/Camera"}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "format(locale, format, *args)");
            long queryMediaSpace2 = queryMediaSpace(db, Intrinsics.stringPlus("serverType=1 AND ", format));
            HashMap hashMap2 = new HashMap();
            if (queryMediaSpace2 != -1) {
                supportSQLiteDatabase = db;
                String format2 = String.format(locale, "medias_space_ratio_of_album_%s", Arrays.copyOf(new Object[]{"Camera-Image"}, 1));
                Intrinsics.checkNotNullExpressionValue(format2, "format(locale, format, *args)");
                hashMap2.put("percentage", String.valueOf(formatProportion((((float) queryMediaSpace2) * 1.0f) / ((float) queryMediaSpace))));
                SamplingStatHelper.recordCountEvent("medias_space", format2, hashMap2);
            } else {
                supportSQLiteDatabase = db;
            }
            String format3 = String.format(locale, "localGroupId IN (SELECT _id FROM album WHERE localPath LIKE '%s')", Arrays.copyOf(new Object[]{"DCIM/Camera"}, 1));
            Intrinsics.checkNotNullExpressionValue(format3, "format(locale, format, *args)");
            SupportSQLiteDatabase supportSQLiteDatabase2 = supportSQLiteDatabase;
            long queryMediaSpace3 = queryMediaSpace(supportSQLiteDatabase2, Intrinsics.stringPlus("serverType=2 AND ", format3));
            if (queryMediaSpace3 != -1) {
                String format4 = String.format(locale, "medias_space_ratio_of_album_%s", Arrays.copyOf(new Object[]{"Camera-Video"}, 1));
                Intrinsics.checkNotNullExpressionValue(format4, "format(locale, format, *args)");
                hashMap2.put("percentage", String.valueOf(formatProportion((((float) queryMediaSpace3) * 1.0f) / ((float) queryMediaSpace))));
                SamplingStatHelper.recordCountEvent("medias_space", format4, hashMap2);
            }
            long queryMediaSpace4 = queryMediaSpace(supportSQLiteDatabase2, "serverType=2");
            if (queryMediaSpace4 != -1) {
                String format5 = String.format(locale, "medias_space_ratio_of_album_%s", Arrays.copyOf(new Object[]{"Video"}, 1));
                Intrinsics.checkNotNullExpressionValue(format5, "format(locale, format, *args)");
                hashMap2.put("percentage", String.valueOf(formatProportion((((float) queryMediaSpace4) * 1.0f) / ((float) queryMediaSpace))));
                SamplingStatHelper.recordCountEvent("medias_space", format5, hashMap2);
            }
            long queryAlbumAttributes = queryAlbumAttributes(supportSQLiteDatabase2, "tencent/micromsg/weixin");
            if (queryAlbumAttributes == -1 || (queryAlbumAttributes & 64) != 0) {
                return;
            }
            String format6 = String.format(locale, "localGroupId IN (SELECT _id FROM album WHERE localPath LIKE '%s')", Arrays.copyOf(new Object[]{"tencent/micromsg/weixin"}, 1));
            Intrinsics.checkNotNullExpressionValue(format6, "format(locale, format, *args)");
            long queryMediaSpace5 = queryMediaSpace(supportSQLiteDatabase2, format6);
            if (queryMediaSpace5 == -1) {
                return;
            }
            String format7 = String.format(locale, "medias_space_ratio_of_album_%s", Arrays.copyOf(new Object[]{"weixin"}, 1));
            Intrinsics.checkNotNullExpressionValue(format7, "format(locale, format, *args)");
            hashMap2.put("percentage", String.valueOf(formatProportion((((float) queryMediaSpace5) * 1.0f) / ((float) queryMediaSpace))));
            SamplingStatHelper.recordCountEvent("medias_space", format7, hashMap2);
        }

        public final String formatSize(long j) {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String format = String.format(Locale.US, "%.1fG", Arrays.copyOf(new Object[]{Float.valueOf((((float) (j / 500000000)) * 1.0f) / 2)}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "format(locale, format, *args)");
            return format;
        }

        public final void recordPeopleProperties() {
            String num;
            List<Long> queryPeopleIdOfRelation = FaceManager.queryPeopleIdOfRelation(GalleryApp.sGetAndroidContext(), PeopleContactInfo.Relation.myself.getRelationType());
            String str = "0";
            if (queryPeopleIdOfRelation != null && (num = Integer.valueOf(queryPeopleIdOfRelation.size()).toString()) != null) {
                str = num;
            }
            SamplingStatHelper.recordStringPropertyEvent("people_count_under_myself", str);
        }

        /* JADX WARN: Removed duplicated region for block: B:16:0x007e A[Catch: all -> 0x00dc, TryCatch #0 {all -> 0x00dc, blocks: (B:3:0x0008, B:5:0x003e, B:7:0x0044, B:11:0x0068, B:10:0x0054, B:13:0x006e, B:14:0x0078, B:16:0x007e, B:17:0x0093, B:19:0x0099, B:20:0x00a6, B:22:0x00ac, B:25:0x00b9, B:28:0x00cc, B:29:0x00d0), top: B:38:0x0008 }] */
        /* JADX WARN: Removed duplicated region for block: B:35:0x00ed  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x00f0  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void recordDirtyData() {
            /*
                r12 = this;
                com.miui.gallery.provider.GalleryDBHelper r0 = com.miui.gallery.provider.GalleryDBHelper.getInstance()
                androidx.sqlite.db.SupportSQLiteDatabase r0 = r0.getReadableDatabase()
                kotlin.Result$Companion r1 = kotlin.Result.Companion     // Catch: java.lang.Throwable -> Ldc
                java.util.HashMap r1 = new java.util.HashMap     // Catch: java.lang.Throwable -> Ldc
                r1.<init>()     // Catch: java.lang.Throwable -> Ldc
                java.lang.String r2 = "temp"
                java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Ldc
                r3.<init>()     // Catch: java.lang.Throwable -> Ldc
                java.lang.String r4 = "select "
                r3.append(r4)     // Catch: java.lang.Throwable -> Ldc
                r3.append(r2)     // Catch: java.lang.Throwable -> Ldc
                java.lang.String r4 = ".localGroupId, album.localPath from (select distinct localGroupId, groupId from cloud where localGroupId in (select localGroupId from (select distinct localGroupId, groupId from cloud where groupId is not null and groupId != '' and localFlag not in (-3,-2,-1,5,6,9,11,15))group by localGroupId having count(localGroupId)>1)) as "
                r3.append(r4)     // Catch: java.lang.Throwable -> Ldc
                r3.append(r2)     // Catch: java.lang.Throwable -> Ldc
                java.lang.String r4 = " left join album on "
                r3.append(r4)     // Catch: java.lang.Throwable -> Ldc
                r3.append(r2)     // Catch: java.lang.Throwable -> Ldc
                java.lang.String r2 = ".groupId = album.serverId"
                r3.append(r2)     // Catch: java.lang.Throwable -> Ldc
                java.lang.String r2 = r3.toString()     // Catch: java.lang.Throwable -> Ldc
                android.database.Cursor r0 = r0.query(r2)     // Catch: java.lang.Throwable -> Ldc
                r2 = 0
                if (r0 == 0) goto L6e
                boolean r3 = r0.moveToFirst()     // Catch: java.lang.Throwable -> Ldc
                if (r3 == 0) goto L6e
            L44:
                long r3 = r0.getLong(r2)     // Catch: java.lang.Throwable -> Ldc
                r5 = 1
                java.lang.String r5 = r0.getString(r5)     // Catch: java.lang.Throwable -> Ldc
                boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Throwable -> Ldc
                if (r6 == 0) goto L54
                goto L68
            L54:
                java.lang.Long r3 = java.lang.Long.valueOf(r3)     // Catch: java.lang.Throwable -> Ldc
                com.miui.gallery.job.workers.StatisticsWorkerProvider$StatisticsReportWorker$$ExternalSyntheticLambda2 r4 = com.miui.gallery.job.workers.StatisticsWorkerProvider$StatisticsReportWorker$$ExternalSyntheticLambda2.INSTANCE     // Catch: java.lang.Throwable -> Ldc
                java.lang.Object r3 = r1.computeIfAbsent(r3, r4)     // Catch: java.lang.Throwable -> Ldc
                java.util.Set r3 = (java.util.Set) r3     // Catch: java.lang.Throwable -> Ldc
                java.lang.String r4 = "localPath"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r5, r4)     // Catch: java.lang.Throwable -> Ldc
                r3.add(r5)     // Catch: java.lang.Throwable -> Ldc
            L68:
                boolean r3 = r0.moveToNext()     // Catch: java.lang.Throwable -> Ldc
                if (r3 != 0) goto L44
            L6e:
                r3 = 0
                java.util.Set r0 = r1.entrySet()     // Catch: java.lang.Throwable -> Ldc
                java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> Ldc
            L78:
                boolean r1 = r0.hasNext()     // Catch: java.lang.Throwable -> Ldc
                if (r1 == 0) goto Ld0
                java.lang.Object r1 = r0.next()     // Catch: java.lang.Throwable -> Ldc
                java.util.Map$Entry r1 = (java.util.Map.Entry) r1     // Catch: java.lang.Throwable -> Ldc
                java.util.LinkedList r5 = new java.util.LinkedList     // Catch: java.lang.Throwable -> Ldc
                r5.<init>()     // Catch: java.lang.Throwable -> Ldc
                java.lang.Object r1 = r1.getValue()     // Catch: java.lang.Throwable -> Ldc
                java.util.Set r1 = (java.util.Set) r1     // Catch: java.lang.Throwable -> Ldc
                java.util.Iterator r1 = r1.iterator()     // Catch: java.lang.Throwable -> Ldc
            L93:
                boolean r6 = r1.hasNext()     // Catch: java.lang.Throwable -> Ldc
                if (r6 == 0) goto L78
                java.lang.Object r6 = r1.next()     // Catch: java.lang.Throwable -> Ldc
                java.lang.String r6 = (java.lang.String) r6     // Catch: java.lang.Throwable -> Ldc
                r5.add(r6)     // Catch: java.lang.Throwable -> Ldc
                java.util.Iterator r7 = r5.iterator()     // Catch: java.lang.Throwable -> Ldc
            La6:
                boolean r8 = r7.hasNext()     // Catch: java.lang.Throwable -> Ldc
                if (r8 == 0) goto L93
                java.lang.Object r8 = r7.next()     // Catch: java.lang.Throwable -> Ldc
                java.lang.String r8 = (java.lang.String) r8     // Catch: java.lang.Throwable -> Ldc
                boolean r9 = kotlin.jvm.internal.Intrinsics.areEqual(r8, r6)     // Catch: java.lang.Throwable -> Ldc
                if (r9 == 0) goto Lb9
                goto La6
            Lb9:
                java.lang.String r9 = "path"
                kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r8, r9)     // Catch: java.lang.Throwable -> Ldc
                r9 = 0
                r10 = 2
                boolean r11 = kotlin.text.StringsKt__StringsKt.contains$default(r6, r8, r2, r10, r9)     // Catch: java.lang.Throwable -> Ldc
                boolean r8 = kotlin.text.StringsKt__StringsKt.contains$default(r8, r6, r2, r10, r9)     // Catch: java.lang.Throwable -> Ldc
                r8 = r8 | r11
                if (r8 == 0) goto Lcc
                goto La6
            Lcc:
                r6 = 1
                long r3 = r3 + r6
                goto L93
            Ld0:
                java.lang.String r0 = "403.32.0.1.12216"
                com.miui.gallery.analytics.TrackController.trackStats(r0, r3)     // Catch: java.lang.Throwable -> Ldc
                kotlin.Unit r0 = kotlin.Unit.INSTANCE     // Catch: java.lang.Throwable -> Ldc
                java.lang.Object r0 = kotlin.Result.m2569constructorimpl(r0)     // Catch: java.lang.Throwable -> Ldc
                goto Le7
            Ldc:
                r0 = move-exception
                kotlin.Result$Companion r1 = kotlin.Result.Companion
                java.lang.Object r0 = kotlin.ResultKt.createFailure(r0)
                java.lang.Object r0 = kotlin.Result.m2569constructorimpl(r0)
            Le7:
                java.lang.Throwable r1 = kotlin.Result.m2571exceptionOrNullimpl(r0)
                if (r1 != 0) goto Lf0
                kotlin.Unit r0 = (kotlin.Unit) r0
                goto Lf5
            Lf0:
                java.lang.String r0 = "StatisticsReportWorker"
                com.miui.gallery.util.logger.DefaultLogger.e(r0, r1)
            Lf5:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.job.workers.StatisticsWorkerProvider.StatisticsReportWorker.recordDirtyData():void");
        }

        /* renamed from: recordDirtyData$lambda-13$lambda-12$lambda-11$lambda-10  reason: not valid java name */
        public static final Set m1000recordDirtyData$lambda13$lambda12$lambda11$lambda10(Long it) {
            Intrinsics.checkNotNullParameter(it, "it");
            return new HashSet();
        }

        @Override // com.miui.gallery.job.workers.TrackedWorker
        public ListenableWorker.Result doWork() {
            recordGallerySettings();
            recordAlbumsAndImagesCount();
            recordMediaTypes();
            recordLocationInfo();
            recordTagsCount();
            recordMediasSpace();
            recordSyncState();
            recordOtherProperties();
            recordPeopleProperties();
            recordThirdPartyMediaSource();
            recordDirtyData();
            recordLocalStorageOfMedia();
            recordCardsCount();
            recordMediaTypeCount();
            ListenableWorker.Result success = ListenableWorker.Result.success();
            Intrinsics.checkNotNullExpressionValue(success, "success()");
            return success;
        }

        /* compiled from: StatisticsWorkerProvider.kt */
        /* loaded from: classes2.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            public Companion() {
            }
        }
    }
}
