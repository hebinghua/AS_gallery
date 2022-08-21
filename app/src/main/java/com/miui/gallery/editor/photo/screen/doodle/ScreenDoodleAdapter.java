package com.miui.gallery.editor.photo.screen.doodle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.drawable.SelectableDrawable;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;
import miuix.view.animation.SineEaseInOutInterpolator;

/* loaded from: classes2.dex */
public class ScreenDoodleAdapter extends Adapter<ScreenDoodleHolder> implements Selectable {
    public List<DoodleData> mDataList;
    public Selectable.Delegator mDelegator;

    public ScreenDoodleAdapter(List<DoodleData> list, int i) {
        this.mDataList = list;
        this.mDelegator = new Selectable.Delegator(i, null);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataList.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ScreenDoodleHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ScreenDoodleHolder(getInflater().inflate(R.layout.screen_doodle_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ScreenDoodleHolder screenDoodleHolder, int i) {
        super.onBindViewHolder((ScreenDoodleAdapter) screenDoodleHolder, i);
        screenDoodleHolder.bind(this.mDelegator.getSelection() == i, this.mDataList.get(i));
        this.mDelegator.onBindViewHolder(screenDoodleHolder, i);
    }

    public void setSelection(int i) {
        int selection = this.mDelegator.getSelection();
        if (selection != i) {
            this.mDelegator.setSelection(i);
            notifyItemChanged(selection);
            notifyItemChanged(i);
        }
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
    public static final class ScreenDoodleHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ScreenDoodleHolder(View view) {
            super(view);
            FolmeUtil.setDefaultTouchAnim(view, null, true);
            this.mImageView = (ImageView) view.findViewById(R.id.img);
        }

        public void bind(boolean z, DoodleData doodleData) {
            SelectableDrawable selectableDrawable = new SelectableDrawable(this.mImageView.getResources().getDrawable(doodleData.normal), this.mImageView.getResources().getDrawable(doodleData.selected));
            selectableDrawable.setInterpolator(new SineEaseInOutInterpolator());
            selectableDrawable.setDuration(this.mImageView.getResources().getInteger(R.integer.selectable_drawable_fade_duration));
            this.mImageView.setImageDrawable(selectableDrawable);
            ImageView imageView = this.mImageView;
            imageView.setContentDescription(imageView.getResources().getString(doodleData.talkback));
            this.mImageView.setSelected(z);
        }
    }
}
