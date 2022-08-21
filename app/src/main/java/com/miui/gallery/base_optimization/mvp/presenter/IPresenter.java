package com.miui.gallery.base_optimization.mvp.presenter;

import com.miui.gallery.base_optimization.mvp.view.IView;

/* loaded from: classes.dex */
public interface IPresenter<V extends IView> {
    void onAttachView(V v);

    void onDetachView();

    void onStop();
}
