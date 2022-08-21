package com.miui.gallery.vlog.audio;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
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
import com.miui.gallery.vlog.entity.AudioData;
import com.miui.gallery.vlog.home.ImageLoadingProcess;
import com.miui.gallery.vlog.tools.ImageLoaderUtils;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public class AudioHolder extends RecyclerView.ViewHolder {
    public View mConfigIndicator;
    public DownloadView mDownloadView;
    public ImageLoadingProcess mImageLoadingProcess;
    public RoundImageView mPreviewView;
    public RoundImageView mSelector;
    public RoundImageView mSingleVideoSelector;
    public TextView mTitleView;
    public TextView mValueIndicator;

    public AudioHolder(View view) {
        super(view);
        RoundImageView roundImageView;
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mPreviewView = (RoundImageView) view.findViewById(R$id.preview);
        this.mTitleView = (TextView) view.findViewById(R$id.title);
        this.mValueIndicator = (TextView) view.findViewById(R$id.value_indicator);
        this.mConfigIndicator = view.findViewById(R$id.configable_indicator);
        this.mSelector = (RoundImageView) view.findViewById(R$id.selector);
        this.mSingleVideoSelector = (RoundImageView) view.findViewById(R$id.single_video_selector);
        this.mDownloadView = (DownloadView) view.findViewById(R$id.item_download);
        this.mImageLoadingProcess = new ImageLoadingProcess(this.mPreviewView);
        this.mPreviewView.setCorner(roundImageView.getResources().getDimensionPixelSize(R$dimen.vlog_menu_template_item_corner));
        float dimensionPixelSize = view.getResources().getDimensionPixelSize(R$dimen.vlog_common_menu_recyclerview_item_radius);
        this.mSelector.setCorner(dimensionPixelSize);
        this.mPreviewView.setCorner(dimensionPixelSize);
    }

    public void setDownloadViewState(AudioData audioData) {
        if (audioData.isNone() || audioData.isLocal()) {
            VlogUtils.hideViews(this.mDownloadView);
        } else {
            this.mDownloadView.setStateImage(audioData.getDownloadState());
        }
    }

    public void setName(AudioData audioData) {
        if (audioData.isLocal()) {
            this.mTitleView.setText("");
            return;
        }
        int nameResId = audioData.getNameResId();
        if (nameResId != 0) {
            this.mTitleView.setText(nameResId);
            return;
        }
        this.mTitleView.setText(audioData.getLabel());
    }

    public void setIcon(AudioData audioData) {
        String iconUrl = audioData.getIconUrl();
        int iconResId = audioData.getIconResId();
        int bgColor = audioData.getBgColor();
        if (audioData.isNone()) {
            VlogUtils.showViews(this.mSelector);
            this.mPreviewView.setImageResource(R$color.vlog_audio_none_color);
            this.mSelector.setImageResource(iconResId);
            return;
        }
        VlogUtils.hideViews(this.mSelector);
        if (iconResId != 0) {
            this.mPreviewView.setImageResource(iconResId);
        } else if (!TextUtils.isEmpty(iconUrl)) {
            this.mImageLoadingProcess.setBgColor(bgColor);
            Glide.with(this.itemView).mo985asBitmap().mo963load(iconUrl).mo946apply((BaseRequestOptions<?>) ImageLoaderUtils.sRequestOptions).into((RequestBuilder<Bitmap>) new GalleryBitmapImageViewTarget(this.mPreviewView, iconUrl, this.mImageLoadingProcess));
        } else if (bgColor == 0) {
        } else {
            this.mPreviewView.setImageResource(bgColor);
        }
    }

    public void setIcon() {
        VlogUtils.showViews(this.mSelector);
        this.mPreviewView.setImageResource(R$color.vlog_common_highlight_color);
        this.mSelector.setImageResource(R$drawable.ic_audio_menu_edit);
    }

    public void hideTitleView() {
        VlogUtils.hideViews(this.mTitleView);
    }

    public void showTitleView() {
        VlogUtils.showViews(this.mTitleView);
    }

    public void setSingleVideoHolder(AudioData audioData, boolean z) {
        this.mSelector.setVisibility(8);
        if (z) {
            this.mSingleVideoSelector.setVisibility(0);
            this.mSingleVideoSelector.setBackground(this.mSelector.getResources().getDrawable(com.miui.gallery.editor.R$drawable.filter_menu_item_selected));
        } else {
            this.mSingleVideoSelector.setVisibility(8);
        }
        setIcon(audioData);
        setName(audioData);
        setDownloadViewState(audioData);
        if (audioData.isDownloadSuccess()) {
            audioData.setDownloadState(17);
        }
    }
}
