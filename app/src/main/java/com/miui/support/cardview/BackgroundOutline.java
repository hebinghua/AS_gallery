package com.miui.support.cardview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.annotation.Keep;
import miuix.smooth.SmoothPathProvider2;

@Keep
@TargetApi(21)
/* loaded from: classes3.dex */
public class BackgroundOutline extends ViewOutlineProvider {
    private float mAlpha;
    private SmoothPathProvider2 mPathProvider = new SmoothPathProvider2();

    public BackgroundOutline(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(i, R.styleable.BackgroundOutline);
        this.mAlpha = obtainStyledAttributes.getFloat(R.styleable.BackgroundOutline_android_alpha, 1.0f);
        obtainStyledAttributes.recycle();
    }

    private BackgroundOutline(float f) {
        this.mAlpha = f;
    }

    @Override // android.view.ViewOutlineProvider
    public void getOutline(View view, Outline outline) {
        if (view.getWidth() == 0 || view.getHeight() == 0) {
            return;
        }
        view.getBackground().getOutline(outline);
        outline.setAlpha(this.mAlpha);
    }

    public BackgroundOutline of(float f) {
        return new BackgroundOutline(f);
    }
}
