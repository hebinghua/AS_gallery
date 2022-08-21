package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.RectF;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import androidx.lifecycle.Lifecycle;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.xiaomi.stat.a.j;
import java.util.List;

/* loaded from: classes.dex */
public class PickPeopleCoverAdapter extends CursorMultiViewMediaAdapter {
    public static final String[] PROJECTION = {j.c, "microthumbfile", "thumbnailFile", "mixedDateTime", "mimeType", "faceXScale", "faceYScale", "faceWScale", "faceHScale", "exifOrientation", "photo_id", "sha1", "localFile", "serverId", "location", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE};

    public PickPeopleCoverAdapter(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter, com.miui.gallery.adapter.BaseMediaAdapter
    public GlideOptions getRequestOptions(int i) {
        Cursor mo1558getItem = mo1558getItem(i);
        return super.getRequestOptions(i).decodeRegion(RegionConfig.ofFace(new FaceRegionRectF(mo1558getItem.getFloat(5), mo1558getItem.getFloat(6), mo1558getItem.getFloat(7) + mo1558getItem.getFloat(5), mo1558getItem.getFloat(6) + mo1558getItem.getFloat(8), mo1558getItem.getInt(9)), 2.0f));
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
        return new SingleImageViewHolder(view, lifecycle);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getThumbFilePath(int i) {
        return mo1558getItem(i).getString(2);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMicroThumbFilePath(int i) {
        return BaseMediaAdapter.getMicroPath(mo1558getItem(i), 1, 11);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOriginFilePath(int i) {
        return mo1558getItem(i).getString(12);
    }

    public String getFaceServerId(int i) {
        return mo1558getItem(i).getString(13);
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public String getOptimalThumbFilePath(int i, boolean z) {
        Cursor mo1558getItem = mo1558getItem(i);
        String string = mo1558getItem.getString(2);
        if (TextUtils.isEmpty(string)) {
            string = mo1558getItem.getString(12);
        }
        if (TextUtils.isEmpty(string)) {
            string = mo1558getItem.getString(1);
        }
        return TextUtils.isEmpty(string) ? StorageUtils.getSafePriorMicroThumbnailPath(mo1558getItem.getString(11)) : string;
    }

    public String getClearThumbFilePath(int i) {
        Cursor mo1558getItem = mo1558getItem(i);
        String string = mo1558getItem.getString(2);
        if (TextUtils.isEmpty(string)) {
            string = mo1558getItem.getString(12);
        }
        if (TextUtils.isEmpty(string)) {
            string = mo1558getItem.getString(1);
        }
        return TextUtils.isEmpty(string) ? StorageUtils.getSafePriorMicroThumbnailPath(mo1558getItem.getString(11)) : string;
    }

    public long getItemPhotoId(int i) {
        return mo1558getItem(i).getLong(10);
    }

    public RectF getFaceRegionRectF(int i) {
        Cursor mo1558getItem = mo1558getItem(i);
        if (mo1558getItem == null) {
            return new FaceRegionRectF(0.0f, 0.0f, 0.0f, 0.0f, 0);
        }
        return new FaceRegionRectF(mo1558getItem.getFloat(5), mo1558getItem.getFloat(6), mo1558getItem.getFloat(5) + mo1558getItem.getFloat(7), mo1558getItem.getFloat(6) + mo1558getItem.getFloat(8), mo1558getItem.getInt(9));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getItemKey(int i) {
        return getItemPhotoId(i);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getSha1(int i) {
        return mo1558getItem(i).getString(11);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMimeType(int i) {
        return mo1558getItem(i).getString(4);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public Uri getDownloadUri(int i) {
        return BaseMediaAdapter.getDownloadUri(mo1558getItem(i), 10);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getFileLength(int i) {
        return mo1558getItem(i).getLong(15);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getCreateTime(int i) {
        return mo1558getItem(i).getLong(3);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getLocation(int i) {
        return mo1558getItem(i).getString(14);
    }

    /* loaded from: classes.dex */
    public class SingleImageViewHolder extends AbsSingleImageViewHolder {
        public SingleImageViewHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
        }

        @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<Object> list) {
            int packDataPosition = PickPeopleCoverAdapter.this.packDataPosition(i, i2);
            this.mView.bindImage(PickPeopleCoverAdapter.this.getBindImagePath(packDataPosition), PickPeopleCoverAdapter.this.getDownloadUri(packDataPosition), DownloadType.MICRO, PickPeopleCoverAdapter.this.getRequestOptions(packDataPosition));
        }
    }
}
