package com.miui.gallery.permission.core;

/* loaded from: classes2.dex */
public interface PermissionCheckCallback {
    Permission[] getRuntimePermissions();

    void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr);
}
