package miuix.internal.hybrid;

import android.util.Base64;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

/* loaded from: classes3.dex */
public class SignUtils {
    public static PublicKey getPublicKey(KeySpec keySpec) throws Exception {
        return KeyFactory.getInstance("RSA").generatePublic(keySpec);
    }

    public static PublicKey getPublicKey(String str, int i) throws Exception {
        return getPublicKey(new X509EncodedKeySpec(Base64.decode(str, i)));
    }

    public static PublicKey getPublicKey(String str) throws Exception {
        return getPublicKey(str, 0);
    }

    public static boolean verify(byte[] bArr, PublicKey publicKey, byte[] bArr2, String str) throws Exception {
        Signature signature = Signature.getInstance(str);
        signature.initVerify(publicKey);
        signature.update(bArr);
        return signature.verify(bArr2);
    }

    public static boolean verify(String str, PublicKey publicKey, String str2, String str3) throws Exception {
        return verify(str.getBytes(), publicKey, Base64.decode(str2, 2), str3);
    }

    public static boolean verify(String str, PublicKey publicKey, String str2) throws Exception {
        return verify(str, publicKey, str2, "SHA1withRSA");
    }
}
