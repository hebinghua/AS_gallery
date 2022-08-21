package com.miui.gallery.magic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class EmptyRecyclerView extends RecyclerView {
    public View emptyView;
    public final RecyclerView.AdapterDataObserver observer;

    public EmptyRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.observer = new RecyclerView.AdapterDataObserver() { // from class: com.miui.gallery.magic.widget.EmptyRecyclerView.1
            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onChanged() {
                EmptyRecyclerView.this.checkIfEmpty();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeInserted(int i, int i2) {
                EmptyRecyclerView.this.checkIfEmpty();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
            public void onItemRangeRemoved(int i, int i2) {
                EmptyRecyclerView.this.checkIfEmpty();
            }
        };
    }

    public final void checkIfEmpty() {
        if (this.emptyView == null || getAdapter() == null) {
            return;
        }
        int i = 0;
        boolean z = getAdapter().getItemCount() == 0;
        this.emptyView.setVisibility(z ? 0 : 8);
        if (z) {
            i = 8;
        }
        setVisibility(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public void setAdapter(RecyclerView.Adapter adapter) {
        RecyclerView.Adapter adapter2 = getAdapter();
        if (adapter2 != null) {
            adapter2.unregisterAdapterDataObserver(this.observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(this.observer);
        }
        checkIfEmpty();
    }

    public void setEmptyView(View view) {
        this.emptyView = view;
        checkIfEmpty();
    }
}
