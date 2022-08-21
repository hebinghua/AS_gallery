package com.miui.gallery.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Trace;
import android.text.TextUtils;
import android.view.KeyEvent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.internal.WindowCompat;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.permission.core.PermissionInjector;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ReceiverUtils;
import com.miui.gallery.util.TransitionPatching;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes.dex */
public class BaseActivity extends GalleryActivity implements PermissionCheckCallback {
    public ActionBar mActionBar;
    public FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallback;
    public boolean mIsResumed;
    public boolean mIsStartingEnterTransition = false;
    public BroadcastReceiver mScreenReceiver;

    /* loaded from: classes.dex */
    public interface FragmentCreator {
        Fragment create(String str);
    }

    public static /* synthetic */ void $r8$lambda$E8VUNCbSodKe8o5uuRWyzHjHPVs(BaseActivity baseActivity, boolean z) {
        baseActivity.lambda$onPermissionsChecked$0(z);
    }

    public boolean allowUseOnOffline() {
        return true;
    }

    public int getFragmentContainerId() {
        return 0;
    }

    public boolean hasCustomContentView() {
        return false;
    }

    public boolean isCheckPermissionCustomized() {
        return false;
    }

    public boolean needShowUserAgreements() {
        return true;
    }

    public void onCtaChecked(boolean z, boolean z2) {
    }

    public boolean supportEnterSetting() {
        return false;
    }

    public boolean supportShowOnScreenLocked() {
        return false;
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            Trace.beginSection("BaseActivityInternalCreate");
            if (!hasCustomContentView()) {
                setContentView(R.layout.base_activity);
            }
            initActionBar();
            if (!isCheckPermissionCustomized()) {
                Trace.beginSection("checkPermission");
                checkPermission();
                Trace.endSection();
            }
            if (supportShowOnScreenLocked() && isShowWhenLocked()) {
                WindowCompat.setShowWhenLocked(this, true);
                ScreenBroadcastReceiver screenBroadcastReceiver = new ScreenBroadcastReceiver();
                this.mScreenReceiver = screenBroadcastReceiver;
                ReceiverUtils.registerReceiver(this, screenBroadcastReceiver, "android.intent.action.SCREEN_OFF");
            }
            this.mFragmentLifecycleCallback = new FragmentManager.FragmentLifecycleCallbacks() { // from class: com.miui.gallery.activity.BaseActivity.1
                {
                    BaseActivity.this = this;
                }

                @Override // androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
                public void onFragmentResumed(FragmentManager fragmentManager, Fragment fragment) {
                    super.onFragmentResumed(fragmentManager, fragment);
                    if (!AutoTracking.contains(fragment.getClass().getCanonicalName()) || !fragment.getUserVisibleHint()) {
                        return;
                    }
                    AutoTracking.track(fragment.getClass().getCanonicalName());
                }
            };
            getSupportFragmentManager().registerFragmentLifecycleCallbacks(this.mFragmentLifecycleCallback, true);
        } finally {
            Trace.endSection();
        }
    }

    public void checkPermission() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("SHOW_WHEN_LOCKED", isShowWhenLocked());
        PermissionInjector.injectIfNeededIn(this, this, bundle);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        if (this.mIsStartingEnterTransition) {
            TransitionPatching.onActivityStopWhenEnterStarting(this);
            this.mIsStartingEnterTransition = false;
        }
        super.onStop();
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.mIsResumed = true;
        this.mIsStartingEnterTransition = false;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mIsResumed = false;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        BroadcastReceiver broadcastReceiver = this.mScreenReceiver;
        if (broadcastReceiver != null) {
            ReceiverUtils.safeUnregisterReceiver(this, broadcastReceiver);
        }
        if (this.mFragmentLifecycleCallback != null) {
            getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(this.mFragmentLifecycleCallback);
        }
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

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        return new Permission[]{new Permission("android.permission.WRITE_EXTERNAL_STORAGE", getString(R.string.permission_storage_desc), true)};
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        if (!BaseGalleryPreferences.CTA.containCTACanConnectNetworkKey()) {
            GalleryPreferences.Sync.setNeedShowAutoDownloadDialog(true);
        }
        if ((BaseGalleryPreferences.CTA.allowUseOnOfflineGlobal() && allowUseOnOffline()) || BaseGalleryPreferences.CTA.canConnectNetwork() || !needShowUserAgreements()) {
            onCtaChecked(true, false);
        } else if (!BaseBuildUtil.isInternational() && BaseGalleryPreferences.CTA.hasShownNetworkingAgreements() && !BaseGalleryPreferences.CTA.remindConnectNetworkEveryTime()) {
            onCtaChecked(false, false);
            if (allowUseOnOffline()) {
                return;
            }
            finish();
            return;
        } else {
            AgreementsUtils.showUserAgreements(this, isShowWhenLocked(), new OnAgreementInvokedListener() { // from class: com.miui.gallery.activity.BaseActivity$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.agreement.core.OnAgreementInvokedListener
                public final void onAgreementInvoked(boolean z) {
                    BaseActivity.$r8$lambda$E8VUNCbSodKe8o5uuRWyzHjHPVs(BaseActivity.this, z);
                }
            });
        }
        if (permissionArr == null || permissionArr.length <= 0) {
            return;
        }
        for (int i = 0; i < permissionArr.length; i++) {
            if ("android.permission.WRITE_EXTERNAL_STORAGE".equals(permissionArr[i].mName) && iArr[i] == 0 && zArr[i]) {
                ScannerEngine.getInstance().triggerScan();
            }
        }
    }

    public /* synthetic */ void lambda$onPermissionsChecked$0(boolean z) {
        onCtaChecked(z, true);
        if (!BaseBuildUtil.isInternational() && !z) {
            BaseGalleryPreferences.CTA.setRemindConnectNetworkEveryTime(false);
        }
        if (z || allowUseOnOffline()) {
            return;
        }
        finish();
    }

    public <T extends Fragment> T startFragment(FragmentCreator fragmentCreator, String str, boolean z, boolean z2) {
        T t;
        if (getFragmentContainerId() <= 0) {
            throw new IllegalArgumentException("invalidate container id");
        }
        if (!TextUtils.isEmpty(str) && (t = (T) getSupportFragmentManager().findFragmentByTag(str)) != null) {
            DefaultLogger.w("BaseActivity", "already has tag of fragment %s", str);
            return t;
        }
        T t2 = (T) fragmentCreator.create(str);
        if (t2 == null) {
            throw new IllegalArgumentException("create fragment failed, tag: " + str);
        }
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        if (z) {
            beginTransaction.addToBackStack(str);
        }
        if (z2) {
            beginTransaction.add(getFragmentContainerId(), t2, str);
        } else {
            beginTransaction.replace(getFragmentContainerId(), t2, str);
        }
        beginTransaction.commitAllowingStateLoss();
        return t2;
    }

    public boolean isShowWhenLocked() {
        return getIntent().getBooleanExtra("StartActivityWhenLocked", false);
    }

    /* loaded from: classes.dex */
    public class ScreenBroadcastReceiver extends BroadcastReceiver {
        public ScreenBroadcastReceiver() {
            BaseActivity.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!"android.intent.action.SCREEN_OFF".equals(intent.getAction()) || BaseActivity.this.isFinishing()) {
                return;
            }
            BaseActivity.this.finish();
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if ((i == 82 || i == 187) && supportEnterSetting()) {
            IntentUtil.enterGallerySetting(this);
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    public final boolean resumed() {
        return this.mIsResumed;
    }

    @Override // android.app.Activity
    public void startPostponedEnterTransition() {
        super.startPostponedEnterTransition();
        this.mIsStartingEnterTransition = true;
        TransitionPatching.setOnEnterStartedListener(this, new Runnable() { // from class: com.miui.gallery.activity.BaseActivity.2
            {
                BaseActivity.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                BaseActivity.this.mIsStartingEnterTransition = false;
            }
        });
    }
}
