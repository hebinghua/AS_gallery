package com.miui.gallery.ui.photoPage.menufilter.config;

import android.content.Context;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.Config$SecretAlbumConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.request.OCRRequestHelper;
import com.miui.gallery.ui.photoPage.bars.PhotoPageMenuManager;
import com.miui.gallery.ui.photoPage.bars.menuitem.AddCloud;
import com.miui.gallery.ui.photoPage.bars.menuitem.IMenuItemDelegate;
import com.miui.gallery.ui.photoPage.menufilter.MenuFilterController;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.VideoWallpaperUtils;
import com.miui.gallery.util.WallpaperUtils;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.compress.VideoCompressCheckHelper;
import com.miui.os.Rom;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class CommonConfigFilter implements MenuFilterController.IConfigFilter {
    @Override // com.miui.gallery.ui.photoPage.menufilter.MenuFilterController.IConfigFilter
    public void filter(ConcurrentHashMap<PhotoPageMenuManager.MenuItemType, IMenuItemDelegate> concurrentHashMap) {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        if (!VideoCompressCheckHelper.isDeviceSupport()) {
            concurrentHashMap.remove(PhotoPageMenuManager.MenuItemType.VIDEO_COMPRESS);
        }
        if (!WallpaperUtils.supported(sGetAndroidContext)) {
            concurrentHashMap.remove(PhotoPageMenuManager.MenuItemType.SET_WALLPAPER);
        }
        boolean z = Rom.IS_INTERNATIONAL;
        if (z || !Rom.IS_MIUI || BaseBuildUtil.isPad() || BaseBuildUtil.isFoldableDevice()) {
            concurrentHashMap.remove(PhotoPageMenuManager.MenuItemType.SET_SLIDE_WALLPAPER);
        }
        if (!VideoWallpaperUtils.isSupported()) {
            concurrentHashMap.remove(PhotoPageMenuManager.MenuItemType.SET_VIDEO_WALLPAPER);
        }
        if (!OCRRequestHelper.SUPPORT_OCR.get(sGetAndroidContext).booleanValue()) {
            concurrentHashMap.remove(PhotoPageMenuManager.MenuItemType.OCR);
        }
        if (!ApplicationHelper.isSecretAlbumFeatureOpen() || !Config$SecretAlbumConfig.isVideoSupported()) {
            IMenuItemDelegate iMenuItemDelegate = concurrentHashMap.get(PhotoPageMenuManager.MenuItemType.ADD_CLOUD);
            if (iMenuItemDelegate instanceof AddCloud) {
                ((AddCloud) iMenuItemDelegate).setIsSupportAddSecret(false);
            }
        }
        if (!ExtraPhotoSDK.isDeviceSupportCorrectDocument(GalleryApp.sGetAndroidContext())) {
            concurrentHashMap.remove(PhotoPageMenuManager.MenuItemType.CORRECT_DOC);
        }
        if (!z) {
            concurrentHashMap.remove(PhotoPageMenuManager.MenuItemType.GOOGLE_LENS);
        }
        DefaultLogger.d("PhotoPageFragment_MenuManager_Filter", "CommonConfigFilter filter map.size=" + concurrentHashMap.size());
    }
}
