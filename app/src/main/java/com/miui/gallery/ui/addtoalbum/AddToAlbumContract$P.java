package com.miui.gallery.ui.addtoalbum;

import com.miui.gallery.base_optimization.mvp.presenter.BasePresenter;

/* loaded from: classes2.dex */
public abstract class AddToAlbumContract$P extends BasePresenter<AddToAlbumContract$V> {
    public abstract void initUsecase();

    public abstract void onInitData();

    public abstract void onRecordLastSelectedAlbum(long j);

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter, com.miui.gallery.base_optimization.mvp.presenter.IPresenter
    public void onAttachView(AddToAlbumContract$V addToAlbumContract$V) {
        super.onAttachView((AddToAlbumContract$P) addToAlbumContract$V);
        initUsecase();
    }
}
