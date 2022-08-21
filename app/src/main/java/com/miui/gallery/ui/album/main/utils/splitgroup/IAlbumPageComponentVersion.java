package com.miui.gallery.ui.album.main.utils.splitgroup;

import com.miui.gallery.ui.album.main.utils.AlbumGroupByAlbumTypeFunction;
import com.miui.itemdrag.RecyclerViewDragItemManager;

/* loaded from: classes2.dex */
public interface IAlbumPageComponentVersion {
    RecyclerViewDragItemManager.OnDragCallback getDragItemTouchCallback();

    IGroupSettingInfo getGroupSettingInfo();

    AlbumGroupByAlbumTypeFunction.OnDataProcessingCallback getQueryAllAlbumsLoadComplateListener();

    ISplitGroupMode getSplitGroupMode();
}
