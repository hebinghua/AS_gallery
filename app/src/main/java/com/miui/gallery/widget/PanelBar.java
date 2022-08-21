package com.miui.gallery.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class PanelBar extends FrameLayout {
    public PanelBar(Context context) {
        this(context, null);
    }

    public PanelBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PanelBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (isClickable()) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshVisibility();
    }

    public final void refreshVisibility() {
        if (getChildCount() > 0) {
            setVisibility(0);
        } else {
            setVisibility(8);
        }
    }

    public void replaceItem(PanelItem panelItem, boolean z) {
        final View childAt = getChildAt(getChildCount() - 1);
        if (childAt != null) {
            Animator loadAnimator = loadAnimator(false);
            startViewTransition(childAt);
            loadAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.PanelBar.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PanelBar.this.endViewTransition(childAt);
                }
            });
            loadAnimator.setTarget(childAt);
            loadAnimator.start();
            if (!z) {
                loadAnimator.end();
                endViewTransition(childAt);
            }
            removeView(childAt);
        }
        removeAllViews();
        if (panelItem != null && panelItem.getView() != null) {
            Animator loadAnimator2 = loadAnimator(true);
            loadAnimator2.setTarget(panelItem.getView());
            FolmeUtil.setDefaultTouchAnim(panelItem.getView(), this, null, false, false, true);
            loadAnimator2.start();
            if (!z) {
                loadAnimator2.end();
            }
            ViewParent parent = panelItem.getView().getParent();
            if (parent != null) {
                if (!(parent instanceof ViewGroup)) {
                    return;
                }
                ViewGroup viewGroup = (ViewGroup) parent;
                viewGroup.removeView(panelItem.getView());
                if (panelItem.getView().getParent() != null) {
                    viewGroup.endViewTransition(panelItem.getView());
                }
            }
            addView(panelItem.getView());
        }
        refreshVisibility();
    }

    public final Animator loadAnimator(boolean z) {
        ObjectAnimator ofFloat = z ? ObjectAnimator.ofFloat((Object) null, "alpha", 0.0f, 1.0f) : ObjectAnimator.ofFloat((Object) null, "alpha", 1.0f, 0.0f);
        ofFloat.setDuration(500L);
        return ofFloat;
    }
}
