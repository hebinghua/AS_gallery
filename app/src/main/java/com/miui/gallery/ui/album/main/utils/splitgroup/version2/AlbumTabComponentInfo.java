package com.miui.gallery.ui.album.main.utils.splitgroup.version2;

import com.miui.gallery.ui.album.main.AlbumTabContract$P;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$P;
import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.gallery.ui.album.main.utils.splitgroup.IAlbumPageComponentVersion;
import com.miui.gallery.ui.album.main.utils.splitgroup.IGroupSettingInfo;
import com.miui.gallery.ui.album.main.utils.splitgroup.ISplitGroupMode;
import com.miui.itemdrag.RecyclerViewDragItemManager;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class AlbumTabComponentInfo<P extends BaseAlbumTabContract$P> implements IAlbumPageComponentVersion {
    public WeakReference<P> mPresenterRef;

    public String toString() {
        return "AlbumTabComponentVersion2";
    }

    public AlbumTabComponentInfo(P p) {
        this.mPresenterRef = null;
        if (p != null) {
            this.mPresenterRef = new WeakReference<>(p);
            p.setEnableAlbumById(2147483638, false);
        }
    }

    @Override // com.miui.gallery.ui.album.main.utils.splitgroup.IAlbumPageComponentVersion
    public ISplitGroupMode getSplitGroupMode() {
        return new AlbumSplitModeImpl();
    }

    @Override // com.miui.gallery.ui.album.main.utils.splitgroup.IAlbumPageComponentVersion
    public RecyclerViewDragItemManager.OnDragCallback getDragItemTouchCallback() {
        WeakReference<P> weakReference = this.mPresenterRef;
        if (weakReference != null && !(weakReference.get() instanceof AlbumTabContract$P)) {
            throw new IllegalStateException("only support AlbumTabPresenter!");
        }
        return new AlbumTabDragImpl(this.mPresenterRef);
    }

    @Override // com.miui.gallery.ui.album.main.utils.splitgroup.IAlbumPageComponentVersion
    public AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback getQueryAllAlbumsLoadComplateListener() {
        WeakReference<P> weakReference = this.mPresenterRef;
        if (weakReference == null || weakReference.get() == null) {
            throw new IllegalArgumentException("need presenter!");
        }
        return new AlbumTabDataProcessingCallback(this.mPresenterRef, getGroupSettingInfo());
    }

    @Override // com.miui.gallery.ui.album.main.utils.splitgroup.IAlbumPageComponentVersion
    public IGroupSettingInfo getGroupSettingInfo() {
        return new GroupSettingInfo();
    }
}
