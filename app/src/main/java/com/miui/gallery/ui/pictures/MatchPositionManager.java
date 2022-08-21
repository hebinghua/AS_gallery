package com.miui.gallery.ui.pictures;

import android.graphics.Rect;
import android.view.View;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.IMatchItemAdapter;
import com.miui.gallery.widget.recyclerview.transition.FuzzyMatchItem;

/* loaded from: classes2.dex */
public class MatchPositionManager {
    public static String TAG = "MatchPositionManager";
    public IMatchItemAdapter mAdapter;
    public MatchPositionCallBack mCallBack;
    public FuzzyMatchItem mMatchFromItem;
    public GalleryRecyclerView mRecyclerView;
    public int[] mSwitchMatchPosAndOffset = new int[2];

    /* loaded from: classes2.dex */
    public interface MatchPositionCallBack {
        int[] unpackGroupIndexAndOffset(int i);
    }

    public MatchPositionManager(GalleryRecyclerView galleryRecyclerView, IMatchItemAdapter iMatchItemAdapter, MatchPositionCallBack matchPositionCallBack) {
        this.mRecyclerView = galleryRecyclerView;
        this.mAdapter = iMatchItemAdapter;
        this.mCallBack = matchPositionCallBack;
    }

    public void calculateMatchFromItem(PictureViewMode pictureViewMode) {
        View childAt = this.mRecyclerView.getChildCount() > 0 ? this.mRecyclerView.getChildAt(0) : null;
        if (childAt != null) {
            Rect rect = new Rect();
            int childAdapterPosition = this.mRecyclerView.getChildAdapterPosition(childAt);
            if (childAdapterPosition == -1) {
                DefaultLogger.w(TAG, "can't find the position of firstVisibleItem %s", childAt);
                return;
            }
            childAt.getLocalVisibleRect(rect);
            int i = rect.top;
            if (i > 0) {
                this.mSwitchMatchPosAndOffset[1] = -i;
            } else {
                this.mSwitchMatchPosAndOffset[1] = i;
            }
            int[] unpackGroupIndexAndOffset = this.mCallBack.unpackGroupIndexAndOffset(childAdapterPosition);
            if (unpackGroupIndexAndOffset[1] == -1) {
                unpackGroupIndexAndOffset[1] = 0;
                this.mSwitchMatchPosAndOffset[1] = 0;
            }
            FuzzyMatchItem matchItemByGroupAndChildIndex = this.mAdapter.getMatchItemByGroupAndChildIndex(pictureViewMode, unpackGroupIndexAndOffset[0], unpackGroupIndexAndOffset[1], this.mRecyclerView.getWidth());
            this.mMatchFromItem = matchItemByGroupAndChildIndex;
            DefaultLogger.i(TAG, "mMatchFromItem[%s]", matchItemByGroupAndChildIndex.toString());
        }
    }

    public void scrollToMatchItemPos(PictureViewMode pictureViewMode) {
        FuzzyMatchItem fuzzyMatchToItem;
        FuzzyMatchItem fuzzyMatchItem = this.mMatchFromItem;
        if (fuzzyMatchItem == null || (fuzzyMatchToItem = this.mAdapter.fuzzyMatchToItem(fuzzyMatchItem, pictureViewMode)) == null) {
            return;
        }
        DefaultLogger.i(TAG, "matchToItem[%s]", fuzzyMatchToItem.toString());
        this.mSwitchMatchPosAndOffset[0] = this.mAdapter.calculateViewPosition(pictureViewMode, fuzzyMatchToItem.mPosition);
        DefaultLogger.i(TAG, "mSwitchMatchPosAndOffset[0]=%s,mSwitchMatchPosAndOffset[1]=%s", Integer.valueOf(this.mSwitchMatchPosAndOffset[0]), Integer.valueOf(this.mSwitchMatchPosAndOffset[1]));
        GalleryRecyclerView galleryRecyclerView = this.mRecyclerView;
        int[] iArr = this.mSwitchMatchPosAndOffset;
        galleryRecyclerView.scrollToPositionWithOffset(iArr[0], iArr[1]);
        this.mMatchFromItem = null;
    }

    public void clearMatchItem() {
        this.mMatchFromItem = null;
    }
}
