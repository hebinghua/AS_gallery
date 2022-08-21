package com.miui.gallery.ui.photoPage.menufilter.enter.external;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class FileManagerFilter extends CommonExternalFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.enter.external.CommonExternalFilter, com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IEnterFilter
    public MenuFilterController.IItemTypeFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem) {
        MenuFilterController.IItemTypeFilter filter = super.filter(concurrentHashMap, baseDataItem);
        FilterResult filterResult = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PHOTO_RENAME);
        if (filterResult != null) {
            filterResult.setSupport(true);
        }
        return filter;
    }
}
