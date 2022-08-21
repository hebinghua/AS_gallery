package miuix.appcompat.internal.app.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import miuix.appcompat.R$color;
import miuix.internal.util.ViewUtils;

/* loaded from: classes3.dex */
public class ScrollingTabTextView extends TextView {
    public ValueAnimator mAnimator;
    public int mClipPosition;
    public boolean mLeftToRight;
    public int mNormalColor;
    public ColorStateList mOriginColor;
    public int mSelectedColor;

    public ScrollingTabTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setupColors();
    }

    public final void setupColors() {
        ColorStateList textColors = getTextColors();
        this.mOriginColor = textColors;
        this.mNormalColor = textColors.getColorForState(TextView.ENABLED_STATE_SET, getResources().getColor(R$color.miuix_appcompat_action_bar_tab_text_color_normal_light));
        this.mSelectedColor = this.mOriginColor.getColorForState(TextView.ENABLED_SELECTED_STATE_SET, getResources().getColor(R$color.miuix_appcompat_action_bar_tab_text_color_selected_light));
    }

    @Override // android.widget.TextView
    public void setTextColor(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
        setupColors();
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        int i;
        int i2;
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            super.onDraw(canvas);
            return;
        }
        if ((this.mLeftToRight && !isSelected()) || (!this.mLeftToRight && isSelected())) {
            i = this.mNormalColor;
        } else {
            i = this.mSelectedColor;
        }
        setTextColor(i);
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i3 = this.mClipPosition;
        int height = getHeight();
        if (isLayoutRtl) {
            i2 = getScrollX() + 0;
            i3 += getScrollX();
        } else {
            i2 = 0;
        }
        canvas.save();
        canvas.clipRect(i2, 0, i3, height);
        super.onDraw(canvas);
        canvas.restore();
        int i4 = this.mNormalColor;
        if (i == i4) {
            i = this.mSelectedColor;
        } else if (i == this.mSelectedColor) {
            i = i4;
        }
        setTextColor(i);
        int i5 = this.mClipPosition;
        int width = getWidth();
        if (isLayoutRtl) {
            i5 += getScrollX();
            width += getScrollX();
        }
        canvas.save();
        canvas.clipRect(i5, 0, width, height);
        super.onDraw(canvas);
        canvas.restore();
        setTextColor(this.mOriginColor);
    }

    public void startScrollAnimation(boolean z) {
        int width;
        int i;
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator == null) {
            this.mAnimator = new ValueAnimator();
        } else {
            valueAnimator.cancel();
        }
        this.mLeftToRight = z;
        if (z) {
            i = getWidth();
            width = 0;
        } else {
            width = getWidth();
            i = 0;
        }
        this.mAnimator.setIntValues(width, i);
        this.mAnimator.setDuration(200L);
        this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: miuix.appcompat.internal.app.widget.ScrollingTabTextView.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                ScrollingTabTextView.this.mClipPosition = ((Integer) valueAnimator2.getAnimatedValue()).intValue();
                ScrollingTabTextView.this.invalidate();
            }
        });
        this.mAnimator.addListener(new AnimatorListenerAdapter() { // from class: miuix.appcompat.internal.app.widget.ScrollingTabTextView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScrollingTabTextView scrollingTabTextView = ScrollingTabTextView.this;
                scrollingTabTextView.mClipPosition = scrollingTabTextView.getWidth();
            }
        });
        this.mAnimator.start();
    }
}
