package miuix.miuixbasewidget.widget;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.ArrayList;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;
import miuix.miuixbasewidget.R$dimen;
import miuix.miuixbasewidget.R$drawable;
import miuix.miuixbasewidget.R$id;
import miuix.miuixbasewidget.R$layout;
import miuix.miuixbasewidget.R$style;
import miuix.miuixbasewidget.R$styleable;
import miuix.miuixbasewidget.widget.FilterSortView;
import miuix.view.CompatViewMethod;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes3.dex */
public class FilterSortView extends ConstraintLayout {
    public TabView mBackgroundTabView;
    public boolean mEnabled;
    public TabView.FilterHoverListener mFilterHoverListener;
    public int mFilteredId;
    public boolean mFilteredUpdated;
    public View mHoverBgView;
    public TabView.OnFilteredListener mOnFilteredListener;
    public final int mPadding;
    public float mPreTabViewHeight;
    public float mPreTabViewWidth;
    public List<Integer> mTabViewChildIds;

    /* renamed from: $r8$lambda$5LQJAMuyaG1O_5mPkbbInj-DPVQ */
    public static /* synthetic */ void m2620$r8$lambda$5LQJAMuyaG1O_5mPkbbInjDPVQ(FilterSortView filterSortView, ConstraintLayout.LayoutParams layoutParams) {
        filterSortView.lambda$updateFiltered$0(layoutParams);
    }

    public FilterSortView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FilterSortView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTabViewChildIds = new ArrayList();
        this.mFilteredId = -1;
        this.mEnabled = true;
        this.mFilteredUpdated = false;
        this.mPreTabViewHeight = 0.0f;
        this.mPreTabViewWidth = 0.0f;
        this.mOnFilteredListener = new TabView.OnFilteredListener() { // from class: miuix.miuixbasewidget.widget.FilterSortView.1
            {
                FilterSortView.this = this;
            }

            @Override // miuix.miuixbasewidget.widget.FilterSortView.TabView.OnFilteredListener
            public void OnFilteredChangedListener(TabView tabView, boolean z) {
                if (!z || FilterSortView.this.mBackgroundTabView.getVisibility() != 0) {
                    return;
                }
                AnimState add = new AnimState("target").add(ViewProperty.X, tabView.getX()).add(ViewProperty.Y, tabView.getY());
                if (FilterSortView.this.mPreTabViewWidth != tabView.getWidth()) {
                    add.add(ViewProperty.WIDTH, tabView.getWidth());
                }
                if (FilterSortView.this.mPreTabViewHeight != tabView.getHeight()) {
                    add.add(ViewProperty.HEIGHT, tabView.getHeight());
                }
                FilterSortView.this.mPreTabViewWidth = tabView.getWidth();
                FilterSortView.this.mPreTabViewHeight = tabView.getHeight();
                Folme.useAt(FilterSortView.this.mBackgroundTabView).state().setFlags(1L).to(add, new AnimConfig[0]);
                FilterSortView.this.mFilteredId = tabView.getId();
                FilterSortView.this.mFilteredUpdated = false;
            }
        };
        this.mFilterHoverListener = new TabView.FilterHoverListener() { // from class: miuix.miuixbasewidget.widget.FilterSortView.2
            {
                FilterSortView.this = this;
            }

            @Override // miuix.miuixbasewidget.widget.FilterSortView.TabView.FilterHoverListener
            public void onHoverFilterEnter() {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(FilterSortView.this.mBackgroundTabView, "scaleX", FilterSortView.this.mBackgroundTabView.getScaleX(), 1.05f);
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(FilterSortView.this.mBackgroundTabView, "scaleY", FilterSortView.this.mBackgroundTabView.getScaleY(), 1.05f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ofFloat, ofFloat2);
                animatorSet.setDuration(350L);
                animatorSet.setInterpolator(new DecelerateInterpolator(1.5f));
                animatorSet.start();
            }

            @Override // miuix.miuixbasewidget.widget.FilterSortView.TabView.FilterHoverListener
            public void onHoverFilterExit() {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(FilterSortView.this.mBackgroundTabView, "scaleX", FilterSortView.this.mBackgroundTabView.getScaleX(), 1.0f);
                ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(FilterSortView.this.mBackgroundTabView, "scaleY", FilterSortView.this.mBackgroundTabView.getScaleY(), 1.0f);
                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(ofFloat, ofFloat2);
                animatorSet.setDuration(350L);
                animatorSet.setInterpolator(new DecelerateInterpolator(1.5f));
                animatorSet.start();
            }

            @Override // miuix.miuixbasewidget.widget.FilterSortView.TabView.FilterHoverListener
            public void onHoverEnter() {
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(FilterSortView.this.mHoverBgView, "alpha", FilterSortView.this.mHoverBgView.getAlpha(), 1.0f);
                ofFloat.setDuration(350L);
                ofFloat.setInterpolator(new DecelerateInterpolator(1.5f));
                ofFloat.start();
            }

            @Override // miuix.miuixbasewidget.widget.FilterSortView.TabView.FilterHoverListener
            public void onHoverExit(float f, float f2) {
                if (f < FilterSortView.this.mPadding || f2 < 0.0f || f > (FilterSortView.this.getRight() - FilterSortView.this.getLeft()) - (FilterSortView.this.mPadding * 2) || f2 > (FilterSortView.this.getBottom() - FilterSortView.this.getTop()) - (FilterSortView.this.mPadding * 2)) {
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(FilterSortView.this.mHoverBgView, "alpha", FilterSortView.this.mHoverBgView.getAlpha(), 0.0f);
                    ofFloat.setDuration(350L);
                    ofFloat.setInterpolator(new DecelerateInterpolator(1.5f));
                    ofFloat.start();
                }
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.FilterSortView, i, R$style.Widget_FilterSortView_DayNight);
        Drawable drawable = obtainStyledAttributes.getDrawable(R$styleable.FilterSortView_filterSortViewBackground);
        Drawable drawable2 = obtainStyledAttributes.getDrawable(R$styleable.FilterSortView_filterSortTabViewCoverBg);
        this.mEnabled = obtainStyledAttributes.getBoolean(R$styleable.FilterSortView_android_enabled, true);
        obtainStyledAttributes.recycle();
        this.mPadding = getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_filter_sort_view_padding);
        setBackground(drawable);
        initHoverBgView();
        initCoverBg(drawable2);
        CompatViewMethod.setForceDarkAllowed(this, false);
    }

    public final void initHoverBgView() {
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(0, 0);
        View view = new View(getContext());
        this.mHoverBgView = view;
        view.setLayoutParams(layoutParams);
        this.mHoverBgView.setId(View.generateViewId());
        this.mHoverBgView.setBackgroundResource(R$drawable.miuix_appcompat_filter_sort_hover_bg);
        this.mHoverBgView.setAlpha(0.0f);
        addView(this.mHoverBgView);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(this.mHoverBgView.getId(), 3, getId(), 3);
        constraintSet.connect(this.mHoverBgView.getId(), 4, getId(), 4);
        constraintSet.connect(this.mHoverBgView.getId(), 6, getId(), 6);
        constraintSet.connect(this.mHoverBgView.getId(), 7, getId(), 7);
        constraintSet.applyTo(this);
    }

    public final void initCoverBg(Drawable drawable) {
        TabView inflateTabView = inflateTabView();
        this.mBackgroundTabView = inflateTabView;
        inflateTabView.setBackground(drawable);
        this.mBackgroundTabView.mArrow.setVisibility(8);
        this.mBackgroundTabView.mTextView.setVisibility(8);
        this.mBackgroundTabView.setVisibility(4);
        this.mBackgroundTabView.setEnabled(this.mEnabled);
        addView(this.mBackgroundTabView);
    }

    public final TabView inflateTabView() {
        return (TabView) LayoutInflater.from(getContext()).inflate(R$layout.layout_filter_tab_view, (ViewGroup) null);
    }

    public void setTabIncatorVisibility(int i) {
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            View childAt = getChildAt(i2);
            if (childAt instanceof TabView) {
                ((TabView) childAt).setIndicatorVisibility(i);
            }
        }
    }

    public void setFilteredTab(TabView tabView) {
        this.mFilteredId = tabView.getId();
        this.mFilteredUpdated = false;
        tabView.setFiltered(true);
        updateChildIdsFromXml();
    }

    public final void updateChildIdsFromXml() {
        if (this.mTabViewChildIds.size() == 0) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                if (childAt instanceof TabView) {
                    TabView tabView = (TabView) childAt;
                    if (tabView.getId() != this.mBackgroundTabView.getId()) {
                        tabView.setOnFilteredListener(this.mOnFilteredListener);
                        this.mTabViewChildIds.add(Integer.valueOf(tabView.getId()));
                        tabView.setFilterHoverListener(this.mFilterHoverListener);
                    }
                }
            }
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(this);
            updateTabViews(constraintSet);
            constraintSet.applyTo(this);
        }
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        if (this.mEnabled != z) {
            this.mEnabled = z;
            refreshTabState();
        }
    }

    public final void refreshTabState() {
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt instanceof TabView) {
                ((TabView) childAt).setEnabled(this.mEnabled);
            }
        }
    }

    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        TabView tabView;
        super.onLayout(z, i, i2, i3, i4);
        int i5 = this.mFilteredId;
        if (i5 == -1 || this.mFilteredUpdated || (tabView = (TabView) findViewById(i5)) == null) {
            return;
        }
        updateFiltered(tabView);
        if (tabView.getWidth() <= 0) {
            return;
        }
        this.mFilteredUpdated = true;
    }

    public final void updateFiltered(TabView tabView) {
        if (this.mBackgroundTabView.getVisibility() != 0) {
            this.mBackgroundTabView.setVisibility(0);
        }
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mBackgroundTabView.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).width = tabView.getWidth();
        ((ViewGroup.MarginLayoutParams) layoutParams).height = tabView.getHeight();
        this.mBackgroundTabView.setX(tabView.getX());
        this.mBackgroundTabView.setY(tabView.getY());
        post(new Runnable() { // from class: miuix.miuixbasewidget.widget.FilterSortView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FilterSortView.m2620$r8$lambda$5LQJAMuyaG1O_5mPkbbInjDPVQ(FilterSortView.this, layoutParams);
            }
        });
    }

    public /* synthetic */ void lambda$updateFiltered$0(ConstraintLayout.LayoutParams layoutParams) {
        this.mBackgroundTabView.setLayoutParams(layoutParams);
    }

    public final void updateTabViews(ConstraintSet constraintSet) {
        int i = 0;
        while (i < this.mTabViewChildIds.size()) {
            int intValue = this.mTabViewChildIds.get(i).intValue();
            constraintSet.constrainWidth(intValue, 0);
            constraintSet.constrainHeight(intValue, -2);
            constraintSet.setHorizontalWeight(intValue, 1.0f);
            int intValue2 = i == 0 ? 0 : this.mTabViewChildIds.get(i - 1).intValue();
            int intValue3 = i == this.mTabViewChildIds.size() + (-1) ? 0 : this.mTabViewChildIds.get(i + 1).intValue();
            constraintSet.centerVertically(intValue, 0);
            constraintSet.connect(intValue, 6, intValue2, intValue2 == 0 ? 6 : 7, intValue2 == 0 ? this.mPadding : 0);
            constraintSet.connect(intValue, 7, intValue3, intValue3 == 0 ? 7 : 6, intValue3 == 0 ? this.mPadding : 0);
            constraintSet.connect(intValue, 3, 0, 3, this.mPadding);
            constraintSet.connect(intValue, 4, 0, 4, this.mPadding);
            i++;
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mFilteredUpdated = false;
    }

    /* loaded from: classes3.dex */
    public static class TabView extends LinearLayout {
        public ImageView mArrow;
        public Drawable mArrowIcon;
        public boolean mDescending;
        public boolean mDescendingEnabled;
        public FilterHoverListener mFilterHoverListener;
        public boolean mFiltered;
        public int mIndicatorVisibility;
        public OnFilteredListener mOnFilteredListener;
        public FilterSortView mParent;
        public ColorStateList mTextColor;
        public TextView mTextView;

        /* loaded from: classes3.dex */
        public interface FilterHoverListener {
            void onHoverEnter();

            void onHoverExit(float f, float f2);

            void onHoverFilterEnter();

            void onHoverFilterExit();
        }

        /* loaded from: classes3.dex */
        public interface OnFilteredListener {
            void OnFilteredChangedListener(TabView tabView, boolean z);
        }

        public static /* synthetic */ boolean $r8$lambda$Sj4hBjmHHepqZ5SW71Z1cGwSsvM(TabView tabView, View view, MotionEvent motionEvent) {
            return tabView.lambda$initView$0(view, motionEvent);
        }

        public TabView(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public TabView(Context context, AttributeSet attributeSet, int i) {
            super(context, attributeSet, i);
            this.mDescendingEnabled = true;
            LayoutInflater.from(context).inflate(R$layout.miuix_appcompat_filter_sort_tab_view, (ViewGroup) this, true);
            this.mTextView = (TextView) findViewById(16908308);
            this.mArrow = (ImageView) findViewById(R$id.arrow);
            if (attributeSet != null) {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.FilterSortTabView, i, R$style.Widget_FilterSortTabView_DayNight);
                String string = obtainStyledAttributes.getString(R$styleable.FilterSortTabView_android_text);
                boolean z = obtainStyledAttributes.getBoolean(R$styleable.FilterSortTabView_descending, true);
                this.mIndicatorVisibility = obtainStyledAttributes.getInt(R$styleable.FilterSortTabView_indicatorVisibility, 0);
                this.mArrowIcon = obtainStyledAttributes.getDrawable(R$styleable.FilterSortTabView_arrowFilterSortTabView);
                this.mTextColor = obtainStyledAttributes.getColorStateList(R$styleable.FilterSortTabView_filterSortTabViewTextColor);
                obtainStyledAttributes.recycle();
                initView(string, z);
            }
            this.mArrow.setVisibility(this.mIndicatorVisibility);
            if (getId() == -1) {
                setId(LinearLayout.generateViewId());
            }
        }

        public void setOnFilteredListener(OnFilteredListener onFilteredListener) {
            this.mOnFilteredListener = onFilteredListener;
        }

        public void setFilterHoverListener(FilterHoverListener filterHoverListener) {
            this.mFilterHoverListener = filterHoverListener;
        }

        public final void initView(CharSequence charSequence, boolean z) {
            setGravity(17);
            if (getBackground() == null) {
                setBackground(parseBackground());
            }
            this.mArrow.setBackground(this.mArrowIcon);
            this.mTextView.setTextColor(this.mTextColor);
            this.mTextView.setText(charSequence);
            setDescending(z);
            setOnHoverListener(new View.OnHoverListener() { // from class: miuix.miuixbasewidget.widget.FilterSortView$TabView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnHoverListener
                public final boolean onHover(View view, MotionEvent motionEvent) {
                    return FilterSortView.TabView.$r8$lambda$Sj4hBjmHHepqZ5SW71Z1cGwSsvM(FilterSortView.TabView.this, view, motionEvent);
                }
            });
        }

        public /* synthetic */ boolean lambda$initView$0(View view, MotionEvent motionEvent) {
            if (this.mFilterHoverListener == null || motionEvent.getSource() == 4098) {
                return false;
            }
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 9) {
                if (this.mFiltered) {
                    this.mFilterHoverListener.onHoverFilterEnter();
                }
                this.mFilterHoverListener.onHoverEnter();
                return true;
            } else if (actionMasked != 10) {
                return true;
            } else {
                if (this.mFiltered) {
                    this.mFilterHoverListener.onHoverFilterExit();
                }
                this.mFilterHoverListener.onHoverExit(motionEvent.getX() + getLeft(), motionEvent.getY());
                return true;
            }
        }

        public void setIndicatorVisibility(int i) {
            this.mArrow.setVisibility(i);
        }

        public void setFiltered(boolean z) {
            TabView tabView;
            FilterSortView filterSortView = (FilterSortView) getParent();
            this.mParent = filterSortView;
            if (z && filterSortView != null) {
                int childCount = filterSortView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = this.mParent.getChildAt(i);
                    if ((childAt instanceof TabView) && (tabView = (TabView) childAt) != this && tabView.mFiltered) {
                        tabView.setFiltered(false);
                    }
                }
            }
            this.mFiltered = z;
            this.mTextView.setSelected(z);
            this.mArrow.setSelected(z);
            setSelected(z);
            OnFilteredListener onFilteredListener = this.mOnFilteredListener;
            if (onFilteredListener != null) {
                onFilteredListener.OnFilteredChangedListener(this, z);
            }
        }

        public final Drawable parseBackground() {
            return getResources().getDrawable(R$drawable.miuix_appcompat_filter_sort_tab_view_bg_normal);
        }

        public void setDescending(boolean z) {
            this.mDescending = z;
            if (z) {
                this.mArrow.setRotationX(0.0f);
            } else {
                this.mArrow.setRotationX(180.0f);
            }
        }

        public boolean getDescendingEnabled() {
            return this.mDescendingEnabled;
        }

        public void setDescendingEnabled(boolean z) {
            this.mDescendingEnabled = z;
        }

        public View getArrowView() {
            return this.mArrow;
        }

        @Override // android.view.View
        public void setOnClickListener(final View.OnClickListener onClickListener) {
            super.setOnClickListener(new View.OnClickListener() { // from class: miuix.miuixbasewidget.widget.FilterSortView.TabView.1
                {
                    TabView.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (TabView.this.mFiltered) {
                        if (TabView.this.mDescendingEnabled) {
                            TabView tabView = TabView.this;
                            tabView.setDescending(true ^ tabView.mDescending);
                        }
                    } else {
                        TabView.this.setFiltered(true);
                    }
                    onClickListener.onClick(view);
                    HapticCompat.performHapticFeedback(view, HapticFeedbackConstants.MIUI_MESH_NORMAL);
                }
            });
        }

        @Override // android.view.View
        public void setEnabled(boolean z) {
            super.setEnabled(z);
            this.mTextView.setEnabled(z);
        }
    }
}
