package com.miui.gallery.gallerywidget.common;

import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBEntity;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBManager;
import com.miui.gallery.gallerywidget.db.RecommendWidgetDBManager;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class WidgetStatisticsHelper {
    public static void logParamsInfo(String str, Map<String, Object> map) {
    }

    /* renamed from: com.miui.gallery.gallerywidget.common.WidgetStatisticsHelper$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize;

        static {
            int[] iArr = new int[IWidgetProviderConfig.WidgetSize.values().length];
            $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize = iArr;
            try {
                iArr[IWidgetProviderConfig.WidgetSize.SIZE_2_2.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_2_3.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_4_2.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_4_4.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public static String getStatisticsValue(IWidgetProviderConfig.WidgetSize widgetSize) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[widgetSize.ordinal()];
        return i != 1 ? i != 2 ? i != 3 ? i != 4 ? "" : "4x4" : "4x2" : "2x3" : "2x2";
    }

    public static void statisticsCustomWidgetStatus(int i, IWidgetProviderConfig.WidgetSize widgetSize, String str, int i2) {
        HashMap hashMap = new HashMap();
        hashMap.put("element_id", Integer.valueOf(i));
        hashMap.put("tip", "403.53.2.1.14205");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        hashMap.put("value", getStatisticsValue(widgetSize));
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i2));
        TrackController.trackStats(hashMap);
        logParamsInfo("statisticsCustomWidgetStatus", hashMap);
    }

    public static void statisticsRecommendWidgetStatus(int i, IWidgetProviderConfig.WidgetSize widgetSize, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("element_id", Integer.valueOf(i));
        hashMap.put("tip", "403.53.2.1.14204");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        hashMap.put("value", getStatisticsValue(widgetSize));
        TrackController.trackStats(hashMap);
        logParamsInfo("statisticsRecommendWidgetStatus", hashMap);
    }

    public static void statisticsCustomWidgetCount(IWidgetProviderConfig.WidgetSize widgetSize, int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.53.2.1.14203");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "selectedimages");
        hashMap.put("value", getStatisticsValue(widgetSize));
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        TrackController.trackStats(hashMap);
        logParamsInfo("statisticsCustomWidgetCount", hashMap);
    }

    public static void statisticsRecommendWidgetCount(IWidgetProviderConfig.WidgetSize widgetSize, int i) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.53.2.1.14202");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "memories");
        hashMap.put("value", getStatisticsValue(widgetSize));
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        TrackController.trackStats(hashMap);
        logParamsInfo("statisticsRecommendWidgetCount", hashMap);
    }

    public static void statisticsCustomWidgetDelete(int i, IWidgetProviderConfig.WidgetSize widgetSize) {
        CustomWidgetDBEntity findWidgetEntity = CustomWidgetDBManager.getInstance().findWidgetEntity(i);
        HashMap hashMap = new HashMap();
        hashMap.put("element_id", Integer.valueOf(i));
        hashMap.put("tip", "403.53.2.1.14205");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, statisticsGetTypeStatus(findWidgetEntity));
        hashMap.put("value", getStatisticsValue(widgetSize));
        hashMap.put("status", "delete");
        TrackController.trackStats(hashMap);
        logParamsInfo("statisticsCustomWidgetDelete", hashMap);
    }

    public static String statisticsGetTypeStatus(CustomWidgetDBEntity customWidgetDBEntity) {
        if (customWidgetDBEntity == null) {
            return "selectedimages_null";
        }
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(customWidgetDBEntity.getPicPath(), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("WidgetStatisticsHelper", "statisticsGetTypeStatus"));
        return (documentFile == null || !documentFile.exists()) ? "selectedimages_null" : "selectedimages_ture";
    }

    public static void statisticsRecommendWidgetDelete(int i, IWidgetProviderConfig.WidgetSize widgetSize) {
        String str;
        if (GalleryWidgetUtils.isStoryFunctionOn()) {
            str = RecommendWidgetDBManager.getInstance().findWidgetEntity((long) i) != null ? "memories_ture" : "memories_null";
        } else {
            str = "memories_false";
        }
        HashMap hashMap = new HashMap();
        hashMap.put("element_id", Integer.valueOf(i));
        hashMap.put("tip", "403.53.2.1.14204");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
        hashMap.put("value", getStatisticsValue(widgetSize));
        hashMap.put("status", "delete");
        TrackController.trackStats(hashMap);
        logParamsInfo("statisticsRecommendWidgetDelete", hashMap);
    }

    public static void statisticsWidgetEditorInit(String str, IWidgetProviderConfig.WidgetSize widgetSize, int i, long j) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.56.1.1.14198");
        hashMap.put("ref_tip", str);
        hashMap.put("value", getStatisticsValue(widgetSize));
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        hashMap.put("duration", Long.valueOf(j));
        TrackController.trackStats(hashMap);
        logParamsInfo("statisticsWidgetEditorInit", hashMap);
    }

    public static void statisticsWidgetEditorReselect(String str, IWidgetProviderConfig.WidgetSize widgetSize) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.56.1.1.14200");
        hashMap.put("ref_tip", str);
        hashMap.put("value", getStatisticsValue(widgetSize));
        TrackController.trackClick(hashMap);
        logParamsInfo("statisticsWidgetEditorReselect", hashMap);
    }

    public static void statisticsWidgetEditorComplete(String str, IWidgetProviderConfig.WidgetSize widgetSize, int i, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.56.1.1.14201");
        hashMap.put("ref_tip", str);
        hashMap.put("value", getStatisticsValue(widgetSize));
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str2);
        TrackController.trackClick(hashMap);
        logParamsInfo("statisticsWidgetEditorComplete", hashMap);
    }

    public static void statisticsWidgetEditorBack(String str, IWidgetProviderConfig.WidgetSize widgetSize) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.56.1.1.14199");
        hashMap.put("ref_tip", str);
        hashMap.put("value", getStatisticsValue(widgetSize));
        TrackController.trackClick(hashMap);
        logParamsInfo("statisticsWidgetEditorBack", hashMap);
    }
}
