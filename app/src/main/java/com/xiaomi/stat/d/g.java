package com.xiaomi.stat.d;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class g {
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static MessageDigest a(String str) {
        try {
            return MessageDigest.getInstance(str);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static MessageDigest a() {
        return a("MD5");
    }

    public static byte[] a(byte[] bArr) {
        return a().digest(bArr);
    }

    public static byte[] b(String str) {
        return a(a(str, Keyczar.DEFAULT_ENCODING));
    }

    public static String b(byte[] bArr) {
        return a(a(bArr), true);
    }

    public static String c(String str) {
        return a(b(str), true);
    }

    private static MessageDigest b() {
        return a("SHA-256");
    }

    public static String d(String str) {
        return a(e(str), true);
    }

    public static byte[] e(String str) {
        return c(a(str, Keyczar.DEFAULT_ENCODING));
    }

    public static byte[] c(byte[] bArr) {
        return b().digest(bArr);
    }

    public static String d(byte[] bArr) {
        return a(c(bArr), true);
    }

    public static String a(byte[] bArr, boolean z) {
        return new String(a(bArr, z ? a : b));
    }

    private static char[] a(byte[] bArr, char[] cArr) {
        int length = bArr.length;
        char[] cArr2 = new char[length << 1];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + 1;
            cArr2[i] = cArr[(bArr[i2] & 240) >>> 4];
            i = i3 + 1;
            cArr2[i3] = cArr[bArr[i2] & 15];
        }
        return cArr2;
    }

    private static byte[] a(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(str2);
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }
}
