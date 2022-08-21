package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.widget.slip.ISlipAnimView;

/* loaded from: classes2.dex */
public class PhotoChoiceTitle extends ConstraintLayout implements ISlipAnimView {
    public ImageView mExitButton;
    public TextView mSubTitle;
    public TextView mTitle;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public PhotoChoiceTitle(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PhotoChoiceTitle(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitle = (TextView) findViewById(R.id.choice_mode_title);
        this.mExitButton = (ImageView) findViewById(R.id.choice_mode_exit);
        this.mSubTitle = (TextView) findViewById(R.id.choice_mode_subtitle);
    }

    public TextView getTitle() {
        return this.mTitle;
    }

    public TextView getSubTitle() {
        return this.mSubTitle;
    }

    public ImageView getExitButton() {
        return this.mExitButton;
    }

    @Override // com.miui.gallery.widget.slip.ISlipAnimView
    public void onSlipping(float f) {
        doSlipAnim(f);
        setAlpha(f);
    }

    public final void doSlipAnim(float f) {
        ((FrameLayout.LayoutParams) getLayoutParams()).topMargin = (int) ((1.0f - f) * getHeight());
        if (BaseMiscUtil.floatEquals(f, 0.0f)) {
            setVisibility(4);
        } else {
            setVisibility(0);
        }
    }
}
