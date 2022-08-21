package com.miui.gallery.model;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;

/* loaded from: classes2.dex */
public interface AlbumConstants {
    public static final String[] DB_REAL_PROJECTION = {j.c, "name", "attributes", "dateTaken", "dateModified", "sortInfo", CallMethod.ARG_EXTRA_STRING, "localFlag", "serverId", "localPath", "coverId", "realDateModified", "serverTag", "serverStatus", "editedColumns"};
    public static final String[] QUERY_ALBUM_PROJECTION = {j.c, "name", "attributes", "dateTaken", "dateModified", "sortInfo", CallMethod.ARG_EXTRA_STRING, "localFlag", "serverId", "localPath", "coverId", "realDateModified", "serverTag", "serverStatus", "editedColumns", "photoCount", "coverSyncState", "coverSize", "coverPath", "coverSha1", "is_manual_set_cover", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "sortBy"};
    public static final String[] SHARED_ALBUM_PROJECTION = {j.c, "creatorId", MiStat.Param.COUNT, "nickname"};
}
