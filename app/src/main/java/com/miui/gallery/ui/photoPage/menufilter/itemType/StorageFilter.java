package com.miui.gallery.ui.photoPage.menufilter.itemType;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.provider.cache.BurstInfo;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class StorageFilter extends BaseItemTypeFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.itemType.BaseItemTypeFilter, com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IItemTypeFilter
    public MenuFilterController.IExtraFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem) {
        MenuFilterController.IExtraFilter filter = super.filter(concurrentHashMap, baseDataItem);
        String fileTitle = BaseFileUtils.getFileTitle(baseDataItem.getPathDisplayBetter());
        String parentFolderPath = BaseFileUtils.getParentFolderPath(baseDataItem.getPathDisplayBetter());
        FilterResult filterResult = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PHOTO_RENAME);
        if (filterResult != null && (BurstInfo.maybeBurst(fileTitle) || !StorageUtils.isSupportRename(parentFolderPath))) {
            DefaultLogger.fd("PhotoPageFragment_MenuManager_Filter", "StorageFilter is Support [%b] title [%s]", Boolean.FALSE, fileTitle);
            filterResult.setSupport(false);
        }
        return filter;
    }
}
