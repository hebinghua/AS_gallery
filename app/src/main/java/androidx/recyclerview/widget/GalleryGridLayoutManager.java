package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.stat.StatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;

/* loaded from: classes.dex */
public class GalleryGridLayoutManager extends GridLayoutManager {
    public final SparseArray<Rect> mCachedDecorInsets;
    public boolean mShouldAdjustAnchor;

    public GalleryGridLayoutManager(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mCachedDecorInsets = new SparseArray<>();
    }

    public GalleryGridLayoutManager(Context context, int i) {
        super(context, i);
        this.mCachedDecorInsets = new SparseArray<>();
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager
    public void onAnchorReady(RecyclerView.Recycler recycler, RecyclerView.State state, LinearLayoutManager.AnchorInfo anchorInfo, int i) {
        super.onAnchorReady(recycler, state, anchorInfo, i);
        int i2 = 0;
        if (state.isPreLayout()) {
            if (this.mPendingScrollPosition != -1) {
                return;
            }
            if (anchorInfo.mCoordinate != (anchorInfo.mLayoutFromEnd ? this.mOrientationHelper.getEndAfterPadding() : this.mOrientationHelper.getStartAfterPadding())) {
                return;
            }
            int i3 = anchorInfo.mPosition;
            if (getStackFromEnd()) {
                i2 = state.getItemCount() - 1;
            }
            if (i3 != i2) {
                return;
            }
            this.mShouldAdjustAnchor = true;
        } else if (!this.mShouldAdjustAnchor) {
        } else {
            anchorInfo.assignCoordinateFromPadding();
            if (getStackFromEnd()) {
                i2 = state.getItemCount() - 1;
            }
            anchorInfo.mPosition = i2;
        }
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            cacheDecorInsets();
        }
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            DefaultLogger.e("GalleryGridLayoutManager", e);
            RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
            int itemCount = adapter != null ? adapter.getItemCount() : 0;
            HashMap hashMap = new HashMap();
            hashMap.put("error", "countInAdapter:" + itemCount + ", " + e.getMessage());
            StatHelper.recordCountEvent("error_full", "recycler_view_inconsistency", hashMap);
        }
        clearDecorInsets();
    }

    @Override // androidx.recyclerview.widget.GridLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        this.mShouldAdjustAnchor = false;
    }

    public final void cacheDecorInsets() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) getChildAt(i).getLayoutParams();
            this.mCachedDecorInsets.put(layoutParams.getViewLayoutPosition(), new Rect(layoutParams.mDecorInsets));
        }
    }

    public final void clearDecorInsets() {
        this.mCachedDecorInsets.clear();
    }

    public Rect getCachedDecorInsets(int i) {
        return this.mCachedDecorInsets.get(i);
    }
}
