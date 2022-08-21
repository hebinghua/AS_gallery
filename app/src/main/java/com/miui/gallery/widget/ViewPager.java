package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import com.baidu.platform.comapi.UIMsg;
import com.miui.gallery.R$styleable;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.slip.ISlipAnimView;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ViewPager extends ViewGroup implements ISlipAnimView {
    public static final Comparator<ItemInfo> COMPARATOR;
    public static final boolean DEBUG = Log.isLoggable("ViewPager", 3);
    public static final int DEFAULT_MORE_OFFSCREEN_PAGES;
    public static final int[] LAYOUT_ATTRS;
    public static final Interpolator sInterpolator;
    public boolean isPreload;
    public int mActivePointerId;
    public PagerAdapter mAdapter;
    public OnAdapterChangeListener mAdapterChangeListener;
    public float mBottomMarginProgress;
    public int mBottomPageBounds;
    public boolean mCalledSuper;
    public int mChildHeightMeasureSpec;
    public int mChildWidthMeasureSpec;
    public int mCloseEnough;
    public int mCurItem;
    public int mDecorChildCount;
    public int mDefaultGutterSize;
    public boolean mDragEnabled;
    public final Runnable mEndScrollRunnable;
    public boolean mFakeDragging;
    public boolean mFirstLayout;
    public float mFirstOffset;
    public float mFirstOffsetLeftScreen;
    public int mFlingDistance;
    public boolean mForceReplayout;
    public int mGutterSize;
    public float mHeightSlipRatio;
    public boolean mInLayout;
    public int mInitialHeight;
    public float mInitialMotionX;
    public int mInitialPageMargin;
    public int mInitialWidth;
    public OnPageChangeListener mInternalPageChangeListener;
    public boolean mIsBeingDragged;
    public boolean mIsLeftScrolled;
    public boolean mIsUnableToDrag;
    public final ArrayList<ItemInfo> mItems;
    public float mLastMotionX;
    public float mLastMotionY;
    public float mLastOffset;
    public float mLastOffsetLeftScreen;
    public int mLastOrientation;
    public float mLastPageOffset;
    public int mLastSettledItem;
    public EdgeEffect mLeftEdge;
    public int mLeftOffscreenPageLimit;
    public Drawable mMarginDrawable;
    public float mMarginSlipRatio;
    public int mMaximumVelocity;
    public int mMinimumVelocity;
    public boolean mNeedCalculatePageOffsets;
    public PagerObserver mObserver;
    public OnPageChangeListener mOnPageChangeListener;
    public int mPageMargin;
    public OnPageSettledListener mPageSettledListener;
    public boolean mPopulatePending;
    public int mPreviousItem;
    public Parcelable mRestoredAdapterState;
    public int mRestoredCurItem;
    public int mReverseVelocity;
    public EdgeEffect mRightEdge;
    public int mRightOffscreenPageLimit;
    public int mScrollState;
    public Scroller mScroller;
    public boolean mScrollingCacheEnabled;
    public float mSlipProgress;
    public int mSlippedHeight;
    public final ItemInfo mTempItem;
    public final Rect mTempRect;
    public int mTopPageBounds;
    public int mTouchSlop;
    public VelocityTracker mVelocityTracker;
    public float mWidthSlipRatio;

    /* loaded from: classes2.dex */
    public static class ItemInfo {
        public Object object;
        public float offset;
        public float offsetLeftScreen;
        public float offsetTopScreen;
        public int position;
        public boolean scrolling;
        public float widthFactor;
    }

    /* loaded from: classes2.dex */
    public interface OnAdapterChangeListener {
        void onAdapterChanged(PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2);
    }

    /* loaded from: classes2.dex */
    public interface OnPageChangeListener {
        void onPageScrollStateChanged(int i);

        void onPageScrolled(int i, float f, int i2);

        void onPageSelected(int i);
    }

    /* loaded from: classes2.dex */
    public interface OnPageSettledListener {
        void onPageSettled(int i);
    }

    public static /* synthetic */ void $r8$lambda$4xifaSmwbSRl1VBek7ejuX6MCiw(ViewPager viewPager, boolean z) {
        viewPager.lambda$completeScroll$0(z);
    }

    static {
        int i = 3;
        if (BaseBuildUtil.isLowRamDevice()) {
            i = 1;
        }
        DEFAULT_MORE_OFFSCREEN_PAGES = i;
        LAYOUT_ATTRS = new int[]{16842931};
        COMPARATOR = new Comparator<ItemInfo>() { // from class: com.miui.gallery.widget.ViewPager.1
            @Override // java.util.Comparator
            public int compare(ItemInfo itemInfo, ItemInfo itemInfo2) {
                return itemInfo.position - itemInfo2.position;
            }
        };
        sInterpolator = new Interpolator() { // from class: com.miui.gallery.widget.ViewPager.2
            @Override // android.animation.TimeInterpolator
            public float getInterpolation(float f) {
                float f2 = f - 1.0f;
                return (f2 * f2 * f2 * f2 * f2) + 1.0f;
            }
        };
    }

    public ViewPager(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ViewPager(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mItems = new ArrayList<>();
        this.mTempItem = new ItemInfo();
        this.mTempRect = new Rect();
        this.mRestoredCurItem = -1;
        this.mRestoredAdapterState = null;
        this.mFirstOffset = -3.4028235E38f;
        this.mLastOffset = Float.MAX_VALUE;
        this.mFirstOffsetLeftScreen = 0.0f;
        this.mLastOffsetLeftScreen = 0.0f;
        this.mLeftOffscreenPageLimit = 1;
        this.mRightOffscreenPageLimit = DEFAULT_MORE_OFFSCREEN_PAGES;
        this.mIsLeftScrolled = true;
        this.mActivePointerId = -1;
        this.mFirstLayout = true;
        this.mNeedCalculatePageOffsets = false;
        this.mDragEnabled = true;
        this.mLastPageOffset = 0.0f;
        this.mEndScrollRunnable = new Runnable() { // from class: com.miui.gallery.widget.ViewPager.3
            {
                ViewPager.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                ViewPager.this.populate();
            }
        };
        this.mScrollState = 0;
        this.mInitialWidth = 0;
        this.mInitialHeight = 0;
        this.mInitialPageMargin = this.mPageMargin;
        this.mSlipProgress = 0.0f;
        this.mWidthSlipRatio = 1.0f;
        this.mHeightSlipRatio = 1.0f;
        this.mMarginSlipRatio = 1.0f;
        this.mLastOrientation = 0;
        initViewPager(context, attributeSet, 0);
    }

    public void initViewPager(Context context, AttributeSet attributeSet, int i) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ViewPager, i, 0);
            this.mLeftOffscreenPageLimit = obtainStyledAttributes.getInteger(1, 1);
            this.mRightOffscreenPageLimit = obtainStyledAttributes.getInteger(3, DEFAULT_MORE_OFFSCREEN_PAGES);
            this.mWidthSlipRatio = obtainStyledAttributes.getFloat(4, 1.0f);
            this.mHeightSlipRatio = obtainStyledAttributes.getFloat(0, 1.0f);
            this.mMarginSlipRatio = obtainStyledAttributes.getFloat(2, 1.0f);
            obtainStyledAttributes.recycle();
        }
        setWillNotDraw(false);
        setDescendantFocusability(nexEngine.ExportHEVCMainTierLevel52);
        setFocusable(true);
        this.mScroller = new Scroller(context, sInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        int scaledMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMaximumVelocity = scaledMaximumFlingVelocity;
        this.mReverseVelocity = Math.max(this.mMinimumVelocity * 10, scaledMaximumFlingVelocity / 10);
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        float f = context.getResources().getDisplayMetrics().density;
        this.mFlingDistance = (int) (10.0f * f);
        this.mCloseEnough = (int) (2.0f * f);
        this.mDefaultGutterSize = (int) (f * 16.0f);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    private void setScrollState(int i) {
        if (this.mScrollState == i) {
            return;
        }
        this.mScrollState = i;
        OnPageChangeListener onPageChangeListener = this.mOnPageChangeListener;
        if (onPageChangeListener != null) {
            onPageChangeListener.onPageScrollStateChanged(i);
        }
        OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
        if (onPageChangeListener2 == null) {
            return;
        }
        onPageChangeListener2.onPageScrollStateChanged(i);
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        PagerAdapter pagerAdapter2 = this.mAdapter;
        if (pagerAdapter2 != null) {
            pagerAdapter2.unregisterDataSetObserver(this.mObserver);
            this.mAdapter.startUpdate((ViewGroup) this);
            Iterator<ItemInfo> it = this.mItems.iterator();
            while (it.hasNext()) {
                ItemInfo next = it.next();
                this.mAdapter.destroyItem(this, next.position, next.object);
            }
            this.mAdapter.finishUpdate((ViewGroup) this);
            this.mItems.clear();
            removeNonDecorViews();
            setCurrentItemValue(0);
            scrollTo(0, 0);
        }
        PagerAdapter pagerAdapter3 = this.mAdapter;
        this.mAdapter = pagerAdapter;
        if (pagerAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver();
            }
            this.mAdapter.registerDataSetObserver(this.mObserver);
            this.mPopulatePending = false;
            this.mFirstLayout = true;
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, null);
                setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
            } else {
                populate();
            }
        }
        OnAdapterChangeListener onAdapterChangeListener = this.mAdapterChangeListener;
        if (onAdapterChangeListener == null || pagerAdapter3 == pagerAdapter) {
            return;
        }
        onAdapterChangeListener.onAdapterChanged(pagerAdapter3, pagerAdapter);
    }

    public final void removeNonDecorViews() {
        int i = 0;
        while (i < getChildCount()) {
            if (!((LayoutParams) getChildAt(i).getLayoutParams()).isDecor) {
                removeViewAt(i);
                i--;
            }
            i++;
        }
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setOnAdapterChangeListener(OnAdapterChangeListener onAdapterChangeListener) {
        this.mAdapterChangeListener = onAdapterChangeListener;
    }

    public void setCurrentItem(int i) {
        this.mPopulatePending = false;
        setCurrentItemInternal(i, !this.mFirstLayout, false);
    }

    public void setCurrentItem(int i, boolean z) {
        this.mPopulatePending = false;
        setCurrentItemInternal(i, z, false);
    }

    private void setCurrentItemValue(int i) {
        this.mPreviousItem = this.mCurItem;
        this.mCurItem = i;
    }

    public int getCurrentItem() {
        return this.mCurItem;
    }

    public int getPreviousItem() {
        return this.mPreviousItem;
    }

    public boolean isLeftScrolled() {
        return this.mIsLeftScrolled;
    }

    public void setCurrentItemInternal(int i, boolean z, boolean z2) {
        setCurrentItemInternal(i, z, z2, 0);
    }

    private float getMinScrollOffset() {
        return this.mFirstOffset - this.mFirstOffsetLeftScreen;
    }

    private float getMaxScrollOffset() {
        return this.mLastOffset - this.mLastOffsetLeftScreen;
    }

    public final void callBackPageSettled() {
        int i = this.mCurItem;
        this.mLastSettledItem = i;
        OnPageSettledListener onPageSettledListener = this.mPageSettledListener;
        if (onPageSettledListener != null) {
            onPageSettledListener.onPageSettled(i);
        }
    }

    public void setCurrentItemInternal(int i, boolean z, boolean z2, int i2) {
        OnPageChangeListener onPageChangeListener;
        OnPageChangeListener onPageChangeListener2;
        OnPageChangeListener onPageChangeListener3;
        OnPageChangeListener onPageChangeListener4;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || pagerAdapter.getCount() <= 0) {
            setScrollingCacheEnabled(false);
        } else if (!z2 && this.mCurItem == i && this.mItems.size() != 0) {
            setScrollingCacheEnabled(false);
        } else {
            boolean z3 = true;
            if (i < 0) {
                i = 0;
            } else if (i >= this.mAdapter.getCount()) {
                i = this.mAdapter.getCount() - 1;
            }
            if (i > this.mCurItem + getRightOffscreenPageLimit() || i < this.mCurItem - getLeftOffscreenPageLimit()) {
                Iterator<ItemInfo> it = this.mItems.iterator();
                while (it.hasNext()) {
                    it.next().scrolling = true;
                }
            }
            if (this.mCurItem == i) {
                z3 = false;
            }
            populate(i);
            ItemInfo infoForPosition = infoForPosition(i);
            int width = infoForPosition != null ? (int) (getWidth() * Math.max(getMinScrollOffset(), Math.min(infoForPosition.offset - infoForPosition.offsetLeftScreen, getMaxScrollOffset()))) : 0;
            if (z) {
                smoothScrollTo(width, 0, i2);
                if (z3 && (onPageChangeListener4 = this.mOnPageChangeListener) != null) {
                    onPageChangeListener4.onPageSelected(i);
                }
                if (!z3 || (onPageChangeListener3 = this.mInternalPageChangeListener) == null) {
                    return;
                }
                onPageChangeListener3.onPageSelected(i);
                return;
            }
            if (z3 && (onPageChangeListener2 = this.mOnPageChangeListener) != null) {
                onPageChangeListener2.onPageSelected(i);
            }
            if (z3 && (onPageChangeListener = this.mInternalPageChangeListener) != null) {
                onPageChangeListener.onPageSelected(i);
            }
            completeScroll(false);
            scrollTo(width, 0);
            callBackPageSettled();
        }
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setOnPageSettledListener(OnPageSettledListener onPageSettledListener) {
        this.mPageSettledListener = onPageSettledListener;
    }

    public int getLeftOffscreenPageLimit() {
        return this.mLeftOffscreenPageLimit;
    }

    public int getRightOffscreenPageLimit() {
        return this.mRightOffscreenPageLimit;
    }

    public void setLeftOffscreenPageLimit(int i) {
        if (i != this.mLeftOffscreenPageLimit) {
            this.mLeftOffscreenPageLimit = i;
            populate();
        }
    }

    public void setRightOffscreenPageLimit(int i) {
        if (i != this.mRightOffscreenPageLimit) {
            this.mRightOffscreenPageLimit = i;
            populate();
        }
    }

    public void setPageMargin(int i) {
        int i2 = this.mPageMargin;
        this.mPageMargin = i;
        this.mInitialPageMargin = i;
        int width = getWidth();
        recomputeScrollPosition(width, width, i, i2);
        requestLayout();
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    public void setPageMarginDrawable(Drawable drawable) {
        setPageMarginDrawable(drawable, true);
    }

    public void setPageMarginDrawable(Drawable drawable, boolean z) {
        this.mMarginDrawable = drawable;
        if (drawable != null) {
            refreshDrawableState();
        }
        setWillNotDraw(drawable == null);
        if (z) {
            invalidate();
        }
    }

    public void setPageMarginDrawable(int i) {
        setPageMarginDrawable(getContext().getResources().getDrawable(i));
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.mMarginDrawable;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mMarginDrawable;
        if (drawable == null || !drawable.isStateful()) {
            return;
        }
        drawable.setState(getDrawableState());
    }

    public float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((float) ((f - 0.5f) * 0.4712389167638204d));
    }

    public void smoothScrollTo(int i, int i2, int i3) {
        int abs;
        if (getChildCount() == 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int i4 = i - scrollX;
        int i5 = i2 - scrollY;
        if (i4 == 0 && i5 == 0) {
            completeScroll(false);
            populate();
            setScrollState(0);
            callBackPageSettled();
            return;
        }
        setScrollingCacheEnabled(true);
        setScrollState(2);
        int width = getWidth();
        int i6 = width / 2;
        float f = width;
        float f2 = i6;
        float distanceInfluenceForSnapDuration = f2 + (distanceInfluenceForSnapDuration(Math.min(1.0f, (Math.abs(i4) * 1.0f) / f)) * f2);
        int abs2 = Math.abs(i3);
        if (abs2 > 0) {
            abs = Math.round(Math.abs(distanceInfluenceForSnapDuration / abs2) * 1000.0f) * 4;
        } else {
            abs = (int) (((Math.abs(i4) / ((f * this.mAdapter.getPageWidth(this.mCurItem)) + this.mPageMargin)) + 1.0f) * 100.0f);
        }
        this.mScroller.startScroll(scrollX, scrollY, i4, i5, Math.min(abs, (int) UIMsg.MSG_MAP_PANO_DATA));
        postInvalidateOnAnimation();
    }

    public ItemInfo addNewItem(int i, int i2) {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = i;
        itemInfo.object = this.mAdapter.instantiateItem(this, i);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(i);
        if (i2 < 0 || i2 >= this.mItems.size()) {
            this.mItems.add(itemInfo);
        } else {
            this.mItems.add(i2, itemInfo);
        }
        return itemInfo;
    }

    public void dataSetChanged() {
        boolean z = this.mItems.size() < (getLeftOffscreenPageLimit() + getRightOffscreenPageLimit()) + 1 && this.mItems.size() < this.mAdapter.getCount();
        int i = this.mCurItem;
        int i2 = 0;
        boolean z2 = false;
        while (i2 < this.mItems.size()) {
            ItemInfo itemInfo = this.mItems.get(i2);
            int itemPosition = this.mAdapter.getItemPosition(itemInfo.object, itemInfo.position);
            if (itemPosition != -1) {
                if (itemPosition == -3) {
                    refreshItem(i2);
                } else {
                    if (itemPosition == -2) {
                        this.mItems.remove(i2);
                        i2--;
                        if (!z2) {
                            this.mAdapter.startUpdate((ViewGroup) this);
                            z2 = true;
                        }
                        this.mAdapter.destroyItem(this, itemInfo.position, itemInfo.object);
                        int i3 = this.mCurItem;
                        if (i3 == itemInfo.position) {
                            i = Math.max(0, Math.min(i3, this.mAdapter.getCount() - 1));
                        }
                    } else {
                        int i4 = itemInfo.position;
                        if (i4 != itemPosition) {
                            if (i4 == this.mCurItem) {
                                i = itemPosition;
                            }
                            itemInfo.position = itemPosition;
                        }
                    }
                    z = true;
                }
            }
            i2++;
        }
        if (z2) {
            this.mAdapter.finishUpdate((ViewGroup) this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (z) {
            int childCount = getChildCount();
            for (int i5 = 0; i5 < childCount; i5++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i5).getLayoutParams();
                if (!layoutParams.isDecor) {
                    layoutParams.widthFactor = 0.0f;
                }
            }
            setCurrentItemInternal(i, false, true);
            requestLayout();
        }
    }

    public void populate() {
        populate(this.mCurItem);
    }

    public void enablePreload() {
        this.isPreload = true;
        populate();
    }

    public final void refreshItem(int i) {
        ItemInfo itemInfo;
        if (this.mAdapter != null && i >= 0 && i < this.mItems.size() && (itemInfo = this.mItems.get(i)) != null) {
            this.mAdapter.refreshItem(itemInfo.object, itemInfo.position);
        }
    }

    public Object getItem(int i) {
        Iterator<ItemInfo> it = this.mItems.iterator();
        while (it.hasNext()) {
            ItemInfo next = it.next();
            if (next.position == i) {
                return next.object;
            }
        }
        return null;
    }

    public final Object getItemByNativeIndex(int i) {
        ItemInfo itemInfo;
        if (i < 0 || i >= this.mItems.size() || (itemInfo = this.mItems.get(i)) == null) {
            return null;
        }
        return itemInfo.object;
    }

    public final int getActiveCount() {
        return this.mItems.size();
    }

    /* JADX WARN: Code restructure failed: missing block: B:135:0x007b, code lost:
        if (r9 == r10) goto L34;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void populate(int r12) {
        /*
            Method dump skipped, instructions count: 357
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.ViewPager.populate(int):void");
    }

    public final int populateLeft(ItemInfo itemInfo, int i, int i2) {
        int i3 = i - 1;
        ItemInfo itemInfo2 = i3 >= 0 ? this.mItems.get(i3) : null;
        float f = 2.0f - itemInfo.widthFactor;
        float f2 = 0.0f;
        for (int i4 = this.mCurItem - 1; i4 >= 0; i4--) {
            if (f2 < f || i4 >= i2) {
                if (itemInfo2 != null && i4 == itemInfo2.position) {
                    f2 += itemInfo2.widthFactor;
                    i3--;
                    if (i3 >= 0) {
                        itemInfo2 = this.mItems.get(i3);
                    }
                    itemInfo2 = null;
                } else {
                    f2 += addNewItem(i4, i3 + 1).widthFactor;
                    i++;
                    if (i3 >= 0) {
                        itemInfo2 = this.mItems.get(i3);
                    }
                    itemInfo2 = null;
                }
            } else if (itemInfo2 == null) {
                break;
            } else if (i4 == itemInfo2.position && !itemInfo2.scrolling) {
                this.mItems.remove(i3);
                this.mAdapter.destroyItem(this, i4, itemInfo2.object);
                i3--;
                i--;
                if (i3 >= 0) {
                    itemInfo2 = this.mItems.get(i3);
                }
                itemInfo2 = null;
            }
        }
        return i;
    }

    public final int populateRight(ItemInfo itemInfo, int i, int i2) {
        float f = itemInfo.widthFactor;
        int i3 = i + 1;
        int count = this.mAdapter.getCount();
        if (f < 2.0f) {
            ItemInfo itemInfo2 = i3 < this.mItems.size() ? this.mItems.get(i3) : null;
            int i4 = this.mCurItem;
            while (true) {
                i4++;
                if (i4 >= count) {
                    break;
                } else if (f < 2.0f || i4 <= i2) {
                    if (itemInfo2 != null && i4 == itemInfo2.position) {
                        f += itemInfo2.widthFactor;
                        i3++;
                        if (i3 < this.mItems.size()) {
                            itemInfo2 = this.mItems.get(i3);
                        }
                    } else {
                        ItemInfo addNewItem = addNewItem(i4, i3);
                        i3++;
                        f += addNewItem.widthFactor;
                        itemInfo2 = i3 < this.mItems.size() ? this.mItems.get(i3) : null;
                    }
                } else if (itemInfo2 == null) {
                    break;
                } else if (i4 == itemInfo2.position && !itemInfo2.scrolling) {
                    this.mItems.remove(i3);
                    this.mAdapter.destroyItem(this, i4, itemInfo2.object);
                    if (i3 < this.mItems.size()) {
                        itemInfo2 = this.mItems.get(i3);
                    }
                }
            }
        }
        return i;
    }

    public final void calculatePageLimits(int i) {
        int i2 = this.mCurItem;
        if (i2 > i) {
            this.mIsLeftScrolled = false;
            this.mLeftOffscreenPageLimit = DEFAULT_MORE_OFFSCREEN_PAGES;
            this.mRightOffscreenPageLimit = 1;
        } else if (i2 >= i) {
        } else {
            this.mIsLeftScrolled = true;
            this.mRightOffscreenPageLimit = DEFAULT_MORE_OFFSCREEN_PAGES;
            this.mLeftOffscreenPageLimit = 1;
        }
    }

    public final float getSlipScale(int i) {
        int slipWidth;
        int i2 = this.mSlippedHeight;
        if (i2 == 0) {
            slipWidth = this.mAdapter.getSlipWidth((int) (this.mInitialHeight * this.mHeightSlipRatio), i);
        } else {
            slipWidth = this.mAdapter.getSlipWidth(i2, i);
        }
        return slipWidth > 0 ? Math.min(this.mWidthSlipRatio, (slipWidth * 1.0f) / this.mInitialWidth) : this.mWidthSlipRatio;
    }

    public final void calculatePageWidthFactor(ItemInfo itemInfo) {
        float f = itemInfo.widthFactor;
        this.mPageMargin = (int) ((1.0f - ((1.0f - this.mMarginSlipRatio) * this.mSlipProgress)) * this.mInitialPageMargin);
        Iterator<ItemInfo> it = this.mItems.iterator();
        while (it.hasNext()) {
            ItemInfo next = it.next();
            float slipScale = (!BaseMiscUtil.floatEquals(this.mSlipProgress, 0.0f) ? 1.0f - ((1.0f - getSlipScale(next.position)) * this.mSlipProgress) : 1.0f) * this.mAdapter.getPageWidth(next.position);
            next.widthFactor = slipScale;
            next.offsetLeftScreen = (1.0f - slipScale) / 2.0f;
        }
        itemInfo.offset += (f - itemInfo.widthFactor) / 2.0f;
    }

    @Override // com.miui.gallery.widget.slip.ISlipAnimView
    public void onSlipping(float f) {
        this.mSlipProgress = f;
    }

    public void setHeightSlipRatio(float f) {
        this.mHeightSlipRatio = BaseMiscUtil.clamp(f, 0.0f, 1.0f);
        if (isLaidOut()) {
            if (DEBUG) {
                Log.d("ViewPager", "update height slip ratio, request layout");
            }
            requestLayout();
        }
    }

    public void setSlippedHeight(int i) {
        this.mSlippedHeight = i;
    }

    public void setWidthSlipRatio(float f) {
        this.mWidthSlipRatio = BaseMiscUtil.clamp(f, 0.0f, 1.0f);
        if (isLaidOut()) {
            if (DEBUG) {
                Log.d("ViewPager", "update width slip ratio, request layout");
            }
            requestLayout();
        }
    }

    public void setMarginSlipRatio(float f) {
        this.mMarginSlipRatio = BaseMiscUtil.clamp(f, 0.0f, 1.0f);
        if (isLaidOut()) {
            if (DEBUG) {
                Log.d("ViewPager", "update margin slip ratio, request layout");
            }
            requestLayout();
        }
    }

    public final void calculatePageOffsets(ItemInfo itemInfo, int i, ItemInfo itemInfo2) {
        int i2;
        int i3;
        ItemInfo itemInfo3;
        ItemInfo itemInfo4;
        calculatePageWidthFactor(itemInfo);
        int count = this.mAdapter.getCount();
        int width = getWidth();
        float f = 0.0f;
        float f2 = width > 0 ? this.mPageMargin / width : 0.0f;
        if (itemInfo2 != null) {
            int i4 = itemInfo2.position;
            int i5 = itemInfo.position;
            if (i4 < i5) {
                float f3 = itemInfo2.offset + itemInfo2.widthFactor + f2;
                int i6 = i4 + 1;
                int i7 = 0;
                while (i6 <= itemInfo.position && i7 < this.mItems.size()) {
                    ItemInfo itemInfo5 = this.mItems.get(i7);
                    while (true) {
                        itemInfo4 = itemInfo5;
                        if (i6 <= itemInfo4.position || i7 >= this.mItems.size() - 1) {
                            break;
                        }
                        i7++;
                        itemInfo5 = this.mItems.get(i7);
                    }
                    while (i6 < itemInfo4.position) {
                        f3 += this.mAdapter.getPageWidth(i6) + f2;
                        i6++;
                    }
                    itemInfo4.offset = f3;
                    f3 += itemInfo4.widthFactor + f2;
                    i6++;
                }
            } else if (i4 > i5) {
                int size = this.mItems.size() - 1;
                float f4 = itemInfo2.offset;
                while (true) {
                    i4--;
                    if (i4 < itemInfo.position || size < 0) {
                        break;
                    }
                    ItemInfo itemInfo6 = this.mItems.get(size);
                    while (true) {
                        itemInfo3 = itemInfo6;
                        if (i4 >= itemInfo3.position || size <= 0) {
                            break;
                        }
                        size--;
                        itemInfo6 = this.mItems.get(size);
                    }
                    while (i4 > itemInfo3.position) {
                        f4 -= this.mAdapter.getPageWidth(i4) + f2;
                        i4--;
                    }
                    f4 -= itemInfo3.widthFactor + f2;
                    itemInfo3.offset = f4;
                }
            }
        }
        int size2 = this.mItems.size();
        float f5 = itemInfo.offset;
        int i8 = itemInfo.position;
        int i9 = i8 - 1;
        this.mFirstOffset = i8 == 0 ? f5 : -3.4028235E38f;
        this.mFirstOffsetLeftScreen = i8 == 0 ? itemInfo.offsetLeftScreen : 0.0f;
        int i10 = count - 1;
        this.mLastOffset = i8 == i10 ? f5 : Float.MAX_VALUE;
        if (i8 == i10) {
            f = itemInfo.offsetLeftScreen;
        }
        this.mLastOffsetLeftScreen = f;
        int i11 = i - 1;
        while (i11 >= 0) {
            ItemInfo itemInfo7 = this.mItems.get(i11);
            while (true) {
                i3 = itemInfo7.position;
                if (i9 <= i3) {
                    break;
                }
                f5 -= this.mAdapter.getPageWidth(i9) + f2;
                i9--;
            }
            f5 -= itemInfo7.widthFactor + f2;
            itemInfo7.offset = f5;
            if (i3 == 0) {
                this.mFirstOffset = f5;
                this.mFirstOffsetLeftScreen = itemInfo7.offsetLeftScreen;
            }
            i11--;
            i9--;
        }
        float f6 = itemInfo.offset + itemInfo.widthFactor + f2;
        int i12 = itemInfo.position + 1;
        int i13 = i + 1;
        while (i13 < size2) {
            ItemInfo itemInfo8 = this.mItems.get(i13);
            while (true) {
                i2 = itemInfo8.position;
                if (i12 >= i2) {
                    break;
                }
                f6 += this.mAdapter.getPageWidth(i12) + f2;
                i12++;
            }
            if (i2 == i10) {
                this.mLastOffset = f6;
                this.mLastOffsetLeftScreen = itemInfo8.offsetLeftScreen;
            }
            itemInfo8.offset = f6;
            f6 += itemInfo8.widthFactor + f2;
            i13++;
            i12++;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    /* loaded from: classes2.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.miui.gallery.widget.ViewPager.SavedState.1
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public SavedState mo1810createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public SavedState[] mo1811newArray(int i) {
                return new SavedState[i];
            }
        };
        public Parcelable adapterState;
        public int position;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, i);
        }

        public String toString() {
            return "FragmentPager.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " position=" + this.position + "}";
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(null);
        }
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            savedState.adapterState = pagerAdapter.saveState();
        }
        return savedState;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            pagerAdapter.restoreState(savedState.adapterState, null);
            setCurrentItemInternal(savedState.position, false, true);
            return;
        }
        this.mRestoredCurItem = savedState.position;
        this.mRestoredAdapterState = savedState.adapterState;
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (!checkLayoutParams(layoutParams)) {
            layoutParams = generateLayoutParams(layoutParams);
        }
        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
        boolean z = layoutParams2.isDecor | false;
        layoutParams2.isDecor = z;
        if (!this.mInLayout) {
            super.addView(view, i, layoutParams);
        } else if (z) {
            throw new IllegalStateException("Cannot add pager decor view during layout");
        } else {
            layoutParams2.needsMeasure = true;
            addViewInLayout(view, i, layoutParams);
        }
    }

    public ItemInfo infoForChild(View view) {
        Iterator<ItemInfo> it = this.mItems.iterator();
        while (it.hasNext()) {
            ItemInfo next = it.next();
            if (this.mAdapter.isViewFromObject(view, next.object)) {
                return next;
            }
        }
        return null;
    }

    public ItemInfo infoForAnyChild(View view) {
        while (true) {
            ViewParent parent = view.getParent();
            if (parent != this) {
                if (!(parent instanceof View)) {
                    return null;
                }
                view = (View) parent;
            } else {
                return infoForChild(view);
            }
        }
    }

    public ItemInfo infoForPosition(int i) {
        Iterator<ItemInfo> it = this.mItems.iterator();
        while (it.hasNext()) {
            ItemInfo next = it.next();
            if (next.position == i) {
                return next;
            }
        }
        return null;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        LayoutParams layoutParams;
        int i3;
        setMeasuredDimension(ViewGroup.getDefaultSize(0, i), ViewGroup.getDefaultSize(0, i2));
        int measuredWidth = getMeasuredWidth();
        this.mGutterSize = Math.min(measuredWidth / 10, this.mDefaultGutterSize);
        int paddingLeft = (measuredWidth - getPaddingLeft()) - getPaddingRight();
        int measuredHeight = (getMeasuredHeight() - getPaddingTop()) - getPaddingBottom();
        int childCount = getChildCount();
        int i4 = 0;
        while (true) {
            boolean z = true;
            int i5 = 1073741824;
            if (i4 >= childCount) {
                break;
            }
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() != 8 && (layoutParams = (LayoutParams) childAt.getLayoutParams()) != null && layoutParams.isDecor) {
                int i6 = layoutParams.gravity;
                int i7 = i6 & 7;
                int i8 = i6 & 112;
                boolean z2 = i8 == 48 || i8 == 80;
                if (i7 != 3 && i7 != 5) {
                    z = false;
                }
                int i9 = Integer.MIN_VALUE;
                if (z2) {
                    i3 = Integer.MIN_VALUE;
                    i9 = 1073741824;
                } else {
                    i3 = z ? 1073741824 : Integer.MIN_VALUE;
                }
                int i10 = ((ViewGroup.LayoutParams) layoutParams).width;
                if (i10 != -2) {
                    if (i10 == -1) {
                        i10 = paddingLeft;
                    }
                    i9 = 1073741824;
                } else {
                    i10 = paddingLeft;
                }
                int i11 = ((ViewGroup.LayoutParams) layoutParams).height;
                if (i11 == -2) {
                    i11 = measuredHeight;
                    i5 = i3;
                } else if (i11 == -1) {
                    i11 = measuredHeight;
                }
                childAt.measure(View.MeasureSpec.makeMeasureSpec(i10, i9), View.MeasureSpec.makeMeasureSpec(i11, i5));
                if (z2) {
                    measuredHeight -= childAt.getMeasuredHeight();
                } else if (z) {
                    paddingLeft -= childAt.getMeasuredWidth();
                }
            }
            i4++;
        }
        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(paddingLeft, 1073741824);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824);
        this.mInLayout = true;
        if (!this.isPreload) {
            populate();
        } else {
            this.isPreload = false;
        }
        this.mInLayout = false;
        int childCount2 = getChildCount();
        for (int i12 = 0; i12 < childCount2; i12++) {
            View childAt2 = getChildAt(i12);
            if (childAt2.getVisibility() != 8) {
                if (DEBUG) {
                    Log.v("ViewPager", "Measuring #" + i12 + " " + childAt2 + ": " + this.mChildWidthMeasureSpec);
                }
                LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
                if (!layoutParams2.isDecor) {
                    childAt2.measure(View.MeasureSpec.makeMeasureSpec((int) (paddingLeft * layoutParams2.widthFactor), 1073741824), View.MeasureSpec.makeMeasureSpec(measuredHeight, 1073741824));
                }
            }
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (i != i3) {
            recomputeScrollPosition(i, i3, 0, 0);
        }
    }

    public final void recomputeScrollPosition(int i, int i2, int i3, int i4) {
        if (i2 > 0 && !this.mItems.isEmpty()) {
            int round = Math.round((getScrollX() / (i2 + i4)) * (i3 + i));
            scrollTo(round, getScrollY());
            if (!this.mScroller.isFinished()) {
                int duration = this.mScroller.getDuration() - this.mScroller.timePassed();
                ItemInfo infoForPosition = infoForPosition(this.mCurItem);
                if (infoForPosition != null) {
                    this.mScroller.startScroll(round, 0, ((int) ((infoForPosition.offset - infoForPosition.offsetLeftScreen) * i)) - round, 0, duration);
                    return;
                }
            }
            this.mScroller.startScroll(round, 0, 0, 0, 0);
            this.mScroller.abortAnimation();
            return;
        }
        ItemInfo infoForPosition2 = infoForPosition(this.mCurItem);
        int min = (int) ((infoForPosition2 != null ? Math.min(infoForPosition2.offset, this.mLastOffset) : 0.0f) * i);
        if (min == getScrollX()) {
            return;
        }
        completeScroll(false);
        scrollTo(min, getScrollY());
    }

    /* JADX WARN: Removed duplicated region for block: B:85:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x009a  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onLayout(boolean r19, int r20, int r21, int r22, int r23) {
        /*
            Method dump skipped, instructions count: 404
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.ViewPager.onLayout(boolean, int, int, int, int):void");
    }

    @Override // android.view.View
    public void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                scrollTo(currX, currY);
                if (!pageScrolled(currX, false)) {
                    this.mScroller.abortAnimation();
                    scrollTo(0, currY);
                }
            }
            postInvalidateOnAnimation();
            return;
        }
        completeScroll(true);
    }

    public final boolean pageScrolled(int i, boolean z) {
        if (this.mItems.size() == 0) {
            this.mCalledSuper = false;
            onPageScrolled(0, 0.0f, 0);
            if (!this.mCalledSuper) {
                throw new IllegalStateException("onPageScrolled did not call superclass implementation");
            }
            return false;
        }
        ItemInfo infoForCurrentScrollPosition = infoForCurrentScrollPosition();
        int width = getWidth();
        int i2 = this.mPageMargin;
        int i3 = width + i2;
        float f = width;
        int i4 = infoForCurrentScrollPosition.position;
        float f2 = (((i / f) - infoForCurrentScrollPosition.offset) - infoForCurrentScrollPosition.offsetLeftScreen) / (infoForCurrentScrollPosition.widthFactor + (i2 / f));
        this.mCalledSuper = false;
        onPageScrolled(i4, f2, (int) (i3 * f2));
        if (!this.mCalledSuper) {
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:58:0x0063  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onPageScrolled(int r12, float r13, int r14) {
        /*
            r11 = this;
            int r0 = r11.mDecorChildCount
            r1 = 1
            if (r0 <= 0) goto L6a
            int r0 = r11.getScrollX()
            int r2 = r11.getPaddingLeft()
            int r3 = r11.getPaddingRight()
            int r4 = r11.getWidth()
            int r5 = r11.getChildCount()
            r6 = 0
        L1a:
            if (r6 >= r5) goto L6a
            android.view.View r7 = r11.getChildAt(r6)
            android.view.ViewGroup$LayoutParams r8 = r7.getLayoutParams()
            com.miui.gallery.widget.ViewPager$LayoutParams r8 = (com.miui.gallery.widget.ViewPager.LayoutParams) r8
            boolean r9 = r8.isDecor
            if (r9 != 0) goto L2b
            goto L67
        L2b:
            int r8 = r8.gravity
            r8 = r8 & 7
            if (r8 == r1) goto L4c
            r9 = 3
            if (r8 == r9) goto L46
            r9 = 5
            if (r8 == r9) goto L39
            r8 = r2
            goto L5b
        L39:
            int r8 = r4 - r3
            int r9 = r7.getMeasuredWidth()
            int r8 = r8 - r9
            int r9 = r7.getMeasuredWidth()
            int r3 = r3 + r9
            goto L58
        L46:
            int r8 = r7.getWidth()
            int r8 = r8 + r2
            goto L5b
        L4c:
            int r8 = r7.getMeasuredWidth()
            int r8 = r4 - r8
            int r8 = r8 / 2
            int r8 = java.lang.Math.max(r8, r2)
        L58:
            r10 = r8
            r8 = r2
            r2 = r10
        L5b:
            int r2 = r2 + r0
            int r9 = r7.getLeft()
            int r2 = r2 - r9
            if (r2 == 0) goto L66
            r7.offsetLeftAndRight(r2)
        L66:
            r2 = r8
        L67:
            int r6 = r6 + 1
            goto L1a
        L6a:
            com.miui.gallery.widget.ViewPager$OnPageChangeListener r0 = r11.mOnPageChangeListener
            if (r0 == 0) goto L71
            r0.onPageScrolled(r12, r13, r14)
        L71:
            com.miui.gallery.widget.ViewPager$OnPageChangeListener r0 = r11.mInternalPageChangeListener
            if (r0 == 0) goto L78
            r0.onPageScrolled(r12, r13, r14)
        L78:
            r11.mCalledSuper = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.ViewPager.onPageScrolled(int, float, int):void");
    }

    public final void completeScroll(boolean z) {
        int i = this.mScrollState;
        boolean z2 = i == 2;
        final boolean z3 = i == 2;
        if (z2) {
            setScrollingCacheEnabled(false);
            this.mScroller.abortAnimation();
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            if (scrollX != currX || scrollY != currY) {
                scrollTo(currX, currY);
            }
            setScrollState(0);
        }
        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.widget.ViewPager$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                ViewPager.$r8$lambda$4xifaSmwbSRl1VBek7ejuX6MCiw(ViewPager.this, z3);
            }
        });
        this.mPopulatePending = false;
        Iterator<ItemInfo> it = this.mItems.iterator();
        while (it.hasNext()) {
            ItemInfo next = it.next();
            if (next.scrolling) {
                next.scrolling = false;
                z2 = true;
            }
        }
        if (z2) {
            if (z) {
                postOnAnimation(this.mEndScrollRunnable);
            } else {
                this.mEndScrollRunnable.run();
            }
        }
    }

    public /* synthetic */ void lambda$completeScroll$0(boolean z) {
        if (z) {
            callBackPageSettled();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        removeCallbacks(this.mEndScrollRunnable);
        super.onDetachedFromWindow();
    }

    public final boolean isGutterDrag(float f, float f2) {
        return (f < ((float) this.mGutterSize) && f2 > 0.0f) || (f > ((float) (getWidth() - this.mGutterSize)) && f2 < 0.0f);
    }

    public final boolean needIntercept(float f, float f2) {
        return f > ((float) this.mTouchSlop) && ((double) f) > ((double) f2) * Math.tan(0.4363323129985824d);
    }

    public final int findPointerIndex(MotionEvent motionEvent, int i) {
        int findPointerIndex = motionEvent.findPointerIndex(i);
        int pointerCount = motionEvent.getPointerCount();
        if (findPointerIndex < 0 || findPointerIndex >= pointerCount) {
            HashMap hashMap = new HashMap();
            hashMap.put("invalid", String.format(Locale.US, "id[%d], index[%d], count[%d]", Integer.valueOf(i), Integer.valueOf(findPointerIndex), Integer.valueOf(pointerCount)));
            SamplingStatHelper.recordCountEvent("gesture", "gesture_view_pager", hashMap);
            return -1;
        }
        return findPointerIndex;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int findPointerIndex;
        float f;
        float f2;
        if (!this.mDragEnabled) {
            return false;
        }
        int action = motionEvent.getAction() & 255;
        if (action == 3 || action == 1) {
            if (DEBUG) {
                Log.v("ViewPager", "Intercept done!");
            }
            this.mIsBeingDragged = false;
            this.mIsUnableToDrag = false;
            this.mActivePointerId = -1;
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.recycle();
                this.mVelocityTracker = null;
            }
            return false;
        }
        if (action != 0) {
            if (this.mIsBeingDragged) {
                if (DEBUG) {
                    Log.v("ViewPager", "Intercept returning true!");
                }
                return true;
            } else if (this.mIsUnableToDrag) {
                if (DEBUG) {
                    Log.v("ViewPager", "Intercept returning false!");
                }
                return false;
            }
        }
        if (action == 0) {
            float x = motionEvent.getX();
            this.mInitialMotionX = x;
            this.mLastMotionX = x;
            this.mLastMotionY = motionEvent.getY();
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mIsUnableToDrag = false;
            this.mScroller.computeScrollOffset();
            if (this.mScrollState == 2 && Math.abs(this.mScroller.getFinalX() - this.mScroller.getCurrX()) > this.mCloseEnough) {
                this.mScroller.abortAnimation();
                this.mPopulatePending = false;
                populate();
                this.mIsBeingDragged = true;
                setScrollState(1);
            } else {
                completeScroll(false);
                this.mIsBeingDragged = false;
            }
            if (DEBUG) {
                Log.v("ViewPager", "Down at " + this.mLastMotionX + "," + this.mLastMotionY + " mIsBeingDragged=" + this.mIsBeingDragged + "mIsUnableToDrag=" + this.mIsUnableToDrag);
            }
        } else if (action == 2) {
            int i = this.mActivePointerId;
            if (i != -1 && (findPointerIndex = findPointerIndex(motionEvent, i)) != -1) {
                float x2 = motionEvent.getX(findPointerIndex);
                float f3 = x2 - this.mLastMotionX;
                float abs = Math.abs(f3);
                float y = motionEvent.getY(findPointerIndex);
                float abs2 = Math.abs(y - this.mLastMotionY);
                boolean z = DEBUG;
                if (z) {
                    Log.v("ViewPager", "Moved x to " + x2 + "," + y + " diff=" + abs + "," + abs2);
                }
                if (BaseMiscUtil.floatEquals(f3, 0.0f) || isGutterDrag(this.mLastMotionX, f3)) {
                    f = 0.0f;
                } else {
                    f = 0.0f;
                    if (canScroll(this, false, (int) f3, (int) x2, (int) y)) {
                        this.mLastMotionX = x2;
                        this.mInitialMotionX = x2;
                        this.mLastMotionY = y;
                        this.mIsUnableToDrag = true;
                        return false;
                    }
                }
                if (needIntercept(abs, abs2)) {
                    if (z) {
                        Log.v("ViewPager", "Starting drag!");
                    }
                    this.mIsBeingDragged = true;
                    setScrollState(1);
                    float abs3 = Math.abs(x2 - this.mInitialMotionX);
                    int i2 = this.mTouchSlop;
                    if (abs3 > i2 * 2) {
                        this.mLastMotionX = x2;
                        this.mInitialMotionX = x2;
                        this.mLastMotionY = y;
                    } else {
                        if (f3 > f) {
                            f2 = this.mInitialMotionX + i2;
                        } else {
                            f2 = this.mInitialMotionX - i2;
                        }
                        this.mLastMotionX = f2;
                    }
                    setScrollingCacheEnabled(true);
                } else if (abs2 > this.mTouchSlop) {
                    if (z) {
                        Log.v("ViewPager", "Starting unable to drag!");
                    }
                    this.mIsUnableToDrag = true;
                }
                if (this.mIsBeingDragged && performDrag(x2)) {
                    postInvalidateOnAnimation();
                }
            }
        } else if (action == 6) {
            onSecondaryPointerUp(motionEvent);
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        return this.mIsBeingDragged;
    }

    /* JADX WARN: Code restructure failed: missing block: B:122:0x0095, code lost:
        if (r11.mRightEdge.isFinished() == false) goto L32;
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x0196, code lost:
        if (r11.mRightEdge.isFinished() == false) goto L32;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r12) {
        /*
            Method dump skipped, instructions count: 446
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.ViewPager.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final boolean performDrag(float f) {
        boolean z;
        boolean z2;
        float f2 = this.mLastMotionX - f;
        this.mLastMotionX = f;
        float scrollX = getScrollX() + f2;
        float width = getWidth();
        float minScrollOffset = getMinScrollOffset() * width;
        float maxScrollOffset = getMaxScrollOffset() * width;
        boolean z3 = false;
        ItemInfo itemInfo = this.mItems.get(0);
        ArrayList<ItemInfo> arrayList = this.mItems;
        ItemInfo itemInfo2 = arrayList.get(arrayList.size() - 1);
        if (itemInfo.position != 0) {
            minScrollOffset = (itemInfo.offset - itemInfo.offsetLeftScreen) * width;
            z = false;
        } else {
            z = true;
        }
        if (itemInfo2.position != this.mAdapter.getCount() - 1) {
            maxScrollOffset = (itemInfo2.offset - itemInfo2.offsetLeftScreen) * width;
            z2 = false;
        } else {
            z2 = true;
        }
        if (scrollX < minScrollOffset) {
            if (z) {
                this.mLeftEdge.onPull(Math.abs(minScrollOffset - scrollX) / width);
                z3 = true;
            }
            scrollX = minScrollOffset;
        } else if (scrollX > maxScrollOffset) {
            if (z2) {
                this.mRightEdge.onPull(Math.abs(scrollX - maxScrollOffset) / width);
                z3 = true;
            }
            scrollX = maxScrollOffset;
        }
        int i = (int) scrollX;
        this.mLastMotionX += scrollX - i;
        scrollTo(i, getScrollY());
        pageScrolled(i, true);
        return z3;
    }

    public final ItemInfo infoForCurrentScrollPosition() {
        int i;
        int width = getWidth();
        float f = 0.0f;
        float scrollX = width > 0 ? getScrollX() / width : 0.0f;
        float f2 = width > 0 ? this.mPageMargin / width : 0.0f;
        ItemInfo itemInfo = null;
        int i2 = 0;
        int i3 = -1;
        boolean z = true;
        float f3 = 0.0f;
        while (i2 < this.mItems.size()) {
            ItemInfo itemInfo2 = this.mItems.get(i2);
            if (!z && itemInfo2.position != (i = i3 + 1)) {
                itemInfo2 = this.mTempItem;
                itemInfo2.offset = f + f3 + f2;
                itemInfo2.position = i;
                itemInfo2.widthFactor = this.mAdapter.getPageWidth(i);
                i2--;
            }
            f = itemInfo2.offset;
            float f4 = itemInfo2.offsetLeftScreen;
            float f5 = f - f4;
            float f6 = (f - f4) + itemInfo2.widthFactor + f2;
            if (!z && scrollX < f5) {
                return itemInfo;
            }
            if (scrollX < f6 || i2 == this.mItems.size() - 1) {
                return itemInfo2;
            }
            i3 = itemInfo2.position;
            f3 = itemInfo2.widthFactor;
            i2++;
            z = false;
            itemInfo = itemInfo2;
        }
        return itemInfo;
    }

    public final int determineTargetPage(int i, float f, int i2, int i3) {
        int i4;
        if (Math.abs(i3) <= this.mFlingDistance || Math.abs(i2) <= this.mMinimumVelocity) {
            i4 = (int) (i + (i >= this.mCurItem ? 0.6f : 0.4f) + f);
        } else if (i3 < 0) {
            i4 = (i2 > this.mReverseVelocity ? 0 : 1) + i;
            if (i2 < 0 && i4 == getCurrentItem()) {
                if (DEBUG) {
                    Log.d("ViewPager", "go on scrolling to new page, offset: " + f);
                }
                i4 += (int) (0.3f + f);
            }
        } else {
            i4 = (i2 < (-this.mReverseVelocity) ? 1 : 0) + i;
            if (i2 > 0 && i4 == getCurrentItem()) {
                if (DEBUG) {
                    Log.d("ViewPager", "go on scrolling to new page, offset: " + f);
                }
                i4 -= f < 0.3f ? 1 : 0;
            }
        }
        if (this.mItems.size() > 0) {
            ArrayList<ItemInfo> arrayList = this.mItems;
            i4 = Math.max(this.mItems.get(0).position, Math.min(i4, arrayList.get(arrayList.size() - 1).position));
        }
        if (DEBUG) {
            Log.d("ViewPager", String.format(Locale.US, "deltaX: %s, velocity: %s, offset: %s, curItem: %s, displayItem: %s, tarItem: %s", Integer.valueOf(i3), Integer.valueOf(i2), Float.valueOf(f), Integer.valueOf(getCurrentItem()), Integer.valueOf(i), Integer.valueOf(i4)));
        }
        return i4;
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        PagerAdapter pagerAdapter;
        super.draw(canvas);
        int overScrollMode = getOverScrollMode();
        boolean z = false;
        if (overScrollMode == 0 || (overScrollMode == 1 && (pagerAdapter = this.mAdapter) != null && pagerAdapter.getCount() > 1)) {
            if (!this.mLeftEdge.isFinished()) {
                int save = canvas.save();
                int height = (getHeight() - getPaddingTop()) - getPaddingBottom();
                int width = getWidth();
                canvas.rotate(270.0f);
                canvas.translate((-height) + getPaddingTop(), this.mFirstOffset * width);
                this.mLeftEdge.setSize(height, width);
                z = this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(save);
            }
            if (!this.mRightEdge.isFinished()) {
                int save2 = canvas.save();
                int width2 = getWidth();
                int height2 = (getHeight() - getPaddingTop()) - getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate(-getPaddingTop(), (-(this.mLastOffset + 1.0f)) * width2);
                this.mRightEdge.setSize(height2, width2);
                z |= this.mRightEdge.draw(canvas);
                canvas.restoreToCount(save2);
            }
        } else {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        }
        if (z) {
            postInvalidateOnAnimation();
        }
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        int width;
        int i;
        float f;
        float f2;
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int scrollX = getScrollX();
            float width2 = getWidth();
            float f3 = this.mPageMargin / width2;
            int i2 = 0;
            ItemInfo itemInfo = this.mItems.get(0);
            float f4 = itemInfo.offset;
            int size = this.mItems.size();
            int i3 = itemInfo.position;
            int i4 = this.mItems.get(size - 1).position;
            while (i3 < i4) {
                while (true) {
                    i = itemInfo.position;
                    if (i3 <= i || i2 >= size) {
                        break;
                    }
                    i2++;
                    itemInfo = this.mItems.get(i2);
                }
                if (i3 == i) {
                    float f5 = itemInfo.offset;
                    float f6 = itemInfo.widthFactor;
                    f = (f5 + f6) * width2;
                    f4 = f5 + f6 + f3;
                } else {
                    float pageWidth = this.mAdapter.getPageWidth(i3);
                    f = (f4 + pageWidth) * width2;
                    f4 += pageWidth + f3;
                }
                int i5 = this.mPageMargin;
                if (i5 + f > scrollX) {
                    f2 = f3;
                    this.mMarginDrawable.setBounds((int) (f - 1.0f), this.mTopPageBounds, (int) (i5 + f + 1.0f), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                } else {
                    f2 = f3;
                }
                if (f > scrollX + width) {
                    break;
                }
                i3++;
                f3 = f2;
            }
        }
        super.onDraw(canvas);
    }

    public final void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
            int i = actionIndex == 0 ? 1 : 0;
            this.mLastMotionX = motionEvent.getX(i);
            this.mActivePointerId = motionEvent.getPointerId(i);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker == null) {
                return;
            }
            velocityTracker.clear();
        }
    }

    public final void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private void setScrollingCacheEnabled(boolean z) {
        if (this.mScrollingCacheEnabled != z) {
            this.mScrollingCacheEnabled = z;
        }
    }

    public boolean canScroll(View view, boolean z, int i, int i2, int i3) {
        int i4;
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = viewGroup.getChildAt(childCount);
                int i5 = i2 + scrollX;
                if (i5 >= childAt.getLeft() && i5 < childAt.getRight() && (i4 = i3 + scrollY) >= childAt.getTop() && i4 < childAt.getBottom() && canScroll(childAt, true, i, i5 - childAt.getLeft(), i4 - childAt.getTop())) {
                    return true;
                }
            }
        }
        return z && view.canScrollHorizontally(-i);
    }

    @Override // android.view.ViewGroup, android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return super.dispatchKeyEvent(keyEvent) || executeKeyEvent(keyEvent);
    }

    public boolean executeKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            int keyCode = keyEvent.getKeyCode();
            if (keyCode == 21) {
                return arrowScroll(17);
            }
            if (keyCode == 22) {
                return arrowScroll(66);
            }
            if (keyCode == 61 && Build.VERSION.SDK_INT >= 11) {
                if (keyEvent.hasNoModifiers()) {
                    return arrowScroll(2);
                }
                if (keyEvent.hasModifiers(1)) {
                    return arrowScroll(1);
                }
            }
        }
        return false;
    }

    public boolean arrowScroll(int i) {
        boolean requestFocus;
        View findFocus = findFocus();
        if (findFocus == this) {
            findFocus = null;
        }
        boolean z = false;
        View findNextFocus = FocusFinder.getInstance().findNextFocus(this, findFocus, i);
        if (findNextFocus != null && findNextFocus != findFocus) {
            if (i == 17) {
                int i2 = getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left;
                int i3 = getChildRectInPagerCoordinates(this.mTempRect, findFocus).left;
                if (findFocus != null && i2 >= i3) {
                    requestFocus = pageLeft();
                } else {
                    requestFocus = findNextFocus.requestFocus();
                }
            } else if (i == 66) {
                int i4 = getChildRectInPagerCoordinates(this.mTempRect, findNextFocus).left;
                int i5 = getChildRectInPagerCoordinates(this.mTempRect, findFocus).left;
                if (findFocus != null && i4 <= i5) {
                    requestFocus = pageRight();
                } else {
                    requestFocus = findNextFocus.requestFocus();
                }
            }
            z = requestFocus;
        } else if (i == 17 || i == 1) {
            z = pageLeft();
        } else if (i == 66 || i == 2) {
            z = pageRight();
        }
        if (z) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(i));
        }
        return z;
    }

    public final Rect getChildRectInPagerCoordinates(Rect rect, View view) {
        if (rect == null) {
            rect = new Rect();
        }
        if (view == null) {
            rect.set(0, 0, 0, 0);
            return rect;
        }
        rect.left = view.getLeft();
        rect.right = view.getRight();
        rect.top = view.getTop();
        rect.bottom = view.getBottom();
        ViewParent parent = view.getParent();
        while ((parent instanceof ViewGroup) && parent != this) {
            ViewGroup viewGroup = (ViewGroup) parent;
            rect.left += viewGroup.getLeft();
            rect.right += viewGroup.getRight();
            rect.top += viewGroup.getTop();
            rect.bottom += viewGroup.getBottom();
            parent = viewGroup.getParent();
        }
        return rect;
    }

    public boolean pageLeft() {
        int i = this.mCurItem;
        if (i > 0) {
            setCurrentItem(i - 1, true);
            return true;
        }
        return false;
    }

    public boolean pageRight() {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter == null || this.mCurItem >= pagerAdapter.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1, true);
        return true;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        ItemInfo infoForChild;
        if (arrayList == null) {
            return;
        }
        int size = arrayList.size();
        int descendantFocusability = getDescendantFocusability();
        if (descendantFocusability != 393216) {
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                View childAt = getChildAt(i3);
                if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem) {
                    childAt.addFocusables(arrayList, i, i2);
                }
            }
        }
        if ((descendantFocusability == 262144 && size != arrayList.size()) || !isFocusable()) {
            return;
        }
        if ((i2 & 1) == 1 && isInTouchMode() && !isFocusableInTouchMode()) {
            return;
        }
        arrayList.add(this);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addTouchables(ArrayList<View> arrayList) {
        ItemInfo infoForChild;
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem) {
                childAt.addTouchables(arrayList);
            }
        }
    }

    @Override // android.view.ViewGroup
    public boolean onRequestFocusInDescendants(int i, Rect rect) {
        int i2;
        int i3;
        ItemInfo infoForChild;
        int childCount = getChildCount();
        int i4 = -1;
        if ((i & 2) != 0) {
            i4 = childCount;
            i2 = 0;
            i3 = 1;
        } else {
            i2 = childCount - 1;
            i3 = -1;
        }
        while (i2 != i4) {
            View childAt = getChildAt(i2);
            if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem && childAt.requestFocus(i, rect)) {
                return true;
            }
            i2 += i3;
        }
        return false;
    }

    @Override // android.view.View
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        ItemInfo infoForChild;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0 && (infoForChild = infoForChild(childAt)) != null && infoForChild.position == this.mCurItem && childAt.dispatchPopulateAccessibilityEvent(accessibilityEvent)) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return generateDefaultLayoutParams();
    }

    @Override // android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return (layoutParams instanceof LayoutParams) && super.checkLayoutParams(layoutParams);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ViewPager.class.getName());
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        int i;
        int i2;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(ViewPager.class.getName());
        PagerAdapter pagerAdapter = this.mAdapter;
        accessibilityNodeInfo.setScrollable(pagerAdapter != null && pagerAdapter.getCount() > 1);
        PagerAdapter pagerAdapter2 = this.mAdapter;
        if (pagerAdapter2 != null && (i2 = this.mCurItem) >= 0 && i2 < pagerAdapter2.getCount() - 1) {
            accessibilityNodeInfo.addAction(4096);
        }
        PagerAdapter pagerAdapter3 = this.mAdapter;
        if (pagerAdapter3 == null || (i = this.mCurItem) <= 0 || i >= pagerAdapter3.getCount()) {
            return;
        }
        accessibilityNodeInfo.addAction(8192);
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        int i2;
        PagerAdapter pagerAdapter;
        int i3;
        if (super.performAccessibilityAction(i, bundle)) {
            return true;
        }
        if (i != 4096) {
            if (i != 8192 || (pagerAdapter = this.mAdapter) == null || (i3 = this.mCurItem) <= 0 || i3 >= pagerAdapter.getCount()) {
                return false;
            }
            setCurrentItem(this.mCurItem - 1);
            return true;
        }
        PagerAdapter pagerAdapter2 = this.mAdapter;
        if (pagerAdapter2 == null || (i2 = this.mCurItem) < 0 || i2 >= pagerAdapter2.getCount() - 1) {
            return false;
        }
        setCurrentItem(this.mCurItem + 1);
        return true;
    }

    public void setDraggable(boolean z) {
        this.mDragEnabled = z;
    }

    /* loaded from: classes2.dex */
    public class PagerObserver extends DataSetObserver {
        public PagerObserver() {
            ViewPager.this = r1;
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            ViewPager.this.dataSetChanged();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            ViewPager.this.dataSetChanged();
        }
    }

    public void setBottomMarginProgress(float f) {
        this.mBottomMarginProgress = f;
        this.mForceReplayout = true;
        requestLayout();
        invalidate();
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int i) {
        if (this.mAdapter == null) {
            return false;
        }
        if (getLayoutDirection() == 1) {
            i = -i;
        }
        return i < 0 ? getCurrentItem() != 0 : i > 0 && getCurrentItem() != this.mAdapter.getCount() - 1;
    }

    /* loaded from: classes2.dex */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int gravity;
        public boolean isDecor;
        public boolean needsMeasure;
        public float widthFactor;

        public LayoutParams() {
            super(-1, -1);
            this.widthFactor = 0.0f;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.widthFactor = 0.0f;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, ViewPager.LAYOUT_ATTRS);
            this.gravity = obtainStyledAttributes.getInteger(0, 48);
            obtainStyledAttributes.recycle();
        }
    }

    public void preloadFirstItem() {
        enablePreload();
    }
}
