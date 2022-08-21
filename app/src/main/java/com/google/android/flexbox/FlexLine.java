package com.google.android.flexbox;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class FlexLine {
    public int mCrossSize;
    public int mDividerLengthInMainSize;
    public int mFirstIndex;
    public int mGoneItemCount;
    public int mItemCount;
    public int mLastIndex;
    public int mMainSize;
    public int mMaxBaseline;
    public int mSumCrossSizeBefore;
    public float mTotalFlexGrow;
    public float mTotalFlexShrink;
    public int mLeft = Integer.MAX_VALUE;
    public int mTop = Integer.MAX_VALUE;
    public int mRight = Integer.MIN_VALUE;
    public int mBottom = Integer.MIN_VALUE;
    public List<Integer> mIndicesAlignSelfStretch = new ArrayList();

    public int getCrossSize() {
        return this.mCrossSize;
    }

    public int getItemCount() {
        return this.mItemCount;
    }

    public int getItemCountNotGone() {
        return this.mItemCount - this.mGoneItemCount;
    }
}
