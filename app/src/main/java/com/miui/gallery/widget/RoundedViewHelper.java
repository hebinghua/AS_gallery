package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public final class RoundedViewHelper {
    public static final int[] ATTRS = {R.attr.outlineCornerRadius};

    public static int obtainRoundedCornerRadius(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ATTRS);
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(0, 0);
            obtainStyledAttributes.recycle();
            return dimensionPixelSize;
        }
        return 0;
    }
}
