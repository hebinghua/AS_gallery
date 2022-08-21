package com.miui.gallery.migrate.migrator.gallery;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Pair;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.migrate.migrator.AbsMigrator;
import com.miui.gallery.migrate.migrator.MigrateUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.nio.file.CopyOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class GallerySecretMigrator extends AbsMigrator {
    public static final String OLD_GALLERY_CLOUD;
    public static final String OLD_GALLERY_SECRET_ALBUM;

    static {
        String str = "MIUI/Gallery" + File.separator + "cloud";
        OLD_GALLERY_CLOUD = str;
        OLD_GALLERY_SECRET_ALBUM = str + "/.secretAlbum";
    }

    public GallerySecretMigrator(Context context, long j) {
        super(context, j);
    }

    @Override // com.miui.gallery.migrate.migrator.AbsMigrator
    public boolean check() {
        return exists(OLD_GALLERY_SECRET_ALBUM);
    }

    @Override // com.miui.gallery.migrate.migrator.IMigrator
    public boolean doMigrate() {
        ArrayList<Pair> arrayList = new ArrayList(2);
        String str = OLD_GALLERY_SECRET_ALBUM;
        arrayList.add(new Pair(Paths.get(StorageUtils.getPathInPrimaryStorage(str), new String[0]), Paths.get(StorageUtils.getPathInPrimaryStorage("MIUI/Gallery/cloud/secretAlbum"), new String[0])));
        if (!TextUtils.isEmpty(StorageUtils.getSecondaryStoragePath())) {
            arrayList.add(new Pair(Paths.get(StorageUtils.getPathInSecondaryStorage(str), new String[0]), Paths.get(StorageUtils.getPathInSecondaryStorage("MIUI/Gallery/cloud/secretAlbum"), new String[0])));
        }
        boolean z = true;
        for (Pair pair : arrayList) {
            try {
                MigrateUtils.moveDirectory((Path) pair.first, (Path) pair.second, GallerySecretMigrator$$ExternalSyntheticLambda0.INSTANCE, new CopyOption[0]);
            } catch (NoSuchFileException unused) {
            } catch (Exception e) {
                DefaultLogger.e("GallerySecretMigrator", "migrateSecret from [%s] to [%s] failed. %s", pair.first, pair.second, e);
                z = false;
            }
        }
        return z;
    }

    public static /* synthetic */ void lambda$doMigrate$0(Path path, Path path2) {
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

    @Override // com.miui.gallery.migrate.migrator.AbsMigrator
    public void postMigrate(boolean z, Throwable th) {
        if (z) {
            LinkedList<String> linkedList = new LinkedList();
            String str = OLD_GALLERY_SECRET_ALBUM;
            linkedList.add(StorageUtils.getPathInPrimaryStorage(str));
            if (!TextUtils.isEmpty(StorageUtils.getSecondaryStoragePath())) {
                linkedList.add(StorageUtils.getPathInSecondaryStorage(str));
            }
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("GallerySecretMigrator", "postMigrate");
            for (String str2 : linkedList) {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str2, IStoragePermissionStrategy.Permission.QUERY_DIRECTORY, appendInvokerTag);
                if (documentFile != null) {
                    documentFile.delete();
                }
            }
        }
    }

    public static boolean exists(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(StorageUtils.getPrimaryStoragePath());
        String str2 = File.separator;
        sb.append(str2);
        sb.append(str);
        File file = new File(sb.toString());
        if (TextUtils.isEmpty(StorageUtils.getSecondaryStoragePath())) {
            return file.exists();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(StorageUtils.getSecondaryStoragePath());
        sb2.append(str2);
        sb2.append(str);
        return file.exists() || new File(sb2.toString()).exists();
    }
}
