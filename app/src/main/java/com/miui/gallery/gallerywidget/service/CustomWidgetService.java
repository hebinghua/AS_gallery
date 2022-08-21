package com.miui.gallery.gallerywidget.service;

import android.app.Notification;
import android.content.Intent;
import android.graphics.RectF;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.common.WidgetInstallManager;
import com.miui.gallery.gallerywidget.common.WidgetStatisticsHelper;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBEntity;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBManager;
import com.miui.gallery.service.IntentServiceBase;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;

/* loaded from: classes2.dex */
public class CustomWidgetService extends IntentServiceBase {
    public int mCallNum;
    public boolean mIsFromCustomEditor;
    public boolean mIsFromPicStatusChange;
    public int[] mNewWidgetIds;
    public int[] mOldWidgetIds;
    public long[] mPicIds;
    public final String[] PIC_COLUMN_LIST = {j.c, "localFile", "thumbnailFile"};
    public final int PIC_COLUMN_INDEX_CLOUD_ID = 0;
    public final int PIC_COLUMN_INDEX_LOCAL_FILE = 1;
    public final int PIC_COLUMN_INDEX_THUMBNAIL_FILE = 2;
    public IWidgetProviderConfig.WidgetSize mWidgetSize = IWidgetProviderConfig.WidgetSize.SIZE_2_2;

    public static /* synthetic */ void $r8$lambda$SXfDCpK9moImHO_sQCPBlQTVeEg(List list, List list2, Long l, String str) {
        lambda$updateEntityIfUpdatePic$2(list, list2, l, str);
    }

    public static /* synthetic */ boolean $r8$lambda$UBE18qHNDa1ls49aEVu2FjyBSkE(List list, String str) {
        return list.contains(str);
    }

    @Override // com.miui.gallery.service.IntentServiceBase
    public int getNotificationId() {
        return 13;
    }

    @Override // com.miui.gallery.service.IntentServiceBase
    public Notification getNotification() {
        return NotificationHelper.getEmptyNotification(getApplicationContext());
    }

    @Override // com.miui.gallery.service.IntentServiceBase, android.app.IntentService
    public void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        DefaultLogger.d("CustomWidgetService", "---log---CustomWidgetService start");
        if (intent == null) {
            return;
        }
        int[] intArrayExtra = intent.getIntArrayExtra("appWidgetIds");
        int[] intArrayExtra2 = intent.getIntArrayExtra("gallery_app_restore_widget_id");
        this.mWidgetSize = (IWidgetProviderConfig.WidgetSize) intent.getSerializableExtra("gallery_app_widget_size");
        int i = 0;
        this.mIsFromPicStatusChange = intent.getBooleanExtra("from_pic_status_change", false);
        this.mIsFromCustomEditor = intent.getBooleanExtra("from_custom_editor", false);
        this.mPicIds = intent.getLongArrayExtra("pic_status_change_pic_ids");
        if (intArrayExtra == null) {
            return;
        }
        if (intent.getBooleanExtra("start_widget_service_for_widget_delete", false)) {
            delete(intArrayExtra);
        } else if (intArrayExtra2 != null && intArrayExtra2.length > 0 && intArrayExtra.length > 0) {
            if (Arrays.equals(intArrayExtra, this.mOldWidgetIds) && Arrays.equals(intArrayExtra2, this.mNewWidgetIds)) {
                return;
            }
            this.mNewWidgetIds = intArrayExtra2;
            this.mOldWidgetIds = intArrayExtra;
            while (i < intArrayExtra.length) {
                DefaultLogger.i("CustomWidgetService", "restoreWidgets");
                updateWidget(intArrayExtra[i], intArrayExtra2[i]);
                i++;
            }
        } else {
            int length = intArrayExtra.length;
            while (i < length) {
                int i2 = intArrayExtra[i];
                if (this.mIsFromPicStatusChange) {
                    updateWidgetForPicStatusChange(i2, this.mPicIds);
                } else {
                    updateWidget(i2, -1);
                }
                i++;
            }
        }
    }

    public final void delete(int[] iArr) {
        CustomWidgetDBManager.getInstance().delete(iArr);
        for (int i : iArr) {
            WidgetStatisticsHelper.statisticsCustomWidgetDelete(i, this.mWidgetSize);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:101:0x00cb  */
    /* JADX WARN: Removed duplicated region for block: B:102:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:115:0x00ee  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:129:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r5v14 */
    /* JADX WARN: Type inference failed for: r5v17 */
    /* JADX WARN: Type inference failed for: r7v5 */
    /* JADX WARN: Type inference failed for: r7v6, types: [java.util.HashMap] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void updateWidgetForPicStatusChange(int r12, long[] r13) {
        /*
            Method dump skipped, instructions count: 256
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.gallerywidget.service.CustomWidgetService.updateWidgetForPicStatusChange(int, long[]):void");
    }

    public final CustomWidgetDBEntity updateEntityIfDeletePic(CustomWidgetDBEntity customWidgetDBEntity, List<String> list) {
        if (customWidgetDBEntity == null || !BaseMiscUtil.isValid(list)) {
            return customWidgetDBEntity;
        }
        List<String> dataList = GalleryWidgetUtils.getDataList(customWidgetDBEntity.getPicCropList());
        List<String> dataList2 = GalleryWidgetUtils.getDataList(customWidgetDBEntity.getPicMatrixList());
        List<String> dataList3 = GalleryWidgetUtils.getDataList(customWidgetDBEntity.getPicPathList());
        List<String> dataList4 = GalleryWidgetUtils.getDataList(customWidgetDBEntity.getPicIDList());
        for (String str : list) {
            int indexOf = dataList4.indexOf(str);
            if (indexOf >= 0) {
                if (indexOf >= dataList.size() || indexOf >= dataList2.size() || indexOf >= dataList3.size() || indexOf >= dataList4.size()) {
                    DefaultLogger.w("CustomWidgetService", "---log---entity.getPicCropList()=%s,entity.getPicMatrixList()=%s,entity.getPicPathList()=%s,entity.getPicIDList()=%s", customWidgetDBEntity.getPicCropList(), customWidgetDBEntity.getPicMatrixList(), customWidgetDBEntity.getPicPathList(), customWidgetDBEntity.getPicIDList());
                    DefaultLogger.w("CustomWidgetService", "---log---picCropList.size()=%s,picMatrixList.size()=%s,picPathList.size()=%s,picIDList.size()=%s", Integer.valueOf(dataList.size()), Integer.valueOf(dataList2.size()), Integer.valueOf(dataList3.size()), Integer.valueOf(dataList4.size()));
                    CustomWidgetDBManager.getInstance().delete(new int[]{customWidgetDBEntity.getWidgetId()});
                    return null;
                }
                dataList.remove(indexOf);
                dataList2.remove(indexOf);
                dataList3.remove(indexOf);
                dataList4.remove(indexOf);
            }
        }
        if (!BaseMiscUtil.isValid(dataList4) || !BaseMiscUtil.isValid(dataList3)) {
            CustomWidgetDBManager.getInstance().delete(new int[]{customWidgetDBEntity.getWidgetId()});
            return null;
        }
        return new CustomWidgetDBEntity.Builder().setEntity(customWidgetDBEntity).setPicPath(dataList3.get(0)).setPicCropList(GalleryWidgetUtils.getDataListString(dataList)).setPicMatrixList(GalleryWidgetUtils.getDataListString(dataList2)).setPicPathList(GalleryWidgetUtils.getDataListString(dataList3)).setPicIDList(GalleryWidgetUtils.getDataListString(dataList4)).setCurrentIndex(0).build();
    }

    public final CustomWidgetDBEntity updateEntityIfUpdatePic(CustomWidgetDBEntity customWidgetDBEntity, HashMap<Long, String> hashMap) {
        if (customWidgetDBEntity == null || !BaseMiscUtil.isValid(hashMap)) {
            return customWidgetDBEntity;
        }
        final List<String> dataList = GalleryWidgetUtils.getDataList(customWidgetDBEntity.getPicIDList());
        final List<String> dataList2 = GalleryWidgetUtils.getDataList(customWidgetDBEntity.getPicPathList());
        if (!BaseMiscUtil.isValid(dataList) || !BaseMiscUtil.isValid(dataList2)) {
            CustomWidgetDBManager.getInstance().delete(new int[]{customWidgetDBEntity.getWidgetId()});
            return null;
        }
        hashMap.forEach(new BiConsumer() { // from class: com.miui.gallery.gallerywidget.service.CustomWidgetService$$ExternalSyntheticLambda0
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CustomWidgetService.$r8$lambda$SXfDCpK9moImHO_sQCPBlQTVeEg(dataList, dataList2, (Long) obj, (String) obj2);
            }
        });
        return new CustomWidgetDBEntity.Builder().setEntity(customWidgetDBEntity).setPicPath(dataList2.get(0)).setPicPathList(GalleryWidgetUtils.getDataListString(dataList2)).setCurrentIndex(0).build();
    }

    public static /* synthetic */ void lambda$updateEntityIfUpdatePic$2(List list, List list2, Long l, String str) {
        int indexOf = list.indexOf(String.valueOf(l));
        if (indexOf >= 0) {
            list2.set(indexOf, str);
        }
    }

    public final void updateWidget(int i, int i2) {
        CustomWidgetDBEntity findWidgetEntity = CustomWidgetDBManager.getInstance().findWidgetEntity(i);
        if (findWidgetEntity != null) {
            this.mCallNum = 1;
            updateWidget(i, i2, findWidgetEntity);
            return;
        }
        DefaultLogger.d("CustomWidgetService", "---log---findWidgetEntity null appWidgetId> %d ", Integer.valueOf(i));
        WidgetInstallManager.setCustomWidgetEmpty(this, i, i2, this.mWidgetSize);
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x00db A[Catch: all -> 0x002b, TryCatch #0 {all -> 0x002b, blocks: (B:77:0x000e, B:80:0x0019, B:86:0x0033, B:88:0x0039, B:91:0x003f, B:93:0x006d, B:95:0x0077, B:100:0x0083, B:102:0x00db, B:104:0x00ee, B:106:0x00f6, B:107:0x0113, B:108:0x012f, B:110:0x014c, B:113:0x0154, B:123:0x01a4, B:118:0x017c, B:122:0x018b, B:127:0x01cd, B:129:0x01e1, B:133:0x01e7, B:135:0x01f2, B:136:0x0209), top: B:141:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:110:0x014c A[Catch: all -> 0x002b, TryCatch #0 {all -> 0x002b, blocks: (B:77:0x000e, B:80:0x0019, B:86:0x0033, B:88:0x0039, B:91:0x003f, B:93:0x006d, B:95:0x0077, B:100:0x0083, B:102:0x00db, B:104:0x00ee, B:106:0x00f6, B:107:0x0113, B:108:0x012f, B:110:0x014c, B:113:0x0154, B:123:0x01a4, B:118:0x017c, B:122:0x018b, B:127:0x01cd, B:129:0x01e1, B:133:0x01e7, B:135:0x01f2, B:136:0x0209), top: B:141:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0187  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x018a  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x01e1 A[Catch: all -> 0x002b, TRY_LEAVE, TryCatch #0 {all -> 0x002b, blocks: (B:77:0x000e, B:80:0x0019, B:86:0x0033, B:88:0x0039, B:91:0x003f, B:93:0x006d, B:95:0x0077, B:100:0x0083, B:102:0x00db, B:104:0x00ee, B:106:0x00f6, B:107:0x0113, B:108:0x012f, B:110:0x014c, B:113:0x0154, B:123:0x01a4, B:118:0x017c, B:122:0x018b, B:127:0x01cd, B:129:0x01e1, B:133:0x01e7, B:135:0x01f2, B:136:0x0209), top: B:141:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:135:0x01f2 A[Catch: all -> 0x002b, TryCatch #0 {all -> 0x002b, blocks: (B:77:0x000e, B:80:0x0019, B:86:0x0033, B:88:0x0039, B:91:0x003f, B:93:0x006d, B:95:0x0077, B:100:0x0083, B:102:0x00db, B:104:0x00ee, B:106:0x00f6, B:107:0x0113, B:108:0x012f, B:110:0x014c, B:113:0x0154, B:123:0x01a4, B:118:0x017c, B:122:0x018b, B:127:0x01cd, B:129:0x01e1, B:133:0x01e7, B:135:0x01f2, B:136:0x0209), top: B:141:0x000e }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final synchronized void updateWidget(int r20, int r21, com.miui.gallery.gallerywidget.db.CustomWidgetDBEntity r22) {
        /*
            Method dump skipped, instructions count: 542
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.gallerywidget.service.CustomWidgetService.updateWidget(int, int, com.miui.gallery.gallerywidget.db.CustomWidgetDBEntity):void");
    }

    public final RectF getRegionRect(float[] fArr) {
        if (fArr == null || fArr.length < 4) {
            return new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        }
        return new RectF(fArr[0], fArr[1], fArr[2], fArr[3]);
    }
}
