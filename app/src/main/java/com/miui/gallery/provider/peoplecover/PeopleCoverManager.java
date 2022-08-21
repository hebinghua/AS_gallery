package com.miui.gallery.provider.peoplecover;

import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.provider.FaceManager;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class PeopleCoverManager {
    public static Map<String, String> sCoverSelectionMap;

    static {
        HashMap hashMap = new HashMap();
        sCoverSelectionMap = hashMap;
        hashMap.put("serverId", "coverServerId");
        sCoverSelectionMap.put("faceXScale", "coverFaceXScale");
        sCoverSelectionMap.put("faceYScale", "coverFaceYScale");
        sCoverSelectionMap.put("faceWScale", "coverFaceWScale");
        sCoverSelectionMap.put("faceHScale", "coverFaceHScale");
        sCoverSelectionMap.put("groupId", "coverGroupId");
        sCoverSelectionMap.put("localGroupId", "coverLocalGroupId");
        sCoverSelectionMap.put("photo_id", "coverCloudId");
        sCoverSelectionMap.put("sha1", "coverSha1");
        sCoverSelectionMap.put("mixedDateTime", "coverDateTime");
        sCoverSelectionMap.put("microthumbfile", "coverMicroThumb");
        sCoverSelectionMap.put("thumbnailFile", "coverThumb");
        sCoverSelectionMap.put("localFile", "coveOrigin");
        sCoverSelectionMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, "coverSize");
        sCoverSelectionMap.put("exifOrientation", "cover_orientation");
    }

    public static String getLocalCoverTable() {
        String format = String.format("serverId IN (%s)  AND ((localFlag = 0 OR localFlag = 5 )  AND serverStatus = 'normal')", FaceManager.formatSelectionIn(LocalPeopleCoverManager.getInstance().getCoverIds()));
        return "(SELECT * FROM " + getCoverTableSelection(format) + " GROUP BY coverLocalGroupId)localSelect";
    }

    public static String getUserCoverTable() {
        return getCoverTableSelection("((localFlag = 0 OR localFlag = 5 )  AND serverStatus = 'normal')") + "userSelect";
    }

    public static String getCoverTableSelection(String str) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sCoverSelectionMap.entrySet()) {
            if (sb.length() != 0) {
                sb.append(",");
            }
            sb.append(entry.getKey());
            sb.append(" AS ");
            sb.append(entry.getValue());
        }
        return "(SELECT " + ((CharSequence) sb) + " FROM extended_faceImage WHERE " + str + " GROUP BY serverId ORDER BY mixedDateTime DESC )";
    }
}
