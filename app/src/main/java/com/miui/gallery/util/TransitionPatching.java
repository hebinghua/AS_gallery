package com.miui.gallery.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.reflect.Method;

/* loaded from: classes2.dex */
public class TransitionPatching {
    public static void onActivityStopWhenEnterStarting(Activity activity) {
        Object field;
        Method method;
        DefaultLogger.w("TransitionPatching", "onActivityStopWhenEnterStarting");
        if (activity == null || Build.VERSION.SDK_INT <= 23) {
            return;
        }
        try {
            Object field2 = ReflectUtils.getField("android.app.Activity", activity, "mActivityTransitionState");
            if (field2 == null || (field = ReflectUtils.getField("android.app.ActivityTransitionState", field2, "mEnterTransitionCoordinator")) == null) {
                return;
            }
            Method method2 = ReflectUtils.getMethod("android.app.EnterTransitionCoordinator", "forceViewsToAppear");
            if (method2 != null) {
                ReflectUtils.invokeMethod(field, method2, new Object[0]);
                DefaultLogger.d("TransitionPatching", "forceViewsToAppear");
            }
            Object field3 = ReflectUtils.getField("android.app.EnterTransitionCoordinator", field, "mViewsReadyListener");
            if (field3 == null || (method = ReflectUtils.getMethod("com.android.internal.view.OneShotPreDrawListener", "removeListener")) == null) {
                return;
            }
            ReflectUtils.invokeMethod(field3, method, new Object[0]);
            DefaultLogger.d("TransitionPatching", "removeListener");
        } catch (Exception e) {
            DefaultLogger.w("TransitionPatching", "preActivityStop occurs error.\n", e);
        }
    }

    public static void setOnEnterStartedListener(Activity activity, final Runnable runnable) {
        Window window;
        final View decorView;
        final ViewTreeObserver viewTreeObserver;
        DefaultLogger.d("TransitionPatching", "onStartEnterTransition");
        if (activity == null || runnable == null || (window = activity.getWindow()) == null || (decorView = window.getDecorView()) == null || (viewTreeObserver = decorView.getViewTreeObserver()) == null) {
            return;
        }
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.miui.gallery.util.TransitionPatching.1
            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                DefaultLogger.d("TransitionPatching", "onEnterTransitionStarted");
                runnable.run();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.removeOnPreDrawListener(this);
                    return true;
                }
                ViewTreeObserver viewTreeObserver2 = decorView.getViewTreeObserver();
                if (viewTreeObserver2 == null || !viewTreeObserver2.isAlive()) {
                    return true;
                }
                viewTreeObserver2.removeOnPreDrawListener(this);
                return true;
            }
        });
    }
}
