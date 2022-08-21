package com.miui.gallery.util.uil;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.ScreenUtils;

/* loaded from: classes2.dex */
public class SpecialPhotoLoadUtil {
    public static Integer sGifFollowOriginalResolutionMaxSideLength;
    public static Integer sGifMinSideLength;

    public static boolean shouldFollowOriginalResolution(String str, int i, int i2) {
        if (BaseFileMimeUtil.isGifFromMimeType(str) && i > 0 && i2 > 0) {
            if (i > i2) {
                float f = i2;
                if (f < getGifFollowOriginalResolutionMinSideLength() && (getGifFollowOriginalResolutionMinSideLength() / f) * i > getGifFollowOriginalResolutionMaxSideLength()) {
                    return false;
                }
            }
            return ((float) i) < getGifFollowOriginalResolutionMaxSideLength() && ((float) i2) < getGifFollowOriginalResolutionMaxSideLength();
        }
        return false;
    }

    public static float getFollowOriginalResolutionBaseScale(String str, int i, int i2) {
        int screenWidth = ScreenUtils.getScreenWidth();
        int screenHeight = ScreenUtils.getScreenHeight();
        float f = 1.0f;
        if (!shouldFollowOriginalResolution(str, i, i2)) {
            return 1.0f;
        }
        float min = Math.min(i, i2);
        if (min < getGifFollowOriginalResolutionMinSideLength()) {
            f = getGifFollowOriginalResolutionMinSideLength() / min;
        }
        float f2 = i;
        float f3 = i2;
        float f4 = screenWidth;
        float f5 = screenHeight;
        return f * (f2 / f3 > f4 / f5 ? f2 / f4 : f3 / f5);
    }

    public static float getGifFollowOriginalResolutionMinSideLength() {
        if (sGifMinSideLength == null) {
            sGifMinSideLength = Integer.valueOf((int) GalleryApp.sGetAndroidContext().getResources().getDimension(R.dimen.gif_min_side_length));
        }
        return sGifMinSideLength.intValue();
    }

    public static float getGifFollowOriginalResolutionMaxSideLength() {
        if (sGifFollowOriginalResolutionMaxSideLength == null) {
            sGifFollowOriginalResolutionMaxSideLength = Integer.valueOf((int) GalleryApp.sGetAndroidContext().getResources().getDimension(R.dimen.gif_follow_original_resolution_max_side_length));
        }
        return sGifFollowOriginalResolutionMaxSideLength.intValue();
    }
}
