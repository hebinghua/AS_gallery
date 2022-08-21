package com.miui.pickdrag.blur;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import com.miui.pickdrag.R$color;
import kotlin.ranges.RangesKt___RangesKt;
import miuix.appcompat.app.AppCompatActivity;

/* compiled from: TranslucentWindowController.kt */
/* loaded from: classes3.dex */
public final class TranslucentWindowController extends BlurController {
    public boolean isFunctionEnabled = true;
    public boolean isTranslucentOpen;
    public ColorDrawable windowBackground;

    public final TranslucentWindowController setFunctionEnabled(boolean z) {
        this.isFunctionEnabled = z;
        return this;
    }

    @Override // com.miui.pickdrag.blur.BlurController
    public void setBlurEnabled(boolean z) {
        AppCompatActivity activity;
        if (!this.isFunctionEnabled || this.isTranslucentOpen == z || (activity = getActivity()) == null) {
            return;
        }
        ensureWindowBackground(activity);
        ColorDrawable colorDrawable = this.windowBackground;
        if (colorDrawable != null) {
            colorDrawable.setAlpha(z ? 255 : 0);
        }
        activity.getWindow().setBackgroundDrawable(this.windowBackground);
        this.isTranslucentOpen = z;
    }

    @Override // com.miui.pickdrag.blur.BlurController
    public void setBlurAlpha(float f) {
        AppCompatActivity activity;
        if (this.isFunctionEnabled && (activity = getActivity()) != null) {
            ensureWindowBackground(activity);
            int i = (int) (f * 255);
            ColorDrawable colorDrawable = this.windowBackground;
            if (colorDrawable != null) {
                colorDrawable.setAlpha(RangesKt___RangesKt.coerceAtLeast(0, RangesKt___RangesKt.coerceAtMost(255, i)));
            }
            activity.getWindow().setBackgroundDrawable(this.windowBackground);
        }
    }

    public final void ensureWindowBackground(Activity activity) {
        if (this.windowBackground == null) {
            this.windowBackground = new ColorDrawable(activity.getColor(R$color.pa_picker_window_background_color));
        }
    }
}
