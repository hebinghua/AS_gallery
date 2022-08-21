package com.miui.gallery.collage.app.stitching;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.collage.core.stitching.StitchingModel;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes.dex */
public class StitchingAdapter extends Adapter<StitchingHolder> implements Selectable {
    public Context mContext;
    public List<StitchingModel> mStitchingModelList;
    public int mLastPosition = 0;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(0, null);

    public StitchingAdapter(Context context, List<StitchingModel> list, Selectable.Selector selector) {
        this.mStitchingModelList = list;
        this.mContext = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public StitchingHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new StitchingHolder(viewGroup, this.mContext);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(StitchingHolder stitchingHolder, int i) {
        super.onBindViewHolder((StitchingAdapter) stitchingHolder, i);
        stitchingHolder.setStitchingModel(this.mContext.getResources(), this.mStitchingModelList.get(i), i);
        stitchingHolder.setSelectorState(i == this.mDelegator.getSelection());
        this.mDelegator.onBindViewHolder(stitchingHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mStitchingModelList.size();
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
}
