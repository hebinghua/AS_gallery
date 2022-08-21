package com.miui.itemdrag.decorator;

import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public abstract class BaseDraggableItemDecorator extends RecyclerView.ItemDecoration {
    public AnimtorFinshCallback mAnimFinshCallback;
    public RecyclerView.ViewHolder mDraggingItemViewHolder;
    public final RecyclerView mRecyclerView;
    public int mReturnToDefaultPositionDuration = 200;
    public Interpolator mReturnToDefaultPositionInterpolator;

    /* loaded from: classes3.dex */
    public interface AnimtorFinshCallback {
        void onFinsh(View view);
    }

    public BaseDraggableItemDecorator(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        this.mRecyclerView = recyclerView;
        this.mDraggingItemViewHolder = viewHolder;
    }

    public void setReturnToDefaultPositionAnimationDuration(int i) {
        this.mReturnToDefaultPositionDuration = i;
    }

    public void setReturnToDefaultPositionAnimationInterpolator(Interpolator interpolator) {
        this.mReturnToDefaultPositionInterpolator = interpolator;
    }

    public void moveToDefaultPosition(View view, boolean z) {
        final float translationZ = ViewCompat.getTranslationZ(view);
        int determineMoveToDefaultPositionAnimationDurationFactor = (int) (this.mReturnToDefaultPositionDuration * determineMoveToDefaultPositionAnimationDurationFactor(view));
        if (z && determineMoveToDefaultPositionAnimationDurationFactor > 20) {
            ViewPropertyAnimatorCompat animate = ViewCompat.animate(view);
            ViewCompat.setTranslationZ(view, translationZ + 1.0f);
            animate.cancel();
            animate.setDuration(determineMoveToDefaultPositionAnimationDurationFactor);
            animate.setInterpolator(this.mReturnToDefaultPositionInterpolator);
            animate.translationX(0.0f);
            animate.translationY(0.0f);
            animate.translationZ(translationZ);
            animate.alpha(1.0f);
            animate.rotation(0.0f);
            animate.scaleX(1.0f);
            animate.scaleY(1.0f);
            animate.setListener(new ViewPropertyAnimatorListener() { // from class: com.miui.itemdrag.decorator.BaseDraggableItemDecorator.1
                @Override // androidx.core.view.ViewPropertyAnimatorListener
                public void onAnimationCancel(View view2) {
                }

                @Override // androidx.core.view.ViewPropertyAnimatorListener
                public void onAnimationStart(View view2) {
                }

                @Override // androidx.core.view.ViewPropertyAnimatorListener
                public void onAnimationEnd(View view2) {
                    ViewCompat.animate(view2).setListener(null);
                    BaseDraggableItemDecorator.resetDraggingItemViewEffects(view2, translationZ);
                    if (view2.getParent() instanceof RecyclerView) {
                        ViewCompat.postInvalidateOnAnimation((RecyclerView) view2.getParent());
                    }
                    if (BaseDraggableItemDecorator.this.mAnimFinshCallback != null) {
                        BaseDraggableItemDecorator.this.mAnimFinshCallback.onFinsh(view2);
                    }
                }
            });
            animate.start();
            return;
        }
        resetDraggingItemViewEffects(view, translationZ);
        AnimtorFinshCallback animtorFinshCallback = this.mAnimFinshCallback;
        if (animtorFinshCallback == null) {
            return;
        }
        animtorFinshCallback.onFinsh(view);
    }

    public float determineMoveToDefaultPositionAnimationDurationFactor(View view) {
        float translationX = view.getTranslationX();
        float translationY = view.getTranslationY();
        int width = view.getWidth() / 2;
        int height = view.getHeight() / 2;
        return Math.min(Math.max(Math.max(0.0f, width > 0 ? Math.abs(translationX / width) : 0.0f), height > 0 ? Math.abs(translationY / height) : 0.0f), 1.0f);
    }

    public static void resetDraggingItemViewEffects(View view, float f) {
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);
        ViewCompat.setTranslationZ(view, f);
        view.setAlpha(1.0f);
        view.setRotation(0.0f);
        view.setScaleX(1.0f);
        view.setScaleY(1.0f);
    }

    public static void setItemTranslation(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2) {
        RecyclerView.ItemAnimator itemAnimator = recyclerView.getItemAnimator();
        if (itemAnimator != null) {
            itemAnimator.endAnimation(viewHolder);
        }
        viewHolder.itemView.setTranslationX(f);
        viewHolder.itemView.setTranslationY(f2);
    }

    public void setOnAnimatorFinshCallback(AnimtorFinshCallback animtorFinshCallback) {
        this.mAnimFinshCallback = animtorFinshCallback;
    }
}
