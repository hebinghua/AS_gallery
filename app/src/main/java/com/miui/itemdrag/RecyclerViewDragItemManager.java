package com.miui.itemdrag;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.itemdrag.animator.DraggableItemAnimator;
import com.miui.itemdrag.animator.base.GeneralItemAnimator;
import com.miui.itemdrag.decorator.BaseDraggableItemDecorator;
import com.miui.itemdrag.decorator.DraggingItemDecorator;
import com.miui.itemdrag.decorator.DraggingItemEffectsInfo;
import java.util.Collections;
import java.util.List;

/* loaded from: classes3.dex */
public class RecyclerViewDragItemManager {
    public boolean isEnlargeItemEnable;
    public int mActualScrollByXAmount;
    public int mActualScrollByYAmount;
    public RecyclerView.Adapter mAdapter;
    public Config mConfig;
    public float mDisplayDensity;
    public int mDragMaxTouchX;
    public int mDragMaxTouchY;
    public int mDragMinTouchX;
    public int mDragMinTouchY;
    public int mDragStartTouchX;
    public int mDragStartTouchY;
    public DraggingItemBean mDraggingItemBean;
    public DraggingItemDecorator mDraggingItemDecorator;
    public long mFirstTouchItemId;
    public int mFirstTouchX;
    public int mFirstTouchY;
    public InternalHandler mHandler;
    public boolean mInScrollByMethod;
    public boolean mIsEnableSwapItem;
    public GeneralItemAnimator mItemAnimator;
    public int mLastTouchX;
    public int mLastTouchY;
    public NestedScrollView mNestedScrollView;
    public int mNestedScrollViewScrollX;
    public int mNestedScrollViewScrollY;
    public OnDragCallback mOnDragCallback;
    public OnSwapItemListener mOnSwapItemListener;
    public int mOriginOverScrollMode;
    public int mPreSwapFromPosition;
    public int mPreSwapItemPosition;
    public RecyclerView mRecyclerView;
    public ScrollOnDraggingProcessRunnable mScrollRunnable;
    public int mScrollTouchSlop;
    public int mTouchSlop;
    public DraggingItemEffectsInfo mDraggingItemEffectsInfo = new DraggingItemEffectsInfo();
    public boolean isReleased = false;
    public int mScrollDirMask = 0;
    public int mPreSwapToPosition = -1;
    public final RecyclerView.OnItemTouchListener mOnTouchListener = new RecyclerView.OnItemTouchListener() { // from class: com.miui.itemdrag.RecyclerViewDragItemManager.2
        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 0) {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        return RecyclerViewDragItemManager.this.handleActionMove(recyclerView, motionEvent);
                    }
                    if (actionMasked != 3) {
                        if (actionMasked == 5) {
                            RecyclerViewDragItemManager.this.mHandler.cancelLongPressDetection();
                        }
                    }
                }
                return RecyclerViewDragItemManager.this.handleActionUpOrCancel(actionMasked, true);
            } else if (!RecyclerViewDragItemManager.this.isDragging()) {
                return RecyclerViewDragItemManager.this.handleActionDown(recyclerView, motionEvent);
            }
            return false;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (RecyclerViewDragItemManager.this.isDragging()) {
                if (actionMasked != 1) {
                    if (actionMasked == 2) {
                        RecyclerViewDragItemManager.this.handleActionMove(recyclerView, motionEvent);
                        return;
                    } else if (actionMasked != 3) {
                        return;
                    }
                }
                RecyclerViewDragItemManager.this.handleActionUpOrCancel(actionMasked, true);
                return;
            }
            Log.w("DragItemManager", "onTouchEvent() - unexpected state");
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
        public void onRequestDisallowInterceptTouchEvent(boolean z) {
            if (z) {
                RecyclerViewDragItemManager.this.cancelDrag();
            }
        }
    };
    public final RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() { // from class: com.miui.itemdrag.RecyclerViewDragItemManager.3
        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (RecyclerViewDragItemManager.this.mInScrollByMethod) {
                RecyclerViewDragItemManager.this.mActualScrollByXAmount = i;
                RecyclerViewDragItemManager.this.mActualScrollByYAmount = i2;
            } else if (!RecyclerViewDragItemManager.this.isDragging()) {
            } else {
                ViewCompat.postOnAnimationDelayed(RecyclerViewDragItemManager.this.mRecyclerView, RecyclerViewDragItemManager.this.mCheckItemSwappingRunnable, 500L);
            }
        }
    };
    public final Runnable mCheckItemSwappingRunnable = new Runnable() { // from class: com.miui.itemdrag.RecyclerViewDragItemManager.4
        @Override // java.lang.Runnable
        public void run() {
            if (RecyclerViewDragItemManager.this.mDraggingItemBean == null || RecyclerViewDragItemManager.this.mDraggingItemBean.viewHolder == null) {
                return;
            }
            RecyclerViewDragItemManager recyclerViewDragItemManager = RecyclerViewDragItemManager.this;
            recyclerViewDragItemManager.checkItemSwapping(recyclerViewDragItemManager.getRecyclerView());
        }
    };

    /* loaded from: classes3.dex */
    public static abstract class OnDragItemEffectCallback {
        public Bitmap onCreateDragView(View view) {
            return null;
        }

        public abstract boolean onDraw(Canvas canvas, int i, int i2, Bitmap bitmap, Paint paint, int i3, int i4);
    }

    /* loaded from: classes3.dex */
    public interface OnSwapItemListener {
        void onSwapItem();

        void onSwapItemFinish();
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public RecyclerViewDragItemManager(Config config) {
        if (config.getDragCallback() == null) {
            throw new IllegalStateException("DragCallback can't be null");
        }
        this.mConfig = config;
    }

    public void attachToRecyclerView(RecyclerView recyclerView) {
        if (isReleased()) {
            throw new IllegalStateException("Accessing released object");
        }
        this.mRecyclerView = recyclerView;
        this.mScrollRunnable = new ScrollOnDraggingProcessRunnable(this);
        this.mRecyclerView.addOnScrollListener(this.mOnScrollListener);
        this.mRecyclerView.addOnItemTouchListener(this.mOnTouchListener);
        this.mDisplayDensity = this.mRecyclerView.getResources().getDisplayMetrics().density;
        this.mScrollTouchSlop = (int) ((this.mTouchSlop * 1.5f) + 0.5f);
        this.mHandler = new InternalHandler(this);
        this.mItemAnimator = new DraggableItemAnimator();
        internalConfig();
        this.mRecyclerView.setItemAnimator(this.mItemAnimator);
        DragSimpleAdapterWrapper dragSimpleAdapterWrapper = new DragSimpleAdapterWrapper(recyclerView.getAdapter());
        this.mAdapter = dragSimpleAdapterWrapper;
        this.mRecyclerView.setAdapter(dragSimpleAdapterWrapper);
    }

    public void release() {
        RecyclerView.OnScrollListener onScrollListener;
        RecyclerView.OnItemTouchListener onItemTouchListener;
        cancelDrag();
        InternalHandler internalHandler = this.mHandler;
        if (internalHandler != null) {
            internalHandler.release();
            this.mHandler = null;
        }
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null && (onItemTouchListener = this.mOnTouchListener) != null) {
            recyclerView.removeOnItemTouchListener(onItemTouchListener);
        }
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 != null && (onScrollListener = this.mOnScrollListener) != null) {
            recyclerView2.removeOnScrollListener(onScrollListener);
        }
        ScrollOnDraggingProcessRunnable scrollOnDraggingProcessRunnable = this.mScrollRunnable;
        if (scrollOnDraggingProcessRunnable != null) {
            scrollOnDraggingProcessRunnable.release();
            this.mScrollRunnable = null;
        }
        this.mPreSwapFromPosition = -1;
        this.mPreSwapToPosition = -1;
        this.isReleased = true;
    }

    public boolean isReleased() {
        return this.isReleased;
    }

    public boolean isDragging() {
        return this.mDraggingItemBean != null;
    }

    public final int getLastTouchX() {
        int i = this.mLastTouchX;
        NestedScrollView nestedScrollView = this.mNestedScrollView;
        return nestedScrollView != null ? i + (nestedScrollView.getScrollX() - this.mNestedScrollViewScrollX) : i;
    }

    public final int getLastTouchY() {
        int i = this.mLastTouchY;
        NestedScrollView nestedScrollView = this.mNestedScrollView;
        return nestedScrollView != null ? i + (nestedScrollView.getScrollY() - this.mNestedScrollViewScrollY) : i;
    }

    public final boolean isDelayedStartDrag() {
        return this.mConfig.getLongPressTimeout() != 0;
    }

    public final boolean isDelayedSwap() {
        return this.mConfig.getSwapItemNeedHowLongHover() != 0;
    }

    public void setSwapEnableStatus(boolean z) {
        this.mIsEnableSwapItem = z;
    }

    public final boolean isEnableSwap() {
        return this.mIsEnableSwapItem;
    }

    public void setConfig(Config config) {
        this.mConfig = config;
        internalConfig();
    }

    public Config getConfig() {
        return this.mConfig;
    }

    public final void internalConfig() {
        this.mOnDragCallback = this.mConfig.mOnDragCallback;
        this.mTouchSlop = this.mConfig.getEnterDragNeedHowLongOffset() == 0 ? ViewConfiguration.get(getRecyclerView().getContext()).getScaledTouchSlop() : this.mConfig.getEnterDragNeedHowLongOffset();
        if (this.mConfig.getAnimationDurations() != null) {
            SparseIntArray animationDurations = this.mConfig.getAnimationDurations();
            for (int i = 0; i < animationDurations.size(); i++) {
                int keyAt = animationDurations.keyAt(i);
                if (keyAt == 3) {
                    this.mItemAnimator.setAddDuration(animationDurations.get(3));
                }
                if (keyAt == 2) {
                    this.mItemAnimator.setRemoveDuration(animationDurations.get(2));
                }
                if (keyAt == 1) {
                    this.mItemAnimator.setMoveDuration(animationDurations.get(1));
                }
                if (keyAt == 4) {
                    this.mItemAnimator.setChangeDuration(animationDurations.get(4));
                }
            }
        }
        if (this.mConfig.getAnimationInterpolators() != null) {
            SparseArray<Interpolator> animationInterpolators = this.mConfig.getAnimationInterpolators();
            Interpolator interpolator = animationInterpolators.get(3);
            if (interpolator != null) {
                this.mItemAnimator.setAddInterpolator(interpolator);
            }
            Interpolator interpolator2 = animationInterpolators.get(2);
            if (interpolator2 != null) {
                this.mItemAnimator.setRemoveInterpolator(interpolator2);
            }
            Interpolator interpolator3 = animationInterpolators.get(1);
            if (interpolator3 != null) {
                this.mItemAnimator.setMoveInterpolator(interpolator3);
            }
            Interpolator interpolator4 = animationInterpolators.get(4);
            if (interpolator4 == null) {
                return;
            }
            this.mItemAnimator.setChangeInterpolator(interpolator4);
        }
    }

    public final boolean handleActionDown(RecyclerView recyclerView, MotionEvent motionEvent) {
        RecyclerView.ViewHolder findChildViewHolderUnderWithoutTranslation = RecyclerViewUtils.findChildViewHolderUnderWithoutTranslation(recyclerView, motionEvent.getX(), motionEvent.getY());
        if (!checkTouchedItemState(recyclerView, findChildViewHolderUnderWithoutTranslation)) {
            return false;
        }
        int x = (int) (motionEvent.getX() + 0.5f);
        int y = (int) (motionEvent.getY() + 0.5f);
        if (!canStartDrag(findChildViewHolderUnderWithoutTranslation, x, y)) {
            return false;
        }
        this.mLastTouchX = x;
        this.mFirstTouchX = x;
        this.mLastTouchY = y;
        this.mFirstTouchY = y;
        this.mFirstTouchItemId = findChildViewHolderUnderWithoutTranslation.getItemId();
        if (!isDelayedStartDrag()) {
            return checkConditionAndStartDragging(recyclerView, motionEvent, false);
        }
        this.mHandler.startLongPressDetection(motionEvent, this.mConfig.getLongPressTimeout());
        return false;
    }

    public final boolean handleActionUpOrCancel(int i, boolean z) {
        boolean z2 = true;
        if (i != 1) {
            z2 = false;
        }
        boolean isDragging = isDragging();
        InternalHandler internalHandler = this.mHandler;
        if (internalHandler != null) {
            internalHandler.cancelLongPressDetection();
        }
        if (z && isDragging()) {
            finishDragging(z2);
        }
        this.mFirstTouchY = 0;
        this.mFirstTouchX = 0;
        this.mLastTouchX = 0;
        this.mLastTouchY = 0;
        this.mFirstTouchItemId = -1L;
        return isDragging;
    }

    public final boolean handleActionMove(RecyclerView recyclerView, MotionEvent motionEvent) {
        if (!isDragging()) {
            if (!isDelayedStartDrag()) {
                return checkConditionAndStartDragging(recyclerView, motionEvent, true);
            }
            if (this.mHandler.isLongPressedRequested()) {
                this.mLastTouchX = (int) (motionEvent.getX() + 0.5f);
                this.mLastTouchY = (int) (motionEvent.getY() + 0.5f);
                if (checkMoveTouchSlop()) {
                    Log.d("DragItemManager", "cancel long pressed event");
                    this.mHandler.cancelLongPressDetection();
                }
            }
            return false;
        }
        this.mLastTouchX = (int) (motionEvent.getX() + 0.5f);
        this.mLastTouchY = (int) (motionEvent.getY() + 0.5f);
        NestedScrollView nestedScrollView = this.mNestedScrollView;
        this.mNestedScrollViewScrollX = nestedScrollView != null ? nestedScrollView.getScrollX() : 0;
        NestedScrollView nestedScrollView2 = this.mNestedScrollView;
        this.mNestedScrollViewScrollY = nestedScrollView2 != null ? nestedScrollView2.getScrollY() : 0;
        this.mDragMinTouchX = Math.min(this.mDragMinTouchX, this.mLastTouchX);
        this.mDragMinTouchY = Math.min(this.mDragMinTouchY, this.mLastTouchY);
        this.mDragMaxTouchX = Math.max(this.mDragMaxTouchX, this.mLastTouchX);
        this.mDragMaxTouchY = Math.max(this.mDragMaxTouchY, this.mLastTouchY);
        updateDragDirectionMask();
        this.mOnDragCallback.onTouchMoveWhenStartDrag(this.mLastTouchX, this.mLastTouchY);
        boolean update = this.mDraggingItemDecorator.update(getLastTouchX(), getLastTouchY(), false);
        if (isEnableSwap() && update) {
            checkItemSwapping(recyclerView);
        }
        return true;
    }

    public final void updateDragDirectionMask() {
        int orientation = RecyclerViewUtils.getOrientation(this.mRecyclerView);
        if (orientation == 0) {
            int lastTouchX = getLastTouchX();
            int i = this.mDragStartTouchX;
            int i2 = this.mDragMinTouchX;
            int i3 = i - i2;
            int i4 = this.mScrollTouchSlop;
            if (i3 > i4 || this.mDragMaxTouchX - lastTouchX > i4) {
                this.mScrollDirMask |= 4;
            }
            if (this.mDragMaxTouchX - i <= i4 && lastTouchX - i2 <= i4) {
                return;
            }
            this.mScrollDirMask |= 8;
        } else if (orientation != 1) {
        } else {
            int lastTouchY = getLastTouchY();
            int i5 = this.mDragStartTouchY;
            int i6 = this.mDragMinTouchY;
            int i7 = i5 - i6;
            int i8 = this.mScrollTouchSlop;
            if (i7 > i8 || this.mDragMaxTouchY - lastTouchY > i8) {
                this.mScrollDirMask = 1 | this.mScrollDirMask;
            }
            if (this.mDragMaxTouchY - i5 <= i8 && lastTouchY - i6 <= i8) {
                return;
            }
            this.mScrollDirMask |= 2;
        }
    }

    public void handleScrollOnDragging() {
        RecyclerView recyclerView = this.mRecyclerView;
        int orientation = RecyclerViewUtils.getOrientation(recyclerView);
        boolean z = true;
        if (orientation != 0) {
            if (orientation != 1) {
                return;
            }
            z = false;
        }
        handleScrollOnDraggingInternalWithRecyclerView(recyclerView, z);
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00ac, code lost:
        if ((r0 & (r11 ? 8 : 2)) == 0) goto L33;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00ae, code lost:
        r2 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00b8, code lost:
        if ((r0 & (r11 ? 4 : 1)) == 0) goto L33;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void handleScrollOnDraggingInternalWithRecyclerView(androidx.recyclerview.widget.RecyclerView r10, boolean r11) {
        /*
            Method dump skipped, instructions count: 225
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.itemdrag.RecyclerViewDragItemManager.handleScrollOnDraggingInternalWithRecyclerView(androidx.recyclerview.widget.RecyclerView, boolean):void");
    }

    public final int scrollByYAndGetScrolledAmount(int i) {
        this.mActualScrollByYAmount = 0;
        this.mInScrollByMethod = true;
        this.mRecyclerView.scrollBy(0, i);
        this.mInScrollByMethod = false;
        return this.mActualScrollByYAmount;
    }

    public final int scrollByXAndGetScrolledAmount(int i) {
        this.mActualScrollByXAmount = 0;
        this.mInScrollByMethod = true;
        this.mRecyclerView.scrollBy(i, 0);
        this.mInScrollByMethod = false;
        return this.mActualScrollByXAmount;
    }

    public final boolean checkConditionAndStartDragging(RecyclerView recyclerView, MotionEvent motionEvent, boolean z) {
        if (this.mDraggingItemBean != null) {
            return false;
        }
        int x = (int) (motionEvent.getX() + 0.5f);
        int y = (int) (motionEvent.getY() + 0.5f);
        if (this.mFirstTouchItemId == -1) {
            return false;
        }
        if (z && checkMoveTouchSlop()) {
            return false;
        }
        RecyclerView.ViewHolder findChildViewHolderUnderWithoutTranslation = RecyclerViewUtils.findChildViewHolderUnderWithoutTranslation(recyclerView, this.mFirstTouchX, this.mFirstTouchY);
        RecyclerView.ViewHolder findChildViewHolderUnderWithoutTranslation2 = RecyclerViewUtils.findChildViewHolderUnderWithoutTranslation(recyclerView, this.mLastTouchX, this.mLastTouchY);
        if (findChildViewHolderUnderWithoutTranslation == null || findChildViewHolderUnderWithoutTranslation != findChildViewHolderUnderWithoutTranslation2) {
            Log.d("DragItemManager", String.format("开始结束holder不同，拦截拖拽: last X:[%s]->curX:[%s],last Y:[%s]->curY:[%s]", Integer.valueOf(this.mLastTouchX), Integer.valueOf(this.mFirstTouchX), Integer.valueOf(this.mLastTouchY), Integer.valueOf(this.mFirstTouchY)));
            return false;
        } else if (!canStartDrag(findChildViewHolderUnderWithoutTranslation, x, y)) {
            Log.d("DragItemManager", String.format("checkConditionAndStartDragging holder %s cant't drag", findChildViewHolderUnderWithoutTranslation.toString()));
            return false;
        } else {
            this.mLastTouchX = x;
            this.mLastTouchY = y;
            Log.d("DragItemManager", String.format("start startDragging firstX:[%s],firstY:[%s],lastX:[%s],lastY:[%s]", Integer.valueOf(this.mFirstTouchX), Integer.valueOf(this.mFirstTouchY), Integer.valueOf(this.mLastTouchX), Integer.valueOf(this.mLastTouchY)));
            startDragging(recyclerView, motionEvent, findChildViewHolderUnderWithoutTranslation);
            return true;
        }
    }

    public final boolean checkMoveTouchSlop() {
        return Math.abs(this.mLastTouchX - this.mFirstTouchX) > this.mTouchSlop || Math.abs(this.mLastTouchY - this.mFirstTouchY) > this.mTouchSlop;
    }

    public final void startDragging(RecyclerView recyclerView, MotionEvent motionEvent, RecyclerView.ViewHolder viewHolder) {
        if (!this.mOnDragCallback.onBeforeDragItemStart(viewHolder)) {
            return;
        }
        DraggingItemDecorator draggingItemDecorator = new DraggingItemDecorator(this.mRecyclerView, viewHolder);
        this.mDraggingItemDecorator = draggingItemDecorator;
        draggingItemDecorator.setOnDragItemEffectCallback(this.mConfig.getDragItemEffectCallback());
        safeEndAnimation(recyclerView, viewHolder);
        this.mHandler.cancelLongPressDetection();
        DraggingItemBean draggingItemBean = new DraggingItemBean(recyclerView, viewHolder, this.mLastTouchX, this.mLastTouchY);
        this.mDraggingItemBean = draggingItemBean;
        Log.d("DragItemManager", String.format("拖拽position[%s]->id:[%s]", Integer.valueOf(draggingItemBean.mDraggingItemRealPosition), Long.valueOf(this.mDraggingItemBean.id)));
        this.mPreSwapFromPosition = this.mDraggingItemBean.mDraggingItemRealPosition;
        NestedScrollView findAncestorNestedScrollView = RecyclerViewUtils.findAncestorNestedScrollView(this.mRecyclerView);
        if (findAncestorNestedScrollView != null && !this.mRecyclerView.isNestedScrollingEnabled()) {
            this.mNestedScrollView = findAncestorNestedScrollView;
        } else {
            this.mNestedScrollView = null;
        }
        this.mOriginOverScrollMode = recyclerView.getOverScrollMode();
        recyclerView.setOverScrollMode(2);
        this.mLastTouchX = (int) (motionEvent.getX() + 0.5f);
        int y = (int) (motionEvent.getY() + 0.5f);
        this.mLastTouchY = y;
        this.mDragMaxTouchY = y;
        this.mDragMinTouchY = y;
        this.mDragStartTouchY = y;
        int i = this.mLastTouchX;
        this.mDragMaxTouchX = i;
        this.mDragMinTouchX = i;
        this.mDragStartTouchX = i;
        NestedScrollView nestedScrollView = this.mNestedScrollView;
        this.mNestedScrollViewScrollX = nestedScrollView != null ? nestedScrollView.getScrollX() : 0;
        NestedScrollView nestedScrollView2 = this.mNestedScrollView;
        this.mNestedScrollViewScrollY = nestedScrollView2 != null ? nestedScrollView2.getScrollY() : 0;
        this.mScrollDirMask = 0;
        this.mRecyclerView.getParent().requestDisallowInterceptTouchEvent(true);
        startScrollOnDraggingProcess();
        this.mDraggingItemDecorator.setupDraggingItemEffects(this.mDraggingItemEffectsInfo);
        this.mDraggingItemDecorator.start(this.mDraggingItemBean, this.mLastTouchX, this.mLastTouchY, this.isEnlargeItemEnable);
        this.mAdapter.onBindViewHolder(viewHolder, viewHolder.getAdapterPosition());
        this.mOnDragCallback.onDragItemStarted(viewHolder, this.mDragStartTouchX, this.mDragStartTouchY);
    }

    public final void finishDragging(boolean z) {
        DraggingItemBean draggingItemBean;
        if (!isDragging()) {
            return;
        }
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null && (draggingItemBean = this.mDraggingItemBean) != null && draggingItemBean.viewHolder != null) {
            recyclerView.setOverScrollMode(this.mOriginOverScrollMode);
        }
        if (this.mDraggingItemDecorator != null) {
            final int i = this.mDraggingItemBean.mDraggingItemRealPosition;
            final int onBeforeDragItemEnd = this.mOnDragCallback.onBeforeDragItemEnd(this.mRecyclerView, this.mLastTouchX, this.mLastTouchY, i);
            this.mDraggingItemDecorator.setReturnToDefaultPositionAnimationDuration(this.mConfig.getDragItemReturnToSourcePositionAnimDuration());
            this.mDraggingItemDecorator.setReturnToDefaultPositionAnimationInterpolator(this.mConfig.getDragItemReturnToSourcePositionAnimInterpolator());
            this.mDraggingItemDecorator.setOnAnimatorFinshCallback(new BaseDraggableItemDecorator.AnimtorFinshCallback() { // from class: com.miui.itemdrag.RecyclerViewDragItemManager.1
                @Override // com.miui.itemdrag.decorator.BaseDraggableItemDecorator.AnimtorFinshCallback
                public void onFinsh(View view) {
                    if (RecyclerViewDragItemManager.this.mOnDragCallback != null) {
                        if (-1 != onBeforeDragItemEnd) {
                            RecyclerViewDragItemManager.this.mOnDragCallback.onDragItemEnd(i, onBeforeDragItemEnd);
                            RecyclerViewDragItemManager.this.mPreSwapToPosition = onBeforeDragItemEnd;
                        } else {
                            RecyclerViewDragItemManager.this.mOnDragCallback.onDragItemEnd(RecyclerViewDragItemManager.this.mPreSwapFromPosition, RecyclerViewDragItemManager.this.mPreSwapToPosition);
                        }
                    }
                    view.setPressed(false);
                    RecyclerViewDragItemManager.this.mPreSwapToPosition = -1;
                    RecyclerViewDragItemManager.this.mPreSwapFromPosition = -1;
                }
            });
            this.mDraggingItemDecorator.finish(false);
        }
        stopScrollOnDraggingProcess();
        RecyclerView recyclerView2 = this.mRecyclerView;
        if (recyclerView2 != null && recyclerView2.getParent() != null) {
            this.mRecyclerView.getParent().requestDisallowInterceptTouchEvent(false);
        }
        RecyclerView recyclerView3 = this.mRecyclerView;
        if (recyclerView3 != null) {
            recyclerView3.invalidate();
        }
        this.mDraggingItemBean.viewHolder = null;
        this.mDraggingItemBean = null;
        this.mNestedScrollView = null;
        this.mLastTouchX = 0;
        this.mLastTouchY = 0;
        this.mNestedScrollViewScrollX = 0;
        this.mNestedScrollViewScrollY = 0;
        this.mDragStartTouchX = 0;
        this.mDragStartTouchY = 0;
        this.mDragMinTouchX = 0;
        this.mDragMinTouchY = 0;
        this.mDragMaxTouchX = 0;
        this.mDragMaxTouchY = 0;
        OnSwapItemListener onSwapItemListener = this.mOnSwapItemListener;
        if (onSwapItemListener == null) {
            return;
        }
        onSwapItemListener.onSwapItemFinish();
    }

    public final boolean canStartDrag(RecyclerView.ViewHolder viewHolder, int i, int i2) {
        int adapterPosition = viewHolder.getAdapterPosition();
        if (-1 == adapterPosition) {
            adapterPosition = viewHolder.getLayoutPosition();
        }
        return adapterPosition != -1 && this.mOnDragCallback.canStartDrag(viewHolder, i, i2);
    }

    public void cancelDrag() {
        handleActionUpOrCancel(3, false);
        finishDragging(false);
    }

    public final void startScrollOnDraggingProcess() {
        this.mScrollRunnable.start();
    }

    public final void stopScrollOnDraggingProcess() {
        ScrollOnDraggingProcessRunnable scrollOnDraggingProcessRunnable = this.mScrollRunnable;
        if (scrollOnDraggingProcessRunnable != null) {
            scrollOnDraggingProcessRunnable.stop();
        }
    }

    public void checkItemSwapping(RecyclerView recyclerView) {
        RecyclerView.ViewHolder findSwapTargetItem;
        DraggingItemDecorator draggingItemDecorator = this.mDraggingItemDecorator;
        if ((draggingItemDecorator == null || !draggingItemDecorator.isIsScrolling()) && (findSwapTargetItem = this.mOnDragCallback.findSwapTargetItem(recyclerView, this.mLastTouchX, this.mLastTouchY)) != null) {
            if (this.mDraggingItemDecorator != null && findSwapTargetItem == this.mDraggingItemBean.viewHolder) {
                return;
            }
            if (this.mPreSwapItemPosition == findSwapTargetItem.getAdapterPosition() && isDelayedSwap() && this.mHandler.hasSwapItemMsg()) {
                return;
            }
            int adapterPosition = findSwapTargetItem.getAdapterPosition();
            this.mPreSwapItemPosition = adapterPosition;
            if (adapterPosition == -1) {
                return;
            }
            if (isDelayedSwap()) {
                this.mHandler.preSwapItem(this.mDraggingItemBean.mDraggingItemRealPosition, this.mPreSwapItemPosition, this.mConfig.getSwapItemNeedHowLongHover());
            } else {
                startSwapItems(this.mDraggingItemBean.mDraggingItemRealPosition, this.mPreSwapItemPosition);
            }
        }
    }

    public final void startSwapItems(int i, int i2) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        if (!isDragging() || !isEnableSwap()) {
            return;
        }
        if ((this.mScrollRunnable != null && this.mDraggingItemDecorator.isIsScrolling()) || i2 != this.mPreSwapItemPosition || RecyclerViewUtils.findChildViewHolderUnderWithoutTranslation(this.mRecyclerView, this.mLastTouchX, this.mLastTouchY) != (findViewHolderForAdapterPosition = this.mRecyclerView.findViewHolderForAdapterPosition(i2))) {
            return;
        }
        this.mPreSwapToPosition = i2;
        Log.d("DragItemManager", "改变drag position:" + i2);
        if (i2 == -1) {
            return;
        }
        DraggingItemBean draggingItemBean = this.mDraggingItemBean;
        RecyclerView.ViewHolder viewHolder = draggingItemBean != null ? draggingItemBean.viewHolder : null;
        boolean z = false;
        int findFirstVisibleItemPosition = RecyclerViewUtils.findFirstVisibleItemPosition(this.mRecyclerView, false);
        int layoutPosition = viewHolder != null ? viewHolder.getLayoutPosition() : -1;
        int layoutPosition2 = findViewHolderForAdapterPosition.getLayoutPosition();
        boolean z2 = findFirstVisibleItemPosition == layoutPosition;
        if (findFirstVisibleItemPosition == layoutPosition2) {
            z = true;
        }
        if (!this.mOnDragCallback.onMoveItem(this.mRecyclerView, i, i2)) {
            if (z || z2) {
                getAdapter().notifyDataSetChanged();
            } else {
                getAdapter().notifyItemMoved(i, i2);
            }
            OnSwapItemListener onSwapItemListener = this.mOnSwapItemListener;
            if (onSwapItemListener != null) {
                onSwapItemListener.onSwapItem();
            }
        }
        this.mDraggingItemBean.mDraggingItemRealPosition = i2;
        this.mPreSwapItemPosition = -1;
    }

    public final boolean checkTouchedItemState(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder == null) {
            return false;
        }
        if (this.mAdapter == null) {
            this.mAdapter = this.mRecyclerView.getAdapter();
        }
        if (this.mAdapter == null) {
            throw new IllegalStateException("adapter cant be null!");
        }
        int adapterPosition = viewHolder.getAdapterPosition();
        return adapterPosition >= 0 && adapterPosition < this.mRecyclerView.getAdapter().getItemCount();
    }

    public static void safeEndAnimation(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.ItemAnimator itemAnimator = recyclerView != null ? recyclerView.getItemAnimator() : null;
        if (itemAnimator != null) {
            itemAnimator.endAnimation(viewHolder);
        }
    }

    public void setEnlargeItemEnable(boolean z) {
        this.isEnlargeItemEnable = z;
    }

    public void setSwapItemListener(OnSwapItemListener onSwapItemListener) {
        this.mOnSwapItemListener = onSwapItemListener;
    }

    /* loaded from: classes3.dex */
    public static class InternalHandler extends Handler {
        public MotionEvent mDownMotionEvent;
        public RecyclerViewDragItemManager mHolder;

        public InternalHandler(RecyclerViewDragItemManager recyclerViewDragItemManager) {
            this.mHolder = recyclerViewDragItemManager;
        }

        public void release() {
            removeCallbacksAndMessages(null);
            this.mHolder = null;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                this.mHolder.handleOnLongPress(this.mDownMotionEvent);
            } else if (i != 2) {
            } else {
                this.mHolder.startSwapItems(message.arg1, message.arg2);
            }
        }

        public void startLongPressDetection(MotionEvent motionEvent, int i) {
            Log.d("DragItemManager", "send long pressed msg");
            cancelLongPressDetection();
            this.mDownMotionEvent = MotionEvent.obtain(motionEvent);
            sendEmptyMessageDelayed(1, i);
        }

        public void preSwapItem(int i, int i2, long j) {
            removeMessages(2);
            Message obtainMessage = obtainMessage(2);
            obtainMessage.arg1 = i;
            obtainMessage.arg2 = i2;
            obtainMessage.what = 2;
            sendMessageDelayed(obtainMessage, j);
        }

        public boolean hasSwapItemMsg() {
            return hasMessages(2);
        }

        public void cancelLongPressDetection() {
            removeMessages(1);
            MotionEvent motionEvent = this.mDownMotionEvent;
            if (motionEvent != null) {
                motionEvent.recycle();
                this.mDownMotionEvent = null;
            }
        }

        public boolean isLongPressedRequested() {
            return hasMessages(1);
        }
    }

    public void handleOnLongPress(MotionEvent motionEvent) {
        checkConditionAndStartDragging(this.mRecyclerView, motionEvent, true);
    }

    /* loaded from: classes3.dex */
    public interface OnDragCallback {
        boolean canStartDrag(RecyclerView.ViewHolder viewHolder, int i, int i2);

        default int onBeforeDragItemEnd(RecyclerView recyclerView, int i, int i2, int i3) {
            return -1;
        }

        default boolean onBeforeDragItemStart(RecyclerView.ViewHolder viewHolder) {
            return true;
        }

        void onDragItemEnd(int i, int i2);

        default void onDragItemStarted(RecyclerView.ViewHolder viewHolder, int i, int i2) {
        }

        boolean onMoveItem(RecyclerView recyclerView, int i, int i2);

        default void onTouchMoveWhenStartDrag(int i, int i2) {
        }

        default RecyclerView.ViewHolder findSwapTargetItem(RecyclerView recyclerView, int i, int i2) {
            return RecyclerViewUtils.findChildViewHolderUnderWithoutTranslation(recyclerView, i, i2);
        }
    }

    public RecyclerView.Adapter getAdapter() {
        RecyclerView.Adapter adapter = this.mAdapter;
        return adapter instanceof WrapperSource ? (RecyclerView.Adapter) ((WrapperSource) adapter).mo1836getSource() : adapter;
    }

    /* loaded from: classes3.dex */
    public class DragSimpleAdapterWrapper<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements WrapperSource<RecyclerView.Adapter<VH>> {
        public RecyclerView.Adapter<VH> mWrapped;

        public DragSimpleAdapterWrapper(RecyclerView.Adapter<VH> adapter) {
            this.mWrapped = adapter;
            super.setHasStableIds(adapter.hasStableIds());
        }

        @Override // com.miui.itemdrag.WrapperSource
        /* renamed from: getSource */
        public RecyclerView.Adapter<VH> mo1836getSource() {
            RecyclerView.Adapter<VH> adapter = this.mWrapped;
            return adapter instanceof WrapperSource ? (RecyclerView.Adapter) ((WrapperSource) adapter).mo1836getSource() : adapter;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public VH mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return this.mWrapped.mo1843onCreateViewHolder(viewGroup, i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(VH vh, int i) {
            onBindViewHolder(vh, i, Collections.emptyList());
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(VH vh, int i, List<Object> list) {
            this.mWrapped.onBindViewHolder(vh, i, list);
            checkAndResetHolder(vh);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mWrapped.getItemCount();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public long getItemId(int i) {
            return this.mWrapped.getItemId(i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(VH vh) {
            this.mWrapped.onViewRecycled(vh);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public boolean onFailedToRecycleView(VH vh) {
            return this.mWrapped.onFailedToRecycleView(vh);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewAttachedToWindow(VH vh) {
            this.mWrapped.onViewAttachedToWindow(vh);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewDetachedFromWindow(VH vh) {
            this.mWrapped.onViewDetachedFromWindow(vh);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.mWrapped.registerAdapterDataObserver(adapterDataObserver);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.mWrapped.unregisterAdapterDataObserver(adapterDataObserver);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            this.mWrapped.onAttachedToRecyclerView(recyclerView);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            this.mWrapped.onDetachedFromRecyclerView(recyclerView);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int i) {
            return this.mWrapped.getItemViewType(i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void setHasStableIds(boolean z) {
            super.setHasStableIds(z);
            this.mWrapped.setHasStableIds(z);
        }

        public final void checkAndResetHolder(VH vh) {
            if (RecyclerViewDragItemManager.this.isDraggingHolder(vh)) {
                RecyclerViewDragItemManager.this.setDragItemHolder(vh);
            } else {
                vh.itemView.setVisibility(0);
            }
        }
    }

    public final void setDragItemHolder(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder != this.mDraggingItemBean.viewHolder) {
            this.mDraggingItemDecorator.invalidateDraggingItem();
            this.mDraggingItemDecorator.setDraggingItemViewHolder(viewHolder);
        }
    }

    public final boolean isDraggingHolder(RecyclerView.ViewHolder viewHolder) {
        return isDragging() && viewHolder.getItemId() == this.mDraggingItemBean.id;
    }

    /* loaded from: classes3.dex */
    public static class Config {
        public OnDragItemEffectCallback OnDragItemEffectCallback;
        public final int SCROLL_BASIC_AMOUNT;
        public SparseIntArray mAnimationDurations;
        public SparseArray<Interpolator> mAnimationInterpolators;
        public int mDragItemReturnToSourcePositionAnimDuration;
        public Interpolator mDragItemReturnToSourcePositionAnimInterpolator;
        public int mDragItemScrollCoeff;
        public float mDragItemScrollSpeed;
        public float mDragItemScrollThreshold;
        public int mEnterDragNeedHowLongOffset;
        public int mLongPressTimeout;
        public OnDragCallback mOnDragCallback;
        public int mSwapItemNeedHowLongHover;

        public Config(int i, int i2, int i3, Interpolator interpolator, SparseIntArray sparseIntArray, SparseArray<Interpolator> sparseArray, OnDragCallback onDragCallback, int i4, float f, float f2, int i5, OnDragItemEffectCallback onDragItemEffectCallback) {
            this.SCROLL_BASIC_AMOUNT = 25;
            this.mDragItemReturnToSourcePositionAnimDuration = i;
            this.mSwapItemNeedHowLongHover = i2;
            this.mLongPressTimeout = i3;
            this.mDragItemReturnToSourcePositionAnimInterpolator = interpolator;
            this.mAnimationDurations = sparseIntArray;
            this.mAnimationInterpolators = sparseArray;
            this.mOnDragCallback = onDragCallback;
            this.mEnterDragNeedHowLongOffset = i4;
            this.OnDragItemEffectCallback = onDragItemEffectCallback;
            this.mDragItemScrollSpeed = 0.0f == f ? 0.4f : f;
            this.mDragItemScrollThreshold = 0.0f == f2 ? 0.2f : f2;
            this.mDragItemScrollCoeff = i5 == 0 ? 25 : i5;
        }

        public Config(Builder builder) {
            this(builder.mDragItemReturnToSourcePositionAnimDuration, builder.mSwapItemNeedHowLongHover, builder.mLongPressTimeout, builder.mDragItemReturnToSourcePositionAnimInterpolator, builder.mAnimationDurations, builder.mAnimationInterpolators, builder.mOnDragCallback, builder.mEnterDragNeedHowLongOffset, builder.mDragItemScrollSpeed, builder.mDragItemScrollThreshold, builder.mDragItemScrollCoeff, builder.mOnDragItemEffectCallback);
        }

        /* loaded from: classes3.dex */
        public static class Builder {
            public SparseIntArray mAnimationDurations;
            public SparseArray<Interpolator> mAnimationInterpolators;
            public int mDragItemReturnToSourcePositionAnimDuration;
            public Interpolator mDragItemReturnToSourcePositionAnimInterpolator;
            public int mDragItemScrollCoeff;
            public float mDragItemScrollSpeed;
            public float mDragItemScrollThreshold;
            public int mEnterDragNeedHowLongOffset;
            public int mLongPressTimeout;
            public OnDragCallback mOnDragCallback;
            public OnDragItemEffectCallback mOnDragItemEffectCallback;
            public int mSwapItemNeedHowLongHover;

            public Builder(OnDragCallback onDragCallback) {
                this.mOnDragCallback = onDragCallback;
            }

            public Builder setDragItemReturnToSourcePositionAnimDuration(int i) {
                this.mDragItemReturnToSourcePositionAnimDuration = i;
                return this;
            }

            public Builder setSwapItemNeedHowLongHover(int i) {
                this.mSwapItemNeedHowLongHover = i;
                return this;
            }

            public Builder setLongPressTimeout(int i) {
                this.mLongPressTimeout = i;
                return this;
            }

            public Builder setAnimDuration(int i, int i2) {
                if (this.mAnimationDurations == null) {
                    this.mAnimationDurations = new SparseIntArray(4);
                }
                this.mAnimationDurations.put(i, i2);
                return this;
            }

            public Builder setAnimInterpolator(int i, Interpolator interpolator) {
                if (this.mAnimationInterpolators == null) {
                    this.mAnimationInterpolators = new SparseArray<>(4);
                }
                this.mAnimationInterpolators.put(i, interpolator);
                return this;
            }

            public Builder setOnDragItemEffectCallback(OnDragItemEffectCallback onDragItemEffectCallback) {
                this.mOnDragItemEffectCallback = onDragItemEffectCallback;
                return this;
            }

            public Config build() {
                return new Config(this);
            }
        }

        public int getDragItemReturnToSourcePositionAnimDuration() {
            return this.mDragItemReturnToSourcePositionAnimDuration;
        }

        public int getSwapItemNeedHowLongHover() {
            return this.mSwapItemNeedHowLongHover;
        }

        public int getLongPressTimeout() {
            return this.mLongPressTimeout;
        }

        public Interpolator getDragItemReturnToSourcePositionAnimInterpolator() {
            return this.mDragItemReturnToSourcePositionAnimInterpolator;
        }

        public SparseIntArray getAnimationDurations() {
            return this.mAnimationDurations;
        }

        public SparseArray<Interpolator> getAnimationInterpolators() {
            return this.mAnimationInterpolators;
        }

        public OnDragCallback getDragCallback() {
            return this.mOnDragCallback;
        }

        public OnDragItemEffectCallback getDragItemEffectCallback() {
            return this.OnDragItemEffectCallback;
        }

        public int getEnterDragNeedHowLongOffset() {
            return this.mEnterDragNeedHowLongOffset;
        }

        public float getDragItemScrollSpeed() {
            return this.mDragItemScrollSpeed;
        }

        public float getDragItemScrollThreshold() {
            return this.mDragItemScrollThreshold;
        }

        public int getDragItemScrollCoeff() {
            return this.mDragItemScrollCoeff;
        }
    }
}
