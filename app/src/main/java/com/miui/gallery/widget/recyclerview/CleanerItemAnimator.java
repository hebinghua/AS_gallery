package com.miui.gallery.widget.recyclerview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.ArrayList;
import java.util.Iterator;
import miuix.view.animation.CubicEaseInOutInterpolator;

/* loaded from: classes3.dex */
public class CleanerItemAnimator extends SimpleItemAnimator {
    public ArrayList<RecyclerView.ViewHolder> mPendingRemoveHolders = new ArrayList<>();
    public ArrayList<RecyclerView.ViewHolder> mPendingMoveHolders = new ArrayList<>();
    public ArrayList<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<>();
    public ArrayList<RecyclerView.ViewHolder> mMoveAnimations = new ArrayList<>();
    public Interpolator mInterpolator = new CubicEaseInOutInterpolator();

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimations() {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void runPendingAnimations() {
        boolean z = !this.mPendingRemoveHolders.isEmpty();
        boolean z2 = !this.mPendingMoveHolders.isEmpty();
        if (z) {
            Iterator<RecyclerView.ViewHolder> it = this.mPendingRemoveHolders.iterator();
            while (it.hasNext()) {
                animateRemoveImpl(it.next());
            }
            this.mPendingRemoveHolders.clear();
        }
        if (z2) {
            Iterator<RecyclerView.ViewHolder> it2 = this.mPendingMoveHolders.iterator();
            while (it2.hasNext()) {
                animateMoveImpl(it2.next());
            }
            this.mPendingMoveHolders.clear();
        }
    }

    public final void animateMoveImpl(final RecyclerView.ViewHolder viewHolder) {
        this.mMoveAnimations.add(viewHolder);
        ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.widget.recyclerview.CleanerItemAnimator.1
            @Override // java.lang.Runnable
            public void run() {
                View view = viewHolder.itemView;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "translationY", view.getTranslationY(), 0.0f);
                ofFloat.setDuration(500L);
                ofFloat.setInterpolator(CleanerItemAnimator.this.mInterpolator);
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.CleanerItemAnimator.1.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                        CleanerItemAnimator.this.dispatchMoveStarting(viewHolder);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                        CleanerItemAnimator.this.dispatchMoveFinished(viewHolder);
                        CleanerItemAnimator.this.mMoveAnimations.remove(viewHolder);
                        if (!CleanerItemAnimator.this.isRunning()) {
                            CleanerItemAnimator.this.dispatchAnimationsFinished();
                        }
                    }
                });
                ofFloat.start();
            }
        }, 300L);
    }

    public final void animateRemoveImpl(final RecyclerView.ViewHolder viewHolder) {
        this.mRemoveAnimations.add(viewHolder);
        ThreadManager.getMainHandler().postDelayed(new Runnable() { // from class: com.miui.gallery.widget.recyclerview.CleanerItemAnimator.2
            @Override // java.lang.Runnable
            public void run() {
                final View view = viewHolder.itemView;
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(view, "alpha", 1.0f, 0.0f);
                ofFloat.setDuration(350L);
                ofFloat.setInterpolator(CleanerItemAnimator.this.mInterpolator);
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.recyclerview.CleanerItemAnimator.2.1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationStart(Animator animator) {
                        AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                        CleanerItemAnimator.this.dispatchRemoveStarting(viewHolder);
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public void onAnimationEnd(Animator animator) {
                        CleanerItemAnimator.this.mRemoveAnimations.remove(viewHolder);
                        view.setVisibility(8);
                        AnonymousClass2 anonymousClass2 = AnonymousClass2.this;
                        CleanerItemAnimator.this.dispatchRemoveFinished(viewHolder);
                        if (!CleanerItemAnimator.this.isRunning()) {
                            CleanerItemAnimator.this.dispatchAnimationsFinished();
                        }
                    }
                });
                ofFloat.start();
            }
        }, 300L);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        this.mPendingRemoveHolders.add(viewHolder);
        return true;
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateMove(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
        View view = viewHolder.itemView;
        view.setTranslationY(-(i4 - ((int) (i2 + view.getTranslationY()))));
        this.mPendingMoveHolders.add(viewHolder);
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean isRunning() {
        return !this.mPendingMoveHolders.isEmpty() || !this.mPendingRemoveHolders.isEmpty() || !this.mRemoveAnimations.isEmpty() || !this.mMoveAnimations.isEmpty();
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
        if (viewHolder == viewHolder2) {
            return animateMove(viewHolder, i, i2, i3, i4);
        }
        return false;
    }
}
