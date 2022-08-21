package com.miui.gallery.content;

import com.miui.gallery.GalleryApp;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.lang.ref.WeakReference;

/* loaded from: classes.dex */
public class ExtendedCursorHelper {
    public boolean mLoadInProcessStopped = false;
    public Runnable mCheckProcessBackground = new CheckProcessBackgroundRunnable(this);
    public Runnable mCheckProcessBackgroundDeprecated = new CheckProcessBackgroundRunnableDeprecated(this);

    public void onStopLoading() {
        ThreadManager.getMainHandler().removeCallbacks(this.mCheckProcessBackground);
        ThreadManager.getWorkHandler().post(this.mCheckProcessBackgroundDeprecated);
        ThreadManager.getMainHandler().postDelayed(this.mCheckProcessBackground, 800L);
    }

    public void onStartLoading() {
        this.mLoadInProcessStopped = false;
        ThreadManager.getMainHandler().removeCallbacks(this.mCheckProcessBackground);
    }

    public boolean shouldLoadInProcessStopped() {
        return this.mLoadInProcessStopped;
    }

    /* loaded from: classes.dex */
    public static class CheckProcessBackgroundRunnable implements Runnable {
        public WeakReference<ExtendedCursorHelper> mHelperRef;

        public CheckProcessBackgroundRunnable(ExtendedCursorHelper extendedCursorHelper) {
            this.mHelperRef = new WeakReference<>(extendedCursorHelper);
        }

        @Override // java.lang.Runnable
        public void run() {
            ExtendedCursorHelper extendedCursorHelper = this.mHelperRef.get();
            if (extendedCursorHelper != null && !MiscUtil.isAppProcessInForeground()) {
                extendedCursorHelper.mLoadInProcessStopped = true;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class CheckProcessBackgroundRunnableDeprecated implements Runnable {
        public WeakReference<ExtendedCursorHelper> mHelperRef;

        public CheckProcessBackgroundRunnableDeprecated(ExtendedCursorHelper extendedCursorHelper) {
            this.mHelperRef = new WeakReference<>(extendedCursorHelper);
        }

        @Override // java.lang.Runnable
        public void run() {
            ExtendedCursorHelper extendedCursorHelper = this.mHelperRef.get();
            if (extendedCursorHelper != null && !MiscUtil.isAppProcessInForeground(GalleryApp.sGetAndroidContext())) {
                extendedCursorHelper.mLoadInProcessStopped = true;
            }
        }
    }
}
