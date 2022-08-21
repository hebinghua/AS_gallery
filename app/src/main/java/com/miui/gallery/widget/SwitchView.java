package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;

/* loaded from: classes2.dex */
public class SwitchView extends ConstraintLayout {
    public TextView mBackgroundTabView;
    public Context mContext;
    public Drawable mDotBackground;
    public int mDotHeight;
    public int mDotPadding;
    public int mDotStartMargin;
    public int mDotTextColor;
    public int mDotTextSize;
    public int mDotTopMargin;
    public TextView mDotView;
    public int mDotWidth;
    public boolean mIsSwitchable;
    public OnSwitchChangeListener mOnSwitchChangeListener;
    public OnSwitchDoubleClickListener mOnSwitchDoubleClickListener;
    public int mSelectedId;
    public List<Integer> mTabViewChildIds;
    public int mTextColor;
    public int mTextSize;

    /* loaded from: classes2.dex */
    public interface OnSwitchChangeListener {
        void onSwitchChange(int i);
    }

    /* loaded from: classes2.dex */
    public interface OnSwitchDoubleClickListener {
        void onDoubleClick(int i);
    }

    public static /* synthetic */ boolean $r8$lambda$dj3Qol0sixWRrG4Hu9xDrep_ejg(GestureDetector gestureDetector, View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SwitchView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTabViewChildIds = new ArrayList();
        this.mSelectedId = -1;
        this.mIsSwitchable = true;
        this.mContext = context;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SwitchView, i, 2131951984);
        Drawable drawable = obtainStyledAttributes.getDrawable(1);
        Drawable drawable2 = obtainStyledAttributes.getDrawable(11);
        this.mTextColor = obtainStyledAttributes.getColor(12, getResources().getColor(R.color.switch_view_text_unselected_color));
        this.mTextSize = obtainStyledAttributes.getDimensionPixelSize(13, 0);
        this.mDotBackground = obtainStyledAttributes.getDrawable(2);
        this.mDotTextColor = obtainStyledAttributes.getColor(6, getResources().getColor(R.color.switch_view_dot_text_color));
        this.mDotTextSize = obtainStyledAttributes.getDimensionPixelSize(7, 0);
        this.mDotStartMargin = obtainStyledAttributes.getDimensionPixelSize(5, 0);
        this.mDotTopMargin = obtainStyledAttributes.getDimensionPixelSize(8, 0);
        this.mDotWidth = obtainStyledAttributes.getDimensionPixelSize(9, 0);
        this.mDotHeight = obtainStyledAttributes.getDimensionPixelSize(3, 0);
        this.mDotPadding = obtainStyledAttributes.getDimensionPixelSize(4, 0);
        int dimensionPixelOffset = obtainStyledAttributes.getDimensionPixelOffset(10, 0);
        obtainStyledAttributes.recycle();
        setPadding(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset);
        if (drawable != null) {
            setBackground(drawable);
        }
        initTabViewBackground(drawable2);
    }

    public final void initTabViewBackground(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        TextView textView = new TextView(this.mContext);
        this.mBackgroundTabView = textView;
        if (textView.getId() == -1) {
            this.mBackgroundTabView.setId(ViewGroup.generateViewId());
        }
        this.mBackgroundTabView.setGravity(17);
        this.mBackgroundTabView.setBackground(drawable);
        this.mBackgroundTabView.setVisibility(4);
        addView(this.mBackgroundTabView);
    }

    public void addTab(CharSequence charSequence) {
        final TextView textView = new TextView(this.mContext);
        if (textView.getId() == -1) {
            textView.setId(ViewGroup.generateViewId());
        }
        addView(textView);
        this.mTabViewChildIds.add(Integer.valueOf(textView.getId()));
        final int size = this.mTabViewChildIds.size() - 1;
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        updateTabViews(constraintSet);
        constraintSet.applyTo(this);
        textView.setGravity(17);
        textView.setTextColor(this.mTextColor);
        textView.setTextSize(0, this.mTextSize);
        textView.setText(charSequence);
        textView.setContentDescription(charSequence);
        textView.setTypeface(Typeface.create(BuildUtil.isMiui13() ? "misans" : "mipro-medium", 0));
        final GestureDetector gestureDetector = new GestureDetector(this.mContext, new GestureDetector.SimpleOnGestureListener() { // from class: com.miui.gallery.widget.SwitchView.1
            {
                SwitchView.this = this;
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                if (!textView.isSelected()) {
                    SwitchView.this.setSelectedTab(size);
                }
                return super.onSingleTapUp(motionEvent);
            }

            @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
            public boolean onDoubleTap(MotionEvent motionEvent) {
                if (!textView.isSelected() || SwitchView.this.mOnSwitchDoubleClickListener == null) {
                    return false;
                }
                SwitchView.this.mOnSwitchDoubleClickListener.onDoubleClick(size);
                return false;
            }
        });
        textView.setOnTouchListener(new View.OnTouchListener() { // from class: com.miui.gallery.widget.SwitchView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return SwitchView.$r8$lambda$dj3Qol0sixWRrG4Hu9xDrep_ejg(gestureDetector, view, motionEvent);
            }
        });
    }

    public final void updateTabViews(ConstraintSet constraintSet) {
        int i = 0;
        while (i < this.mTabViewChildIds.size()) {
            int intValue = this.mTabViewChildIds.get(i).intValue();
            constraintSet.constrainWidth(intValue, 0);
            constraintSet.constrainHeight(intValue, 0);
            constraintSet.setHorizontalWeight(intValue, 1.0f);
            int intValue2 = i == 0 ? 0 : this.mTabViewChildIds.get(i - 1).intValue();
            int intValue3 = i == this.mTabViewChildIds.size() + (-1) ? 0 : this.mTabViewChildIds.get(i + 1).intValue();
            constraintSet.centerVertically(intValue, 0);
            int i2 = 6;
            constraintSet.connect(intValue, 6, intValue2, intValue2 == 0 ? 6 : 7);
            if (intValue3 == 0) {
                i2 = 7;
            }
            constraintSet.connect(intValue, 7, intValue3, i2);
            constraintSet.connect(intValue, 3, 0, 3);
            constraintSet.connect(intValue, 4, 0, 4);
            i++;
        }
    }

    public void setSelectedTab(int i) {
        if (this.mIsSwitchable && i >= 0 && i < this.mTabViewChildIds.size()) {
            int intValue = this.mTabViewChildIds.get(i).intValue();
            this.mSelectedId = intValue;
            TextView textView = (TextView) findViewById(intValue);
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                TextView textView2 = (TextView) getChildAt(i2);
                if (textView2 != textView && textView2.isSelected()) {
                    textView2.setTextColor(getResources().getColor(R.color.switch_view_text_unselected_color));
                    textView2.setSelected(false);
                    textView2.getPaint().setFakeBoldText(false);
                }
            }
            textView.setSelected(true);
            textView.setTextColor(getResources().getColor(R.color.switch_view_text_selected_color));
            textView.getPaint().setFakeBoldText(true);
            if (this.mBackgroundTabView.getVisibility() == 0) {
                Folme.useAt(this.mBackgroundTabView).state().to(new AnimState().add(ViewProperty.X, textView.getX()).add(ViewProperty.Y, textView.getY()).add(ViewProperty.WIDTH, textView.getWidth()).add(ViewProperty.HEIGHT, textView.getHeight()), new AnimConfig[0]);
            }
            OnSwitchChangeListener onSwitchChangeListener = this.mOnSwitchChangeListener;
            if (onSwitchChangeListener != null) {
                onSwitchChangeListener.onSwitchChange(i);
            }
            updateChildIdsFromXml();
        }
    }

    public void setSwitchable(boolean z) {
        this.mIsSwitchable = z;
    }

    public final void updateChildIdsFromXml() {
        if (this.mTabViewChildIds.size() == 0) {
            for (int i = 0; i < getChildCount(); i++) {
                TextView textView = (TextView) getChildAt(i);
                if (textView.getId() != this.mBackgroundTabView.getId() && textView.getId() != this.mDotView.getId()) {
                    this.mTabViewChildIds.add(Integer.valueOf(textView.getId()));
                }
            }
        }
    }

    public void initDotView() {
        TextView textView = new TextView(this.mContext);
        this.mDotView = textView;
        if (textView.getId() == -1) {
            this.mDotView.setId(ViewGroup.generateViewId());
        }
        addView(this.mDotView);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        int id = this.mDotView.getId();
        constraintSet.constrainWidth(id, -2);
        constraintSet.constrainHeight(id, this.mDotHeight);
        constraintSet.setMargin(id, 6, this.mDotStartMargin);
        constraintSet.setMargin(id, 3, this.mDotTopMargin);
        constraintSet.connect(id, 6, 0, 6);
        constraintSet.connect(id, 3, 0, 3);
        constraintSet.applyTo(this);
        this.mDotView.setBackground(this.mDotBackground);
        this.mDotView.setGravity(17);
        this.mDotView.setPadding(this.mDotPadding, getResources().getDimensionPixelOffset(R.dimen.switch_view_padding_top), this.mDotPadding, 0);
        this.mDotView.setTypeface(Typeface.create("mitype-mono-bold", 0));
        this.mDotView.setTextColor(this.mDotTextColor);
        this.mDotView.setTextSize(0, this.mDotTextSize);
        this.mDotView.setVisibility(8);
    }

    public void showOrHideDotView(boolean z) {
        TextView textView = this.mDotView;
        if (textView == null) {
            return;
        }
        if (z && textView.getVisibility() != 0) {
            FolmeUtil.setCustomVisibleAnim(this.mDotView, true, new AnimParams.Builder().setAlpha(1.0f).setScale(1.0f).build(), new AnimConfig().setEase(-2, 0.8f, 0.25f), null);
        } else if (z || this.mDotView.getVisibility() == 8) {
        } else {
            FolmeUtil.setCustomVisibleAnim(this.mDotView, false, new AnimParams.Builder().setAlpha(0.0f).setScale(0.8f).build(), new AnimConfig().setEase(-2, 1.0f, 0.2f), null);
        }
    }

    public void setDotContent(CharSequence charSequence, boolean z) {
        TextView textView = this.mDotView;
        if (textView == null) {
            return;
        }
        textView.setText(charSequence);
        this.mDotView.setContentDescription(charSequence);
        if (z) {
            int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.switch_view_padding_remain);
            this.mDotView.setPadding(this.mDotPadding + dimensionPixelOffset, getResources().getDimensionPixelOffset(R.dimen.switch_view_padding_top), this.mDotPadding + dimensionPixelOffset, 0);
            return;
        }
        this.mDotView.setPadding(this.mDotPadding, getResources().getDimensionPixelOffset(R.dimen.switch_view_padding_top), this.mDotPadding, 0);
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        TextView textView;
        super.onLayout(z, i, i2, i3, i4);
        DefaultLogger.i("SwitchView", "onLayout");
        int i5 = this.mSelectedId;
        if (i5 == -1 || !z || (textView = (TextView) findViewById(i5)) == null) {
            return;
        }
        updateSelected(textView);
    }

    public final void updateSelected(TextView textView) {
        if (this.mBackgroundTabView.getVisibility() != 0) {
            this.mBackgroundTabView.setVisibility(0);
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mBackgroundTabView.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).width = textView.getWidth();
        ((ViewGroup.MarginLayoutParams) layoutParams).height = textView.getHeight();
        this.mBackgroundTabView.setLayoutParams(layoutParams);
        this.mBackgroundTabView.setX(textView.getX());
        this.mBackgroundTabView.setY(textView.getY());
    }

    public void setOnSwitchClickListener(OnSwitchChangeListener onSwitchChangeListener, OnSwitchDoubleClickListener onSwitchDoubleClickListener) {
        this.mOnSwitchChangeListener = onSwitchChangeListener;
        this.mOnSwitchDoubleClickListener = onSwitchDoubleClickListener;
    }
}
