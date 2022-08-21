package com.miui.gallery.activity;

import android.animation.TimeInterpolator;
import android.content.Intent;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.transition.TransitionSet;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.compat.transition.TransitionCompat;
import com.miui.gallery.compat.view.WindowCompat;
import com.miui.gallery.transition.CrossFade;
import com.miui.gallery.transition.PhotoViewTransition;
import com.miui.gallery.ui.BurstPhotoFragment;
import com.miui.gallery.util.OrientationCheckHelper;
import com.miui.gallery.util.SystemUiUtil;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes.dex */
public class BurstPhotoActivity extends BaseActivity {
    public boolean hasTransition;
    public BurstPhotoFragment mFragment;

    public static /* synthetic */ Fragment $r8$lambda$XWiIDcdItkSiaTL6B_JsWGV1QjQ(Intent intent, String str) {
        return BurstPhotoFragment.newInstance(intent);
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return 16908290;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        final Intent intent = getIntent();
        if (intent == null || intent.getData() == null) {
            finish();
            return;
        }
        super.onCreate(bundle);
        this.mFragment = (BurstPhotoFragment) startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.activity.BurstPhotoActivity$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str) {
                return BurstPhotoActivity.$r8$lambda$XWiIDcdItkSiaTL6B_JsWGV1QjQ(intent, str);
            }
        }, "BurstPhotoFragment", false, true);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.burst_background_color));
        getWindow().getDecorView().setBackgroundResource(R.color.burst_background_color);
        if (!OrientationCheckHelper.isSupportOrientationChange()) {
            SystemUiUtil.setRequestedOrientation(1, this);
        }
        requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        super.finish();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!this.mFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void onTransitionImageLoadFinish() {
        if (this.hasTransition) {
            configureEnterTransition();
            ActivityCompat.startPostponedEnterTransition(this);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().getDecorView().setSystemUiVisibility(1284);
    }

    public void onExit(Drawable drawable) {
        if (this.hasTransition) {
            configureExitTransition(drawable);
        }
    }

    public final void configureEnterTransition() {
        TransitionSet transitionSet = new TransitionSet();
        PhotoViewTransition photoViewTransition = new PhotoViewTransition(true, (RectF) getIntent().getParcelableExtra("extra_display_rect"));
        ChangeBounds changeBounds = new ChangeBounds();
        transitionSet.setInterpolator((TimeInterpolator) new CubicEaseOutInterpolator());
        transitionSet.addTransition(photoViewTransition);
        transitionSet.addTransition(changeBounds);
        TransitionCompat.addTarget(transitionSet, getResources().getString(R.string.burst_transition_image_view));
        WindowCompat.setSharedElementEnterTransition(getWindow(), transitionSet);
    }

    public final void configureExitTransition(Drawable drawable) {
        TransitionSet transitionSet = new TransitionSet();
        PhotoViewTransition photoViewTransition = new PhotoViewTransition(false, (RectF) getIntent().getParcelableExtra("extra_display_rect"));
        CrossFade crossFade = new CrossFade(false, drawable);
        ChangeBounds changeBounds = new ChangeBounds();
        transitionSet.setInterpolator((TimeInterpolator) new CubicEaseOutInterpolator());
        transitionSet.addTransition(photoViewTransition);
        transitionSet.addTransition(crossFade);
        transitionSet.addTransition(changeBounds);
        TransitionCompat.addTarget(transitionSet, getResources().getString(R.string.burst_transition_image_view));
        WindowCompat.setSharedElementReturnTransition(getWindow(), transitionSet);
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        BurstPhotoFragment burstPhotoFragment = this.mFragment;
        if (burstPhotoFragment != null) {
            burstPhotoFragment.onAttachDialogFragment(fragment);
        }
    }
}
