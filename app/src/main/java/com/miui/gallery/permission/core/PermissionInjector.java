package com.miui.gallery.permission.core;

import android.os.Bundle;
import android.os.Trace;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class PermissionInjector extends Fragment implements PermissionCheckCallback {
    public PermissionCheckCallback mCallback;
    public PermissionCheckHelper mHelper;
    public long mRequestTime;

    public static void injectIfNeededIn(FragmentActivity fragmentActivity, PermissionCheckCallback permissionCheckCallback, Bundle bundle) {
        if (permissionCheckCallback == null) {
            DefaultLogger.d("PermissionInjector", "The callback is null");
            return;
        }
        Permission[] runtimePermissions = permissionCheckCallback.getRuntimePermissions();
        if (runtimePermissions == null || runtimePermissions.length == 0) {
            DefaultLogger.d("PermissionInjector", "Has no permissions to grant");
            return;
        }
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag("com.miui.gallery.permission.injector") != null) {
            return;
        }
        PermissionInjector permissionInjector = new PermissionInjector();
        permissionInjector.setCallback(permissionCheckCallback);
        permissionInjector.setArguments(bundle);
        supportFragmentManager.beginTransaction().add(permissionInjector, "com.miui.gallery.permission.injector").commitAllowingStateLoss();
        supportFragmentManager.executePendingTransactions();
    }

    public void setCallback(PermissionCheckCallback permissionCheckCallback) {
        this.mCallback = permissionCheckCallback;
    }

    public final boolean isShowWhenLocked() {
        Bundle arguments = getArguments();
        return arguments != null && arguments.getBoolean("SHOW_WHEN_LOCKED", false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        Trace.beginSection("permissionInjectorCreate");
        super.onCreate(bundle);
        PermissionCheckHelper permissionCheckHelper = new PermissionCheckHelper(this, isShowWhenLocked(), this);
        this.mHelper = permissionCheckHelper;
        permissionCheckHelper.checkPermission();
        this.mRequestTime = System.currentTimeMillis();
        Trace.endSection();
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        this.mHelper.onRequestPermissionsResult(i, strArr, iArr);
        DefaultLogger.d("PermissionInjector", "[Permission] onCreate -> onRequestPermissionsResult consume %d", Long.valueOf(System.currentTimeMillis() - this.mRequestTime));
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        PermissionCheckCallback permissionCheckCallback = this.mCallback;
        return permissionCheckCallback != null ? permissionCheckCallback.getRuntimePermissions() : new Permission[0];
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        PermissionCheckCallback permissionCheckCallback = this.mCallback;
        if (permissionCheckCallback != null) {
            permissionCheckCallback.onPermissionsChecked(permissionArr, iArr, zArr);
        }
        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }
}
