package com.miui.itemdrag.decorator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.itemdrag.DraggingItemBean;
import com.miui.itemdrag.RecyclerViewDragItemManager;
import com.miui.itemdrag.RecyclerViewUtils;

/* loaded from: classes3.dex */
public class DraggingItemDecorator extends BaseDraggableItemDecorator {
    public Runnable mCreateBitmapRunnable;
    public DraggingItemBean mDraggingItemBean;
    public Bitmap mDraggingItemImage;
    public Bitmap mEnlargeDraggingItemImage;
    public boolean mIsScrolling;
    public int mLayoutOrientation;
    public int mLayoutType;
    public RecyclerViewDragItemManager.OnDragItemEffectCallback mOnDragItemEffectCallback;
    public Paint mPaint;
    public long mStartAnimationDurationMillis;
    public long mStartMillis;
    public boolean mStarted;
    public int mTouchPositionX;
    public int mTouchPositionY;
    public int mTranslationBottomLimit;
    public int mTranslationItemLeftX;
    public int mTranslationItemTopY;
    public int mTranslationLeftLimit;
    public int mTranslationRightLimit;
    public int mTranslationTopLimit;

    public DraggingItemDecorator(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super(recyclerView, viewHolder);
        this.mStartAnimationDurationMillis = 0L;
        this.mPaint = new Paint();
    }

    public static View findRangeFirstItem(RecyclerView recyclerView, int i, int i2) {
        int layoutPosition;
        if (i == -1 || i2 == -1) {
            return null;
        }
        int childCount = recyclerView.getChildCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = recyclerView.getChildAt(i3);
            RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(childAt);
            if (childViewHolder != null && (layoutPosition = childViewHolder.getLayoutPosition()) >= i && layoutPosition <= i2) {
                return childAt;
            }
        }
        return null;
    }

    public static View findRangeLastItem(RecyclerView recyclerView, int i, int i2) {
        int layoutPosition;
        if (i == -1 || i2 == -1) {
            return null;
        }
        for (int childCount = recyclerView.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = recyclerView.getChildAt(childCount);
            RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(childAt);
            if (childViewHolder != null && (layoutPosition = childViewHolder.getLayoutPosition()) >= i && layoutPosition <= i2) {
                return childAt;
            }
        }
        return null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        if (this.mEnlargeDraggingItemImage == null) {
            return;
        }
        int min = (int) Math.min(System.currentTimeMillis() - this.mStartMillis, this.mStartAnimationDurationMillis);
        long j = this.mStartAnimationDurationMillis;
        float f = j > 0 ? min / ((float) j) : 1.0f;
        int width = (this.mEnlargeDraggingItemImage.getWidth() - this.mDraggingItemImage.getWidth()) / 2;
        int height = (this.mEnlargeDraggingItemImage.getHeight() - this.mDraggingItemImage.getHeight()) / 2;
        RecyclerViewDragItemManager.OnDragItemEffectCallback onDragItemEffectCallback = this.mOnDragItemEffectCallback;
        if (onDragItemEffectCallback == null || !onDragItemEffectCallback.onDraw(canvas, this.mTranslationItemLeftX, this.mTranslationItemTopY, this.mEnlargeDraggingItemImage, this.mPaint, width, height)) {
            int save = canvas.save();
            canvas.translate(this.mTranslationItemLeftX, this.mTranslationItemTopY);
            canvas.drawBitmap(this.mEnlargeDraggingItemImage, -width, -height, this.mPaint);
            canvas.restoreToCount(save);
        }
        if (f >= 1.0f) {
            return;
        }
        ViewCompat.postInvalidateOnAnimation(recyclerView);
    }

    public final Bitmap enlargeBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(1.1f, 1.1f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public void setupDraggingItemEffects(DraggingItemEffectsInfo draggingItemEffectsInfo) {
        this.mStartAnimationDurationMillis = draggingItemEffectsInfo.durationMillis;
    }

    public void start(DraggingItemBean draggingItemBean, int i, int i2, final boolean z) {
        if (this.mStarted) {
            return;
        }
        final View view = this.mDraggingItemViewHolder.itemView;
        Runnable runnable = new Runnable() { // from class: com.miui.itemdrag.decorator.DraggingItemDecorator.1
            @Override // java.lang.Runnable
            public void run() {
                DraggingItemDecorator draggingItemDecorator = DraggingItemDecorator.this;
                draggingItemDecorator.mDraggingItemImage = draggingItemDecorator.createDraggingItemImage(view);
                DraggingItemDecorator draggingItemDecorator2 = DraggingItemDecorator.this;
                draggingItemDecorator2.mEnlargeDraggingItemImage = z ? draggingItemDecorator2.enlargeBitmap(draggingItemDecorator2.mDraggingItemImage) : draggingItemDecorator2.mDraggingItemImage;
                view.setVisibility(4);
            }
        };
        this.mCreateBitmapRunnable = runnable;
        view.postDelayed(runnable, 0L);
        this.mDraggingItemBean = draggingItemBean;
        this.mTranslationLeftLimit = this.mRecyclerView.getPaddingLeft();
        this.mTranslationTopLimit = this.mRecyclerView.getPaddingTop();
        this.mLayoutOrientation = RecyclerViewUtils.getOrientation(this.mRecyclerView);
        this.mLayoutType = RecyclerViewUtils.getLayoutType(this.mRecyclerView);
        update(i, i2, true);
        this.mRecyclerView.addItemDecoration(this);
        this.mStartMillis = System.currentTimeMillis();
        this.mStarted = true;
        this.mDraggingItemViewHolder.setIsRecyclable(false);
    }

    public void finish(boolean z) {
        RecyclerView.ViewHolder viewHolder;
        View view;
        RecyclerView.ItemAnimator itemAnimator;
        if (this.mStarted) {
            this.mRecyclerView.removeItemDecoration(this);
        }
        if (z && (itemAnimator = this.mRecyclerView.getItemAnimator()) != null) {
            itemAnimator.endAnimations();
        }
        Runnable runnable = this.mCreateBitmapRunnable;
        if (runnable != null && (viewHolder = this.mDraggingItemViewHolder) != null && (view = viewHolder.itemView) != null) {
            view.removeCallbacks(runnable);
            this.mDraggingItemViewHolder.itemView.setVisibility(0);
        }
        internalFinish();
    }

    public final void internalFinish() {
        this.mRecyclerView.stopScroll();
        updateDraggingItemPosition(this.mTranslationItemLeftX, this.mTranslationItemTopY);
        RecyclerView.ViewHolder viewHolder = this.mDraggingItemViewHolder;
        if (viewHolder != null) {
            moveToDefaultPosition(viewHolder.itemView, true);
        }
        RecyclerView.ViewHolder viewHolder2 = this.mDraggingItemViewHolder;
        if (viewHolder2 != null) {
            viewHolder2.itemView.setVisibility(0);
        }
        this.mDraggingItemViewHolder.setIsRecyclable(true);
        this.mDraggingItemViewHolder = null;
        Bitmap bitmap = this.mDraggingItemImage;
        if (bitmap != null) {
            bitmap.recycle();
            this.mDraggingItemImage = null;
        }
        Bitmap bitmap2 = this.mEnlargeDraggingItemImage;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.mEnlargeDraggingItemImage = null;
        }
        this.mTranslationItemLeftX = 0;
        this.mTranslationItemTopY = 0;
        this.mTranslationLeftLimit = 0;
        this.mTranslationRightLimit = 0;
        this.mTranslationTopLimit = 0;
        this.mTranslationBottomLimit = 0;
        this.mTouchPositionX = 0;
        this.mTouchPositionY = 0;
        this.mStarted = false;
    }

    public boolean update(int i, int i2, boolean z) {
        this.mTouchPositionX = i;
        this.mTouchPositionY = i2;
        return refresh(z);
    }

    public boolean refresh(boolean z) {
        int i = this.mTranslationItemLeftX;
        int i2 = this.mTranslationItemTopY;
        updateTranslationOffset();
        boolean z2 = (i == this.mTranslationItemLeftX && i2 == this.mTranslationItemTopY) ? false : true;
        if (z2 || z) {
            ViewCompat.postInvalidateOnAnimation(this.mRecyclerView);
        }
        return z2;
    }

    public final void updateTranslationOffset() {
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView.getChildCount() > 0) {
            this.mTranslationLeftLimit = 0;
            this.mTranslationRightLimit = recyclerView.getWidth() - this.mDraggingItemBean.width;
            this.mTranslationTopLimit = 0;
            this.mTranslationBottomLimit = recyclerView.getHeight() - this.mDraggingItemBean.height;
            int i = this.mLayoutOrientation;
            if (i == 0) {
                this.mTranslationTopLimit += recyclerView.getPaddingTop();
                this.mTranslationBottomLimit -= recyclerView.getPaddingBottom();
                this.mTranslationLeftLimit = -this.mDraggingItemBean.width;
                this.mTranslationRightLimit = recyclerView.getWidth();
            } else if (i == 1) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                DraggingItemBean draggingItemBean = this.mDraggingItemBean;
                View view = draggingItemBean.viewHolder.itemView;
                this.mTranslationTopLimit = -draggingItemBean.height;
                this.mTranslationBottomLimit = recyclerView.getHeight();
                this.mTranslationLeftLimit += layoutManager.getLeftDecorationWidth(view);
                this.mTranslationRightLimit -= layoutManager.getRightDecorationWidth(view);
            }
            this.mTranslationRightLimit = Math.max(this.mTranslationLeftLimit, this.mTranslationRightLimit);
            this.mTranslationBottomLimit = Math.max(this.mTranslationTopLimit, this.mTranslationBottomLimit);
            if (!this.mIsScrolling) {
                int findFirstVisibleItemPosition = RecyclerViewUtils.findFirstVisibleItemPosition(recyclerView, true);
                int findLastVisibleItemPosition = RecyclerViewUtils.findLastVisibleItemPosition(recyclerView, true);
                View findRangeFirstItem = findRangeFirstItem(recyclerView, findFirstVisibleItemPosition, findLastVisibleItemPosition);
                View findRangeLastItem = findRangeLastItem(recyclerView, findFirstVisibleItemPosition, findLastVisibleItemPosition);
                int i2 = this.mLayoutOrientation;
                if (i2 == 0) {
                    if (findRangeFirstItem != null) {
                        this.mTranslationLeftLimit = Math.min(this.mTranslationLeftLimit, findRangeFirstItem.getLeft());
                    }
                    if (findRangeLastItem != null) {
                        this.mTranslationRightLimit = Math.min(this.mTranslationRightLimit, Math.max(0, findRangeLastItem.getRight() - this.mDraggingItemBean.width));
                    }
                } else if (i2 == 1) {
                    if (findRangeFirstItem != null) {
                        this.mTranslationTopLimit = Math.min(this.mTranslationBottomLimit, findRangeFirstItem.getTop());
                    }
                    if (findRangeLastItem != null) {
                        this.mTranslationBottomLimit = Math.min(this.mTranslationBottomLimit, Math.max(0, findRangeLastItem.getBottom() - this.mDraggingItemBean.height));
                    }
                }
            }
        } else {
            int paddingLeft = recyclerView.getPaddingLeft();
            this.mTranslationLeftLimit = paddingLeft;
            this.mTranslationRightLimit = paddingLeft;
            int paddingTop = recyclerView.getPaddingTop();
            this.mTranslationTopLimit = paddingTop;
            this.mTranslationBottomLimit = paddingTop;
        }
        int i3 = this.mTouchPositionX;
        DraggingItemBean draggingItemBean2 = this.mDraggingItemBean;
        this.mTranslationItemLeftX = i3 - draggingItemBean2.grabbedPositionX;
        this.mTranslationItemTopY = this.mTouchPositionY - draggingItemBean2.grabbedPositionY;
        if (RecyclerViewUtils.isLinearLayoutType(this.mLayoutType) || RecyclerViewUtils.isGridlayoutAndIsSingleSpanCountLayoutType(this.mRecyclerView)) {
            this.mTranslationItemLeftX = clip(this.mTranslationItemLeftX, this.mTranslationLeftLimit, this.mTranslationRightLimit);
        }
    }

    public static int clip(int i, int i2, int i3) {
        return Math.min(Math.max(i, i2), i3);
    }

    public final Bitmap createDraggingItemImage(View view) {
        Bitmap createBitmap;
        RecyclerViewDragItemManager.OnDragItemEffectCallback onDragItemEffectCallback = this.mOnDragItemEffectCallback;
        if (onDragItemEffectCallback == null || (createBitmap = onDragItemEffectCallback.onCreateDragView(view)) == null) {
            int top = view.getTop();
            int left = view.getLeft();
            int width = view.getWidth();
            int height = view.getHeight();
            view.measure(View.MeasureSpec.makeMeasureSpec(width, 1073741824), View.MeasureSpec.makeMeasureSpec(height, 1073741824));
            view.layout(left, top, left + width, top + height);
            createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public void updateDraggingItemPosition(float f, int i) {
        RecyclerView.ViewHolder viewHolder = this.mDraggingItemViewHolder;
        if (viewHolder != null) {
            BaseDraggableItemDecorator.setItemTranslation(this.mRecyclerView, viewHolder, f - viewHolder.itemView.getLeft(), i - this.mDraggingItemViewHolder.itemView.getTop());
        }
    }

    public void setIsScrolling(boolean z) {
        if (this.mIsScrolling == z) {
            return;
        }
        this.mIsScrolling = z;
    }

    public boolean isIsScrolling() {
        return this.mIsScrolling;
    }

    public void invalidateDraggingItem() {
        RecyclerView.ViewHolder viewHolder = this.mDraggingItemViewHolder;
        if (viewHolder != null) {
            viewHolder.itemView.setTranslationX(0.0f);
            this.mDraggingItemViewHolder.itemView.setTranslationY(0.0f);
            this.mDraggingItemViewHolder.itemView.setVisibility(0);
        }
        this.mDraggingItemViewHolder = null;
    }

    public void setDraggingItemViewHolder(RecyclerView.ViewHolder viewHolder) {
        if (this.mDraggingItemViewHolder != null) {
            throw new IllegalStateException("A new view holder is attempt to be assigned before invalidating the older one");
        }
        this.mDraggingItemViewHolder = viewHolder;
        viewHolder.itemView.setVisibility(4);
        DraggingItemBean draggingItemBean = this.mDraggingItemBean;
        RecyclerView.ViewHolder viewHolder2 = this.mDraggingItemViewHolder;
        draggingItemBean.viewHolder = viewHolder2;
        draggingItemBean.mDraggingItemRealPosition = viewHolder2.getLayoutPosition();
    }

    public void setOnDragItemEffectCallback(RecyclerViewDragItemManager.OnDragItemEffectCallback onDragItemEffectCallback) {
        this.mOnDragItemEffectCallback = onDragItemEffectCallback;
    }
}
