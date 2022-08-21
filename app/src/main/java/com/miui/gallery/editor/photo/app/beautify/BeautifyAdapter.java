package com.miui.gallery.editor.photo.app.beautify;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.BeautifyData;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class BeautifyAdapter extends Adapter<BeautifyAdapterItemHolder> implements Selectable {
    public List<BeautifyData> mDataList;
    public Selectable.Delegator mDelegator;

    public BeautifyAdapter(Context context, List<BeautifyData> list, Selectable.Selector selector) {
        this.mDataList = list;
        this.mDelegator = new Selectable.Delegator(-1, selector);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BeautifyAdapterItemHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BeautifyAdapterItemHolder(getInflater().inflate(R.layout.beautify_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(BeautifyAdapterItemHolder beautifyAdapterItemHolder, int i) {
        super.onBindViewHolder((BeautifyAdapter) beautifyAdapterItemHolder, i);
        beautifyAdapterItemHolder.bind(this.mDataList.get(i));
        this.mDelegator.onBindViewHolder(beautifyAdapterItemHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<BeautifyData> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mDelegator.getSelection();
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
    public static class BeautifyAdapterItemHolder extends RecyclerView.ViewHolder {
        public ImageView mIconView;
        public TextView mLabelView;

        public BeautifyAdapterItemHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mLabelView = (TextView) view.findViewById(R.id.labelView);
            this.mIconView = (ImageView) view.findViewById(R.id.iconView);
        }

        public void bind(BeautifyData beautifyData) {
            ImageView imageView = this.mIconView;
            imageView.setImageDrawable(imageView.getContext().getDrawable(beautifyData.icon));
            this.mLabelView.setText(beautifyData.name);
        }
    }
}
