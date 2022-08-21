package com.miui.gallery.video.editor;

import android.content.Context;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexApplicationConfig;
import com.nexstreaming.nexeditorsdk.nexConfig;
import com.nexstreaming.nexeditorsdk.nexEngine;

/* loaded from: classes2.dex */
public class NexEngine {
    public static nexEngine mEngine = null;
    public static boolean mIsInit = false;
    public static Boolean mIsInitFinished = Boolean.FALSE;

    public static synchronized nexEngine getEngine(Context context) {
        nexEngine nexengine;
        synchronized (NexEngine.class) {
            if (!mIsInit) {
                throw new IllegalStateException("call init first");
            }
            if (mEngine == null) {
                nexEngine.setLoadListAsync(true);
                mEngine = new nexEngine(context.getApplicationContext());
            }
            nexengine = mEngine;
        }
        return nexengine;
    }

    public static void init(Context context, int i, final Runnable runnable) {
        if (!mIsInit) {
            nexApplicationConfig.setAspectMode(i);
            Context applicationContext = context.getApplicationContext();
            nexApplicationConfig.createApp(applicationContext);
            nexConfig.set(24883200, 4, 250, false, 8294400);
            nexApplicationConfig.init(applicationContext, "MiuiGallery");
            mIsInit = true;
            nexApplicationConfig.waitForLoading(context, new Runnable() { // from class: com.miui.gallery.video.editor.NexEngine.1
                @Override // java.lang.Runnable
                public void run() {
                    Boolean unused = NexEngine.mIsInitFinished = Boolean.TRUE;
                    runnable.run();
                }
            });
            return;
        }
        runnable.run();
    }

    public static void releaseEngine() {
        if (mIsInitFinished.booleanValue()) {
            Log.d("NexEngine", "releaseEngine");
            mEngine = null;
            try {
                nexApplicationConfig.releaseApp();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mIsInit = false;
            mIsInitFinished = Boolean.FALSE;
        }
    }
}
