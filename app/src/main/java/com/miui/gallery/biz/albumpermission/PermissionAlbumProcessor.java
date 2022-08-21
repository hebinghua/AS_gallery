package com.miui.gallery.biz.albumpermission;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.biz.albumpermission.data.PermissionAlbum;
import com.miui.gallery.provider.cache.AlbumCacheItem;
import com.miui.gallery.provider.cache.IMediaProcessor;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.xspace.XSpaceHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;

/* compiled from: PermissionAlbumProcessor.kt */
/* loaded from: classes.dex */
public final class PermissionAlbumProcessor implements IMediaProcessor<AlbumCacheItem, PermissionAlbum> {
    @Override // com.miui.gallery.provider.cache.IMediaProcessor
    public List<PermissionAlbum> processCache(List<? extends AlbumCacheItem> result, Bundle bundle) {
        Intrinsics.checkNotNullParameter(result, "result");
        File xSpacePath = XSpaceHelper.isXSpaceEnable(GalleryApp.sGetAndroidContext()) ? XSpaceHelper.getXSpacePath() : null;
        HashMap hashMap = new HashMap();
        for (final AlbumCacheItem albumCacheItem : result) {
            String directoryPath = albumCacheItem.getDirectoryPath();
            if (directoryPath == null) {
                directoryPath = "";
            }
            String separator = File.separator;
            Intrinsics.checkNotNullExpressionValue(separator, "separator");
            if (!StringsKt__StringsJVMKt.endsWith$default(directoryPath, separator, false, 2, null)) {
                directoryPath = Intrinsics.stringPlus(directoryPath, separator);
            }
            final String relativeRootParentFolderPath = BaseFileUtils.getRelativeRootParentFolderPath(directoryPath);
            if (!TextUtils.isEmpty(relativeRootParentFolderPath)) {
                Object computeIfAbsent = hashMap.computeIfAbsent(relativeRootParentFolderPath, new Function() { // from class: com.miui.gallery.biz.albumpermission.PermissionAlbumProcessor$$ExternalSyntheticLambda0
                    @Override // java.util.function.Function
                    public final Object apply(Object obj) {
                        PermissionAlbum m586processCache$lambda0;
                        m586processCache$lambda0 = PermissionAlbumProcessor.m586processCache$lambda0(AlbumCacheItem.this, relativeRootParentFolderPath, (String) obj);
                        return m586processCache$lambda0;
                    }
                });
                Intrinsics.checkNotNullExpressionValue(computeIfAbsent, "map.computeIfAbsent(pare…          )\n            }");
                PermissionAlbum permissionAlbum = (PermissionAlbum) computeIfAbsent;
                permissionAlbum.getAlbums().add(albumCacheItem);
                StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                String[] absolutePath = StorageUtils.getAbsolutePath(GalleryApp.sGetAndroidContext(), albumCacheItem.getDirectoryPath());
                if (absolutePath == null) {
                    absolutePath = new String[0];
                }
                List<IStoragePermissionStrategy.PermissionResult> checkPermission = storageStrategyManager.checkPermission(CollectionsKt__CollectionsKt.listOf(Arrays.copyOf(absolutePath, absolutePath.length)), IStoragePermissionStrategy.Permission.INSERT_DIRECTORY);
                Intrinsics.checkNotNullExpressionValue(checkPermission, "get().checkPermission(\n …T_DIRECTORY\n            )");
                StorageStrategyManager storageStrategyManager2 = StorageSolutionProvider.get();
                String[] absolutePath2 = StorageUtils.getAbsolutePath(GalleryApp.sGetAndroidContext(), ((Object) albumCacheItem.getDirectoryPath()) + ((Object) separator) + "test.jpg");
                if (absolutePath2 == null) {
                    absolutePath2 = new String[0];
                }
                List<IStoragePermissionStrategy.PermissionResult> checkPermission2 = storageStrategyManager2.checkPermission(CollectionsKt__CollectionsKt.listOf(Arrays.copyOf(absolutePath2, absolutePath2.length)), IStoragePermissionStrategy.Permission.INSERT);
                Intrinsics.checkNotNullExpressionValue(checkPermission2, "get().checkPermission(\n …sion.INSERT\n            )");
                boolean z = true;
                boolean z2 = true;
                for (IStoragePermissionStrategy.PermissionResult permissionResult : checkPermission) {
                    if (!BaseFileUtils.contains(xSpacePath == null ? null : xSpacePath.getAbsolutePath(), permissionResult.path)) {
                        boolean z3 = permissionResult.granted;
                        z &= z3;
                        z2 &= permissionResult.applicable | z3;
                    }
                }
                for (IStoragePermissionStrategy.PermissionResult permissionResult2 : checkPermission2) {
                    if (!BaseFileUtils.contains(xSpacePath == null ? null : xSpacePath.getAbsolutePath(), permissionResult2.path)) {
                        boolean z4 = permissionResult2.granted;
                        z &= z4;
                        z2 &= permissionResult2.applicable | z4;
                    }
                }
                permissionAlbum.setGranted(permissionAlbum.getGranted() & z);
                permissionAlbum.setApplicable(permissionAlbum.getApplicable() & (z | z2));
            }
        }
        return new ArrayList(hashMap.values());
    }

    /* renamed from: processCache$lambda-0  reason: not valid java name */
    public static final PermissionAlbum m586processCache$lambda0(AlbumCacheItem cacheItem, String str, String it) {
        Intrinsics.checkNotNullParameter(cacheItem, "$cacheItem");
        Intrinsics.checkNotNullParameter(it, "it");
        long id = cacheItem.getId();
        String fileName = BaseFileUtils.getFileName(str);
        Intrinsics.checkNotNullExpressionValue(fileName, "getFileName(parent)");
        String coverPath = cacheItem.getCoverPath();
        LinkedList linkedList = new LinkedList();
        String[] absolutePath = StorageUtils.getAbsolutePath(GalleryApp.sGetAndroidContext(), str);
        Intrinsics.checkNotNullExpressionValue(absolutePath, "getAbsolutePath(GalleryA…AndroidContext(), parent)");
        return new PermissionAlbum(id, fileName, str, coverPath, null, true, true, linkedList, absolutePath);
    }

    @Override // com.miui.gallery.provider.cache.IMediaProcessor
    public List<PermissionAlbum> processCursor(Cursor cursor) {
        Intrinsics.checkNotNullParameter(cursor, "cursor");
        return CollectionsKt__CollectionsKt.emptyList();
    }
}
