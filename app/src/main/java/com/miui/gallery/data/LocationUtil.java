package com.miui.gallery.data;

import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class LocationUtil {
    public static int INT_COORDINATE_FACTOR = 1000000;

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0091, code lost:
        if (r13.equals("W") != false) goto L16;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static double convertRationalLatLonToDouble(java.lang.String r12, java.lang.String r13) {
        /*
            java.lang.String r0 = ","
            java.lang.String r1 = "/"
            boolean r2 = android.text.TextUtils.isEmpty(r12)
            r3 = 0
            if (r2 == 0) goto Ld
            return r3
        Ld:
            boolean r2 = r12.contains(r0)     // Catch: java.lang.Throwable -> L96
            if (r2 == 0) goto L18
            java.lang.String[] r12 = r12.split(r0)     // Catch: java.lang.Throwable -> L96
            goto L1e
        L18:
            java.lang.String r0 = " "
            java.lang.String[] r12 = r12.split(r0)     // Catch: java.lang.Throwable -> L96
        L1e:
            r0 = 0
            r2 = r12[r0]     // Catch: java.lang.Throwable -> L96
            java.lang.String[] r2 = r2.split(r1)     // Catch: java.lang.Throwable -> L96
            r5 = r2[r0]     // Catch: java.lang.Throwable -> L96
            java.lang.String r5 = r5.trim()     // Catch: java.lang.Throwable -> L96
            double r5 = java.lang.Double.parseDouble(r5)     // Catch: java.lang.Throwable -> L96
            r7 = 1
            r2 = r2[r7]     // Catch: java.lang.Throwable -> L96
            java.lang.String r2 = r2.trim()     // Catch: java.lang.Throwable -> L96
            double r8 = java.lang.Double.parseDouble(r2)     // Catch: java.lang.Throwable -> L96
            double r5 = r5 / r8
            r2 = r12[r7]     // Catch: java.lang.Throwable -> L96
            java.lang.String[] r2 = r2.split(r1)     // Catch: java.lang.Throwable -> L96
            r8 = r2[r0]     // Catch: java.lang.Throwable -> L96
            java.lang.String r8 = r8.trim()     // Catch: java.lang.Throwable -> L96
            double r8 = java.lang.Double.parseDouble(r8)     // Catch: java.lang.Throwable -> L96
            r2 = r2[r7]     // Catch: java.lang.Throwable -> L96
            java.lang.String r2 = r2.trim()     // Catch: java.lang.Throwable -> L96
            double r10 = java.lang.Double.parseDouble(r2)     // Catch: java.lang.Throwable -> L96
            double r8 = r8 / r10
            r2 = 2
            r12 = r12[r2]     // Catch: java.lang.Throwable -> L96
            java.lang.String[] r12 = r12.split(r1)     // Catch: java.lang.Throwable -> L96
            r0 = r12[r0]     // Catch: java.lang.Throwable -> L96
            java.lang.String r0 = r0.trim()     // Catch: java.lang.Throwable -> L96
            double r0 = java.lang.Double.parseDouble(r0)     // Catch: java.lang.Throwable -> L96
            r12 = r12[r7]     // Catch: java.lang.Throwable -> L96
            java.lang.String r12 = r12.trim()     // Catch: java.lang.Throwable -> L96
            double r10 = java.lang.Double.parseDouble(r12)     // Catch: java.lang.Throwable -> L96
            double r0 = r0 / r10
            r10 = 4633641066610819072(0x404e000000000000, double:60.0)
            double r8 = r8 / r10
            double r5 = r5 + r8
            r7 = 4660134898793709568(0x40ac200000000000, double:3600.0)
            double r0 = r0 / r7
            double r5 = r5 + r0
            boolean r12 = android.text.TextUtils.isEmpty(r13)     // Catch: java.lang.Throwable -> L96
            if (r12 != 0) goto L95
            java.lang.String r12 = "S"
            boolean r12 = r13.equals(r12)     // Catch: java.lang.Throwable -> L96
            if (r12 != 0) goto L93
            java.lang.String r12 = "W"
            boolean r12 = r13.equals(r12)     // Catch: java.lang.Throwable -> L96
            if (r12 == 0) goto L95
        L93:
            double r12 = -r5
            return r12
        L95:
            return r5
        L96:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.data.LocationUtil.convertRationalLatLonToDouble(java.lang.String, java.lang.String):double");
    }

    public static String convertDoubleToLaLon(double d) {
        long j = (long) d;
        double d2 = d - j;
        long j2 = (long) (d2 * 60.0d);
        long round = Math.round((d2 - (j2 / 60.0d)) * 3600.0d * 1.0E7d);
        return j + "/1," + j2 + "/1," + round + "/10000000";
    }

    public static boolean isValidateCoordinate(double d, double d2) {
        return !BaseMiscUtil.doubleEquals(d, SearchStatUtils.POW) && !BaseMiscUtil.doubleEquals(d2, SearchStatUtils.POW);
    }

    public static String getCityNameFromRes(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        int identifier = context.getResources().getIdentifier("cityname_" + str, "string", context.getPackageName());
        if (identifier == 0) {
            DefaultLogger.e("LocationUtil", "cannot find a res id for %s", str);
            return null;
        }
        return context.getResources().getString(identifier);
    }

    public static int convertIntLat(double d) {
        return (int) (d * INT_COORDINATE_FACTOR);
    }

    public static boolean isLocationValidate(String str) {
        return !TextUtils.isEmpty(str) && !"-1".equals(str) && !"-2".equals(str);
    }

    public static Address getInvalidAddress() {
        Address address = new Address(null);
        Bundle bundle = new Bundle(2);
        bundle.putBoolean("invalid_key", true);
        address.setExtras(bundle);
        return address;
    }

    public static boolean isInvalidAddress(Address address) {
        if (address == null) {
            return true;
        }
        return address.getExtras() != null && address.getExtras().getBoolean("invalid_key");
    }
}
