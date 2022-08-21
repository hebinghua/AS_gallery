package com.miui.gallery.ui;

import com.miui.gallery.util.IntentUtil;

/* loaded from: classes2.dex */
public class ProduceRangeContract {
    public static final int COLLAGE_RANGE_MAX;
    public static final int[] PRODUCE_COLLAGE_RANGE;
    public static final int[] PRODUCE_MAGIC_RANGE = {1, 1};
    public static final int[] PRODUCE_ID_PHOTO_RANGE = {1, 1};
    public static final int[] PRODUCE_PHOTO_MOVIE_RANGE = {3, 20};
    public static final int[] PRODUCE_VIDE_POST_RANGE = {1, 1};
    public static final int[] PRODUCE_VLOG_RANGE = {1, 500};
    public static final int[] PRODUCE_ART_STILL_RANGE = {1, 1};

    static {
        int collageMaxImageSize = IntentUtil.getCollageMaxImageSize();
        COLLAGE_RANGE_MAX = collageMaxImageSize;
        PRODUCE_COLLAGE_RANGE = new int[]{1, collageMaxImageSize};
    }
}
