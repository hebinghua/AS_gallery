package com.miui.gallery.widget;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.miui.gallery.R;
import com.miui.gallery.assistant.model.MediaFeatureItem;
import com.miui.gallery.card.CardUtil;
import com.miui.gallery.card.ui.cardlist.CardCoverSizeUtil;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.glide.BindImageHelper;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class CardCoverView extends LinearLayout implements ICardView {
    public ImageView mCoverImageView;
    public CardCoverSizeUtil.CoverItemInfo mCoverItemInfo;
    public String mLocalPath;
    public List<MediaFeatureItem> mMediaFeatureItems;
    public int mOptionHeight;
    public int mOptionWidth;
    public RequestOptions mOptions;
    public boolean mShouldUpdateViews;

    @Override // com.miui.gallery.widget.ICardView
    public /* bridge */ /* synthetic */ int getCurrentIndex() {
        return super.getCurrentIndex();
    }

    @Override // com.miui.gallery.widget.ICardView
    public /* bridge */ /* synthetic */ void setLoadIndex(int i) {
        super.setLoadIndex(i);
    }

    public CardCoverView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CardCoverView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews(context);
    }

    public void initViews(Context context) {
        LinearLayout.inflate(context, R.layout.card_cover_layout, this);
        this.mCoverImageView = (ImageView) findViewById(R.id.cover_image);
        this.mMediaFeatureItems = new ArrayList(5);
    }

    public void setCoverItemInfo(CardCoverSizeUtil.CoverItemInfo coverItemInfo) {
        this.mCoverItemInfo = coverItemInfo;
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, View.MeasureSpec.makeMeasureSpec(this.mCoverItemInfo.getHeight(getContext()), 1073741824));
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateViews();
    }

    public void update(List<MediaFeatureItem> list, RequestOptions requestOptions) {
        this.mMediaFeatureItems.clear();
        this.mMediaFeatureItems.addAll(list);
        this.mOptions = requestOptions;
        this.mShouldUpdateViews = true;
        requestLayout();
    }

    public void setOptionWidthAndHeight(int i, int i2) {
        this.mOptionWidth = i;
        this.mOptionHeight = i2;
    }

    public void updateViews() {
        if (!BaseMiscUtil.isValid(this.mMediaFeatureItems) || !this.mShouldUpdateViews) {
            return;
        }
        this.mShouldUpdateViews = false;
        MediaFeatureItem mediaFeatureItem = this.mMediaFeatureItems.get(0);
        long fileSize = mediaFeatureItem.getFileSize();
        DownloadType downloadType = CardUtil.getDownloadType();
        String imagePath = getImagePath(mediaFeatureItem, downloadType);
        this.mLocalPath = imagePath;
        bindView(this.mCoverImageView, imagePath, getDownloadUri(mediaFeatureItem.getLocalFlag(), mediaFeatureItem.getId()), downloadType, this.mOptions.clone().mo976signature(new ObjectKey(Long.valueOf(fileSize))).mo971override(getOptionWidth(), getOptionHeight()));
    }

    public int getOptionWidth() {
        int i = this.mOptionWidth;
        return i == 0 ? getWidth() : i;
    }

    public int getOptionHeight() {
        int i = this.mOptionHeight;
        return i == 0 ? getHeight() : i;
    }

    public void bindView(ImageView imageView, String str, Uri uri, DownloadType downloadType, RequestOptions requestOptions) {
        BindImageHelper.bindImage(str, uri, downloadType, imageView, requestOptions, false, true);
    }

    public static String getImagePath(MediaFeatureItem mediaFeatureItem, DownloadType downloadType) {
        if (mediaFeatureItem != null) {
            String originPath = mediaFeatureItem.getOriginPath();
            String thumbnailPath = mediaFeatureItem.getThumbnailPath();
            String microThumbnailPath = mediaFeatureItem.getMicroThumbnailPath();
            return downloadType == DownloadType.ORIGIN ? TextUtils.isEmpty(originPath) ? TextUtils.isEmpty(thumbnailPath) ? microThumbnailPath : thumbnailPath : originPath : downloadType == DownloadType.THUMBNAIL ? TextUtils.isEmpty(thumbnailPath) ? TextUtils.isEmpty(originPath) ? microThumbnailPath : originPath : thumbnailPath : TextUtils.isEmpty(microThumbnailPath) ? TextUtils.isEmpty(originPath) ? thumbnailPath : originPath : microThumbnailPath;
        }
        return "";
    }

    public static Uri getDownloadUri(int i, long j) {
        if (i == 0) {
            return CloudUriAdapter.getDownloadUri(j);
        }
        return null;
    }

    public void update(int i, RequestOptions requestOptions) {
        this.mCoverImageView.setImageResource(i);
    }

    public void update(String str, RequestOptions requestOptions) {
        Glide.with(this.mCoverImageView).mo985asBitmap().mo962load(GalleryModel.of(str)).mo946apply((BaseRequestOptions<?>) requestOptions).mo971override(getWidth(), getHeight()).into(this.mCoverImageView);
    }

    public void unbind() {
        BindImageHelper.cancel(this.mCoverImageView);
    }

    @Override // com.miui.gallery.widget.ICardView
    public String getCurrentLocalPath() {
        return this.mLocalPath;
    }
}
