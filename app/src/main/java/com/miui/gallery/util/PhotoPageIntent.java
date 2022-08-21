package com.miui.gallery.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.RegionConfig;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.util.photoview.PhotoPageDataCache;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class PhotoPageIntent {
    public final FragmentActivity activity;
    public final ViewGroup adapterView;
    public final long albumId;
    public final String albumName;
    public final Class<?> clazz;
    public final int count;
    public final EnterTypeUtils.EnterType enterType;
    public final long idForPicker;
    public final ImageLoadParams imageLoadParams;
    public final int initPosition;
    public final boolean isPreview;
    public final boolean needConfirmPassword;
    public final long operationMask;
    public final String orderBy;
    public final String recommendFaceId;
    public final String selection;
    public final String[] selectionArgs;
    public final ArrayList<ItemViewInfo> specialItemViewInfos;
    public final boolean unfoldBurst;
    public final Uri uri;

    public PhotoPageIntent(Builder builder) {
        this.activity = builder.activity;
        this.clazz = builder.clazz;
        this.adapterView = builder.adapterView;
        this.uri = builder.uri;
        this.initPosition = builder.initPosition;
        this.count = builder.count;
        this.selection = builder.selection;
        this.selectionArgs = builder.selectionArgs;
        this.orderBy = builder.orderBy;
        this.imageLoadParams = builder.imageLoadParams;
        this.albumId = builder.albumId;
        this.albumName = builder.albumName;
        this.operationMask = builder.operationMask;
        this.recommendFaceId = builder.recommendFaceId;
        this.specialItemViewInfos = builder.specialItemViewInfos;
        this.isPreview = builder.isPreview;
        this.unfoldBurst = builder.unfoldBurst;
        this.needConfirmPassword = builder.needConfirmPassword;
        this.enterType = builder.enterType;
        this.idForPicker = builder.idForPicker;
    }

    public void gotoPhotoPage() {
        TimeMonitor.createNewTimeMonitor("403.11.0.1.13760");
        ThreadManager.getPreviewPool().submit(new ThreadPool.Job<Object>() { // from class: com.miui.gallery.util.PhotoPageIntent.1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public Object mo1807run(ThreadPool.JobContext jobContext) {
                Intent intent = new Intent(PhotoPageIntent.this.activity, PhotoPageIntent.this.clazz);
                intent.putExtra("extra_photo_page_from", PhotoPageIntent.this.activity.toString());
                PhotoPageIntent.this.activity.startActivity(intent);
                return null;
            }
        });
        Bundle bundle = new Bundle();
        bundle.putParcelable("photo_data", this.uri);
        bundle.putInt("photo_init_position", this.initPosition);
        bundle.putParcelable("photo_page_enter_type", this.enterType);
        bundle.putInt("photo_count", this.count);
        if (!TextUtils.isEmpty(this.selection)) {
            bundle.putString("photo_selection", this.selection);
        }
        String[] strArr = this.selectionArgs;
        if (strArr != null) {
            bundle.putStringArray("photo_selection_args", strArr);
        }
        if (!TextUtils.isEmpty(this.orderBy)) {
            bundle.putString("photo_order_by", this.orderBy);
        }
        ImageLoadParams imageLoadParams = this.imageLoadParams;
        if (imageLoadParams != null) {
            bundle.putParcelable("photo_transition_data", imageLoadParams);
        }
        bundle.putBoolean("from_gallery", true);
        if (this.adapterView != null) {
            PhotoPageDataCache.getInstance().setItemViewParent(this.adapterView, this.activity.toString());
        } else {
            ArrayList<ItemViewInfo> arrayList = this.specialItemViewInfos;
            if (arrayList != null && arrayList.size() > 0) {
                PhotoPageDataCache.getInstance().setAppointedItemViewInfos(this.specialItemViewInfos);
            }
        }
        if (this.idForPicker != -1) {
            PhotoPageDataCache.getInstance().setPickerEnterItemId(this.idForPicker);
        }
        if (!TextUtils.isEmpty(this.albumName)) {
            bundle.putString("album_name", this.albumName);
        }
        long j = this.albumId;
        if (j == 2147483640 || j == -1000) {
            bundle.putBoolean("photodetail_is_photo_datetime_editable", false);
        }
        Uri uri = this.uri;
        if (uri != null) {
            if (uri.getEncodedPath().contains("person")) {
                bundle.putBoolean("photodetail_is_photo_datetime_editable", false);
            } else if (this.uri.getEncodedPath().contains("searchResultPhoto")) {
                bundle.putBoolean("photodetail_is_photo_datetime_editable", false);
            } else if (this.uri.getEncodedPath().contains("share_album_media")) {
                bundle.putBoolean("photodetail_is_photo_datetime_editable", false);
                bundle.putBoolean("photodetail_is_photo_renamable", false);
            }
        }
        bundle.putLong("album_id", this.albumId);
        bundle.putLong("support_operation_mask", this.operationMask);
        bundle.putString("recommend_face_id", this.recommendFaceId);
        bundle.putBoolean("photo_enter_transit", true);
        bundle.putBoolean("photo_preview_mode", this.isPreview);
        bundle.putBoolean("need_confirm_psw", this.needConfirmPassword);
        if (this.unfoldBurst) {
            bundle.putBoolean("unford_burst", true);
        }
        bundle.putString("extra_photo_page_from", this.activity.toString());
        PhotoPageDataCache.getInstance().setArguments(bundle, this.activity.toString());
        ImageLoadParams imageLoadParams2 = this.imageLoadParams;
        if (imageLoadParams2 == null || imageLoadParams2.getTargetSize() == null) {
            return;
        }
        PhotoPageDataCache.preload(this.activity, new ImageLoadParams.Builder().cloneFrom(this.imageLoadParams).setRequestOptions((RequestOptions) GlideOptions.microThumbOf(this.imageLoadParams.getFileLength()).mo971override(this.imageLoadParams.getTargetSize().getWidth(), this.imageLoadParams.getTargetSize().getHeight()).secretKey(this.imageLoadParams.getSecretKey()).decodeRegion(RegionConfig.of(this.imageLoadParams.getRegionRectF()))).build());
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public final FragmentActivity activity;
        public ViewGroup adapterView;
        public String albumName;
        public final Class<?> clazz;
        public ImageLoadParams imageLoadParams;
        public int initPosition;
        public boolean isPreview;
        public boolean needConfirmPassword;
        public String orderBy;
        public String recommendFaceId;
        public String selection;
        public String[] selectionArgs;
        public ArrayList<ItemViewInfo> specialItemViewInfos;
        public boolean unfoldBurst;
        public Uri uri;
        public int count = 1;
        public long albumId = -1;
        public long operationMask = -1;
        public EnterTypeUtils.EnterType enterType = EnterTypeUtils.EnterType.FROM_NO_CARE;
        public long idForPicker = -1;

        public Builder(FragmentActivity fragmentActivity, Class<?> cls) {
            this.activity = fragmentActivity;
            this.clazz = cls;
        }

        public Builder(Fragment fragment, Class<?> cls) {
            this.activity = fragment.getActivity();
            this.clazz = cls;
        }

        public Builder setAdapterView(ViewGroup viewGroup) {
            this.adapterView = viewGroup;
            return this;
        }

        public Builder setUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public Builder setInitPosition(int i) {
            this.initPosition = i;
            return this;
        }

        public Builder setCount(int i) {
            this.count = i;
            return this;
        }

        public Builder setSelection(String str) {
            this.selection = str;
            return this;
        }

        public Builder setSelectionArgs(String[] strArr) {
            this.selectionArgs = strArr;
            return this;
        }

        public Builder setOrderBy(String str) {
            this.orderBy = str;
            return this;
        }

        public Builder setImageLoadParams(ImageLoadParams imageLoadParams) {
            this.imageLoadParams = imageLoadParams;
            return this;
        }

        public Builder setAlbumId(long j) {
            this.albumId = j;
            return this;
        }

        public Builder setAlbumName(String str) {
            this.albumName = str;
            return this;
        }

        public Builder setOperationMask(long j) {
            this.operationMask = j;
            return this;
        }

        public Builder setRecommendFaceId(String str) {
            this.recommendFaceId = str;
            return this;
        }

        public Builder setSpecialItemViewInfos(ArrayList<ItemViewInfo> arrayList) {
            this.specialItemViewInfos = arrayList;
            return this;
        }

        public Builder setPreview(boolean z) {
            this.isPreview = z;
            return this;
        }

        public Builder setUnfoldBurst(boolean z) {
            this.unfoldBurst = z;
            return this;
        }

        public Builder setNeedConfirmPassword(boolean z) {
            this.needConfirmPassword = z;
            return this;
        }

        public Builder setEnterType(EnterTypeUtils.EnterType enterType) {
            this.enterType = enterType;
            return this;
        }

        public Builder setIdForPicker(long j) {
            this.idForPicker = j;
            return this;
        }

        public PhotoPageIntent build() {
            return new PhotoPageIntent(this);
        }
    }
}
