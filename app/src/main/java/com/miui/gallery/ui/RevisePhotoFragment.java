package com.miui.gallery.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.model.BaseDataItem;

/* loaded from: classes2.dex */
public class RevisePhotoFragment extends BaseFragment {
    public BaseDataItem mBaseDataItem;
    public PhotoView mPhotoView;

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "RevisePhotoFragment";
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public int getThemeRes() {
        return 2131952018;
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mBaseDataItem = (BaseDataItem) getArguments().getSerializable("key_revise_photo_item");
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        BaseDataItem baseDataItem = this.mBaseDataItem;
        if (baseDataItem == null || !baseDataItem.isSecret()) {
            return;
        }
        this.mActivity.getWindow().addFlags(8192);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.revise_photo_fragment, viewGroup, false);
        PhotoView photoView = (PhotoView) inflate.findViewById(R.id.photoview);
        this.mPhotoView = photoView;
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        this.mPhotoView.setUseFillWidthMode(true);
        this.mPhotoView.setRotatable(false);
        this.mPhotoView.setDisableDragDownOut(true);
        displayImage(this.mBaseDataItem);
        return inflate;
    }

    public final boolean isPathValid(String str) {
        return !TextUtils.isEmpty(str);
    }

    public final RequestOptions getRequestOptions(BaseDataItem baseDataItem, boolean z) {
        GlideOptions mo973placeholder;
        if (!z) {
            mo973placeholder = GlideOptions.bigPhotoOf(baseDataItem.getSize());
        } else {
            mo973placeholder = GlideOptions.microThumbOf(baseDataItem.getSize()).mo973placeholder((Drawable) null);
        }
        Size imageSize = getImageSize(z);
        return mo973placeholder.mo971override(imageSize.getWidth(), imageSize.getHeight()).secretKey(baseDataItem.getSecretKey());
    }

    public final Size getImageSize(boolean z) {
        if (z) {
            return Config$ThumbConfig.get().sMicroTargetSize;
        }
        int max = Math.max(BaseConfig$ScreenConfig.getScreenWidth(), BaseConfig$ScreenConfig.getScreenHeight());
        return new Size(max, max);
    }

    public final void displayImage(BaseDataItem baseDataItem) {
        boolean z;
        if (baseDataItem == null) {
            return;
        }
        String originalPath = baseDataItem.getOriginalPath();
        if (!isPathValid(originalPath)) {
            originalPath = baseDataItem.getThumnailPath();
        }
        if (!isPathValid(originalPath)) {
            originalPath = baseDataItem.getMicroPath();
            z = true;
        } else {
            z = false;
        }
        if (TextUtils.isEmpty(originalPath)) {
            return;
        }
        if (!z && !isPathValid(originalPath)) {
            return;
        }
        Glide.with(this).mo985asBitmap().mo962load(GalleryModel.of(originalPath)).mo946apply((BaseRequestOptions<?>) getRequestOptions(baseDataItem, z)).into(this.mPhotoView);
    }
}
