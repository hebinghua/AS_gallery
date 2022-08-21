package com.miui.gallery.card.scenario;

import com.miui.gallery.assistant.manager.AnalyticFaceAndSceneManager;
import com.miui.gallery.cloud.CloudTableUtils;

/* loaded from: classes.dex */
public class ScenarioConstants {
    public static final String ALL_MEDIA_CALCULATION_SELECTION;
    public static final String ALL_MEDIA_SCENARIO_CALCULATION_SELECTION;
    public static final String BASE_IMAGE_SELECTION;
    public static final String BASE_MEDIA_CALCULATION_SELECTION;
    public static final String BASE_MEDIA_SCENARIO_SELECTION;
    public static final String BASE_MEDIA_SELECTION;
    public static final String BASE_SELECTION;
    public static final String IMAGE_FEATURE_CALCULATION_SELECTION;
    public static final String IMAGE_SCENARIO_SELECTION;
    public static final String MEDIA_CALCULATION_SELECTION;
    public static final String MEDIA_SCENARIO_CALCULATION_SELECTION;
    public static final String MEDIA_SCENARIO_SELECTION;

    static {
        String str = "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND exifImageWidth > 512 AND exifImageLength > 512 AND (duration IS NULL OR duration <= 480) AND (specialTypeFlags & 262144 == 0 OR specialTypeFlags & 4194304 == 0) AND ( localGroupId NOT IN  ( select _id from album where  ( attributes & 2048 <> 0))) AND localGroupId != " + CloudTableUtils.getCloudIdForGroupWithoutRecord(1000L) + " AND localGroupId != " + CloudTableUtils.getCloudIdForGroupWithoutRecord(1001L);
        BASE_SELECTION = str;
        String str2 = str + " AND serverType=1";
        BASE_IMAGE_SELECTION = str2;
        String str3 = str2 + " AND (lower(mimeType) IN ('image/jpeg', 'image/png', 'image/x-ms-bmp', 'image/webp', 'image/vnd.wap.wbmp', 'image/heif', 'image/heic', 'video/mp4', 'video/quicktime'))";
        IMAGE_FEATURE_CALCULATION_SELECTION = str3;
        String str4 = str + " AND (serverType=1 AND (exifModel NOTNULL OR localGroupId = (SELECT _id FROM album WHERE serverId=1)) OR serverType=2 AND localGroupId = (SELECT _id FROM album WHERE serverId=1))";
        BASE_MEDIA_SELECTION = str4;
        String str5 = str4 + " AND (lower(mimeType) IN ('image/jpeg', 'image/png', 'image/x-ms-bmp', 'image/webp', 'image/vnd.wap.wbmp', 'image/heif', 'image/heic', 'video/mp4', 'video/quicktime'))";
        BASE_MEDIA_SCENARIO_SELECTION = str5;
        String str6 = str3 + " AND (localGroupId = (SELECT _id FROM album WHERE serverId=1) OR exifModel != '')";
        IMAGE_SCENARIO_SELECTION = str6;
        if (!AnalyticFaceAndSceneManager.isDeviceSupportVideo()) {
            str5 = str6;
        }
        MEDIA_SCENARIO_SELECTION = str5;
        String str7 = str + " AND (serverType=1 AND (exifModel NOTNULL OR localGroupId = (SELECT _id FROM album WHERE serverId=1)) OR ((serverType=2 AND localFile IS NOT NULL AND localFile != '') AND localGroupId = (SELECT _id FROM album WHERE serverId=1)))";
        MEDIA_SCENARIO_CALCULATION_SELECTION = str7;
        if (AnalyticFaceAndSceneManager.isDeviceSupportVideo()) {
            str6 = str7;
        }
        ALL_MEDIA_SCENARIO_CALCULATION_SELECTION = str6;
        String str8 = str + " AND (serverType=1 OR (serverType=2 AND localFile IS NOT NULL AND localFile != ''))";
        BASE_MEDIA_CALCULATION_SELECTION = str8;
        String str9 = str8 + " AND (lower(mimeType) IN ('image/jpeg', 'image/png', 'image/x-ms-bmp', 'image/webp', 'image/vnd.wap.wbmp', 'image/heif', 'image/heic', 'video/mp4', 'video/quicktime'))";
        MEDIA_CALCULATION_SELECTION = str9;
        if (AnalyticFaceAndSceneManager.isDeviceSupportVideo()) {
            str3 = str9;
        }
        ALL_MEDIA_CALCULATION_SELECTION = str3;
    }
}
