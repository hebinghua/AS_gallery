package com.miui.gallery.collage.app.poster;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.collage.core.poster.PosterModel;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes.dex */
public class PosterMenuAdapter extends Adapter<PosterHolder> implements Selectable {
    public Context mContext;
    public int mImageCount;
    public List<PosterModel> mPosterList;
    public int mLastPosition = 0;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(0, null);

    public PosterMenuAdapter(Context context, List<PosterModel> list, Selectable.Selector selector) {
        this.mContext = context;
        this.mPosterList = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public PosterHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new PosterHolder(viewGroup, this.mContext);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(PosterHolder posterHolder, int i) {
        super.onBindViewHolder((PosterMenuAdapter) posterHolder, i);
        posterHolder.setPosterModel(this.mContext.getResources(), this.mPosterList.get(i), this.mImageCount, i);
        posterHolder.setSelectorState(i == this.mDelegator.getSelection());
        this.mDelegator.onBindViewHolder(posterHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mPosterList.size();
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
        notifyItemChanged(i);
        notifyItemChanged(this.mLastPosition);
        this.mLastPosition = i;
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

    public void setImageCount(int i) {
        this.mImageCount = i;
    }
}
