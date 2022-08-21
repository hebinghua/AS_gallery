package com.miui.gallery.editor.photo.app.adjust2;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.OnCompositionLoadedListener;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class Adjust2MenuAdapter extends Adapter<AdjustAdapterItemHolder> implements Selectable {
    public List<Adjust2Data> mDataList;
    public SparseArray<Boolean> mIsChangedByPosition;
    public boolean mIsPlayAnimation = true;
    public int mSelectedPosition;

    public Adjust2MenuAdapter(Context context, List<Adjust2Data> list, Selectable.Selector selector) {
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
        super.onBindViewHolder((Adjust2MenuAdapter) adjustAdapterItemHolder, i);
        Adjust2Data adjust2Data = this.mDataList.get(i);
        adjustAdapterItemHolder.bind(adjust2Data, adjust2Data.getId(), this.mSelectedPosition == i, this.mIsPlayAnimation);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataList.size() - 1;
    }

    public void setSelection(int i) {
        this.mSelectedPosition = i;
        notifyItemChanged(i);
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mSelectedPosition;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void setIsPlayAnimation(boolean z) {
        this.mIsPlayAnimation = z;
    }

    /* loaded from: classes2.dex */
    public static class AdjustAdapterItemHolder extends RecyclerView.ViewHolder {
        public ImageView mIconLavIv;
        public ImageView mIconView;
        public TextView mLabelView;
        public TextView mProgressView;

        public AdjustAdapterItemHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mLabelView = (TextView) view.findViewById(R.id.labelView);
            this.mIconLavIv = (ImageView) view.findViewById(R.id.iv_icon_lav);
            this.mProgressView = (TextView) view.findViewById(R.id.progress);
            this.mIconView = (ImageView) view.findViewById(R.id.iconView);
        }

        public void bind(Adjust2Data adjust2Data, int i, boolean z, boolean z2) {
            this.mIconView.setImageResource(adjust2Data.icon);
            this.itemView.setSelected(z);
            this.mLabelView.setText(adjust2Data.name);
            this.mProgressView.setText(String.valueOf((int) adjust2Data.progress));
            int i2 = 4;
            this.mProgressView.setVisibility((((int) adjust2Data.progress) == 0 || i == 10) ? 4 : 0);
            ImageView imageView = this.mIconView;
            if (((int) adjust2Data.progress) == 0 || i == 10) {
                i2 = 0;
            }
            imageView.setVisibility(i2);
            this.mIconLavIv.setVisibility(8);
            if (((int) adjust2Data.progress) != 0 || !z || !z2) {
                return;
            }
            this.mIconView.setVisibility(8);
            this.mIconLavIv.setVisibility(0);
            final LottieDrawable lottieDrawable = new LottieDrawable();
            this.mIconLavIv.setImageDrawable(lottieDrawable);
            LottieComposition.Factory.fromRawFile(this.itemView.getContext(), adjust2Data.iconJson, new OnCompositionLoadedListener() { // from class: com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuAdapter.AdjustAdapterItemHolder.1
                @Override // com.airbnb.lottie.OnCompositionLoadedListener
                public void onCompositionLoaded(LottieComposition lottieComposition) {
                    lottieDrawable.setComposition(lottieComposition);
                    lottieDrawable.playAnimation();
                }
            });
        }
    }
}
