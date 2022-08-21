package com.miui.gallery.ui.album.main;

import android.graphics.Rect;
import android.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.album.main.AlbumTabContract$P;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment;

/* loaded from: classes2.dex */
public abstract class AlbumTabContract$V<PRESENTER extends AlbumTabContract$P> extends BaseAlbumTabFragment<PRESENTER> {
    public abstract boolean canDrag(RecyclerView.ViewHolder viewHolder, int i, int i2);

    public abstract void changeDragStatus(boolean z, boolean z2);

    public abstract void closeItemSwapWhenDragMode();

    public abstract Pair<Integer, Rect> findAdjacentItemByPoint(RecyclerView recyclerView, int i, int i2);

    public abstract int getCheckedCount();

    public abstract long[] getCheckedItemIds();

    public abstract int[] getCheckedItemOrderedPositions();

    public abstract int getCurrentListVisiblePosition();

    public abstract int getTouchSlop();

    public abstract boolean isInChoiceMode();

    public abstract boolean isInMoveMode();

    public void onBeforeDragItemStart() {
    }

    public abstract void onChangeHeadGroupEmptyStatus(boolean z);

    public abstract void onFillItemWhenEmptyHeadGroup(int i, int i2);

    public void onFirstMoveWhenDragItem() {
    }

    public abstract void onMoveAlbumFailed(int i);

    public abstract void onStartChoiceMode();

    public abstract void onStopChoiceMode();

    public abstract void openDragMode(boolean z);

    public abstract void openItemSwapWhenDragMode();
}
