package miuix.appcompat.internal.view.menu.action;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuItemImpl;
import miuix.appcompat.internal.view.menu.MenuView;
import miuix.internal.util.DeviceHelper;

/* loaded from: classes3.dex */
public abstract class ActionMenuView extends LinearLayout implements MenuBuilder.ItemInvoker, MenuView {
    public MenuBuilder mMenu;
    public OpenCloseAnimator mOpenCloseAnimator;
    public ActionMenuPresenter mPresenter;
    public boolean mReserveOverflow;

    public float computeAlpha(float f, boolean z, boolean z2) {
        int i;
        if (!z || !z2) {
            if (z) {
                i = (int) ((1.0f - f) * 10.0f);
            } else if (!z2) {
                return 1.0f;
            } else {
                i = (int) (f * 10.0f);
            }
            return i / 10.0f;
        }
        return 1.0f;
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return false;
    }

    public abstract int getCollapsedHeight();

    public int getWindowAnimations() {
        return 0;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView
    public boolean hasBackgroundView() {
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView
    public boolean hasBlurBackgroundView() {
        return false;
    }

    public ActionMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setBaselineAligned(false);
        this.mOpenCloseAnimator = new OpenCloseAnimator();
        setLayoutAnimation(null);
    }

    public void setPresenter(ActionMenuPresenter actionMenuPresenter) {
        this.mPresenter = actionMenuPresenter;
    }

    public ActionMenuPresenter getPresenter() {
        return this.mPresenter;
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.updateMenuView(false);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        } else {
            super.onMeasure(i, i2);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mPresenter.dismissPopupMenus(false);
    }

    public void setOverflowReserved(boolean z) {
        this.mReserveOverflow = z;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView
    public boolean filterLeftoverView(int i) {
        View childAt = getChildAt(i);
        childAt.clearAnimation();
        removeView(childAt);
        return true;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    /* renamed from: generateDefaultLayoutParams  reason: collision with other method in class */
    public LayoutParams mo2607generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    /* renamed from: generateLayoutParams  reason: collision with other method in class */
    public LayoutParams mo2609generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    /* renamed from: generateLayoutParams  reason: collision with other method in class */
    public LayoutParams mo2610generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) layoutParams);
        }
        return mo2607generateDefaultLayoutParams();
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams != null && (layoutParams instanceof LayoutParams);
    }

    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams mo2607generateDefaultLayoutParams = mo2607generateDefaultLayoutParams();
        mo2607generateDefaultLayoutParams.isOverflowButton = true;
        return mo2607generateDefaultLayoutParams;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuBuilder.ItemInvoker
    public boolean invokeItem(MenuItemImpl menuItemImpl) {
        return this.mMenu.performItemAction(menuItemImpl, 0);
    }

    @Override // miuix.appcompat.internal.view.menu.MenuView
    public void initialize(MenuBuilder menuBuilder) {
        this.mMenu = menuBuilder;
    }

    /* loaded from: classes3.dex */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        @ViewDebug.ExportedProperty
        public boolean isOverflowButton;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((LinearLayout.LayoutParams) layoutParams);
            this.isOverflowButton = layoutParams.isOverflowButton;
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.isOverflowButton = false;
        }
    }

    public float computeTranslationY(float f, boolean z, boolean z2) {
        float collapsedHeight = getCollapsedHeight();
        if (z && z2) {
            f = ((double) f) < 0.5d ? f * 2.0f : (1.0f - f) * 2.0f;
        } else if (z2) {
            f = 1.0f - f;
        }
        return f * collapsedHeight;
    }

    public void onPageScrolled(int i, float f, boolean z, boolean z2) {
        if (DeviceHelper.isFeatureWholeAnim()) {
            setAlpha(computeAlpha(f, z, z2));
        }
        float computeTranslationY = computeTranslationY(f, z, z2);
        if (!z || !z2 || getTranslationY() != 0.0f) {
            setTranslationY(computeTranslationY);
        }
        this.mOpenCloseAnimator.setTranslationY(computeTranslationY);
    }

    /* loaded from: classes3.dex */
    public class OpenCloseAnimator implements Animator.AnimatorListener {
        public Animator mCurrentAnimator;

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        public void setTranslationY(float f) {
            for (int i = 0; i < ActionMenuView.this.getChildCount(); i++) {
                ActionMenuView.this.getChildAt(i).setTranslationY(f);
            }
        }

        public OpenCloseAnimator() {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
            this.mCurrentAnimator = animator;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.mCurrentAnimator = null;
        }
    }
}
