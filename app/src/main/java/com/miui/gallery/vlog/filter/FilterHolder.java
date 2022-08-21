package com.miui.gallery.vlog.filter;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.BaseRequestOptions;
import com.miui.gallery.editor.R$drawable;
import com.miui.gallery.editor.R$raw;
import com.miui.gallery.editor.ui.view.DownloadView;
import com.miui.gallery.editor.ui.view.RoundImageView;
import com.miui.gallery.glide.request.target.GalleryBitmapImageViewTarget;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.entity.FilterData;
import com.miui.gallery.vlog.home.ImageLoadingProcess;
import com.miui.gallery.vlog.tools.ImageLoaderUtils;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public class FilterHolder extends RecyclerView.ViewHolder {
    public ColorDrawable mColorDrawable;
    public LottieAnimationView mConfigIndicator;
    public DownloadView mDownloadView;
    public ImageLoadingProcess mImageLoadingProcess;
    public RoundImageView mPreviewView;
    public boolean mSelected;
    public RoundImageView mSelector;
    public TextView mTitleView;
    public TextView mValueIndicator;

    public static /* synthetic */ void $r8$lambda$MiEjpWi1tjGuXUKBsAXcTZzyKuQ(FilterHolder filterHolder) {
        filterHolder.lambda$setConfigIndicator$0();
    }

    public FilterHolder(View view) {
        super(view);
        this.mColorDrawable = (ColorDrawable) new ColorDrawable().mutate();
        FolmeUtil.setDefaultTouchAnim(view, null, true);
        this.mPreviewView = (RoundImageView) view.findViewById(R$id.preview);
        this.mTitleView = (TextView) view.findViewById(R$id.title);
        this.mValueIndicator = (TextView) view.findViewById(R$id.value_indicator);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R$id.configable_indicator);
        this.mConfigIndicator = lottieAnimationView;
        lottieAnimationView.setAnimation(R$raw.lottie_filter_configable_indicator);
        this.mSelector = (RoundImageView) view.findViewById(R$id.selector);
        this.mDownloadView = (DownloadView) view.findViewById(R$id.item_download);
        this.mDownloadView.setBackground(view.getResources().getDrawable(R$drawable.filter_menu_item_download_bg));
        this.mImageLoadingProcess = new ImageLoadingProcess(this.mPreviewView);
        float dimensionPixelSize = view.getResources().getDimensionPixelSize(R$dimen.vlog_common_menu_recyclerview_item_radius);
        this.mSelector.setCorner(dimensionPixelSize);
        this.mPreviewView.setCorner(dimensionPixelSize);
    }

    public void setDownloadViewState(FilterData filterData) {
        if (filterData.isNone()) {
            VlogUtils.hideViews(this.mDownloadView);
        } else {
            this.mDownloadView.setStateImage(filterData.getDownloadState());
        }
    }

    public void setName(FilterData filterData) {
        int nameResId = filterData.getNameResId();
        if (nameResId != 0) {
            this.mTitleView.setText(nameResId);
            return;
        }
        this.mTitleView.setText(filterData.getLabel());
    }

    public void setIcon(FilterData filterData) {
        String iconUrl = filterData.getIconUrl();
        int iconResId = filterData.getIconResId();
        int bgColor = filterData.getBgColor();
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

    public void setState(FilterData filterData, boolean z, boolean z2) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mSelector.getLayoutParams();
        this.mSelected = z;
        if (z) {
            this.mSelector.setVisibility(0);
        } else {
            this.mSelector.setVisibility(8);
        }
        int[] rules = layoutParams.getRules();
        if (!z) {
            VlogUtils.hideViews(this.mValueIndicator, this.mConfigIndicator);
            int i = rules[6];
            int i2 = R$id.title;
            if (i == i2) {
                return;
            }
            layoutParams.removeRule(10);
            layoutParams.addRule(6, i2);
            return;
        }
        if (!z2) {
            VlogUtils.hideViews(this.mValueIndicator);
        } else {
            VlogUtils.showViews(this.mValueIndicator);
        }
        if (rules[10] == -1) {
            return;
        }
        layoutParams.addRule(10, -1);
        layoutParams.removeRule(6);
    }

    public /* synthetic */ void lambda$setConfigIndicator$0() {
        this.mConfigIndicator.playAnimation();
    }

    public void setConfigIndicator() {
        this.mConfigIndicator.post(new Runnable() { // from class: com.miui.gallery.vlog.filter.FilterHolder$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                FilterHolder.$r8$lambda$MiEjpWi1tjGuXUKBsAXcTZzyKuQ(FilterHolder.this);
            }
        });
    }

    public void updateIndicator(int i) {
        this.mValueIndicator.setText(String.valueOf(i));
    }
}
