package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import androidx.lifecycle.Lifecycle;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import com.xiaomi.stat.a.j;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class FacePageAdapter extends CursorMultiViewMediaAdapter implements CheckableAdapter {
    public static final String[] PROJECTION = {j.c, "microthumbfile", "thumbnailFile", "mixedDateTime", "mimeType", "duration", "faceXScale", "faceYScale", "faceWScale", "faceHScale", "exifOrientation", "photo_id", "sha1", "localFile", "serverId", "isFavorite", "location", "specialTypeFlags", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "exifImageWidth", "exifImageLength"};
    public boolean isPhotoModeNotFaceMode;

    public FacePageAdapter(Context context, Lifecycle lifecycle) {
        super(context, lifecycle);
        this.isPhotoModeNotFaceMode = true;
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public AbsSingleImageViewHolder createSingleImageViewHolder(View view, Lifecycle lifecycle) {
        return new BaseSingleImageViewHolder(view, lifecycle);
    }

    public void changeDisplayMode() {
        this.isPhotoModeNotFaceMode = !this.isPhotoModeNotFaceMode;
        notifyDataSetChanged();
    }

    public final RequestOptions getFaceModeRequestOptions(int i) {
        Cursor mo1558getItem = mo1558getItem(i);
        return GlideOptions.peopleFaceOf(new FaceRegionRectF(mo1558getItem.getFloat(6), mo1558getItem.getFloat(7), mo1558getItem.getFloat(8) + mo1558getItem.getFloat(6), mo1558getItem.getFloat(7) + mo1558getItem.getFloat(9), mo1558getItem.getInt(10)), getFileLength(i));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getThumbFilePath(int i) {
        return mo1558getItem(i).getString(2);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMicroThumbFilePath(int i) {
        return BaseMediaAdapter.getMicroPath(mo1558getItem(i), 1, 12);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOriginFilePath(int i) {
        return mo1558getItem(i).getString(13);
    }

    public String getFirstFaceServerId() {
        if (getItemCount() > 0) {
            return mo1558getItem(0).getString(14);
        }
        return null;
    }

    @Override // com.miui.gallery.adapter.MultiViewMediaAdapter
    public String getOptimalThumbFilePath(int i, boolean z) {
        String string;
        Cursor mo1558getItem = mo1558getItem(i);
        if (!this.isPhotoModeNotFaceMode) {
            string = mo1558getItem.getString(2);
            if (TextUtils.isEmpty(string)) {
                string = mo1558getItem.getString(13);
            }
            if (TextUtils.isEmpty(string)) {
                string = mo1558getItem.getString(1);
            }
        } else {
            string = mo1558getItem.getString(1);
            if (TextUtils.isEmpty(string)) {
                string = mo1558getItem.getString(2);
            }
            if (TextUtils.isEmpty(string)) {
                string = mo1558getItem.getString(13);
            }
        }
        return TextUtils.isEmpty(string) ? StorageUtils.getSafePriorMicroThumbnailPath(mo1558getItem.getString(12)) : string;
    }

    public long getItemPhotoId(int i) {
        return mo1558getItem(i).getLong(11);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getItemKey(int i) {
        return getItemPhotoId(i);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getSha1(int i) {
        return mo1558getItem(i).getString(12);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMimeType(int i) {
        return mo1558getItem(i).getString(4);
    }

    public boolean isFaceDisplayMode() {
        return !this.isPhotoModeNotFaceMode;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public Uri getDownloadUri(int i) {
        return BaseMediaAdapter.getDownloadUri(mo1558getItem(i), 11);
    }

    @Override // com.miui.gallery.adapter.CheckableAdapter
    public View getCheckableView(View view) {
        return ((MicroThumbGridItem) view).getCheckBox();
    }

    public boolean isFavorite(int i) {
        return mo1558getItem(i).getInt(15) > 0;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getFileLength(int i) {
        return mo1558getItem(i).getLong(18);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getCreateTime(int i) {
        return mo1558getItem(i).getLong(3);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getLocation(int i) {
        return mo1558getItem(i).getString(16);
    }

    /* loaded from: classes.dex */
    public class BaseSingleImageViewHolder extends AbsSingleImageViewHolder {
        public BaseSingleImageViewHolder(View view, Lifecycle lifecycle) {
            super(view, lifecycle);
        }

        @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<Object> list) {
            int packDataPosition = FacePageAdapter.this.packDataPosition(i, i2);
            Cursor mo1558getItem = FacePageAdapter.this.mo1558getItem(packDataPosition);
            if (FacePageAdapter.this.isPhotoModeNotFaceMode) {
                this.mView.bindImage(FacePageAdapter.this.getBindImagePath(packDataPosition), FacePageAdapter.this.getDownloadUri(packDataPosition), FacePageAdapter.this.getRequestOptions(packDataPosition));
            } else {
                this.mView.bindImage(FacePageAdapter.this.getBindImagePath(packDataPosition), FacePageAdapter.this.getDownloadUri(packDataPosition), FacePageAdapter.this.getFaceModeRequestOptions(packDataPosition));
            }
            String string = mo1558getItem.getString(4);
            this.mView.bindIndicator(string, mo1558getItem.getLong(5), mo1558getItem.getLong(17));
            this.mView.bindFavoriteIndicator(FacePageAdapter.this.isFavorite(packDataPosition));
            this.mView.setContentDescription(TalkBackUtil.getContentDescriptionForImage(FacePageAdapter.this.mContext, mo1558getItem.getLong(3), mo1558getItem.getString(16), string));
            Folme.useAt(this.mView).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(this.mView, new AnimConfig[0]);
        }
    }
}
