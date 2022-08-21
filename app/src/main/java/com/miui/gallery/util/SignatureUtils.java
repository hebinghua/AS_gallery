package com.miui.gallery.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/* loaded from: classes2.dex */
public class SignatureUtils {
    public static boolean checkIsPlatformApp(Context context, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        String certificateSHA1Fingerprint = getCertificateSHA1Fingerprint(context, str);
        if (!TextUtils.isEmpty(certificateSHA1Fingerprint)) {
            DefaultLogger.d("SignatureUtils", "checkIsAllowedApp cer " + certificateSHA1Fingerprint);
            DefaultLogger.d("SignatureUtils", "checkIsAllowedApp " + certificateSHA1Fingerprint.equalsIgnoreCase("7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25"));
            return certificateSHA1Fingerprint.equalsIgnoreCase("7B:6D:C7:07:9C:34:73:9C:E8:11:59:71:9F:B5:EB:61:D2:A0:32:25");
        }
        DefaultLogger.d("SignatureUtils", "check failed");
        return false;
    }

    public static String getCertificateSHA1Fingerprint(Context context, String str) {
        PackageInfo packageInfo;
        CertificateFactory certificateFactory;
        X509Certificate x509Certificate;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(str, 64);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            packageInfo = null;
        }
        if (packageInfo == null) {
            return null;
        }
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packageInfo.signatures[0].toByteArray());
        try {
            certificateFactory = CertificateFactory.getInstance("X509");
        } catch (Exception e2) {
            e2.printStackTrace();
            certificateFactory = null;
        }
        if (certificateFactory == null) {
            return null;
        }
        try {
            x509Certificate = (X509Certificate) certificateFactory.generateCertificate(byteArrayInputStream);
        } catch (Exception e3) {
            e3.printStackTrace();
            x509Certificate = null;
        }
        if (x509Certificate == null) {
            return null;
        }
        try {
            return byte2HexFormatted(MessageDigest.getInstance("SHA1").digest(x509Certificate.getEncoded()));
        } catch (NoSuchAlgorithmException | CertificateEncodingException e4) {
            e4.printStackTrace();
            return null;
        }
    }

    public static String byte2HexFormatted(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            String hexString = Integer.toHexString(bArr[i]);
            int length = hexString.length();
            if (length == 1) {
                hexString = "0" + hexString;
            }
            if (length > 2) {
                hexString = hexString.substring(length - 2, length);
            }
            sb.append(hexString.toUpperCase());
            if (i < bArr.length - 1) {
                sb.append(CoreConstants.COLON_CHAR);
            }
        }
        return sb.toString();
    }
}
