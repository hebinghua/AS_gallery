package com.miui.gallery.movie.picker;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.adapter.BaseMediaAdapter;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.adapter.CursorMediaAdapter;
import com.miui.gallery.adapter.SyncStateDisplay$DisplayScene;
import com.miui.gallery.biz.story.StoryAlbumAdapter;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;

/* loaded from: classes2.dex */
public class StoryMoviePickAdapter extends CursorMediaAdapter implements CheckableAdapter {
    public static final String[] PROJECTION = StoryAlbumAdapter.PROJECTION;
    public Picker mPicker;

    public StoryMoviePickAdapter(Context context, Picker picker) {
        this(context, SyncStateDisplay$DisplayScene.SCENE_IN_CHECK_MODE);
        this.mPicker = picker;
    }

    public StoryMoviePickAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene) {
        super(context, syncStateDisplay$DisplayScene);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public void doBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        MicroThumbGridItem microThumbGridItem = (MicroThumbGridItem) baseViewHolder.itemView;
        Cursor mo1558getItem = mo1558getItem(i);
        microThumbGridItem.bindImage(getBindImagePath(mo1558getItem.getPosition()), getDownloadUri(mo1558getItem.getPosition()), getRequestOptions(i));
        microThumbGridItem.bindFavoriteIndicator(isFavorite(mo1558getItem.getPosition()));
        microThumbGridItem.bindSyncIndicator(mo1558getItem.getLong(0), getSyncState(mo1558getItem), this.mShowScene);
    }

    public final int getSyncState(Cursor cursor) {
        return getSyncStateInternal(cursor.getInt(16));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new BaseViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.base_image_grid_item, viewGroup, false));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getItemKey(int i) {
        return mo1558getItem(i).getLong(0);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getSha1(int i) {
        return mo1558getItem(i).getString(15);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMimeType(int i) {
        return mo1558getItem(i).getString(4);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOptimalThumbFilePath(int i) {
        return BaseMediaAdapter.getMicroPath(mo1558getItem(i), 22, 15);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOriginFilePath(int i) {
        return mo1558getItem(i).getString(3);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getCreateTime(int i) {
        return mo1558getItem(i).getLong(5);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getLocation(int i) {
        return mo1558getItem(i).getString(6);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public Uri getDownloadUri(int i) {
        return BaseMediaAdapter.getDownloadUri(mo1558getItem(i), 16, 0);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getThumbFilePath(int i) {
        return mo1558getItem(i).getString(2);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMicroThumbFilePath(int i) {
        return BaseMediaAdapter.getMicroPath(mo1558getItem(i), 1, 15);
    }

    public boolean isFavorite(int i) {
        return mo1558getItem(i).getInt(18) > 0;
    }

    @Override // com.miui.gallery.adapter.CheckableAdapter
    public View getCheckableView(View view) {
        return ((MicroThumbGridItem) view).getCheckBox();
    }
}
