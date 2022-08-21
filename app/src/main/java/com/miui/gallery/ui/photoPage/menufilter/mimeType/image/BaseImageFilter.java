package com.miui.gallery.ui.photoPage.menufilter.mimeType.image;

import android.text.TextUtils;
import android.webkit.URLUtil;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.FilterResult;
import com.miui.gallery.ui.ShareStateRouter;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.menufilter.FilterFactory;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import com.miui.gallery.util.StorageUtils;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public abstract class BaseImageFilter implements MenuFilterController.IMimeTypeFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IMimeTypeFilter
    public MenuFilterController.IEnterFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem, EnterTypeUtils.EnterType enterType) {
        concurrentHashMap.forEach(BaseImageFilter$$ExternalSyntheticLambda0.INSTANCE);
        FilterResult filterResult = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SEND);
        boolean z = true;
        if (filterResult != null) {
            filterResult.setSupport(true);
        }
        FilterResult filterResult2 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.EDIT);
        if (filterResult2 != null) {
            filterResult2.setSupport(true);
        }
        FilterResult filterResult3 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.FAVORITE);
        if (filterResult3 != null) {
            filterResult3.setSupport(true);
        }
        FilterResult filterResult4 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.DELETE);
        if (filterResult4 != null) {
            filterResult4.setSupport(true);
        }
        FilterResult filterResult5 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.MORE);
        if (filterResult5 != null) {
            filterResult5.setSupport(true);
        }
        if (filterResult2 != null) {
            filterResult2.setTitleStr(GalleryApp.sGetAndroidContext().getResources().getString(R.string.operation_edit));
            filterResult2.setIconId(R.drawable.button_photo_beautify_light);
        }
        String pathDisplayBetter = baseDataItem.getPathDisplayBetter();
        boolean z2 = !TextUtils.isEmpty(pathDisplayBetter) && StorageUtils.isInExternalStorage(GalleryApp.sGetAndroidContext(), pathDisplayBetter);
        if (filterResult4 != null) {
            filterResult4.setEnable(z2);
        }
        FilterResult filterResult6 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.ADD_CLOUD);
        if (filterResult6 != null) {
            filterResult6.setIconId(R.drawable.action_button_addto);
            filterResult6.setSupportAddSecret(true);
        }
        if (filterResult6 != null) {
            filterResult6.setSupport(true);
        }
        FilterResult filterResult7 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PLAY_SLIDESHOW);
        if (filterResult7 != null) {
            filterResult7.setSupport(true);
        }
        FilterResult filterResult8 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.DETAILS);
        if (filterResult8 != null) {
            filterResult8.setSupport(true);
        }
        FilterResult filterResult9 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PHOTO_RENAME);
        if (filterResult9 != null) {
            filterResult9.setSupport(true);
        }
        FilterResult filterResult10 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_WALLPAPER);
        if (filterResult10 != null) {
            filterResult10.setSupport(true);
        }
        FilterResult filterResult11 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_SLIDE_WALLPAPER);
        if (filterResult11 != null) {
            filterResult11.setSupport(true);
        }
        FilterResult filterResult12 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.OCR);
        if (filterResult12 != null) {
            filterResult12.setSupport(true);
        }
        FilterResult filterResult13 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.CORRECT_DOC);
        if (filterResult13 != null) {
            filterResult13.setSupport(true);
        }
        FilterResult filterResult14 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.WATERMARK);
        if (filterResult14 != null) {
            filterResult14.setSupport(true);
        }
        FilterResult filterResult15 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.TO_PDF);
        if (filterResult15 != null) {
            filterResult15.setSupport(true);
        }
        FilterResult filterResult16 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.GOOGLE_LENS);
        if (filterResult16 != null) {
            filterResult16.setSupport(true);
        }
        FilterResult filterResult17 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.CAST);
        if (filterResult17 != null) {
            if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(GalleryApp.sGetAndroidContext()).booleanValue() || (baseDataItem.getOriginalPath() != null && URLUtil.isContentUrl(baseDataItem.getOriginalPath()))) {
                z = false;
            }
            filterResult17.setSupport(z);
        }
        return FilterFactory.getEnterFilter(enterType);
    }
}
