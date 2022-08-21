package com.miui.gallery.scanner.utils;

import com.miui.gallery.concurrent.PriorityThreadFactory;
import com.miui.gallery.scanner.extra.genthumbnail.AbsGenThumbnailTask;
import com.miui.gallery.scanner.extra.genthumbnail.GenMicroThumbnailTask;
import com.miui.gallery.threadpool.PriorityTaskManager;

/* loaded from: classes2.dex */
public class GenThumbnailUtil {
    public static final Object sThumbnailLock = new Object();
    public static volatile PriorityTaskManager<AbsGenThumbnailTask> sThumbnailManager;

    public static void genMicroThumbnail(String str, long j) {
        getThumbnailTaskManager().submit(new GenMicroThumbnailTask(str, j));
    }

    public static PriorityTaskManager<AbsGenThumbnailTask> getThumbnailTaskManager() {
        if (sThumbnailManager == null) {
            synchronized (sThumbnailLock) {
                if (sThumbnailManager == null) {
                    sThumbnailManager = new PriorityTaskManager<>(2, new PriorityThreadFactory("thumbnail-preload", 10), (PriorityTaskManager.OnAllTasksExecutedListener) null);
                }
            }
        }
        return sThumbnailManager;
    }
}
