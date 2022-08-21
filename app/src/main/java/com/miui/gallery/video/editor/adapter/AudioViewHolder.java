package com.miui.gallery.video.editor.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.editor.ui.view.DownloadView;
import com.miui.gallery.glide.request.target.GalleryBitmapImageViewTarget;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.video.editor.ImageLoadingProcess;
import com.miui.gallery.video.editor.util.ImageLoaderUtils;
import com.miui.gallery.video.editor.util.ToolsUtil;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;

/* loaded from: classes2.dex */
public class AudioViewHolder extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder {
    public static RequestOptions mDisplayImageOptions = ImageLoaderUtils.mVideoEditorDefaultOptions;
    public DownloadView mDownloadView;
    public ImageView mIcon;
    public ImageLoadingProcess mImageLoadingProcess;
    public TextView mNameTextView;
    public View mSelected;

    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder
    public void setSelect(boolean z) {
    }

    public AudioViewHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mNameTextView = (TextView) view.findViewById(R.id.item_title);
        this.mIcon = (ImageView) view.findViewById(R.id.item_iv);
        this.mDownloadView = (DownloadView) view.findViewById(R.id.item_download);
        this.mSelected = view.findViewById(R.id.iv_selected);
        this.mImageLoadingProcess = new ImageLoadingProcess(this.mIcon);
    }

    public void setName(int i) {
        if (i != 0) {
            this.mNameTextView.setText(i);
        }
    }

    public void setStateImage(int i) {
        this.mDownloadView.setStateImage(i);
    }

    public void updateSelected(boolean z, boolean z2) {
        if (z && z2) {
            ToolsUtil.showView(this.mSelected);
        } else {
            ToolsUtil.hideView(this.mSelected);
        }
        this.mNameTextView.setSelected(z);
    }

    public void setIcon(String str, int i, int i2) {
        if (i != 0) {
            this.mIcon.setImageResource(i);
        } else if (!TextUtils.isEmpty(str)) {
            this.mImageLoadingProcess.setBgColor(i2);
            Glide.with(this.itemView).mo985asBitmap().mo963load(str).mo946apply((BaseRequestOptions<?>) mDisplayImageOptions).into((RequestBuilder<Bitmap>) new GalleryBitmapImageViewTarget(this.mIcon, str, this.mImageLoadingProcess));
        } else if (i2 == 0) {
        } else {
            this.mIcon.setImageResource(i2);
        }
    }
}
