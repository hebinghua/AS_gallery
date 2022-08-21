package com.miui.gallery.storage.strategies;

import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import java.util.List;

/* loaded from: classes2.dex */
public interface IFilePathResolverStorageStrategyHolder extends IOrderedStorageStrategyHolder {
    List<IStoragePermissionStrategy.PermissionResult> checkPermission(Object obj, int i, IStoragePermissionStrategy.Permission permission);
}
