package com.miui.gallery.backup;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.google.protobuf.BoolValue;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.backup.GalleryBackupProtos;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.scanner.core.scanner.MediaScannerHelper;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class GalleryBackupHelper {
    private static final String TAG = "GalleryBackupHelper";
    public static final String TEMP_BACKUP_STORED;
    private static final String TEMP_BACKUP_STORED_DIR;

    public static /* synthetic */ Object $r8$lambda$KSdAKJIpNIr8zLBepPLTa2KgxY4(List list, Cursor cursor) {
        return lambda$backupAlbumProfiles$0(list, cursor);
    }

    static {
        String str = GalleryApp.sGetAndroidContext().getFilesDir() + "/backup";
        TEMP_BACKUP_STORED_DIR = str;
        TEMP_BACKUP_STORED = str + "/message_temp";
    }

    public static GalleryBackupProtos.BackupMessage backup(int i) {
        long currentTimeMillis = System.currentTimeMillis();
        HashMap hashMap = new HashMap();
        GalleryBackupProtos.BackupMessage.Builder addAllAlbumProfiles = GalleryBackupProtos.BackupMessage.newBuilder().setSettings(backupSettings()).addAllAlbumProfiles(backupAlbumProfiles(hashMap));
        if (i == 1) {
            addAllAlbumProfiles.addAllCloudProfiles(backupCloudProfiles(hashMap));
        }
        hashMap.put("cost_time", String.valueOf(System.currentTimeMillis() - currentTimeMillis));
        SamplingStatHelper.recordCountEvent("local_backup", "full_backup", hashMap);
        return addAllAlbumProfiles.mo403build();
    }

    private static GalleryBackupProtos.BackupMessage.Settings backupSettings() {
        return GalleryBackupProtos.BackupMessage.Settings.newBuilder().setOnlyShowLocalPhoto(GalleryPreferences.LocalMode.isOnlyShowLocalPhoto()).setShowHiddenAlbum(GalleryPreferences.HiddenAlbum.isShowHiddenAlbum()).setSlideshowInterval(GalleryPreferences.SlideShow.getSlideShowInterval()).setRemindConnectNetworkEveryTime(BaseGalleryPreferences.CTA.remindConnectNetworkEveryTime()).setSelectBestPhotoEnable(BoolValue.of(GalleryPreferences.Assistant.isImageSelectionFunctionOn())).setMemoriesEnable(BoolValue.of(GalleryPreferences.Assistant.isStoryFunctionOn())).setSlideShowLoop(BoolValue.of(GalleryPreferences.SlideShow.isSlideShowLoop())).setAutoConvertHeifToJpegEnable(BoolValue.of(GalleryPreferences.IncompatibleMedia.isIncompatibleMediaAutoConvert())).mo403build();
    }

    private static List<GalleryBackupProtos.BackupMessage.AlbumProfile> backupAlbumProfiles(Map<String, String> map) {
        final LinkedList linkedList = new LinkedList();
        Uri build = GalleryContract.Album.URI.buildUpon().appendQueryParameter("param_exclude_empty_album", "true").appendQueryParameter("param_exclude_hidden_album", "false").build();
        SafeDBUtil.safeQuery(StaticContext.sGetAndroidContext(), build, new String[]{"localPath", "attributes", "name"}, "attributes & 48 <> 16", (String[]) null, (String) null, new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.backup.GalleryBackupHelper$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public final Object mo1808handle(Cursor cursor) {
                return GalleryBackupHelper.$r8$lambda$KSdAKJIpNIr8zLBepPLTa2KgxY4(linkedList, cursor);
            }
        });
        DefaultLogger.i(TAG, "Backup %d album profiles", Integer.valueOf(linkedList.size()));
        map.put("album_count", String.valueOf(linkedList.size()));
        return linkedList;
    }

    public static /* synthetic */ Object lambda$backupAlbumProfiles$0(List list, Cursor cursor) {
        if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(GalleryBackupProtos.BackupMessage.AlbumProfile.newBuilder().setPath(cursor.getString(0)).setAttributes(cursor.getLong(1)).setName(cursor.getString(2)).mo403build());
                cursor.moveToNext();
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:63:0x01ec, code lost:
        r1.close();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static java.util.List<com.miui.gallery.backup.GalleryBackupProtos.BackupMessage.CloudProfile> backupCloudProfiles(java.util.Map<java.lang.String, java.lang.String> r29) {
        /*
            Method dump skipped, instructions count: 496
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.backup.GalleryBackupHelper.backupCloudProfiles(java.util.Map):java.util.List");
    }

    public static void restore(GalleryBackupProtos.BackupMessage backupMessage) {
        if (backupMessage == null) {
            DefaultLogger.w(TAG, "Backup message is null");
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        HashMap hashMap = new HashMap();
        restoreSettings(backupMessage.getSettings());
        restoreAlbumProfiles(backupMessage.getAlbumProfilesList());
        restoreCloudProfiles(backupMessage);
        hashMap.put("cost_time", String.valueOf(System.currentTimeMillis() - currentTimeMillis));
        SamplingStatHelper.recordCountEvent("local_backup", "full_restore", hashMap);
    }

    private static void restoreSettings(GalleryBackupProtos.BackupMessage.Settings settings) {
        if (settings == null) {
            return;
        }
        GalleryPreferences.LocalMode.setOnlyShowLocalPhoto(settings.getOnlyShowLocalPhoto());
        GalleryPreferences.HiddenAlbum.setShowHiddenAlbum(settings.getShowHiddenAlbum());
        GalleryPreferences.SlideShow.setSlideShowInterval(settings.getSlideshowInterval());
        boolean z = true;
        boolean value = settings.getAutoConvertHeifToJpegEnable() == BoolValue.getDefaultInstance() ? true : settings.getAutoConvertHeifToJpegEnable().getValue();
        boolean value2 = settings.getSelectBestPhotoEnable() == BoolValue.getDefaultInstance() ? true : settings.getSelectBestPhotoEnable().getValue();
        if (settings.getMemoriesEnable() != BoolValue.getDefaultInstance()) {
            z = settings.getMemoriesEnable().getValue();
        }
        boolean value3 = settings.getSlideShowLoop() == BoolValue.getDefaultInstance() ? false : settings.getSlideShowLoop().getValue();
        GalleryPreferences.Assistant.setImageSelectionFunctionState(value2);
        GalleryPreferences.Assistant.setStoryFunctionState(z);
        GalleryPreferences.IncompatibleMedia.setIncompatibleMediaAutoConvert(value);
        GalleryPreferences.SlideShow.setSlideShowLoop(value3);
    }

    private static void restoreAlbumProfiles(List<GalleryBackupProtos.BackupMessage.AlbumProfile> list) {
        if (list == null || list.size() <= 0) {
            DefaultLogger.i(TAG, "No album profile exists");
            return;
        }
        int i = 0;
        ContentValues contentValues = new ContentValues();
        for (GalleryBackupProtos.BackupMessage.AlbumProfile albumProfile : list) {
            if (!TextUtils.isEmpty(albumProfile.getPath())) {
                contentValues.clear();
                contentValues.put("attributes", Long.valueOf(albumProfile.getAttributes()));
                if (!TextUtils.isEmpty(albumProfile.getName())) {
                    contentValues.put("name", albumProfile.getName());
                }
                MediaScannerHelper.updateOrInsertAlbum(StaticContext.sGetAndroidContext(), albumProfile.getPath(), contentValues);
                i++;
            }
        }
        if (i > 0) {
            GalleryPreferences.MediaScanner.recordAlbumRestoreTimeMillis();
        }
        DefaultLogger.i(TAG, "Restore %d album profiles", Integer.valueOf(i));
    }

    private static void restoreCloudProfiles(GalleryBackupProtos.BackupMessage backupMessage) {
        storeMessage(backupMessage);
        cacheCloudProfile(backupMessage);
    }

    private static void storeMessage(GalleryBackupProtos.BackupMessage backupMessage) {
        FileOutputStream fileOutputStream;
        long currentTimeMillis = System.currentTimeMillis();
        String str = TEMP_BACKUP_STORED_DIR;
        File file = new File(str);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag(TAG, "storeMessage");
        if (!file.exists()) {
            StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag);
        }
        try {
            try {
                File createTempFile = File.createTempFile("message", ".tmp", file);
                try {
                    fileOutputStream = new FileOutputStream(createTempFile);
                } catch (IOException e) {
                    DefaultLogger.e(TAG, "store message failed, %s.", e.getMessage());
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(createTempFile.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile != null) {
                        documentFile.delete();
                    }
                }
                try {
                    backupMessage.writeTo(fileOutputStream);
                    StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                    String str2 = TEMP_BACKUP_STORED;
                    DocumentFile documentFile2 = storageStrategyManager.getDocumentFile(str2, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile2 != null && documentFile2.exists()) {
                        documentFile2.delete();
                    }
                    StorageSolutionProvider.get().moveFile(createTempFile.getAbsolutePath(), str2, appendInvokerTag);
                    fileOutputStream.close();
                } catch (Throwable th) {
                    try {
                        fileOutputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } finally {
                DefaultLogger.d(TAG, "store message cost [%d] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            }
        } catch (IOException e2) {
            DefaultLogger.e(TAG, "store message failed, %s.", e2.getMessage());
        }
    }

    public static void restoreCloudProfiles() {
        if (BaseMiscUtil.isValid((Map) ScanCache.getInstance().get("key_mi_mover_cloud_profiles"))) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        try {
            try {
                FileInputStream fileInputStream = new FileInputStream(TEMP_BACKUP_STORED);
                try {
                    GalleryBackupProtos.BackupMessage parseFrom = GalleryBackupProtos.BackupMessage.parseFrom(fileInputStream);
                    fileInputStream.close();
                    DefaultLogger.d(TAG, "parse message cost [%d] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                    cacheCloudProfile(parseFrom);
                } catch (Throwable th) {
                    try {
                        fileInputStream.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                    throw th;
                }
            } catch (IOException e) {
                DefaultLogger.e(TAG, "parse message failed, %s.", e.getMessage());
                DefaultLogger.d(TAG, "parse message cost [%d] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            }
        } catch (Throwable th3) {
            DefaultLogger.d(TAG, "parse message cost [%d] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            throw th3;
        }
    }

    private static void cacheCloudProfile(GalleryBackupProtos.BackupMessage backupMessage) {
        List<GalleryBackupProtos.BackupMessage.CloudProfile> cloudProfilesList = backupMessage.getCloudProfilesList();
        if (!BaseMiscUtil.isValid(cloudProfilesList)) {
            return;
        }
        Map synchronizedMap = Collections.synchronizedMap(new HashMap());
        for (GalleryBackupProtos.BackupMessage.CloudProfile cloudProfile : cloudProfilesList) {
            if (!TextUtils.isEmpty(cloudProfile.getPath())) {
                synchronizedMap.put(cloudProfile.getPath(), cloudProfile);
            }
        }
        ScanCache.getInstance().put("key_mi_mover_cloud_profiles", synchronizedMap);
    }
}
