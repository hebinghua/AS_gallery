package com.miui.gallery.scanner.core.task.convertor.scanpaths;

import com.miui.gallery.scanner.core.task.eventual.CleanFileTask;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.eventual.ScanDirectoryTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import io.reactivex.ObservableEmitter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class TaskEmitter {
    public final ObservableEmitter<EventualScanTask> mEmitter;
    public final Map<Path, ScanDirectoryTask> mParentTasks = new HashMap();
    public final SemiScanTask mSemiScanTask;

    public TaskEmitter(SemiScanTask semiScanTask, ObservableEmitter<EventualScanTask> observableEmitter) {
        this.mSemiScanTask = semiScanTask;
        this.mEmitter = observableEmitter;
    }

    public void registerAndEmit(EventualScanTask eventualScanTask) {
        if (eventualScanTask instanceof CleanFileTask) {
            eventualScanTask.setParentTask(this.mSemiScanTask);
        } else {
            ScanDirectoryTask scanDirectoryTask = this.mParentTasks.get(eventualScanTask.getPath().getParent());
            if (scanDirectoryTask != null) {
                eventualScanTask.setParentTask(scanDirectoryTask);
            } else {
                eventualScanTask.setParentTask(this.mSemiScanTask);
            }
        }
        if (eventualScanTask instanceof ScanDirectoryTask) {
            this.mParentTasks.put(eventualScanTask.getPath(), (ScanDirectoryTask) eventualScanTask);
        } else {
            this.mEmitter.onNext(eventualScanTask);
        }
    }

    public ScanDirectoryTask get(Path path) {
        return this.mParentTasks.get(path);
    }

    public void onPostEmit() {
        for (ScanDirectoryTask scanDirectoryTask : this.mParentTasks.values()) {
            if (scanDirectoryTask != null && scanDirectoryTask.isProducing()) {
                scanDirectoryTask.setIsProducing(false);
            }
        }
    }
}
