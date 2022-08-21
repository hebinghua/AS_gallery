package com.miui.gallery.storage.strategies.android30;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import androidx.documentfile.provider.DocumentFile;
import androidx.documentfile.provider.GalleryRawDocumentFile;
import com.miui.gallery.storage.exceptions.StorageException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.android30.utils.MediaStoreIdResolver;
import com.miui.gallery.storage.strategies.base.StrategyType;
import com.miui.gallery.storage.utils.Utils;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;
import java.io.File;
import java.io.IOException;

@StrategyType(type = 0)
/* loaded from: classes2.dex */
public class FileApiStorageStrategy extends com.miui.gallery.storage.strategies.android28.FileApiStorageStrategy {
    public FileApiStorageStrategy(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.storage.strategies.android28.FileApiStorageStrategy, com.miui.gallery.storage.strategies.android26.FileApiStorageStrategy, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
        if (Utils.isUnderAppSpecificDirectory(this.mApplicationContext, str)) {
            permissionResult.granted = true;
            return permissionResult;
        }
        int[] iArr = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission;
        int i = iArr[permission.ordinal()];
        if (i == 1 || i == 2) {
            permissionResult.granted = Utils.isUnderAppSpecificDirectory(this.mApplicationContext, str);
            return permissionResult;
        } else if (BaseFileUtils.contains(Environment.DIRECTORY_DOWNLOADS, StorageUtils.getRelativePath(this.mApplicationContext, str))) {
            int i2 = iArr[permission.ordinal()];
            if (i2 == 3 || i2 == 4) {
                permissionResult.granted = new File(str).canWrite();
                return permissionResult;
            } else if (i2 == 5) {
                permissionResult.granted = new File(str).canRead();
                return permissionResult;
            } else {
                permissionResult.granted = true;
                return permissionResult;
            }
        } else {
            String mimeType = BaseFileMimeUtil.getMimeType(str);
            if (BaseFileMimeUtil.isImageFromMimeType(mimeType) || BaseFileMimeUtil.isVideoFromMimeType(mimeType)) {
                if (iArr[permission.ordinal()] == 5) {
                    permissionResult.granted = true;
                    return permissionResult;
                } else if (PermissionUtils.checkWriteImagesOrVideoAppOps(this.mApplicationContext, Process.myUid(), "com.miui.gallery")) {
                    return permissionResult;
                }
            }
            switch (iArr[permission.ordinal()]) {
                case 6:
                case 7:
                    String relativePath = StorageUtils.getRelativePath(this.mApplicationContext, str);
                    if ((BaseFileMimeUtil.isImageFromMimeType(mimeType) || BaseFileMimeUtil.isVideoFromMimeType(mimeType)) && Utils.isUnderMediaCollection(relativePath)) {
                        permissionResult.granted = true;
                    }
                    return permissionResult;
                case 8:
                    String str2 = File.separator;
                    if (!str.endsWith(str2)) {
                        str = str + str2;
                    }
                    if (new File(str).exists()) {
                        permissionResult.granted = true;
                        return permissionResult;
                    } else if (new File(BaseFileUtils.getAbsoluteRootParentFolderPath(this.mApplicationContext, str)).exists()) {
                        permissionResult.granted = true;
                        return permissionResult;
                    } else if (!Utils.isUnderStandardCollection(StorageUtils.getRelativePath(this.mApplicationContext, str))) {
                        permissionResult.applicable = false;
                        return permissionResult;
                    } else {
                        permissionResult.granted = true;
                        return permissionResult;
                    }
                case 9:
                    permissionResult.granted = true;
                    return permissionResult;
                default:
                    return permissionResult;
            }
        }
    }

    /* renamed from: com.miui.gallery.storage.strategies.android30.FileApiStorageStrategy$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission;

        static {
            int[] iArr = new int[IStoragePermissionStrategy.Permission.values().length];
            $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission = iArr;
            try {
                iArr[IStoragePermissionStrategy.Permission.DELETE_DIRECTORY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE_DIRECTORY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.QUERY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.APPEND.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT_DIRECTORY.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.QUERY_DIRECTORY.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    @Override // com.miui.gallery.storage.strategies.android26.FileApiStorageStrategy, com.miui.gallery.storage.strategies.IStorageStrategy
    public DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        if (!checkPermission(str, permission).granted) {
            return null;
        }
        File file = new File(str);
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission.ordinal()];
        if (i == 6 || i == 7) {
            if (file.exists()) {
                throw new StorageException("[%s] already exists", str);
            }
            if (createDirectory(file.getParent()) == null) {
                return null;
            }
            if (!Utils.isUnderAppSpecificDirectory(this.mApplicationContext, str)) {
                Uri uri = MediaStoreIdResolver.getUri(this.mApplicationContext, str);
                if (uri == null) {
                    throw new StorageException("[%s] not belongs to [images/videos]", str);
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("relative_path", StorageUtils.getRelativePath(this.mApplicationContext, BaseFileUtils.getParentFolderPath(str)));
                contentValues.put("_display_name", BaseFileUtils.getFileName(str));
                if (this.mApplicationContext.getContentResolver().insert(uri, contentValues, bundle) == null) {
                    throw new StorageException(String.format("failed to insert [%s] to MediaStore.", str), new Object[0]);
                }
            } else {
                try {
                    if (!file.createNewFile()) {
                        return null;
                    }
                } catch (IOException e) {
                    throw new StorageException(e.getMessage(), new Object[0]);
                }
            }
        } else if (i == 8) {
            return createDirectory(str);
        }
        return new GalleryRawDocumentFile(null, file);
    }
}
