package com.miui.gallery;

import android.graphics.Bitmap;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.DeviceCharacteristics;

/* loaded from: classes.dex */
public class Config$TileConfig {
    public static final int REGION_DECODER_PARALLELISM;
    public static final int TILE_SIZE;
    public static final int TILE_SIZE_UPPER_LIMIT;
    public static final int sCacheCount;

    static {
        int i = BaseBuildUtil.isLowRamDevice() ? 360 : 1080;
        TILE_SIZE = i;
        TILE_SIZE_UPPER_LIMIT = (int) (i * 1.2f);
        sCacheCount = BaseBuildUtil.isLowRamDevice() ? 0 : 8;
        if (DeviceCharacteristics.isHighEndDevice() || DeviceCharacteristics.isMiddleEndDevice()) {
            REGION_DECODER_PARALLELISM = 2;
        } else {
            REGION_DECODER_PARALLELISM = 1;
        }
    }

    public static int getMaxCacheCount() {
        return sCacheCount;
    }

    public static Bitmap.Config getBitmapConfig() {
        return Bitmap.Config.ARGB_8888;
    }

    public static boolean needUseTile(int i, int i2) {
        return !BaseBuildUtil.isLowRamDevice() || ((float) Math.max(i, i2)) > ((float) Math.min(BaseConfig$ScreenConfig.getScreenWidth(), BaseConfig$ScreenConfig.getScreenHeight())) * 1.0f;
    }
}
