package com.miui.gallery.cleaner;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.util.ArrayUtils;
import com.xiaomi.stat.a.j;

/* loaded from: classes.dex */
public interface CleanerContract$Projection {
    public static final String[] BASE_SCAN_PROJECTION;
    public static final String[] NORMAL_SCAN_PROJECTION;
    public static final String[] SIMILAR_SCAN_PROJECTION;
    public static final String[] SLIM_SCAN_PROJECTION;

    static {
        String[] strArr = {j.c, MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "localFile"};
        BASE_SCAN_PROJECTION = strArr;
        String[] strArr2 = (String[]) ArrayUtils.concat(strArr, new String[]{"thumbnailFile", "microthumbfile", "sha1"});
        NORMAL_SCAN_PROJECTION = strArr2;
        SLIM_SCAN_PROJECTION = (String[]) ArrayUtils.concat(strArr, new String[]{"exifImageWidth", "exifImageLength"});
        SIMILAR_SCAN_PROJECTION = (String[]) ArrayUtils.concat(strArr2, new String[]{"alias_create_time"});
    }
}
