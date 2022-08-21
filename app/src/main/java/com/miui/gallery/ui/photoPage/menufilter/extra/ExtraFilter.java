package com.miui.gallery.ui.photoPage.menufilter.extra;

import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class ExtraFilter implements MenuFilterController.IExtraFilter {
    public final boolean isUnSupportedOptions(long j, int i) {
        return (j & ((long) i)) == 0;
    }

    @Override // com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IExtraFilter
    public void filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem, ExtraParams extraParams) {
        FilterResult filterResult;
        if (extraParams == null) {
            return;
        }
        if (!extraParams.isPhotoRenameAble() && (filterResult = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PHOTO_RENAME)) != null) {
            filterResult.setSupport(false);
        }
        filterByMask(concurrentHashMap, extraParams.getOperationMask());
        if (!extraParams.isStartWhenLockedAndSecret()) {
            return;
        }
        FilterResult filterResult2 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SEND);
        if (filterResult2 != null) {
            filterResult2.setSupport(false);
        }
        FilterResult filterResult3 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.EDIT);
        if (filterResult3 != null) {
            filterResult3.setSupport(false);
        }
        FilterResult filterResult4 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SAVE);
        if (filterResult4 != null) {
            filterResult4.setSupport(false);
        }
        FilterResult filterResult5 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_WALLPAPER);
        if (filterResult5 != null) {
            filterResult5.setSupport(false);
        }
        FilterResult filterResult6 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.OCR);
        if (filterResult6 != null) {
            filterResult6.setSupport(false);
        }
        FilterResult filterResult7 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.TO_PDF);
        if (filterResult7 != null) {
            filterResult7.setSupport(false);
        }
        FilterResult filterResult8 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_SLIDE_WALLPAPER);
        if (filterResult8 != null) {
            filterResult8.setSupport(false);
        }
        FilterResult filterResult9 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.ADD_CLOUD);
        if (filterResult9 != null) {
            filterResult9.setSupport(false);
        }
        FilterResult filterResult10 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PLAY_SLIDESHOW);
        if (filterResult10 != null) {
            filterResult10.setSupport(false);
        }
        FilterResult filterResult11 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.REMOVE_SECRET);
        if (filterResult11 != null) {
            filterResult11.setSupport(false);
        }
        FilterResult filterResult12 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.VIDEO_COMPRESS);
        if (filterResult12 != null) {
            filterResult12.setSupport(false);
        }
        FilterResult filterResult13 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PHOTO_RENAME);
        if (filterResult13 != null) {
            filterResult13.setSupport(false);
        }
        FilterResult filterResult14 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.CORRECT_DOC);
        if (filterResult14 != null) {
            filterResult14.setSupport(false);
        }
        FilterResult filterResult15 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.WATERMARK);
        if (filterResult15 != null) {
            filterResult15.setSupport(false);
        }
        FilterResult filterResult16 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.CAST);
        if (filterResult16 != null) {
            filterResult16.setSupport(false);
        }
        FilterResult filterResult17 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.GOOGLE_LENS);
        if (filterResult17 == null) {
            return;
        }
        filterResult17.setSupport(false);
    }

    public final void filterByMask(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, long j) {
        FilterResult filterResult;
        FilterResult filterResult2;
        FilterResult filterResult3;
        FilterResult filterResult4;
        FilterResult filterResult5;
        FilterResult filterResult6;
        FilterResult filterResult7;
        FilterResult filterResult8;
        FilterResult filterResult9;
        FilterResult filterResult10;
        FilterResult filterResult11;
        FilterResult filterResult12;
        FilterResult filterResult13;
        FilterResult filterResult14;
        FilterResult filterResult15;
        FilterResult filterResult16;
        if (isUnSupportedOptions(j, 4) && (filterResult16 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SEND)) != null) {
            filterResult16.setSupport(false);
        }
        if (isUnSupportedOptions(j, 512)) {
            FilterResult filterResult17 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.EDIT);
            if (filterResult17 != null) {
                filterResult17.setSupport(false);
            }
            FilterResult filterResult18 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.CORRECT_DOC);
            if (filterResult18 != null) {
                filterResult18.setSupport(false);
            }
            FilterResult filterResult19 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.WATERMARK);
            if (filterResult19 != null) {
                filterResult19.setSupport(false);
            }
        }
        if (isUnSupportedOptions(j, nexEngine.ExportHEVCMainTierLevel6) && (filterResult15 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.FAVORITE)) != null) {
            filterResult15.setSupport(false);
        }
        if (isUnSupportedOptions(j, 1) && (filterResult14 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.DELETE)) != null) {
            filterResult14.setSupport(false);
        }
        if (isUnSupportedOptions(j, 32) && (filterResult13 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_WALLPAPER)) != null) {
            filterResult13.setSupport(false);
        }
        if (isUnSupportedOptions(j, 256) && (filterResult12 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.DOWNLOAD_ORIGINAL)) != null) {
            filterResult12.setSupport(false);
        }
        if (isUnSupportedOptions(j, 2048) && (filterResult11 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_SLIDE_WALLPAPER)) != null) {
            filterResult11.setSupport(false);
        }
        if (isUnSupportedOptions(j, 4096) && (filterResult10 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_VIDEO_WALLPAPER)) != null) {
            filterResult10.setSupport(false);
        }
        if (isUnSupportedOptions(j, 16384) && (filterResult9 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PLAY_SLIDESHOW)) != null) {
            filterResult9.setSupport(false);
        }
        if (isUnSupportedOptions(j, 32768) && (filterResult8 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SAVE)) != null) {
            filterResult8.setSupport(false);
        }
        if (isUnSupportedOptions(j, 65536) && (filterResult7 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.ADD_CLOUD)) != null) {
            filterResult7.setSupport(false);
        }
        if (isUnSupportedOptions(j, nexEngine.ExportHEVCMainTierLevel52) && (filterResult6 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.ADD_CLOUD)) != null) {
            filterResult6.setSupportAddSecret(false);
        }
        if (isUnSupportedOptions(j, nexEngine.ExportHEVCHighTierLevel52) && (filterResult5 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.REMOVE_SECRET)) != null) {
            filterResult5.setSupport(false);
        }
        if (isUnSupportedOptions(j, nexEngine.ExportHEVCHighTierLevel6) && (filterResult4 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.DETAILS)) != null) {
            filterResult4.setSupport(false);
        }
        if (isUnSupportedOptions(j, nexEngine.ExportHEVCMainTierLevel61) && (filterResult3 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.OCR)) != null) {
            filterResult3.setSupport(false);
        }
        if (isUnSupportedOptions(j, nexEngine.ExportHEVCHighTierLevel61) && (filterResult2 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PHOTO_RENAME)) != null) {
            filterResult2.setSupport(false);
        }
        if (!isUnSupportedOptions(j, nexEngine.ExportHEVCMainTierLevel62) || (filterResult = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.TO_PDF)) == null) {
            return;
        }
        filterResult.setSupport(false);
    }
}
