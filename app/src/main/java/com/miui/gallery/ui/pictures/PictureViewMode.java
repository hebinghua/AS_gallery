package com.miui.gallery.ui.pictures;

import android.util.Size;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.DeviceCharacteristics;
import com.miui.gallery.util.LazyValue;
import java.util.Arrays;
import java.util.function.Supplier;
import miuix.core.util.SystemProperties;

/* loaded from: classes2.dex */
public final class PictureViewMode implements Comparable<PictureViewMode> {
    public static final PictureViewMode[] ALL_VALUES;
    public static final LazyValue<Void, Boolean> FOLD_DEVICE_DEFAULT_VALUE;
    public static final PictureViewMode LARGE_THUMB;
    public static final PictureViewMode MICRO_THUMB;
    public static final PictureViewMode MINI_THUMB;
    public static final PictureViewMode[] SUPPORTED_VALUES;
    public static final PictureViewMode TINY_THUMB;
    public static final PictureViewMode[] sCurrentValues;
    public final int mAggregatedLines;
    public final Supplier<Size> mImageSizeSupplier;
    public final boolean mIsSquare;
    public final String mName;
    public final int mOrdinal;
    public final Supplier<GlideOptions> mRequestOptionsSupplier;
    public int mSpacing = -1;
    public final int mSpacingResId;
    public final int mSpanResId;
    public int mSupportedZoomFlag;

    static {
        PictureViewMode pictureViewMode = new PictureViewMode(0, "LARGE_THUMB", R.integer.thumb_large_columns, R.dimen.micro_thumb_horizontal_spacing_day_loose, true, PictureViewMode$$ExternalSyntheticLambda5.INSTANCE, PictureViewMode$$ExternalSyntheticLambda0.INSTANCE, 1);
        LARGE_THUMB = pictureViewMode;
        PictureViewMode pictureViewMode2 = new PictureViewMode(1, "MICRO_THUMB", R.integer.thumb_micro_columns, R.dimen.micro_thumb_horizontal_spacing_day_compact, true, PictureViewMode$$ExternalSyntheticLambda7.INSTANCE, PictureViewMode$$ExternalSyntheticLambda1.INSTANCE, 1);
        MICRO_THUMB = pictureViewMode2;
        PictureViewMode pictureViewMode3 = new PictureViewMode(2, "MINI_THUMB", R.integer.thumb_mini_columns, R.dimen.micro_thumb_horizontal_spacing_month, true, PictureViewMode$$ExternalSyntheticLambda4.INSTANCE, PictureViewMode$$ExternalSyntheticLambda2.INSTANCE, 3);
        MINI_THUMB = pictureViewMode3;
        PictureViewMode pictureViewMode4 = new PictureViewMode(3, "TINY_THUMB", R.integer.thumb_tiny_columns, R.dimen.micro_thumb_horizontal_spacing_year, true, PictureViewMode$$ExternalSyntheticLambda6.INSTANCE, PictureViewMode$$ExternalSyntheticLambda3.INSTANCE, 5);
        TINY_THUMB = pictureViewMode4;
        PictureViewMode[] pictureViewModeArr = {pictureViewMode, pictureViewMode2, pictureViewMode3, pictureViewMode4};
        ALL_VALUES = pictureViewModeArr;
        if (DeviceCharacteristics.isNonLowEndDevice() || !BaseBuildUtil.isLowRamDevice()) {
            SUPPORTED_VALUES = pictureViewModeArr;
            pictureViewMode.mSupportedZoomFlag = 2;
            pictureViewMode2.mSupportedZoomFlag = 3;
            pictureViewMode3.mSupportedZoomFlag = 3;
            pictureViewMode4.mSupportedZoomFlag = 1;
        } else {
            SUPPORTED_VALUES = new PictureViewMode[]{pictureViewMode, pictureViewMode2, pictureViewMode3};
            pictureViewMode.mSupportedZoomFlag = 2;
            pictureViewMode2.mSupportedZoomFlag = 3;
            pictureViewMode3.mSupportedZoomFlag = 1;
        }
        sCurrentValues = SUPPORTED_VALUES;
        FOLD_DEVICE_DEFAULT_VALUE = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.ui.pictures.PictureViewMode.1
            @Override // com.miui.gallery.util.LazyValue
            /* renamed from: onInit  reason: avoid collision after fix types in other method */
            public Boolean mo1272onInit(Void r3) {
                boolean z = false;
                if (SystemProperties.getInt("persist.sys.muiltdisplay_type", 0) == 2) {
                    z = true;
                }
                return Boolean.valueOf(z);
            }
        };
    }

    public static /* synthetic */ Size lambda$static$0() {
        return Config$ThumbConfig.get().sLargeTargetSize;
    }

    public static /* synthetic */ Size lambda$static$1() {
        return Config$ThumbConfig.get().sMicroTargetSize;
    }

    public static /* synthetic */ Size lambda$static$2() {
        return Config$ThumbConfig.get().sMiniTargetSize;
    }

    public static /* synthetic */ Size lambda$static$3() {
        return Config$ThumbConfig.get().sTinyTargetSize;
    }

    public PictureViewMode(int i, String str, int i2, int i3, boolean z, Supplier<Size> supplier, Supplier<GlideOptions> supplier2, int i4) {
        this.mOrdinal = i;
        this.mName = str;
        this.mSpanResId = i2;
        this.mSpacingResId = i3;
        this.mIsSquare = z;
        this.mImageSizeSupplier = supplier;
        this.mRequestOptionsSupplier = supplier2;
        this.mAggregatedLines = i4;
    }

    public static PictureViewMode zoomIn(PictureViewMode pictureViewMode) {
        int i;
        PictureViewMode[] pictureViewModeArr = sCurrentValues;
        int binarySearch = Arrays.binarySearch(pictureViewModeArr, pictureViewMode);
        if (binarySearch >= 0 && binarySearch - 1 >= 0) {
            return pictureViewModeArr[i];
        }
        return null;
    }

    public static PictureViewMode zoomOut(PictureViewMode pictureViewMode) {
        int i;
        PictureViewMode[] pictureViewModeArr = sCurrentValues;
        int binarySearch = Arrays.binarySearch(pictureViewModeArr, pictureViewMode);
        if (binarySearch >= 0 && (i = binarySearch + 1) < pictureViewModeArr.length) {
            return pictureViewModeArr[i];
        }
        return null;
    }

    public int ordinal() {
        return this.mOrdinal;
    }

    public String name() {
        return this.mName;
    }

    public boolean isAggregated() {
        return this.mAggregatedLines > 1;
    }

    public int getSpan() {
        return GalleryApp.sGetAndroidContext().getResources().getInteger(this.mSpanResId);
    }

    public int getSpacing() {
        if (this.mSpacing < 0) {
            this.mSpacing = GalleryApp.sGetAndroidContext().getResources().getDimensionPixelSize(this.mSpacingResId);
        }
        return this.mSpacing;
    }

    public Size getImageSize() {
        return this.mImageSizeSupplier.get();
    }

    public GlideOptions getRequestOptions() {
        return this.mRequestOptionsSupplier.get();
    }

    public int getSupportedZoomFlag() {
        return this.mSupportedZoomFlag;
    }

    public int getAggregatedLines() {
        return this.mAggregatedLines;
    }

    @Override // java.lang.Comparable
    public int compareTo(PictureViewMode pictureViewMode) {
        return Integer.compare(this.mOrdinal, pictureViewMode.mOrdinal);
    }

    public static PictureViewMode getViewModeByOrdinal(int i) {
        PictureViewMode[] pictureViewModeArr;
        for (PictureViewMode pictureViewMode : ALL_VALUES) {
            if (pictureViewMode.ordinal() == i) {
                return pictureViewMode;
            }
        }
        return null;
    }

    public static boolean isLargeDevice() {
        return FOLD_DEVICE_DEFAULT_VALUE.get(null).booleanValue() || BaseBuildUtil.isPad();
    }

    public static boolean isYearMode(PictureViewMode pictureViewMode) {
        return pictureViewMode == TINY_THUMB;
    }

    public static boolean isMonthMode(PictureViewMode pictureViewMode) {
        if (isLargeDevice()) {
            if (pictureViewMode == MINI_THUMB || pictureViewMode == MICRO_THUMB) {
                return true;
            }
        } else if (pictureViewMode == MINI_THUMB) {
            return true;
        }
        return false;
    }

    public static boolean isDayMode(PictureViewMode pictureViewMode) {
        if (isLargeDevice()) {
            if (pictureViewMode != LARGE_THUMB) {
                return false;
            }
        } else if (pictureViewMode != LARGE_THUMB && pictureViewMode != MICRO_THUMB) {
            return false;
        }
        return true;
    }

    public int getClusterKey() {
        if (isYearMode(this)) {
            return 4;
        }
        return isMonthMode(this) ? 2 : 1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return obj != null && PictureViewMode.class == obj.getClass() && this.mOrdinal == ((PictureViewMode) obj).mOrdinal;
    }

    public int hashCode() {
        return this.mOrdinal;
    }

    public String toString() {
        return "PictureViewMode{, mName='" + this.mName + CoreConstants.SINGLE_QUOTE_CHAR + ", mIsAggregated=" + isAggregated() + ", mAggregatedLines=" + this.mAggregatedLines + ", mSupportedZoomFlag=" + this.mSupportedZoomFlag + '}';
    }
}
