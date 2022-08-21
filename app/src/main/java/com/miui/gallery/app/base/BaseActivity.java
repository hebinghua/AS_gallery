package com.miui.gallery.app.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.android.internal.WindowCompat;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.util.ReceiverUtils;

/* loaded from: classes.dex */
public abstract class BaseActivity<P extends IPresenter> extends BasePermissionCheckActivitity<P> {
    public FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallback;
    public BroadcastReceiver mScreenReceiver;

    public boolean supportShowOnScreenLocked() {
        return false;
    }

    @Override // com.miui.gallery.app.base.BasePermissionCheckActivitity, com.miui.gallery.base_optimization.mvp.view.Activity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (supportShowOnScreenLocked() && isShowWhenLocked()) {
            WindowCompat.setShowWhenLocked(this, true);
            ScreenBroadcastReceiver screenBroadcastReceiver = new ScreenBroadcastReceiver();
            this.mScreenReceiver = screenBroadcastReceiver;
            ReceiverUtils.registerReceiver(this, screenBroadcastReceiver, "android.intent.action.SCREEN_OFF");
        }
        this.mFragmentLifecycleCallback = new FragmentManager.FragmentLifecycleCallbacks() { // from class: com.miui.gallery.app.base.BaseActivity.1
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
    }

    @Override // com.miui.gallery.app.base.BasePermissionCheckActivitity, com.miui.gallery.base_optimization.mvp.view.Activity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        BroadcastReceiver broadcastReceiver = this.mScreenReceiver;
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        if (this.mFragmentLifecycleCallback != null) {
            getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(this.mFragmentLifecycleCallback);
        }
    }

    @Override // com.miui.gallery.app.base.BasePermissionCheckActivitity
    public boolean isShowWhenLocked() {
        return getIntent().getBooleanExtra("StartActivityWhenLocked", false);
    }

    /* loaded from: classes.dex */
    public class ScreenBroadcastReceiver extends BroadcastReceiver {
        public ScreenBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!"android.intent.action.SCREEN_OFF".equals(intent.getAction()) || BaseActivity.this.isFinishing()) {
                return;
            }
            BaseActivity.this.finish();
        }
    }
}
