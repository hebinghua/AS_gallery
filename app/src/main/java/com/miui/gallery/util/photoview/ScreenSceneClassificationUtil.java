package com.miui.gallery.util.photoview;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.provider.Settings;
import com.miui.core.SdkHelper;
import com.miui.display.DisplayFeatureHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ScreenSceneClassificationUtil {
    public static boolean sHasRegistered = false;
    public static LazyValue<Void, Boolean> sIsSettingEnableScreenScene = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.photoview.ScreenSceneClassificationUtil.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r6) {
            ContentResolver contentResolver = GalleryApp.sGetAndroidContext().getContentResolver();
            String string = Settings.Global.getString(contentResolver, "screen_enhance_engine_gallery_ai_mode_status");
            if (string != null && !ScreenSceneClassificationUtil.sHasRegistered) {
                contentResolver.registerContentObserver(Settings.Global.getUriFor("screen_enhance_engine_gallery_ai_mode_status"), false, ScreenSceneClassificationUtil.sSettingObserver);
                boolean unused = ScreenSceneClassificationUtil.sHasRegistered = true;
                boolean equals = string.equals("true");
                DefaultLogger.d("ScreenSceneClassificationUtil", "sIsSettingEnableScreenScene %s", Boolean.valueOf(equals));
                return Boolean.valueOf(equals);
            }
            DefaultLogger.d("ScreenSceneClassificationUtil", "sIsSettingEnableScreenScene false");
            return Boolean.FALSE;
        }
    };
    public static LazyValue<Void, Boolean> sIsCloudControlEnableScreenScene = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.util.photoview.ScreenSceneClassificationUtil.2
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r4) {
            boolean equals = FeatureProfile.Status.ENABLE.equals(CloudControlManager.getInstance().queryFeatureStatus("AImode"));
            DefaultLogger.d("ScreenSceneClassificationUtil", "sIsCloudControlEnableScreenScene %s", Boolean.valueOf(equals));
            return Boolean.valueOf(equals);
        }
    };
    public static ContentObserver sSettingObserver = new ContentObserver(ThreadManager.getMainHandler()) { // from class: com.miui.gallery.util.photoview.ScreenSceneClassificationUtil.3
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            DefaultLogger.d("ScreenSceneClassificationUtil", "setting changed %s", Boolean.valueOf(z));
            super.onChange(z);
            ScreenSceneClassificationUtil.sIsSettingEnableScreenScene.reset();
        }
    };

    public static boolean isScreenSceneClassifyEnable() {
        return ScreenUtils.isDeviceSupportAIMode() && sIsCloudControlEnableScreenScene.get(null).booleanValue();
    }

    public static void setScreenSceneClassification(int i) {
        if (!SdkHelper.IS_MIUI || !ScreenUtils.isUseScreenSceneMode()) {
            return;
        }
        DisplayFeatureHelper.setScreenSceneClassification(i);
    }

    public static void clearScreenSceneClassificationMode() {
        setScreenSceneClassification(0);
    }
}
