package com.miui.gallery.vlog.clip.sort;

import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public interface ItemTouchHelperAdapter {
    void onChildDraw(RecyclerView.ViewHolder viewHolder, boolean z);

    void onMove(int i, int i2);

    void onMoveFinished(RecyclerView.ViewHolder viewHolder, int i);

    void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i);
}
