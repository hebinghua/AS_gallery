package com.miui.gallery.provider;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.FavoriteInfo;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;

/* loaded from: classes2.dex */
public class FavoritesManager {
    public static final String[] PROJECTION = {j.c, "sha1", "localGroupId", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE};
    public static String TAG = "FavoritesManager";

    public static Cursor queryCloudItemByPath(Context context, String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag(TAG, "queryCloudItemByPath"));
        if (documentFile == null || !documentFile.exists()) {
            return context.getContentResolver().query(GalleryContract.Cloud.CLOUD_URI, PROJECTION, "(localFile LIKE ? or thumbnailFile LIKE ?) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", new String[]{str, str}, null);
        }
        String relativePath = StorageUtils.getRelativePath(context, BaseFileUtils.getParentFolderPath(str));
        if (TextUtils.isEmpty(relativePath)) {
            DefaultLogger.w(TAG, "Could't get album path for %s", str);
            return null;
        }
        Cursor query = context.getContentResolver().query(GalleryContract.Cloud.CLOUD_URI, PROJECTION, "fileName LIKE ? AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND localGroupId IN (SELECT _id FROM album WHERE localPath LIKE ?)", new String[]{BaseFileUtils.getFileName(str), relativePath}, null);
        if (!z || query == null || !query.moveToFirst() || query.getLong(3) == BaseFileUtils.getFileSize(str)) {
            return query;
        }
        DefaultLogger.w(TAG, "file size not equals, can not favorite: %s", str);
        BaseMiscUtil.closeSilently(query);
        return null;
    }

    public static void queryFavoriteInfoStateByFilePath(FavoriteInfo favoriteInfo, String str, boolean z) {
        if (favoriteInfo == null) {
            return;
        }
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Cursor cursor = null;
        try {
            try {
                Cursor queryCloudItemByPath = !TextUtils.isEmpty(str) ? queryCloudItemByPath(sGetAndroidContext, str, z) : null;
                if (queryCloudItemByPath != null) {
                    try {
                        if (queryCloudItemByPath.moveToFirst()) {
                            boolean z2 = true;
                            if (queryCloudItemByPath.getLong(2) != -1000) {
                                Integer num = (Integer) SafeDBUtil.safeQuery(sGetAndroidContext, GalleryContract.Favorites.URI, new String[]{"isFavorite"}, "cloud_id = ?", new String[]{String.valueOf(Long.valueOf(queryCloudItemByPath.getLong(0)))}, (String) null, new SafeDBUtil.QueryHandler<Integer>() { // from class: com.miui.gallery.provider.FavoritesManager.1
                                    /* JADX WARN: Can't rename method to resolve collision */
                                    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                                    /* renamed from: handle */
                                    public Integer mo1808handle(Cursor cursor2) {
                                        if (cursor2 != null && cursor2.moveToFirst()) {
                                            return Integer.valueOf(cursor2.getInt(0));
                                        }
                                        return 0;
                                    }
                                });
                                if (num == null || num.intValue() <= 0) {
                                    z2 = false;
                                }
                                favoriteInfo.setFavorite(z2);
                            }
                        }
                    } catch (Exception e) {
                        e = e;
                        cursor = queryCloudItemByPath;
                        DefaultLogger.e(TAG, e);
                        BaseMiscUtil.closeSilently(cursor);
                        return;
                    } catch (Throwable th) {
                        th = th;
                        cursor = queryCloudItemByPath;
                        BaseMiscUtil.closeSilently(cursor);
                        throw th;
                    }
                }
                BaseMiscUtil.closeSilently(queryCloudItemByPath);
            } catch (Exception e2) {
                e = e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
