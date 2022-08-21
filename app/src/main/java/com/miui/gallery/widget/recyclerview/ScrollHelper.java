package com.miui.gallery.widget.recyclerview;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class ScrollHelper {
    public static void onItemClick(RecyclerView recyclerView, int i) {
        int i2;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null || layoutManager == null || !(layoutManager instanceof LinearLayoutManager)) {
            return;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
        int findLastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
        int findLastCompletelyVisibleItemPosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int findFirstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
        int findFirstCompletelyVisibleItemPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (findLastVisibleItemPosition == i || findLastCompletelyVisibleItemPosition == i) {
            i2 = i + 1;
            if (i2 <= 0 || i2 >= adapter.getItemCount()) {
                i2 = adapter.getItemCount();
            }
        } else if (findFirstVisibleItemPosition == i || findFirstCompletelyVisibleItemPosition == i) {
            i2 = i - 1;
            if (i2 < 0 || i2 >= adapter.getItemCount()) {
                i2 = 0;
            }
        } else {
            i2 = -1;
        }
        if (i2 == -1) {
            return;
        }
        recyclerView.smoothScrollToPosition(i2);
    }
}
