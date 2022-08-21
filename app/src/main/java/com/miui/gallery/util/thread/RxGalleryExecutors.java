package com.miui.gallery.util.thread;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

/* loaded from: classes2.dex */
public class RxGalleryExecutors {
    public static volatile RxGalleryExecutors INSTANCE;
    public UIThreadExecutor mUiThreadExecutor = new UIThreadExecutor();
    public Scheduler mUiThreadScheduler;
    public UserThreadExecutor mUserThreadExecutor;
    public Scheduler mUserThreadScheduler;

    public RxGalleryExecutors() {
        UserThreadExecutor userThreadExecutor = new UserThreadExecutor();
        this.mUserThreadExecutor = userThreadExecutor;
        this.mUserThreadScheduler = Schedulers.from(userThreadExecutor);
        this.mUiThreadScheduler = this.mUiThreadExecutor.getScheduler();
    }

    public static RxGalleryExecutors getInstance() {
        if (INSTANCE == null) {
            synchronized (RxGalleryExecutors.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxGalleryExecutors();
                }
            }
        }
        return INSTANCE;
    }

    public UIThreadExecutor getUiThreadExecutor() {
        return this.mUiThreadExecutor;
    }

    public UserThreadExecutor getUserThreadExecutor() {
        return this.mUserThreadExecutor;
    }

    public Scheduler getUserThreadScheduler() {
        return this.mUserThreadScheduler;
    }

    public Scheduler getUiThreadScheduler() {
        return this.mUiThreadScheduler;
    }
}
