package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.miui.gallery.R;
import com.miui.gallery.widget.slip.ISlipAnimView;

/* loaded from: classes2.dex */
public class PhotoChoiceContainer extends FrameLayout implements ISlipAnimView {
    public int mInitPadding;
    public int mSlipPadding;
    public float mSlipPaddingRatio;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public PhotoChoiceContainer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PhotoChoiceContainer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mInitPadding = 0;
        this.mSlipPadding = 0;
        this.mSlipPaddingRatio = 0.0f;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mInitPadding = getPaddingTop();
        this.mSlipPaddingRatio = getResources().getFraction(R.fraction.share_channel_slip_padding_ratio, 1, 1);
    }

    @Override // com.miui.gallery.widget.slip.ISlipAnimView
    public void onSlipping(float f) {
        doSlipAnim(f);
        setAlpha(f);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mSlipPadding == 0 || z) {
            this.mSlipPadding = (int) ((i4 - i2) * this.mSlipPaddingRatio);
        }
    }

    public final void doSlipAnim(float f) {
        int i = this.mSlipPadding;
        if (i > 0) {
            int i2 = this.mInitPadding;
            setPadding(getPaddingLeft(), (int) (i2 + ((i - i2) * f)), getPaddingRight(), getPaddingBottom());
        }
    }
}
