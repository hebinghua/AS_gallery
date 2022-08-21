package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.widget.EndTopCornerTextView;
import miuix.view.animation.CubicEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class SpaceRingProgressBar extends FrameLayout {
    public TextView mDescriptionText;
    public TextView mDetailDescriptionText;
    public ImageView mEmptyValueView;
    public ObjectAnimator mNumberAnimator;
    public ObjectAnimator mRingAlphaAnimator;
    public ImageView mRingIcon;
    public int mSpaceNumber;
    public EndTopCornerTextView mSpaceNumberView;

    public SpaceRingProgressBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SpaceRingProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        View inflate = LayoutInflater.from(context).inflate(R.layout.space_ring_progress_bar, this);
        this.mDescriptionText = (TextView) inflate.findViewById(R.id.value_description);
        this.mDetailDescriptionText = (TextView) inflate.findViewById(R.id.value_detail_description);
        this.mSpaceNumberView = (EndTopCornerTextView) inflate.findViewById(R.id.used_value);
        this.mRingIcon = (ImageView) inflate.findViewById(R.id.ring_icon);
        this.mEmptyValueView = (ImageView) inflate.findViewById(R.id.value_empty_view);
    }

    public final void changeNumberWithAnim(int i, AnimatorListenerAdapter animatorListenerAdapter) {
        cancelAnimIfRunning(this.mNumberAnimator);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "spaceNumber", getSpaceNumber(), i);
        this.mNumberAnimator = ofInt;
        ofInt.setDuration(getChangeDuration(i));
        this.mNumberAnimator.setInterpolator(new LinearInterpolator());
        if (animatorListenerAdapter != null) {
            this.mNumberAnimator.addListener(animatorListenerAdapter);
        }
        this.mNumberAnimator.start();
    }

    public int getChangeDuration(int i) {
        return Math.abs(i - getSpaceNumber()) < 500000 ? 2000 : 3000;
    }

    public final void changeNumberEndStageAnim(int i, AnimatorListenerAdapter animatorListenerAdapter) {
        cancelAnimIfRunning(this.mNumberAnimator);
        ObjectAnimator ofInt = ObjectAnimator.ofInt(this, "spaceNumber", getSpaceNumber(), i);
        this.mNumberAnimator = ofInt;
        ofInt.setDuration(1000L);
        this.mNumberAnimator.setInterpolator(new DecelerateInterpolator());
        if (animatorListenerAdapter != null) {
            this.mNumberAnimator.addListener(animatorListenerAdapter);
        }
        this.mNumberAnimator.start();
    }

    public final void disappearRingIconAnim() {
        cancelAnimIfRunning(this.mRingAlphaAnimator);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mRingIcon, "alpha", 1.0f, 0.0f);
        this.mRingAlphaAnimator = ofFloat;
        ofFloat.setDuration(500L);
        this.mRingAlphaAnimator.setInterpolator(new CubicEaseInOutInterpolator());
        this.mRingAlphaAnimator.start();
    }

    public final void appearRingIconAnim() {
        cancelAnimIfRunning(this.mRingAlphaAnimator);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this.mRingIcon, "alpha", 0.0f, 1.0f);
        this.mRingAlphaAnimator = ofFloat;
        ofFloat.setDuration(500L);
        this.mRingAlphaAnimator.setInterpolator(new CubicEaseInOutInterpolator());
        this.mRingAlphaAnimator.start();
    }

    public void cancelAllAnim() {
        cancelAnimIfRunning(this.mNumberAnimator);
        cancelAnimIfRunning(this.mRingAlphaAnimator);
    }

    public void cancelAnimIfRunning(Animator animator) {
        if (animator != null) {
            animator.removeAllListeners();
            if (!animator.isRunning()) {
                return;
            }
            animator.cancel();
        }
    }

    public final void changeNumberWithNoAnim(int i) {
        setSpaceNumber(i);
    }

    public void setDescription(String str) {
        this.mDescriptionText.setText(str);
    }

    public void setDetailDescription(String str) {
        if (TextUtils.isEmpty(str)) {
            this.mDetailDescriptionText.setVisibility(8);
            this.mDescriptionText.setTextSize(0, getContext().getResources().getDimensionPixelSize(R.dimen.rotate_ring_description_text_size));
            return;
        }
        this.mDetailDescriptionText.setVisibility(0);
        this.mDetailDescriptionText.setText(str);
        this.mDetailDescriptionText.setTextSize(0, getContext().getResources().getDimensionPixelSize(R.dimen.rotate_ring_detail_description_text_size));
        this.mDescriptionText.setTextSize(0, getContext().getResources().getDimensionPixelSize(R.dimen.rotate_ring_description_scanned_text_size));
    }

    public void setSpaceNumber(int i) {
        String format;
        this.mSpaceNumber = i;
        if (i != 0 && i < 100) {
            i = 100;
        }
        Object[] objArr = new Object[1];
        if (i > 0) {
            objArr[0] = Float.valueOf((i - (i % 100)) / 1000.0f);
            format = String.format("%.1f", objArr);
        } else {
            objArr[0] = Integer.valueOf(i);
            format = String.format("%d", objArr);
        }
        if (i > 900 && (i = i / 1000) > 0) {
            format = String.format("%d", Integer.valueOf(i));
        }
        int i2 = R.string.megabyteShort;
        if (i > 900) {
            format = String.format("%.1f", Float.valueOf(i / 1000.0f));
            i2 = R.string.gigabyteShort;
        }
        this.mSpaceNumberView.setText(format);
        this.mSpaceNumberView.setCornerText(getResources().getString(i2));
        this.mSpaceNumberView.setContentDescription(String.format("%s%s%s", this.mDescriptionText.getText(), format, getResources().getString(i2)));
    }

    public int getSpaceNumber() {
        return this.mSpaceNumber;
    }

    public boolean isCalculating() {
        ObjectAnimator objectAnimator = this.mNumberAnimator;
        return objectAnimator != null && objectAnimator.isRunning();
    }
}
