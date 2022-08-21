package com.miui.gallery.ui.photoPage.bars;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.DownloadOriginal;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;

/* loaded from: classes2.dex */
public interface IPhotoPageMenuItemManager {
    void downloadOrigin(BaseDataItem baseDataItem, DownloadOriginal.DownloadCallback downloadCallback);

    PhotoPageMenuManager.IMenuOwner getMenuOwner();

    void initFunction(PhotoPageMenuManager.MenuItemType menuItemType);

    boolean isFavorite();

    boolean isNeedDownloadOriginal();

    boolean isNeedProjectEnter();

    boolean isSupportPhotoRename();

    void onProjectedClicked();

    void refreshMenuItem(IMenuItemDelegate iMenuItemDelegate);

    void refreshNonResidentData(IMenuItemDelegate iMenuItemDelegate, boolean z);

    void refreshProjectState();

    void requestFocus(IMenuItemDelegate iMenuItemDelegate);

    void toggleMoreActions(boolean z);
}
