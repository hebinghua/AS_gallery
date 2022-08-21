package com.miui.gallery.vlog.caption;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.editor.ui.view.DownloadView;
import com.miui.gallery.editor.ui.view.RoundImageView;
import com.miui.gallery.glide.request.target.GalleryBitmapImageViewTarget;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$color;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.entity.HeaderTailData;
import com.miui.gallery.vlog.home.ImageLoadingProcess;
import com.miui.gallery.vlog.tools.ImageLoaderUtils;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public class HeaderTailHolder extends RecyclerView.ViewHolder {
    public HeaderTailData mData;
    public DownloadView mDownloadView;
    public ImageView mEditor;
    public ImageView mFrame;
    public ImageLoadingProcess mImageLoadingProcess;
    public RoundImageView mPreviewView;
    public boolean mSelected;
    public ImageView mSelector;

    public HeaderTailHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mPreviewView = (RoundImageView) view.findViewById(R$id.preview);
        this.mSelector = (ImageView) view.findViewById(R$id.selector);
        this.mEditor = (ImageView) view.findViewById(R$id.configable_editor);
        this.mFrame = (ImageView) view.findViewById(R$id.frame);
        this.mImageLoadingProcess = new ImageLoadingProcess(this.mPreviewView);
        this.mDownloadView = (DownloadView) view.findViewById(R$id.item_download);
        RoundImageView roundImageView = this.mPreviewView;
        roundImageView.setCorner(roundImageView.getResources().getDimensionPixelSize(R$dimen.vlog_header_tail_menu_corner));
    }

    public void bind(HeaderTailData headerTailData) {
        this.mData = headerTailData;
    }

    public void setIcon(String str, int i, int i2) {
        if (i != 0) {
            if (this.mData.isNone()) {
                this.mPreviewView.setImageResource(0);
            } else {
                this.mPreviewView.setImageResource(i);
            }
        } else if (!TextUtils.isEmpty(str)) {
            this.mImageLoadingProcess.setBgColor(i2);
            Glide.with(this.itemView).mo985asBitmap().mo963load(str).mo946apply((BaseRequestOptions<?>) ImageLoaderUtils.sRequestOptions).into((RequestBuilder<Bitmap>) new GalleryBitmapImageViewTarget(this.mPreviewView, str, this.mImageLoadingProcess));
        } else if (i2 == 0) {
        } else {
            this.mPreviewView.setImageResource(i2);
        }
    }

    public void setState(boolean z) {
        this.mSelected = z;
        this.mFrame.setBackgroundResource(R$color.vlog_transparent);
        this.mEditor.setImageResource(R$drawable.ic_audio_menu_edit);
        if (this.mData.isNone()) {
            VlogUtils.hideViews(this.mSelector);
            VlogUtils.showViews(this.mPreviewView, this.mEditor);
            this.mEditor.setImageResource(R$drawable.template_menu_default);
            this.mPreviewView.setImageResource(R$drawable.vlog_template_default_bg);
            if (!this.mSelected) {
                return;
            }
            this.mFrame.setBackgroundResource(com.miui.gallery.editor.R$drawable.filter_menu_item_selected);
        } else if (this.mData.isCustom()) {
            this.mSelector.setImageResource(R$drawable.vlog_caption_default_bg_selected);
            if (!z) {
                VlogUtils.hideViews(this.mEditor, this.mSelector);
                VlogUtils.showViews(this.mPreviewView);
                return;
            }
            VlogUtils.showViews(this.mEditor, this.mSelector);
            VlogUtils.hideViews(this.mPreviewView);
        } else {
            this.mSelector.setImageResource(R$drawable.vlog_caption_default_bg_selected);
            if (!z) {
                VlogUtils.hideViews(this.mEditor, this.mSelector);
                VlogUtils.showViews(this.mPreviewView);
            } else if (this.mData.type.equals("type_single") || this.mData.type.equals("type_double")) {
                VlogUtils.showViews(this.mEditor, this.mSelector);
                VlogUtils.hideViews(this.mPreviewView);
            } else {
                this.mFrame.setBackgroundResource(com.miui.gallery.editor.R$drawable.filter_menu_item_selected);
                VlogUtils.showViews(this.mPreviewView);
                VlogUtils.hideViews(this.mEditor, this.mSelector);
            }
        }
    }

    public void setDownloadViewState(HeaderTailData headerTailData) {
        if (headerTailData.type.equals("type_none") || headerTailData.type.equals("type_custom")) {
            VlogUtils.hideViews(this.mDownloadView);
        } else {
            this.mDownloadView.setStateImage(headerTailData.getDownloadState());
        }
    }
}
