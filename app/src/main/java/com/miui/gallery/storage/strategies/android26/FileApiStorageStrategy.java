package com.miui.gallery.storage.strategies.android26;

import android.content.Context;
import android.os.Bundle;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.documentfile.provider.GalleryRawDocumentFile;
import com.miui.gallery.storage.exceptions.StorageException;
import com.miui.gallery.storage.strategies.BaseStorageStrategy;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StrategyType;
import com.miui.gallery.storage.utils.Utils;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;

@StrategyType(type = 0)
/* loaded from: classes2.dex */
public class FileApiStorageStrategy extends BaseStorageStrategy {
    public final Context mApplicationContext;

    public FileApiStorageStrategy(Context context) {
        this.mApplicationContext = context;
    }

    public DocumentFile createDirectory(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String parentFolderPath = BaseFileUtils.getParentFolderPath(str);
        DocumentFile fromFile = DocumentFile.fromFile(new File(parentFolderPath));
        if (!fromFile.exists()) {
            fromFile = createDirectory(parentFolderPath);
        } else if (!fromFile.isDirectory()) {
            return null;
        }
        if (fromFile == null || !fromFile.exists()) {
            return null;
        }
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        } else if (file.isFile()) {
            if (!file.delete() || !file.mkdir()) {
                return null;
            }
        } else {
            return new GalleryRawDocumentFile(null, file);
        }
        if (!file.exists()) {
            return null;
        }
        try {
            Os.chmod(str, 511);
            Os.chown(str, -1, -1);
        } catch (ErrnoException e) {
            DefaultLogger.d("FileApiStorageStrategy", "error in chmod or chown for [%s], reason [%s]", str, e.getMessage());
        }
        return new GalleryRawDocumentFile(null, file);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategy
    public DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        if (!checkPermission(str, permission).granted) {
            return null;
        }
        File file = new File(str);
        GalleryRawDocumentFile galleryRawDocumentFile = new GalleryRawDocumentFile(null, file);
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission.ordinal()];
        if (i == 1 || i == 2) {
            if (file.exists()) {
                throw new StorageException("[%s] already exists", str);
            }
            try {
                if (createDirectory(file.getParent()) == null || !file.createNewFile()) {
                    return null;
                }
                Utils.triggerMediaScan(this.mApplicationContext, galleryRawDocumentFile);
            } catch (IOException e) {
                throw new StorageException(e.getMessage(), new Object[0]);
            }
        } else if (i == 3) {
            return createDirectory(str);
        }
        return galleryRawDocumentFile;
    }

    /* renamed from: com.miui.gallery.storage.strategies.android26.FileApiStorageStrategy$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission;

        static {
            int[] iArr = new int[IStoragePermissionStrategy.Permission.values().length];
            $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission = iArr;
            try {
                iArr[IStoragePermissionStrategy.Permission.APPEND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT_DIRECTORY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE_DIRECTORY.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE_DIRECTORY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission.ordinal()];
        if (i == 4 || i == 5) {
            permissionResult.granted = Utils.isUnderAppSpecificDirectory(this.mApplicationContext, str);
            return permissionResult;
        }
        permissionResult.granted = str != null;
        return permissionResult;
    }
}
