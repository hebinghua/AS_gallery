package com.miui.gallery.app.base;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.base_optimization.mvp.view.Fragment;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.VideoPlayerCompat;

/* loaded from: classes.dex */
public abstract class BaseFragment<P extends IPresenter> extends Fragment<P> {
    public Configuration mConfiguration;

    /* renamed from: $r8$lambda$-jLiLqzb1MZUfiNWIMLQQosSadk */
    public static /* synthetic */ void m548$r8$lambda$jLiLqzb1MZUfiNWIMLQQosSadk(BaseFragment baseFragment, Configuration configuration) {
        baseFragment.lambda$onCreate$0(configuration);
    }

    /* renamed from: $r8$lambda$zSX-sZrFXX9dUTPYkBISDkNNK8k */
    public static /* synthetic */ void m549$r8$lambda$zSXsZrFXX9dUTPYkBISDkNNK8k(BaseFragment baseFragment, Bundle bundle, Configuration configuration) {
        baseFragment.lambda$onCreate$1(bundle, configuration);
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment
    public int getThemeRes() {
        return 2131952018;
    }

    public void onActivityDestroy() {
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onScreenOrientationChangeToHorizontal() {
    }

    public void onScreenOrientationChangeToVertical() {
    }

    public boolean recordPageByDefault() {
        return true;
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mConfiguration = getResources().getConfiguration();
        addScreenChangeListener(new IScreenChange.OnOrientationChangeListener() { // from class: com.miui.gallery.app.base.BaseFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnOrientationChangeListener
            public final void onOrientationChange(Configuration configuration) {
                BaseFragment.m548$r8$lambda$jLiLqzb1MZUfiNWIMLQQosSadk(BaseFragment.this, configuration);
            }
        });
        addScreenChangeListener(new IScreenChange.OnRestoreInstanceListener() { // from class: com.miui.gallery.app.base.BaseFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnRestoreInstanceListener
            public final void onRestoreInstance(Bundle bundle2, Configuration configuration) {
                BaseFragment.m549$r8$lambda$zSXsZrFXX9dUTPYkBISDkNNK8k(BaseFragment.this, bundle2, configuration);
            }
        });
    }

    public /* synthetic */ void lambda$onCreate$0(Configuration configuration) {
        int i = configuration.orientation;
        if (i == 1) {
            onScreenOrientationChangeToVertical();
        } else if (i != 2) {
        } else {
            onScreenOrientationChangeToHorizontal();
        }
    }

    public /* synthetic */ void lambda$onCreate$1(Bundle bundle, Configuration configuration) {
        updateThumbConfig();
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (recordPageByDefault()) {
            SamplingStatHelper.recordPageStart(getActivity(), getPageName());
        }
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (recordPageByDefault()) {
            SamplingStatHelper.recordPageEnd(getActivity(), getPageName());
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mConfiguration = configuration;
        updateThumbConfig();
    }

    public final void updateThumbConfig() {
        Config$ThumbConfig.get().updateConfig();
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.strategy.IStrategyFollower
    public boolean isSupportCutoutModeShortEdges() {
        return VideoPlayerCompat.isVideoPlayerSupportCutoutModeShortEdges();
    }
}
