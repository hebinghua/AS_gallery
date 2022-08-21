package com.miui.gallery.ui.photoPage.menufilter.mimeType.image;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class ImageFilter extends BaseImageFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.mimeType.image.BaseImageFilter, com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IMimeTypeFilter
    public MenuFilterController.IEnterFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem, EnterTypeUtils.EnterType enterType) {
        return super.filter(concurrentHashMap, baseDataItem, enterType);
    }
}
