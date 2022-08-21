package com.miui.gallery.vlog.template;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.editor.ui.view.DownloadView;
import com.miui.gallery.editor.ui.view.RoundImageView;
import com.miui.gallery.glide.request.target.GalleryBitmapImageViewTarget;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$drawable;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.home.ImageLoadingProcess;
import com.miui.gallery.vlog.tools.ImageLoaderUtils;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public class TemplateHolder extends RecyclerView.ViewHolder {
    public DownloadView mDownloadView;
    public RoundImageView mIcon;
    public ImageLoadingProcess mImageLoadingProcess;
    public ImageView mNone;
    public View mSelector;
    public TextView mTitle;

    public TemplateHolder(View view) {
        super(view);
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mNone = (ImageView) view.findViewById(R$id.none);
        this.mIcon = (RoundImageView) view.findViewById(R$id.preview);
        this.mTitle = (TextView) view.findViewById(R$id.title);
        this.mSelector = view.findViewById(R$id.selector);
        this.mImageLoadingProcess = new ImageLoadingProcess(this.mIcon);
        this.mDownloadView = (DownloadView) view.findViewById(R$id.item_download);
        this.mIcon.setCorner(view.getResources().getDimension(R$dimen.vlog_template_image_icon_corner));
    }

    public void setDownloadViewState(TemplateResource templateResource) {
        if (templateResource.isNone()) {
            VlogUtils.hideViews(this.mDownloadView);
        } else {
            this.mDownloadView.setStateImage(templateResource.getDownloadState());
        }
    }

    public void setName(TemplateResource templateResource) {
        if (templateResource.isNone()) {
            this.mTitle.setText("");
            return;
        }
        int nameResId = templateResource.getNameResId();
        if (nameResId != 0) {
            this.mTitle.setText(nameResId);
            return;
        }
        this.mTitle.setText(templateResource.getLabel());
    }

    public void setTitleViewState(TemplateResource templateResource) {
        if (templateResource.isNone()) {
            VlogUtils.hideViews(this.mTitle);
        } else {
            VlogUtils.showViews(this.mTitle);
        }
    }

    public void setIcon(TemplateResource templateResource, boolean z) {
        int i = 4;
        int i2 = 0;
        this.mSelector.setVisibility(z ? 0 : 4);
        String iconUrl = templateResource.getIconUrl();
        int iconResId = templateResource.getIconResId();
        int bgColor = templateResource.getBgColor();
        ImageView imageView = this.mNone;
        if (templateResource.isNone()) {
            i = 0;
        }
        imageView.setVisibility(i);
        if (templateResource.isNone()) {
            this.mIcon.setImageResource(0);
            RoundImageView roundImageView = this.mIcon;
            roundImageView.setBackground(roundImageView.getResources().getDrawable(R$drawable.vlog_template_default_bg_selected));
        } else if (iconResId != 0) {
            RoundImageView roundImageView2 = this.mIcon;
            roundImageView2.setBackground(roundImageView2.getResources().getDrawable(R$drawable.vlog_template_default_bg_selected));
            RoundImageView roundImageView3 = this.mIcon;
            if (!templateResource.isNone()) {
                i2 = iconResId;
            }
            roundImageView3.setImageResource(i2);
        } else if (!TextUtils.isEmpty(iconUrl)) {
            this.mImageLoadingProcess.setBgColor(bgColor);
            Glide.with(this.itemView).mo985asBitmap().mo963load(iconUrl).mo946apply((BaseRequestOptions<?>) ImageLoaderUtils.sRequestOptions).into((RequestBuilder<Bitmap>) new GalleryBitmapImageViewTarget(this.mIcon, iconUrl, this.mImageLoadingProcess));
        } else if (bgColor == 0) {
        } else {
            this.mIcon.setImageResource(bgColor);
        }
    }
}
