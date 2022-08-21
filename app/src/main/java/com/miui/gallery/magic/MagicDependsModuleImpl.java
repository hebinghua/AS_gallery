package com.miui.gallery.magic;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.imodule.base.IModule;
import com.miui.gallery.imodule.modules.MagicDependsModule;
import com.miui.gallery.net.library.LibraryStrategyUtils;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.util.market.MediaEditorInstaller;
import com.miui.gallery.vlog.rule.Util;
import com.miui.gallery.vlog.rule.VideoInfo;

/* loaded from: classes2.dex */
public class MagicDependsModuleImpl implements MagicDependsModule, IModule {
    @Override // com.miui.gallery.imodule.modules.MagicDependsModule
    public String getFileProviderAuthority() {
        return "com.miui.gallery.file-provider";
    }

    @Override // com.miui.gallery.imodule.modules.MagicDependsModule
    public Context getAndroidContext() {
        return GalleryApp.sGetAndroidContext();
    }

    @Override // com.miui.gallery.imodule.modules.MagicDependsModule
    public boolean is8KVideo(String str) {
        VideoInfo extractVideoInfo = Util.extractVideoInfo(str);
        if (extractVideoInfo.getWidth() <= 0) {
            return true;
        }
        return Util.is8KVideo(str, extractVideoInfo.getWidth(), extractVideoInfo.getHeight());
    }

    @Override // com.miui.gallery.imodule.modules.MagicDependsModule
    public void scanSingleFile(Context context, String str) {
        ScannerEngine.getInstance().scanFile(context, str, 13);
    }

    @Override // com.miui.gallery.imodule.modules.MagicDependsModule
    public boolean is8450() {
        return LibraryStrategyUtils.is8450();
    }

    @Override // com.miui.gallery.imodule.modules.MagicDependsModule
    public void removeInstallListener() {
        MediaEditorInstaller.getInstance().removeInstallListener();
    }

    @Override // com.miui.gallery.imodule.modules.MagicDependsModule
    public boolean isInFreeFormWindow(Context context) {
        return ActivityCompat.isInFreeFormWindow(context);
    }

    @Override // com.miui.gallery.imodule.modules.MagicDependsModule
    public boolean installIfNotExist(FragmentActivity fragmentActivity, final MagicDependsModule.Callback callback, boolean z) {
        return MediaEditorInstaller.getInstance().installIfNotExist(fragmentActivity, callback != null ? new MediaEditorInstaller.Callback() { // from class: com.miui.gallery.magic.MagicDependsModuleImpl.1
            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onDialogConfirm() {
                MagicDependsModule.Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onDialogConfirm();
                }
            }

            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onDialogCancel() {
                MagicDependsModule.Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onDialogCancel();
                }
            }

            @Override // com.miui.gallery.util.market.MediaEditorInstaller.Callback
            public void onInstallSuccess() {
                MagicDependsModule.Callback callback2 = callback;
                if (callback2 != null) {
                    callback2.onInstallSuccess();
                }
            }
        } : null, z);
    }
}
