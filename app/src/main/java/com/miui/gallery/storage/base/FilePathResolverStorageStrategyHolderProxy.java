package com.miui.gallery.storage.base;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.strategies.IFilePathResolverStorageStrategyHolder;
import com.miui.gallery.storage.strategies.IOrderedStorageStrategyHolder;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.IStorageStrategy;
import com.miui.gallery.storage.utils.IFilePathResolver;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class FilePathResolverStorageStrategyHolderProxy implements IFilePathResolverStorageStrategyHolder {
    public final IOrderedStorageStrategyHolder mDelegate;
    public final IFilePathResolver mFilePathResolver;

    public FilePathResolverStorageStrategyHolderProxy(IOrderedStorageStrategyHolder iOrderedStorageStrategyHolder, IFilePathResolver iFilePathResolver) {
        this.mDelegate = iOrderedStorageStrategyHolder;
        this.mFilePathResolver = iFilePathResolver;
    }

    @Override // com.miui.gallery.storage.strategies.IFilePathResolverStorageStrategyHolder
    public List<IStoragePermissionStrategy.PermissionResult> checkPermission(Object obj, int i, IStoragePermissionStrategy.Permission permission) {
        IFilePathResolver iFilePathResolver = this.mFilePathResolver;
        if (iFilePathResolver == null) {
            return Collections.emptyList();
        }
        return this.mDelegate.checkPermission(iFilePathResolver.getPaths(obj, i), permission);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void addAt(IStorageStrategy iStorageStrategy, int i) {
        this.mDelegate.addAt(iStorageStrategy, i);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void append(IStorageStrategy iStorageStrategy) {
        this.mDelegate.append(iStorageStrategy);
    }

    @Override // com.miui.gallery.storage.strategies.IOrderedStorageStrategyHolder, com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, String str2) {
        return this.mDelegate.getDocumentFile(str, permission, str2);
    }

    @Override // com.miui.gallery.storage.strategies.IOrderedStorageStrategyHolder, com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStorageStrategy
    public DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        return this.mDelegate.getDocumentFile(str, permission, bundle);
    }

    @Override // com.miui.gallery.storage.strategies.IOrderedStorageStrategyHolder
    public DocumentFile getDocumentFile(String str, String str2, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        return this.mDelegate.getDocumentFile(str, str2, permission, bundle);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        return this.mDelegate.checkPermission(str, permission);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public List<IStoragePermissionStrategy.PermissionResult> checkPermission(List<String> list, IStoragePermissionStrategy.Permission permission) {
        return this.mDelegate.checkPermission(list, permission);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity fragmentActivity, String str, IStoragePermissionStrategy.Permission... permissionArr) {
        this.mDelegate.requestPermission(fragmentActivity, str, permissionArr);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity fragmentActivity, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission... permissionArr) {
        this.mDelegate.requestPermission(fragmentActivity, str, map, permissionArr);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, int i, int i2, Intent intent) {
        this.mDelegate.onHandleRequestPermissionResult(fragmentActivity, i, i2, intent);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, Uri uri) {
        this.mDelegate.onHandleRequestPermissionResult(fragmentActivity, uri);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(Fragment fragment, Uri uri) {
        this.mDelegate.onHandleRequestPermissionResult(fragment, uri);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void registerPermissionObserver(ContentObserver contentObserver) {
        this.mDelegate.registerPermissionObserver(contentObserver);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void unregisterPermissionObserver(ContentObserver contentObserver) {
        this.mDelegate.unregisterPermissionObserver(contentObserver);
    }
}
