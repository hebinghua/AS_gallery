package com.miui.pickdrag.anim;

import android.content.Context;
import android.util.Log;
import android.view.View;
import com.miui.pickdrag.blur.BlurController;
import com.miui.pickdrag.utils.Device;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;

/* compiled from: BlurAnimationController.kt */
/* loaded from: classes3.dex */
public final class BlurAnimationController {
    public BlurController blurController;
    public Context context;
    public View mContentView;
    public int mScreenHeight;

    public BlurAnimationController(Context context, BlurController blurController, View mContentView) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(blurController, "blurController");
        Intrinsics.checkNotNullParameter(mContentView, "mContentView");
        this.context = context;
        this.blurController = blurController;
        this.mContentView = mContentView;
        this.mScreenHeight = Device.getRealDisplayHeight(context);
    }

    public final void checkAndInvalidateBlurAlpha(boolean z, int i) {
        if (!z) {
            return;
        }
        if (i <= 0) {
            updateBlurAlpha(1.0f);
        } else if (i >= this.mScreenHeight) {
            updateBlurAlpha(0.0f);
        } else {
            float calculateSlideFraction = calculateSlideFraction(i);
            if (calculateSlideFraction >= 0.0f) {
                updateBlurAlpha(1 - calculateSlideFraction);
            }
        }
        Log.d("BlurAnimationController", Intrinsics.stringPlus("offsetY = ", Integer.valueOf(i)));
    }

    public final void updateBlurAlpha(float f) {
        BlurController blurController = this.blurController;
        if (blurController == null) {
            return;
        }
        blurController.setBlurAlpha(f);
    }

    public final float calculateSlideFraction(int i) {
        int i2 = this.mScreenHeight;
        if (i2 == 0) {
            return -1.0f;
        }
        return RangesKt___RangesKt.coerceAtMost(i / i2, 1.0f);
    }
}
