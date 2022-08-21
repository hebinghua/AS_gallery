package com.miui.gallery.video.editor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.video.editor.Filter;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterRecyclerViewAdapter extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter<FilterViewHolder> {
    public List<Filter> filterList;
    public LayoutInflater mLayoutInflater;
    public final int ITEM_TYPE_ITEM_NORMAL = 1;
    public int mFirstMarginLeft = 0;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return 1;
    }

    public FilterRecyclerViewAdapter(Context context, List<Filter> list) {
        this.mLayoutInflater = LayoutInflater.from(context);
        this.filterList = list;
    }

    public Filter getFilter(int i) {
        List<Filter> list = this.filterList;
        if (list == null || i >= list.size()) {
            return null;
        }
        return this.filterList.get(i);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    /* renamed from: onCreateSingleChoiceViewHolder */
    public FilterViewHolder mo1754onCreateSingleChoiceViewHolder(ViewGroup viewGroup, int i) {
        return new FilterViewHolder(this.mLayoutInflater.inflate(R.layout.video_editor_filter_item, viewGroup, false));
    }

    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter
    public void onBindView(FilterViewHolder filterViewHolder, int i) {
        if (getItemCount() <= 0) {
            return;
        }
        filterViewHolder.setMarginLeft(i == 0 ? this.mFirstMarginLeft : 0);
        Filter filter = this.filterList.get(i);
        filterViewHolder.setName(filter.getNameResId());
        filterViewHolder.setIcon(filter.getIconResId());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<Filter> list = this.filterList;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}
