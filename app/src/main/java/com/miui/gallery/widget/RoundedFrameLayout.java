package com.miui.gallery.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

/* loaded from: classes2.dex */
public class RoundedFrameLayout extends FrameLayout {
    public RoundedFrameLayout(Activity activity, int i) {
        this(activity, null, 0, i);
    }

    public RoundedFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundedFrameLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, -1);
    }

    public RoundedFrameLayout(Context context, AttributeSet attributeSet, int i, final int i2) {
        super(context, attributeSet, i);
        i2 = i2 == -1 ? RoundedViewHelper.obtainRoundedCornerRadius(context, attributeSet) : i2;
        setOutlineProvider(new ViewOutlineProvider() { // from class: com.miui.gallery.widget.RoundedFrameLayout.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), i2);
            }
        });
        setClipToOutline(true);
    }
}
