package com.nexstreaming.kminternal.kinemaster.config;

import android.os.Build;
import android.os.Environment;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import com.nexstreaming.nexeditorsdk.BuildConfig;
import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* loaded from: classes3.dex */
public final class EditorGlobal {
    public static final String[] a = {".jpg", ".jpeg", ".png", ".webp"};
    public static final String[] b = {".mp4", ".3gp", ".3gpp", ".mov", ".k3g", ".acc", ".avi", ".wmv"};
    public static final String[] c = {".aac", ".mp3", ".3gp", ".3gpp"};
    public static final String d;
    public static final boolean e;
    public static final VersionType f;
    public static final DataUsage g;
    public static final boolean h;
    public static final String i;
    public static final boolean j;
    public static final boolean k;
    public static final Executor l;
    private static final Edition m;
    private static final Date n;
    private static String o;

    /* loaded from: classes3.dex */
    public enum DataUsage {
        WIFI_AND_MOBILE,
        WIFI_ONLY,
        NEVER,
        ASK_WIFI_OR_MOBILE,
        ASK_WIFI_MOBILE_NEVER
    }

    /* loaded from: classes3.dex */
    public enum Edition {
        DeviceLock,
        TimeLock,
        PlayStore
    }

    /* loaded from: classes3.dex */
    public enum VersionType {
        Alpha,
        Beta,
        Dev,
        RC,
        Release
    }

    public static final boolean a(int i2) {
        return i2 > 0;
    }

    public static String b() {
        return "keepfileFG2HJ6D4";
    }

    static {
        Edition edition = BuildConfig.KM_EDITION;
        m = edition;
        boolean z = false;
        n = a(0, 0, 0);
        d = null;
        Edition edition2 = Edition.DeviceLock;
        e = edition != edition2;
        f = VersionType.RC;
        Edition edition3 = Edition.PlayStore;
        g = edition == edition3 ? DataUsage.WIFI_AND_MOBILE : DataUsage.ASK_WIFI_MOBILE_NEVER;
        h = edition == edition2;
        o = "KineMaster";
        i = null;
        j = edition == edition3;
        if (edition == edition3) {
            z = true;
        }
        k = z;
        l = Executors.newCachedThreadPool();
    }

    public static NexEditor a() {
        if (a.a() != null) {
            return a.a().c();
        }
        return null;
    }

    public static void a(String str) {
        o = str;
    }

    public static int c() {
        int i2;
        Edition edition = m;
        int i3 = 0;
        if (edition != Edition.DeviceLock) {
            if (edition == Edition.TimeLock || edition == Edition.PlayStore) {
                i2 = 33878;
                while (i3 < 1) {
                    i2 = (i2 << 16) | 10309;
                    i3++;
                }
            }
            return (323616768 ^ i3) ^ 38286;
        }
        i2 = 0;
        while (i3 < 4) {
            i2 = (i2 << 8) | 32;
            i3++;
        }
        i3 = i2;
        return (323616768 ^ i3) ^ 38286;
    }

    public static String d() {
        return Build.MODEL;
    }

    public static File e() {
        return Environment.getExternalStorageDirectory();
    }

    public static File f() {
        return new File(e().getAbsolutePath() + File.separator + o);
    }

    public static File g() {
        StringBuilder sb = new StringBuilder();
        sb.append(e().getAbsolutePath());
        String str = File.separator;
        sb.append(str);
        sb.append(o);
        sb.append(str);
        sb.append("Plugins");
        return new File(sb.toString());
    }

    public static File h() {
        StringBuilder sb = new StringBuilder();
        sb.append(e().getAbsolutePath());
        String str = File.separator;
        sb.append(str);
        sb.append(o);
        sb.append(str);
        sb.append("AssetPlugins");
        return new File(sb.toString());
    }

    public static File i() {
        StringBuilder sb = new StringBuilder();
        sb.append(e().getAbsolutePath());
        String str = File.separator;
        sb.append(str);
        sb.append(o);
        sb.append(str);
        sb.append("Projects");
        sb.append(str);
        sb.append("Overlays");
        return new File(sb.toString());
    }

    private static Date a(int i2, int i3, int i4) {
        return a(i2, i3, i4, 0, 0, 0);
    }

    private static Date a(int i2, int i3, int i4, int i5, int i6, int i7) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        gregorianCalendar.set(i2, i3 - 1, i4, i5, i6, i7);
        return gregorianCalendar.getTime();
    }

    public static String b(String str) {
        return c("com.nexstreaming.kinemaster.builtin.watermark." + str);
    }

    private static String c(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int length = str.length();
        Random random = new Random();
        char[] cArr = new char[8];
        for (int i2 = 0; i2 < 8; i2++) {
            cArr[i2] = (char) (((random.nextInt() & 268435455) % 94) + 32);
        }
        int i3 = cArr[0] ^ '?';
        int i4 = cArr[1] ^ 129;
        sb.append(cArr);
        for (int i5 = 0; i5 < length; i5++) {
            sb.append((char) ((((((str.charAt(i5) - ' ') + "Ax/VXn_zsAiwFi[CITPC;y2c}*0B'S0-7&QznQlMa6U9gmSoighZeC&@$-hAaXiN".charAt((i3 + i5) % 64)) - cArr[(i4 + i5) % 8]) + 188) % 94) + 32));
        }
        return sb.toString();
    }
}
