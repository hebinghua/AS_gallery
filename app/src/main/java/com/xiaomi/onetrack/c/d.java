package com.xiaomi.onetrack.c;

import android.text.TextUtils;
import com.xiaomi.onetrack.util.p;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class d {
    public static final char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static final char[] c = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static MessageDigest a(String str) {
        try {
            return MessageDigest.getInstance(str);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static MessageDigest a() {
        return a("MD5");
    }

    public static byte[] a(byte[] bArr) {
        return a().digest(bArr);
    }

    public static byte[] b(String str) {
        return a(a(str, Keyczar.DEFAULT_ENCODING));
    }

    public static String c(String str) {
        return a(b(str), true);
    }

    public static String a(byte[] bArr, boolean z) {
        return new String(a(bArr, z ? b : c));
    }

    public static char[] a(byte[] bArr, char[] cArr) {
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

    public static byte[] a(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(str2);
        } catch (UnsupportedEncodingException unused) {
            return null;
        }
    }

    public static String e(byte[] bArr) {
        String format;
        if (bArr != null) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(bArr);
                format = String.format("%1$032X", new BigInteger(1, messageDigest.digest()));
            } catch (Exception e) {
                p.b("DigestUtil", "getMD5 exception: " + e);
            }
            return format.toLowerCase();
        }
        format = "";
        return format.toLowerCase();
    }

    public static String h(String str) {
        return TextUtils.isEmpty(str) ? "" : e(str.getBytes());
    }
}
