package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.media.MediaCodecList;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.app.common.nexasset.assetpackage.security.provider.BasicEncryptionProvider;
import com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB;
import com.nexstreaming.app.common.task.Task;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.kminternal.kinemaster.config.NexEditorDeviceProfile;
import com.nexstreaming.kminternal.kinemaster.mediainfo.MediaInfo;
import com.nexstreaming.kminternal.nexvideoeditor.NexThemeView;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public final class nexApplicationConfig {
    private static long AppAssetPackageVersionCode = 1560757754503L;
    private static com.nexstreaming.kminternal.kinemaster.config.a KMSingle = null;
    private static final int SDK_VERSION_DevCode = 17;
    private static final int SDK_VERSION_Major = 2;
    private static final int SDK_VERSION_Minor = 0;
    private static final int SDK_VERSION_Patch = 13;
    private static String TAG = "nexApplicationConfig";
    public static final int kAspectRatio_Mode_16v9 = 1;
    public static final int kAspectRatio_Mode_1v1 = 2;
    public static final int kAspectRatio_Mode_1v2 = 5;
    public static final int kAspectRatio_Mode_2v1 = 4;
    public static final int kAspectRatio_Mode_3v4 = 7;
    public static final int kAspectRatio_Mode_4v3 = 6;
    public static final int kAspectRatio_Mode_9v16 = 3;
    public static final int kAspectRatio_Mode_free = 0;
    public static final int kOverlayCoordinateMode_ScreenDimention = 0;
    public static final int kOverlayCoordinateMode_Stretching = 1;
    public static final int kScreenMode_horizonDual = 1;
    public static final int kScreenMode_normal = 0;
    private static String pendingDevAssetLoadError = null;
    private static boolean pendingDevAssetNotification = false;
    private static Task s_asyncInitDBTask;
    private static com.nexstreaming.app.common.nexasset.assetpackage.security.b[] securityProvider;
    private static nexAspectProfile aspectProfile = nexAspectProfile.ar16v9;
    private static int sAspectRatioMode = 1;
    private static int sScreenMode = 0;
    private static int sOverlayCoordinateMode = 0;
    public static String letterbox_effect_black = "black";
    public static String letterbox_effect_blur10 = "com.nexstreaming.editor.blurall range.f_block_size 10.0";
    private static String default_letterbox_effect = "black";
    private static final Executor sInitAssetDBThreadPool = Executors.newSingleThreadExecutor();

    @Deprecated
    /* loaded from: classes3.dex */
    public static abstract class OnEffectLoadListener {
        public abstract void onEffectLoad(String str, int i);

        public abstract void onEffectLoadComplete();

        public abstract void onEffectPriorityLoadComplete();
    }

    public static final int getApiLevel() {
        return 0;
    }

    public static String getDevelopString() {
        return "Develop";
    }

    @Deprecated
    public static void setEffectLoadListener(OnEffectLoadListener onEffectLoadListener) {
    }

    public static synchronized void createApp(Context context) {
        synchronized (nexApplicationConfig.class) {
            Log.d(TAG, "call createApp");
            if (KMSingle == null) {
                KMSingle = new com.nexstreaming.kminternal.kinemaster.config.a(context);
                new Thread(new Runnable() { // from class: com.nexstreaming.nexeditorsdk.nexApplicationConfig.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MediaCodecList.getCodecCount();
                    }
                }).start();
            }
        }
    }

    public static void init(Context context, String str) {
        init(context, str, null, null);
    }

    public static void init(Context context, String str, String str2) {
        init(context, str, null, str2);
    }

    public static void init(Context context, String str, File file, String str2) {
        init(context, null, str, file, str2);
    }

    public static void init(Context context, Context context2, String str, File file, String str2) {
        String str3 = TAG;
        Log.i(str3, "KMSDK Version :" + getSDKVersion() + ".17");
        AssetLocalInstallDB.getInstance(context);
        EditorGlobal.a(str);
        MediaInfo.a(context);
        int[] iArr = nexConfig.sConfigProperty;
        if (iArr[0] != 0) {
            NexEditorDeviceProfile.setAppContext(context, iArr);
        } else {
            NexEditorDeviceProfile.setAppContext(context, null);
        }
        if (securityProvider == null) {
            int i = -1;
            com.nexstreaming.app.common.nexasset.assetpackage.security.provider.a aVar = new com.nexstreaming.app.common.nexasset.assetpackage.security.provider.a();
            securityProvider = new com.nexstreaming.app.common.nexasset.assetpackage.security.b[aVar.a().size()];
            for (Map.Entry<int[], String[]> entry : aVar.a().entrySet()) {
                i++;
                securityProvider[i] = new BasicEncryptionProvider(entry.getKey(), entry.getValue());
                com.nexstreaming.app.common.nexasset.assetpackage.security.b[] bVarArr = securityProvider;
                if (bVarArr[i] != null) {
                    AssetPackageReader.a(bVarArr[i]);
                }
            }
            String str4 = TAG;
            Log.d(str4, "Register " + securityProvider.length + " security provider!");
        }
        if (s_asyncInitDBTask == null) {
            loadInitDB(context, file);
        } else {
            new AsyncTask<Context, Void, Void>() { // from class: com.nexstreaming.nexeditorsdk.nexApplicationConfig.2
                @Override // android.os.AsyncTask
                /* renamed from: a */
                public Void doInBackground(Context... contextArr) {
                    nexApplicationConfig.loadInitDB(contextArr[0], null);
                    return null;
                }

                @Override // android.os.AsyncTask
                /* renamed from: a */
                public void onPostExecute(Void r4) {
                    nexApplicationConfig.s_asyncInitDBTask.signalEvent(Task.Event.COMPLETE);
                }
            }.executeOnExecutor(sInitAssetDBThreadPool, context);
        }
    }

    public static void initMaster(Context context, String str) {
        String str2 = TAG;
        Log.d(str2, "initMaster nexEditorSDK Version :" + getSDKVersion() + ".17");
        EditorGlobal.a(str);
        MediaInfo.a(context);
        int[] iArr = nexConfig.sConfigProperty;
        if (iArr[0] != 0) {
            NexEditorDeviceProfile.setAppContext(context, iArr);
        } else {
            NexEditorDeviceProfile.setAppContext(context, null);
        }
        Log.d(TAG, "initMaster End");
    }

    public static void initAssetDB(Context context, File file) {
        Log.d(TAG, "initAssetDB start");
        AssetLocalInstallDB.getInstance(context);
        if (securityProvider == null) {
            int i = -1;
            com.nexstreaming.app.common.nexasset.assetpackage.security.provider.a aVar = new com.nexstreaming.app.common.nexasset.assetpackage.security.provider.a();
            securityProvider = new com.nexstreaming.app.common.nexasset.assetpackage.security.b[aVar.a().size()];
            for (Map.Entry<int[], String[]> entry : aVar.a().entrySet()) {
                i++;
                securityProvider[i] = new BasicEncryptionProvider(entry.getKey(), entry.getValue());
                com.nexstreaming.app.common.nexasset.assetpackage.security.b[] bVarArr = securityProvider;
                if (bVarArr[i] != null) {
                    AssetPackageReader.a(bVarArr[i]);
                }
            }
            String str = TAG;
            Log.d(str, "Register " + securityProvider.length + " security provider!");
        }
        if (s_asyncInitDBTask == null) {
            loadInitDB(context, file);
        } else {
            new AsyncTask<Context, Void, Void>() { // from class: com.nexstreaming.nexeditorsdk.nexApplicationConfig.3
                @Override // android.os.AsyncTask
                /* renamed from: a */
                public Void doInBackground(Context... contextArr) {
                    nexApplicationConfig.loadInitDB(contextArr[0], null);
                    return null;
                }

                @Override // android.os.AsyncTask
                /* renamed from: a */
                public void onPostExecute(Void r4) {
                    nexApplicationConfig.s_asyncInitDBTask.signalEvent(Task.Event.COMPLETE);
                }
            }.executeOnExecutor(sInitAssetDBThreadPool, context);
        }
        Log.d(TAG, "initAssetDB End");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loadInitDB(Context context, File file) {
        try {
            com.nexstreaming.app.common.nexasset.assetpackage.c.a(context).a(context, "kmassets", AppAssetPackageVersionCode);
            try {
                if (file != null) {
                    com.nexstreaming.app.common.nexasset.assetpackage.c.a(context).a(file);
                    pendingDevAssetNotification = com.nexstreaming.app.common.nexasset.assetpackage.c.a(context).b(file);
                } else {
                    com.nexstreaming.app.common.nexasset.assetpackage.c.a(context).a(EditorGlobal.h());
                    pendingDevAssetNotification = com.nexstreaming.app.common.nexasset.assetpackage.c.a(context).b(EditorGlobal.h());
                }
            } catch (IOException e) {
                pendingDevAssetLoadError = e.getLocalizedMessage();
            }
            com.nexstreaming.kminternal.kinemaster.editorwrapper.b.a(context);
            nexOverlayPreset.getOverlayPreset(context);
        } catch (IOException e2) {
            throw new RuntimeException("Cannot read prepackaged assets", e2);
        }
    }

    public static void asyncLoadAssetDB(final Runnable runnable) {
        if (s_asyncInitDBTask == null) {
            s_asyncInitDBTask = new Task();
        }
        if (runnable != null) {
            s_asyncInitDBTask.mo1850onComplete(new Task.OnTaskEventListener() { // from class: com.nexstreaming.nexeditorsdk.nexApplicationConfig.4
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task, Task.Event event) {
                    new Handler(Looper.getMainLooper()).post(runnable);
                }
            });
        }
    }

    public static void releaseApp() {
        Log.d(TAG, "call releaseApp");
        com.nexstreaming.app.common.nexasset.assetpackage.security.b[] bVarArr = securityProvider;
        if (bVarArr != null) {
            for (com.nexstreaming.app.common.nexasset.assetpackage.security.b bVar : bVarArr) {
                if (bVar != null) {
                    AssetPackageReader.b(bVar);
                }
            }
            Log.d(TAG, "UnRegister " + securityProvider.length + " security provider!");
            securityProvider = null;
        }
        if (com.nexstreaming.kminternal.kinemaster.editorwrapper.b.c() != null) {
            com.nexstreaming.kminternal.kinemaster.editorwrapper.b.c().a();
        }
        releaseNativeEngine();
    }

    public static void releaseNativeEngine() {
        com.nexstreaming.kminternal.kinemaster.config.a aVar = KMSingle;
        if (aVar != null) {
            aVar.d();
        }
    }

    public static void releaseNativeEngine(nexEngine nexengine) {
        if (nexengine != null) {
            nexengine.setEventHandler(null);
            nexengine.clearProject();
            nexengine.removeEditorListener();
        }
        com.nexstreaming.kminternal.kinemaster.config.a aVar = KMSingle;
        if (aVar != null) {
            aVar.d();
        }
    }

    public static void waitForLoading(Context context, final Runnable runnable) {
        Log.d(TAG, "call waitForLoading");
        Task task = s_asyncInitDBTask;
        if (task != null) {
            task.mo1850onComplete(new Task.OnTaskEventListener() { // from class: com.nexstreaming.nexeditorsdk.nexApplicationConfig.5
                @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
                public void onTaskEvent(Task task2, Task.Event event) {
                    runnable.run();
                }
            });
        } else if (runnable != null) {
            runnable.run();
        }
        Log.d(TAG, "call waitForLoading end");
    }

    @Deprecated
    public static void waitForMinimumLoading(Context context, Runnable runnable) {
        Log.d(TAG, "call waitForMinimumLoading");
        waitForLoading(context, runnable);
    }

    public static String getSDKVersion() {
        return String.format("%d.%d.%d", 2, 0, 13);
    }

    public static void setAspectMode(int i) {
        nexAspectProfile aspectProfile2 = nexAspectProfile.getAspectProfile(i);
        aspectProfile = aspectProfile2;
        if (aspectProfile2 != null) {
            sAspectRatioMode = i;
            NexThemeView.setAspectRatio(aspectProfile2.getAspectRatio());
        }
    }

    public static int getAspectMode() {
        return sAspectRatioMode;
    }

    public static float getAspectRatio() {
        nexAspectProfile nexaspectprofile = aspectProfile;
        if (nexaspectprofile != null) {
            return nexaspectprofile.getAspectRatio();
        }
        return 1.7777778f;
    }

    public static float getAspectRatioInScreenMode() {
        float f;
        float f2;
        nexAspectProfile nexaspectprofile = aspectProfile;
        if (nexaspectprofile != null) {
            f = nexaspectprofile.getWidth();
            f2 = aspectProfile.getHeight();
        } else {
            f = 16.0f;
            f2 = 9.0f;
        }
        if (sScreenMode == 1) {
            f *= 2.0f;
        }
        Log.d("ScreenMode", "w=" + f + ", h=" + f2);
        return f / f2;
    }

    public static int getAspectRatioMode() {
        return sAspectRatioMode;
    }

    public static void setScreenMode(int i) {
        Log.d("ScreenMode", "setScreenMode=" + i);
        sScreenMode = i;
    }

    public static int getScreenMode() {
        return sScreenMode;
    }

    /* loaded from: classes3.dex */
    public enum APILevel {
        UnLimited(0),
        OverlayImageLimited(1),
        OverlayVideoLimited(2),
        OverlayAnimateLimited(3);
        
        private final int internalValue;

        APILevel(int i) {
            this.internalValue = i;
        }

        public int getValue() {
            return this.internalValue;
        }

        public static APILevel fromValue(int i) {
            APILevel[] values;
            for (APILevel aPILevel : values()) {
                if (aPILevel.getValue() == i) {
                    return aPILevel;
                }
            }
            return null;
        }
    }

    public static final boolean isSupportedApi(APILevel aPILevel) {
        return EditorGlobal.a(aPILevel.getValue());
    }

    public static final void setAppAssetPackageVersionCode(long j) {
        AppAssetPackageVersionCode = j;
    }

    public static final void setAssetStoreRootPath(String str) {
        AssetLocalInstallDB.setAssetStoreRootPath(str);
    }

    public static final void setAssetInstallRootPath(String str) {
        AssetLocalInstallDB.setInstalledAssetPath(str);
    }

    public static void setAspectProfile(nexAspectProfile nexaspectprofile) {
        sAspectRatioMode = nexaspectprofile.getAspectMode();
        aspectProfile = nexaspectprofile;
        NexThemeView.setAspectRatio(nexaspectprofile.getAspectRatio());
    }

    public static final nexAspectProfile getAspectProfile() {
        return aspectProfile;
    }

    public static void setDefaultLetterboxEffect(String str) {
        default_letterbox_effect = str;
    }

    public static String getDefaultLetterboxEffect() {
        return default_letterbox_effect;
    }

    public static nexExternalModuleManager getExternalModuleManager() {
        return nexExternalModuleManager.getInstance();
    }

    public static void setOverlayCoordinateMode(int i) {
        sOverlayCoordinateMode = i;
    }

    public static int getOverlayCoordinateMode() {
        return sOverlayCoordinateMode;
    }
}
