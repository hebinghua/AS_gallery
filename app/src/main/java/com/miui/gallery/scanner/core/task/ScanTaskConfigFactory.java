package com.miui.gallery.scanner.core.task;

import com.miui.gallery.scanner.core.scanner.ShareImageScanner;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class ScanTaskConfigFactory {
    public static final Map<Integer, ScanTaskConfig.Builder> sScanTaskConfigBuilderMap;

    static {
        HashMap hashMap = new HashMap(20);
        sScanTaskConfigBuilderMap = hashMap;
        ScanTaskConfig.Builder sceneCode = new ScanTaskConfig.Builder().setSceneCode(0);
        ScanTaskConfig.Builder sceneCode2 = new ScanTaskConfig.Builder().setSceneCode(0);
        ScanTaskConfig.Builder priority = new ScanTaskConfig.Builder().setSceneCode(2).setBulkNotify(true).isActiveScan(true).setPriority(4L);
        ScanTaskConfig.Builder priority2 = new ScanTaskConfig.Builder().setSceneCode(3).setBulkNotify(true).setPriority(4L);
        ScanTaskConfig.Builder priority3 = new ScanTaskConfig.Builder().setSceneCode(4).setBulkNotify(true).setPriority(4L);
        ScanTaskConfig.Builder showInRecentAlbum = new ScanTaskConfig.Builder().setSceneCode(5).setPriority(16L).setRecursiveScan(true).forceScan(true).showInRecentAlbum(true);
        ScanTaskConfig.Builder credible = new ScanTaskConfig.Builder().setSceneCode(6).setPriority(32L).setCredible(true);
        ScanTaskConfig.Builder recursiveScan = new ScanTaskConfig.Builder().setSceneCode(7).setPriority(2L).setBulkNotify(true).setRecursiveScan(true);
        ScanTaskConfig.Builder showInRecentAlbum2 = new ScanTaskConfig.Builder().setSceneCode(8).setPriority(32L).isActiveScan(true).setCredible(true).showInRecentAlbum(true);
        ScanTaskConfig.Builder isActiveScan = new ScanTaskConfig.Builder().setSceneCode(9).setPriority(8L).isActiveScan(true);
        ScanTaskConfig.Builder recursiveScan2 = new ScanTaskConfig.Builder().setSceneCode(10).setPriority(1L).setBulkNotify(true).setRecursiveScan(true);
        ScanTaskConfig.Builder needTriggerSync = new ScanTaskConfig.Builder().setSceneCode(11).setPriority(16L).setRecursiveScan(true).needTriggerSync(false);
        ScanTaskConfig.Builder isActiveScan2 = new ScanTaskConfig.Builder().setSceneCode(12).setPriority(32L).isActiveScan(true);
        ScanTaskConfig.Builder credible2 = new ScanTaskConfig.Builder().setSceneCode(13).setPriority(32L).isActiveScan(true).setCredible(true);
        ScanTaskConfig.Builder isActiveScan3 = new ScanTaskConfig.Builder().setSceneCode(14).setScanner(new ShareImageScanner()).setPriority(32L).isActiveScan(true);
        ScanTaskConfig.Builder priority4 = new ScanTaskConfig.Builder().setSceneCode(15).setPriority(16L);
        ScanTaskConfig.Builder priority5 = new ScanTaskConfig.Builder().setSceneCode(16).setPriority(32L);
        ScanTaskConfig.Builder credible3 = new ScanTaskConfig.Builder().setSceneCode(17).setPriority(32L).setCredible(true);
        ScanTaskConfig.Builder recursiveScan3 = new ScanTaskConfig.Builder().setSceneCode(18).setPriority(1L).forceScan(true).setBulkNotify(true).setRecursiveScan(true);
        ScanTaskConfig.Builder isActiveScan4 = new ScanTaskConfig.Builder().setSceneCode(19).setPriority(4L).setBulkNotify(true).isActiveScan(true);
        ScanTaskConfig.Builder needTriggerSync2 = new ScanTaskConfig.Builder().setSceneCode(20).setPriority(32L).setBulkNotify(true).needTriggerSync(false);
        ScanTaskConfig.Builder priority6 = new ScanTaskConfig.Builder().setSceneCode(21).setPriority(1L);
        ScanTaskConfig.Builder priority7 = new ScanTaskConfig.Builder().setSceneCode(22).setPriority(1L);
        hashMap.put(0, sceneCode);
        hashMap.put(1, sceneCode2);
        hashMap.put(2, priority);
        hashMap.put(3, priority2);
        hashMap.put(4, priority3);
        hashMap.put(5, showInRecentAlbum);
        hashMap.put(6, credible);
        hashMap.put(7, recursiveScan);
        hashMap.put(8, showInRecentAlbum2);
        hashMap.put(9, isActiveScan);
        hashMap.put(10, recursiveScan2);
        hashMap.put(11, needTriggerSync);
        hashMap.put(12, isActiveScan2);
        hashMap.put(13, credible2);
        hashMap.put(14, isActiveScan3);
        hashMap.put(15, priority4);
        hashMap.put(16, priority5);
        hashMap.put(17, credible3);
        hashMap.put(18, recursiveScan3);
        hashMap.put(19, isActiveScan4);
        hashMap.put(20, needTriggerSync2);
        hashMap.put(21, priority6);
        hashMap.put(22, priority7);
    }

    public static ScanTaskConfig get(int i) {
        ScanTaskConfig.Builder builder = sScanTaskConfigBuilderMap.get(Integer.valueOf(i));
        if (builder == null) {
            throw new IllegalArgumentException(String.format("scene code [%d] is illegal.", Integer.valueOf(i)));
        }
        return builder.build();
    }
}
