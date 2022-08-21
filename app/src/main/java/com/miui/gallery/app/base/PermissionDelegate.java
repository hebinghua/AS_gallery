package com.miui.gallery.app.base;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.permission.core.PermissionInjector;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class PermissionDelegate {
    public WeakReference<FragmentActivity> mActivityWeakReference;
    public Permission[] mUserRequestPermission;

    public PermissionDelegate(FragmentActivity fragmentActivity) {
        this.mActivityWeakReference = new WeakReference<>(fragmentActivity);
    }

    public void onCreate(Permission[] permissionArr, boolean z) {
        if (!(getActivity() instanceof PermissionCheckCallback)) {
            finish();
            return;
        }
        this.mUserRequestPermission = permissionArr;
        Bundle bundle = new Bundle();
        bundle.putBoolean("SHOW_WHEN_LOCKED", z);
        PermissionInjector.injectIfNeededIn(getActivity(), (PermissionCheckCallback) getActivity(), bundle);
    }

    public void onDestroy() {
        WeakReference<FragmentActivity> weakReference = this.mActivityWeakReference;
        if (weakReference != null) {
            weakReference.clear();
        }
        this.mActivityWeakReference = null;
    }

    public final FragmentActivity getActivity() {
        WeakReference<FragmentActivity> weakReference = this.mActivityWeakReference;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    public final void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }
}
