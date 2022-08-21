package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

/* loaded from: classes2.dex */
public class CircleAppCompatImageView extends WHRatioImageView {
    public CircleAppCompatImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CircleAppCompatImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setOutlineProvider(new ViewOutlineProvider() { // from class: com.miui.gallery.widget.CircleAppCompatImageView.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                int width = view.getWidth();
                int height = view.getHeight();
                outline.setRoundRect(0, 0, width, height, Math.min(width, height) / 2.0f);
            }
        });
        setClipToOutline(true);
    }
}
