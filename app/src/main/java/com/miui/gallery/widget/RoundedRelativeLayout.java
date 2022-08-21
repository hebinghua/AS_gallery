package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;

/* loaded from: classes2.dex */
public class RoundedRelativeLayout extends RelativeLayout {
    public RoundedRelativeLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public RoundedRelativeLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        final int obtainRoundedCornerRadius = RoundedViewHelper.obtainRoundedCornerRadius(context, attributeSet);
        setOutlineProvider(new ViewOutlineProvider() { // from class: com.miui.gallery.widget.RoundedRelativeLayout.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), obtainRoundedCornerRadius);
            }
        });
        setClipToOutline(true);
    }
}
