package com.miui.gallery.provider.cloudmanager;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.xiaomi.stat.a.j;

/* loaded from: classes2.dex */
public class Contracts {
    public static final String[] QUERY_BY_PATH_PROJECTION = {j.c, "sha1", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "serverStatus"};
    public static final String[] PROJECTION = {j.c, "groupId", "localFlag", "localGroupId", "serverId", "serverType", "fileName", "localFile", "thumbnailFile", "sha1", "ubiSubImageCount", "secretKey", "microthumbfile", "albumId", "description", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "dateModified", "mimeType", "title", "description", "dateTaken", "duration", "serverTag", "serverStatus", "downloadFile", "sortBy", "localImageId", "downloadFileStatus", "downloadFileTime", "mixedDateTime", "exifImageWidth", "exifImageLength", "exifOrientation", "exifGPSLatitude", "exifGPSLongitude", "exifMake", "exifModel", "exifFlash", "exifGPSLatitudeRef", "exifGPSLongitudeRef", "exifExposureTime", "exifFNumber", "exifISOSpeedRatings", "exifGPSAltitude", "exifGPSAltitudeRef", "exifGPSTimeStamp", "exifGPSDateStamp", "exifWhiteBalance", "exifFocalLength", "exifGPSProcessingMethod", "exifDateTime", "creatorId", "ubiFocusIndex", "ubiSubIndex", "editedColumns", "fromLocalGroupId", "location", "extraGPS", "address", "specialTypeFlags"};
    public static final String[] PRIVATE_COPYABLE_PROJECTION = {"thumbnailFile", "downloadFile", "localFile", "microthumbfile"};
    public static final String[] PUBLIC_COPYABLE_PROJECTION = {"fileName", "dateTaken", "dateModified", "description", "serverType", MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "mimeType", "title", "sha1", "duration", "appKey", "babyInfoJson", "mixedDateTime", "location", "extraGPS", "address", "exifImageWidth", "exifImageLength", "exifOrientation", "exifGPSLatitude", "exifGPSLongitude", "exifMake", "exifModel", "exifFlash", "exifGPSLatitudeRef", "exifGPSLongitudeRef", "exifExposureTime", "exifFNumber", "exifISOSpeedRatings", "exifGPSAltitude", "exifGPSAltitudeRef", "exifGPSTimeStamp", "exifGPSDateStamp", "exifWhiteBalance", "exifFocalLength", "exifGPSProcessingMethod", "exifDateTime", "ubiSubImageCount", "ubiFocusIndex", "ubiSubIndex", "specialTypeFlags"};
}
