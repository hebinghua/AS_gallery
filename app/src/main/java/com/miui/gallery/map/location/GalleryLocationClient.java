package com.miui.gallery.map.location;

import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

/* loaded from: classes2.dex */
public class GalleryLocationClient {
    public static boolean isLocationServiceAvailable(Context context) {
        if (Build.VERSION.SDK_INT < 28) {
            return Settings.Secure.getInt(context.getContentResolver(), "location_mode", 0) != 0;
        }
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        return locationManager != null && locationManager.isLocationEnabled();
    }

    /* JADX WARN: Can't wrap try/catch for region: R(13:(2:8|9)|(11:11|12|13|(2:15|(1:(4:(1:28)(1:34)|(1:30)(1:33)|31|32)(2:21|(2:23|24)(2:25|26)))(1:18))|36|(0)|(0)|(0)(0)|(0)(0)|31|32)|40|12|13|(0)|36|(0)|(0)|(0)(0)|(0)(0)|31|32) */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0031, code lost:
        r5 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0032, code lost:
        com.miui.gallery.util.logger.DefaultLogger.d("GalleryLocationClient", (java.lang.Throwable) r5);
     */
    /* JADX WARN: Removed duplicated region for block: B:16:0x002c A[Catch: Exception -> 0x0031, TRY_LEAVE, TryCatch #0 {Exception -> 0x0031, blocks: (B:14:0x0026, B:16:0x002c), top: B:41:0x0026 }] */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0038 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x003d A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x006c  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0077  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.map.cluster.MapLatLng getLastKnownLocation(android.content.Context r5) {
        /*
            java.lang.String r0 = "gps"
            java.lang.String r1 = "GalleryLocationClient"
            java.lang.String r2 = "network"
            r3 = 0
            if (r5 != 0) goto La
            return r3
        La:
            java.lang.String r4 = "location"
            java.lang.Object r5 = r5.getSystemService(r4)
            android.location.LocationManager r5 = (android.location.LocationManager) r5
            if (r5 == 0) goto L80
            boolean r4 = r5.isProviderEnabled(r2)     // Catch: java.lang.Exception -> L21
            if (r4 == 0) goto L1f
            android.location.Location r2 = r5.getLastKnownLocation(r2)     // Catch: java.lang.Exception -> L21
            goto L26
        L1f:
            r2 = r3
            goto L26
        L21:
            r2 = move-exception
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r2)
            goto L1f
        L26:
            boolean r4 = r5.isProviderEnabled(r0)     // Catch: java.lang.Exception -> L31
            if (r4 == 0) goto L35
            android.location.Location r5 = r5.getLastKnownLocation(r0)     // Catch: java.lang.Exception -> L31
            goto L36
        L31:
            r5 = move-exception
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r5)
        L35:
            r5 = r3
        L36:
            if (r2 != 0) goto L3b
            if (r5 != 0) goto L3b
            return r3
        L3b:
            if (r2 == 0) goto L65
            if (r5 == 0) goto L65
            long r0 = r2.getTime()
            long r3 = r5.getTime()
            int r0 = (r0 > r3 ? 1 : (r0 == r3 ? 0 : -1))
            if (r0 >= 0) goto L58
            double r0 = r5.getLatitude()
            double r2 = r5.getLongitude()
            com.miui.gallery.map.cluster.MapLatLng r5 = com.miui.gallery.map.utils.LocationConverter.convertToMapLatLng(r0, r2)
            return r5
        L58:
            double r0 = r2.getLatitude()
            double r2 = r2.getLongitude()
            com.miui.gallery.map.cluster.MapLatLng r5 = com.miui.gallery.map.utils.LocationConverter.convertToMapLatLng(r0, r2)
            return r5
        L65:
            if (r5 != 0) goto L6c
            double r0 = r2.getLatitude()
            goto L70
        L6c:
            double r0 = r5.getLatitude()
        L70:
            if (r5 != 0) goto L77
            double r2 = r2.getLongitude()
            goto L7b
        L77:
            double r2 = r5.getLongitude()
        L7b:
            com.miui.gallery.map.cluster.MapLatLng r5 = com.miui.gallery.map.utils.LocationConverter.convertToMapLatLng(r0, r2)
            return r5
        L80:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.map.location.GalleryLocationClient.getLastKnownLocation(android.content.Context):com.miui.gallery.map.cluster.MapLatLng");
    }
}
