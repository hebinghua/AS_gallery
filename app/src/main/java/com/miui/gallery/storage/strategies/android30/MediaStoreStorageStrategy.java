package com.miui.gallery.storage.strategies.android30;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.documentfile.provider.MediaStoreDocumentFile;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.GalleryStorageConstants;
import com.miui.gallery.storage.exceptions.StorageException;
import com.miui.gallery.storage.exceptions.StorageUnsupportedOperationException;
import com.miui.gallery.storage.strategies.BaseStorageStrategy;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.android30.utils.MediaStoreIdResolver;
import com.miui.gallery.storage.strategies.base.StrategyType;
import com.miui.gallery.storage.utils.IMediaStoreIdResolver;
import com.miui.gallery.storage.utils.Utils;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;

@StrategyType(type = 1)
/* loaded from: classes2.dex */
public class MediaStoreStorageStrategy extends BaseStorageStrategy {
    public final Context mApplicationContext;
    public final IMediaStoreIdResolver mMediaStoreIdResolver;

    public MediaStoreStorageStrategy(Context context, IMediaStoreIdResolver iMediaStoreIdResolver) {
        this.mApplicationContext = context;
        this.mMediaStoreIdResolver = new MediaStoreIdResolver(context, iMediaStoreIdResolver);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategy
    public DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        if (!checkPermission(str, permission).granted) {
            return null;
        }
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission.ordinal()];
        if (i != 1 && i != 2) {
            if (i == 3 || i == 4) {
                if (!isSystemGallery()) {
                    throw new StorageException("[%s] is not system gallery", "com.miui.gallery");
                }
                Uri mediaStoreUri = getMediaStoreUri(str);
                if (mediaStoreUri == null) {
                    throw new StorageException("media [%s] not exists", str);
                }
                return new MediaStoreDocumentFile(this.mApplicationContext, mediaStoreUri);
            } else if (i == 5) {
                Uri mediaStoreUri2 = getMediaStoreUri(str);
                if (mediaStoreUri2 == null) {
                    throw new StorageException("media [%s] not exists", str);
                }
                return new MediaStoreDocumentFile(this.mApplicationContext, mediaStoreUri2);
            } else {
                throw new StorageUnsupportedOperationException();
            }
        } else if (getMediaStoreUri(str) != null) {
            throw new StorageException("[%s] already exists", str);
        } else {
            if (StorageSolutionProvider.get().getDocumentFile(BaseFileUtils.getParentFolderPath(str), IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, bundle) == null) {
                throw new StorageException("[%s] cannot create parent", str);
            }
            String relativePath = StorageUtils.getRelativePath(this.mApplicationContext, BaseFileUtils.getParentFolderPath(str));
            Uri uri = MediaStoreIdResolver.getUri(this.mApplicationContext, str);
            if (uri == null) {
                throw new StorageException("[%s] not belongs to [images/videos]", str);
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("relative_path", relativePath);
            contentValues.put("_display_name", BaseFileUtils.getFileName(str));
            Uri insert = this.mApplicationContext.getContentResolver().insert(uri, contentValues, bundle);
            if (insert == null) {
                throw new StorageException("insert media [%s] failed", str);
            }
            return new MediaStoreDocumentFile(this.mApplicationContext, insert);
        }
    }

    /* renamed from: com.miui.gallery.storage.strategies.android30.MediaStoreStorageStrategy$1  reason: invalid class name */
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
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE_DIRECTORY.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE_DIRECTORY.ordinal()] = 7;
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

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
        String mimeType = BaseFileMimeUtil.getMimeType(str);
        if ((BaseFileMimeUtil.isImageFromMimeType(mimeType) || BaseFileMimeUtil.isVideoFromMimeType(mimeType)) && !Utils.isUnderOtherAppSpecificDirectory(this.mApplicationContext, str)) {
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission.ordinal()];
            boolean z = true;
            if (i != 1) {
                if (i == 2) {
                    String relativePath = StorageUtils.getRelativePath(this.mApplicationContext, str);
                    if (BaseFileMimeUtil.isImageFromMimeType(mimeType)) {
                        if (!BaseFileUtils.contains(Environment.DIRECTORY_DCIM, relativePath) && !BaseFileUtils.contains(Environment.DIRECTORY_PICTURES, relativePath)) {
                            z = false;
                        }
                        permissionResult.granted = z;
                    } else {
                        if (!BaseFileUtils.contains(Environment.DIRECTORY_DCIM, relativePath) && !BaseFileUtils.contains(Environment.DIRECTORY_PICTURES, relativePath) && !BaseFileUtils.contains(Environment.DIRECTORY_MOVIES, relativePath)) {
                            z = false;
                        }
                        permissionResult.granted = z;
                    }
                } else if (i == 3 || i == 4) {
                    permissionResult.granted = isSystemGallery();
                } else if (i == 5) {
                    permissionResult.granted = true;
                }
            } else if (TextUtils.equals(GalleryStorageConstants.KEY_FOR_EMPTY_RELATIVE_PATH, StorageUtils.getRelativePath(this.mApplicationContext, BaseFileUtils.getParentFolderPath(str)))) {
                return permissionResult;
            } else {
                permissionResult.granted = true;
            }
            return permissionResult;
        }
        return permissionResult;
    }

    public final boolean isSystemGallery() {
        return PermissionUtils.checkWriteImagesOrVideoAppOps(this.mApplicationContext, Process.myUid(), "com.miui.gallery");
    }

    public final Uri getMediaStoreUri(String str) {
        long mediaStoreId = this.mMediaStoreIdResolver.getMediaStoreId(str);
        if (mediaStoreId == -1) {
            return null;
        }
        return ContentUris.withAppendedId(MediaStoreIdResolver.getUri(this.mApplicationContext, str), mediaStoreId);
    }
}
