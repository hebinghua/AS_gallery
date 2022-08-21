package com.miui.gallery.widget.imageview;

import android.content.Context;
import com.miui.gallery.baseui.R$dimen;
import com.miui.gallery.widget.imageview.ScaleGestureDetector;

/* loaded from: classes2.dex */
public class SensitiveScaleGestureDetector extends ScaleGestureDetector {
    public SensitiveScaleGestureDetector(Context context, ScaleGestureDetector.OnScaleGestureListener onScaleGestureListener) {
        super(context, onScaleGestureListener);
        setScaleMinSpan(context.getResources().getDimensionPixelSize(R$dimen.scale_gesture_detector_min_spin));
    }
}
