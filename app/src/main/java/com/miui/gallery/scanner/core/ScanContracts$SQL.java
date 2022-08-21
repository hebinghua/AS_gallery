package com.miui.gallery.scanner.core;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.xiaomi.stat.a.j;

/* loaded from: classes2.dex */
public interface ScanContracts$SQL {
    public static final String[] ALBUM_PROJECTION = {j.c, "serverId", "realDateModified", "localFlag", "serverStatus", "name", "attributes", "editedColumns", "localPath", "scan_public_media_count", "scan_public_media_generation_modified"};
    public static final String[] ALBUM_NAME_CONFLICT_PROJECTION = {"count(*)"};
    public static final String[] SPECIAL_TYPE_FLAGS_PROJECTION = {j.c, "localFile"};
    public static final String SPECIAL_TYPE_FLAGS_WHERE = "_id > ? AND serverType = 1 AND " + InternalContract$Cloud.ALIAS_ORIGIN_FILE_VALID + " AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
    public static final String[] CLEAN_UP_FILE_PATH_PROJECTION = {j.c, "fileName", "localFile", "thumbnailFile"};
    public static final String[] ENSURE_INFO_PROJECTION_VIDEO = {"latitude", "longitude"};
    public static final String[] CLOUD_PROJECTION = {j.c, MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "fileName", "serverStatus", "localFlag", "sha1", "localFile", "thumbnailFile", "serverId", "dateTaken", "localGroupId"};
}
