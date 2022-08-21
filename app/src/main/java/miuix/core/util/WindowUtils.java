package miuix.core.util;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;

/* loaded from: classes3.dex */
public class WindowUtils {
    public static WindowManager getWindowManager(Context context) {
        return (WindowManager) context.getSystemService("window");
    }

    public static Display getDisplay(Context context) {
        if (Build.VERSION.SDK_INT >= 30) {
            return context.getDisplay();
        }
        return getWindowManager(context).getDefaultDisplay();
    }

    public static WindowMetrics getCurrentWindowMetrics(Context context) {
        return getWindowManager(context).getCurrentWindowMetrics();
    }

    public static Point getWindowSize(Context context) {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 30) {
            Rect windowsBounds = getWindowsBounds(context);
            point.x = windowsBounds.width();
            point.y = windowsBounds.height();
        } else if (MiuixUIUtils.isInMultiWindowMode(context)) {
            getDisplay(context).getSize(point);
        } else {
            getDisplay(context).getRealSize(point);
        }
        return point;
    }

    public static Rect getWindowsBounds(Context context) {
        return getCurrentWindowMetrics(context).getBounds();
    }

    public static int getWindowHeight(Context context) {
        return getWindowSize(context).y;
    }
}
