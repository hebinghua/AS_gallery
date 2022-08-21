package com.miui.gallery.ui.photoPage.menufilter.itemType;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class MediaFilter extends BaseItemTypeFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.itemType.BaseItemTypeFilter, com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IItemTypeFilter
    public MenuFilterController.IExtraFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem) {
        return super.filter(concurrentHashMap, baseDataItem);
    }
}
