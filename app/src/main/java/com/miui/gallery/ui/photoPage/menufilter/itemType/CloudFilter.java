package com.miui.gallery.ui.photoPage.menufilter.itemType;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class CloudFilter extends BaseItemTypeFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.itemType.BaseItemTypeFilter, com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IItemTypeFilter
    public MenuFilterController.IExtraFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem) {
        FilterResult filterResult;
        MenuFilterController.IExtraFilter filter = super.filter(concurrentHashMap, baseDataItem);
        CloudItem cloudItem = (CloudItem) baseDataItem;
        FilterResult filterResult2 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.DOWNLOAD_ORIGINAL);
        if (filterResult2 != null) {
            filterResult2.setSupport(cloudItem.needDownloadOrigin());
        }
        if (cloudItem.isShare()) {
            FilterResult filterResult3 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.EDIT);
            if (filterResult3 != null) {
                filterResult3.setSupport(false);
            }
            FilterResult filterResult4 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.FAVORITE);
            if (filterResult4 != null) {
                filterResult4.setSupport(false);
            }
            FilterResult filterResult5 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.DELETE);
            if (filterResult5 != null) {
                filterResult5.setSupport(cloudItem.canDelete());
            }
            FilterResult filterResult6 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.ADD_CLOUD);
            if (filterResult6 != null) {
                filterResult6.setSupportAddSecret(false);
            }
            FilterResult filterResult7 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.REMOVE_SECRET);
            if (filterResult7 != null) {
                filterResult7.setSupport(false);
            }
            FilterResult filterResult8 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.WATERMARK);
            if (filterResult8 != null) {
                filterResult8.setSupport(false);
            }
            if (cloudItem.isVideo() && (filterResult = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.VIDEO_COMPRESS)) != null) {
                filterResult.setSupport(false);
            }
        }
        if (cloudItem.isSecret()) {
            if (cloudItem.isVideo()) {
                FilterResult filterResult9 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.EDIT);
                if (filterResult9 != null) {
                    filterResult9.setSupport(false);
                }
                FilterResult filterResult10 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_VIDEO_WALLPAPER);
                if (filterResult10 != null) {
                    filterResult10.setSupport(false);
                }
                FilterResult filterResult11 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.VIDEO_COMPRESS);
                if (filterResult11 != null) {
                    filterResult11.setSupport(false);
                }
            }
            FilterResult filterResult12 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.FAVORITE);
            if (filterResult12 != null) {
                filterResult12.setSupport(false);
            }
            FilterResult filterResult13 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.ADD_CLOUD);
            if (filterResult13 != null) {
                filterResult13.setSupportAddSecret(false);
                filterResult13.setSupport(false);
            }
            FilterResult filterResult14 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_WALLPAPER);
            if (filterResult14 != null) {
                filterResult14.setSupport(false);
            }
            FilterResult filterResult15 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_SLIDE_WALLPAPER);
            if (filterResult15 != null) {
                filterResult15.setSupport(false);
            }
            FilterResult filterResult16 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.CORRECT_DOC);
            if (filterResult16 != null) {
                filterResult16.setSupport(false);
            }
            FilterResult filterResult17 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.TO_PDF);
            if (filterResult17 != null) {
                filterResult17.setSupport(false);
            }
            FilterResult filterResult18 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.REMOVE_SECRET);
            if (filterResult18 != null) {
                filterResult18.setSupport(cloudItem.isSecret());
            }
        }
        return filter;
    }
}
