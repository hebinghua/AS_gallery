package miuix.appcompat.internal.app.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ViewProperty;
import miuix.appcompat.R$bool;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.ActionBarTransitionListener;
import miuix.appcompat.internal.view.menu.action.ActionMenuPresenter;
import miuix.appcompat.internal.view.menu.action.ActionMenuView;
import miuix.internal.util.DeviceHelper;
import miuix.internal.util.ViewUtils;

/* loaded from: classes3.dex */
public abstract class AbsActionBarView extends ViewGroup {
    public ActionMenuPresenter mActionMenuPresenter;
    public TransitionListener mAnimConfigListener;
    public AnimConfig mCollapseAnimHideConfig;
    public AnimConfig mCollapseAnimShowConfig;
    public int mContentHeight;
    public int mExpandState;
    public AnimConfig mHideProcessConfig;
    public int mInnerExpandState;
    public float mLastProcess;
    public ActionMenuView mMenuView;
    public AnimConfig mMovableAnimConfig;
    public boolean mResizable;
    public AnimConfig mShowProcessConfig;
    public boolean mSplitActionBar;
    public ActionBarContainer mSplitView;
    public boolean mSplitWhenNarrow;
    public ActionBarTransitionListener mTransitionListener;

    public int getActionBarStyle() {
        return 16843470;
    }

    public void onAnimatedExpandStateChanged(int i, int i2) {
    }

    public void onExpandStateChanged(int i, int i2) {
    }

    public AbsActionBarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public AbsActionBarView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mInnerExpandState = 1;
        this.mExpandState = 1;
        this.mResizable = true;
        this.mLastProcess = 0.0f;
        this.mAnimConfigListener = new TransitionListener() { // from class: miuix.appcompat.internal.app.widget.AbsActionBarView.1
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj) {
                super.onBegin(obj);
                ActionBarTransitionListener actionBarTransitionListener = AbsActionBarView.this.mTransitionListener;
                if (actionBarTransitionListener != null) {
                    actionBarTransitionListener.onTransitionBegin(obj);
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onUpdate(Object obj, Collection<UpdateInfo> collection) {
                super.onUpdate(obj, collection);
                ActionBarTransitionListener actionBarTransitionListener = AbsActionBarView.this.mTransitionListener;
                if (actionBarTransitionListener != null) {
                    actionBarTransitionListener.onTransitionUpdate(obj, collection);
                }
            }

            @Override // miuix.animation.listener.TransitionListener
            public void onComplete(Object obj) {
                super.onComplete(obj);
                ActionBarTransitionListener actionBarTransitionListener = AbsActionBarView.this.mTransitionListener;
                if (actionBarTransitionListener != null) {
                    actionBarTransitionListener.onTransitionComplete(obj);
                }
            }
        };
        this.mCollapseAnimShowConfig = new AnimConfig().setEase(-2, 1.0f, 0.3f);
        this.mShowProcessConfig = new AnimConfig().setEase(-2, 1.0f, 0.3f).addListeners(this.mAnimConfigListener);
        this.mCollapseAnimHideConfig = new AnimConfig().setEase(-2, 1.0f, 0.15f);
        this.mHideProcessConfig = new AnimConfig().setEase(-2, 1.0f, 0.15f).addListeners(this.mAnimConfigListener);
        this.mMovableAnimConfig = new AnimConfig().setEase(-2, 1.0f, 0.6f);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActionBar, 16843470, 0);
        int i2 = obtainStyledAttributes.getInt(R$styleable.ActionBar_expandState, 1);
        boolean z = obtainStyledAttributes.getBoolean(R$styleable.ActionBar_resizable, true);
        obtainStyledAttributes.recycle();
        if (i2 == 0 || (getContext().getResources().getConfiguration().orientation == 2 && !DeviceHelper.isTablet(getContext()))) {
            this.mInnerExpandState = 0;
            this.mExpandState = 0;
        } else {
            this.mInnerExpandState = 1;
            this.mExpandState = 1;
        }
        this.mResizable = z;
    }

    public void setActionBarTransitionListener(ActionBarTransitionListener actionBarTransitionListener) {
        this.mTransitionListener = actionBarTransitionListener;
    }

    public ActionBarTransitionListener getActionBarTransitionListener() {
        return this.mTransitionListener;
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(null, R$styleable.ActionBar, getActionBarStyle(), 0);
        setContentHeight(obtainStyledAttributes.getLayoutDimension(R$styleable.ActionBar_android_height, 0));
        obtainStyledAttributes.recycle();
        if (this.mSplitWhenNarrow) {
            setSplitActionBar(getContext().getResources().getBoolean(R$bool.abc_split_action_bar_is_narrow));
        }
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.onConfigurationChanged(configuration);
        }
        if (getContext().getResources().getConfiguration().orientation != 2 || DeviceHelper.isTablet(getContext())) {
            return;
        }
        setExpandState(0);
    }

    public void setSplitActionBar(boolean z) {
        this.mSplitActionBar = z;
    }

    public void setSplitWhenNarrow(boolean z) {
        this.mSplitWhenNarrow = z;
    }

    public void setContentHeight(int i) {
        this.mContentHeight = i;
        requestLayout();
    }

    public int getContentHeight() {
        return this.mContentHeight;
    }

    public void setSplitView(ActionBarContainer actionBarContainer) {
        this.mSplitView = actionBarContainer;
    }

    public int getAnimatedVisibility() {
        return getVisibility();
    }

    public ActionMenuView getActionMenuView() {
        return this.mMenuView;
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        if (i != getVisibility()) {
            super.setVisibility(i);
        }
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
    }

    public void postShowOverflowMenu() {
        post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.AbsActionBarView.2
            @Override // java.lang.Runnable
            public void run() {
                AbsActionBarView.this.showOverflowMenu();
            }
        });
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu(false);
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    public boolean isOverflowReserved() {
        ActionMenuPresenter actionMenuPresenter = this.mActionMenuPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowReserved();
    }

    public int measureChildView(View view, int i, int i2, int i3) {
        view.measure(View.MeasureSpec.makeMeasureSpec(i, Integer.MIN_VALUE), i2);
        return Math.max(0, (i - view.getMeasuredWidth()) - i3);
    }

    public int positionChild(View view, int i, int i2, int i3) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i4 = i2 + ((i3 - measuredHeight) / 2);
        ViewUtils.layoutChildView(this, view, i, i4, i + measuredWidth, i4 + measuredHeight);
        return measuredWidth;
    }

    public int positionChildInverse(View view, int i, int i2, int i3) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int i4 = i2 + ((i3 - measuredHeight) / 2);
        ViewUtils.layoutChildView(this, view, i - measuredWidth, i4, i, i4 + measuredHeight);
        return measuredWidth;
    }

    public ActionMenuView getMenuView() {
        return this.mMenuView;
    }

    public int getExpandState() {
        return this.mExpandState;
    }

    public void setExpandState(int i) {
        setExpandState(i, false);
    }

    public void setExpandState(int i, boolean z) {
        int i2;
        if ((getContext().getResources().getConfiguration().orientation != 2 || i == 0 || DeviceHelper.isTablet(getContext())) && this.mResizable && (i2 = this.mInnerExpandState) != i) {
            if (z) {
                onAnimatedExpandStateChanged(i2, i);
                return;
            }
            this.mInnerExpandState = i;
            if (i == 0) {
                this.mExpandState = 0;
            } else if (i == 1) {
                this.mExpandState = 1;
            }
            onExpandStateChanged(i2, i);
            requestLayout();
        }
    }

    public void setResizable(boolean z) {
        this.mResizable = z;
    }

    public boolean isResizable() {
        return this.mResizable;
    }

    /* loaded from: classes3.dex */
    public static class CollapseView {
        public float mAlpha;
        public List<View> mViews = new ArrayList();
        public boolean mIsAcceptAlphaChange = true;

        public void attachViews(View view) {
            if (this.mViews.contains(view)) {
                return;
            }
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: miuix.appcompat.internal.app.widget.AbsActionBarView.CollapseView.1
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view2) {
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view2) {
                    Iterator it = CollapseView.this.mViews.iterator();
                    while (it.hasNext()) {
                        Folme.clean((View) it.next());
                    }
                }
            });
            this.mViews.add(view);
        }

        public void setAlpha(float f) {
            this.mAlpha = f;
            Iterator<View> it = this.mViews.iterator();
            while (it.hasNext()) {
                Folme.useAt(it.next()).state().setTo(ViewProperty.ALPHA, Float.valueOf(f));
            }
        }

        public void setAcceptAlphaChange(boolean z) {
            this.mIsAcceptAlphaChange = z;
        }

        public void setVisibility(int i) {
            for (View view : this.mViews) {
                view.setVisibility(i);
            }
        }

        public void setAnimFrom(float f, int i, int i2) {
            AnimState animState = new AnimState("from");
            ViewProperty viewProperty = ViewProperty.ALPHA;
            if (!this.mIsAcceptAlphaChange) {
                f = this.mAlpha;
            }
            AnimState add = animState.add(viewProperty, f).add(ViewProperty.TRANSLATION_X, i).add(ViewProperty.TRANSLATION_Y, i2);
            Iterator<View> it = this.mViews.iterator();
            while (it.hasNext()) {
                Folme.useAt(it.next()).state().setTo(add);
            }
        }

        public void animTo(float f, int i, int i2, AnimConfig animConfig) {
            AnimState animState = new AnimState("to");
            ViewProperty viewProperty = ViewProperty.ALPHA;
            if (!this.mIsAcceptAlphaChange) {
                f = this.mAlpha;
            }
            AnimState add = animState.add(viewProperty, f).add(ViewProperty.TRANSLATION_X, i).add(ViewProperty.TRANSLATION_Y, i2);
            Iterator<View> it = this.mViews.iterator();
            while (it.hasNext()) {
                Folme.useAt(it.next()).state().to(add, animConfig);
            }
        }
    }
}
