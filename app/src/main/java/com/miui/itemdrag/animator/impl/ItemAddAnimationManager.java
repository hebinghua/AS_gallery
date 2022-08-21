package com.miui.itemdrag.animator.impl;

import android.util.Log;
import android.view.animation.Interpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.itemdrag.animator.base.BaseItemAnimator;

/* loaded from: classes3.dex */
public abstract class ItemAddAnimationManager extends BaseItemAnimationManager<AddAnimationInfo> {
    public abstract boolean addPendingAnimation(RecyclerView.ViewHolder viewHolder);

    public ItemAddAnimationManager(BaseItemAnimator baseItemAnimator) {
        super(baseItemAnimator);
    }

    public long getDuration() {
        return this.mItemAnimator.getAddDuration();
    }

    public Interpolator getInterpolator() {
        return this.mItemAnimator.getAddInterpolator();
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public void dispatchStarting(AddAnimationInfo addAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        if (debugLogEnabled()) {
            Log.d("ARVItemAddAnimMgr", "dispatchAddStarting(" + viewHolder + ")");
        }
        this.mItemAnimator.dispatchAddStarting(viewHolder);
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public void dispatchFinished(AddAnimationInfo addAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        if (debugLogEnabled()) {
            Log.d("ARVItemAddAnimMgr", "dispatchAddFinished(" + viewHolder + ")");
        }
        this.mItemAnimator.dispatchAddFinished(viewHolder);
    }

    @Override // com.miui.itemdrag.animator.impl.BaseItemAnimationManager
    public boolean endNotStartedAnimation(AddAnimationInfo addAnimationInfo, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.ViewHolder viewHolder2 = addAnimationInfo.holder;
        if (viewHolder2 != null) {
            if (viewHolder != null && viewHolder2 != viewHolder) {
                return false;
            }
            onAnimationEndedBeforeStarted(addAnimationInfo, viewHolder2);
            dispatchFinished(addAnimationInfo, addAnimationInfo.holder);
            addAnimationInfo.clear(addAnimationInfo.holder);
            return true;
        }
        return false;
    }
}
