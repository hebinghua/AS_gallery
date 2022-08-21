package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.tracing.Trace;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import com.miui.gallery.ui.pictures.ScrollingCalculator;
import com.miui.gallery.util.photoview.ScrollableView;
import com.miui.gallery.widget.ViewUtils;
import com.miui.gallery.widget.recyclerview.FastScrollerBar;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import java.util.LinkedList;
import java.util.List;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class GalleryRecyclerView extends RecyclerView implements ItemClickSupport.OnItemLongClickListener, ScrollableView {
    public boolean isHideViewCalled;
    public AdapterPos2ViewPosConverter mAdapterPos2ViewPosConverter;
    public ContextMenu.ContextMenuInfo mContextMenuInfo;
    public FastScrollerBar mCustomFastScroller;
    public View mEmptyView;
    public boolean mEnableCustomFastScroller;
    public boolean mEnableImmerse;
    public final Rect mFirstViewVisibleLocation;
    public ImmersiveHeaderDecoration mImmersiveDecor;
    public boolean mIsFastScrollerPressed;
    public ItemClickSupport mItemClickSupport;
    public final RecyclerViewDataObserver mObserver;
    public ItemClickSupport.OnItemLongClickListener mOnItemLongClickListener;
    public final List<RecyclerView.OnItemTouchListener> mOnItemTouchListeners;
    public PendingShowPlaceholderTask mPendingShowPlaceholderTask;
    public View mPlaceholderItem;
    public ScrollingCalculator mScrollingCalculator;

    /* loaded from: classes3.dex */
    public interface AdapterPos2ViewPosConverter {
        int convert(int i);
    }

    public GalleryRecyclerView(Context context) {
        this(context, null);
    }

    public GalleryRecyclerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GalleryRecyclerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mContextMenuInfo = null;
        this.mOnItemTouchListeners = new LinkedList();
        this.mFirstViewVisibleLocation = new Rect();
        this.isHideViewCalled = false;
        this.mObserver = new RecyclerViewDataObserver();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.RecyclerView, i, 0);
            this.mEnableCustomFastScroller = obtainStyledAttributes.getBoolean(4, false);
            obtainStyledAttributes.recycle();
        }
        initItemClick();
    }

    public void initItemClick() {
        ItemClickSupport addTo = ItemClickSupport.addTo(this);
        this.mItemClickSupport = addTo;
        addTo.setOnItemLongClickListener(this);
    }

    public void setFastScrollStyle(int i) {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            fastScrollerBar.setStyle(i);
        }
    }

    public void setFastScrollCapsuleStyle(int i) {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            fastScrollerBar.setCapsuleStyle(i);
        }
    }

    public boolean isFastScrollerPressed() {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            return fastScrollerBar.isFastScrollerPressed() || this.mCustomFastScroller.isProportionTagViewPressed();
        }
        return false;
    }

    public void setFastScrollerInvisible(boolean z) {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            fastScrollerBar.setFastScrollerInvisible(z);
        }
    }

    public void setFastScrollEnabled(boolean z) {
        if (this.mEnableCustomFastScroller != z) {
            this.mEnableCustomFastScroller = z;
            if (z) {
                if (this.mCustomFastScroller == null) {
                    FastScrollerThumbView fastScrollerThumbView = new FastScrollerThumbView(getContext());
                    fastScrollerThumbView.setStyle(R.style.FastScroll);
                    this.mCustomFastScroller = new FastScrollerBar(this, fastScrollerThumbView);
                }
                this.mCustomFastScroller.setBottomMargin(getResources().getDimensionPixelOffset(R.dimen.fast_scroller_safe_distance_bottom));
                this.mCustomFastScroller.attach();
                return;
            }
            FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
            if (fastScrollerBar == null) {
                return;
            }
            fastScrollerBar.detach();
        }
    }

    public void setImmerseEnable(boolean z) {
        if (this.mEnableImmerse != z) {
            this.mEnableImmerse = z;
            if (z) {
                if (this.mImmersiveDecor != null) {
                    return;
                }
                ImmersiveHeaderDrawer immersiveHeaderDrawer = new ImmersiveHeaderDrawer(getContext());
                immersiveHeaderDrawer.setStyle(R.style.immersiveHeader);
                ImmersiveHeaderDecoration immersiveHeaderDecoration = new ImmersiveHeaderDecoration(this, immersiveHeaderDrawer);
                this.mImmersiveDecor = immersiveHeaderDecoration;
                immersiveHeaderDecoration.attach();
                return;
            }
            ImmersiveHeaderDecoration immersiveHeaderDecoration2 = this.mImmersiveDecor;
            if (immersiveHeaderDecoration2 == null) {
                return;
            }
            immersiveHeaderDecoration2.detach();
        }
    }

    public void setAdapterPos2ViewPosConverter(AdapterPos2ViewPosConverter adapterPos2ViewPosConverter) {
        this.mAdapterPos2ViewPosConverter = adapterPos2ViewPosConverter;
    }

    public void setOnFastScrollerStateChangedListener(FastScrollerBar.OnStateChangedListener onStateChangedListener) {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            fastScrollerBar.setOnStateChangedListener(onStateChangedListener);
        }
    }

    public void setFastScrollerTopMargin(int i) {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            fastScrollerBar.setTopMargin(i);
        }
    }

    public int getFastScrollerTopMargin() {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            return fastScrollerBar.getTopMargin();
        }
        return 0;
    }

    public void setFastScrollerBottomMargin(int i) {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            fastScrollerBar.setBottomMargin(i);
        }
    }

    public int getFastScrollerBottomMargin() {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            return fastScrollerBar.getBottomMargin();
        }
        return 0;
    }

    public void setProportionTagAdapterProvider(ProportionTagAdapterProvider<Integer> proportionTagAdapterProvider) {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            fastScrollerBar.setProportionTagAdapterProvider(proportionTagAdapterProvider);
        }
    }

    public void setFastScrollerCapsuleViewProvider(FastScrollerCapsuleViewProvider fastScrollerCapsuleViewProvider) {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            fastScrollerBar.setFastScrollerCapsuleViewProvider(fastScrollerCapsuleViewProvider);
        }
    }

    public void setCapsuleCalculator(FastScrollerCapsuleCalculator fastScrollerCapsuleCalculator) {
        FastScrollerBar fastScrollerBar = this.mCustomFastScroller;
        if (fastScrollerBar != null) {
            fastScrollerBar.setCapsuleCalculator(fastScrollerCapsuleCalculator);
        }
    }

    public void hideScrollerBar() {
        FastScrollerBar fastScrollerBar;
        if (!this.mEnableCustomFastScroller || (fastScrollerBar = this.mCustomFastScroller) == null || fastScrollerBar.isFastScrollerPressed()) {
            return;
        }
        this.mCustomFastScroller.hideScrollerBar();
        this.mCustomFastScroller.hideCapsule();
        this.mCustomFastScroller.hideProportionTag();
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration, int i) {
        super.addItemDecoration(itemDecoration, i);
        if (this.mCustomFastScroller != null) {
            bringFastScrollerToFront();
        }
    }

    public final void bringFastScrollerToFront() {
        removeItemDecoration(this.mCustomFastScroller);
        super.addItemDecoration(this.mCustomFastScroller, -1);
    }

    public void setOnItemClickListener(ItemClickSupport.OnItemClickListener onItemClickListener) {
        this.mItemClickSupport.setOnItemClickListener(onItemClickListener);
    }

    public void setOnItemLongClickListener(ItemClickSupport.OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void addOnItemTouchListener(RecyclerView.OnItemTouchListener onItemTouchListener) {
        super.addOnItemTouchListener(onItemTouchListener);
        this.mOnItemTouchListeners.add(onItemTouchListener);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void removeOnItemTouchListener(RecyclerView.OnItemTouchListener onItemTouchListener) {
        super.removeOnItemTouchListener(onItemTouchListener);
        this.mOnItemTouchListeners.remove(onItemTouchListener);
    }

    public RecyclerView.OnItemTouchListener getItemTouchListenerAt(int i) {
        int size = this.mOnItemTouchListeners.size();
        if (i > -1 && i < size) {
            return this.mOnItemTouchListeners.get(i);
        }
        throw new IndexOutOfBoundsException(i + " is an invalid index for size " + size);
    }

    public int getItemTouchListenerCount() {
        return this.mOnItemTouchListeners.size();
    }

    @Override // android.view.View
    public ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return this.mContextMenuInfo;
    }

    public final boolean openContextMenu(int i) {
        if (i >= 0) {
            createContextMenuInfo(i);
        }
        return showContextMenu();
    }

    public void createContextMenuInfo(int i) {
        ContextMenu.ContextMenuInfo contextMenuInfo = this.mContextMenuInfo;
        if (contextMenuInfo == null) {
            this.mContextMenuInfo = new RecyclerContextMenuInfo(i);
        } else {
            ((RecyclerContextMenuInfo) contextMenuInfo).setValues(i);
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemLongClickListener
    public boolean onItemLongClick(androidx.recyclerview.widget.RecyclerView recyclerView, View view, int i, long j, float f, float f2, boolean z) {
        ItemClickSupport.OnItemLongClickListener onItemLongClickListener = this.mOnItemLongClickListener;
        return (onItemLongClickListener != null ? onItemLongClickListener.onItemLongClick(recyclerView, view, i, j, f, f2, z) : false) || openContextMenu(i);
    }

    /* loaded from: classes3.dex */
    public static class RecyclerContextMenuInfo implements ContextMenu.ContextMenuInfo {
        public int position;

        public RecyclerContextMenuInfo(int i) {
            setValues(i);
        }

        public void setValues(int i) {
            this.position = i;
        }
    }

    public RecyclerView.ViewHolder findViewHolderForAdapterPositionForExternal(int i) {
        return findViewHolderForAdapterPosition(i);
    }

    public View findChildViewUnderForExternal(float f, float f2) {
        return findChildViewUnder(f, f2);
    }

    public int getChildAdapterPositionForExternal(View view) {
        return getChildAdapterPosition(view);
    }

    public int findFirstVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || layoutManager.getChildCount() <= 0) {
            return -1;
        }
        return getChildAdapterPosition(layoutManager.getChildAt(0));
    }

    public int findLastVisibleItemPosition() {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager == null || layoutManager.getChildCount() <= 0) {
            return -1;
        }
        return getChildAdapterPosition(layoutManager.getChildAt(layoutManager.getChildCount() - 1));
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapterInternal(adapter, false, true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void swapAdapter(RecyclerView.Adapter adapter, boolean z) {
        setAdapterInternal(adapter, true, z);
    }

    private void setAdapterInternal(RecyclerView.Adapter adapter, boolean z, boolean z2) {
        RecyclerView.Adapter adapter2 = getAdapter();
        if (adapter2 != null) {
            adapter2.unregisterAdapterDataObserver(this.mObserver);
        }
        if (adapter == null) {
            return;
        }
        adapter.registerAdapterDataObserver(this.mObserver);
        if (z) {
            super.swapAdapter(adapter, z2);
        } else {
            RecyclerView.LayoutManager layoutManager = getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager.SpanSizeLookup spanSizeLookup = ((GridLayoutManager) layoutManager).getSpanSizeLookup();
                spanSizeLookup.invalidateSpanIndexCache();
                spanSizeLookup.invalidateSpanGroupIndexCache();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                ((StaggeredGridLayoutManager) layoutManager).invalidateSpanAssignments();
            }
            super.setAdapter(adapter);
        }
        updateEmptyStatus();
    }

    /* loaded from: classes3.dex */
    public class RecyclerViewDataObserver extends RecyclerView.AdapterDataObserver {
        public RecyclerViewDataObserver() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            super.onChanged();
            GalleryRecyclerView.this.updateEmptyStatus();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i, int i2) {
            super.onItemRangeChanged(i, i2);
            GalleryRecyclerView.this.updateEmptyStatus();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i, int i2, Object obj) {
            super.onItemRangeChanged(i, i2, obj);
            GalleryRecyclerView.this.updateEmptyStatus();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int i, int i2) {
            super.onItemRangeInserted(i, i2);
            GalleryRecyclerView.this.updateEmptyStatus();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int i, int i2) {
            super.onItemRangeRemoved(i, i2);
            GalleryRecyclerView.this.updateEmptyStatus();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeMoved(int i, int i2, int i3) {
            super.onItemRangeMoved(i, i2, i3);
            GalleryRecyclerView.this.updateEmptyStatus();
        }
    }

    public void setEmptyView(View view) {
        this.mEmptyView = view;
        if (view != null && view.getImportantForAccessibility() == 0) {
            view.setImportantForAccessibility(1);
        }
        updateEmptyStatus();
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        this.isHideViewCalled = i != 0;
        super.setVisibility(i);
    }

    public final void updateEmptyStatus() {
        if (this.mEmptyView == null) {
            return;
        }
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter == null || adapter.getItemCount() == 0) {
            View view = this.mEmptyView;
            if (view != null) {
                view.setVisibility(0);
                super.setVisibility(8);
                return;
            } else if (this.isHideViewCalled) {
                return;
            } else {
                super.setVisibility(0);
                return;
            }
        }
        View view2 = this.mEmptyView;
        if (view2 != null) {
            view2.setVisibility(8);
        }
        if (this.isHideViewCalled) {
            return;
        }
        super.setVisibility(0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void scrollToPosition(int i) {
        super.scrollToPosition(i);
    }

    public void scrollToPositionWithOffset(int i, int i2) {
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).scrollToPositionWithOffset(i, i2);
        } else {
            scrollToPosition(i);
        }
    }

    @Override // com.miui.gallery.util.photoview.ScrollableView
    public void viewToPosition(int i) {
        AdapterPos2ViewPosConverter adapterPos2ViewPosConverter = this.mAdapterPos2ViewPosConverter;
        if (adapterPos2ViewPosConverter != null) {
            i = adapterPos2ViewPosConverter.convert(i);
        }
        scrollToPositionWithOffset(i, getHeight() / 2);
    }

    public void viewByOffset(int i, int i2) {
        scrollBy(i, i2);
    }

    @Override // com.miui.gallery.util.photoview.ScrollableView
    public int translatePosition(int i) {
        AdapterPos2ViewPosConverter adapterPos2ViewPosConverter = this.mAdapterPos2ViewPosConverter;
        if (adapterPos2ViewPosConverter != null) {
            return adapterPos2ViewPosConverter.convert(i);
        }
        return -1;
    }

    /* loaded from: classes3.dex */
    public class PendingShowPlaceholderTask implements Runnable {
        public int position;
        public View view;

        public PendingShowPlaceholderTask(int i) {
            this.position = -1;
            this.position = i;
        }

        public PendingShowPlaceholderTask(View view) {
            this.position = -1;
            this.view = view;
        }

        @Override // java.lang.Runnable
        public void run() {
            View view = this.view;
            if (view != null) {
                GalleryRecyclerView.this.showPlaceholderItemInternal(view);
                return;
            }
            int i = this.position;
            if (i < 0) {
                return;
            }
            GalleryRecyclerView.this.showPlaceholderItemInternal(i);
        }
    }

    public final void showPlaceholderItemInternal(int i) {
        Trace.beginSection("findRealPosition");
        int findRealPosition = findRealPosition(i);
        Trace.endSection();
        if (findRealPosition == -1) {
            return;
        }
        Trace.beginSection("getViewHolderItemView");
        View viewHolderItemView = getViewHolderItemView(findRealPosition);
        Trace.endSection();
        internalShowPlaceholderItem(viewHolderItemView);
    }

    public final void showPlaceholderItemInternal(View view) {
        internalShowPlaceholderItem(view);
    }

    public final void internalShowPlaceholderItem(View view) {
        if (view == null) {
            hidePlaceholderItem();
        } else if (this.mPlaceholderItem == view && view.getVisibility() == 4) {
        } else {
            hidePlaceholderItem();
            this.mPlaceholderItem = view;
            view.setVisibility(4);
        }
    }

    @Override // com.miui.gallery.util.photoview.ScrollableView
    public void postShowPlaceholderItem(int i) {
        this.mPendingShowPlaceholderTask = new PendingShowPlaceholderTask(i);
    }

    @Override // com.miui.gallery.util.photoview.ScrollableView
    public void postShowPlaceholderItem(View view) {
        this.mPendingShowPlaceholderTask = new PendingShowPlaceholderTask(view);
    }

    @Override // com.miui.gallery.util.photoview.ScrollableView
    public void exeShowPlaceholderItem() {
        PendingShowPlaceholderTask pendingShowPlaceholderTask = this.mPendingShowPlaceholderTask;
        if (pendingShowPlaceholderTask != null) {
            pendingShowPlaceholderTask.run();
            this.mPendingShowPlaceholderTask = null;
        }
    }

    public final View getViewHolderItemView(int i) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition = findViewHolderForAdapterPosition(i);
        if (findViewHolderForAdapterPosition != null) {
            return findViewHolderForAdapterPosition.itemView;
        }
        RecyclerView.ViewHolder findViewHolderForLayoutPosition = findViewHolderForLayoutPosition(i);
        if (findViewHolderForLayoutPosition != null) {
            return findViewHolderForLayoutPosition.itemView;
        }
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            return layoutManager.getChildAt(i - findFirstVisibleItemPosition());
        }
        return null;
    }

    public final int findRealPosition(int i) {
        int i2;
        GroupedItemAdapter groupedItemAdapter;
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter == null) {
            return -1;
        }
        int i3 = 0;
        if (adapter instanceof AbstractHeaderFooterWrapperAdapter) {
            AbstractHeaderFooterWrapperAdapter abstractHeaderFooterWrapperAdapter = (AbstractHeaderFooterWrapperAdapter) adapter;
            i2 = abstractHeaderFooterWrapperAdapter.getHeaderItemCount();
            adapter = abstractHeaderFooterWrapperAdapter.getWrappedAdapter();
        } else {
            i2 = 0;
        }
        if (adapter == null || (groupedItemAdapter = (GroupedItemAdapter) WrapperAdapterUtils.findWrappedAdapter(adapter, GroupedItemAdapter.class)) == null) {
            return i;
        }
        if (!groupedItemAdapter.isFlatten()) {
            int groupCount = groupedItemAdapter.getGroupCount();
            int i4 = i;
            int i5 = 0;
            int i6 = 0;
            while (i4 >= i3 && i5 < groupCount) {
                int i7 = i5 + 1;
                i6 += groupedItemAdapter.getChildCount(i5);
                i4 = i + i7;
                i5 = i7;
                i3 = i7 + i6;
            }
            i = i4;
        }
        int i8 = i + i2;
        if (getAdapter().getItemCount() > i8) {
            return i8;
        }
        return -1;
    }

    @Override // com.miui.gallery.util.photoview.ScrollableView
    public void hidePlaceholderItem() {
        View view = this.mPlaceholderItem;
        if (view == null || view.getVisibility() == 0) {
            return;
        }
        this.mPlaceholderItem.setVisibility(0);
        this.mPlaceholderItem = null;
    }

    public void setScrollingCalculator(ScrollingCalculator scrollingCalculator) {
        this.mScrollingCalculator = scrollingCalculator;
    }

    public int[] computeScrollPositionAndOffset(float f) {
        if (this.mScrollingCalculator == null || getLayoutManager().getChildCount() == 0) {
            return null;
        }
        return this.mScrollingCalculator.computeScrollPositionAndOffset((getWidth() - getPaddingLeft()) - getPaddingRight(), (getHeight() - getPaddingTop()) - getPaddingBottom(), f);
    }

    public int computeScrollOffset() {
        int i;
        if (this.mScrollingCalculator == null || getLayoutManager().getChildCount() == 0) {
            return computeVerticalScrollOffset();
        }
        View childAt = getChildAt(0);
        if (childAt == null) {
            return 0;
        }
        int childAdapterPosition = getChildAdapterPosition(childAt);
        childAt.getLocalVisibleRect(this.mFirstViewVisibleLocation);
        int computeScrollOffset = this.mScrollingCalculator.computeScrollOffset(childAdapterPosition, getWidth());
        Rect rect = this.mFirstViewVisibleLocation;
        int i2 = rect.top;
        return (i2 < 0 || (i = rect.bottom) < 0) ? computeScrollOffset : computeScrollOffset - (i - i2);
    }

    public int computeScrollRange() {
        if (this.mScrollingCalculator == null || getLayoutManager().getChildCount() == 0) {
            return computeVerticalScrollRange();
        }
        return this.mScrollingCalculator.computeScrollRange(getWidth());
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.ViewParent
    public void requestDisallowInterceptTouchEvent(boolean z) {
        ViewUtils.requestParentDisallowInterceptTouchEvent(this, z);
        super.requestDisallowInterceptTouchEvent(z);
    }

    @Override // androidx.recyclerview.widget.SpringRecyclerView, androidx.recyclerview.widget.RemixRecyclerView, androidx.recyclerview.widget.RecyclerView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (getScrollState() != 0) {
            return super.onTouchEvent(motionEvent);
        }
        if (super.onTouchEvent(motionEvent)) {
            if (isFastScrollerPressed() && motionEvent.getActionMasked() == 0) {
                this.mIsFastScrollerPressed = true;
                return true;
            } else if (this.mIsFastScrollerPressed && (motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 3)) {
                this.mIsFastScrollerPressed = false;
                return true;
            } else {
                return this.mItemClickSupport.onTouchEvent(this, motionEvent) | true;
            }
        }
        return this.mItemClickSupport.onTouchEvent(this, motionEvent);
    }
}
