package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Pair;
import androidx.documentfile.provider.DocumentFile;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.migrate.migrator.MigrateUtils;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.trash.TrashBinItem;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class GalleryDBUpdater105 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        clearMicroThumbnailData(supportSQLiteDatabase);
        updateOwnerAlbumPath(supportSQLiteDatabase);
        updateTranBinRecoveryPath();
        migrateCache();
        return UpdateResult.defaultResult();
    }

    public final void clearMicroThumbnailData(SupportSQLiteDatabase supportSQLiteDatabase) {
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format("UPDATE %s SET %s = NULL", "cloud", "microthumbfile"));
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format("UPDATE %s SET %s = NULL", "shareImage", "microthumbfile"));
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format("UPDATE %s SET %s = NULL", "ownerSubUbifocus", "microthumbfile"));
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format("UPDATE %s SET %s = NULL", "shareSubUbifocus", "microthumbfile"));
        ContentValues contentValues = new ContentValues();
        contentValues.putNull("microFilePath");
        GalleryEntityManager.getInstance().update(TrashBinItem.class, contentValues, null, null);
    }

    public final void updateOwnerAlbumPath(SupportSQLiteDatabase supportSQLiteDatabase) {
        List<OwnerAlbumItem> list = (List) SafeDBUtil.safeQuery(supportSQLiteDatabase, "album", OwnerAlbumItem.SELECTION, "localPath like 'MIUI/Gallery/cloud/owner/%'", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<OwnerAlbumItem>>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater105.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<OwnerAlbumItem> mo1808handle(Cursor cursor) {
                if (cursor == null || cursor.getCount() <= 0) {
                    return Collections.emptyList();
                }
                ArrayList arrayList = new ArrayList(cursor.getCount());
                while (cursor.moveToNext()) {
                    arrayList.add(OwnerAlbumItem.fromCursor(cursor));
                }
                return arrayList;
            }
        });
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        for (OwnerAlbumItem ownerAlbumItem : list) {
            String replaceIgnoreCase = StringUtils.replaceIgnoreCase(ownerAlbumItem.localFile, "MIUI/Gallery/cloud/owner", StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM);
            String mergeEditedColumns = GalleryCloudUtils.mergeEditedColumns(ownerAlbumItem.editedColumns, GalleryCloudUtils.transformToEditedColumnsElement(13));
            ContentValues contentValues = new ContentValues();
            contentValues.put("localPath", replaceIgnoreCase);
            contentValues.put("editedColumns", mergeEditedColumns);
            SafeDBUtil.safeUpdate(supportSQLiteDatabase, "album", contentValues, "_id=" + ownerAlbumItem.id, (String[]) null);
        }
    }

    public final void updateTranBinRecoveryPath() {
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        galleryEntityManager.execSQL("update " + TrashBinItem.class.getSimpleName() + " set albumPath=replace(albumPath, 'MIUI/Gallery/cloud/owner', '" + StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM + "')");
        GalleryEntityManager galleryEntityManager2 = GalleryEntityManager.getInstance();
        galleryEntityManager2.execSQL("update " + TrashBinItem.class.getSimpleName() + " set albumPath=replace(albumPath, 'MIUI/Gallery/cloud/sharer', '" + StorageConstants.RELATIVE_DIRECTORY_SHARER_ALBUM + "')");
    }

    public static void migrateCache() {
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage("MIUI/Gallery/cloud/.cache");
        if (TextUtils.isEmpty(pathInPrimaryStorage)) {
            return;
        }
        ArrayList<Pair> arrayList = new ArrayList(1);
        Path path = Paths.get(pathInPrimaryStorage + "/.noMediaRecord", new String[0]);
        arrayList.add(new Pair(path, Paths.get(StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/files/noMediaRecord") + "/.noMediaRecord", new String[0])));
        for (Pair pair : arrayList) {
            try {
                MigrateUtils.moveDirectory((Path) pair.first, (Path) pair.second, new CopyOption[0]);
            } catch (NoSuchFileException unused) {
            } catch (Exception e) {
                DefaultLogger.e("GalleryDBUpdater105", "migrateCache from [%s] to [%s] failed. %s", pair.first, pair.second, e);
            }
        }
        ArrayList<Pair> arrayList2 = new ArrayList(5);
        Path path2 = Paths.get(pathInPrimaryStorage + "/search_histories.json", new String[0]);
        arrayList2.add(new Pair(path2, Paths.get(StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/searchCache") + "/search_histories.json", new String[0])));
        Path path3 = Paths.get(pathInPrimaryStorage + "/search_log.json", new String[0]);
        arrayList2.add(new Pair(path3, Paths.get(StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/cache/searchCache") + "/search_log.json", new String[0])));
        for (Pair pair2 : arrayList2) {
            try {
                Files.move((Path) pair2.first, (Path) pair2.second, new CopyOption[0]);
            } catch (NoSuchFileException unused2) {
            } catch (Exception e2) {
                DefaultLogger.e("GalleryDBUpdater105", "migrateCache from [%s] to [%s] failed. %s", pair2.first, pair2.second, e2);
            }
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("GalleryDBUpdater105", "migrateCache"));
        if (documentFile == null) {
            return;
        }
        documentFile.delete();
    }

    /* loaded from: classes2.dex */
    public static class OwnerAlbumItem {
        public static final String[] SELECTION = {j.c, "editedColumns", "localPath"};
        public String editedColumns;
        public long id;
        public String localFile;

        public static OwnerAlbumItem fromCursor(Cursor cursor) {
            OwnerAlbumItem ownerAlbumItem = new OwnerAlbumItem();
            ownerAlbumItem.id = cursor.getLong(0);
            ownerAlbumItem.editedColumns = cursor.getString(1);
            ownerAlbumItem.localFile = cursor.getString(2);
            return ownerAlbumItem;
        }
    }
}
