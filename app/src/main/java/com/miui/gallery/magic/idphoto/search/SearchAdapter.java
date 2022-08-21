package com.miui.gallery.magic.idphoto.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.idphoto.bean.CategoryItem;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchAdapter extends Adapter<ViewHolder> {
    public List<CategoryItem> mList;

    public SearchAdapter(List<CategoryItem> list) {
        this.mList = list;
    }

    /* loaded from: classes2.dex */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public TextView titleView;

        public ViewHolder(View view) {
            super(view);
            this.titleView = (TextView) view.findViewById(R$id.magic_idp_make_recycler_title);
            this.textView = (TextView) view.findViewById(R$id.magic_idp_make_recycler_text);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.ts_magic_id_photo_search_recycler_item, viewGroup, false);
        FolmeUtil.setDefaultTouchAnim(inflate, null, true);
        return new ViewHolder(inflate);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mList.size();
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        super.onBindViewHolder((SearchAdapter) viewHolder, i);
        CategoryItem categoryItem = this.mList.get(i);
        viewHolder.titleView.setText(categoryItem.title);
        TextView textView = viewHolder.textView;
        textView.setText(categoryItem.text + " | " + categoryItem.textmm);
    }

    public CategoryItem getItem(int i) {
        return this.mList.get(i);
    }

    public void newData(ArrayList<CategoryItem> arrayList) {
        this.mList = arrayList;
        notifyDataSetChanged();
    }
}
