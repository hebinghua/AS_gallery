package com.miui.gallery.collage.app.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.collage.core.layout.LayoutModel;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes.dex */
public class LayoutMenuAdapter extends Adapter<RecyclerView.ViewHolder> implements Selectable {
    public Context mContext;
    public List<LayoutModel> mLayoutList;
    public int mPreviousPosition = 2;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(2, null);

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (i == 0) {
            return 0;
        }
        return i == 1 ? 1 : 2;
    }

    public LayoutMenuAdapter(Context context, List<LayoutModel> list, Selectable.Selector selector) {
        this.mContext = context;
        this.mLayoutList = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            return new MarginViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_icon_margin_item, viewGroup, false));
        }
        if (i == 1) {
            return new RatioViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_icon_ratio_item, viewGroup, false));
        }
        return new LayoutHolder(viewGroup, this.mContext);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        if (i != 0 || !(viewHolder instanceof MarginViewHolder)) {
            boolean z = true;
            if (i != 1 || !(viewHolder instanceof RatioViewHolder)) {
                if (!(viewHolder instanceof LayoutHolder)) {
                    return;
                }
                LayoutHolder layoutHolder = (LayoutHolder) viewHolder;
                layoutHolder.setLayoutModel(this.mLayoutList.get(i), i);
                if (i != this.mDelegator.getSelection()) {
                    z = false;
                }
                layoutHolder.setSelectorState(z);
                this.mDelegator.onBindViewHolder(viewHolder, i);
                return;
            }
            ((RatioViewHolder) viewHolder).mRatioText.setText(R.string.collage_ratio_text);
            viewHolder.itemView.setContentDescription(this.mContext.getResources().getString(R.string.collage_talkback_ratio_3_4));
            return;
        }
        ((MarginViewHolder) viewHolder).mMarginText.setText(R.string.collage_margin_text);
        viewHolder.itemView.setContentDescription(this.mContext.getResources().getString(R.string.collage_talkback_margin_non));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mLayoutList.size();
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
        notifyItemChanged(i);
        notifyItemChanged(this.mPreviousPosition);
        this.mPreviousPosition = i;
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

    /* loaded from: classes.dex */
    public static class MarginViewHolder extends RecyclerView.ViewHolder {
        public ImageView mMarginIcon;
        public TextView mMarginText;

        public MarginViewHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mMarginIcon = (ImageView) view.findViewById(R.id.collage_margin_icon);
            this.mMarginText = (TextView) view.findViewById(R.id.collage_margin_icon_title);
        }

        public ImageView getmMarginIcon() {
            return this.mMarginIcon;
        }
    }

    /* loaded from: classes.dex */
    public static class RatioViewHolder extends RecyclerView.ViewHolder {
        public ImageView mRatioIcon;
        public TextView mRatioText;

        public RatioViewHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mRatioIcon = (ImageView) view.findViewById(R.id.collage_ratio_icon);
            this.mRatioText = (TextView) view.findViewById(R.id.collage_ratio_icon_title);
        }

        public ImageView getmRatioIcon() {
            return this.mRatioIcon;
        }
    }
}
