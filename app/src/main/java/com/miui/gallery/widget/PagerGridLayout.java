package com.miui.gallery.widget;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.gridlayout.widget.GridLayout;

/* loaded from: classes2.dex */
public class PagerGridLayout extends GridLayout {
    public BaseAdapter mAdapter;
    public int mColumnCount;
    public int mCount;
    public DataSetObserver mDataSetObserver;
    public int mItemCount;
    public OnPageChangedListener mListener;
    public int mPageCount;
    public int mPageIndex;
    public int mRowCount;

    /* loaded from: classes2.dex */
    public interface OnPageChangedListener {
        void onPageChanged(int i, int i2, boolean z);
    }

    public PagerGridLayout(Context context) {
        this(context, null);
    }

    public PagerGridLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PagerGridLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDataSetObserver = new DataSetObserver() { // from class: com.miui.gallery.widget.PagerGridLayout.1
            @Override // android.database.DataSetObserver
            public void onChanged() {
                PagerGridLayout pagerGridLayout = PagerGridLayout.this;
                pagerGridLayout.bindPage(pagerGridLayout.mPageIndex);
            }

            @Override // android.database.DataSetObserver
            public void onInvalidated() {
                PagerGridLayout.this.refresh();
            }
        };
    }

    public void setOnPageChangedListener(OnPageChangedListener onPageChangedListener) {
        this.mListener = onPageChangedListener;
    }

    public void setAdapter(BaseAdapter baseAdapter) {
        setAdapter(baseAdapter, 0);
    }

    public void setAdapter(BaseAdapter baseAdapter, int i) {
        BaseAdapter baseAdapter2 = this.mAdapter;
        if (baseAdapter2 != null) {
            baseAdapter2.unregisterDataSetObserver(this.mDataSetObserver);
        }
        this.mAdapter = baseAdapter;
        baseAdapter.registerDataSetObserver(this.mDataSetObserver);
        this.mPageIndex = i;
        addViews();
        bindPage(this.mPageIndex);
    }

    public final void refresh() {
        removeAllViews();
        addViews();
        bindPage(this.mPageIndex);
    }

    public final void addViews() {
        LayoutInflater from = LayoutInflater.from(getContext());
        int columnsCount = this.mAdapter.getColumnsCount();
        int rowsCount = this.mAdapter.getRowsCount();
        this.mRowCount = rowsCount;
        this.mColumnCount = columnsCount;
        this.mItemCount = columnsCount * rowsCount;
        int count = this.mAdapter.getCount();
        this.mCount = count;
        int i = this.mItemCount;
        this.mPageCount = (count + (i - 1)) / i;
        for (int i2 = 0; i2 < rowsCount; i2++) {
            for (int i3 = 0; i3 < columnsCount; i3++) {
                addView(this.mAdapter.getView(from, this), new GridLayout.LayoutParams(GridLayout.spec(i2, 1.0f), GridLayout.spec(i3, 1.0f)));
            }
        }
    }

    public final void bindPage(int i) {
        boolean z = false;
        if (i < this.mPageCount) {
            int i2 = this.mItemCount * i;
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                int i4 = i2 + i3;
                if (i4 >= this.mCount) {
                    childAt.setVisibility(4);
                } else {
                    childAt.setVisibility(0);
                    this.mAdapter.bindData(i4, childAt);
                }
            }
        }
        OnPageChangedListener onPageChangedListener = this.mListener;
        if (onPageChangedListener != null) {
            int i5 = this.mPageIndex;
            int i6 = this.mPageCount;
            if (i == i6) {
                z = true;
            }
            onPageChangedListener.onPageChanged(i5, i6, z);
        }
    }

    public void changeToNextPage() {
        int i = this.mPageIndex;
        if (i + 1 > this.mPageCount) {
            return;
        }
        this.mPageIndex = i + 1;
        this.mAdapter.onPageChanged();
        bindPage(this.mPageIndex);
    }

    public void freshCurrentPage() {
        if (this.mAdapter == null) {
            return;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0) {
                this.mAdapter.freshView(childAt);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class BaseAdapter {
        public final DataSetObservable mDataSetObservable = new DataSetObservable();

        public abstract void bindData(int i, View view);

        public abstract void freshView(View view);

        public abstract int getColumnsCount();

        public abstract int getCount();

        public abstract int getRowsCount();

        public abstract View getView(LayoutInflater layoutInflater, ViewGroup viewGroup);

        public abstract void onPageChanged();

        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            this.mDataSetObservable.registerObserver(dataSetObserver);
        }

        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            this.mDataSetObservable.unregisterObserver(dataSetObserver);
        }

        public void notifyDataSetChanged() {
            this.mDataSetObservable.notifyChanged();
        }

        public void notifyDataSetInvalidated() {
            this.mDataSetObservable.notifyInvalidated();
        }
    }
}
