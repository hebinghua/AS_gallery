package com.miui.gallery.base_optimization.clean;

import com.miui.gallery.base_optimization.clean.thread.ObserveThreadExecutor;
import com.miui.gallery.base_optimization.clean.thread.SubScribeThreadExecutor;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.ArrayList;
import java.util.List;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/* loaded from: classes.dex */
public abstract class HotUseCase<T, Param> extends LifecycleUseCase<T, Param> {
    public boolean isNeedReStartStream;
    public Flowable<T> mLastFlowable;
    public List<Subscription> mSubscriptions;
    public HotUseCase<T, Param>.BaseFunction mTransform;
    public Disposable mUpStreamDisposable;

    public abstract Flowable<T> buildFlowable(Param param);

    public HotUseCase(SubScribeThreadExecutor subScribeThreadExecutor, ObserveThreadExecutor observeThreadExecutor) {
        super(subScribeThreadExecutor, observeThreadExecutor);
        this.mSubscriptions = new ArrayList(2);
        this.isNeedReStartStream = true;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public final Flowable<T> buildUseCaseFlowable(Param param) {
        Flowable<T> buildFlowable = buildFlowable(param);
        HotUseCase<T, Param>.BaseFunction modeImpl = getModeImpl();
        this.mTransform = modeImpl;
        Flowable<T> flowable = (Flowable) buildFlowable.to(modeImpl);
        this.mLastFlowable = flowable;
        return flowable.doOnSubscribe(new Consumer<Subscription>() { // from class: com.miui.gallery.base_optimization.clean.HotUseCase.2
            @Override // io.reactivex.functions.Consumer
            public void accept(Subscription subscription) throws Exception {
                HotUseCase.this.mSubscriptions.add(subscription);
            }
        }).onErrorResumeNext(new Function<Throwable, Publisher<? extends T>>() { // from class: com.miui.gallery.base_optimization.clean.HotUseCase.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public Publisher<? extends T> mo2564apply(Throwable th) throws Exception {
                HotUseCase.this.mLastSubscribe.onError(th);
                return (Publisher<T>) new Publisher<T>() { // from class: com.miui.gallery.base_optimization.clean.HotUseCase.1.1
                    @Override // org.reactivestreams.Publisher
                    public void subscribe(Subscriber<? super T> subscriber) {
                    }
                };
            }
        });
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public void execute(DisposableSubscriber<T> disposableSubscriber, Param param) {
        super.execute(disposableSubscriber, param);
    }

    @Override // com.miui.gallery.base_optimization.clean.LifecycleUseCase
    public void executeWith(DisposableSubscriber<T> disposableSubscriber, Param param, Object obj) {
        super.executeWith(disposableSubscriber, param, obj);
    }

    public HotUseCase<T, Param>.BaseFunction getModeImpl() {
        return new HotModeFunction();
    }

    /* loaded from: classes.dex */
    public abstract class BaseFunction implements Function<Flowable<T>, Flowable<T>> {
        public volatile boolean isStop = false;
        public int mBufferSize;

        public void doOnCancel() {
        }

        public abstract void onLifecycleStart();

        public abstract Flowable<T> transform(Flowable<T> flowable);

        @Override // io.reactivex.functions.Function
        /* renamed from: apply */
        public /* bridge */ /* synthetic */ Object mo2564apply(Object obj) throws Exception {
            return apply((Flowable) ((Flowable) obj));
        }

        public BaseFunction(int i) {
            this.mBufferSize = i;
        }

        public final Flowable<T> apply(Flowable<T> flowable) throws Exception {
            return transform(flowable).doOnCancel(new Action() { // from class: com.miui.gallery.base_optimization.clean.HotUseCase.BaseFunction.1
                @Override // io.reactivex.functions.Action
                public void run() throws Exception {
                    BaseFunction.this.isStop = true;
                    BaseFunction.this.doOnCancel();
                }
            });
        }

        public Flowable<T> getWrapperLastFlowable() {
            return getWrapperFlowable(HotUseCase.this.mLastFlowable);
        }

        public Flowable<T> getWrapperFlowable(Flowable<T> flowable) {
            return flowable.doOnSubscribe(new Consumer<Subscription>() { // from class: com.miui.gallery.base_optimization.clean.HotUseCase.BaseFunction.2
                @Override // io.reactivex.functions.Consumer
                public void accept(Subscription subscription) throws Exception {
                    BaseFunction.this.isStop = false;
                    HotUseCase.this.mSubscriptions.add(subscription);
                }
            });
        }

        public final void onStart() {
            if (this.isStop) {
                onLifecycleStart();
            }
        }

        public boolean isStop() {
            return this.isStop;
        }
    }

    /* loaded from: classes.dex */
    public class HotModeFunction extends HotUseCase<T, Param>.BaseFunction {
        public volatile boolean isEmptyData;
        public volatile boolean isFirstData;
        public volatile Boolean onReturnValueWhenOnStop;

        public boolean reBindWhenOnStart() {
            return true;
        }

        public HotModeFunction() {
            super(1);
            this.isFirstData = true;
        }

        @Override // com.miui.gallery.base_optimization.clean.HotUseCase.BaseFunction
        public Flowable<T> transform(Flowable<T> flowable) {
            return flowable.doOnNext(new Consumer<T>() { // from class: com.miui.gallery.base_optimization.clean.HotUseCase.HotModeFunction.2
                @Override // io.reactivex.functions.Consumer
                public void accept(T t) throws Exception {
                    if (!HotModeFunction.this.isStop) {
                        return;
                    }
                    HotModeFunction.this.onReturnValueWhenOnStop = Boolean.TRUE;
                    DefaultLogger.d("HotUsecase", "onStop期间接收数据:");
                    DefaultLogger.d("HotUsecase", t);
                }
            }).replay(this.mBufferSize).autoConnect(this.mBufferSize, new Consumer<Disposable>() { // from class: com.miui.gallery.base_optimization.clean.HotUseCase.HotModeFunction.1
                @Override // io.reactivex.functions.Consumer
                public void accept(Disposable disposable) throws Exception {
                    HotUseCase.this.mUpStreamDisposable = disposable;
                }
            });
        }

        @Override // com.miui.gallery.base_optimization.clean.HotUseCase.BaseFunction
        public void doOnCancel() {
            super.doOnCancel();
            this.isFirstData = true;
            this.isEmptyData = false;
        }

        @Override // com.miui.gallery.base_optimization.clean.HotUseCase.BaseFunction
        public void onLifecycleStart() {
            if (HotUseCase.this.mLastSubscribe != null) {
                boolean z = false;
                this.isEmptyData = this.onReturnValueWhenOnStop == null || !this.onReturnValueWhenOnStop.booleanValue();
                if (!this.isEmptyData && HotUseCase.this.mUpStreamDisposable != null && reBindWhenOnStart()) {
                    z = true;
                }
                if (z) {
                    HotUseCase.this.mUpStreamDisposable.dispose();
                }
                this.onReturnValueWhenOnStop = Boolean.FALSE;
                HotUseCase hotUseCase = HotUseCase.this;
                hotUseCase.addDisposable((Disposable) (z ? getWrapperFlowable(hotUseCase.buildUseCaseFlowable(hotUseCase.getParam())).subscribeOn(Schedulers.from(HotUseCase.this.mSubScribeThreadExecutor)) : getWrapperLastFlowable()).observeOn(HotUseCase.this.mObserveThreadExecutor.getScheduler(), true).subscribeWith(new DisposableSubscriber<T>() { // from class: com.miui.gallery.base_optimization.clean.HotUseCase.HotModeFunction.3
                    @Override // org.reactivestreams.Subscriber
                    public void onNext(T t) {
                        if (!HotModeFunction.this.isEmptyData || !HotModeFunction.this.isFirstData) {
                            HotModeFunction.this.internalDispatchOnNextEvent(t);
                        } else {
                            HotModeFunction.this.isFirstData = false;
                        }
                    }

                    @Override // org.reactivestreams.Subscriber
                    public void onError(Throwable th) {
                        HotModeFunction.this.internalDispatchOnErrorEvent(th);
                    }

                    @Override // org.reactivestreams.Subscriber
                    public void onComplete() {
                        HotModeFunction.this.internalDispatchOnCompleteEvent();
                    }
                }));
            }
        }

        public final void internalDispatchOnCompleteEvent() {
            DisposableSubscriber<T> disposableSubscriber = HotUseCase.this.mLastSubscribe;
            if (disposableSubscriber != null) {
                disposableSubscriber.onComplete();
            }
        }

        public final void internalDispatchOnErrorEvent(Throwable th) {
            DisposableSubscriber<T> disposableSubscriber = HotUseCase.this.mLastSubscribe;
            if (disposableSubscriber != null) {
                disposableSubscriber.onError(th);
            }
        }

        public final void internalDispatchOnNextEvent(T t) {
            DisposableSubscriber<T> disposableSubscriber = HotUseCase.this.mLastSubscribe;
            if (disposableSubscriber == null || t == null) {
                return;
            }
            disposableSubscriber.onNext(t);
        }
    }

    @Override // com.miui.gallery.base_optimization.clean.LifecycleUseCase
    public void onStop() {
        super.onStop();
        HotUseCase<T, Param>.BaseFunction baseFunction = this.mTransform;
        if (baseFunction != null && baseFunction.isStop()) {
            this.isNeedReStartStream = false;
            return;
        }
        for (Subscription subscription : this.mSubscriptions) {
            subscription.cancel();
        }
        this.mSubscriptions.clear();
    }

    @Override // com.miui.gallery.base_optimization.clean.LifecycleUseCase
    public void onStart() {
        super.onStart();
        if (!this.isNeedReStartStream) {
            this.isNeedReStartStream = true;
            return;
        }
        HotUseCase<T, Param>.BaseFunction baseFunction = this.mTransform;
        if (baseFunction == null) {
            return;
        }
        baseFunction.onStart();
    }

    @Override // com.miui.gallery.base_optimization.clean.LifecycleUseCase
    public void onDestroy() {
        Disposable disposable = this.mUpStreamDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        dispose();
        this.mLastSubscribe = null;
        this.mSubscriptions = null;
        this.mUpStreamDisposable = null;
        this.mLastFlowable = null;
        this.mTransform = null;
    }
}
