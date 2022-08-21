package com.miui.gallery.migrate;

import android.content.Context;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.migrate.migrator.AbsMigrator;
import com.miui.gallery.migrate.migrator.StorageMigratorFactory;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class StorageMigrationManager {
    /* renamed from: $r8$lambda$WhKk2Z7bsayUkuDR7v-wDpn0E_I */
    public static /* synthetic */ void m1089$r8$lambda$WhKk2Z7bsayUkuDR7vwDpn0E_I(Context context) {
        lambda$migrate$0(context);
    }

    public static boolean checkPermission(Context context) {
        boolean z;
        String[] absolutePath = StorageUtils.getAbsolutePath(context, "MIUI/Gallery");
        if (absolutePath == null) {
            return false;
        }
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("StorageMigrationManager", "checkPermission");
        int length = absolutePath.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                z = false;
                break;
            }
            if (StorageSolutionProvider.get().getDocumentFile(absolutePath[i], IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, appendInvokerTag) != null) {
                z = true;
                break;
            }
            i++;
        }
        if (!z) {
            return true;
        }
        for (IStoragePermissionStrategy.PermissionResult permissionResult : StorageSolutionProvider.get().checkPermission(Arrays.asList(absolutePath), IStoragePermissionStrategy.Permission.DELETE)) {
            if (!permissionResult.granted) {
                return false;
            }
        }
        return true;
    }

    public static void migrate(final Context context) {
        ThreadManager.execute(BaiduSceneResult.BLACK_WHITE, new Runnable() { // from class: com.miui.gallery.migrate.StorageMigrationManager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                StorageMigrationManager.m1089$r8$lambda$WhKk2Z7bsayUkuDR7vwDpn0E_I(context);
            }
        });
    }

    public static /* synthetic */ void lambda$migrate$0(Context context) {
        if (!checkPermission(context)) {
            return;
        }
        long albumMigrationState = GalleryPreferences.Album.getAlbumMigrationState();
        long migrateStrategyVersion = CloudControlStrategyHelper.getMigrateStrategyVersion();
        if (albumMigrationState == migrateStrategyVersion) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        List<AbsMigrator> create = StorageMigratorFactory.create(context, albumMigrationState);
        if (!BaseMiscUtil.isValid(create)) {
            return;
        }
        long j = migrateStrategyVersion;
        for (AbsMigrator absMigrator : create) {
            try {
                try {
                } catch (Exception e) {
                    DefaultLogger.e("StorageMigrationManager", e);
                }
                if (!absMigrator.migrate()) {
                    j &= ~absMigrator.getVersion();
                    DefaultLogger.e("StorageMigrationManager", "%s failed.", absMigrator);
                }
            } catch (Throwable th) {
                absMigrator.getVersion();
                DefaultLogger.e("StorageMigrationManager", "%s failed.", absMigrator);
                throw th;
            }
        }
        DefaultLogger.d("StorageMigrationManager", "migrate done from version [%d] to [%d].", Long.valueOf(albumMigrationState), Long.valueOf(j));
        GalleryPreferences.Album.applyAlbumMigrationState(true, j);
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.57.0.1.14600");
        hashMap.put("duration", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        hashMap.put("old_ver_code", Long.valueOf(albumMigrationState));
        hashMap.put("last_ver_code", Long.valueOf(migrateStrategyVersion));
        hashMap.put("cur_ver_code", Long.valueOf(j));
        TrackController.trackStats(hashMap);
    }
}
