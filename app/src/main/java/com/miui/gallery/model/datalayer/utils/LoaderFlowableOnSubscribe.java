package com.miui.gallery.model.datalayer.utils;

import androidx.loader.content.Loader;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Cancellable;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public abstract class LoaderFlowableOnSubscribe<T, R> implements FlowableOnSubscribe<R> {
    public FlowableEmitter<R> mEmitter;
    public Loader<T> mLoader;
    public final int MODE_SELF_THREAD = 1;
    public final int MODE_MAIN_THREAD = 2;
    public final int MODE_DEFAULT = 1;
    public Loader.OnLoadCompleteListener mCompleteListener = new Loader.OnLoadCompleteListener<T>() { // from class: com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe.1
        @Override // androidx.loader.content.Loader.OnLoadCompleteListener
        public void onLoadComplete(Loader<T> loader, final T t) {
            if (LoaderFlowableOnSubscribe.this.callbackThreadMode() == 2) {
                ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe.1.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.lang.Runnable
                    public void run() {
                        LoaderFlowableOnSubscribe loaderFlowableOnSubscribe = LoaderFlowableOnSubscribe.this;
                        loaderFlowableOnSubscribe.subscribe(loaderFlowableOnSubscribe.mEmitter, t);
                    }
                });
                return;
            }
            LoaderFlowableOnSubscribe loaderFlowableOnSubscribe = LoaderFlowableOnSubscribe.this;
            loaderFlowableOnSubscribe.subscribe(loaderFlowableOnSubscribe.mEmitter, t);
        }
    };

    public int callbackThreadMode() {
        return 1;
    }

    public abstract Loader<T> getLoader();

    public boolean saveNextValue() {
        return false;
    }

    public abstract void subscribe(FlowableEmitter<R> flowableEmitter, T t);

    @Override // io.reactivex.FlowableOnSubscribe
    public final void subscribe(FlowableEmitter<R> flowableEmitter) {
        try {
            internalSubscribe(wrapperEmitterIfNeed(flowableEmitter));
        } catch (Exception e) {
            DefaultLogger.e("LoaderFlowableOnSubscribe", e.getMessage());
        }
    }

    public final FlowableEmitter<R> wrapperEmitterIfNeed(FlowableEmitter<R> flowableEmitter) {
        return saveNextValue() ? new FlowableEmitterWrapper(flowableEmitter) : flowableEmitter;
    }

    public final void internalSubscribe(FlowableEmitter<R> flowableEmitter) {
        Loader<T> loader = getLoader();
        this.mLoader = loader;
        loader.registerListener(loader.getId(), this.mCompleteListener);
        this.mEmitter = flowableEmitter;
        this.mLoader.startLoading();
        this.mEmitter.setCancellable(new Cancellable() { // from class: com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe.2
            @Override // io.reactivex.functions.Cancellable
            public void cancel() throws Exception {
                LoaderFlowableOnSubscribe.this.mLoader.reset();
            }
        });
    }

    public R getPrevValue() {
        if (!saveNextValue()) {
            throw new IllegalStateException("not open saveNextValue config");
        }
        return (R) ((FlowableEmitterWrapper) this.mEmitter).getPrevValue();
    }

    /* loaded from: classes2.dex */
    public static class FlowableEmitterWrapper<R> implements FlowableEmitter<R> {
        public WeakReference<R> mNextValue;
        public FlowableEmitter<R> mSource;

        public FlowableEmitterWrapper(FlowableEmitter<R> flowableEmitter) {
            this.mSource = flowableEmitter;
        }

        @Override // io.reactivex.Emitter
        public void onNext(R r) {
            this.mSource.onNext(r);
            this.mNextValue = new WeakReference<>(r);
        }

        @Override // io.reactivex.Emitter
        public void onError(Throwable th) {
            this.mSource.onError(th);
        }

        public R getPrevValue() {
            WeakReference<R> weakReference = this.mNextValue;
            if (weakReference != null) {
                return weakReference.get();
            }
            return null;
        }

        @Override // io.reactivex.FlowableEmitter
        public void setCancellable(Cancellable cancellable) {
            this.mSource.setCancellable(cancellable);
        }
    }
}
