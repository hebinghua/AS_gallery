package com.miui.gallery.video.editor.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.video.editor.model.AdjustData;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class AdjustAdapter extends Adapter<AdjustAdapterItemHolder> implements Selectable {
    public List<AdjustData> mDataList;
    public Selectable.Delegator mDelegator;
    public int mItemWidth;

    public AdjustAdapter(Context context, List<AdjustData> list, Selectable.Selector selector) {
        this.mDataList = list;
        this.mDelegator = new Selectable.Delegator(-1, selector);
        this.mItemWidth = (int) ((ScreenUtils.getScreenWidth() - (context.getResources().getDimension(R.dimen.photo_editor_menu_bound_padding) * 2.0f)) / getItemCount());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public AdjustAdapterItemHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        View inflate = getInflater().inflate(R.layout.video_editor_adjust_menu_item, viewGroup, false);
        inflate.getLayoutParams().width = this.mItemWidth;
        return new AdjustAdapterItemHolder(inflate);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(AdjustAdapterItemHolder adjustAdapterItemHolder, int i) {
        super.onBindViewHolder((AdjustAdapter) adjustAdapterItemHolder, i);
        adjustAdapterItemHolder.bind(this.mDataList.get(i));
        this.mDelegator.onBindViewHolder(adjustAdapterItemHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataList.size();
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
    public static class AdjustAdapterItemHolder extends RecyclerView.ViewHolder {
        public ImageView mIconView;
        public TextView mLabelView;

        public AdjustAdapterItemHolder(View view) {
            super(view);
            FolmeUtil.setCustomTouchAnim(view, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
            this.mLabelView = (TextView) view.findViewById(R.id.labelView);
            this.mIconView = (ImageView) view.findViewById(R.id.iconView);
        }

        public void bind(AdjustData adjustData) {
            this.mIconView.setImageResource(adjustData.icon);
            this.mLabelView.setText(adjustData.name);
        }
    }
}
