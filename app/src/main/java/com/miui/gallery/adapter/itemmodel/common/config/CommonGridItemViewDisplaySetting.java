package com.miui.gallery.adapter.itemmodel.common.config;

import android.text.TextUtils;
import com.miui.gallery.util.ResourceUtils;

/* loaded from: classes.dex */
public class CommonGridItemViewDisplaySetting {
    public boolean isShowSubTitleView;
    public boolean isShowTitleView;
    public boolean isSubTitleCenterHorizontal;
    public boolean isSubTitleCenterVertical;
    public boolean isTitleCenterHorizontal;
    public boolean isTitleCenterVertical;
    public int mCoverForegroundRes;
    public String mDefaultAlbumName;
    public int mDefaultAlbumNameResource;
    public int mEmptyCoverForegroundRes;
    public int mImageHeight;
    public int mImageWidth;
    public int mSubTitleColor;
    public int mSubTitleSize;
    public int mTitleColor;
    public int mTitleSize;

    public CommonGridItemViewDisplaySetting(DisplayConfig displayConfig) {
        this.mTitleColor = displayConfig.mTitleColor;
        this.mSubTitleColor = displayConfig.mSubTitleColor;
        this.mTitleSize = displayConfig.mTitleSize;
        this.mSubTitleSize = displayConfig.mSubTitleSize;
        this.isShowTitleView = displayConfig.isShowTitleView;
        this.isShowSubTitleView = displayConfig.isShowSubTitleView;
        this.isTitleCenterHorizontal = displayConfig.isTitleCenterHorizontal;
        this.isTitleCenterVertical = displayConfig.isTitleCenterVertical;
        this.isSubTitleCenterVertical = displayConfig.isSubTitleCenterVertical;
        this.isSubTitleCenterHorizontal = displayConfig.isSubTitleCenterHorizontal;
        this.mImageWidth = displayConfig.mImageWidth;
        this.mImageHeight = displayConfig.mImageHeight;
        this.mCoverForegroundRes = displayConfig.mCoverForegroundRes;
        this.mEmptyCoverForegroundRes = displayConfig.mEmptyCoverForegroundRes;
        this.mDefaultAlbumName = displayConfig.mDefaultAlbumName;
        this.mDefaultAlbumNameResource = displayConfig.mDefaultAlbumNameResource;
    }

    public int getTitleColor() {
        return this.mTitleColor;
    }

    public int getSubTitleColor() {
        return this.mSubTitleColor;
    }

    public int getTitleSize() {
        int i = this.mTitleSize;
        if (i == 0) {
            return 0;
        }
        return ResourceUtils.getDimentionPixelsSize(i);
    }

    public int getSubTitleSize() {
        int i = this.mSubTitleSize;
        if (i == 0) {
            return 0;
        }
        return ResourceUtils.getDimentionPixelsSize(i);
    }

    public boolean isShowTitleView() {
        return this.isShowTitleView;
    }

    public boolean isShowSubTitleView() {
        return this.isShowSubTitleView;
    }

    public boolean isTitleCenterHorizontal() {
        return this.isTitleCenterHorizontal;
    }

    public boolean isTitleCenterVertical() {
        return this.isTitleCenterVertical;
    }

    public boolean isSubTitleCenterVertical() {
        return this.isSubTitleCenterVertical;
    }

    public boolean isSubTitleCenterHorizontal() {
        return this.isSubTitleCenterHorizontal;
    }

    public int getImageWidth() {
        return this.mImageWidth;
    }

    public int getImageHeight() {
        return this.mImageHeight;
    }

    public boolean isChangeImageSize() {
        return (this.mImageHeight == 0 && this.mImageWidth == 0) ? false : true;
    }

    public int getForegroundResource() {
        return this.mCoverForegroundRes;
    }

    public int getEmptyCoverForegroundRes() {
        return this.mEmptyCoverForegroundRes;
    }

    public String getDefaultAlbumName() {
        return this.mDefaultAlbumName;
    }

    public int getDefaultAlbumNameResource() {
        return this.mDefaultAlbumNameResource;
    }

    public boolean isHaveDefaultAlbumName() {
        return this.mDefaultAlbumNameResource != 0 || !TextUtils.isEmpty(this.mDefaultAlbumName);
    }

    /* loaded from: classes.dex */
    public static class DisplayConfig {
        public boolean isSubTitleCenterHorizontal;
        public boolean isSubTitleCenterVertical;
        public boolean isTitleCenterHorizontal;
        public boolean isTitleCenterVertical;
        public int mCoverForegroundRes;
        public String mDefaultAlbumName;
        public int mDefaultAlbumNameResource;
        public int mEmptyCoverForegroundRes;
        public int mImageHeight;
        public int mImageWidth;
        public int mSubTitleColor;
        public int mSubTitleSize;
        public int mTitleColor;
        public int mTitleSize;
        public boolean isShowTitleView = true;
        public boolean isShowSubTitleView = true;

        public DisplayConfig titleSize(int i) {
            this.mTitleSize = i;
            return this;
        }

        public DisplayConfig setShowSubTitleView(boolean z) {
            this.isShowSubTitleView = z;
            return this;
        }

        public DisplayConfig setTitleViewCenterHorizontal(boolean z) {
            this.isTitleCenterHorizontal = z;
            return this;
        }

        public DisplayConfig setImageSize(int i, int i2) {
            this.mImageWidth = i;
            this.mImageHeight = i2;
            return this;
        }

        public DisplayConfig setForegroundResource(int i) {
            this.mCoverForegroundRes = i;
            return this;
        }

        public DisplayConfig setDefaultAlbumName(int i) {
            this.mDefaultAlbumNameResource = i;
            return this;
        }

        public CommonGridItemViewDisplaySetting create() {
            return new CommonGridItemViewDisplaySetting(this);
        }
    }
}
