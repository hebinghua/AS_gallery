package com.miui.gallery.ui.album.main.base.config;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import com.miui.gallery.R;
import com.miui.gallery.util.ResourceUtils;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class BaseIconStyle {
    public int mEnterAnimDuration;
    public int mExitAnimDuration;

    public BaseIconStyle() {
        initResource();
    }

    public void initResource() {
        this.mEnterAnimDuration = ResourceUtils.getInt(R.integer.widget_alpha_anim_enter_duration);
        this.mExitAnimDuration = ResourceUtils.getInt(R.integer.widget_alpha_anim_exit_duration);
    }

    public ValueAnimator getEnterAnimator() {
        ObjectAnimator duration = ObjectAnimator.ofFloat((Object) null, "alpha", 0.0f, 1.0f).setDuration(this.mEnterAnimDuration);
        duration.setInterpolator(new CubicEaseOutInterpolator());
        return duration;
    }

    public ValueAnimator getExitAnimator() {
        ObjectAnimator duration = ObjectAnimator.ofFloat((Object) null, "alpha", 1.0f, 0.0f).setDuration(this.mExitAnimDuration);
        duration.setInterpolator(new CubicEaseOutInterpolator());
        return duration;
    }
}
