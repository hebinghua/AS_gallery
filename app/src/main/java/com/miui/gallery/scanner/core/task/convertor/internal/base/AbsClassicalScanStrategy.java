package com.miui.gallery.scanner.core.task.convertor.internal.base;

import android.content.Context;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class AbsClassicalScanStrategy {
    public final Context mContext;
    public final List<IScanner> mScanners = new LinkedList();

    public AbsClassicalScanStrategy(Context context) {
        this.mContext = context;
    }

    public final void newClassicalScanTask(TaskEmitter taskEmitter) {
        Context context = this.mContext;
        if (context == null) {
            DefaultLogger.w("AbsClassicalScanStrategy", "Context should not be null");
        } else if (!PermissionUtils.checkStoragePermission(context)) {
            DefaultLogger.w("AbsClassicalScanStrategy", "checkStoragePermission failed.");
        } else {
            newBaseClassicalScanTask(taskEmitter);
        }
    }

    public void newBaseClassicalScanTask(TaskEmitter taskEmitter) {
        this.mScanners.add(new ShareAlbumScanner());
        this.mScanners.add(new TrashAlbumScanner());
        for (IScanner iScanner : this.mScanners) {
            taskEmitter.registerAndEmit(iScanner.createTasks(this.mContext));
        }
        taskEmitter.emit(new CorrectMediaTaskScanner().createTasks(this.mContext));
    }
}
