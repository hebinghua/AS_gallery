package com.miui.gallery.scanner.extra.genthumbnail;

import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.threadpool.PriorityTask;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public abstract class AbsGenThumbnailTask implements PriorityTask<AbsGenThumbnailTask, Void> {
    public final long mGenTime = System.currentTimeMillis();
    public final long mKeepValidTime;
    public final String mMimeType;
    public final String mPath;

    public abstract void request(ThreadPool.JobContext jobContext);

    public AbsGenThumbnailTask(String str, long j) {
        this.mPath = str;
        this.mMimeType = BaseFileMimeUtil.getMimeType(str);
        this.mKeepValidTime = j;
    }

    public long getGenTime() {
        return this.mGenTime;
    }

    @Override // java.lang.Comparable
    public int compareTo(AbsGenThumbnailTask absGenThumbnailTask) {
        if (this.mGenTime == absGenThumbnailTask.getGenTime()) {
            return 0;
        }
        return this.mGenTime - absGenThumbnailTask.getGenTime() > 0 ? 1 : -1;
    }

    @Override // com.miui.gallery.concurrent.ThreadPool.Job
    /* renamed from: run  reason: collision with other method in class */
    public final Void mo1807run(ThreadPool.JobContext jobContext) {
        if (jobContext.isCancelled()) {
            DefaultLogger.d("AbsGenThumbnailTask", "%s is cancelled", this);
            return null;
        } else if (this.mKeepValidTime > 0 && System.currentTimeMillis() - this.mGenTime > this.mKeepValidTime) {
            DefaultLogger.d("AbsGenThumbnailTask", "%s over valid time", this);
            return null;
        } else {
            request(jobContext);
            return null;
        }
    }
}
