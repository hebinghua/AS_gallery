package com.miui.itemdrag.animator.base;

import android.view.animation.Interpolator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

/* loaded from: classes3.dex */
public abstract class BaseItemAnimator extends SimpleItemAnimator {
    public Interpolator mAddInterpolator;
    public Interpolator mChangeInterpolator;
    public ItemAnimatorListener mListener;
    public Interpolator mMoveInterpolator;
    public Interpolator mRemoveInterpolator;

    /* loaded from: classes3.dex */
    public interface ItemAnimatorListener {
        void onAddFinished(RecyclerView.ViewHolder viewHolder);

        void onChangeFinished(RecyclerView.ViewHolder viewHolder);

        void onMoveFinished(RecyclerView.ViewHolder viewHolder);

        void onRemoveFinished(RecyclerView.ViewHolder viewHolder);
    }

    public abstract boolean debugLogEnabled();

    public void onAddFinishedImpl(RecyclerView.ViewHolder viewHolder) {
    }

    public void onAddStartingImpl(RecyclerView.ViewHolder viewHolder) {
    }

    public void onChangeFinishedImpl(RecyclerView.ViewHolder viewHolder, boolean z) {
    }

    public void onChangeStartingItem(RecyclerView.ViewHolder viewHolder, boolean z) {
    }

    public void onMoveFinishedImpl(RecyclerView.ViewHolder viewHolder) {
    }

    public void onMoveStartingImpl(RecyclerView.ViewHolder viewHolder) {
    }

    public void onRemoveFinishedImpl(RecyclerView.ViewHolder viewHolder) {
    }

    public void onRemoveStartingImpl(RecyclerView.ViewHolder viewHolder) {
    }

    public void setMoveInterpolator(Interpolator interpolator) {
        this.mMoveInterpolator = interpolator;
    }

    public void setAddInterpolator(Interpolator interpolator) {
        this.mAddInterpolator = interpolator;
    }

    public void setRemoveInterpolator(Interpolator interpolator) {
        this.mRemoveInterpolator = interpolator;
    }

    public void setChangeInterpolator(Interpolator interpolator) {
        this.mChangeInterpolator = interpolator;
    }

    public Interpolator getMoveInterpolator() {
        return this.mMoveInterpolator;
    }

    public Interpolator getAddInterpolator() {
        return this.mAddInterpolator;
    }

    public Interpolator getRemoveInterpolator() {
        return this.mRemoveInterpolator;
    }

    public Interpolator getChangeInterpolator() {
        return this.mChangeInterpolator;
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void onAddStarting(RecyclerView.ViewHolder viewHolder) {
        onAddStartingImpl(viewHolder);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void onAddFinished(RecyclerView.ViewHolder viewHolder) {
        onAddFinishedImpl(viewHolder);
        ItemAnimatorListener itemAnimatorListener = this.mListener;
        if (itemAnimatorListener != null) {
            itemAnimatorListener.onAddFinished(viewHolder);
        }
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void onChangeStarting(RecyclerView.ViewHolder viewHolder, boolean z) {
        onChangeStartingItem(viewHolder, z);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void onChangeFinished(RecyclerView.ViewHolder viewHolder, boolean z) {
        onChangeFinishedImpl(viewHolder, z);
        ItemAnimatorListener itemAnimatorListener = this.mListener;
        if (itemAnimatorListener != null) {
            itemAnimatorListener.onChangeFinished(viewHolder);
        }
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void onMoveStarting(RecyclerView.ViewHolder viewHolder) {
        onMoveStartingImpl(viewHolder);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void onMoveFinished(RecyclerView.ViewHolder viewHolder) {
        onMoveFinishedImpl(viewHolder);
        ItemAnimatorListener itemAnimatorListener = this.mListener;
        if (itemAnimatorListener != null) {
            itemAnimatorListener.onMoveFinished(viewHolder);
        }
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void onRemoveStarting(RecyclerView.ViewHolder viewHolder) {
        onRemoveStartingImpl(viewHolder);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void onRemoveFinished(RecyclerView.ViewHolder viewHolder) {
        onRemoveFinishedImpl(viewHolder);
        ItemAnimatorListener itemAnimatorListener = this.mListener;
        if (itemAnimatorListener != null) {
            itemAnimatorListener.onRemoveFinished(viewHolder);
        }
    }

    public boolean dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
            return true;
        }
        return false;
    }
}
