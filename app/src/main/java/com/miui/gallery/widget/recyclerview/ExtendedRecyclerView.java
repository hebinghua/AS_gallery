package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import com.miui.gallery.util.Numbers;

/* loaded from: classes3.dex */
public class ExtendedRecyclerView extends RelativeLayout {
    public boolean mClipToPadding;
    public ViewStub mEmpty;
    public int mEmptyId;
    public View mEmptyView;
    public int mMainContentId;
    public boolean mManualInflateEmptyView;
    public final RecyclerViewDataObserver mObserver;
    public int mPadding;
    public int mPaddingBottom;
    public int mPaddingLeft;
    public int mPaddingRight;
    public int mPaddingTop;
    public ViewStub mProgress;
    public int mProgressId;
    public View mProgressView;
    public RecyclerView mRecycler;
    public int mScrollbarStyle;

    public ExtendedRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mObserver = new RecyclerViewDataObserver();
        initAttrs(attributeSet);
        initView();
    }

    public void initAttrs(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.ExtendedRecyclerView);
        try {
            this.mClipToPadding = obtainStyledAttributes.getBoolean(4, false);
            this.mManualInflateEmptyView = obtainStyledAttributes.getBoolean(3, false);
            this.mPadding = (int) obtainStyledAttributes.getDimension(5, -1.0f);
            this.mPaddingTop = (int) obtainStyledAttributes.getDimension(9, 0.0f);
            this.mPaddingBottom = (int) obtainStyledAttributes.getDimension(6, 0.0f);
            this.mPaddingLeft = (int) obtainStyledAttributes.getDimension(7, 0.0f);
            this.mPaddingRight = (int) obtainStyledAttributes.getDimension(8, 0.0f);
            this.mScrollbarStyle = obtainStyledAttributes.getInt(10, -1);
            this.mEmptyId = obtainStyledAttributes.getResourceId(0, 0);
            this.mProgressId = obtainStyledAttributes.getResourceId(2, R.layout.layout_progress);
            this.mMainContentId = obtainStyledAttributes.getResourceId(1, R.layout.extended_recyclerview);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public final void initView() {
        if (isInEditMode()) {
            return;
        }
        View inflate = LayoutInflater.from(getContext()).inflate(this.mMainContentId, this);
        ViewStub viewStub = (ViewStub) inflate.findViewById(R.id.progress);
        this.mProgress = viewStub;
        if (viewStub == null) {
            throw new RuntimeException("layout_main_content must contains progress stub!");
        }
        viewStub.setLayoutResource(this.mProgressId);
        this.mProgressView = this.mProgress.inflate();
        ViewStub viewStub2 = (ViewStub) inflate.findViewById(R.id.empty);
        this.mEmpty = viewStub2;
        if (viewStub2 == null) {
            throw new RuntimeException("layout_main_content must contains empty stub!");
        }
        int i = this.mEmptyId;
        if (i != 0 && !this.mManualInflateEmptyView) {
            viewStub2.setLayoutResource(i);
            this.mEmptyView = this.mEmpty.inflate();
        }
        initRecyclerView(inflate);
    }

    public void initRecyclerView(View view) {
        View findViewById = view.findViewById(R.id.recycler);
        if (findViewById != null && (findViewById instanceof RecyclerView)) {
            RecyclerView recyclerView = (RecyclerView) findViewById;
            this.mRecycler = recyclerView;
            recyclerView.setClipToPadding(this.mClipToPadding);
            if (!Numbers.equals(Float.valueOf(this.mPadding), Float.valueOf(-1.0f))) {
                RecyclerView recyclerView2 = this.mRecycler;
                int i = this.mPadding;
                recyclerView2.setPadding(i, i, i, i);
            } else {
                this.mRecycler.setPadding(this.mPaddingLeft, this.mPaddingTop, this.mPaddingRight, this.mPaddingBottom);
            }
            int i2 = this.mScrollbarStyle;
            if (i2 == -1) {
                return;
            }
            if (i2 != 16777216 && i2 != 0 && i2 != 50331648 && i2 != 33554432) {
                return;
            }
            this.mRecycler.setScrollBarStyle(i2);
            return;
        }
        throw new IllegalArgumentException("ExtendedRecyclerView only works with a RecyclerView!");
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mRecycler.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapterInternal(adapter, false, true);
    }

    public final void setAdapterInternal(RecyclerView.Adapter adapter, boolean z, boolean z2) {
        RecyclerView.Adapter adapter2 = this.mRecycler.getAdapter();
        if (adapter2 != null) {
            adapter2.unregisterAdapterDataObserver(this.mObserver);
        }
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.mObserver);
        }
        if (z) {
            this.mRecycler.swapAdapter(adapter, z2);
        } else {
            this.mRecycler.setAdapter(adapter);
        }
        if (adapter != null && adapter.getItemCount() > 0) {
            showRecycler();
        } else {
            showEmpty();
        }
    }

    public final void showRecycler() {
        this.mRecycler.setVisibility(0);
        setEmptyVisibility(8);
        this.mProgress.setVisibility(8);
    }

    public final void showEmpty() {
        this.mRecycler.setVisibility(8);
        setEmptyVisibility(0);
        this.mProgress.setVisibility(8);
    }

    private void setEmptyVisibility(int i) {
        if (this.mEmptyView != null) {
            this.mEmpty.setVisibility(i);
        }
    }

    public void setItemAnimator(RecyclerView.ItemAnimator itemAnimator) {
        this.mRecycler.setItemAnimator(itemAnimator);
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return this.mRecycler.getLayoutManager();
    }

    public RecyclerView getRecycler() {
        return this.mRecycler;
    }

    /* loaded from: classes3.dex */
    public class RecyclerViewDataObserver extends RecyclerView.AdapterDataObserver {
        public RecyclerViewDataObserver() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i, int i2) {
            super.onItemRangeChanged(i, i2);
            update();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int i, int i2) {
            super.onItemRangeInserted(i, i2);
            update();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int i, int i2) {
            super.onItemRangeRemoved(i, i2);
            update();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeMoved(int i, int i2, int i3) {
            super.onItemRangeMoved(i, i2, i3);
            update();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            super.onChanged();
            update();
        }

        public final void update() {
            if (ExtendedRecyclerView.this.mRecycler.getAdapter() == null || ExtendedRecyclerView.this.mRecycler.getAdapter().getItemCount() <= 0) {
                ExtendedRecyclerView.this.showEmpty();
            } else {
                ExtendedRecyclerView.this.showRecycler();
            }
        }
    }
}
