package com.miui.gallery.widget.recyclerview;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import java.util.List;

/* loaded from: classes3.dex */
public class SimpleHeaderFooterWrapperAdapter extends AbstractHeaderFooterWrapperAdapter<BaseViewHolder, BaseViewHolder> {
    public List<View> mFooters;
    public List<View> mHeaders;

    @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
    public int getFooterItemViewType(int i) {
        return i;
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
    public int getHeaderItemViewType(int i) {
        return i;
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
    public void onBindFooterItemViewHolder(BaseViewHolder baseViewHolder, int i) {
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
    public void onBindHeaderItemViewHolder(BaseViewHolder baseViewHolder, int i) {
    }

    public SimpleHeaderFooterWrapperAdapter(RecyclerView.Adapter adapter) {
        this.mHeaders = Lists.newLinkedList();
        this.mFooters = Lists.newLinkedList();
        setAdapter(adapter);
    }

    public SimpleHeaderFooterWrapperAdapter(RecyclerView.Adapter adapter, List<View> list, List<View> list2) {
        if (list != null) {
            this.mHeaders = Lists.newLinkedList(list);
        } else {
            this.mHeaders = Lists.newLinkedList();
        }
        if (list2 != null) {
            this.mFooters = Lists.newLinkedList(list2);
        } else {
            this.mFooters = Lists.newLinkedList();
        }
        setAdapter(adapter);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
    public BaseViewHolder onCreateHeaderItemViewHolder(ViewGroup viewGroup, int i) {
        View view = this.mHeaders.get(i);
        if (view != null && view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return new BaseViewHolder(view);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
    public BaseViewHolder onCreateFooterItemViewHolder(ViewGroup viewGroup, int i) {
        View view = this.mFooters.get(i);
        if (view != null && view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        return new BaseViewHolder(view);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
    public int getHeaderItemCount() {
        return this.mHeaders.size();
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter
    public int getFooterItemCount() {
        return this.mFooters.size();
    }

    public void addHeader(View view) {
        if (!this.mHeaders.contains(view)) {
            this.mHeaders.add(view);
            RecyclerView.Adapter headerAdapter = getHeaderAdapter();
            if (headerAdapter == null) {
                return;
            }
            notifyDataSetChanged(headerAdapter);
        }
    }

    public void removeHeader(View view) {
        RecyclerView.Adapter headerAdapter;
        if (!this.mHeaders.remove(view) || (headerAdapter = getHeaderAdapter()) == null) {
            return;
        }
        notifyDataSetChanged(headerAdapter);
    }

    public void addFooter(View view) {
        if (!this.mFooters.contains(view)) {
            this.mFooters.add(view);
            RecyclerView.Adapter footerAdapter = getFooterAdapter();
            if (footerAdapter == null) {
                return;
            }
            notifyDataSetChanged(footerAdapter);
        }
    }

    public void removeFooter(View view) {
        RecyclerView.Adapter footerAdapter;
        if (!this.mFooters.remove(view) || (footerAdapter = getFooterAdapter()) == null) {
            return;
        }
        notifyDataSetChanged(footerAdapter);
    }

    public void notifyDataSetChanged(final RecyclerView.Adapter adapter) {
        new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.miui.gallery.widget.recyclerview.SimpleHeaderFooterWrapperAdapter.1
            @Override // java.lang.Runnable
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
