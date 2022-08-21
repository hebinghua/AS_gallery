package com.miui.itemdrag.animator.impl;

import android.util.Log;
import android.view.animation.Interpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.itemdrag.animator.base.BaseItemAnimator;

/* loaded from: classes3.dex */
public abstract class ItemRemoveAnimationManager extends BaseItemAnimationManager<RemoveAnimationInfo> {
    public abstract boolean addPendingAnimation(RecyclerView.ViewHolder viewHolder);

    public ItemRemoveAnimationManager(BaseItemAnimator baseItemAnimator) {
        super(baseItemAnimator);
    }

    public long getDuration() {
        return this.mItemAnimator.getRemoveDuration();
    }

    public Interpolator getInterpolator() {
        return this.mItemAnimator.getRemoveInterpolator();
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public void dispatchStarting(RemoveAnimationInfo removeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        if (debugLogEnabled()) {
            Log.d("ARVItemRemoveAnimMgr", "dispatchRemoveStarting(" + viewHolder + ")");
        }
        this.mItemAnimator.dispatchRemoveStarting(viewHolder);
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public void dispatchFinished(RemoveAnimationInfo removeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        if (debugLogEnabled()) {
            Log.d("ARVItemRemoveAnimMgr", "dispatchRemoveFinished(" + viewHolder + ")");
        }
        this.mItemAnimator.dispatchRemoveFinished(viewHolder);
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public boolean endNotStartedAnimation(RemoveAnimationInfo removeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.ViewHolder viewHolder2 = removeAnimationInfo.holder;
        if (viewHolder2 != null) {
            if (viewHolder != null && viewHolder2 != viewHolder) {
                return false;
            }
            onAnimationEndedBeforeStarted(removeAnimationInfo, viewHolder2);
            dispatchFinished(removeAnimationInfo, removeAnimationInfo.holder);
            removeAnimationInfo.clear(removeAnimationInfo.holder);
            return true;
        }
        return false;
    }
}
