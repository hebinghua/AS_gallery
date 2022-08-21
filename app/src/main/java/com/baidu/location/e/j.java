package com.baidu.location.e;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.Jni;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;

/* loaded from: classes.dex */
public class j {
    public static float A = 2.2f;
    public static float B = 2.3f;
    public static float C = 3.8f;
    public static int D = 3;
    public static int E = 10;
    public static int F = 2;
    public static int G = 7;
    public static int H = 20;
    public static int I = 70;
    public static int J = 120;
    public static float K = 2.0f;
    public static float L = 10.0f;
    public static float M = 50.0f;
    public static float N = 200.0f;
    public static int O = 16;
    public static int P = 32;
    public static float Q = 0.9f;
    public static int R = 10000;
    public static float S = 0.5f;
    public static float T = 0.0f;
    public static float U = 0.1f;
    public static int V = 30;
    public static int W = 100;
    public static int X = 0;
    public static int Y = 0;
    public static int Z = 0;
    public static boolean a = false;
    public static float aA = 0.75f;
    public static double aB = -0.10000000149011612d;
    public static int aC = 0;
    public static boolean aD = false;
    public static int aE = -1;
    public static int aF = 10;
    public static int aG = 3;
    public static int aH = 40;
    public static double[] aI = null;
    public static int aJ = 1;
    public static int aK = 1;
    public static int aL = 1;
    private static String aM = "http://loc.map.baidu.com/sdk.php";
    private static String aN = "http://loc.map.baidu.com/user_err.php";
    private static String aO = "http://loc.map.baidu.com/oqur.php";
    private static String aP = "https://loc.map.baidu.com/tcu.php";
    private static String aQ = "http://loc.map.baidu.com/rtbu.php";
    private static String aR = "http://loc.map.baidu.com/iofd.php";
    private static String aS = "http://loc.map.baidu.com/wloc";
    public static int aa = 420000;
    public static boolean ab = true;
    public static boolean ac = true;
    public static int ad = 20;
    public static int ae = 300;
    public static int af = 1000;
    public static int ag = Integer.MAX_VALUE;
    public static long ah = 900000;
    public static long ai = 420000;
    public static long aj = 180000;
    public static long ak = 0;
    public static long al = 15;
    public static long am = 300000;
    public static int an = 1000;
    public static int ao = 0;
    public static int ap = 30000;
    public static int aq = 30000;
    public static float ar = 10.0f;
    public static float as = 6.0f;
    public static float at = 10.0f;
    public static int au = 60;
    public static int av = 70;
    public static int aw = 6;
    public static String ax = null;
    public static boolean ay = false;
    public static int az = 16;
    public static boolean b = false;
    public static boolean c = false;
    public static int d = 0;
    public static String e = "https://loc.map.baidu.com/sdk_ep.php";
    public static String f = "https://loc.map.baidu.com/sdk.php";
    public static String g = "no";
    public static boolean h = false;
    public static boolean i = false;
    public static boolean j = false;
    public static boolean k = false;
    public static boolean l = false;
    public static boolean m = false;
    public static String n = "gcj02";
    public static String o = "";
    public static boolean p = true;
    public static int q = 3;
    public static double r = 0.0d;
    public static double s = 0.0d;
    public static double t = 0.0d;
    public static double u = 0.0d;
    public static int v = 0;
    public static byte[] w = null;
    public static boolean x = false;
    public static int y = 0;
    public static float z = 1.1f;

    public static double a(double d2, double d3, double d4, double d5) {
        float[] fArr = new float[1];
        Location.distanceBetween(d2, d3, d4, d5, fArr);
        return fArr[0];
    }

    public static int a(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(), "airplane_mode_on", 0);
        } catch (Exception unused) {
            return 2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0017 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0016 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int a(android.content.Context r4, java.lang.String r5) {
        /*
            r0 = 0
            r1 = 1
            int r2 = android.os.Process.myPid()     // Catch: java.lang.Exception -> L13
            int r3 = android.os.Process.myUid()     // Catch: java.lang.Exception -> L13
            int r4 = r4.checkPermission(r5, r2, r3)     // Catch: java.lang.Exception -> L13
            if (r4 != 0) goto L11
            goto L13
        L11:
            r4 = r0
            goto L14
        L13:
            r4 = r1
        L14:
            if (r4 != 0) goto L17
            return r0
        L17:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.j.a(android.content.Context, java.lang.String):int");
    }

    public static int a(Object obj, String str) throws Exception {
        Method declaredMethod = obj.getClass().getDeclaredMethod(str, null);
        if (!declaredMethod.isAccessible()) {
            declaredMethod.setAccessible(true);
        }
        return ((Integer) declaredMethod.invoke(obj, null)).intValue();
    }

    public static int a(String str, String str2, String str3) {
        int indexOf;
        int length;
        int indexOf2;
        String substring;
        if (str != null && !str.equals("") && (indexOf = str.indexOf(str2)) != -1 && (indexOf2 = str.indexOf(str3, (length = indexOf + str2.length()))) != -1 && (substring = str.substring(length, indexOf2)) != null && !substring.equals("")) {
            try {
                return Integer.parseInt(substring);
            } catch (NumberFormatException unused) {
            }
        }
        return Integer.MIN_VALUE;
    }

    public static String a() {
        Calendar calendar = Calendar.getInstance();
        int i2 = calendar.get(5);
        return String.format(Locale.CHINA, "%d-%02d-%02d %02d:%02d:%02d", Integer.valueOf(calendar.get(1)), Integer.valueOf(calendar.get(2) + 1), Integer.valueOf(i2), Integer.valueOf(calendar.get(11)), Integer.valueOf(calendar.get(12)), Integer.valueOf(calendar.get(13)));
    }

    public static String a(com.baidu.location.c.a aVar, com.baidu.location.c.h hVar, Location location, String str, int i2) {
        return a(aVar, hVar, location, str, i2, false);
    }

    public static String a(com.baidu.location.c.a aVar, com.baidu.location.c.h hVar, Location location, String str, int i2, boolean z2) {
        String b2;
        StringBuffer stringBuffer = new StringBuffer(2048);
        if (aVar != null && (b2 = com.baidu.location.c.b.a().b(aVar)) != null) {
            stringBuffer.append(b2);
        }
        if (hVar != null) {
            String b3 = i2 == 0 ? z2 ? hVar.b() : hVar.c() : hVar.d();
            if (b3 != null) {
                stringBuffer.append(b3);
            }
        }
        if (location != null) {
            String b4 = (d == 0 || i2 == 0) ? com.baidu.location.c.f.b(location) : com.baidu.location.c.f.c(location);
            if (b4 != null) {
                stringBuffer.append(b4);
            }
        }
        boolean z3 = false;
        if (i2 == 0) {
            z3 = true;
        }
        String a2 = b.a().a(z3);
        if (a2 != null) {
            stringBuffer.append(a2);
        }
        if (str != null) {
            stringBuffer.append(str);
        }
        stringBuffer.append(com.baidu.location.c.f.a().m());
        String a3 = com.baidu.location.c.b.a().a(aVar);
        if (a3 != null && a3.length() + stringBuffer.length() < 2000) {
            stringBuffer.append(a3);
        }
        String stringBuffer2 = stringBuffer.toString();
        if (location != null && hVar != null) {
            try {
                float speed = location.getSpeed();
                int i3 = d;
                int h2 = hVar.h();
                int a4 = hVar.a();
                boolean i4 = hVar.i();
                if (speed < as && ((i3 == 1 || i3 == 0) && (h2 < au || i4))) {
                    q = 1;
                } else if (speed < at && ((i3 == 1 || i3 == 0 || i3 == 3) && (h2 < av || a4 > aw))) {
                    q = 2;
                }
            } catch (Exception unused) {
                q = 3;
            }
            return stringBuffer2;
        }
        q = 3;
        return stringBuffer2;
    }

    public static String a(String str) {
        return Jni.en1(o + ";" + str);
    }

    public static String a(byte[] bArr, String str, boolean z2) {
        StringBuilder sb = new StringBuilder();
        for (byte b2 : bArr) {
            String hexString = Integer.toHexString(b2 & 255);
            if (z2) {
                hexString = hexString.toUpperCase();
            }
            if (hexString.length() == 1) {
                sb.append("0");
            }
            sb.append(hexString);
            sb.append(str);
        }
        return sb.toString();
    }

    public static String a(byte[] bArr, boolean z2) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(bArr);
            return a(messageDigest.digest(), "", z2);
        } catch (NoSuchAlgorithmException e2) {
            throw new RuntimeException(e2);
        }
    }

    public static boolean a(double d2, double d3) {
        return Math.abs(d2 - d3) <= 1.192092896E-7d;
    }

    public static boolean a(float f2, float f3) {
        return Math.abs(f2 - f3) <= 1.1920929E-7f;
    }

    public static boolean a(BDLocation bDLocation) {
        int locType = bDLocation.getLocType();
        return (locType > 100 && locType < 200) || locType == 62;
    }

    public static int b(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            try {
                return Settings.Secure.getInt(context.getContentResolver(), "location_mode", -1);
            } catch (Exception unused) {
                return -1;
            }
        }
        return -2;
    }

    public static boolean b() {
        return false;
    }

    public static boolean b(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 1);
            return true;
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean b(String str) {
        try {
            Class.forName(str);
            return true;
        } catch (Throwable unused) {
            return false;
        }
    }

    public static String c() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration<InetAddress> inetAddresses = networkInterfaces.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress nextElement = inetAddresses.nextElement();
                    if (!nextElement.isLoopbackAddress() && (nextElement instanceof Inet6Address) && nextElement.getHostAddress() != null && !nextElement.getHostAddress().startsWith("fe80:")) {
                        return nextElement.getHostAddress();
                    }
                }
            }
            return "";
        } catch (Throwable unused) {
            return "";
        }
    }

    public static String c(String str) {
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop " + str).getInputStream()), 1024);
        } catch (IOException unused) {
            bufferedReader = null;
        } catch (Throwable th) {
            th = th;
        }
        try {
            String readLine = bufferedReader.readLine();
            bufferedReader.close();
            try {
                bufferedReader.close();
            } catch (IOException unused2) {
            }
            if (!TextUtils.isEmpty(readLine)) {
                return readLine;
            }
            return null;
        } catch (IOException unused3) {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException unused4) {
                }
            }
            return null;
        } catch (Throwable th2) {
            th = th2;
            bufferedReader2 = bufferedReader;
            if (bufferedReader2 != null) {
                try {
                    bufferedReader2.close();
                } catch (IOException unused5) {
                }
            }
            throw th;
        }
    }

    public static boolean c(Context context) {
        int i2;
        if (context != null) {
            try {
                i2 = context.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION");
            } catch (Exception e2) {
                e2.printStackTrace();
                i2 = 0;
            }
            boolean z2 = i2 == 0;
            if (z2 && Build.VERSION.SDK_INT >= 23) {
                try {
                    if (Settings.Secure.getInt(context.getContentResolver(), "location_mode", 1) == 0) {
                        return false;
                    }
                } catch (Exception unused) {
                }
            }
            return z2;
        }
        return true;
    }

    public static String d() {
        return aM;
    }

    public static String d(Context context) {
        int a2 = a(context, "android.permission.ACCESS_COARSE_LOCATION");
        int a3 = a(context, "android.permission.ACCESS_FINE_LOCATION");
        int a4 = a(context, "android.permission.READ_PHONE_STATE");
        return "&per=" + a2 + "|" + a3 + "|" + a4;
    }

    public static String e() {
        return "https://daup.map.baidu.com/cltr/rcvr";
    }

    public static String e(Context context) {
        int i2 = -1;
        if (context != null) {
            try {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
                    i2 = activeNetworkInfo.getType();
                }
            } catch (Throwable unused) {
            }
        }
        return "&netc=" + i2;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x003f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String f() {
        /*
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 28
            r2 = 0
            if (r0 > r1) goto L20
            java.lang.String r0 = android.os.Environment.getExternalStorageState()     // Catch: java.lang.Exception -> L1c
            java.lang.String r3 = "mounted"
            boolean r0 = r0.equals(r3)     // Catch: java.lang.Exception -> L1c
            if (r0 == 0) goto L20
            java.io.File r0 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.lang.Exception -> L1c
            java.lang.String r0 = r0.getPath()     // Catch: java.lang.Exception -> L1c
            goto L21
        L1c:
            r0 = move-exception
            r0.printStackTrace()
        L20:
            r0 = r2
        L21:
            if (r0 != 0) goto L3d
            int r3 = android.os.Build.VERSION.SDK_INT
            if (r3 <= r1) goto L3d
            android.content.Context r1 = com.baidu.location.f.getServiceContext()
            if (r1 == 0) goto L3d
            android.content.Context r0 = com.baidu.location.f.getServiceContext()     // Catch: java.lang.Exception -> L3c
            java.lang.String r1 = android.os.Environment.DIRECTORY_MOVIES     // Catch: java.lang.Exception -> L3c
            java.io.File r0 = r0.getExternalFilesDir(r1)     // Catch: java.lang.Exception -> L3c
            java.lang.String r0 = r0.getAbsolutePath()     // Catch: java.lang.Exception -> L3c
            goto L3d
        L3c:
            r0 = r2
        L3d:
            if (r0 == 0) goto L64
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Exception -> L5f
            r1.<init>()     // Catch: java.lang.Exception -> L5f
            r1.append(r0)     // Catch: java.lang.Exception -> L5f
            java.lang.String r3 = "/baidu/tempdata"
            r1.append(r3)     // Catch: java.lang.Exception -> L5f
            java.lang.String r1 = r1.toString()     // Catch: java.lang.Exception -> L5f
            java.io.File r3 = new java.io.File     // Catch: java.lang.Exception -> L5f
            r3.<init>(r1)     // Catch: java.lang.Exception -> L5f
            boolean r1 = r3.exists()     // Catch: java.lang.Exception -> L5f
            if (r1 != 0) goto L64
            r3.mkdirs()     // Catch: java.lang.Exception -> L5f
            goto L64
        L5f:
            r0 = move-exception
            r0.printStackTrace()
            goto L65
        L64:
            r2 = r0
        L65:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.location.e.j.f():java.lang.String");
    }

    public static String g() {
        String f2 = f();
        if (f2 == null) {
            return null;
        }
        return f2 + "/baidu/tempdata";
    }

    public static String h() {
        try {
            File file = new File(com.baidu.location.f.getServiceContext().getFilesDir() + File.separator + "lldt");
            if (!file.exists()) {
                file.mkdirs();
            }
            return file.getAbsolutePath();
        } catch (Exception unused) {
            return null;
        }
    }

    public static String i() {
        try {
            File file = new File(com.baidu.location.f.getServiceContext().getFilesDir() + File.separator + "/baidu/tempdata");
            if (!file.exists()) {
                file.mkdirs();
            }
            return com.baidu.location.f.getServiceContext().getFilesDir().getPath();
        } catch (Exception unused) {
            return null;
        }
    }

    public static String j() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(com.baidu.location.f.getServiceContext().getFilesDir());
            String str = File.separator;
            sb.append(str);
            sb.append("/baidu/tempdata");
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.mkdirs();
            }
            return com.baidu.location.f.getServiceContext().getFilesDir().getPath() + str + "/baidu/tempdata";
        } catch (Exception unused) {
            return null;
        }
    }

    public static String k() {
        return c("ro.mediatek.platform");
    }
}
