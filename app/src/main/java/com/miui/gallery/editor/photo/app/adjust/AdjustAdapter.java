package com.miui.gallery.editor.photo.app.adjust;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.AdjustData;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class AdjustAdapter extends Adapter<AdjustAdapterItemHolder> implements Selectable {
    public List<AdjustData> mDataList;
    public SparseArray<Boolean> mIsChangedByPosition;
    public boolean mIsPlayAnimation;
    public int mSelectedPosition;

    public AdjustAdapter(Context context, List<AdjustData> list, Selectable.Selector selector) {
        this.mDataList = list;
        int size = list.size();
        this.mIsChangedByPosition = new SparseArray<>(size);
        for (int i = 0; i < size; i++) {
            this.mIsChangedByPosition.append(i, Boolean.FALSE);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public AdjustAdapterItemHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AdjustAdapterItemHolder(getInflater().inflate(R.layout.adjust_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(AdjustAdapterItemHolder adjustAdapterItemHolder, int i) {
        super.onBindViewHolder((AdjustAdapter) adjustAdapterItemHolder, i);
        adjustAdapterItemHolder.bind(this.mDataList.get(i), this.mSelectedPosition == i, this.mIsPlayAnimation);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataList.size();
    }

    public void setSelection(int i) {
        this.mSelectedPosition = i;
        notifyDataSetChanged();
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mSelectedPosition;
    }

    public void setSelectedChanged() {
        this.mIsChangedByPosition.setValueAt(getSelection(), Boolean.TRUE);
        notifyItemChanged(getSelection());
    }

    public void setIsPlayAnimation(boolean z) {
        this.mIsPlayAnimation = z;
    }

    /* loaded from: classes2.dex */
    public static class AdjustAdapterItemHolder extends RecyclerView.ViewHolder {
        public LottieAnimationView mIconLottieView;
        public ImageView mIconView;
        public TextView mLabelView;
        public TextView mProgressView;

        public AdjustAdapterItemHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mLabelView = (TextView) view.findViewById(R.id.labelView);
            this.mProgressView = (TextView) view.findViewById(R.id.progress);
            this.mIconView = (ImageView) view.findViewById(R.id.iconView);
            this.mIconLottieView = (LottieAnimationView) view.findViewById(R.id.icon_lav);
        }

        public final void playAnimation(boolean z) {
            if (z) {
                this.mIconLottieView.setVisibility(0);
                this.mIconLottieView.playAnimation();
                this.mIconView.setImageDrawable(null);
                return;
            }
            this.mIconView.setVisibility(0);
            this.mIconLottieView.setVisibility(8);
        }

        public void bind(AdjustData adjustData, boolean z, boolean z2) {
            this.itemView.setSelected(z);
            ImageView imageView = this.mIconView;
            imageView.setImageDrawable(imageView.getContext().getDrawable(adjustData.icon));
            this.mLabelView.setText(adjustData.name);
            this.mProgressView.setText(String.valueOf(adjustData.progress));
            this.mIconLottieView.setAnimation(adjustData.iconJson);
            if (z && adjustData.progress == 0) {
                this.mProgressView.setVisibility(4);
                playAnimation(z2);
            } else if (adjustData.progress != 0) {
                this.mIconView.setVisibility(8);
                this.mProgressView.setVisibility(0);
                this.mIconLottieView.setVisibility(8);
                this.mIconLottieView.setVisibility(8);
            } else {
                this.mIconView.setVisibility(0);
                this.mProgressView.setVisibility(8);
                this.mIconLottieView.setVisibility(8);
            }
        }
    }
}
