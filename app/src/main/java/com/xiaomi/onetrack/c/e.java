package com.xiaomi.onetrack.c;

import android.os.Build;
import com.xiaomi.onetrack.util.p;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/* loaded from: classes3.dex */
public class e {
    public static byte[] a(byte[] bArr) throws Exception {
        try {
            RSAPublicKey a = a("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiH0r18h2G+lOzZz0mSZT9liZY\r6ibWUv/biAioduf0zuRbWUYGb3pHobyCOaw2LpVnlf8CeCYtbRJhxL9skOyoU1Qa\rwGtoJzvVR4GbCo1MBTmZ8XThMprr0unRfzsu9GNV4+twciOdS2cNJB7INcwAYBFQ\r9vKpgXFoEjWRhIgwMwIDAQAB\r");
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
            cipher.init(1, a);
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            p.b(p.a("RsaUtils"), "RsaUtils encrypt exception:", e);
            return null;
        }
    }

    public static RSAPublicKey a(String str) throws Exception {
        KeyFactory keyFactory;
        if (Build.VERSION.SDK_INT >= 28) {
            keyFactory = KeyFactory.getInstance("RSA");
        } else {
            keyFactory = KeyFactory.getInstance("RSA", "BC");
        }
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(c.a(str)));
    }
}
