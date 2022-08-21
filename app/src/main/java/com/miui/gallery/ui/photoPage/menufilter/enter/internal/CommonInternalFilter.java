package com.miui.gallery.ui.photoPage.menufilter.enter.internal;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.FilterFactory;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class CommonInternalFilter implements MenuFilterController.IEnterFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IEnterFilter
    public MenuFilterController.IItemTypeFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem) {
        return FilterFactory.getItemTypeFilter(baseDataItem);
    }
}
