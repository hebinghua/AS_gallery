package com.miui.gallery.ui.photoPage.menufilter.mimeType.image;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class GifFilter extends BaseImageFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.mimeType.image.BaseImageFilter, com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IMimeTypeFilter
    public MenuFilterController.IEnterFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem, EnterTypeUtils.EnterType enterType) {
        MenuFilterController.IEnterFilter filter = super.filter(concurrentHashMap, baseDataItem, enterType);
        FilterResult filterResult = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.EDIT);
        if (filterResult != null) {
            filterResult.setEnable(false);
        }
        FilterResult filterResult2 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_WALLPAPER);
        if (filterResult2 != null) {
            filterResult2.setSupport(false);
        }
        FilterResult filterResult3 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_SLIDE_WALLPAPER);
        if (filterResult3 != null) {
            filterResult3.setSupport(false);
        }
        FilterResult filterResult4 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.OCR);
        if (filterResult4 != null) {
            filterResult4.setSupport(false);
        }
        return filter;
    }
}
