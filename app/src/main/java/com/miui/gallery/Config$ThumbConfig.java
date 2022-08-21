package com.miui.gallery;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Trace;
import android.util.Size;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.resource.bitmap.GaussianBlur;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseFeatureUtil;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.StaticContext;

/* loaded from: classes.dex */
public class Config$ThumbConfig {
    public static final boolean sUseHighQuality;
    public int mScreenHeight;
    public int mScreenWidth;
    public Size sAlbumHeaderThumbTargetSize;
    public Size sLargeTargetSize;
    public Size sMicroHorizontalDocumentTargetSize;
    public int sMicroPanoColumns;
    public Size sMicroPanoTargetSize;
    public Size sMicroRecentTargetSize;
    public Size sMicroScreenshotTargetSize;
    public Size sMicroTargetSize;
    public int sMicroThumbColumnsLand;
    public int sMicroThumbColumnsPortrait;
    public int sMicroThumbHorizontalDocumentColumns;
    public int sMicroThumbRecentColumnsLand;
    public int sMicroThumbRecentColumnsPortrait;
    public int sMicroThumbScreenshotColumnsLand;
    public int sMicroThumbScreenshotColumnsPortrait;
    public int sMicroThumbVideoColumnsLand;
    public int sMicroThumbVideoColumnsPortrait;
    public Size sMicroVideoTargetSize;
    public Size sMiniTargetSize;
    public int sPredictHeadersOneScreen;
    public int sPredictItemsOneScreen;
    public int sPreloadNum;
    public Size sTinyTargetSize;
    public static final Placeholder TINY_THUMB_PLACEHOLDER = new Placeholder();
    public static final Placeholder MINI_THUMB_PLACEHOLDER = new Placeholder();
    public static final Placeholder MICRO_THUMB_PLACEHOLDER = new Placeholder();
    public static final Placeholder LARGE_THUMB_PLACEHOLDER = new Placeholder();

    public static int getBlurRadius() {
        return 3;
    }

    static {
        String[] strArr = {"cepheus", "merlin", "merlinin", "venus", "camellia", "camellian", "zeus", "cupid", "psyche", "matisse"};
        boolean z = false;
        int i = 0;
        while (true) {
            if (i >= 10) {
                break;
            } else if (strArr[i].equalsIgnoreCase(Build.DEVICE)) {
                z = true;
                break;
            } else {
                i++;
            }
        }
        sUseHighQuality = z;
    }

    public Config$ThumbConfig() {
        this.sMicroThumbHorizontalDocumentColumns = 2;
        this.sMicroPanoColumns = 1;
        Trace.beginSection("initThumbConfig");
        this.mScreenWidth = BaseConfig$ScreenConfig.getScreenWidth();
        this.mScreenHeight = BaseConfig$ScreenConfig.getScreenHeight();
        initFixedValues();
        internalUpdateConfig(false);
        Trace.endSection();
    }

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final Config$ThumbConfig INSTANCE = new Config$ThumbConfig();
    }

    public static Config$ThumbConfig get() {
        return SingletonHolder.INSTANCE;
    }

    public final void initFixedValues() {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        int dimensionPixelOffset = resources.getDimensionPixelOffset(R.dimen.micro_thumb_horizontal_spacing);
        int dimensionPixelOffset2 = resources.getDimensionPixelOffset(R.dimen.home_page_margin_horizontal);
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.smallestScreenWidthDp = Math.round(Math.min(this.mScreenWidth, this.mScreenHeight) / resources.getDisplayMetrics().density);
        Resources resources2 = GalleryApp.sGetAndroidContext().createConfigurationContext(configuration).getResources();
        int min = Math.min(resources2.getInteger(R.integer.thumbnail_columns), resources2.getInteger(R.integer.thumbnail_columns_land));
        int min2 = ((Math.min(this.mScreenWidth, this.mScreenHeight) - (dimensionPixelOffset2 * 2)) - (dimensionPixelOffset * (min - 1))) / min;
        this.sMicroTargetSize = new Size(min2, min2);
        this.sMicroRecentTargetSize = new Size(min2, min2);
        this.sPredictItemsOneScreen = Math.round((((Math.max(this.mScreenWidth, this.mScreenHeight) * 0.93f) / min2) * min) + 0.5f);
        this.sPredictHeadersOneScreen = 3;
    }

    public final void internalUpdateConfig(boolean z) {
        if (z) {
            this.mScreenWidth = BaseConfig$ScreenConfig.getScreenWidth();
            this.mScreenHeight = BaseConfig$ScreenConfig.getScreenHeight();
        }
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        this.sMicroThumbColumnsPortrait = resources.getInteger(R.integer.thumbnail_columns);
        this.sMicroThumbColumnsLand = resources.getInteger(R.integer.thumbnail_columns_land);
        int min = Math.min(this.mScreenWidth, this.mScreenHeight) / resources.getInteger(R.integer.album_page_header_columns);
        this.sAlbumHeaderThumbTargetSize = new Size(min, (int) (min / 1.5d));
        this.sMicroThumbRecentColumnsPortrait = resources.getInteger(R.integer.thumbnail_recent_columns);
        this.sMicroThumbRecentColumnsLand = resources.getInteger(R.integer.thumbnail_recent_columns_land);
        this.sMicroThumbScreenshotColumnsPortrait = resources.getInteger(R.integer.thumbnail_screenshot_columns);
        this.sMicroThumbScreenshotColumnsLand = resources.getInteger(R.integer.thumbnail_screenshot_columns_land);
        this.sMicroThumbVideoColumnsPortrait = resources.getInteger(R.integer.thumbnail_video_columns);
        this.sMicroThumbVideoColumnsLand = resources.getInteger(R.integer.thumbnail_video_columns_land);
        this.sMicroScreenshotTargetSize = new Size(resources.getDimensionPixelSize(R.dimen.micro_screenshot_thumbnail_width), resources.getDimensionPixelSize(R.dimen.micro_screenshot_thumbnail_height));
        this.sMicroVideoTargetSize = new Size(resources.getDimensionPixelSize(R.dimen.micro_video_thumbnail_width), resources.getDimensionPixelSize(R.dimen.micro_video_thumbnail_height));
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.micro_pano_thumbnail_width);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.micro_pano_thumbnail_height);
        this.sMicroPanoTargetSize = new Size(dimensionPixelSize, dimensionPixelSize2);
        int dimensionPixelSize3 = resources.getDimensionPixelSize(R.dimen.micro_horizontal_document_thumbnail_width);
        if (BuildUtil.isXiaoMi()) {
            dimensionPixelSize3 = Math.min(this.mScreenWidth, this.mScreenHeight) / this.sMicroThumbHorizontalDocumentColumns;
            dimensionPixelSize2 = (resources.getInteger(R.integer.micro_horizontal_document_item_height_scale) * dimensionPixelSize3) / resources.getInteger(R.integer.micro_horizontal_document_item_width_scale);
        }
        this.sMicroHorizontalDocumentTargetSize = new Size(dimensionPixelSize3, dimensionPixelSize2);
        this.sTinyTargetSize = new Size(resources.getDimensionPixelSize(R.dimen.tiny_thumbnail_size), resources.getDimensionPixelSize(R.dimen.tiny_thumbnail_size));
        this.sMiniTargetSize = new Size(resources.getDimensionPixelSize(R.dimen.mini_thumbnail_size), resources.getDimensionPixelSize(R.dimen.mini_thumbnail_size));
        this.sLargeTargetSize = new Size(resources.getDimensionPixelSize(R.dimen.large_thumbnail_size), resources.getDimensionPixelSize(R.dimen.large_thumbnail_size));
        this.sPreloadNum = BaseBuildUtil.isLowRamDevice() ? 0 : 24;
    }

    public void updateConfig() {
        internalUpdateConfig(true);
    }

    public static Bitmap.Config getMicroThumbConfig() {
        return sUseHighQuality ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
    }

    public static Bitmap.Config getThumbnailConfig() {
        return Bitmap.Config.RGB_565;
    }

    public static RequestOptions applyProcessingOptions(RequestOptions requestOptions) {
        if (!BaseFeatureUtil.isDisableFastBlur()) {
            if (requestOptions.isLocked()) {
                requestOptions = requestOptions.clone();
            }
            return requestOptions.mo958format(DecodeFormat.PREFER_ARGB_8888).mo980transform(new GaussianBlur(getBlurRadius()));
        }
        return requestOptions;
    }

    public static RequestOptions markAsTemp(RequestOptions requestOptions) {
        return GlideOptions.markTempOf().mo946apply((BaseRequestOptions<?>) requestOptions);
    }

    public static Drawable getTinyThumbPlaceholder() {
        return TINY_THUMB_PLACEHOLDER.get(get().sTinyTargetSize);
    }

    public static Drawable getMiniThumbPlaceholder() {
        return MINI_THUMB_PLACEHOLDER.get(get().sMiniTargetSize);
    }

    public static Drawable getMicroThumbPlaceholder() {
        return MICRO_THUMB_PLACEHOLDER.get(get().sMicroTargetSize);
    }

    public static Drawable getLargeThumbPlaceholder() {
        return LARGE_THUMB_PLACEHOLDER.get(get().sLargeTargetSize);
    }

    /* loaded from: classes.dex */
    public static class Placeholder extends LazyValue<Size, Drawable> {
        public Placeholder() {
        }

        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Drawable mo1272onInit(Size size) {
            Drawable drawable = StaticContext.sGetAndroidContext().getDrawable(R.drawable.image_default_cover);
            Bitmap createBitmap = Bitmap.createBitmap(size.getWidth(), size.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, size.getWidth(), size.getHeight());
            drawable.draw(canvas);
            return new BitmapDrawable(StaticContext.sGetAndroidContext().getResources(), createBitmap) { // from class: com.miui.gallery.Config$PlaceholderDrawable
            };
        }
    }
}
