package miuix.blurdrawable.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/* loaded from: classes3.dex */
public class MiBlurBackgroundView extends FrameLayout {
    public BlurBackgroundView mBackgroundView;

    public MiBlurBackgroundView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        addBlurView(context);
    }

    public boolean setBlurBackground(boolean z) {
        return this.mBackgroundView.setBlurBackground(z);
    }

    public final void addBlurView(Context context) {
        this.mBackgroundView = new BlurBackgroundView(context);
        this.mBackgroundView.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
        addView(this.mBackgroundView, 0);
        setBlurBackground(false);
    }
}
