package com.miui.gallery.editor.photo.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class CategoryAdapter extends Adapter<CategoryViewHolder> implements Selectable {
    public List<? extends Metadata> mCategories;
    public Context mContext;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(0, null);

    public CategoryAdapter(Context context, List<? extends Metadata> list) {
        this.mContext = context;
        this.mCategories = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public CategoryViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CategoryViewHolder(viewGroup, this.mContext);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int i) {
        super.onBindViewHolder((CategoryAdapter) categoryViewHolder, i);
        categoryViewHolder.mTitle.setText(this.mCategories.get(i).name);
        this.mDelegator.onBindViewHolder(categoryViewHolder, i);
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mDelegator.getSelection();
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<? extends Metadata> list = this.mCategories;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mDelegator.onAttachedToRecyclerView(recyclerView);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mDelegator.onDetachedFromRecyclerView(recyclerView);
    }

    /* loaded from: classes2.dex */
    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;

        public CategoryViewHolder(ViewGroup viewGroup, Context context) {
            super(LayoutInflater.from(context).inflate(R.layout.filter_sky_text_category_item, viewGroup, false));
            this.mTitle = (TextView) this.itemView.findViewById(R.id.tv_filter_sky_categoty_item);
        }
    }
}
