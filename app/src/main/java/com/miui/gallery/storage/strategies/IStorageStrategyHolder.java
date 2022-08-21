package com.miui.gallery.storage.strategies;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public interface IStorageStrategyHolder extends IStorageStrategy {
    void addAt(IStorageStrategy iStorageStrategy, int i);

    void append(IStorageStrategy iStorageStrategy);

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission);

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    List<IStoragePermissionStrategy.PermissionResult> checkPermission(List<String> list, IStoragePermissionStrategy.Permission permission);

    @Override // com.miui.gallery.storage.strategies.IStorageStrategy
    DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, Bundle bundle);

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    void onHandleRequestPermissionResult(Fragment fragment, Uri uri);

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, int i, int i2, Intent intent);

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    void onHandleRequestPermissionResult(FragmentActivity fragmentActivity, Uri uri);

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    void registerPermissionObserver(ContentObserver contentObserver);

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    void requestPermission(FragmentActivity fragmentActivity, String str, Map<String, Object> map, IStoragePermissionStrategy.Permission... permissionArr);

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    void requestPermission(FragmentActivity fragmentActivity, String str, IStoragePermissionStrategy.Permission... permissionArr);

    void unregisterPermissionObserver(ContentObserver contentObserver);

    default DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("invoker", str2);
        return getDocumentFile(str, permission, bundle);
    }
}
