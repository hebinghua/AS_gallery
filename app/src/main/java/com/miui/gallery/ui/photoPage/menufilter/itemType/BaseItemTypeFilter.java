package com.miui.gallery.ui.photoPage.menufilter.itemType;

import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.request.PicToPdfHelper;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.FilterFactory;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public abstract class BaseItemTypeFilter implements MenuFilterController.IItemTypeFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IItemTypeFilter
    public MenuFilterController.IExtraFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem) {
        FilterResult filterResult;
        FilterResult filterResult2;
        FilterResult filterResult3;
        FilterResult filterResult4;
        FilterResult filterResult5;
        boolean isShareFolderRelativePath = DownloadPathHelper.isShareFolderRelativePath(StorageUtils.getRelativePath(GalleryApp.sGetAndroidContext(), baseDataItem.getPathDisplayBetter()));
        if (isShareFolderRelativePath && (filterResult5 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.EDIT)) != null) {
            filterResult5.setEnable(false);
        }
        if (TextUtils.isEmpty(baseDataItem.getOriginalPath()) && (filterResult4 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.VIDEO_COMPRESS)) != null) {
            filterResult4.setSupport(false);
        }
        if ((baseDataItem.isBurstItem() || baseDataItem.isSecret() || (StringUtils.isEmpty(baseDataItem.getOriginalPath()) && StringUtils.isEmpty(baseDataItem.getThumnailPath()))) && (filterResult3 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PHOTO_RENAME)) != null) {
            DefaultLogger.fd("PhotoPageFragment_MenuManager_Filter", "item isBurstItem or isSecret or path is empty. not support rename!");
            filterResult3.setSupport(false);
        }
        if ((baseDataItem.isDocPhoto() || isShareFolderRelativePath) && (filterResult = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.CORRECT_DOC)) != null) {
            filterResult.setSupport(false);
        }
        if ((!PicToPdfHelper.isPicToPdfSupport() || !PicToPdfHelper.isPicToPdfSupportType(baseDataItem.getMimeType())) && (filterResult2 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.TO_PDF)) != null) {
            filterResult2.setSupport(false);
        }
        return FilterFactory.getExtraFilter();
    }
}
