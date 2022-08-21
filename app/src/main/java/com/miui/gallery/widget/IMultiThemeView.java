package com.miui.gallery.widget;

import android.view.animation.Interpolator;

/* loaded from: classes2.dex */
public interface IMultiThemeView {

    /* loaded from: classes2.dex */
    public enum Theme {
        LIGHT,
        DARK
    }

    /* loaded from: classes2.dex */
    public enum ThemeTransition {
        NONE,
        FADE_OUT,
        FADE_IN
    }

    void setBackgroundAlpha(float f);

    void setTheme(Theme theme, ThemeTransition themeTransition);

    void setTheme(Theme theme, ThemeTransition themeTransition, Interpolator interpolator, long j);
}
