package com.miui.gallery.ui.album.main;

import com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter;
import com.miui.gallery.ui.album.main.usecase.DoChangeSortPositionCase;
import com.miui.itemdrag.RecyclerViewDragItemManager;
import io.reactivex.subscribers.DisposableSubscriber;

/* loaded from: classes2.dex */
public abstract class AlbumTabContract$P extends BaseAlbumTabPresenter<AlbumTabContract$V> {
    public abstract void doChangeDataPendingStatus(boolean z);

    public abstract void doChangeSortPosition(DoChangeSortPositionCase.Param param, DisposableSubscriber<Boolean> disposableSubscriber);

    public abstract RecyclerViewDragItemManager.OnDragCallback getDragItemTouchCallback();

    public abstract void onStartChoiceMode();

    public abstract void onStopChoiceMode();
}
