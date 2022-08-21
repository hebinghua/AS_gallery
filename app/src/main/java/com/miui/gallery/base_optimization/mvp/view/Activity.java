package com.miui.gallery.base_optimization.mvp.view;

import android.os.Bundle;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.base_optimization.fragment.support.ActivityFragmentSupportDelegate;
import com.miui.gallery.base_optimization.fragment.support.FragmentSupport$IActivityFragmentSupport;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;

/* loaded from: classes.dex */
public abstract class Activity<P extends IPresenter> extends GalleryActivity implements FragmentSupport$IActivityFragmentSupport, IView<P> {
    public ActivityDelegate<P> mActivityDelegate;
    public FragmentSupport$IActivityFragmentSupport mFragmentSupport;

    public abstract int getLayoutId();

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActivityDelegate<P> activityDelegate = new ActivityDelegate<>(this);
        this.mActivityDelegate = activityDelegate;
        activityDelegate.onCreate(bundle);
    }

    public final void initFragmentSupport(int i) {
        if (this.mFragmentSupport == null) {
            this.mFragmentSupport = new ActivityFragmentSupportDelegate(i, this);
        }
    }

    @Override // com.miui.gallery.base_optimization.fragment.support.FragmentSupport$IActivityFragmentSupport
    public void loadRootFragment(int i, androidx.fragment.app.Fragment fragment) {
        if (this.mFragmentSupport == null) {
            initFragmentSupport(i);
        }
        this.mFragmentSupport.loadRootFragment(i, fragment);
    }

    @Override // com.miui.gallery.base_optimization.fragment.support.FragmentSupport$IActivityFragmentSupport
    public androidx.fragment.app.Fragment getTopFragment() {
        FragmentSupport$IActivityFragmentSupport fragmentSupport$IActivityFragmentSupport = this.mFragmentSupport;
        if (fragmentSupport$IActivityFragmentSupport == null) {
            return null;
        }
        return fragmentSupport$IActivityFragmentSupport.getTopFragment();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.mActivityDelegate = null;
        this.mFragmentSupport = null;
    }
}
