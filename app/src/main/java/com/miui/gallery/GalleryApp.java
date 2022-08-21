package com.miui.gallery;

import android.content.Context;
import androidx.tracing.Trace;
import androidx.work.Configuration;
import com.miui.core.SdkHelper;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.lifecycle.GalleryAppLifecycle;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.miui.gallery.util.thread.ThreadManager;
import com.miui.os.Rom;
import splitties.init.AppCtxKt;

/* loaded from: classes.dex */
public class GalleryApp extends Hilt_GalleryApp implements Configuration.Provider {
    public static volatile Context sContext;

    static {
        if (SdkHelper.IS_MIUI) {
            TimingTracing.setEnabled(!Rom.IS_STABLE);
        }
    }

    @Override // android.content.ContextWrapper
    public void attachBaseContext(Context context) {
        try {
            AppCtxKt.injectAsAppCtx(this);
            TimeMonitor.createNewTimeMonitor("403.1.0.1.13757");
            registerActivityLifecycleCallbacks(new GalleryAppLifecycle());
            Trace.beginSection("attachBaseContext");
            sContext = this;
            StaticContext.init(sContext);
            super.attachBaseContext(context);
        } finally {
            Trace.endSection();
        }
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onLowMemory() {
        super.onLowMemory();
        DefaultLogger.e("GalleryApp", "onLowMemory");
        ThreadManager.printAllThreadPoolStatus();
    }

    public static Context sGetAndroidContext() {
        return sContext;
    }

    @Override // androidx.work.Configuration.Provider
    public Configuration getWorkManagerConfiguration() {
        return new Configuration.Builder().setJobSchedulerJobIdRange(3000, 5000).setMinimumLoggingLevel(3).setMaxSchedulerLimit(25).build();
    }
}
