package com.miui.gallery.ui.photoPage.bars.menuitem;

import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.view.menu.IMenuItem;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class More extends BaseMenuItemDelegate {
    public boolean isClickable;

    public static More instance(IMenuItem iMenuItem) {
        return new More(iMenuItem);
    }

    public More(IMenuItem iMenuItem) {
        super(iMenuItem);
        iMenuItem.setNeedFolmeAnim(false);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate
    public void onClick(BaseDataItem baseDataItem) {
        if (!this.isFunctionInit || !this.isClickable) {
            return;
        }
        this.mMenuItemManager.toggleMoreActions(true);
        TrackController.trackClick("403.11.5.1.11163", AutoTracking.getRef());
    }

    public void onFilterFinished(BaseDataItem baseDataItem, ArrayList<IMenuItemDelegate> arrayList, ArrayList<IMenuItemDelegate> arrayList2) {
        boolean z = arrayList2 != null && arrayList2.size() > 0;
        this.isClickable = z;
        setEnable(z);
    }
}
