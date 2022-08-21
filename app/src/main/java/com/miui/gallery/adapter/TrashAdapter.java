package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.trash.TrashBinItem;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.ui.MicroThumbGridItem;
import com.miui.gallery.ui.TrashGridItem;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;
import miuix.animation.Folme;
import miuix.animation.IHoverStyle;
import miuix.animation.base.AnimConfig;

/* loaded from: classes.dex */
public class TrashAdapter extends CursorMediaAdapter implements CheckableAdapter {
    public static final String[] PROJECTION = {j.c, "cloudId", "cloudServerId", "fileName", "deleteTime", "microFilePath", "trashFilePath", "isOrigin", "albumLocalId", "albumServerId", "albumName", "albumPath", "sha1", "mimeType", "imageHeight", "imageWidth", "orientation", "duration", "mixedDateTime", GalleryContract.TrashBin.SERVER_TAG, "secretKey", "creatorId", "imageSize", "status", nexExportFormat.TAG_FORMAT_TAG};
    public boolean mCheckable;
    public TrashUtils.UserInfo mUserInfo;

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getLocation(int i) {
        return "";
    }

    public TrashAdapter(Context context) {
        super(context);
        setHasStableIds(true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BaseViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return BaseViewHolder.create(viewGroup, R.layout.trash_grid_item);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewRecycled(BaseViewHolder baseViewHolder) {
        super.onViewRecycled((TrashAdapter) baseViewHolder);
        View view = baseViewHolder.itemView;
        if (view instanceof MicroThumbGridItem) {
            ((MicroThumbGridItem) view).recycle();
        }
    }

    public void setUserInfo(TrashUtils.UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMicroThumbFilePath(int i) {
        return mo1558getItem(i).getString(5);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getOptimalThumbFilePath(int i) {
        Cursor mo1558getItem = mo1558getItem(i);
        String string = mo1558getItem.getString(5);
        return TextUtils.isEmpty(string) ? mo1558getItem.getString(6) : string;
    }

    public Long getImageSize(int i) {
        return Long.valueOf(mo1558getItem(i).getLong(22));
    }

    public boolean getIsOrigin(int i) {
        return mo1558getItem(i).getInt(7) == 1;
    }

    public String getServiceId(int i) {
        return mo1558getItem(i).getString(2);
    }

    public String getFilePath(int i) {
        return mo1558getItem(i).getString(6);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getItemKey(int i) {
        return mo1558getItem(i).getLong(0);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getSha1(int i) {
        return mo1558getItem(i).getString(12);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public String getMimeType(int i) {
        return mo1558getItem(i).getString(13);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public int getImageWidth(int i) {
        return mo1558getItem(i).getInt(15);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public int getImageHeight(int i) {
        return mo1558getItem(i).getInt(14);
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public long getCreateTime(int i) {
        return mo1558getItem(i).getLong(18);
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public void doBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        TrashGridItem trashGridItem = (TrashGridItem) baseViewHolder.itemView;
        if (this.mGridItem == null) {
            this.mGridItem = trashGridItem;
        }
        if (isSecretPosition(i)) {
            trashGridItem.bindImage(null, null, getRequestOptions(i));
            trashGridItem.getBackgroundImageView().setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.secret_image_icon));
        } else {
            trashGridItem.bindImage(getBindImagePath(i), getDownloadUri(i), getRequestOptions(i), getPreviewRequestOptions(i));
        }
        Cursor mo1558getItem = mo1558getItem(i);
        trashGridItem.bindIndicator(mo1558getItem.getString(13), mo1558getItem.getInt(17), 0L);
        trashGridItem.setRemainDuration(TrashUtils.getRetentionTime(mo1558getItem.getLong(4), this.mUserInfo));
        trashGridItem.setCheckable(this.mCheckable);
        trashGridItem.setImageForeground(R.drawable.rect_item_fg_with_stroke);
        Folme.useAt(trashGridItem).hover().setEffect(IHoverStyle.HoverEffect.NORMAL).handleHoverOf(trashGridItem, new AnimConfig[0]);
    }

    public boolean isSecretPosition(int i) {
        Cursor mo1558getItem = mo1558getItem(i);
        return mo1558getItem.getBlob(20) != null || mo1558getItem.getLong(8) == -1000;
    }

    public TrashBinItem getTrashBinItem(int i) {
        return new TrashBinItem(mo1558getItem(i));
    }

    @Override // com.miui.gallery.adapter.IMediaAdapter
    public Uri getDownloadUri(int i) {
        return CloudUriAdapter.getDownloadUri(mo1558getItem(i).getLong(1));
    }

    public void enterChoiceMode() {
        this.mCheckable = true;
    }

    public void exitChoiceMode() {
        this.mCheckable = false;
    }

    @Override // com.miui.gallery.adapter.CheckableAdapter
    public View getCheckableView(View view) {
        return ((TrashGridItem) view).getCheckBox();
    }
}
