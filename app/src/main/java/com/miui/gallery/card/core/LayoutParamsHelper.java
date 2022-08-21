package com.miui.gallery.card.core;

import android.content.res.Resources;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LayoutParamsHelper {
    public int mItemMaxHeight;
    public int mItemMinHeight;
    public List<Size> mLayoutSizes;

    public LayoutParamsHelper() {
        Resources resources = GalleryApp.sGetAndroidContext().getResources();
        this.mItemMinHeight = resources.getDimensionPixelOffset(R.dimen.story_item_min_height);
        this.mItemMaxHeight = resources.getDimensionPixelOffset(R.dimen.story_item_max_height);
        this.mLayoutSizes = new ArrayList();
    }

    public final float getRatio(Size size) {
        float f;
        float f2 = 1.0f;
        try {
            f = size.mWidth / size.mHeight;
        } catch (Exception e) {
            e = e;
        }
        try {
            if (Float.compare(f, 0.0f) != 0) {
                return f;
            }
            return 1.0f;
        } catch (Exception e2) {
            e = e2;
            f2 = f;
            DefaultLogger.e("LayoutParamsHelper", "getRatio error:" + e);
            return f2;
        }
    }

    public void updateLayoutSizes(List<Size> list, int i, int i2) {
        this.mLayoutSizes.clear();
        if (!BaseMiscUtil.isValid(list)) {
            return;
        }
        int i3 = 0;
        int i4 = 0;
        while (i3 < list.size()) {
            DefaultLogger.d("LayoutParamsHelper", "Row index:" + i4);
            i4++;
            Size size = new Size();
            Size size2 = new Size();
            Size size3 = new Size();
            Size imageSizeGuaranteed = getImageSizeGuaranteed(list, i3, i, i2);
            size.mWidth = i;
            int ratio = (int) (i / getRatio(imageSizeGuaranteed));
            size.mHeight = ratio;
            int i5 = this.mItemMinHeight;
            if (ratio < i5 * 1.0f) {
                size.mHeight = (int) (i5 * 1.0f);
                this.mLayoutSizes.add(size);
                DefaultLogger.d("LayoutParamsHelper", size);
            } else {
                int i6 = i3 + 1;
                if (i6 == list.size()) {
                    int i7 = size.mHeight;
                    int i8 = this.mItemMaxHeight;
                    if (i7 > i8) {
                        size.mHeight = i8;
                    }
                    this.mLayoutSizes.add(size);
                    DefaultLogger.d("LayoutParamsHelper", size);
                    return;
                }
                Size imageSizeGuaranteed2 = getImageSizeGuaranteed(list, i6, i, i2);
                float ratio2 = getRatio(imageSizeGuaranteed);
                float ratio3 = getRatio(imageSizeGuaranteed2);
                float f = ratio2 + ratio3;
                int i9 = (int) ((i - i2) / f);
                float f2 = i9;
                if (f2 < this.mItemMinHeight * 1.1f) {
                    size.mHeight = Math.min(size.mHeight, this.mItemMaxHeight);
                    this.mLayoutSizes.add(size);
                    i3 = i6 - 1;
                    DefaultLogger.d("LayoutParamsHelper", size);
                } else {
                    size.mHeight = i9;
                    size.mWidth = (int) (f2 * ratio2);
                    size2.mHeight = i9;
                    size2.mWidth = (int) (f2 * ratio3);
                    i3 = i6 + 1;
                    if (i3 == list.size()) {
                        int i10 = size.mHeight;
                        int i11 = this.mItemMaxHeight;
                        if (i10 > i11) {
                            size.mHeight = i11;
                            size2.mHeight = i11;
                        }
                        this.mLayoutSizes.add(size);
                        this.mLayoutSizes.add(size2);
                        DefaultLogger.d("LayoutParamsHelper", size);
                        DefaultLogger.d("LayoutParamsHelper", size2);
                        return;
                    }
                    float ratio4 = getRatio(getImageSizeGuaranteed(list, i3, i, i2));
                    int i12 = (int) ((i - (i2 * 2)) / (f + ratio4));
                    float f3 = i12;
                    if (f3 < this.mItemMinHeight * 1.2f) {
                        int i13 = size.mHeight;
                        int i14 = this.mItemMaxHeight;
                        if (i13 > i14) {
                            size.mHeight = i14;
                            size2.mHeight = i14;
                        }
                        this.mLayoutSizes.add(size);
                        this.mLayoutSizes.add(size2);
                        i3--;
                        DefaultLogger.d("LayoutParamsHelper", size);
                        DefaultLogger.d("LayoutParamsHelper", size2);
                    } else {
                        int i15 = this.mItemMaxHeight;
                        if (i12 > i15) {
                            size.mHeight = i15;
                            size2.mHeight = i15;
                            size3.mHeight = i15;
                        } else {
                            size.mHeight = i12;
                            size2.mHeight = i12;
                            size3.mHeight = i12;
                        }
                        size.mWidth = (int) (ratio2 * f3);
                        size2.mWidth = (int) (ratio3 * f3);
                        size3.mWidth = (int) (f3 * ratio4);
                        this.mLayoutSizes.add(size);
                        this.mLayoutSizes.add(size2);
                        this.mLayoutSizes.add(size3);
                        DefaultLogger.d("LayoutParamsHelper", size);
                        DefaultLogger.d("LayoutParamsHelper", size2);
                        DefaultLogger.d("LayoutParamsHelper", size3);
                    }
                }
            }
            i3++;
        }
    }

    public final Size getImageSizeGuaranteed(List<Size> list, int i, int i2, int i3) {
        Size size = (!BaseMiscUtil.isValid(list) || i < 0 || i >= list.size()) ? null : list.get(i);
        if (size == null) {
            size = new Size(0, 0);
        }
        if (size.mWidth <= 0 || size.mHeight <= 0) {
            int i4 = (i2 - i3) / 2;
            size.mWidth = i4;
            size.mHeight = i4;
        }
        return size;
    }

    public android.util.Size getLayoutSize(int i) {
        if (i < 0 || i >= this.mLayoutSizes.size()) {
            return null;
        }
        return new android.util.Size(this.mLayoutSizes.get(i).mWidth, this.mLayoutSizes.get(i).mHeight);
    }

    /* loaded from: classes.dex */
    public static class Size {
        public int mHeight;
        public int mWidth;

        public Size() {
            this(0, 0);
        }

        public Size(int i, int i2) {
            this.mWidth = i;
            this.mHeight = i2;
        }

        public String toString() {
            return "Size:" + this.mWidth + "X" + this.mHeight;
        }
    }
}
