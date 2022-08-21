package com.market.sdk.utils;

import android.text.TextUtils;
import com.xiaomi.onetrack.util.oaid.a;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* loaded from: classes.dex */
public class Coder {
    public static final String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", a.a, "b", "c", "d", "e", "f"};

    public static final String encodeMD5(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return byteArrayToString(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.e("MarketManager", e.toString());
            return null;
        }
    }

    public static final String encodeMD5(File file) {
        byte[] bArr = new byte[1024];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                try {
                    try {
                        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                        while (true) {
                            int read = fileInputStream.read(bArr);
                            if (read > 0) {
                                messageDigest.update(bArr, 0, read);
                            } else {
                                try {
                                    break;
                                } catch (IOException e) {
                                    Log.e("MarketManager", e.toString());
                                }
                            }
                        }
                        fileInputStream.close();
                        return byteArrayToString(messageDigest.digest());
                    } catch (Throwable th) {
                        try {
                            fileInputStream.close();
                        } catch (IOException e2) {
                            Log.e("MarketManager", e2.toString());
                        }
                        throw th;
                    }
                } catch (IOException e3) {
                    Log.e("MarketManager", e3.toString());
                    try {
                        fileInputStream.close();
                    } catch (IOException e4) {
                        Log.e("MarketManager", e4.toString());
                    }
                    return null;
                }
            } catch (NoSuchAlgorithmException e5) {
                Log.e("MarketManager", e5.toString());
                try {
                    fileInputStream.close();
                } catch (IOException e6) {
                    Log.e("MarketManager", e6.toString());
                }
                return null;
            }
        } catch (FileNotFoundException e7) {
            Log.e("MarketManager", e7.toString());
            return null;
        }
    }

    public static String byteArrayToString(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (byte b : bArr) {
            stringBuffer.append(byteToHexString(b));
        }
        return stringBuffer.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:0:?, code lost:
        r3 = r3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String byteToHexString(byte r3) {
        /*
            if (r3 >= 0) goto L4
            int r3 = r3 + 256
        L4:
            int r0 = r3 / 16
            int r3 = r3 % 16
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String[] r2 = com.market.sdk.utils.Coder.hexDigits
            r0 = r2[r0]
            r1.append(r0)
            r3 = r2[r3]
            r1.append(r3)
            java.lang.String r3 = r1.toString()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.market.sdk.utils.Coder.byteToHexString(byte):java.lang.String");
    }
}
