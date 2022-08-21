package com.miui.gallery.storage.strategies;

import android.os.Bundle;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;

/* loaded from: classes2.dex */
public interface IOrderedStorageStrategyHolder extends IStorageStrategyHolder {
    DocumentFile getDocumentFile(String str, String str2, IStoragePermissionStrategy.Permission permission, Bundle bundle);

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    default DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, String str2) {
        Bundle bundle = new Bundle();
        bundle.putString("invoker", str2);
        return getDocumentFile("default", str, permission, bundle);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStorageStrategy
    default DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        return getDocumentFile("default", str, permission, bundle);
    }

    default DocumentFile getDocumentFile(String str, String str2, IStoragePermissionStrategy.Permission permission, String str3) {
        Bundle bundle = new Bundle();
        bundle.putString("invoker", str3);
        return getDocumentFile(str, str2, permission, bundle);
    }
}
