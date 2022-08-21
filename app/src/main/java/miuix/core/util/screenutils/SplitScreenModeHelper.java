package miuix.core.util.screenutils;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/* loaded from: classes3.dex */
public class SplitScreenModeHelper {
    public static Point sScreenRealSize = new Point();
    public static WindowManager sWindowManager;

    public static boolean isInRegion(float f, float f2, float f3) {
        return f >= f2 && f < f3;
    }

    public static WindowManager getWindowManager(Context context) {
        if (sWindowManager == null) {
            sWindowManager = (WindowManager) context.getApplicationContext().getSystemService("window");
        }
        return sWindowManager;
    }

    public static boolean isLandscape() {
        Point point = sScreenRealSize;
        return point.x > point.y;
    }

    public static int detectScreenMode(Context context) {
        return detectWindowInfo(context).windowMode;
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0053  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x0058  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static miuix.core.util.screenutils.MultiWindowModeHelper$WindowInfo detectWindowInfo(android.content.Context r5) {
        /*
            miuix.core.util.screenutils.MultiWindowModeHelper$WindowInfo r0 = new miuix.core.util.screenutils.MultiWindowModeHelper$WindowInfo
            r0.<init>()
            android.view.WindowManager r1 = getWindowManager(r5)
            android.view.Display r1 = r1.getDefaultDisplay()
            android.graphics.Point r2 = miuix.core.util.screenutils.SplitScreenModeHelper.sScreenRealSize
            r1.getRealSize(r2)
            android.content.res.Resources r5 = r5.getResources()
            android.util.DisplayMetrics r5 = r5.getDisplayMetrics()
            boolean r1 = isLandscape()
            r2 = 0
            if (r1 == 0) goto L2c
            int r1 = r5.widthPixels
            float r1 = (float) r1
            android.graphics.Point r3 = miuix.core.util.screenutils.SplitScreenModeHelper.sScreenRealSize
            int r3 = r3.x
        L28:
            float r3 = (float) r3
            float r3 = r3 + r2
            float r1 = r1 / r3
            goto L42
        L2c:
            int r1 = r5.widthPixels
            float r1 = (float) r1
            android.graphics.Point r3 = miuix.core.util.screenutils.SplitScreenModeHelper.sScreenRealSize
            int r4 = r3.x
            float r4 = (float) r4
            float r4 = r4 + r2
            float r1 = r1 / r4
            r4 = 1065353216(0x3f800000, float:1.0)
            int r4 = (r1 > r4 ? 1 : (r1 == r4 ? 0 : -1))
            if (r4 != 0) goto L42
            int r1 = r5.heightPixels
            float r1 = (float) r1
            int r3 = r3.y
            goto L28
        L42:
            int r3 = r5.widthPixels
            r0.windowWidth = r3
            int r5 = r5.heightPixels
            r0.windowHeight = r5
            r5 = 1053609165(0x3ecccccd, float:0.4)
            boolean r2 = isInRegion(r1, r2, r5)
            if (r2 == 0) goto L58
            r5 = 4097(0x1001, float:5.741E-42)
            r0.windowMode = r5
            goto L78
        L58:
            r2 = 1058642330(0x3f19999a, float:0.6)
            boolean r5 = isInRegion(r1, r5, r2)
            if (r5 == 0) goto L66
            r5 = 4098(0x1002, float:5.743E-42)
            r0.windowMode = r5
            goto L78
        L66:
            r5 = 1061997773(0x3f4ccccd, float:0.8)
            boolean r5 = isInRegion(r1, r2, r5)
            if (r5 == 0) goto L74
            r5 = 4099(0x1003, float:5.744E-42)
            r0.windowMode = r5
            goto L78
        L74:
            r5 = 4100(0x1004, float:5.745E-42)
            r0.windowMode = r5
        L78:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: miuix.core.util.screenutils.SplitScreenModeHelper.detectWindowInfo(android.content.Context):miuix.core.util.screenutils.MultiWindowModeHelper$WindowInfo");
    }
}
