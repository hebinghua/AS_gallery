package com.miui.gallery.scanner.core.task.convertor.scanpaths;

import android.content.Context;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.model.OwnerEntry;
import com.miui.gallery.scanner.core.model.OwnerItemEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter;
import com.miui.gallery.scanner.core.task.convertor.scanpaths.walker.TreeWalkListener;
import com.miui.gallery.scanner.core.task.convertor.scanpaths.walker.TreeWalkerProvider;
import com.miui.gallery.scanner.core.task.eventual.CleanFileTask;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.eventual.ScanDirectoryTask;
import com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask;
import com.miui.gallery.scanner.core.task.semi.ScanPathsTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ProcessingMediaHelper;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ScanPathsTaskConverter implements IScanTaskConverter<EventualScanTask> {
    public final Context mContext;
    public final ScanPathsTask mTask;

    public static /* synthetic */ void $r8$lambda$spWO6lXqPruMBARNpH7rYiwOY58(ScanPathsTaskConverter scanPathsTaskConverter, List list, ObservableEmitter observableEmitter) {
        scanPathsTaskConverter.lambda$convertFlow$0(list, observableEmitter);
    }

    public ScanPathsTaskConverter(Context context, ScanPathsTask scanPathsTask) {
        this.mContext = context;
        this.mTask = scanPathsTask;
    }

    public /* synthetic */ void lambda$convertFlow$0(List list, ObservableEmitter observableEmitter) throws Exception {
        convertToEventualScanTask(this.mContext, this.mTask, observableEmitter, list);
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
    public Observable<EventualScanTask> convertFlow(final List<Throwable> list) {
        return Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.scanner.core.task.convertor.scanpaths.ScanPathsTaskConverter$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                ScanPathsTaskConverter.$r8$lambda$spWO6lXqPruMBARNpH7rYiwOY58(ScanPathsTaskConverter.this, list, observableEmitter);
            }
        });
    }

    public static void convertToEventualScanTask(Context context, ScanPathsTask scanPathsTask, ObservableEmitter<EventualScanTask> observableEmitter, List<Throwable> list) {
        if (scanPathsTask == null) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        doConvertToEventualScanTasks(context, scanPathsTask, scanPathsTask.getPaths(), scanPathsTask.getConfig(), observableEmitter, list);
        DefaultLogger.d("ScanPathsTaskConverter", String.format("convertToEventualScanTask cost [%s] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis)));
    }

    public static void doConvertToEventualScanTasks(Context context, SemiScanTask semiScanTask, List<String> list, ScanTaskConfig scanTaskConfig, ObservableEmitter<EventualScanTask> observableEmitter, List<Throwable> list2) {
        if (context == null || !BaseMiscUtil.isValid(list)) {
            return;
        }
        TaskEmitter taskEmitter = new TaskEmitter(semiScanTask, observableEmitter);
        for (String str : list) {
            try {
                try {
                    dealWithPath(context, str, scanTaskConfig, taskEmitter);
                } catch (Exception e) {
                    list2.add(e);
                }
            } finally {
                taskEmitter.onPostEmit();
            }
        }
    }

    public static void dealWithPath(final Context context, String str, final ScanTaskConfig scanTaskConfig, final TaskEmitter taskEmitter) throws IOException {
        Path path = Paths.get(str, new String[0]);
        final DatabaseRecords databaseRecords = new DatabaseRecords(context, path, scanTaskConfig);
        TreeWalkerProvider.acquire(context, path, scanTaskConfig).walk(new TreeWalkListener() { // from class: com.miui.gallery.scanner.core.task.convertor.scanpaths.ScanPathsTaskConverter.1
            /* JADX WARN: Multi-variable type inference failed */
            /* JADX WARN: Type inference failed for: r10v2, types: [com.miui.gallery.scanner.core.task.eventual.ScanDirectoryTask] */
            @Override // com.miui.gallery.scanner.core.task.convertor.scanpaths.walker.TreeWalkListener
            public FileVisitResult visit(Path path2, BasicFileAttributes basicFileAttributes, boolean z) {
                ScanTaskConfig.FileState fileState = new ScanTaskConfig.FileState();
                fileState.size = basicFileAttributes.size();
                fileState.modified = basicFileAttributes.lastModifiedTime().toMillis();
                OwnerEntry visit = databaseRecords.visit(path2.toString(), z);
                if (visit != null) {
                    if (visit.isLatest(basicFileAttributes)) {
                        if (!scanTaskConfig.isForceScan()) {
                            return FileVisitResult.SKIP_SUBTREE;
                        }
                    } else if (basicFileAttributes.isRegularFile() && visit.hasSynced()) {
                        if (RemarkManager.isUnHandleMedia(path2.toString(), 1002)) {
                            DefaultLogger.d("ScanPathsTaskConverter", "cleanFile continue, file [%s] is pending handle by remark.", path2.toString());
                            return FileVisitResult.CONTINUE;
                        }
                        DefaultLogger.d("ScanPathsTaskConverter", "synced CloudEntry [%s] is not latest. attr is: [%s]", visit, Long.valueOf(basicFileAttributes.lastModifiedTime().toMillis()));
                        taskEmitter.registerAndEmit(new CleanFileTask(context, new ScanTaskConfig.Builder().cloneFrom(scanTaskConfig).setFileState(fileState).build(), Paths.get(path2.toString(), new String[0]), (OwnerItemEntry) visit));
                    }
                }
                ScanSingleFileTask scanSingleFileTask = null;
                OwnerAlbumEntry ownerAlbumEntry = null;
                if (basicFileAttributes.isRegularFile()) {
                    if (RemarkManager.isUnHandleMedia(path2.toString(), 1001)) {
                        DefaultLogger.d("ScanPathsTaskConverter", "scanFile continue, file [%s] is pending handle by remark.", path2.toString());
                        return FileVisitResult.CONTINUE;
                    }
                    scanSingleFileTask = ScanSingleFileTask.create(context, path2, new ScanTaskConfig.Builder().cloneFrom(scanTaskConfig).setFileState(fileState).build(), visit == null ? 2L : 1L);
                } else if (basicFileAttributes.isDirectory()) {
                    Context context2 = context;
                    ScanTaskConfig scanTaskConfig2 = scanTaskConfig;
                    if (visit instanceof OwnerAlbumEntry) {
                        ownerAlbumEntry = (OwnerAlbumEntry) visit;
                    }
                    scanSingleFileTask = new ScanDirectoryTask(context2, scanTaskConfig2, path2, ownerAlbumEntry);
                }
                if (scanSingleFileTask != null) {
                    taskEmitter.registerAndEmit(scanSingleFileTask);
                }
                return FileVisitResult.CONTINUE;
            }

            @Override // com.miui.gallery.scanner.core.task.convertor.scanpaths.walker.TreeWalkListener
            public void postVisitDirectory(Path path2) {
                ScanDirectoryTask scanDirectoryTask = taskEmitter.get(path2);
                if (scanDirectoryTask != null) {
                    scanDirectoryTask.setIsProducing(false);
                }
            }
        });
        for (Map.Entry<String, OwnerEntry> entry : databaseRecords.entrySet()) {
            if (entry.getValue() instanceof OwnerAlbumEntry) {
                for (Map.Entry<String, OwnerItemEntry> entry2 : ((OwnerAlbumEntry) entry.getValue()).getContents().entrySet()) {
                    cleanItemEntry(context, scanTaskConfig, entry2.getValue(), taskEmitter);
                }
            } else {
                cleanItemEntry(context, scanTaskConfig, (OwnerItemEntry) entry.getValue(), taskEmitter);
            }
        }
    }

    public static void cleanItemEntry(Context context, ScanTaskConfig scanTaskConfig, OwnerItemEntry ownerItemEntry, TaskEmitter taskEmitter) {
        if (!new File(ownerItemEntry.mPath).exists() && !ProcessingMediaHelper.getInstance().isMediaInProcessing(Scheme.FILE.wrap(ownerItemEntry.mPath))) {
            if (RemarkManager.isUnHandleMedia(ownerItemEntry.mPath, 1002)) {
                DefaultLogger.d("ScanPathsTaskConverter", "cleanFile continue, file [%s] is pending handle by remark.", ownerItemEntry.mPath);
            } else {
                taskEmitter.registerAndEmit(new CleanFileTask(context, scanTaskConfig, Paths.get(ownerItemEntry.mPath, new String[0]), ownerItemEntry));
            }
        }
    }
}
