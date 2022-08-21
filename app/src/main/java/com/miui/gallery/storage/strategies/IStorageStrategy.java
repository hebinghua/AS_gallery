package com.miui.gallery.storage.strategies;

import android.os.Bundle;
import android.util.Log;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.storage.exceptions.StorageUnsupportedOperationException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;

/* loaded from: classes2.dex */
public interface IStorageStrategy extends IStoragePermissionStrategy {
    public static final boolean DEBUG = Log.isLoggable("StorageSolutionProvider", 2);

    default DocumentFile getDocumentFile(String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        throw new StorageUnsupportedOperationException();
    }
}
