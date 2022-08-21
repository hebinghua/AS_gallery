package com.miui.gallery.editor.photo.app.miuibeautify;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.drawable.SelectableDrawable;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;
import miuix.view.animation.SineEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class SmartBeautyLevelItemAdapter extends Adapter<BeautyLevelItemHolder> implements Selectable {
    public List<LevelItem> mBeautyItemList;
    public Context mContext;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(0);

    public SmartBeautyLevelItemAdapter(Context context, List<LevelItem> list) {
        this.mContext = context;
        this.mBeautyItemList = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BeautyLevelItemHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BeautyLevelItemHolder(getInflater().inflate(R.layout.beauty_level_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(BeautyLevelItemHolder beautyLevelItemHolder, int i) {
        super.onBindViewHolder((SmartBeautyLevelItemAdapter) beautyLevelItemHolder, i);
        beautyLevelItemHolder.bind(this.mBeautyItemList.get(i).getNormalResource(), this.mBeautyItemList.get(i).getPressedResource());
        this.mDelegator.onBindViewHolder(beautyLevelItemHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<LevelItem> list = this.mBeautyItemList;
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
    public static class BeautyLevelItemHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;

        public BeautyLevelItemHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mImageView = (ImageView) view.findViewById(R.id.img);
        }

        public void bind(int i, int i2) {
            SelectableDrawable selectableDrawable = new SelectableDrawable(this.mImageView.getResources().getDrawable(i), this.mImageView.getResources().getDrawable(i2));
            selectableDrawable.setInterpolator(new SineEaseInOutInterpolator());
            selectableDrawable.setDuration(this.mImageView.getResources().getInteger(R.integer.selectable_drawable_fade_duration));
            this.mImageView.setImageDrawable(selectableDrawable);
        }
    }

    /* loaded from: classes2.dex */
    public static class LevelItem {
        public int mResourceNormal;
        public int mResourcePressed;

        public LevelItem(int i, int i2) {
            this.mResourceNormal = i;
            this.mResourcePressed = i2;
        }

        public int getNormalResource() {
            return this.mResourceNormal;
        }

        public int getPressedResource() {
            return this.mResourcePressed;
        }
    }
}
