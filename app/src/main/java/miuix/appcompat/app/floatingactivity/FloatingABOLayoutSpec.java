package miuix.appcompat.app.floatingactivity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper;
import miuix.core.util.WindowUtils;
import miuix.internal.util.AttributeResolver;
import miuix.reflect.Reflects;

/* loaded from: classes3.dex */
public class FloatingABOLayoutSpec {
    public Context mContext;
    public DisplayMetrics mDisplayMetrics;
    public TypedValue mFixedHeightMajor;
    public TypedValue mFixedHeightMinor;
    public TypedValue mFixedWidthMajor;
    public TypedValue mFixedWidthMinor;
    public boolean mIsInDialogMode;
    public TypedValue mMaxHeightMajor;
    public TypedValue mMaxHeightMinor;
    public TypedValue mMaxWidthMajor;
    public TypedValue mMaxWidthMinor;
    public boolean mFloatingTheme = false;
    public boolean mFloatingWindow = false;
    public Point mPhysicalSize = new Point();

    public FloatingABOLayoutSpec(Context context, AttributeSet attributeSet) {
        this.mContext = context;
        updatePhysicalSize(context);
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
        parseWindowSize(context, attributeSet);
    }

    public void updatePhysicalSize(Context context) {
        this.mPhysicalSize = WindowUtils.getWindowSize(context);
    }

    public void setIsInDialogMode(boolean z) {
        this.mIsInDialogMode = z;
    }

    public final void parseWindowSize(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) {
            return;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Window);
        int i = R$styleable.Window_windowFixedWidthMinor;
        if (obtainStyledAttributes.hasValue(i)) {
            TypedValue typedValue = new TypedValue();
            this.mFixedWidthMinor = typedValue;
            obtainStyledAttributes.getValue(i, typedValue);
        }
        int i2 = R$styleable.Window_windowFixedHeightMajor;
        if (obtainStyledAttributes.hasValue(i2)) {
            TypedValue typedValue2 = new TypedValue();
            this.mFixedHeightMajor = typedValue2;
            obtainStyledAttributes.getValue(i2, typedValue2);
        }
        int i3 = R$styleable.Window_windowFixedWidthMajor;
        if (obtainStyledAttributes.hasValue(i3)) {
            TypedValue typedValue3 = new TypedValue();
            this.mFixedWidthMajor = typedValue3;
            obtainStyledAttributes.getValue(i3, typedValue3);
        }
        int i4 = R$styleable.Window_windowFixedHeightMinor;
        if (obtainStyledAttributes.hasValue(i4)) {
            TypedValue typedValue4 = new TypedValue();
            this.mFixedHeightMinor = typedValue4;
            obtainStyledAttributes.getValue(i4, typedValue4);
        }
        int i5 = R$styleable.Window_windowMaxWidthMinor;
        if (obtainStyledAttributes.hasValue(i5)) {
            TypedValue typedValue5 = new TypedValue();
            this.mMaxWidthMinor = typedValue5;
            obtainStyledAttributes.getValue(i5, typedValue5);
        }
        int i6 = R$styleable.Window_windowMaxWidthMajor;
        if (obtainStyledAttributes.hasValue(i6)) {
            TypedValue typedValue6 = new TypedValue();
            this.mMaxWidthMajor = typedValue6;
            obtainStyledAttributes.getValue(i6, typedValue6);
        }
        int i7 = R$styleable.Window_windowMaxHeightMajor;
        if (obtainStyledAttributes.hasValue(i7)) {
            TypedValue typedValue7 = new TypedValue();
            this.mMaxHeightMajor = typedValue7;
            obtainStyledAttributes.getValue(i7, typedValue7);
        }
        int i8 = R$styleable.Window_windowMaxHeightMinor;
        if (obtainStyledAttributes.hasValue(i8)) {
            TypedValue typedValue8 = new TypedValue();
            this.mMaxHeightMinor = typedValue8;
            obtainStyledAttributes.getValue(i8, typedValue8);
        }
        this.mFloatingTheme = obtainStyledAttributes.getBoolean(R$styleable.Window_isMiuixFloatingTheme, false);
        this.mFloatingWindow = BaseFloatingActivityHelper.isFloatingWindow(context);
        obtainStyledAttributes.recycle();
    }

    public void onFloatingModeChanged(boolean z) {
        if (!this.mFloatingTheme) {
            return;
        }
        this.mFloatingWindow = z;
    }

    public int getWidthMeasureSpecForDialog(int i) {
        return getMeasureSpec(i, true, this.mFixedWidthMinor, this.mFixedWidthMajor, this.mMaxWidthMinor, this.mMaxWidthMajor);
    }

    public int getHeightMeasureSpecForDialog(int i) {
        return getMeasureSpec(i, false, this.mFixedHeightMinor, this.mFixedHeightMajor, this.mMaxHeightMinor, this.mMaxHeightMajor);
    }

    public int getWidthMeasureSpec(int i) {
        return getMeasureSpec(i, true, getFixedWidthMinor(), getFixedWidthMajor(), getMaxWidthMinor(), getMaxWidthMajor());
    }

    public int getHeightMeasureSpec(int i) {
        return getMeasureSpec(i, false, getFixedHeightMinor(), getFixedHeightMajor(), getMaxHeightMinor(), getMaxHeightMajor());
    }

    public final int getMeasureSpec(int i, boolean z, TypedValue typedValue, TypedValue typedValue2, TypedValue typedValue3, TypedValue typedValue4) {
        if (View.MeasureSpec.getMode(i) == Integer.MIN_VALUE) {
            boolean isPortrait = isPortrait();
            if (!isPortrait) {
                typedValue = typedValue2;
            }
            int resolveDimension = resolveDimension(typedValue, z);
            if (resolveDimension > 0) {
                return View.MeasureSpec.makeMeasureSpec(resolveDimension, 1073741824);
            }
            if (!isPortrait) {
                typedValue3 = typedValue4;
            }
            int resolveDimension2 = resolveDimension(typedValue3, z);
            return resolveDimension2 > 0 ? View.MeasureSpec.makeMeasureSpec(Math.min(resolveDimension2, View.MeasureSpec.getSize(i)), Integer.MIN_VALUE) : i;
        }
        return i;
    }

    public final int resolveDimension(TypedValue typedValue, boolean z) {
        int i;
        float fraction;
        if (typedValue != null && (i = typedValue.type) != 0) {
            if (i == 5) {
                fraction = typedValue.getDimension(this.mDisplayMetrics);
            } else if (i == 6) {
                float f = z ? this.mPhysicalSize.x : this.mPhysicalSize.y;
                fraction = typedValue.getFraction(f, f);
            }
            return (int) fraction;
        }
        return 0;
    }

    public final boolean isPortrait() {
        return this.mContext.getApplicationContext().getResources().getConfiguration().orientation == 1;
    }

    public final TypedValue getFixedWidthMinor() {
        if (!this.mFloatingTheme || !this.mFloatingWindow) {
            return null;
        }
        return this.mFixedWidthMinor;
    }

    public final TypedValue getFixedHeightMajor() {
        if (!this.mFloatingTheme || !this.mFloatingWindow) {
            return null;
        }
        return this.mFixedHeightMajor;
    }

    public final TypedValue getFixedWidthMajor() {
        if (!this.mFloatingTheme || !this.mFloatingWindow) {
            return null;
        }
        return this.mFixedWidthMajor;
    }

    public final TypedValue getFixedHeightMinor() {
        if (!this.mFloatingTheme || !this.mFloatingWindow) {
            return null;
        }
        return this.mFixedHeightMinor;
    }

    public final TypedValue getMaxWidthMinor() {
        if (!this.mFloatingTheme || !this.mFloatingWindow) {
            return null;
        }
        return this.mMaxWidthMinor;
    }

    public final TypedValue getMaxWidthMajor() {
        if (!this.mFloatingTheme || !this.mFloatingWindow) {
            return null;
        }
        return this.mMaxWidthMajor;
    }

    public final TypedValue getMaxHeightMinor() {
        if (!this.mFloatingTheme || !this.mFloatingWindow) {
            return null;
        }
        return this.mMaxHeightMinor;
    }

    public final TypedValue getMaxHeightMajor() {
        if (!this.mFloatingTheme || !this.mFloatingWindow) {
            return null;
        }
        return this.mMaxHeightMajor;
    }

    public void onConfigurationChanged() {
        int themeResourceId;
        Context context = this.mContext;
        if (this.mIsInDialogMode && Build.VERSION.SDK_INT >= 23 && (context instanceof ContextThemeWrapper) && (themeResourceId = getThemeResourceId((ContextThemeWrapper) context)) > 0) {
            context = new ContextThemeWrapper(this.mContext.getApplicationContext(), themeResourceId);
        }
        this.mFixedWidthMinor = AttributeResolver.resolveTypedValue(context, R$attr.windowFixedWidthMinor);
        this.mFixedHeightMajor = AttributeResolver.resolveTypedValue(context, R$attr.windowFixedHeightMajor);
        this.mFixedWidthMajor = AttributeResolver.resolveTypedValue(context, R$attr.windowFixedWidthMajor);
        this.mFixedHeightMinor = AttributeResolver.resolveTypedValue(context, R$attr.windowFixedHeightMinor);
        this.mMaxWidthMinor = AttributeResolver.resolveTypedValue(context, R$attr.windowMaxWidthMinor);
        this.mMaxWidthMajor = AttributeResolver.resolveTypedValue(context, R$attr.windowMaxWidthMajor);
        this.mMaxHeightMinor = AttributeResolver.resolveTypedValue(context, R$attr.windowMaxHeightMinor);
        this.mMaxHeightMajor = AttributeResolver.resolveTypedValue(context, R$attr.windowMaxHeightMajor);
        updatePhysicalSize(context);
    }

    public final int getThemeResourceId(ContextThemeWrapper contextThemeWrapper) {
        try {
            return ((Integer) Reflects.invoke(contextThemeWrapper, Reflects.getMethod(contextThemeWrapper.getClass(), "getThemeResId", null), null)).intValue();
        } catch (RuntimeException e) {
            Log.w("FloatingABOLayoutSpec", "catch theme resource get exception", e);
            return 0;
        }
    }
}
