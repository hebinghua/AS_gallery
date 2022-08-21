package com.baidu.mapsdkplatform.comapi.util;

import android.content.Context;
import android.os.Build;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes.dex */
public class a {

    /* renamed from: com.baidu.mapsdkplatform.comapi.util.a$a  reason: collision with other inner class name */
    /* loaded from: classes.dex */
    public static class C0019a {
        public static String a(byte[] bArr) {
            char[] cArr = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuilder sb = new StringBuilder(bArr.length * 2);
            for (int i = 0; i < bArr.length; i++) {
                sb.append(cArr[(bArr[i] & 240) >> 4]);
                sb.append(cArr[bArr[i] & 15]);
            }
            return sb.toString();
        }
    }

    public static String a(Context context) {
        String packageName = context.getPackageName();
        String a = a(context, packageName);
        return a + ";" + packageName;
    }

    private static String a(Context context, String str) {
        String str2;
        try {
            str2 = a((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream((Build.VERSION.SDK_INT >= 28 ? context.getPackageManager().getPackageInfo(str, Box.MAX_BOX_SIZE).signingInfo.hasMultipleSigners() ? context.getPackageManager().getPackageInfo(str, Box.MAX_BOX_SIZE).signingInfo.getApkContentsSigners() : context.getPackageManager().getPackageInfo(str, Box.MAX_BOX_SIZE).signingInfo.getSigningCertificateHistory() : context.getPackageManager().getPackageInfo(str, 64).signatures)[0].toByteArray())));
        } catch (Exception e) {
            e.printStackTrace();
            str2 = "";
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < str2.length(); i++) {
            stringBuffer.append(str2.charAt(i));
            if (i > 0 && i % 2 == 1 && i < str2.length() - 1) {
                stringBuffer.append(":");
            }
        }
        return stringBuffer.toString();
    }

    public static String a(X509Certificate x509Certificate) {
        try {
            return C0019a.a(a(x509Certificate.getEncoded()));
        } catch (CertificateEncodingException unused) {
            return null;
        }
    }

    public static byte[] a(byte[] bArr) {
        try {
            return MessageDigest.getInstance("SHA1").digest(bArr);
        } catch (NoSuchAlgorithmException unused) {
            return null;
        }
    }
}
