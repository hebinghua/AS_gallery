package com.miui.itemdrag.animator.base;

import android.util.Log;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.itemdrag.animator.impl.ItemAddAnimationManager;
import com.miui.itemdrag.animator.impl.ItemChangeAnimationManager;
import com.miui.itemdrag.animator.impl.ItemMoveAnimationManager;
import com.miui.itemdrag.animator.impl.ItemRemoveAnimationManager;

/* loaded from: classes3.dex */
public abstract class GeneralItemAnimator extends BaseItemAnimator {
    public ItemAddAnimationManager mAddAnimationsManager;
    public ItemChangeAnimationManager mChangeAnimationsManager;
    public boolean mDebug;
    public ItemMoveAnimationManager mMoveAnimationsManager;
    public ItemRemoveAnimationManager mRemoveAnimationManager;
    public long mMovesDelayDuration = -1;
    public long mChangesDelayDuration = -1;
    public long mAddsDelayDuration = -1;

    public abstract void onSchedulePendingAnimations();

    public abstract void onSetup();

    public GeneralItemAnimator() {
        setup();
    }

    public final void setup() {
        onSetup();
        if (this.mRemoveAnimationManager == null || this.mAddAnimationsManager == null || this.mChangeAnimationsManager == null || this.mMoveAnimationsManager == null) {
            throw new IllegalStateException("setup incomplete");
        }
    }

    public void setMovesDelayDuration(long j) {
        this.mMovesDelayDuration = j;
    }

    public void setChangesDelayDuration(long j) {
        this.mChangesDelayDuration = j;
    }

    public void setAddsDelayDuration(long j) {
        this.mAddsDelayDuration = j;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void runPendingAnimations() {
        if (!hasPendingAnimations()) {
            return;
        }
        onSchedulePendingAnimations();
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateRemove(RecyclerView.ViewHolder viewHolder) {
        if (this.mDebug) {
            Log.d("ARVGeneralItemAnimator", "animateRemove(id = " + viewHolder.getItemId() + ", position = " + viewHolder.getLayoutPosition() + ")");
        }
        return this.mRemoveAnimationManager.addPendingAnimation(viewHolder);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
        if (this.mDebug) {
            Log.d("ARVGeneralItemAnimator", "animateAdd(id = " + viewHolder.getItemId() + ", position = " + viewHolder.getLayoutPosition() + ")");
        }
        return this.mAddAnimationsManager.addPendingAnimation(viewHolder);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateMove(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
        if (this.mDebug) {
            Log.d("ARVGeneralItemAnimator", "animateMove(id = " + viewHolder.getItemId() + ", position = " + viewHolder.getLayoutPosition() + ", fromX = " + i + ", fromY = " + i2 + ", toX = " + i3 + ", toY = " + i4 + ")");
        }
        return this.mMoveAnimationsManager.addPendingAnimation(viewHolder, i, i2, i3, i4);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
        if (viewHolder == viewHolder2) {
            return this.mMoveAnimationsManager.addPendingAnimation(viewHolder, i, i2, i3, i4);
        }
        if (this.mDebug) {
            String str = "-";
            String l = viewHolder != null ? Long.toString(viewHolder.getItemId()) : str;
            String l2 = viewHolder != null ? Long.toString(viewHolder.getLayoutPosition()) : str;
            String l3 = viewHolder2 != null ? Long.toString(viewHolder2.getItemId()) : str;
            if (viewHolder2 != null) {
                str = Long.toString(viewHolder2.getLayoutPosition());
            }
            Log.d("ARVGeneralItemAnimator", "animateChange(old.id = " + l + ", old.position = " + l2 + ", new.id = " + l3 + ", new.position = " + str + ", fromX = " + i + ", fromY = " + i2 + ", toX = " + i3 + ", toY = " + i4 + ")");
        }
        return this.mChangeAnimationsManager.addPendingAnimation(viewHolder, viewHolder2, i, i2, i3, i4);
    }

    public void cancelAnimations(RecyclerView.ViewHolder viewHolder) {
        ViewCompat.animate(viewHolder.itemView).cancel();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimation(RecyclerView.ViewHolder viewHolder) {
        cancelAnimations(viewHolder);
        this.mMoveAnimationsManager.endPendingAnimations(viewHolder);
        this.mChangeAnimationsManager.endPendingAnimations(viewHolder);
        this.mRemoveAnimationManager.endPendingAnimations(viewHolder);
        this.mAddAnimationsManager.endPendingAnimations(viewHolder);
        this.mMoveAnimationsManager.endDeferredReadyAnimations(viewHolder);
        this.mChangeAnimationsManager.endDeferredReadyAnimations(viewHolder);
        this.mRemoveAnimationManager.endDeferredReadyAnimations(viewHolder);
        this.mAddAnimationsManager.endDeferredReadyAnimations(viewHolder);
        if (this.mRemoveAnimationManager.removeFromActive(viewHolder) && this.mDebug) {
            throw new IllegalStateException("after animation is cancelled, item should not be in the active animation list [remove]");
        }
        if (this.mAddAnimationsManager.removeFromActive(viewHolder) && this.mDebug) {
            throw new IllegalStateException("after animation is cancelled, item should not be in the active animation list [add]");
        }
        if (this.mChangeAnimationsManager.removeFromActive(viewHolder) && this.mDebug) {
            throw new IllegalStateException("after animation is cancelled, item should not be in the active animation list [change]");
        }
        if (this.mMoveAnimationsManager.removeFromActive(viewHolder) && this.mDebug) {
            throw new IllegalStateException("after animation is cancelled, item should not be in the active animation list [move]");
        }
        dispatchFinishedWhenDone();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public boolean isRunning() {
        return this.mRemoveAnimationManager.isRunning() || this.mAddAnimationsManager.isRunning() || this.mChangeAnimationsManager.isRunning() || this.mMoveAnimationsManager.isRunning();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public void endAnimations() {
        this.mMoveAnimationsManager.endAllPendingAnimations();
        this.mRemoveAnimationManager.endAllPendingAnimations();
        this.mAddAnimationsManager.endAllPendingAnimations();
        this.mChangeAnimationsManager.endAllPendingAnimations();
        if (!isRunning()) {
            return;
        }
        this.mMoveAnimationsManager.endAllDeferredReadyAnimations();
        this.mAddAnimationsManager.endAllDeferredReadyAnimations();
        this.mChangeAnimationsManager.endAllDeferredReadyAnimations();
        this.mRemoveAnimationManager.cancelAllStartedAnimations();
        this.mMoveAnimationsManager.cancelAllStartedAnimations();
        this.mAddAnimationsManager.cancelAllStartedAnimations();
        this.mChangeAnimationsManager.cancelAllStartedAnimations();
        dispatchAnimationsFinished();
    }

    @Override // com.miui.itemdrag.animator.base.BaseItemAnimator
    public boolean debugLogEnabled() {
        return this.mDebug;
    }

    @Override // com.miui.itemdrag.animator.base.BaseItemAnimator
    public boolean dispatchFinishedWhenDone() {
        if (this.mDebug && !isRunning()) {
            Log.d("ARVGeneralItemAnimator", "dispatchFinishedWhenDone()");
        }
        return super.dispatchFinishedWhenDone();
    }

    public boolean hasPendingAnimations() {
        return this.mRemoveAnimationManager.hasPending() || this.mMoveAnimationsManager.hasPending() || this.mChangeAnimationsManager.hasPending() || this.mAddAnimationsManager.hasPending();
    }

    public void setItemRemoveAnimationManager(ItemRemoveAnimationManager itemRemoveAnimationManager) {
        this.mRemoveAnimationManager = itemRemoveAnimationManager;
    }

    public void setItemAddAnimationsManager(ItemAddAnimationManager itemAddAnimationManager) {
        this.mAddAnimationsManager = itemAddAnimationManager;
    }

    public void setItemChangeAnimationsManager(ItemChangeAnimationManager itemChangeAnimationManager) {
        this.mChangeAnimationsManager = itemChangeAnimationManager;
    }

    public void setItemMoveAnimationsManager(ItemMoveAnimationManager itemMoveAnimationManager) {
        this.mMoveAnimationsManager = itemMoveAnimationManager;
    }

    public void schedulePendingAnimationsByDefaultRule() {
        boolean hasPending = this.mRemoveAnimationManager.hasPending();
        boolean hasPending2 = this.mMoveAnimationsManager.hasPending();
        boolean hasPending3 = this.mChangeAnimationsManager.hasPending();
        boolean hasPending4 = this.mAddAnimationsManager.hasPending();
        long j = 0;
        long removeDuration = hasPending ? getRemoveDuration() : 0L;
        long moveDuration = hasPending2 ? getMoveDuration() : 0L;
        long changeDuration = hasPending3 ? getChangeDuration() : 0L;
        if (hasPending) {
            this.mRemoveAnimationManager.runPendingAnimations(false, 0L);
        }
        if (hasPending2) {
            long j2 = this.mMovesDelayDuration;
            if (j2 < 0) {
                j2 = removeDuration;
            }
            this.mMoveAnimationsManager.runPendingAnimations(hasPending, j2);
        }
        if (hasPending3) {
            long j3 = this.mChangesDelayDuration;
            if (j3 < 0) {
                j3 = removeDuration;
            }
            this.mChangeAnimationsManager.runPendingAnimations(hasPending, j3);
        }
        if (hasPending4) {
            boolean z = hasPending || hasPending2 || hasPending3;
            long max = removeDuration + Math.max(moveDuration, changeDuration);
            long j4 = this.mAddsDelayDuration;
            if (j4 < 0) {
                if (z) {
                    j = max;
                }
                j4 = j;
            }
            this.mAddAnimationsManager.runPendingAnimations(z, j4);
        }
    }
}
