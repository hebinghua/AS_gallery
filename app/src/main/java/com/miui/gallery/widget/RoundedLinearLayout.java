package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;

/* loaded from: classes2.dex */
public class RoundedLinearLayout extends LinearLayout {
    public RoundedLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundedLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        final int obtainRoundedCornerRadius = RoundedViewHelper.obtainRoundedCornerRadius(context, attributeSet);
        setOutlineProvider(new ViewOutlineProvider() { // from class: com.miui.gallery.widget.RoundedLinearLayout.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), obtainRoundedCornerRadius);
            }
        });
        setClipToOutline(true);
    }
}
