package com.miui.gallery.app.base;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.app.AutoTracking;

/* loaded from: classes.dex */
public abstract class BaseToolBarActivity extends com.miui.gallery.base_optimization.mvp.view.BaseToolBarActivity {
    public FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallback;

    @Override // com.miui.gallery.base_optimization.mvp.view.BaseToolBarActivity, com.miui.gallery.base_optimization.mvp.view.Activity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mFragmentLifecycleCallback == null) {
            this.mFragmentLifecycleCallback = new FragmentManager.FragmentLifecycleCallbacks() { // from class: com.miui.gallery.app.base.BaseToolBarActivity.1
                @Override // androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks
                public void onFragmentResumed(FragmentManager fragmentManager, Fragment fragment) {
                    super.onFragmentResumed(fragmentManager, fragment);
                    if (!AutoTracking.contains(fragment.getClass().getCanonicalName()) || !fragment.getUserVisibleHint()) {
                        return;
                    }
                    AutoTracking.track(fragment.getClass().getCanonicalName());
                }
            };
        }
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(this.mFragmentLifecycleCallback, true);
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Activity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        if (this.mFragmentLifecycleCallback != null) {
            getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(this.mFragmentLifecycleCallback);
        }
    }
}
