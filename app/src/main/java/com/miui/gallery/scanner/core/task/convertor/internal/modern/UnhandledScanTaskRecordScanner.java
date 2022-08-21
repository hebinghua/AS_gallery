package com.miui.gallery.scanner.core.task.convertor.internal.modern;

import android.content.Context;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.scanner.core.ScanContracts$StatusReason;
import com.miui.gallery.scanner.core.model.UnhandledScanTaskRecord;
import com.miui.gallery.scanner.core.task.BaseScanTaskStateListener;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.ScanTaskConfigFactory;
import com.miui.gallery.scanner.core.task.convertor.ExternalScanTaskConverter;
import com.miui.gallery.scanner.core.task.convertor.internal.base.IScanner;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class UnhandledScanTaskRecordScanner implements IScanner {
    @Override // com.miui.gallery.scanner.core.task.convertor.internal.base.IScanner
    public SemiScanTask[] createTasks(Context context) {
        List<UnhandledScanTaskRecord> query = GalleryEntityManager.getInstance().query(UnhandledScanTaskRecord.class, "", null);
        LinkedList linkedList = new LinkedList();
        for (UnhandledScanTaskRecord unhandledScanTaskRecord : query) {
            final long rowId = unhandledScanTaskRecord.getRowId();
            int sceneCode = unhandledScanTaskRecord.getSceneCode();
            String path = unhandledScanTaskRecord.getPath();
            unhandledScanTaskRecord.getCreateTime();
            String callingPackageName = unhandledScanTaskRecord.getCallingPackageName();
            String operatorPackageName = unhandledScanTaskRecord.getOperatorPackageName();
            int parallelProcessState = unhandledScanTaskRecord.getParallelProcessState();
            long mediaStoreId = unhandledScanTaskRecord.getMediaStoreId();
            boolean usingGaussian = unhandledScanTaskRecord.usingGaussian();
            if (sceneCode == 15) {
                List<SemiScanTask> convert = new ExternalScanTaskConverter(context, mediaStoreId, path, usingGaussian, callingPackageName, operatorPackageName, parallelProcessState, new ScanTaskConfig.Builder().cloneFrom(ScanTaskConfigFactory.get(15)).build()).convert(new LinkedList());
                linkedList.addAll(convert);
                for (SemiScanTask semiScanTask : convert) {
                    semiScanTask.addStateListener(new BaseScanTaskStateListener<SemiScanTask>() { // from class: com.miui.gallery.scanner.core.task.convertor.internal.modern.UnhandledScanTaskRecordScanner.1
                        @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
                        public void onAbandoned(SemiScanTask semiScanTask2, ScanContracts$StatusReason scanContracts$StatusReason) {
                            onDone(semiScanTask2, scanContracts$StatusReason);
                        }

                        @Override // com.miui.gallery.scanner.core.task.BaseScanTaskStateListener
                        public void onDone(SemiScanTask semiScanTask2, ScanContracts$StatusReason scanContracts$StatusReason) {
                            GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
                            galleryEntityManager.delete(UnhandledScanTaskRecord.class, "_id = " + rowId, null);
                        }
                    });
                }
            } else {
                DefaultLogger.w("UnhandledScanTaskRecordScanner", "Unsupported task scene code");
            }
        }
        return (SemiScanTask[]) linkedList.toArray(new SemiScanTask[0]);
    }
}
