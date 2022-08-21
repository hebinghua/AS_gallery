package com.miui.gallery.base_optimization.clean;

import com.miui.gallery.base_optimization.clean.thread.ObserveThreadExecutor;
import com.miui.gallery.base_optimization.clean.thread.SubScribeThreadExecutor;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;

/* loaded from: classes.dex */
public abstract class UseCase<T, Param> {
    public CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    public DisposableSubscriber<T> mLastSubscribe;
    public ObserveThreadExecutor mObserveThreadExecutor;
    public Param mParam;
    public SubScribeThreadExecutor mSubScribeThreadExecutor;

    public abstract Flowable<T> buildUseCaseFlowable(Param param);

    public UseCase(SubScribeThreadExecutor subScribeThreadExecutor, ObserveThreadExecutor observeThreadExecutor) {
        this.mSubScribeThreadExecutor = subScribeThreadExecutor;
        this.mObserveThreadExecutor = observeThreadExecutor;
    }

    public void execute(DisposableSubscriber<T> disposableSubscriber, Param param) {
        this.mParam = param;
        this.mLastSubscribe = disposableSubscriber;
        Flowable<T> buildUseCaseFlowable = buildUseCaseFlowable(param);
        SubScribeThreadExecutor subScribeThreadExecutor = this.mSubScribeThreadExecutor;
        if (subScribeThreadExecutor != null) {
            buildUseCaseFlowable = buildUseCaseFlowable.subscribeOn(Schedulers.from(subScribeThreadExecutor));
        }
        ObserveThreadExecutor observeThreadExecutor = this.mObserveThreadExecutor;
        if (observeThreadExecutor != null) {
            buildUseCaseFlowable = buildUseCaseFlowable.observeOn(observeThreadExecutor.getScheduler(), true);
        }
        addDisposable((Disposable) buildUseCaseFlowable.subscribeWith(disposableSubscriber));
    }

    public final void addDisposable(Disposable disposable) {
        getCompositeDisposable().add(disposable);
    }

    public final CompositeDisposable getCompositeDisposable() {
        return this.mCompositeDisposable;
    }

    public boolean isDispose() {
        CompositeDisposable compositeDisposable = this.mCompositeDisposable;
        if (compositeDisposable != null) {
            return compositeDisposable.isDisposed();
        }
        return true;
    }

    public void dispose() {
        CompositeDisposable compositeDisposable = this.mCompositeDisposable;
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
    }

    public void disposeAndAgain() {
        dispose();
        this.mCompositeDisposable = new CompositeDisposable();
    }

    public Param getParam() {
        return this.mParam;
    }
}
