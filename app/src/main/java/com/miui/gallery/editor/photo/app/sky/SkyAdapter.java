package com.miui.gallery.editor.photo.app.sky;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.SkyData;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class SkyAdapter extends Adapter<SkyHolder> implements Selectable {
    public List<SkyData> mEffects;
    public boolean mHasAnimationPlayed;
    public int mLastSelectedIndex = 0;
    public boolean mEditMode = false;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(0, null);

    public SkyAdapter(List<SkyData> list) {
        this.mEffects = list;
    }

    public SkyData getItemData(int i) {
        List<SkyData> list = this.mEffects;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mEffects.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public SkyHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SkyHolder(getInflater().inflate(R.layout.filter_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(SkyHolder skyHolder, int i) {
        super.onBindViewHolder((SkyAdapter) skyHolder, i);
        boolean z = i == this.mDelegator.getSelection();
        skyHolder.bindData(this.mEffects.get(i), z, this.mEditMode);
        this.mDelegator.onBindViewHolder(skyHolder, i);
        if (!z || this.mHasAnimationPlayed) {
            return;
        }
        skyHolder.setConfigIndicator();
        this.mHasAnimationPlayed = true;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mEffects.size();
    }

    public void setSelection(int i) {
        this.mHasAnimationPlayed = false;
        this.mDelegator.setSelection(i);
        notifyItemChanged(i);
        notifyItemChanged(this.mLastSelectedIndex);
        this.mLastSelectedIndex = i;
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mDelegator.getSelection();
    }

    public boolean isInEditMode() {
        return this.mEditMode;
    }

    public void enterEditMode() {
        this.mEditMode = true;
        notifyItemChanged(this.mDelegator.getSelection());
    }

    public void exitEditMode() {
        this.mEditMode = false;
        notifyItemChanged(this.mDelegator.getSelection());
    }

    public void update(int i) {
        if (!this.mEditMode || this.mDelegator.getSelection() == -1) {
            return;
        }
        this.mEffects.get(this.mDelegator.getSelection()).setProgress(i);
        notifyItemChanged(this.mDelegator.getSelection());
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
}
