package com.miui.gallery.vlog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.home.VlogContract$IVlogView;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.vlog.view.VlogMenuTopView;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public abstract class MenuFragment<T extends BasePresenter> extends Fragment {
    public static int sAnimAppearDuration;
    public static int sAnimDisappearDuration;
    public static int sAnimOffset;
    public Configuration mConfiguration;
    public Context mContext;
    public VlogContract$IVlogView mIVlogView;
    public T mMenuPresenter;
    public View mRootView;

    /* renamed from: createPresenter */
    public abstract T mo1801createPresenter();

    public abstract View createView(LayoutInflater layoutInflater, ViewGroup viewGroup);

    public void doAnimationEnd(boolean z) {
    }

    public View getOperationView() {
        return null;
    }

    public View getSeparatedView() {
        return null;
    }

    public abstract void initView(View view);

    public boolean isSetTopMenuView() {
        return true;
    }

    public boolean isUseAnimator() {
        return true;
    }

    public boolean onBackPressed() {
        return false;
    }

    public void onPlaybackEOF() {
    }

    public void onPlaybackStopped() {
    }

    public void onPlaybackTimelinePosition(long j) {
    }

    public void onTimelineStarted() {
    }

    public void seekTime(long j) {
    }

    public void updateClipList() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initAnimatorData();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View createView = createView(layoutInflater, viewGroup);
        this.mRootView = createView;
        initView(createView);
        this.mMenuPresenter = mo1801createPresenter();
        this.mRootView.setBackgroundColor(getResources().getColor(R$color.vlog_bg));
        return this.mRootView;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (isSetTopMenuView()) {
            setMenuTopView();
        }
        if (isLandscape()) {
            setMenuSeparatedView();
        }
        initData();
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        if (context instanceof VlogContract$IVlogView) {
            this.mIVlogView = (VlogContract$IVlogView) context;
        }
        this.mConfiguration = new Configuration(this.mContext.getResources().getConfiguration());
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z && isSetTopMenuView()) {
            setMenuTopView();
        }
        if (z || !isLandscape()) {
            return;
        }
        setMenuSeparatedView();
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if ((this.mConfiguration.updateFrom(configuration) & 1024) != 0) {
            onScreenSizeChanged(configuration.screenLayout & 15);
        }
    }

    public void onScreenSizeChanged(int i) {
        DefaultLogger.d("MenuFragment", "onScreenSizeChanged:" + i);
    }

    public void setMenuTopView() {
        this.mIVlogView.setTopView(getOperationView());
    }

    public void setMenuSeparatedView() {
        this.mIVlogView.setSeparatedView(getSeparatedView());
    }

    public VlogMenuTopView getMenuTopView() {
        return this.mIVlogView.getTopView();
    }

    public VlogContract$IVlogView getIVlogView() {
        return this.mIVlogView;
    }

    public View getTitleView() {
        return LayoutInflater.from(getContext()).inflate(R$layout.vlog_editor_title_layout, (ViewGroup) null, false);
    }

    public View getTitleViewWithCustomTitle(String str) {
        View titleView = getTitleView();
        ((TextView) titleView.findViewById(R$id.effect_title)).setText(str);
        return titleView;
    }

    private void initData() {
        T t = this.mMenuPresenter;
        if (t != null) {
            t.loadData();
        }
    }

    public View getViewById(int i) {
        return this.mRootView.findViewById(i);
    }

    public MiVideoSdkManager getMiVideoSdkManager() {
        Context context = this.mContext;
        if (context == null) {
            return null;
        }
        return ((VlogModel) VlogUtils.getViewModel((FragmentActivity) context, VlogModel.class)).getSdkManager();
    }

    private void initAnimatorData() {
        if (sAnimOffset == 0) {
            sAnimOffset = getActivity().getResources().getDimensionPixelSize(R$dimen.vlog_enter_sub_editor_main_menu_x_offset);
        }
        if (sAnimAppearDuration == 0) {
            sAnimAppearDuration = getActivity().getResources().getInteger(R$integer.vlog_menu_appear_duration);
        }
        if (sAnimDisappearDuration == 0) {
            sAnimDisappearDuration = getActivity().getResources().getInteger(R$integer.vlog_sub_editor_sub_menu_disappear_duration);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, final boolean z, int i2) {
        PropertyValuesHolder ofFloat;
        if (!isUseAnimator()) {
            return null;
        }
        ObjectAnimator objectAnimator = new ObjectAnimator();
        if (z) {
            if (this.mIVlogView.isClickRightTab()) {
                ofFloat = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, sAnimOffset, 0.0f);
            } else {
                ofFloat = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -sAnimOffset, 0.0f);
            }
            objectAnimator.setValues(ofFloat, PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f));
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            if (getView() != null) {
                getView().setVisibility(4);
            }
            objectAnimator.setDuration(sAnimAppearDuration);
        } else {
            PropertyValuesHolder ofFloat2 = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            objectAnimator.setValues(ofFloat2);
            objectAnimator.setDuration(sAnimDisappearDuration);
        }
        objectAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.vlog.MenuFragment.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                MenuFragment.this.doAnimationStart(z);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                MenuFragment.this.doAnimationEnd(z);
            }
        });
        return objectAnimator;
    }

    public void doAnimationStart(boolean z) {
        if (!z || getView() == null) {
            return;
        }
        getView().setVisibility(0);
    }

    public void onSeek(long j) {
        ((VlogActivity) this.mIVlogView).getVlogPresenter().seek(j);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public boolean isLandscape() {
        return VlogUtils.isLandscape(getContext());
    }
}
