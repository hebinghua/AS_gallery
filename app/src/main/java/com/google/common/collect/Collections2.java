package com.google.common.collect;

import ch.qos.logback.core.util.FileSize;
import com.baidu.platform.comapi.map.MapBundleKey;

/* loaded from: classes.dex */
public final class Collections2 {
    public static StringBuilder newStringBuilderForCollection(int i) {
        CollectPreconditions.checkNonnegative(i, MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE);
        return new StringBuilder((int) Math.min(i * 8, (long) FileSize.GB_COEFFICIENT));
    }
}
