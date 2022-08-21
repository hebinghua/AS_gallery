package com.baidu.platform.comapi.d;

import android.content.Context;
import android.os.Build;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes.dex */
public class a {
    private static int a = 621133959;

    public static boolean a(Context context) {
        return c(context);
    }

    private static int b(Context context) {
        try {
            return (Build.VERSION.SDK_INT >= 28 ? context.getPackageManager().getPackageInfo("com.baidu.BaiduMap", Box.MAX_BOX_SIZE).signingInfo.hasMultipleSigners() ? context.getPackageManager().getPackageInfo("com.baidu.BaiduMap", Box.MAX_BOX_SIZE).signingInfo.getApkContentsSigners() : context.getPackageManager().getPackageInfo("com.baidu.BaiduMap", Box.MAX_BOX_SIZE).signingInfo.getSigningCertificateHistory() : context.getPackageManager().getPackageInfo("com.baidu.BaiduMap", 64).signatures)[0].hashCode();
        } catch (Exception unused) {
            return 0;
        }
    }

    private static boolean c(Context context) {
        return b(context) == a;
    }
}
