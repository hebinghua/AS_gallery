package com.miui.gallery.widget.tablayout;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.gallery.baseui.R$color;
import com.miui.gallery.baseui.R$id;
import com.miui.gallery.baseui.R$layout;
import com.miui.gallery.baseui.R$styleable;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes3.dex */
public class SegmentTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener {
    public int mBarColor;
    public int mBarStrokeColor;
    public float mBarStrokeWidth;
    public Context mContext;
    public IndicatorPoint mCurrentP;
    public int mCurrentTab;
    public long mIndicatorAnimDuration;
    public boolean mIndicatorAnimEnable;
    public boolean mIndicatorBounceEnable;
    public int mIndicatorColor;
    public float mIndicatorCornerRadius;
    public GradientDrawable mIndicatorDrawable;
    public float mIndicatorHeight;
    public float mIndicatorMarginBottom;
    public float mIndicatorMarginLeft;
    public float mIndicatorMarginRight;
    public float mIndicatorMarginTop;
    public Rect mIndicatorRect;
    public OvershootInterpolator mInterpolator;
    public boolean mIsFirstDraw;
    public IndicatorPoint mLastP;
    public int mLastTab;
    public OnTabSelectListener mListener;
    public GradientDrawable mRectDrawable;
    public int mTabCount;
    public float mTabPadding;
    public boolean mTabSpaceEqual;
    public float mTabWidth;
    public LinearLayout mTabsContainer;
    public int mTextSelectColor;
    public int mTextUnableColor;
    public int mTextUnselectColor;
    public float mTextsize;
    public String[] mTitles;
    public Set<Integer> mUnableSet;
    public ValueAnimator mValueAnimator;

    /* loaded from: classes3.dex */
    public interface OnTabSelectListener {
        void onTabReselect(int i);

        void onTabSelect(int i);
    }

    public SegmentTabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SegmentTabLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIndicatorRect = new Rect();
        this.mIndicatorDrawable = new GradientDrawable();
        this.mRectDrawable = new GradientDrawable();
        this.mIsFirstDraw = true;
        this.mInterpolator = new OvershootInterpolator(0.8f);
        this.mUnableSet = new HashSet();
        this.mCurrentP = new IndicatorPoint();
        this.mLastP = new IndicatorPoint();
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);
        this.mContext = context;
        LinearLayout linearLayout = new LinearLayout(context);
        this.mTabsContainer = linearLayout;
        addView(linearLayout);
        obtainAttributes(context, attributeSet);
        ValueAnimator ofObject = ValueAnimator.ofObject(new PointEvaluator(), this.mLastP, this.mCurrentP);
        this.mValueAnimator = ofObject;
        ofObject.addUpdateListener(this);
    }

    public final void obtainAttributes(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SegmentTabLayout);
        int i = R$styleable.SegmentTabLayout_tl_indicator_color;
        Resources resources = getResources();
        int i2 = R$color.black;
        this.mIndicatorColor = obtainStyledAttributes.getColor(i, resources.getColor(i2));
        this.mIndicatorHeight = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_indicator_height, -1.0f);
        this.mIndicatorCornerRadius = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_indicator_corner_radius, -1.0f);
        this.mIndicatorMarginLeft = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_indicator_margin_left, dp2px(0.0f));
        this.mIndicatorMarginTop = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_indicator_margin_top, 0.0f);
        this.mIndicatorMarginRight = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_indicator_margin_right, dp2px(0.0f));
        this.mIndicatorMarginBottom = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_indicator_margin_bottom, 0.0f);
        this.mIndicatorAnimEnable = obtainStyledAttributes.getBoolean(R$styleable.SegmentTabLayout_tl_indicator_anim_enable, false);
        this.mIndicatorBounceEnable = obtainStyledAttributes.getBoolean(R$styleable.SegmentTabLayout_tl_indicator_bounce_enable, true);
        this.mIndicatorAnimDuration = obtainStyledAttributes.getInt(R$styleable.SegmentTabLayout_tl_indicator_anim_duration, -1);
        this.mTextsize = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_textsize, sp2px(13.0f));
        this.mTextSelectColor = obtainStyledAttributes.getColor(R$styleable.SegmentTabLayout_tl_textSelectColor, getResources().getColor(R$color.white));
        this.mTextUnselectColor = obtainStyledAttributes.getColor(R$styleable.SegmentTabLayout_tl_textUnselectColor, this.mIndicatorColor);
        this.mTextUnableColor = obtainStyledAttributes.getColor(R$styleable.SegmentTabLayout_tl_textUnableColor, getResources().getColor(i2));
        this.mTabSpaceEqual = obtainStyledAttributes.getBoolean(R$styleable.SegmentTabLayout_tl_tab_space_equal, true);
        float dimension = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_tab_width, dp2px(-1.0f));
        this.mTabWidth = dimension;
        this.mTabPadding = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_tab_padding, (this.mTabSpaceEqual || dimension > 0.0f) ? dp2px(0.0f) : dp2px(10.0f));
        this.mBarColor = obtainStyledAttributes.getColor(R$styleable.SegmentTabLayout_tl_bar_color, 0);
        this.mBarStrokeColor = obtainStyledAttributes.getColor(R$styleable.SegmentTabLayout_tl_bar_stroke_color, this.mIndicatorColor);
        this.mBarStrokeWidth = obtainStyledAttributes.getDimension(R$styleable.SegmentTabLayout_tl_bar_stroke_width, dp2px(1.0f));
        obtainStyledAttributes.recycle();
    }

    public void setTabData(String[] strArr) {
        if (strArr == null || strArr.length == 0) {
            throw new IllegalStateException("Titles can not be NULL or EMPTY !");
        }
        this.mTitles = strArr;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        this.mTabsContainer.removeAllViews();
        this.mTabCount = this.mTitles.length;
        for (int i = 0; i < this.mTabCount; i++) {
            View inflate = View.inflate(this.mContext, R$layout.layout_tab_segment, null);
            inflate.setTag(Integer.valueOf(i));
            addTab(i, inflate);
        }
        updateTabStyles();
        this.mIsFirstDraw = true;
    }

    public final void addTab(int i, View view) {
        LinearLayout.LayoutParams layoutParams;
        ((TextView) view.findViewById(R$id.tv_tab_title)).setText(this.mTitles[i]);
        if (!this.mUnableSet.contains(Integer.valueOf(i))) {
            view.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.widget.tablayout.SegmentTabLayout.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    int intValue = ((Integer) view2.getTag()).intValue();
                    if (SegmentTabLayout.this.mCurrentTab == intValue) {
                        if (SegmentTabLayout.this.mListener == null) {
                            return;
                        }
                        SegmentTabLayout.this.mListener.onTabReselect(intValue);
                        return;
                    }
                    SegmentTabLayout.this.setCurrentTab(intValue);
                    if (SegmentTabLayout.this.mListener == null) {
                        return;
                    }
                    SegmentTabLayout.this.mListener.onTabSelect(intValue);
                }
            });
        }
        if (this.mTabSpaceEqual) {
            layoutParams = new LinearLayout.LayoutParams(0, -1, 1.0f);
        } else {
            layoutParams = new LinearLayout.LayoutParams(-2, -1);
        }
        if (this.mTabWidth > 0.0f) {
            layoutParams = new LinearLayout.LayoutParams((int) this.mTabWidth, -1);
        }
        this.mTabsContainer.addView(view, i, layoutParams);
    }

    public final void updateTabStyles() {
        int i = 0;
        while (i < this.mTabCount) {
            View childAt = this.mTabsContainer.getChildAt(i);
            float f = this.mTabPadding;
            childAt.setPadding((int) f, 0, (int) f, 0);
            TextView textView = (TextView) childAt.findViewById(R$id.tv_tab_title);
            textView.setTextColor(i == this.mCurrentTab ? this.mTextSelectColor : this.mTextUnselectColor);
            textView.setTextSize(0, this.mTextsize);
            if (this.mUnableSet.contains(Integer.valueOf(i))) {
                textView.setTextColor(this.mTextUnableColor);
            }
            i++;
        }
    }

    public final void updateTabSelection(int i) {
        int i2 = 0;
        while (i2 < this.mTabCount) {
            View childAt = this.mTabsContainer.getChildAt(i2);
            boolean z = i2 == i;
            TextView textView = (TextView) childAt.findViewById(R$id.tv_tab_title);
            textView.setTextColor(z ? this.mTextSelectColor : this.mTextUnselectColor);
            if (this.mUnableSet.contains(Integer.valueOf(i2))) {
                textView.setTextColor(this.mTextUnableColor);
            }
            i2++;
        }
    }

    public final void calcOffset() {
        View childAt = this.mTabsContainer.getChildAt(this.mCurrentTab);
        this.mCurrentP.left = childAt.getLeft();
        this.mCurrentP.right = childAt.getRight();
        View childAt2 = this.mTabsContainer.getChildAt(this.mLastTab);
        this.mLastP.left = childAt2.getLeft();
        this.mLastP.right = childAt2.getRight();
        IndicatorPoint indicatorPoint = this.mLastP;
        float f = indicatorPoint.left;
        IndicatorPoint indicatorPoint2 = this.mCurrentP;
        if (f == indicatorPoint2.left && indicatorPoint.right == indicatorPoint2.right) {
            invalidate();
            return;
        }
        this.mValueAnimator.setObjectValues(indicatorPoint, indicatorPoint2);
        if (this.mIndicatorBounceEnable) {
            this.mValueAnimator.setInterpolator(this.mInterpolator);
        }
        if (this.mIndicatorAnimDuration < 0) {
            this.mIndicatorAnimDuration = this.mIndicatorBounceEnable ? 500L : 250L;
        }
        this.mValueAnimator.setDuration(this.mIndicatorAnimDuration);
        this.mValueAnimator.start();
    }

    public final void calcIndicatorRect() {
        View childAt = this.mTabsContainer.getChildAt(this.mCurrentTab);
        Rect rect = this.mIndicatorRect;
        rect.left = childAt.getLeft();
        rect.right = childAt.getRight();
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        IndicatorPoint indicatorPoint = (IndicatorPoint) valueAnimator.getAnimatedValue();
        Rect rect = this.mIndicatorRect;
        rect.left = (int) indicatorPoint.left;
        rect.right = (int) indicatorPoint.right;
        invalidate();
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || this.mTabCount <= 0) {
            return;
        }
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        if (this.mIndicatorHeight < 0.0f) {
            this.mIndicatorHeight = (height - this.mIndicatorMarginTop) - this.mIndicatorMarginBottom;
        }
        float f = this.mIndicatorCornerRadius;
        if (f < 0.0f || f > this.mIndicatorHeight / 2.0f) {
            this.mIndicatorCornerRadius = this.mIndicatorHeight / 2.0f;
        }
        this.mRectDrawable.setColor(this.mBarColor);
        this.mRectDrawable.setStroke((int) this.mBarStrokeWidth, this.mBarStrokeColor);
        this.mRectDrawable.setCornerRadius(this.mIndicatorCornerRadius);
        this.mRectDrawable.setBounds(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        this.mRectDrawable.draw(canvas);
        if (this.mIndicatorAnimEnable) {
            if (this.mIsFirstDraw) {
                this.mIsFirstDraw = false;
                calcIndicatorRect();
            }
        } else {
            calcIndicatorRect();
        }
        this.mIndicatorDrawable.setColor(this.mIndicatorColor);
        GradientDrawable gradientDrawable = this.mIndicatorDrawable;
        Rect rect = this.mIndicatorRect;
        int i = ((int) this.mIndicatorMarginLeft) + paddingLeft + rect.left;
        float f2 = this.mIndicatorMarginTop;
        gradientDrawable.setBounds(i, (int) f2, (int) ((paddingLeft + rect.right) - this.mIndicatorMarginRight), (int) (f2 + this.mIndicatorHeight));
        this.mIndicatorDrawable.setCornerRadius(this.mIndicatorCornerRadius);
        this.mIndicatorDrawable.draw(canvas);
    }

    public void setCurrentTab(int i) {
        this.mLastTab = this.mCurrentTab;
        this.mCurrentTab = i;
        updateTabSelection(i);
        if (this.mIndicatorAnimEnable) {
            calcOffset();
        } else {
            invalidate();
        }
    }

    public void setOnTabSelectListener(OnTabSelectListener onTabSelectListener) {
        this.mListener = onTabSelectListener;
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", this.mCurrentTab);
        return bundle;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            this.mCurrentTab = bundle.getInt("mCurrentTab");
            parcelable = bundle.getParcelable("instanceState");
            if (this.mCurrentTab != 0 && this.mTabsContainer.getChildCount() > 0) {
                updateTabSelection(this.mCurrentTab);
            }
        }
        super.onRestoreInstanceState(parcelable);
    }

    /* loaded from: classes3.dex */
    public class IndicatorPoint {
        public float left;
        public float right;

        public IndicatorPoint() {
        }
    }

    /* loaded from: classes3.dex */
    public class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
        public PointEvaluator() {
        }

        @Override // android.animation.TypeEvaluator
        public IndicatorPoint evaluate(float f, IndicatorPoint indicatorPoint, IndicatorPoint indicatorPoint2) {
            float f2 = indicatorPoint.left;
            float f3 = indicatorPoint.right;
            float f4 = f3 + (f * (indicatorPoint2.right - f3));
            IndicatorPoint indicatorPoint3 = new IndicatorPoint();
            indicatorPoint3.left = f2 + ((indicatorPoint2.left - f2) * f);
            indicatorPoint3.right = f4;
            return indicatorPoint3;
        }
    }

    public int dp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public int sp2px(float f) {
        return (int) ((f * this.mContext.getResources().getDisplayMetrics().scaledDensity) + 0.5f);
    }
}
