package com.miui.gallery.compat.app;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import miui.app.MiuiFreeFormManager;

/* loaded from: classes.dex */
public abstract class ActivityCompat {

    /* loaded from: classes.dex */
    public interface SharedElementCallback {
        void onSharedElementStart();
    }

    public static void postponeEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.postponeEnterTransition();
        }
    }

    public static void startPostponedEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.startPostponedEnterTransition();
        }
    }

    public static void finishAfterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.finishAfterTransition();
        } else {
            activity.finish();
        }
    }

    public static void setEnterSharedElementCallback(Activity activity, final SharedElementCallback sharedElementCallback) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.setEnterSharedElementCallback(new android.app.SharedElementCallback() { // from class: com.miui.gallery.compat.app.ActivityCompat.1
                @Override // android.app.SharedElementCallback
                public void onSharedElementStart(List<String> list, List<View> list2, List<View> list3) {
                    SharedElementCallback sharedElementCallback2 = SharedElementCallback.this;
                    if (sharedElementCallback2 != null) {
                        sharedElementCallback2.onSharedElementStart();
                    }
                }
            });
        }
    }

    public static boolean isInMultiWindowMode(Activity activity) {
        Object invokeSafely;
        int i = Build.VERSION.SDK_INT;
        if (i >= 24) {
            return activity.isInMultiWindowMode();
        }
        if (i >= 21 && (invokeSafely = BaseMiscUtil.invokeSafely(activity, "isInMultiWindowMode", null, new Object[0])) != null && (invokeSafely instanceof Boolean)) {
            return ((Boolean) invokeSafely).booleanValue();
        }
        return false;
    }

    public static boolean isInFreeFormWindow(Context context) {
        try {
        } catch (Exception e) {
            DefaultLogger.e("ActivityCompat", e);
        }
        if (isMiuiFreeFormManagerClassExist()) {
            List allFreeFormStackInfosOnDisplay = MiuiFreeFormManager.getAllFreeFormStackInfosOnDisplay(-1);
            return (allFreeFormStackInfosOnDisplay == null || allFreeFormStackInfosOnDisplay.size() == 0) ? false : true;
        }
        int i = Settings.Secure.getInt(context.getContentResolver(), "freeform_window_state");
        DefaultLogger.d("ActivityCompat", "freeform_window_state %s", Integer.valueOf(i));
        return i == 0 || i == 1;
    }

    public static boolean isMiuiFreeFormManagerClassExist() {
        try {
            Class.forName("miui.app.MiuiFreeFormManager");
            return true;
        } catch (ClassNotFoundException unused) {
            return false;
        }
    }
}
