package com.bumptech.glide.integration.recyclerview;

import android.widget.AbsListView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: RecyclerToListViewScrollListener.kt */
/* loaded from: classes.dex */
public final class RecyclerToListViewScrollListener extends RecyclerView.OnScrollListener {
    public static final Companion Companion = new Companion(null);
    public int lastFirstVisible;
    public int lastItemCount;
    public int lastVisibleCount;
    public final AbsListView.OnScrollListener scrollListener;

    public RecyclerToListViewScrollListener(AbsListView.OnScrollListener scrollListener) {
        Intrinsics.checkNotNullParameter(scrollListener, "scrollListener");
        this.scrollListener = scrollListener;
        this.lastFirstVisible = -1;
        this.lastVisibleCount = -1;
        this.lastItemCount = -1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
    public void onScrollStateChanged(RecyclerView recyclerView, int i) {
        Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
        int i2 = 2;
        if (i == 0) {
            i2 = 0;
        } else if (i == 1) {
            i2 = 1;
        } else if (i != 2) {
            i2 = Integer.MIN_VALUE;
        }
        this.scrollListener.onScrollStateChanged(null, i2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
    public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        Objects.requireNonNull(layoutManager, "null cannot be cast to non-null type androidx.recyclerview.widget.LinearLayoutManager");
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int abs = Math.abs(findFirstVisibleItemPosition - linearLayoutManager.findLastVisibleItemPosition());
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        Intrinsics.checkNotNull(adapter);
        int itemCount = adapter.getItemCount();
        if (findFirstVisibleItemPosition == this.lastFirstVisible && abs == this.lastVisibleCount && itemCount == this.lastItemCount) {
            return;
        }
        this.scrollListener.onScroll(null, findFirstVisibleItemPosition, abs, itemCount);
        this.lastFirstVisible = findFirstVisibleItemPosition;
        this.lastVisibleCount = abs;
        this.lastItemCount = itemCount;
    }

    /* compiled from: RecyclerToListViewScrollListener.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
