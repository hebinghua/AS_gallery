package com.miui.gallery.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.VideoPlayerCompat;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public abstract class BaseFragment extends GalleryFragment {
    public AppCompatActivity mActivity;
    public boolean mUserFirstVisible = false;

    public static /* synthetic */ void $r8$lambda$Ahs0L0qjGWDYEfvP6PEUsqxIVBw(BaseFragment baseFragment, Bundle bundle, Configuration configuration) {
        baseFragment.lambda$onCreate$0(bundle, configuration);
    }

    public abstract String getPageName();

    public int getThemeRes() {
        return 2131952018;
    }

    public void onUserFirstVisible() {
    }

    public boolean recordPageByDefault() {
        return true;
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int themeRes = getThemeRes();
        if (themeRes != 0) {
            setThemeRes(themeRes);
        }
        addScreenChangeListener(new IScreenChange.OnRestoreInstanceListener() { // from class: com.miui.gallery.ui.BaseFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnRestoreInstanceListener
            public final void onRestoreInstance(Bundle bundle2, Configuration configuration) {
                BaseFragment.$r8$lambda$Ahs0L0qjGWDYEfvP6PEUsqxIVBw(BaseFragment.this, bundle2, configuration);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0(Bundle bundle, Configuration configuration) {
        updateThumbConfig();
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateThumbConfig();
    }

    private void updateThumbConfig() {
        Config$ThumbConfig.get().updateConfig();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (recordPageByDefault()) {
            SamplingStatHelper.recordPageStart(this.mActivity, getPageName());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (recordPageByDefault()) {
            SamplingStatHelper.recordPageEnd(this.mActivity, getPageName());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof AppCompatActivity)) {
            throw new IllegalArgumentException("This fragment can only be attach to miuix.appcompat.app.AppCompatActivity");
        }
        this.mActivity = (AppCompatActivity) activity;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (!getUserVisibleHint() || this.mUserFirstVisible) {
            return;
        }
        this.mUserFirstVisible = true;
        onUserFirstVisible();
    }

    public void finish() {
        if (this.mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
            this.mActivity.getSupportFragmentManager().popBackStack();
        } else {
            this.mActivity.onBackPressed();
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.strategy.IStrategyFollower
    public boolean isSupportCutoutModeShortEdges() {
        return VideoPlayerCompat.isVideoPlayerSupportCutoutModeShortEdges();
    }
}
