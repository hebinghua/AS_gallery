package com.miui.gallery.video.editor.adapter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
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
public class WaterMarkHolder extends SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder {
    public static RequestOptions mDisplayImageOptions = ImageLoaderUtils.mVideoEditorDefaultOptions;
    public DownloadView mDownloadView;
    public ImageView mIcon;
    public ImageLoadingProcess mImageLoadingProcess;
    public View mSelectBackground;
    public View mSelected;
    public ImageView mTextEditable;

    @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.SingleChoiceViewHolder
    public void setSelect(boolean z) {
    }

    public WaterMarkHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mIcon = (ImageView) view.findViewById(R.id.video_editor_text_image);
        this.mDownloadView = (DownloadView) view.findViewById(R.id.item_download);
        this.mSelected = view.findViewById(R.id.selector);
        this.mTextEditable = (ImageView) view.findViewById(R.id.video_editor_edit);
        this.mSelectBackground = view.findViewById(R.id.select_edit_background);
        this.mDownloadView.setBackground(ToolsUtil.isRTLDirection() ? view.getResources().getDrawable(R.drawable.video_editor_icon_watermark_download_start) : view.getResources().getDrawable(R.drawable.video_editor_icon_watermark_download));
        this.mImageLoadingProcess = new ImageLoadingProcess(this.mIcon);
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

    public void setStateImage(int i) {
        this.mDownloadView.setStateImage(i);
    }

    public void updateSelected(boolean z, boolean z2) {
        if (z && z2) {
            ToolsUtil.showView(this.mSelected);
        } else {
            ToolsUtil.hideView(this.mSelected);
        }
    }

    public void updateTextEditable(boolean z) {
        if (z) {
            ToolsUtil.showView(this.mTextEditable);
            ToolsUtil.showView(this.mSelectBackground);
            return;
        }
        ToolsUtil.hideView(this.mTextEditable);
        ToolsUtil.hideView(this.mSelectBackground);
    }
}
