package com.miui.gallery.video.editor.manager;

import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.config.VideoEditorConfig;
import com.miui.gallery.video.editor.util.FileHelper;
import com.nexstreaming.nexeditorsdk.nexApplicationConfig;
import com.nexstreaming.nexeditorsdk.nexAssetPackageManager;
import com.nexstreaming.nexeditorsdk.nexColorEffect;
import com.nexstreaming.nexeditorsdk.nexOverlayPreset;
import com.nexstreaming.nexeditorsdk.nexTemplateManager;
import java.util.List;

/* loaded from: classes2.dex */
public class NexAssetTemplateManager {
    public static String TAG = "NexAssetTemplateManager";
    public static String assetInstallRootPath;
    public static String assetStoreSubDir;

    /* loaded from: classes2.dex */
    public interface ICheckExpiredAssetListener {
        void onFinished(List<nexTemplateManager.Template> list);
    }

    /* loaded from: classes2.dex */
    public interface ILoadAssetTemplate {
        void onFail();

        void onSuccess();
    }

    /* loaded from: classes2.dex */
    public interface ILoadWaterMarkListener {
        void onFinished(String[] strArr);
    }

    /* loaded from: classes2.dex */
    public static class NexTemplateManagerHolder {
        public static final NexAssetTemplateManager INSTANCE = new NexAssetTemplateManager();
    }

    public NexAssetTemplateManager() {
    }

    public static NexAssetTemplateManager getInstance() {
        return NexTemplateManagerHolder.INSTANCE;
    }

    public List<nexTemplateManager.Template> loadSmartEffectTemplateList() {
        initPath();
        nexTemplateManager kmTemplateManager = getKmTemplateManager();
        if (kmTemplateManager != null) {
            kmTemplateManager.loadTemplate();
            return kmTemplateManager.getTemplates();
        }
        return null;
    }

    public void checkExpiredAsset(final ICheckExpiredAssetListener iCheckExpiredAssetListener) {
        final nexTemplateManager kmTemplateManager;
        if (iCheckExpiredAssetListener == null || (kmTemplateManager = getKmTemplateManager()) == null) {
            return;
        }
        kmTemplateManager.loadTemplate(new Runnable() { // from class: com.miui.gallery.video.editor.manager.NexAssetTemplateManager.1
            @Override // java.lang.Runnable
            public void run() {
                List<nexTemplateManager.Template> templates = kmTemplateManager.getTemplates();
                boolean z = false;
                for (nexTemplateManager.Template template : templates) {
                    if (template != null) {
                        nexAssetPackageManager.getAssetPackageManager(GalleryApp.sGetAndroidContext());
                        if (nexAssetPackageManager.checkExpireAsset(template.packageInfo())) {
                            NexAssetTemplateManager.this.uninstallPackageById(template.id());
                            z = true;
                        }
                    }
                }
                if (z) {
                    NexAssetTemplateManager.getKmTemplateManager().loadTemplate();
                    iCheckExpiredAssetListener.onFinished(kmTemplateManager.getTemplates());
                    return;
                }
                iCheckExpiredAssetListener.onFinished(templates);
            }
        });
    }

    public void loadWaterMarkTemplateList(ILoadWaterMarkListener iLoadWaterMarkListener) {
        if (iLoadWaterMarkListener == null) {
            return;
        }
        initPath();
        iLoadWaterMarkListener.onFinished(nexOverlayPreset.getOverlayPreset(GalleryApp.sGetAndroidContext()).getIDs());
    }

    public final void initPath() {
        String str = VideoEditorConfig.ASSET_STORE_PATH;
        assetStoreSubDir = str;
        assetInstallRootPath = VideoEditorConfig.ASSET_INSTALL_ROOT_PATH;
        if (FileHelper.createDir(str)) {
            nexApplicationConfig.setAssetStoreRootPath(assetStoreSubDir);
            nexApplicationConfig.setAssetInstallRootPath(assetInstallRootPath);
        }
    }

    public void installProcess(int i, final ILoadAssetTemplate iLoadAssetTemplate) {
        if (iLoadAssetTemplate == null) {
            return;
        }
        initPath();
        nexAssetPackageManager.getAssetPackageManager(GalleryApp.sGetAndroidContext()).installPackagesAsync(i, new nexAssetPackageManager.OnInstallPackageListener() { // from class: com.miui.gallery.video.editor.manager.NexAssetTemplateManager.2
            @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.OnInstallPackageListener
            public void onProgress(int i2, int i3, int i4) {
            }

            @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.OnInstallPackageListener
            public void onCompleted(int i2, int i3) {
                if (i2 == -1) {
                    iLoadAssetTemplate.onFail();
                    DefaultLogger.d(NexAssetTemplateManager.TAG, "Install a new  asset package to NexEditorSDK Fail! ");
                    return;
                }
                iLoadAssetTemplate.onSuccess();
                nexColorEffect.updatePluginLut();
                DefaultLogger.d(NexAssetTemplateManager.TAG, "Install a new asset package to NexEditorSDK Success! ");
            }
        });
    }

    public static nexTemplateManager getKmTemplateManager() {
        return nexTemplateManager.getTemplateManager(GalleryApp.sGetAndroidContext(), GalleryApp.sGetAndroidContext());
    }

    public void uninstallPackageById(String str) {
        if (!TextUtils.isEmpty(str)) {
            nexAssetPackageManager.getAssetPackageManager(GalleryApp.sGetAndroidContext()).uninstallPackageById(str);
        }
    }
}
