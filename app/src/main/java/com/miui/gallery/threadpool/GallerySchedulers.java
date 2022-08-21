package com.miui.gallery.threadpool;

import com.miui.gallery.util.concurrent.ThreadManager;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/* loaded from: classes2.dex */
public final class GallerySchedulers {

    /* loaded from: classes2.dex */
    public static final class MiscHolder {
        public static final Scheduler SCHEDULER = Schedulers.from(ThreadManager.getMiscPool().asExecutorService());
    }

    public static Scheduler misc() {
        return MiscHolder.SCHEDULER;
    }
}
