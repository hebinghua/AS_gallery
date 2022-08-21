package com.miui.support.cardview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewOutlineProvider;
import java.lang.reflect.InvocationTargetException;
import miuix.smooth.SmoothContainerDrawable;

/* loaded from: classes3.dex */
public class CardView extends androidx.cardview.widget.CardView {
    private static final String TAG = "MiuiX.CardView";
    private int mStrokeColor;
    private int mStrokeWidth;

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.cardViewStyle);
    }

    @SuppressLint({"CustomViewStyleable"})
    public CardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        int resourceId;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.CardView, i, 0);
        if (Build.VERSION.SDK_INT >= 21 && (resourceId = obtainStyledAttributes.getResourceId(R.styleable.CardView_outlineStyle, -1)) != -1) {
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(resourceId, R.styleable.OutlineProvider);
            String string = obtainStyledAttributes2.getString(R.styleable.OutlineProvider_android_name);
            if (!TextUtils.isEmpty(string)) {
                setOutlineProviderFromAttribute(context, string, resourceId);
            }
            obtainStyledAttributes2.recycle();
        }
        setStrokeWidth(obtainStyledAttributes.getDimensionPixelSize(R.styleable.CardView_miuix_strokeWidth, 0));
        setStrokeColor(obtainStyledAttributes.getColor(R.styleable.CardView_miuix_strokeColor, 0));
        obtainStyledAttributes.recycle();
        updateBackground();
    }

    @Override // androidx.cardview.widget.CardView
    public void setRadius(float f) {
        super.setRadius(f);
        updateBackground();
    }

    private void updateBackground() {
        Drawable originalBackground = getOriginalBackground();
        SmoothContainerDrawable smoothContainerDrawable = new SmoothContainerDrawable();
        smoothContainerDrawable.setChildDrawable(originalBackground);
        smoothContainerDrawable.setCornerRadius(getRadius());
        smoothContainerDrawable.setStrokeWidth(getStrokeWidth());
        smoothContainerDrawable.setStrokeColor(getStrokeColor());
        setBackground(smoothContainerDrawable);
    }

    private Drawable getOriginalBackground() {
        Drawable background = getBackground();
        return background instanceof SmoothContainerDrawable ? ((SmoothContainerDrawable) background).getChildDrawable() : background;
    }

    public void setStrokeWidth(int i) {
        if (this.mStrokeWidth != i) {
            this.mStrokeWidth = i;
            updateBackground();
        }
    }

    public int getStrokeWidth() {
        return this.mStrokeWidth;
    }

    public void setStrokeColor(int i) {
        if (this.mStrokeColor != i) {
            this.mStrokeColor = i;
            updateBackground();
        }
    }

    public int getStrokeColor() {
        return this.mStrokeColor;
    }

    private void setOutlineProviderFromAttribute(Context context, String str, int i) {
        try {
            Class<? extends U> asSubclass = Class.forName(str).asSubclass(ViewOutlineProvider.class);
            try {
                try {
                    setOutlineProvider((ViewOutlineProvider) asSubclass.getConstructor(Context.class, Integer.TYPE).newInstance(context, Integer.valueOf(i)));
                } catch (IllegalAccessException | NoSuchMethodException unused) {
                    setOutlineProvider((ViewOutlineProvider) asSubclass.getConstructor(new Class[0]).newInstance(new Object[0]));
                } catch (InstantiationException e) {
                    e = e;
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e2) {
                    e = e2;
                    throw new RuntimeException(e);
                }
            } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e3) {
                throw new RuntimeException(e3);
            }
        } catch (ClassNotFoundException unused2) {
            throw new NoClassDefFoundError(str);
        }
    }
}
