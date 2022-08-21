package com.miui.gallery.picker;

import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.miui.display.DisplayFeatureHelper;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.permission.core.PermissionInjector;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes2.dex */
public class PickerCompatActivity extends PickerBaseActivity implements PermissionCheckCallback {
    public ActionBar mActionBar;
    public boolean mIsResumed;

    public boolean allowUseOnOffline() {
        return true;
    }

    public int getFragmentContainerId() {
        return 0;
    }

    public boolean hasCustomContentView() {
        return false;
    }

    public boolean useDefaultScreenSceneMode() {
        return true;
    }

    @Override // com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!hasCustomContentView()) {
            setContentView(R.layout.base_activity);
        }
        initActionBar();
        PermissionInjector.injectIfNeededIn(this, this, null);
    }

    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        if (!BaseGalleryPreferences.CTA.containCTACanConnectNetworkKey()) {
            GalleryPreferences.Sync.setNeedShowAutoDownloadDialog(true);
        }
        if ((!BaseGalleryPreferences.CTA.allowUseOnOfflineGlobal() || !allowUseOnOffline()) && !BaseGalleryPreferences.CTA.canConnectNetwork() && !BaseGalleryPreferences.CTA.canConnectNetwork()) {
            AgreementsUtils.showUserAgreements(this, null);
        }
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        return new Permission[]{new Permission("android.permission.WRITE_EXTERNAL_STORAGE", getString(R.string.permission_storage_desc), true)};
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if (useDefaultScreenSceneMode() && ScreenUtils.isUseScreenSceneMode()) {
            DisplayFeatureHelper.setScreenSceneClassification(0);
        }
        this.mIsResumed = true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mIsResumed = false;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
    }

    public void initActionBar() {
        this.mActionBar = getAppCompatActionBar();
    }

    @Override // android.app.Activity
    public void setTitle(CharSequence charSequence) {
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.setTitle(charSequence);
        }
    }

    public void startFragment(Fragment fragment, String str, boolean z, boolean z2) {
        if (getFragmentContainerId() <= 0) {
            throw new IllegalArgumentException("invalidate container id");
        }
        if (!TextUtils.isEmpty(str) && getSupportFragmentManager().findFragmentByTag(str) != null) {
            DefaultLogger.w("PickerCompatActivity", "already has tag of fragment %s", str);
            return;
        }
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (z) {
            beginTransaction.addToBackStack(str);
        }
        if (z2) {
            beginTransaction.add(getFragmentContainerId(), fragment, str);
        } else {
            beginTransaction.replace(getFragmentContainerId(), fragment, str);
        }
        beginTransaction.commitAllowingStateLoss();
    }
}
