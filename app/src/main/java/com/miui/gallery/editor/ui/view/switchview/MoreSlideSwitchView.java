package com.miui.gallery.editor.ui.view.switchview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.gallery.editor.R$color;
import com.miui.gallery.editor.R$dimen;

/* loaded from: classes2.dex */
public class MoreSlideSwitchView extends LinearLayout implements View.OnClickListener {
    public Point mCurSelectPoint;
    public int mHeight;
    public Point mLastSelectPoint;
    public MoreSlideSwitchConfig mMoreSlideSwitchConfig;
    public int mOldSelectedIndex;
    public OnSelectedListener mOnSelectedListener;
    public Paint mPaint;
    public SparseArray<Point> mPointById;
    public TypeEvaluator<Point> mPointTypeEvaluator;
    public int mSelectedIndex;
    public SparseArray<String> mTitleById;
    public String[] mTitles;
    public ValueAnimator mValueAnimator;
    public SparseArray<TextView> mViewById;
    public int mWidth;

    /* loaded from: classes2.dex */
    public interface OnSelectedListener {
        void onSelected(int i, int i2, String str);
    }

    public MoreSlideSwitchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public MoreSlideSwitchView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mPointTypeEvaluator = new TypeEvaluator<Point>() { // from class: com.miui.gallery.editor.ui.view.switchview.MoreSlideSwitchView.3
            public Point mPoint = new Point();

            @Override // android.animation.TypeEvaluator
            public Point evaluate(float f, Point point, Point point2) {
                int i2 = point.x;
                int i3 = point.y;
                this.mPoint.set((int) (i2 + ((point2.x - i2) * f)), (int) (i3 + ((point2.y - i3) * f)));
                return this.mPoint;
            }
        };
        setOrientation(0);
        this.mPaint = new Paint(1);
    }

    public void initParams(MoreSlideSwitchConfig moreSlideSwitchConfig) {
        if (moreSlideSwitchConfig == null) {
            throw new IllegalArgumentException("view config is empty!");
        }
        this.mMoreSlideSwitchConfig = moreSlideSwitchConfig;
        String[] titles = moreSlideSwitchConfig.getTitles();
        this.mTitles = titles;
        if (titles == null || titles.length < 1) {
            throw new IllegalArgumentException("titles is empty!");
        }
        if (this.mMoreSlideSwitchConfig.getSelectedWidth() == 0) {
            this.mMoreSlideSwitchConfig.setSelectedWidth(getResources().getDimensionPixelSize(R$dimen.more_slide_switch_view_selected_width));
        }
        if (this.mMoreSlideSwitchConfig.getSelectedHeight() == 0) {
            this.mMoreSlideSwitchConfig.setSelectedHeight(getResources().getDimensionPixelSize(R$dimen.more_slide_switch_view_selected_height));
        }
        if (this.mMoreSlideSwitchConfig.getSelectedColor() == 0) {
            this.mMoreSlideSwitchConfig.setSelectedColor(getResources().getColor(R$color.more_slide_switch_view_selected_color));
        }
        if (this.mMoreSlideSwitchConfig.getSelectedXRadius() == 0) {
            this.mMoreSlideSwitchConfig.setSelectedXRadius(getResources().getDimensionPixelSize(R$dimen.more_slide_switch_view_x_radius));
        }
        if (this.mMoreSlideSwitchConfig.getSelectedYRadius() == 0) {
            this.mMoreSlideSwitchConfig.setSelectedYRadius(getResources().getDimensionPixelSize(R$dimen.more_slide_switch_view_y_radius));
        }
        if (this.mMoreSlideSwitchConfig.getSelectedTextColor() == 0) {
            this.mMoreSlideSwitchConfig.setSelectedTextColor(getResources().getColor(R$color.more_slide_switch_view_selected_text_color));
        }
        if (this.mMoreSlideSwitchConfig.getNormalTextColor() == 0) {
            this.mMoreSlideSwitchConfig.setNormalTextColor(getResources().getColor(R$color.more_slide_switch_view_normal_text_color));
        }
        if (this.mMoreSlideSwitchConfig.getTextSize() == 0) {
            this.mMoreSlideSwitchConfig.setTextSize(getResources().getDimensionPixelSize(R$dimen.more_slide_switch_view_text_size));
        }
        this.mPaint.setColor(this.mMoreSlideSwitchConfig.getSelectedColor());
        this.mTitleById = new SparseArray<>();
        this.mViewById = new SparseArray<>();
        this.mPointById = new SparseArray<>();
        int i = 0;
        while (true) {
            String[] strArr = this.mTitles;
            if (i < strArr.length) {
                this.mTitleById.put(i, strArr[i]);
                generateItem(i, this.mTitles[i]);
                i++;
            } else {
                requestLayout();
                return;
            }
        }
    }

    public final void generateItem(int i, String str) {
        TextView textView = new TextView(getContext());
        textView.setTextColor(-16776961);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -1);
        layoutParams.weight = 1.0f;
        textView.setGravity(17);
        textView.setText(str);
        textView.setTextColor(i == this.mSelectedIndex ? this.mMoreSlideSwitchConfig.getSelectedTextColor() : this.mMoreSlideSwitchConfig.getNormalTextColor());
        textView.setTypeface(Typeface.create("mipro-medium", 0));
        textView.setId(i);
        textView.setOnClickListener(this);
        textView.setTextSize(0, this.mMoreSlideSwitchConfig.getTextSize());
        this.mViewById.put(i, textView);
        addView(textView, layoutParams);
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }

    public final void calculateItemPoint() {
        String[] strArr;
        if (this.mWidth == 0 || (strArr = this.mTitles) == null || strArr.length < 1) {
            return;
        }
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int i = (this.mWidth - paddingLeft) - paddingRight;
        int i2 = (this.mHeight - paddingTop) - paddingBottom;
        int length = i / this.mTitles.length;
        for (int i3 = 0; i3 < this.mTitles.length; i3++) {
            this.mPointById.put(i3, new Point((i3 * length) + paddingLeft + (length >> 1), (i2 >> 1) + paddingTop));
        }
        Point point = this.mPointById.get(this.mSelectedIndex);
        this.mLastSelectPoint = point;
        this.mCurSelectPoint = point;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mWidth == getWidth() && this.mHeight == getHeight()) {
            return;
        }
        this.mWidth = getWidth();
        this.mHeight = getHeight();
        calculateItemPoint();
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Point point = this.mCurSelectPoint;
        if (point == null) {
            return;
        }
        int i = point.x;
        int i2 = point.y;
        int selectedWidth = this.mMoreSlideSwitchConfig.getSelectedWidth() >> 1;
        int selectedHeight = this.mMoreSlideSwitchConfig.getSelectedHeight() >> 1;
        canvas.drawRoundRect(i - selectedWidth, i2 - selectedHeight, i + selectedWidth, i2 + selectedHeight, this.mMoreSlideSwitchConfig.getSelectedXRadius(), this.mMoreSlideSwitchConfig.getSelectedYRadius(), this.mPaint);
    }

    public final void doSelectedAnimator(Point point, Point point2) {
        if (this.mValueAnimator == null) {
            ValueAnimator valueAnimator = new ValueAnimator();
            this.mValueAnimator = valueAnimator;
            valueAnimator.setDuration(300L);
            this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.ui.view.switchview.MoreSlideSwitchView.1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    MoreSlideSwitchView.this.mCurSelectPoint = (Point) valueAnimator2.getAnimatedValue();
                    MoreSlideSwitchView.this.invalidate();
                }
            });
            this.mValueAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.ui.view.switchview.MoreSlideSwitchView.2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    super.onAnimationStart(animator);
                    MoreSlideSwitchView.this.updateSelectedTextColor();
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    MoreSlideSwitchView moreSlideSwitchView = MoreSlideSwitchView.this;
                    moreSlideSwitchView.mCurSelectPoint = moreSlideSwitchView.mLastSelectPoint;
                    MoreSlideSwitchView.this.invalidate();
                }
            });
        }
        if (this.mValueAnimator.isRunning()) {
            this.mValueAnimator.cancel();
        }
        this.mValueAnimator.setValues(PropertyValuesHolder.ofObject("curSelectPoint", this.mPointTypeEvaluator, point, point2));
        this.mValueAnimator.start();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.mOldSelectedIndex = this.mSelectedIndex;
        int id = view.getId();
        this.mSelectedIndex = id;
        OnSelectedListener onSelectedListener = this.mOnSelectedListener;
        if (onSelectedListener != null) {
            onSelectedListener.onSelected(this.mOldSelectedIndex, id, this.mTitleById.valueAt(id));
        }
        int i = this.mOldSelectedIndex;
        if (i != this.mSelectedIndex) {
            Point point = this.mPointById.get(i);
            this.mLastSelectPoint = point;
            doSelectedAnimator(point, this.mPointById.get(this.mSelectedIndex));
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ValueAnimator valueAnimator = this.mValueAnimator;
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                this.mValueAnimator.cancel();
            }
            this.mValueAnimator = null;
        }
    }

    public void setCurSelected(int i) {
        setCurSelected(i, false);
    }

    public void setCurSelected(int i, boolean z) {
        int i2 = this.mSelectedIndex;
        if (i != i2) {
            this.mOldSelectedIndex = i2;
            this.mSelectedIndex = i;
            Point point = this.mPointById.get(i2);
            this.mLastSelectPoint = point;
            if (z) {
                doSelectedAnimator(point, this.mPointById.get(this.mSelectedIndex));
                return;
            }
            updateSelectedTextColor();
            this.mCurSelectPoint = this.mPointById.get(this.mSelectedIndex);
            invalidate();
        }
    }

    public final void updateSelectedTextColor() {
        this.mViewById.get(this.mOldSelectedIndex).setTextColor(this.mMoreSlideSwitchConfig.getNormalTextColor());
        this.mViewById.get(this.mSelectedIndex).setTextColor(this.mMoreSlideSwitchConfig.getSelectedTextColor());
    }

    public int getSelectedIndex() {
        return this.mSelectedIndex;
    }

    public void setEnableViewByIndex(int i, boolean z) {
        TextView textView = this.mViewById.get(i);
        textView.setEnabled(z);
        if (!z) {
            textView.setTextColor(getResources().getColor(R$color.editor_text_color_disable));
        } else {
            textView.setTextColor(i == this.mSelectedIndex ? this.mMoreSlideSwitchConfig.getSelectedTextColor() : this.mMoreSlideSwitchConfig.getNormalTextColor());
        }
        invalidate();
    }
}
