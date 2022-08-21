package com.miui.gallery.storage.strategies.base;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.android.internal.storage.StorageInfo;
import com.miui.gallery.storage.strategies.IFilePathResolverStorageStrategyHolder;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.IStorageStrategy;
import com.miui.gallery.util.StorageUtils;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: MultiUserHackStorageStrategyHolderProxy.kt */
/* loaded from: classes2.dex */
public final class MultiUserHackStorageStrategyHolderProxy implements IFilePathResolverStorageStrategyHolder {
    public final Context context;
    public final IFilePathResolverStorageStrategyHolder holder;

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void addAt(IStorageStrategy strategy, int i) {
        Intrinsics.checkNotNullParameter(strategy, "strategy");
        this.holder.addAt(strategy, i);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void append(IStorageStrategy strategy) {
        Intrinsics.checkNotNullParameter(strategy, "strategy");
        this.holder.append(strategy);
    }

    @Override // com.miui.gallery.storage.strategies.IFilePathResolverStorageStrategyHolder
    public List<IStoragePermissionStrategy.PermissionResult> checkPermission(Object obj, int i, IStoragePermissionStrategy.Permission permission) {
        return this.holder.checkPermission(obj, i, permission);
    }

    @Override // com.miui.gallery.storage.strategies.IOrderedStorageStrategyHolder
    public DocumentFile getDocumentFile(String strategyOrder, String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        Intrinsics.checkNotNullParameter(strategyOrder, "strategyOrder");
        return this.holder.getDocumentFile(strategyOrder, str, permission, bundle);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(Fragment fragment, Uri uri) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        this.holder.onHandleRequestPermissionResult(fragment, uri);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity activity, int i, int i2, Intent intent) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        this.holder.onHandleRequestPermissionResult(activity, i, i2, intent);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity host, Uri uri) {
        Intrinsics.checkNotNullParameter(host, "host");
        this.holder.onHandleRequestPermissionResult(host, uri);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void registerPermissionObserver(ContentObserver contentObserver) {
        this.holder.registerPermissionObserver(contentObserver);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity host, String str, Map<String, Object> params, IStoragePermissionStrategy.Permission... permissionArr) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(params, "params");
        this.holder.requestPermission(host, str, params, permissionArr);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity host, String str, IStoragePermissionStrategy.Permission... permissionArr) {
        Intrinsics.checkNotNullParameter(host, "host");
        this.holder.requestPermission(host, str, permissionArr);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void unregisterPermissionObserver(ContentObserver contentObserver) {
        this.holder.unregisterPermissionObserver(contentObserver);
    }

    public MultiUserHackStorageStrategyHolderProxy(Context context, IFilePathResolverStorageStrategyHolder holder) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(holder, "holder");
        this.context = context;
        this.holder = holder;
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission type) {
        Intrinsics.checkNotNullParameter(type, "type");
        IStoragePermissionStrategy.PermissionResult checkPermission = this.holder.checkPermission(str, type);
        Intrinsics.checkNotNullExpressionValue(checkPermission, "holder.checkPermission(path, type)");
        StorageInfo storageInfo = StorageUtils.getStorageInfo(this.context, checkPermission.path);
        if (storageInfo == null || storageInfo.isXspace()) {
            checkPermission.granted = true;
        }
        return checkPermission;
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public List<IStoragePermissionStrategy.PermissionResult> checkPermission(List<String> paths, IStoragePermissionStrategy.Permission type) {
        Intrinsics.checkNotNullParameter(paths, "paths");
        Intrinsics.checkNotNullParameter(type, "type");
        List<IStoragePermissionStrategy.PermissionResult> checkPermission = this.holder.checkPermission(paths, type);
        Intrinsics.checkNotNullExpressionValue(checkPermission, "holder.checkPermission(paths, type)");
        for (IStoragePermissionStrategy.PermissionResult permissionResult : checkPermission) {
            StorageInfo storageInfo = StorageUtils.getStorageInfo(this.context, permissionResult.path);
            if (storageInfo == null || storageInfo.isXspace()) {
                permissionResult.granted = true;
            }
        }
        return checkPermission;
    }
}
