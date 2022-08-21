package com.miui.gallery.vlog.nav;

import android.content.Context;
import android.view.LayoutInflater;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class VlogNavBaseAdapter extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter<VlogNavViewHolder> {
    public Context mContext;
    public LayoutInflater mLayoutInflater;
    public List<VlogNavItem> mList = new ArrayList();

    public abstract void updateItemSize();

    public VlogNavBaseAdapter(Context context, List<VlogNavItem> list) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mList.addAll(list);
    }

    public VlogNavItem getSelectedItem(int i) {
        List<VlogNavItem> list = this.mList;
        if (list == null || i >= list.size()) {
            return null;
        }
        return this.mList.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<VlogNavItem> list = this.mList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }
}
