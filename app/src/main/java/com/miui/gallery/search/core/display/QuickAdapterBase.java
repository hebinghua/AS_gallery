package com.miui.gallery.search.core.display;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.search.core.display.BaseSuggestionViewHolder;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class QuickAdapterBase<VH extends BaseSuggestionViewHolder> extends RecyclerView.Adapter<BaseSuggestionViewHolder> implements FullSpanDelegate {
    public final Context mContext;
    public View mEmptyView;
    public boolean mFootAndEmptyEnable;
    public LinearLayout mFooterLayout;
    public boolean mHeadAndEmptyEnable;
    public LinearLayout mHeaderLayout;
    public final LayoutInflater mLayoutInflater;
    public View mLoadCompleteView;
    public View mLoadMoreFailedView;
    public View mLoadingView;
    public RequestLoadMoreListener mRequestLoadMoreListener;
    public LinearLayout mCopyHeaderLayout = null;
    public LinearLayout mCopyFooterLayout = null;
    public boolean mNextLoadEnable = false;
    public boolean mShowLoadingView = true;
    public boolean mLoadMoreRequested = false;
    public int mLoadingViewRes = -1;
    public int mLoadMoreFailedViewRes = -1;
    public int mLoadCompleteViewRes = -1;
    public final ArrayList<Integer> mViewTypePositionList = new ArrayList<>();
    public View.OnClickListener mLoadFailedViewOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.search.core.display.QuickAdapterBase.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            QuickAdapterBase quickAdapterBase = QuickAdapterBase.this;
            quickAdapterBase.removeFooterView(quickAdapterBase.mLoadMoreFailedView);
            QuickAdapterBase.this.openLoadMore();
        }
    };
    public RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() { // from class: com.miui.gallery.search.core.display.QuickAdapterBase.2
        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            QuickAdapterBase.this.genPositionList();
        }
    };

    /* loaded from: classes2.dex */
    public static abstract class InnerDiffCallback extends DiffUtil.Callback {
        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            return false;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return 0;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return 0;
        }
    }

    public abstract void bindInnerItemViewHolder(VH vh, int i);

    public abstract VH createInnerItemViewHolder(ViewGroup viewGroup, int i);

    public abstract int getInnerItemViewCount();

    public abstract int getInnerItemViewType(int i);

    public QuickAdapterBase(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        registerAdapterDataObserver(this.mDataObserver);
    }

    public int getInnerItemPosition(int i) {
        return i - (this.mHeaderLayout == null ? 0 : 1);
    }

    public void addHeaderView(View view) {
        addHeaderView(view, -1);
    }

    public void addHeaderView(View view, int i) {
        if (this.mHeaderLayout == null) {
            LinearLayout linearLayout = this.mCopyHeaderLayout;
            if (linearLayout == null) {
                LinearLayout linearLayout2 = new LinearLayout(view.getContext());
                this.mHeaderLayout = linearLayout2;
                linearLayout2.setOrientation(1);
                this.mCopyHeaderLayout = this.mHeaderLayout;
            } else {
                this.mHeaderLayout = linearLayout;
            }
        }
        if (i >= this.mHeaderLayout.getChildCount()) {
            i = -1;
        }
        this.mHeaderLayout.addView(view, i);
        notifyNonDataChanged();
    }

    public void removeHeaderView(View view) {
        LinearLayout linearLayout = this.mHeaderLayout;
        if (linearLayout == null) {
            return;
        }
        linearLayout.removeView(view);
        if (this.mHeaderLayout.getChildCount() == 0) {
            this.mHeaderLayout = null;
        }
        notifyNonDataChanged();
    }

    public void addFooterView(View view) {
        addFooterView(view, -1);
    }

    public void addFooterView(View view, int i) {
        if (this.mFooterLayout == null) {
            LinearLayout linearLayout = this.mCopyFooterLayout;
            if (linearLayout == null) {
                LinearLayout linearLayout2 = new LinearLayout(view.getContext());
                this.mFooterLayout = linearLayout2;
                linearLayout2.setOrientation(1);
                this.mCopyFooterLayout = this.mFooterLayout;
            } else {
                this.mFooterLayout = linearLayout;
            }
        }
        this.mFooterLayout.removeView(view);
        if (i >= this.mFooterLayout.getChildCount()) {
            i = -1;
        }
        this.mFooterLayout.addView(view, i);
        notifyNonDataChanged();
    }

    public void removeFooterView(View view) {
        LinearLayout linearLayout = this.mFooterLayout;
        if (linearLayout == null) {
            return;
        }
        linearLayout.removeView(view);
        if (this.mFooterLayout.getChildCount() == 0) {
            this.mFooterLayout = null;
        }
        notifyNonDataChanged();
    }

    public void setOnLoadMoreListener(RequestLoadMoreListener requestLoadMoreListener) {
        this.mRequestLoadMoreListener = requestLoadMoreListener;
    }

    public void openLoadMore() {
        openLoadMore(true);
    }

    public void openLoadMore(boolean z) {
        this.mShowLoadingView = z;
        this.mNextLoadEnable = true;
        this.mLoadMoreRequested = false;
        hideLoadMoreFailedView();
        notifyNonDataChanged();
    }

    public void closeLoadMore() {
        if (this.mNextLoadEnable) {
            this.mNextLoadEnable = false;
        }
        this.mLoadMoreRequested = false;
        hideLoadMoreFailedView();
        notifyNonDataChanged();
    }

    public boolean isLoading() {
        return this.mNextLoadEnable;
    }

    public void hideLoadMoreFailedView() {
        View view = this.mLoadMoreFailedView;
        if (view != null) {
            removeFooterView(view);
        }
    }

    public void showLoadCompleteView() {
        closeLoadMore();
        if (this.mLoadCompleteView == null) {
            LayoutInflater layoutInflater = this.mLayoutInflater;
            int i = this.mLoadCompleteViewRes;
            if (i <= 0) {
                i = R.layout.search_item_load_complete_layout;
            }
            this.mLoadCompleteView = layoutInflater.inflate(i, (ViewGroup) null);
        }
        addFooterView(this.mLoadCompleteView, 0);
    }

    public final void onBindLoadMore(RecyclerView.ViewHolder viewHolder) {
        RequestLoadMoreListener requestLoadMoreListener;
        if (!this.mNextLoadEnable || (requestLoadMoreListener = this.mRequestLoadMoreListener) == null || this.mLoadMoreRequested) {
            return;
        }
        this.mLoadMoreRequested = true;
        requestLoadMoreListener.onLoadMoreRequested();
    }

    public final BaseSuggestionViewHolder getLoadingView(ViewGroup viewGroup) {
        if (this.mLoadingView == null) {
            LayoutInflater layoutInflater = this.mLayoutInflater;
            int i = this.mLoadingViewRes;
            if (i <= 0) {
                i = R.layout.search_item_loading_layout;
            }
            this.mLoadingView = layoutInflater.inflate(i, viewGroup, false);
        }
        return new BaseSuggestionViewHolder(this.mLoadingView);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mViewTypePositionList.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return this.mViewTypePositionList.get(i).intValue();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BaseSuggestionViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        BaseSuggestionViewHolder baseSuggestionViewHolder;
        if (i == 1) {
            baseSuggestionViewHolder = new BaseSuggestionViewHolder(this.mHeaderLayout);
            ensureLayoutParams(this.mHeaderLayout, viewGroup);
        } else if (i == 2) {
            baseSuggestionViewHolder = new BaseSuggestionViewHolder(this.mFooterLayout);
            ensureLayoutParams(this.mFooterLayout, viewGroup);
        } else if (i == 3) {
            baseSuggestionViewHolder = new BaseSuggestionViewHolder(this.mEmptyView);
            ensureLayoutParams(this.mEmptyView, viewGroup);
        } else if (i == 4) {
            baseSuggestionViewHolder = getLoadingView(viewGroup);
            ensureLayoutParams(baseSuggestionViewHolder.itemView, viewGroup);
        } else {
            return createInnerItemViewHolder(viewGroup, i);
        }
        return baseSuggestionViewHolder;
    }

    @Override // com.miui.gallery.search.core.display.FullSpanDelegate
    public boolean isFullSpan(int i) {
        int itemViewType = getItemViewType(i);
        return itemViewType == 1 || itemViewType == 2 || itemViewType == 3 || itemViewType == 4;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(BaseSuggestionViewHolder baseSuggestionViewHolder, int i) {
        int itemViewType = baseSuggestionViewHolder.getItemViewType();
        if (itemViewType == 1 || itemViewType == 2 || itemViewType == 3) {
            return;
        }
        if (itemViewType == 4) {
            onBindLoadMore(baseSuggestionViewHolder);
            return;
        }
        int innerItemPosition = getInnerItemPosition(baseSuggestionViewHolder.getLayoutPosition());
        bindInnerItemViewHolder(baseSuggestionViewHolder, innerItemPosition);
        if (this.mShowLoadingView || innerItemPosition != getInnerItemViewCount() - 1) {
            return;
        }
        onBindLoadMore(null);
    }

    public final void genPositionList() {
        this.mViewTypePositionList.clear();
        this.mViewTypePositionList.ensureCapacity(getItemCount());
        if (noItemView() && !this.mNextLoadEnable) {
            if (this.mHeaderLayout != null && (this.mEmptyView == null || this.mHeadAndEmptyEnable)) {
                this.mViewTypePositionList.add(1);
            }
            if (this.mEmptyView != null) {
                this.mViewTypePositionList.add(3);
            }
            if (this.mFooterLayout == null) {
                return;
            }
            if (this.mEmptyView != null && !this.mFootAndEmptyEnable) {
                return;
            }
            this.mViewTypePositionList.add(2);
            return;
        }
        if (this.mHeaderLayout != null) {
            this.mViewTypePositionList.add(1);
        }
        for (int i = 0; i < getInnerItemViewCount(); i++) {
            this.mViewTypePositionList.add(Integer.valueOf(getInnerItemViewType(i)));
        }
        if (this.mNextLoadEnable && this.mShowLoadingView) {
            this.mViewTypePositionList.add(4);
        }
        if (this.mFooterLayout == null) {
            return;
        }
        this.mViewTypePositionList.add(2);
    }

    public final void ensureLayoutParams(View view, ViewGroup viewGroup) {
        if (view.getLayoutParams() != null || !(viewGroup instanceof RecyclerView)) {
            return;
        }
        RecyclerView recyclerView = (RecyclerView) viewGroup;
        if (recyclerView.getLayoutManager() == null) {
            return;
        }
        view.setLayoutParams(recyclerView.getLayoutManager().generateLayoutParams(new ViewGroup.LayoutParams(-1, -2)));
    }

    public final boolean noItemView() {
        return getInnerItemViewCount() == 0;
    }

    public final void notifyNonDataChanged() {
        ArrayList arrayList = new ArrayList(this.mViewTypePositionList);
        genPositionList();
        DiffUtil.calculateDiff(new NonDataDiffCallback(arrayList, this.mViewTypePositionList)).dispatchUpdatesTo(this);
    }

    /* loaded from: classes2.dex */
    public static class NonDataDiffCallback extends DiffUtil.Callback {
        public ArrayList<Integer> mNewTypeList;
        public ArrayList<Integer> mOldTypeList;

        public NonDataDiffCallback(ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2) {
            this.mOldTypeList = arrayList;
            this.mNewTypeList = arrayList2;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.mOldTypeList.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return this.mNewTypeList.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            return this.mOldTypeList.get(i).equals(this.mNewTypeList.get(i2));
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            return this.mOldTypeList.get(i).equals(this.mNewTypeList.get(i2));
        }
    }

    public void notifyDataChanged(DiffUtil.Callback callback) {
        ArrayList arrayList = new ArrayList(this.mViewTypePositionList);
        genPositionList();
        DiffUtil.calculateDiff(new DataDiffCallback(arrayList, this.mViewTypePositionList, callback)).dispatchUpdatesTo(this);
    }

    /* loaded from: classes2.dex */
    public static class DataDiffCallback extends DiffUtil.Callback {
        public DiffUtil.Callback mInnerDiffCallback;
        public ArrayList<Integer> mNewTypeList;
        public ArrayList<Integer> mOldTypeList;

        public DataDiffCallback(ArrayList<Integer> arrayList, ArrayList<Integer> arrayList2, DiffUtil.Callback callback) {
            this.mOldTypeList = arrayList;
            this.mNewTypeList = arrayList2;
            this.mInnerDiffCallback = callback;
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getOldListSize() {
            return this.mOldTypeList.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public int getNewListSize() {
            return this.mNewTypeList.size();
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areItemsTheSame(int i, int i2) {
            return this.mOldTypeList.get(i).equals(this.mNewTypeList.get(i2));
        }

        @Override // androidx.recyclerview.widget.DiffUtil.Callback
        public boolean areContentsTheSame(int i, int i2) {
            if (this.mOldTypeList.get(i).intValue() < 16 || this.mNewTypeList.get(i2).intValue() < 16) {
                return false;
            }
            return this.mInnerDiffCallback.areContentsTheSame(i - getItemStartPosition(this.mOldTypeList), i2 - getItemStartPosition(this.mNewTypeList));
        }

        public final int getItemStartPosition(ArrayList<Integer> arrayList) {
            for (int i = 0; i < arrayList.size(); i++) {
                if (arrayList.get(i).intValue() != 1) {
                    return i;
                }
            }
            return 0;
        }
    }
}
