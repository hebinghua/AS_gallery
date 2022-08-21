package com.miui.gallery.editor.photo.app.sticker;

import android.content.Context;
import android.util.Size;
import android.view.ViewGroup;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.StickerData;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class CategoryDetailAdapter extends Adapter<StickerHolder> {
    public List<StickerData> mDataList;
    public RequestOptions mRequestOptions = GlideOptions.formatOf(DecodeFormat.PREFER_ARGB_8888).mo950diskCacheStrategy(DiskCacheStrategy.NONE).autoClone();
    public Size mSize;

    public CategoryDetailAdapter(Context context, List<StickerData> list) {
        this.mDataList = list;
        this.mSize = new Size(context.getResources().getDimensionPixelSize(R.dimen.editor_menu_sticker_view_pager_item_width), context.getResources().getDimensionPixelSize(R.dimen.editor_menu_sticker_view_pager_item_height));
    }

    public List<StickerData> getDataList() {
        return this.mDataList;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public StickerHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new StickerHolder(getInflater().inflate(R.layout.sticker_menu_item, viewGroup, false), this.mRequestOptions, this.mSize);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(StickerHolder stickerHolder, int i) {
        super.onBindViewHolder((CategoryDetailAdapter) stickerHolder, i);
        stickerHolder.bind(this.mDataList.get(i), i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataList.size();
    }
}
