package miuix.core.util.screenutils;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.WindowManager;
import android.view.WindowMetrics;
import miuix.core.util.MiuixUIUtils;

/* loaded from: classes3.dex */
public class FreeFormModeHelper {
    public static int detectFreeFormMode(Context context) {
        return detectWindowInfo(context).windowMode;
    }

    public static MultiWindowModeHelper$WindowInfo detectWindowInfo(Context context) {
        return acquireFreeFormWindowRatioInternal(context);
    }

    public static MultiWindowModeHelper$WindowInfo acquireFreeFormWindowRatioInternal(Context context) {
        int i;
        int i2;
        if (!MiuixUIUtils.isFreeformMode(context)) {
            MultiWindowModeHelper$WindowInfo multiWindowModeHelper$WindowInfo = new MultiWindowModeHelper$WindowInfo();
            multiWindowModeHelper$WindowInfo.windowMode = 8192;
            return multiWindowModeHelper$WindowInfo;
        }
        float f = 0.0f;
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        if (Build.VERSION.SDK_INT >= 30) {
            WindowMetrics currentWindowMetrics = windowManager.getCurrentWindowMetrics();
            i = 0;
            if (currentWindowMetrics == null || currentWindowMetrics.getBounds().width() == 0) {
                i2 = 0;
            } else {
                i = currentWindowMetrics.getBounds().width();
                i2 = currentWindowMetrics.getBounds().height();
                f = (i2 * 1.0f) / i;
            }
        } else {
            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            int i3 = point.x;
            int i4 = point.y;
            if (i3 != 0) {
                f = (i4 * 1.0f) / i3;
            }
            i = i3;
            i2 = i4;
        }
        return freeFormModeRatioToCodeInternal(f, i, i2);
    }

    public static MultiWindowModeHelper$WindowInfo freeFormModeRatioToCodeInternal(float f, int i, int i2) {
        MultiWindowModeHelper$WindowInfo multiWindowModeHelper$WindowInfo = new MultiWindowModeHelper$WindowInfo();
        if (f <= 0.0f) {
            multiWindowModeHelper$WindowInfo.windowMode = 8192;
        } else if (f >= 0.74f && f < 0.76f) {
            multiWindowModeHelper$WindowInfo.windowMode = 8195;
        } else if (f >= 1.32f && f < 1.34f) {
            multiWindowModeHelper$WindowInfo.windowMode = 8194;
        } else if (f >= 1.76f && f < 1.79f) {
            multiWindowModeHelper$WindowInfo.windowMode = 8193;
        } else {
            multiWindowModeHelper$WindowInfo.windowMode = 8196;
        }
        multiWindowModeHelper$WindowInfo.windowWidth = i;
        multiWindowModeHelper$WindowInfo.windowHeight = i2;
        return multiWindowModeHelper$WindowInfo;
    }
}
