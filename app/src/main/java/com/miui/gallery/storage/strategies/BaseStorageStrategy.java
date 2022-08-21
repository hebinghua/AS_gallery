package com.miui.gallery.storage.strategies;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseStorageStrategy implements IStorageStrategy {
    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity fragmentActivity, String str, IStoragePermissionStrategy.Permission... permissionArr) {
        requestPermission(fragmentActivity, str, new HashMap(), permissionArr);
    }

    @Override // com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public List<IStoragePermissionStrategy.PermissionResult> checkPermission(List<String> list, IStoragePermissionStrategy.Permission permission) {
        LinkedList linkedList = new LinkedList();
        for (String str : list) {
            linkedList.add(checkPermission(str, permission));
        }
        return linkedList;
    }
}
