package com.xiaomi.stat.d;

import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/* loaded from: classes3.dex */
public class o {
    private static final String a = "RsaUtils";
    private static final String b = "RSA/ECB/PKCS1Padding";
    private static final String c = "BC";
    private static final String d = "RSA";

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        try {
            RSAPublicKey a2 = a(bArr);
            Cipher cipher = Cipher.getInstance(b);
            cipher.init(1, a2);
            return cipher.doFinal(bArr2);
        } catch (Exception e) {
            k.d(a, "RsaUtils encrypt exception:", e);
            return null;
        }
    }

    private static RSAPublicKey a(byte[] bArr) throws Exception {
        return (RSAPublicKey) KeyFactory.getInstance(d).generatePublic(new X509EncodedKeySpec(bArr));
    }
}
