package com.miui.gallery.storage.strategies.android30;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.documentfile.provider.CameraDocumentFile;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.photosapi.PhotosOemApi;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.storage.strategies.BaseStorageStrategy;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.android30.utils.MediaStoreIdResolver;
import com.miui.gallery.storage.strategies.base.StrategyType;
import com.miui.gallery.storage.utils.IMediaStoreIdResolver;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;

@StrategyType(type = 3)
/* loaded from: classes2.dex */
public class CameraParallelProcessProviderStorageStrategy extends BaseStorageStrategy {
    public static final LazyValue<Context, Boolean> HAS_CAMERA_SUPPORTED = new LazyValue<Context, Boolean>() { // from class: com.miui.gallery.storage.strategies.android30.CameraParallelProcessProviderStorageStrategy.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            return Boolean.valueOf(PhotosOemApi.getVersion(context) >= 3);
        }
    };
    public final Context mApplicationContext;
    public final IMediaStoreIdResolver mMediaStoreIdResolver;

    public CameraParallelProcessProviderStorageStrategy(Context context, IMediaStoreIdResolver iMediaStoreIdResolver) {
        this.mApplicationContext = context;
        this.mMediaStoreIdResolver = new MediaStoreIdResolver(context, iMediaStoreIdResolver);
    }

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
        if (HAS_CAMERA_SUPPORTED.get(this.mApplicationContext).booleanValue() && AnonymousClass2.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission.ordinal()] == 1 && BaseFileUtils.contains(MIUIStorageConstants.DIRECTORY_CAMERA_PATH, StorageUtils.getRelativePath(this.mApplicationContext, str))) {
            if (!isOwner(this.mApplicationContext, getMediaStoreUri(str), "com.android.camera")) {
                return permissionResult;
            }
            permissionResult.granted = true;
            return permissionResult;
        }
        return permissionResult;
    }

    /* renamed from: com.miui.gallery.storage.strategies.android30.CameraParallelProcessProviderStorageStrategy$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission;

        static {
            int[] iArr = new int[IStoragePermissionStrategy.Permission.values().length];
            $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission = iArr;
            try {
                iArr[IStoragePermissionStrategy.Permission.QUERY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategy
    public DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        if (!checkPermission(str, permission).granted) {
            return null;
        }
        return new CameraDocumentFile(this.mApplicationContext, getMediaStoreUri(str), PhotosOemApi.getQueryProcessingUri(this.mApplicationContext, this.mMediaStoreIdResolver.getMediaStoreId(str)));
    }

    public static boolean isOwner(Context context, Uri uri, String str) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor query = contentResolver.query(uri, null, "owner_package_name='" + str + "'", null, null);
            boolean z = query != null && query.moveToFirst();
            if (query != null) {
                query.close();
            }
            return z;
        } catch (Exception e) {
            DefaultLogger.e("CameraParallelProcessProviderStorageStrategy", e);
            return false;
        }
    }

    public final Uri getMediaStoreUri(String str) {
        long mediaStoreId = this.mMediaStoreIdResolver.getMediaStoreId(str);
        if (mediaStoreId == -1) {
            return null;
        }
        return ContentUris.withAppendedId(MediaStoreIdResolver.getUri(this.mApplicationContext, str), mediaStoreId);
    }
}
