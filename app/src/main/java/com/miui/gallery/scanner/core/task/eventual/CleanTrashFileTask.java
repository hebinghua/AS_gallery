package com.miui.gallery.scanner.core.task.eventual;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.utils.ScanCache;
import com.miui.gallery.trash.TrashBinItem;
import com.miui.gallery.trash.TrashManager;
import com.miui.gallery.trash.TrashUtils;
import com.miui.gallery.util.BaseMiscUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class CleanTrashFileTask extends EventualScanTask {
    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        return 2103678160;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List list) throws Exception {
        return mo1304doRun(jobContext, (List<Throwable>) list);
    }

    public CleanTrashFileTask(Context context, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig, null);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean checkBeforeRun() {
        if (((List) ScanCache.getInstance().get("key_migrate_affected_paths")) != null) {
            return false;
        }
        return super.checkBeforeRun();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: doRun */
    public ScanResult mo1304doRun(ThreadPool.JobContext jobContext, List<Throwable> list) throws Exception {
        cleanTrashFile();
        return ScanResult.success(ScanContracts$ScanResultReason.DEFAULT).build();
    }

    public final void cleanTrashFile() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        GalleryEntityManager.getInstance().delete(TrashBinItem.class, "cloudServerId IS NULL  AND  ( trashFilePath IS NULL  OR trashFilePath = '' )", null);
        for (TrashBinItem trashBinItem : galleryEntityManager.query(TrashBinItem.class, null, null, null, null)) {
            String trashFilePath = trashBinItem.getTrashFilePath();
            String cloudServerId = trashBinItem.getCloudServerId();
            if (!TextUtils.isEmpty(trashFilePath) && !new File(trashFilePath).exists()) {
                if (!TextUtils.isEmpty(cloudServerId)) {
                    trashBinItem.setTrashFilePath(null);
                    arrayList2.add(cloudServerId);
                } else {
                    arrayList.add(Long.valueOf(trashBinItem.getRowId()));
                }
            }
        }
        if (BaseMiscUtil.isValid(arrayList2)) {
            ContentValues contentValues = new ContentValues();
            contentValues.putNull("trashFilePath");
            GalleryEntityManager.getInstance().update(TrashBinItem.class, contentValues, "cloudServerId IN ('" + TextUtils.join("', '", arrayList2) + "')", null);
        }
        TrashManager.getInstance().removeTrashBinItems(arrayList);
        TrashUtils.cleanRecoveryTrashFile();
    }

    @Override // com.miui.gallery.scanner.core.task.eventual.EventualScanTask, com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return obj instanceof CleanTrashFileTask;
    }
}
