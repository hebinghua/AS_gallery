package com.miui.gallery.app.base;

import com.miui.gallery.base_optimization.clean.LifecycleUseCase;
import com.miui.gallery.base_optimization.clean.thread.ObserveThreadExecutor;
import com.miui.gallery.base_optimization.clean.thread.SubScribeThreadExecutor;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.subscribers.DisposableSubscriber;

/* loaded from: classes.dex */
public abstract class BaseUseCase<T, Param> extends LifecycleUseCase<T, Param> {
    public long mStartTime;

    public BaseUseCase() {
        this(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
    }

    public BaseUseCase(SubScribeThreadExecutor subScribeThreadExecutor, ObserveThreadExecutor observeThreadExecutor) {
        super(subScribeThreadExecutor, observeThreadExecutor);
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public void execute(DisposableSubscriber<T> disposableSubscriber, Param param) {
        this.mStartTime = System.currentTimeMillis();
        super.execute(disposableSubscriber, param);
    }

    public <T2> Flowable<T2> getArgumentNotNullError() {
        return Flowable.error(new IllegalArgumentException("arguments can't null"));
    }
}
