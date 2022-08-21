package com.miui.gallery.ui.photoPage.menufilter.itemType;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class TrashFilter extends BaseItemTypeFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.itemType.BaseItemTypeFilter, com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IItemTypeFilter
    public MenuFilterController.IExtraFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem) {
        MenuFilterController.IExtraFilter filter = super.filter(concurrentHashMap, baseDataItem);
        concurrentHashMap.forEach(TrashFilter$$ExternalSyntheticLambda0.INSTANCE);
        FilterResult filterResult = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PURGE);
        if (filterResult != null) {
            filterResult.setSupport(true);
        }
        FilterResult filterResult2 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.RECOVERY);
        if (filterResult2 != null) {
            filterResult2.setSupport(true);
        }
        return filter;
    }
}
