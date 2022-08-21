package com.miui.gallery.ui.photoPage.menufilter.mimeType.video;

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
public class VideoFilter implements MenuFilterController.IMimeTypeFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IMimeTypeFilter
    public MenuFilterController.IEnterFilter filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, FilterResult> concurrentHashMap, BaseDataItem baseDataItem, EnterTypeUtils.EnterType enterType) {
        concurrentHashMap.forEach(VideoFilter$$ExternalSyntheticLambda0.INSTANCE);
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
            filterResult2.setTitleStr(GalleryApp.sGetAndroidContext().getResources().getString(R.string.operation_edit_video));
            filterResult2.setIconId(R.drawable.button_video_beautify_light);
        }
        FilterResult filterResult6 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.ADD_CLOUD);
        if (filterResult6 != null) {
            filterResult6.setIconId(R.drawable.action_button_addto);
            filterResult6.setSupportAddSecret(true);
        }
        String pathDisplayBetter = baseDataItem.getPathDisplayBetter();
        boolean z2 = !TextUtils.isEmpty(pathDisplayBetter) && StorageUtils.isInExternalStorage(GalleryApp.sGetAndroidContext(), pathDisplayBetter);
        if (filterResult2 != null) {
            filterResult2.setEnable(z2);
        }
        if (filterResult4 != null) {
            filterResult4.setEnable(z2);
        }
        if (filterResult6 != null) {
            filterResult6.setSupport(true);
        }
        FilterResult filterResult7 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.DETAILS);
        if (filterResult7 != null) {
            filterResult7.setSupport(true);
        }
        FilterResult filterResult8 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.PHOTO_RENAME);
        if (filterResult8 != null) {
            filterResult8.setSupport(true);
        }
        FilterResult filterResult9 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.SET_VIDEO_WALLPAPER);
        if (filterResult9 != null) {
            filterResult9.setSupport(true);
        }
        FilterResult filterResult10 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.VIDEO_COMPRESS);
        if (filterResult10 != null) {
            filterResult10.setSupport(true);
        }
        FilterResult filterResult11 = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.CAST);
        if (filterResult11 != null) {
            if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(GalleryApp.sGetAndroidContext()).booleanValue() || (baseDataItem.getOriginalPath() != null && URLUtil.isContentUrl(baseDataItem.getOriginalPath()))) {
                z = false;
            }
            filterResult11.setSupport(z);
        }
        return FilterFactory.getEnterFilter(enterType);
    }
}
