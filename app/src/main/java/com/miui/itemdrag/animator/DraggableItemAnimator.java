package com.miui.itemdrag.animator;

import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class DraggableItemAnimator extends RefactoredDefaultItemAnimator {
    @Override // com.miui.itemdrag.animator.base.GeneralItemAnimator, androidx.recyclerview.widget.SimpleItemAnimator
    public boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
        if (viewHolder == viewHolder2 && i == i3 && i2 == i4) {
            dispatchChangeFinished(viewHolder, true);
            return false;
        }
        return super.animateChange(viewHolder, viewHolder2, i, i2, i3, i4);
    }
}
