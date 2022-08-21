package com.meicam.sdk;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* loaded from: classes.dex */
public class NvsStatisticsInfo {
    private static int NV_OS_TYPE_ANDROID = 1;
    private Context context;
    private LocationListener locationListener = new LocationListener() { // from class: com.meicam.sdk.NvsStatisticsInfo.1
        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    };
    private LocationManager locationManager;

    public NvsStatisticsInfo(Context context) {
        this.context = null;
        this.locationManager = null;
        this.context = context;
        this.locationManager = (LocationManager) context.getSystemService("location");
    }

    public String getAppId() {
        return this.context.getApplicationInfo().packageName;
    }

    public String getStartTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
    }

    public String getDeviceId() {
        String string = Settings.Secure.getString(this.context.getContentResolver(), "android_id");
        String str = Build.SERIAL;
        if (Build.VERSION.SDK_INT >= 26 && this.context.checkSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
            str = Build.getSerial();
        }
        String str2 = string + str;
        try {
            return toMD5(str2).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str2;
        }
    }

    public String getModel() {
        return Build.MODEL;
    }

    public int getOsType() {
        return NV_OS_TYPE_ANDROID;
    }

    public String getOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public String getPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) this.context.getSystemService("phone");
        return (Build.VERSION.SDK_INT < 23 || this.context.checkSelfPermission("android.permission.READ_PHONE_STATE") != 0 || telephonyManager.getLine1Number() == null) ? "" : telephonyManager.getLine1Number();
    }

    /* JADX WARN: Removed duplicated region for block: B:18:0x0062  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.ArrayList getLngAndLat() {
        /*
            r14 = this;
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 0
            r3 = 23
            if (r0 < r3) goto L6e
            android.content.Context r0 = r14.context
            java.lang.String r3 = "android.permission.ACCESS_COARSE_LOCATION"
            int r0 = r0.checkSelfPermission(r3)
            if (r0 != 0) goto L39
            android.location.LocationManager r0 = r14.locationManager
            java.lang.String r3 = "network"
            boolean r0 = r0.isProviderEnabled(r3)
            if (r0 == 0) goto L39
            android.location.LocationManager r4 = r14.locationManager
            r6 = 1000(0x3e8, double:4.94E-321)
            r8 = 0
            android.location.LocationListener r9 = r14.locationListener
            java.lang.String r5 = "network"
            r4.requestLocationUpdates(r5, r6, r8, r9)
            android.location.LocationManager r0 = r14.locationManager
            android.location.Location r0 = r0.getLastKnownLocation(r3)
            if (r0 == 0) goto L39
            double r1 = r0.getLatitude()
            double r3 = r0.getLongitude()
            goto L3a
        L39:
            r3 = r1
        L3a:
            android.content.Context r0 = r14.context
            java.lang.String r5 = "android.permission.ACCESS_FINE_LOCATION"
            int r0 = r0.checkSelfPermission(r5)
            if (r0 != 0) goto L6a
            android.location.LocationManager r0 = r14.locationManager
            java.lang.String r5 = "gps"
            boolean r0 = r0.isProviderEnabled(r5)
            if (r0 == 0) goto L6a
            android.location.LocationManager r6 = r14.locationManager
            r8 = 1000(0x3e8, double:4.94E-321)
            r10 = 0
            android.location.LocationListener r11 = r14.locationListener
            java.lang.String r7 = "gps"
            r6.requestLocationUpdates(r7, r8, r10, r11)
            android.location.LocationManager r0 = r14.locationManager
            android.location.Location r0 = r0.getLastKnownLocation(r5)
            if (r0 == 0) goto L6a
            double r1 = r0.getLatitude()
            double r3 = r0.getLongitude()
        L6a:
            r12 = r1
            r1 = r3
            r3 = r12
            goto L6f
        L6e:
            r3 = r1
        L6f:
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.lang.Double r1 = java.lang.Double.valueOf(r1)
            r0.add(r1)
            java.lang.Double r1 = java.lang.Double.valueOf(r3)
            r0.add(r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.meicam.sdk.NvsStatisticsInfo.getLngAndLat():java.util.ArrayList");
    }

    private static String toMD5(String str) throws NoSuchAlgorithmException {
        byte[] digest = MessageDigest.getInstance("MD5").digest(str.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public void release() {
        this.locationManager.removeUpdates(this.locationListener);
        try {
            Field declaredField = this.locationManager.getClass().getDeclaredField("mContext");
            declaredField.setAccessible(true);
            declaredField.set(this.locationManager, null);
            Field declaredField2 = this.locationManager.getClass().getDeclaredField("mBatchedLocationCallbackTransport");
            declaredField2.setAccessible(true);
            declaredField2.set(this.locationManager, null);
            Field declaredField3 = this.locationManager.getClass().getDeclaredField("mGnssMeasurementCallbackTransport");
            declaredField3.setAccessible(true);
            declaredField3.set(this.locationManager, null);
            Field declaredField4 = this.locationManager.getClass().getDeclaredField("mGnssNavigationMessageCallbackTransport");
            declaredField4.setAccessible(true);
            declaredField4.set(this.locationManager, null);
        } catch (Exception e) {
            System.out.print(e);
        }
        this.locationListener = null;
        this.locationManager = null;
        this.context = null;
    }
}
