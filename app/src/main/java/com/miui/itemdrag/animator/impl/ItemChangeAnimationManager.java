package com.miui.itemdrag.animator.impl;

import android.util.Log;
import android.view.animation.Interpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.itemdrag.animator.base.BaseItemAnimator;

/* loaded from: classes3.dex */
public abstract class ItemChangeAnimationManager extends BaseItemAnimationManager<ChangeAnimationInfo> {
    public abstract boolean addPendingAnimation(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4);

    public abstract void onCreateChangeAnimationForNewItem(ChangeAnimationInfo changeAnimationInfo);

    public abstract void onCreateChangeAnimationForOldItem(ChangeAnimationInfo changeAnimationInfo);

    public ItemChangeAnimationManager(BaseItemAnimator baseItemAnimator) {
        super(baseItemAnimator);
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public void dispatchStarting(ChangeAnimationInfo changeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        if (debugLogEnabled()) {
            Log.d("ARVItemChangeAnimMgr", "dispatchChangeStarting(" + viewHolder + ")");
        }
        this.mItemAnimator.dispatchChangeStarting(viewHolder, viewHolder == changeAnimationInfo.oldHolder);
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public void dispatchFinished(ChangeAnimationInfo changeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        if (debugLogEnabled()) {
            Log.d("ARVItemChangeAnimMgr", "dispatchChangeFinished(" + viewHolder + ")");
        }
        this.mItemAnimator.dispatchChangeFinished(viewHolder, viewHolder == changeAnimationInfo.oldHolder);
    }

    public long getDuration() {
        return this.mItemAnimator.getChangeDuration();
    }

    public Interpolator getInterpolator() {
        return this.mItemAnimator.getChangeInterpolator();
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public void onCreateAnimation(ChangeAnimationInfo changeAnimationInfo) {
        if (changeAnimationInfo.oldHolder != null) {
            onCreateChangeAnimationForOldItem(changeAnimationInfo);
        }
        if (changeAnimationInfo.newHolder != null) {
            onCreateChangeAnimationForNewItem(changeAnimationInfo);
        }
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public boolean endNotStartedAnimation(ChangeAnimationInfo changeAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.ViewHolder viewHolder2 = changeAnimationInfo.oldHolder;
        if (viewHolder2 != null && (viewHolder == null || viewHolder2 == viewHolder)) {
            onAnimationEndedBeforeStarted(changeAnimationInfo, viewHolder2);
            dispatchFinished(changeAnimationInfo, changeAnimationInfo.oldHolder);
            changeAnimationInfo.clear(changeAnimationInfo.oldHolder);
        }
        RecyclerView.ViewHolder viewHolder3 = changeAnimationInfo.newHolder;
        if (viewHolder3 != null && (viewHolder == null || viewHolder3 == viewHolder)) {
            onAnimationEndedBeforeStarted(changeAnimationInfo, viewHolder3);
            dispatchFinished(changeAnimationInfo, changeAnimationInfo.newHolder);
            changeAnimationInfo.clear(changeAnimationInfo.newHolder);
        }
        return changeAnimationInfo.oldHolder == null && changeAnimationInfo.newHolder == null;
    }
}
