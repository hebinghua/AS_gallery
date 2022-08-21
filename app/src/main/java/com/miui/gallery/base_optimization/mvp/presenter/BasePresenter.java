package com.miui.gallery.base_optimization.mvp.presenter;

import com.miui.gallery.base_optimization.clean.UseCase;
import com.miui.gallery.base_optimization.mvp.view.IView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/* loaded from: classes.dex */
public class BasePresenter<V extends IView> implements IPresenter<V> {
    private CompositeDisposable mCompositeDisposable;
    private CompositeDisposable mDelayCompositeDisposable;
    private V mView;

    @Override // com.miui.gallery.base_optimization.mvp.presenter.IPresenter
    public void onAttachView(V v) {
        if (v == null) {
            throw new IllegalArgumentException("view can't null");
        }
        this.mView = v;
    }

    public void onStart() {
        this.mCompositeDisposable = new CompositeDisposable();
    }

    @Override // com.miui.gallery.base_optimization.mvp.presenter.IPresenter
    public void onStop() {
        CompositeDisposable compositeDisposable = this.mCompositeDisposable;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        this.mCompositeDisposable = null;
    }

    @Override // com.miui.gallery.base_optimization.mvp.presenter.IPresenter
    public void onDetachView() {
        onDestroy();
        this.mView = null;
    }

    public void onDestroy() {
        CompositeDisposable compositeDisposable = this.mDelayCompositeDisposable;
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        this.mDelayCompositeDisposable = null;
    }

    public V getView() {
        return this.mView;
    }

    public void addDispose(Disposable disposable) {
        if (this.mCompositeDisposable == null) {
            this.mCompositeDisposable = new CompositeDisposable();
        }
        this.mCompositeDisposable.add(disposable);
    }

    public void addDisposeDelay(Disposable disposable) {
        if (this.mDelayCompositeDisposable == null) {
            this.mDelayCompositeDisposable = new CompositeDisposable();
        }
        this.mDelayCompositeDisposable.add(disposable);
    }

    @Deprecated
    public void dispose(UseCase... useCaseArr) {
        if (useCaseArr == null || useCaseArr.length <= 0) {
            return;
        }
        for (UseCase useCase : useCaseArr) {
            if (useCase != null) {
                useCase.dispose();
            }
        }
    }
}
