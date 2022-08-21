package com.miui.gallery.editor.ui.view.switchview;

import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.miui.gallery.editor.R$color;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.editor.R$drawable;
import com.miui.gallery.editor.R$id;
import com.miui.gallery.editor.R$layout;

/* loaded from: classes2.dex */
public class SlideSwitchView2 extends LinearLayout implements View.OnClickListener {
    public RectF mCurSelectedBgRect;
    public float mInterval;
    public TextView mLeftTv;
    public RectF mLongSelectedBgRect;
    public OnSelectChangeListener mOnSelectChangeListener;
    public Paint mPaint;
    public TypeEvaluator<RectF> mRectFTypeEvaluator;
    public TextView mRightTv;
    public int mSelected;
    public float mSelectedBgCorner;
    public float mSelectedBgHeight;
    public float mSelectedBgWidth;
    public RectF mShortSelectedBgRect;
    public SwitchClickableListener mSwitchClickableListener;
    public ValueAnimator mValueAnimator;

    /* loaded from: classes2.dex */
    public interface OnSelectChangeListener {
        void onSelectChanged(int i);
    }

    /* loaded from: classes2.dex */
    public interface SwitchClickableListener {
        boolean canSwitchClick();
    }

    public SlideSwitchView2(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SlideSwitchView2(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mRectFTypeEvaluator = new TypeEvaluator<RectF>() { // from class: com.miui.gallery.editor.ui.view.switchview.SlideSwitchView2.2
            public RectF mRect = new RectF();

            @Override // android.animation.TypeEvaluator
            public RectF evaluate(float f, RectF rectF, RectF rectF2) {
                float f2 = rectF.left;
                float f3 = f2 + ((rectF2.left - f2) * f);
                float f4 = rectF.top;
                float f5 = f4 + ((rectF2.top - f4) * f);
                float f6 = rectF.right;
                float f7 = rectF.bottom;
                this.mRect.set(f3, f5, f6 + ((rectF2.right - f6) * f), f7 + ((rectF2.bottom - f7) * f));
                return this.mRect;
            }
        };
        init(context);
    }

    public final void init(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R$layout.slide_switch_view, (ViewGroup) this, true);
        this.mLeftTv = (TextView) inflate.findViewById(R$id.tv_left);
        this.mRightTv = (TextView) inflate.findViewById(R$id.tv_right);
        this.mLeftTv.setOnClickListener(this);
        this.mRightTv.setOnClickListener(this);
        initParams();
        setOrientation(0);
        setBackground(ContextCompat.getDrawable(context, R$drawable.slide_switch_view_bg));
    }

    public final void initParams() {
        this.mSelectedBgCorner = getResources().getDimension(R$dimen.slide_switch_view_selected_bg_corner);
        this.mSelectedBgWidth = getResources().getDimension(R$dimen.slide_switch_view_selected_bg_width);
        this.mSelectedBgHeight = getResources().getDimension(R$dimen.slide_switch_view_selected_bg_height);
        this.mInterval = getResources().getDimension(R$dimen.slide_switch_view_interval);
        int color = getResources().getColor(R$color.slide_switch_view_selected_bg);
        Paint paint = new Paint(1);
        this.mPaint = paint;
        paint.setColor(color);
        this.mShortSelectedBgRect = new RectF();
        this.mLongSelectedBgRect = new RectF();
        this.mCurSelectedBgRect = new RectF(this.mShortSelectedBgRect);
        this.mSelected = 0;
    }

    public void initTexts(String str, String str2) {
        this.mLeftTv.setText(str);
        this.mRightTv.setText(str2);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (getLayoutDirection() == 0) {
            RectF rectF = this.mShortSelectedBgRect;
            float f = this.mInterval;
            rectF.left = f;
            rectF.top = f;
            rectF.right = this.mSelectedBgWidth + f;
            rectF.bottom = f + this.mSelectedBgHeight;
            RectF rectF2 = this.mLongSelectedBgRect;
            float f2 = this.mInterval;
            rectF2.right = getWidth() - f2;
            RectF rectF3 = this.mLongSelectedBgRect;
            rectF3.left = rectF3.right - this.mSelectedBgWidth;
            rectF3.top = f2;
            rectF3.bottom = f2 + this.mSelectedBgHeight;
        } else {
            RectF rectF4 = this.mShortSelectedBgRect;
            float f3 = this.mInterval;
            rectF4.top = f3;
            rectF4.bottom = f3 + this.mSelectedBgHeight;
            float f4 = this.mInterval;
            rectF4.right = getWidth() - f4;
            RectF rectF5 = this.mShortSelectedBgRect;
            float f5 = rectF5.right;
            float f6 = this.mSelectedBgWidth;
            rectF5.left = f5 - f6;
            RectF rectF6 = this.mLongSelectedBgRect;
            rectF6.left = f4;
            rectF6.top = f4;
            rectF6.right = f6 + f4;
            rectF6.bottom = f4 + this.mSelectedBgHeight;
        }
        setSelected(this.mSelected);
    }

    public RectF getCurSelectedBgRect() {
        return this.mCurSelectedBgRect;
    }

    public void setCurSelectedBgRect(RectF rectF) {
        this.mCurSelectedBgRect = rectF;
    }

    public int getSelected() {
        return this.mSelected;
    }

    public void setSelected(int i) {
        if (i == 0 || i == 1) {
            this.mSelected = i;
            updateViewSelected(i);
            onSelectChangeCallback(this.mSelected);
            updateCurSelectedBgRect(this.mSelected);
        }
    }

    public final void updateCurSelectedBgRect(int i) {
        if (i == 0) {
            copyRect(this.mCurSelectedBgRect, this.mShortSelectedBgRect);
        } else {
            copyRect(this.mCurSelectedBgRect, this.mLongSelectedBgRect);
        }
        invalidate();
    }

    public final void copyRect(RectF rectF, RectF rectF2) {
        rectF.left = rectF2.left;
        rectF.top = rectF2.top;
        rectF.right = rectF2.right;
        rectF.bottom = rectF2.bottom;
    }

    public void changeSelected(int i, int i2) {
        SwitchClickableListener switchClickableListener = this.mSwitchClickableListener;
        if (switchClickableListener == null || switchClickableListener.canSwitchClick()) {
            if (i == 0 && i2 == 1) {
                startAnimation(this.mShortSelectedBgRect, this.mLongSelectedBgRect);
            } else if (i == 1 && i2 == 0) {
                startAnimation(this.mLongSelectedBgRect, this.mShortSelectedBgRect);
            }
            this.mSelected = i2;
            updateViewSelected(i2);
            onSelectChangeCallback(this.mSelected);
        }
    }

    public final void startAnimation(RectF rectF, RectF rectF2) {
        this.mValueAnimator = new ValueAnimator();
        this.mValueAnimator.setValues(PropertyValuesHolder.ofObject("curSelectedBgRect", this.mRectFTypeEvaluator, rectF, rectF2));
        this.mValueAnimator.setDuration(200L);
        this.mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.ui.view.switchview.SlideSwitchView2.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                SlideSwitchView2.this.mCurSelectedBgRect.set((RectF) valueAnimator.getAnimatedValue());
                SlideSwitchView2.this.invalidate();
            }
        });
        this.mValueAnimator.start();
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = this.mCurSelectedBgRect;
        float f = this.mSelectedBgCorner;
        canvas.drawRoundRect(rectF, f, f, this.mPaint);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mLeftTv) {
            if (this.mSelected != 1) {
                return;
            }
            changeSelected(1, 0);
        } else if (view != this.mRightTv || this.mSelected != 0) {
        } else {
            changeSelected(0, 1);
        }
    }

    public final void updateViewSelected(int i) {
        if (i == 0) {
            this.mLeftTv.setSelected(true);
            this.mRightTv.setSelected(false);
        } else if (i != 1) {
        } else {
            this.mRightTv.setSelected(true);
            this.mLeftTv.setSelected(false);
        }
    }

    public final void onSelectChangeCallback(int i) {
        OnSelectChangeListener onSelectChangeListener = this.mOnSelectChangeListener;
        if (onSelectChangeListener != null) {
            onSelectChangeListener.onSelectChanged(i);
        }
    }

    public void setSwitchClickableListener(SwitchClickableListener switchClickableListener) {
        this.mSwitchClickableListener = switchClickableListener;
    }

    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.mOnSelectChangeListener = onSelectChangeListener;
    }
}
