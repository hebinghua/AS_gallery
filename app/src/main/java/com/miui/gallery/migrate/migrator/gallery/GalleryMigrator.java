package com.miui.gallery.migrate.migrator.gallery;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.migrate.migrator.AbsMigrator;
import com.miui.gallery.migrate.migrator.MigrateUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.trash.TrashBinItem;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class GalleryMigrator extends AbsMigrator {
    public static final String OLD_GALLERY_CACHE;
    public static final String OLD_GALLERY_CLOUD;
    public static final String OLD_GALLERY_MICRO_THUMBNAIL_FILE;
    public static final String OLD_GALLERY_OWNER_ALBUM;
    public static final String OLD_GALLERY_SHARER_ALBUM;
    public static final String OLD_GALLERY_TRASH_BIN;
    public final List<String> mPausedScanPaths;

    public String toString() {
        return "GalleryMigrator";
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("MIUI/Gallery");
        String str = File.separator;
        sb.append(str);
        sb.append("cloud");
        String sb2 = sb.toString();
        OLD_GALLERY_CLOUD = sb2;
        OLD_GALLERY_TRASH_BIN = sb2 + str + ".trashBin";
        OLD_GALLERY_MICRO_THUMBNAIL_FILE = sb2 + str + ".microthumbnailFile";
        OLD_GALLERY_CACHE = sb2 + str + ".cache";
        OLD_GALLERY_OWNER_ALBUM = sb2 + str + "owner";
        OLD_GALLERY_SHARER_ALBUM = sb2 + str + "sharer";
    }

    public GalleryMigrator(Context context, long j) {
        super(context, j);
        this.mPausedScanPaths = new LinkedList();
    }

    @Override // com.miui.gallery.migrate.migrator.AbsMigrator
    public boolean check() {
        return exists(OLD_GALLERY_MICRO_THUMBNAIL_FILE) || exists(OLD_GALLERY_CACHE) || exists(OLD_GALLERY_SHARER_ALBUM) || exists(OLD_GALLERY_OWNER_ALBUM) || exists(OLD_GALLERY_TRASH_BIN);
    }

    @Override // com.miui.gallery.migrate.migrator.AbsMigrator
    public void preMigrate() {
        String primaryStoragePath = StorageUtils.getPrimaryStoragePath();
        List<String> list = this.mPausedScanPaths;
        StringBuilder sb = new StringBuilder();
        sb.append(primaryStoragePath);
        String str = File.separator;
        sb.append(str);
        sb.append("MIUI/Gallery");
        list.add(sb.toString());
        List<String> list2 = this.mPausedScanPaths;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(primaryStoragePath);
        sb2.append(str);
        String str2 = StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM;
        sb2.append(str2);
        list2.add(sb2.toString());
        String secondaryStoragePath = StorageUtils.getSecondaryStoragePath();
        if (!TextUtils.isEmpty(secondaryStoragePath)) {
            List<String> list3 = this.mPausedScanPaths;
            list3.add(secondaryStoragePath + str + "MIUI/Gallery");
            List<String> list4 = this.mPausedScanPaths;
            list4.add(secondaryStoragePath + str + str2);
        }
        MigrateUtils.requestPauseScan(this.mPausedScanPaths);
    }

    @Override // com.miui.gallery.migrate.migrator.IMigrator
    public boolean doMigrate() {
        return deleteMicroThumbnail() & deleteCache() & migrateOwner() & migrateSharer() & migrateTrashBin();
    }

    @Override // com.miui.gallery.migrate.migrator.AbsMigrator
    public void postMigrate(boolean z, Throwable th) {
        MigrateUtils.requestResumeScan(this.mPausedScanPaths);
        if (z) {
            LinkedList<String> linkedList = new LinkedList();
            String str = OLD_GALLERY_MICRO_THUMBNAIL_FILE;
            linkedList.add(StorageUtils.getPathInPrimaryStorage(str));
            String str2 = OLD_GALLERY_CACHE;
            linkedList.add(StorageUtils.getPathInPrimaryStorage(str2));
            String str3 = OLD_GALLERY_SHARER_ALBUM;
            linkedList.add(StorageUtils.getPathInPrimaryStorage(str3));
            String str4 = OLD_GALLERY_OWNER_ALBUM;
            linkedList.add(StorageUtils.getPathInPrimaryStorage(str4));
            String str5 = OLD_GALLERY_TRASH_BIN;
            linkedList.add(StorageUtils.getPathInPrimaryStorage(str5));
            if (!TextUtils.isEmpty(StorageUtils.getSecondaryStoragePath())) {
                linkedList.add(StorageUtils.getPathInSecondaryStorage(str));
                linkedList.add(StorageUtils.getPathInSecondaryStorage(str2));
                linkedList.add(StorageUtils.getPathInSecondaryStorage(str3));
                linkedList.add(StorageUtils.getPathInSecondaryStorage(str4));
                linkedList.add(StorageUtils.getPathInSecondaryStorage(str5));
            }
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("GalleryMigrator", "postMigrate");
            for (String str6 : linkedList) {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str6, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, appendInvokerTag);
                if (documentFile != null) {
                    documentFile.delete();
                }
            }
        }
    }

    public final boolean deleteMicroThumbnail() {
        return delete(OLD_GALLERY_MICRO_THUMBNAIL_FILE);
    }

    public final boolean deleteCache() {
        delete(OLD_GALLERY_CACHE);
        return true;
    }

    public final boolean migrateOwner() {
        List<OwnerAlbumItem> list = (List) SafeDBUtil.safeQuery(this.mContext, GalleryContract.Album.URI, OwnerAlbumItem.SELECTION, "localPath like '" + StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM + "/%'", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<OwnerAlbumItem>>() { // from class: com.miui.gallery.migrate.migrator.gallery.GalleryMigrator.1
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
        boolean z = true;
        if (!BaseMiscUtil.isValid(list)) {
            return true;
        }
        for (OwnerAlbumItem ownerAlbumItem : list) {
            ArrayList<Pair> arrayList = new ArrayList(2);
            arrayList.add(new Pair(Paths.get(StorageUtils.getPathInPrimaryStorage(ownerAlbumItem.oldLocalFile), new String[0]), Paths.get(StorageUtils.getPathInPrimaryStorage(ownerAlbumItem.localFile), new String[0])));
            if (!TextUtils.isEmpty(StorageUtils.getSecondaryStoragePath())) {
                arrayList.add(new Pair(Paths.get(StorageUtils.getPathInSecondaryStorage(ownerAlbumItem.oldLocalFile), new String[0]), Paths.get(StorageUtils.getPathInSecondaryStorage(ownerAlbumItem.localFile), new String[0])));
            }
            for (Pair pair : arrayList) {
                try {
                    MigrateUtils.moveDirectory((Path) pair.first, (Path) pair.second, GalleryMigrator$$ExternalSyntheticLambda2.INSTANCE, new CopyOption[0]);
                } catch (NoSuchFileException unused) {
                } catch (Exception e) {
                    DefaultLogger.e("GalleryMigrator", "migrateCache from [%s] to [%s] failed. %s", pair.first, pair.second, e);
                    z = false;
                }
            }
        }
        return z;
    }

    public static /* synthetic */ void lambda$migrateOwner$0(Path path, Path path2) {
        if (path2 == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("thumbnailFile", path2.toString());
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Uri uri = GalleryContract.Cloud.CLOUD_URI;
        int safeUpdate = SafeDBUtil.safeUpdate(sGetAndroidContext, uri, contentValues, "thumbnailFile='" + path.toString() + "' COLLATE NOCASE", (String[]) null);
        boolean z = true;
        if (1 != safeUpdate) {
            z = false;
        }
        if (z) {
            return;
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("localFile", path2.toString());
        Context sGetAndroidContext2 = GalleryApp.sGetAndroidContext();
        SafeDBUtil.safeUpdate(sGetAndroidContext2, uri, contentValues2, "localFile='" + path.toString() + "' COLLATE NOCASE", (String[]) null);
    }

    public final boolean migrateSharer() {
        ArrayList<Pair> arrayList = new ArrayList(2);
        String str = OLD_GALLERY_SHARER_ALBUM;
        Path path = Paths.get(StorageUtils.getPathInPrimaryStorage(str), new String[0]);
        String str2 = StorageConstants.RELATIVE_DIRECTORY_SHARER_ALBUM;
        arrayList.add(new Pair(path, Paths.get(StorageUtils.getPathInPrimaryStorage(str2), new String[0])));
        if (!TextUtils.isEmpty(StorageUtils.getSecondaryStoragePath())) {
            arrayList.add(new Pair(Paths.get(StorageUtils.getPathInSecondaryStorage(str), new String[0]), Paths.get(StorageUtils.getPathInSecondaryStorage(str2), new String[0])));
        }
        boolean z = true;
        for (Pair pair : arrayList) {
            try {
                MigrateUtils.moveDirectory((Path) pair.first, (Path) pair.second, GalleryMigrator$$ExternalSyntheticLambda1.INSTANCE, new CopyOption[0]);
            } catch (NoSuchFileException unused) {
            } catch (Exception e) {
                DefaultLogger.e("GalleryMigrator", "migrateCache from [%s] to [%s] failed. %s", pair.first, pair.second, e);
                z = false;
            }
        }
        return z;
    }

    public static /* synthetic */ void lambda$migrateSharer$1(Path path, Path path2) {
        if (path2 == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("thumbnailFile", path2.toString());
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Uri uri = GalleryContract.ShareImage.SHARE_URI;
        int safeUpdate = SafeDBUtil.safeUpdate(sGetAndroidContext, uri, contentValues, "thumbnailFile='" + path.toString() + "' COLLATE NOCASE", (String[]) null);
        boolean z = true;
        if (1 != safeUpdate) {
            z = false;
        }
        if (z) {
            return;
        }
        ContentValues contentValues2 = new ContentValues();
        contentValues2.put("localFile", path2.toString());
        Context sGetAndroidContext2 = GalleryApp.sGetAndroidContext();
        SafeDBUtil.safeUpdate(sGetAndroidContext2, uri, contentValues2, "localFile='" + path.toString() + "' COLLATE NOCASE", (String[]) null);
    }

    public final boolean migrateTrashBin() {
        ArrayList<Pair> arrayList = new ArrayList(2);
        String str = OLD_GALLERY_TRASH_BIN;
        arrayList.add(new Pair(Paths.get(StorageUtils.getPathInPrimaryStorage(str), new String[0]), Paths.get(StorageUtils.getPathInPrimaryStorage("/Android/data/com.miui.gallery/files/trashBin"), new String[0])));
        if (!TextUtils.isEmpty(StorageUtils.getSecondaryStoragePath())) {
            arrayList.add(new Pair(Paths.get(StorageUtils.getPathInSecondaryStorage(str), new String[0]), Paths.get(StorageUtils.getPathInSecondaryStorage("/Android/data/com.miui.gallery/files/trashBin"), new String[0])));
        }
        boolean z = true;
        for (Pair pair : arrayList) {
            try {
                MigrateUtils.moveDirectory((Path) pair.first, (Path) pair.second, GalleryMigrator$$ExternalSyntheticLambda0.INSTANCE, new CopyOption[0]);
            } catch (NoSuchFileException unused) {
            } catch (Exception e) {
                DefaultLogger.e("GalleryMigrator", "migrateCache from [%s] to [%s] failed. %s", pair.first, pair.second, e);
                z = false;
            }
        }
        return z;
    }

    public static /* synthetic */ void lambda$migrateTrashBin$2(Path path, Path path2) {
        if (path2 == null) {
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("trashFilePath", path2.toString());
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        galleryEntityManager.update(TrashBinItem.class, contentValues, "trashFilePath='" + path.toString() + "' COLLATE NOCASE", null);
    }

    public static boolean delete(String str) {
        String pathInPrimaryStorage = StorageUtils.getPathInPrimaryStorage(str);
        if (TextUtils.isEmpty(pathInPrimaryStorage)) {
            DefaultLogger.d("GalleryMigrator", "invalid path [%s].", pathInPrimaryStorage);
            return false;
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(pathInPrimaryStorage, IStoragePermissionStrategy.Permission.DELETE, FileHandleRecordHelper.appendInvokerTag("GalleryMigrator", "delete"));
        if (documentFile == null || !documentFile.exists()) {
            DefaultLogger.d("GalleryMigrator", "file [%s] don't exists.", documentFile);
            return true;
        }
        return documentFile.delete();
    }

    public static boolean exists(String str) {
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("GalleryMigrator", "exists");
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        StringBuilder sb = new StringBuilder();
        sb.append(StorageUtils.getPrimaryStoragePath());
        String str2 = File.separator;
        sb.append(str2);
        sb.append(str);
        String sb2 = sb.toString();
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.QUERY_DIRECTORY;
        DocumentFile documentFile = storageStrategyManager.getDocumentFile(sb2, permission, appendInvokerTag);
        if (TextUtils.isEmpty(StorageUtils.getSecondaryStoragePath())) {
            return documentFile != null && documentFile.exists();
        }
        StorageStrategyManager storageStrategyManager2 = StorageSolutionProvider.get();
        DocumentFile documentFile2 = storageStrategyManager2.getDocumentFile(StorageUtils.getSecondaryStoragePath() + str2 + str, permission, appendInvokerTag);
        if (documentFile != null && documentFile.exists()) {
            return true;
        }
        return documentFile2 != null && documentFile2.exists();
    }

    /* loaded from: classes2.dex */
    public static class OwnerAlbumItem {
        public static final String[] SELECTION = {j.c, "editedColumns", "localPath"};
        public String editedColumns;
        public long id;
        public String localFile;
        public String oldLocalFile;

        public static OwnerAlbumItem fromCursor(Cursor cursor) {
            OwnerAlbumItem ownerAlbumItem = new OwnerAlbumItem();
            ownerAlbumItem.id = cursor.getLong(0);
            ownerAlbumItem.editedColumns = cursor.getString(1);
            ownerAlbumItem.localFile = cursor.getString(2);
            StringBuilder sb = new StringBuilder();
            sb.append(GalleryMigrator.OLD_GALLERY_OWNER_ALBUM);
            sb.append(File.separator);
            String str = ownerAlbumItem.localFile;
            sb.append(StringUtils.replaceIgnoreCase(str, StorageConstants.RELATIVE_DIRECTORY_OWNER_ALBUM + h.g, ""));
            ownerAlbumItem.oldLocalFile = sb.toString();
            return ownerAlbumItem;
        }
    }
}
