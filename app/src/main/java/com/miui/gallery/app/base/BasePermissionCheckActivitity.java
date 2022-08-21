package com.miui.gallery.app.base;

import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.base_optimization.mvp.view.Activity;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.preference.BaseGalleryPreferences;

/* loaded from: classes.dex */
public abstract class BasePermissionCheckActivitity<P extends IPresenter> extends Activity<P> implements PermissionCheckCallback {
    public PermissionDelegate mDelegate;

    public boolean allowUseOnOffline() {
        return true;
    }

    public boolean isShowWhenLocked() {
        return false;
    }

    public void onCtaChecked(boolean z) {
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Activity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PermissionDelegate permissionDelegate = new PermissionDelegate(this);
        this.mDelegate = permissionDelegate;
        permissionDelegate.onCreate(getRuntimePermissions(), isShowWhenLocked());
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Activity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.mDelegate.onDestroy();
        this.mDelegate = null;
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        return new Permission[]{new Permission("android.permission.WRITE_EXTERNAL_STORAGE", getString(R.string.permission_storage_desc), true)};
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        if ((BaseGalleryPreferences.CTA.allowUseOnOfflineGlobal() && allowUseOnOffline()) || BaseGalleryPreferences.CTA.canConnectNetwork()) {
            onCtaChecked(true);
        } else {
            AgreementsUtils.showUserAgreements(this, new OnAgreementInvokedListener() { // from class: com.miui.gallery.app.base.BasePermissionCheckActivitity.1
                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public void onAgreementInvoked(boolean z) {
                    if (!BasePermissionCheckActivitity.this.allowUseOnOffline()) {
                        BasePermissionCheckActivitity.this.finish();
                    }
                    BasePermissionCheckActivitity.this.onCtaChecked(z);
                }
            });
        }
    }
}
