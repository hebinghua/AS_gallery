package com.miui.itemdrag.animator.impl;

import android.util.Log;
import android.view.animation.Interpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.itemdrag.animator.base.BaseItemAnimator;

/* loaded from: classes3.dex */
public abstract class ItemMoveAnimationManager extends BaseItemAnimationManager<MoveAnimationInfo> {
    public abstract boolean addPendingAnimation(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4);

    public ItemMoveAnimationManager(BaseItemAnimator baseItemAnimator) {
        super(baseItemAnimator);
    }

    public long getDuration() {
        return this.mItemAnimator.getMoveDuration();
    }

    public Interpolator getInterpolator() {
        return this.mItemAnimator.getMoveInterpolator();
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public void dispatchStarting(MoveAnimationInfo moveAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        if (debugLogEnabled()) {
            Log.d("ARVItemMoveAnimMgr", "dispatchMoveStarting(" + viewHolder + ")");
        }
        this.mItemAnimator.dispatchMoveStarting(viewHolder);
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public void dispatchFinished(MoveAnimationInfo moveAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        if (debugLogEnabled()) {
            Log.d("ARVItemMoveAnimMgr", "dispatchMoveFinished(" + viewHolder + ")");
        }
        this.mItemAnimator.dispatchMoveFinished(viewHolder);
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public boolean endNotStartedAnimation(MoveAnimationInfo moveAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.ViewHolder viewHolder2 = moveAnimationInfo.holder;
        if (viewHolder2 != null) {
            if (viewHolder != null && viewHolder2 != viewHolder) {
                return false;
            }
            onAnimationEndedBeforeStarted(moveAnimationInfo, viewHolder2);
            dispatchFinished(moveAnimationInfo, moveAnimationInfo.holder);
            moveAnimationInfo.clear(moveAnimationInfo.holder);
            return true;
        }
        return false;
    }
}
