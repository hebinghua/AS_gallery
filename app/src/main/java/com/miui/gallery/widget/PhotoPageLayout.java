package com.miui.gallery.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.IMultiThemeView;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.SineEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class PhotoPageLayout extends FrameLayout implements IMultiThemeView {
    public static final IMultiThemeView.Theme DEFAULT_THEME = IMultiThemeView.Theme.LIGHT;
    public float mAnimFinalValue;
    public float mBackgroundAlpha;
    public int mBackgroundColor;
    public ValueAnimator mColorAnim;
    public float mPreBackgroundAlpha;
    public int mPreBackgroundColor;
    public IMultiThemeView.Theme mTheme;
    public IMultiThemeView.ThemeTransition mTransition;

    public PhotoPageLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PhotoPageLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public final void init() {
        setWillNotDraw(false);
        this.mBackgroundAlpha = 1.0f;
        this.mPreBackgroundAlpha = 0.0f;
        setTheme(DEFAULT_THEME);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            return ((int) motionEvent.getY()) >= ScreenUtils.getFullScreenHeight(getContext()) - ScreenUtils.getNavBarHeight(getContext());
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void setTheme(IMultiThemeView.Theme theme) {
        setTheme(theme, IMultiThemeView.ThemeTransition.NONE);
    }

    @Override // com.miui.gallery.widget.IMultiThemeView
    public void setTheme(IMultiThemeView.Theme theme, IMultiThemeView.ThemeTransition themeTransition) {
        setTheme(theme, themeTransition, getDefaultTransitionInterpolator(themeTransition), getDefaultTransitionTime(themeTransition));
    }

    @Override // com.miui.gallery.widget.IMultiThemeView
    public void setTheme(IMultiThemeView.Theme theme, IMultiThemeView.ThemeTransition themeTransition, Interpolator interpolator, long j) {
        float f;
        IMultiThemeView.Theme theme2 = this.mTheme;
        if (theme2 == theme) {
            return;
        }
        DefaultLogger.d("PhotoPageLayout", "setTheme %s -> %s", theme2, theme);
        this.mTheme = theme;
        this.mPreBackgroundColor = this.mBackgroundColor;
        this.mBackgroundColor = getBackgroundColor(theme);
        this.mTransition = themeTransition;
        int i = AnonymousClass2.$SwitchMap$com$miui$gallery$widget$IMultiThemeView$ThemeTransition[themeTransition.ordinal()];
        float f2 = 0.0f;
        if (i == 1) {
            this.mPreBackgroundAlpha = this.mBackgroundAlpha;
            this.mBackgroundAlpha = 0.0f;
            if (isBackColorTransiting()) {
                this.mPreBackgroundAlpha = this.mAnimFinalValue;
            } else {
                this.mAnimFinalValue = this.mPreBackgroundAlpha;
            }
            f = this.mAnimFinalValue;
        } else if (i == 2) {
            if (isBackColorTransiting()) {
                this.mBackgroundAlpha = this.mAnimFinalValue;
            } else {
                this.mAnimFinalValue = this.mBackgroundAlpha;
            }
            float f3 = this.mAnimFinalValue;
            this.mPreBackgroundAlpha = f3;
            f2 = f3;
            f = 0.0f;
        } else {
            this.mPreBackgroundAlpha = 0.0f;
            this.mBackgroundAlpha = 1.0f;
            this.mAnimFinalValue = 1.0f;
            f = 1.0f;
            f2 = 1.0f;
        }
        if (!BaseMiscUtil.floatEquals(f2, f)) {
            cancelBackgroundTransition();
            if (j <= 0) {
                j = getDefaultTransitionTime(themeTransition);
            }
            if (interpolator == null) {
                interpolator = getDefaultTransitionInterpolator(themeTransition);
            }
            ValueAnimator duration = ValueAnimator.ofFloat(f2, f).setDuration(j);
            this.mColorAnim = duration;
            duration.setInterpolator(interpolator);
            this.mColorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.widget.PhotoPageLayout.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    int i2 = AnonymousClass2.$SwitchMap$com$miui$gallery$widget$IMultiThemeView$ThemeTransition[PhotoPageLayout.this.mTransition.ordinal()];
                    if (i2 == 1) {
                        PhotoPageLayout.this.mBackgroundAlpha = floatValue;
                    } else if (i2 == 2) {
                        PhotoPageLayout.this.mPreBackgroundAlpha = floatValue;
                    }
                    PhotoPageLayout.this.invalidate();
                }
            });
            this.mColorAnim.start();
            return;
        }
        invalidate();
    }

    public final Interpolator getDefaultTransitionInterpolator(IMultiThemeView.ThemeTransition themeTransition) {
        int i = AnonymousClass2.$SwitchMap$com$miui$gallery$widget$IMultiThemeView$ThemeTransition[themeTransition.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return new SineEaseInOutInterpolator();
            }
            return null;
        }
        return new CubicEaseOutInterpolator();
    }

    public final long getDefaultTransitionTime(IMultiThemeView.ThemeTransition themeTransition) {
        int integer;
        int i = AnonymousClass2.$SwitchMap$com$miui$gallery$widget$IMultiThemeView$ThemeTransition[themeTransition.ordinal()];
        if (i == 1) {
            integer = getResources().getInteger(R.integer.photo_background_in_duration);
        } else if (i != 2) {
            return 0L;
        } else {
            integer = getResources().getInteger(R.integer.photo_background_out_duration);
        }
        return integer;
    }

    /* renamed from: com.miui.gallery.widget.PhotoPageLayout$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$widget$IMultiThemeView$Theme;
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$widget$IMultiThemeView$ThemeTransition;

        static {
            int[] iArr = new int[IMultiThemeView.Theme.values().length];
            $SwitchMap$com$miui$gallery$widget$IMultiThemeView$Theme = iArr;
            try {
                iArr[IMultiThemeView.Theme.LIGHT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$widget$IMultiThemeView$Theme[IMultiThemeView.Theme.DARK.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            int[] iArr2 = new int[IMultiThemeView.ThemeTransition.values().length];
            $SwitchMap$com$miui$gallery$widget$IMultiThemeView$ThemeTransition = iArr2;
            try {
                iArr2[IMultiThemeView.ThemeTransition.FADE_IN.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$widget$IMultiThemeView$ThemeTransition[IMultiThemeView.ThemeTransition.FADE_OUT.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public final int getBackgroundColor(IMultiThemeView.Theme theme) {
        int i = AnonymousClass2.$SwitchMap$com$miui$gallery$widget$IMultiThemeView$Theme[theme.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return getContext().getResources().getColor(R.color.photo_page_fullscreen_background);
            }
            return 0;
        }
        return getContext().getResources().getColor(R.color.photo_page_default_background);
    }

    @Override // com.miui.gallery.widget.IMultiThemeView
    public void setBackgroundAlpha(float f) {
        if (isBackColorTransiting() && BaseMiscUtil.floatEquals(f, this.mAnimFinalValue)) {
            DefaultLogger.d("PhotoPageLayout", "color transiting while set alpha %s", Float.valueOf(f));
            return;
        }
        cancelBackgroundTransition();
        this.mPreBackgroundAlpha = 0.0f;
        if (BaseMiscUtil.floatEquals(this.mBackgroundAlpha, f)) {
            return;
        }
        this.mBackgroundAlpha = f;
        invalidate();
    }

    public final boolean isBackColorTransiting() {
        ValueAnimator valueAnimator = this.mColorAnim;
        return valueAnimator != null && valueAnimator.isRunning();
    }

    public final void cancelBackgroundTransition() {
        if (isBackColorTransiting()) {
            this.mColorAnim.cancel();
        }
    }

    public final int genColor(int i, float f) {
        return Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
    }

    public final void drawBackground(Canvas canvas) {
        if (isBackColorTransiting()) {
            int i = this.mPreBackgroundColor;
            float f = this.mPreBackgroundAlpha;
            int i2 = this.mBackgroundColor;
            float f2 = this.mBackgroundAlpha;
            if (this.mTransition == IMultiThemeView.ThemeTransition.FADE_OUT) {
                i2 = i;
                i = i2;
                f2 = f;
                f = f2;
            }
            if (f > 0.0f) {
                canvas.save();
                drawColor(canvas, i, f);
                canvas.restore();
            }
            drawColor(canvas, i2, f2);
            return;
        }
        drawColor(canvas, this.mBackgroundColor, this.mBackgroundAlpha);
    }

    public final void drawColor(Canvas canvas, int i, float f) {
        if (f > 0.0f) {
            canvas.drawColor(genColor(i, f));
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        drawBackground(canvas);
        super.onDraw(canvas);
    }
}
