package com.miui.gallery.ui.photoPage.menufilter;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.ui.photoPage.menufilter.extra.ExtraParams;
import com.miui.gallery.ui.photoPage.menufilter.mimeType.image.GifFilter;
import com.miui.gallery.ui.photoPage.menufilter.mimeType.image.ImageFilter;
import com.miui.gallery.ui.photoPage.menufilter.mimeType.image.RawFilter;
import com.miui.gallery.ui.photoPage.menufilter.mimeType.video.VideoFilter;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class MenuFilterController {

    /* loaded from: classes2.dex */
    public interface IConfigFilter {
        void filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, IMenuItemDelegate> concurrentHashMap);
    }

    /* loaded from: classes2.dex */
    public interface IEnterFilter {
        IItemTypeFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem);
    }

    /* loaded from: classes2.dex */
    public interface IExtraFilter {
        void filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem, ExtraParams extraParams);
    }

    /* loaded from: classes2.dex */
    public interface IItemTypeFilter {
        IExtraFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem);
    }

    /* loaded from: classes2.dex */
    public interface IMimeTypeFilter {
        IEnterFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem, EnterTypeUtils.EnterType enterType);
    }

    public void filterByConfig(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, IMenuItemDelegate> concurrentHashMap) {
        FilterFactory.getConfigFilter().filter(concurrentHashMap);
    }

    public void filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, EnterTypeUtils.EnterType enterType, BaseDataItem baseDataItem, ExtraParams extraParams) {
        if (concurrentHashMap == null || concurrentHashMap.isEmpty() || baseDataItem == null || enterType == null) {
            return;
        }
        IMimeTypeFilter mimeTypeFilter = getMimeTypeFilter(baseDataItem);
        DefaultLogger.d("PhotoPageFragment_MenuManager_Filter", "filter start => %d", Long.valueOf(baseDataItem.getKey()));
        mimeTypeFilter.filter(concurrentHashMap, baseDataItem, enterType).filter(concurrentHashMap, baseDataItem).filter(concurrentHashMap, baseDataItem).filter(concurrentHashMap, baseDataItem, extraParams);
        DefaultLogger.d("PhotoPageFragment_MenuManager_Filter", "filter result => %s", concurrentHashMap.toString());
    }

    public final IMimeTypeFilter getMimeTypeFilter(BaseDataItem baseDataItem) {
        if (baseDataItem.isGif()) {
            return new GifFilter();
        }
        if (baseDataItem.isRaw()) {
            return new RawFilter();
        }
        if (baseDataItem.isVideo()) {
            return new VideoFilter();
        }
        return new ImageFilter();
    }
}
