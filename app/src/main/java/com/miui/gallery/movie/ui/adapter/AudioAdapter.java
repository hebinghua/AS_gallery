package com.miui.gallery.movie.ui.adapter;

import android.content.Context;
import android.view.View;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.movie.entity.AudioResource;
import com.miui.gallery.movie.ui.adapter.BaseAdapter;

/* loaded from: classes2.dex */
public class AudioAdapter extends BaseAdapter<AudioResource> {
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return i == 1 ? 1 : 0;
    }

    public AudioAdapter(Context context) {
        super(context);
        this.mSelectedItemPosition = 0;
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    public int getLayoutId(int i) {
        if (i == 1) {
            return R$layout.movie_layout_local_audio_item;
        }
        return R$layout.movie_layout_audio_item;
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter
    public BaseAdapter.BaseHolder<AudioResource> getHolder(View view) {
        return new BaseResourceHolder(view);
    }

    @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(BaseAdapter.BaseHolder<AudioResource> baseHolder, int i) {
        super.onBindViewHolder((BaseAdapter.BaseHolder) baseHolder, i);
        if (i == 0) {
            baseHolder.itemView.setContentDescription(this.mContext.getResources().getString(R$string.movie_audio_no_cd));
        } else if (i != 1) {
        } else {
            baseHolder.itemView.setContentDescription(this.mContext.getResources().getString(R$string.movie_audio_local_cd));
        }
    }
}
