package com.xiaomi.stat.d;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.GregorianCalendar;
import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class b {
    private static final String a = "AndroidKeyStoreUtils";
    private static final String b = "AndroidKeyStore";
    private static final String c = "RSA/ECB/PKCS1Padding";
    private static final String d = "RSA_KEY";

    public static synchronized String a(Context context, String str) throws Exception {
        synchronized (b.class) {
            Cipher cipher = Cipher.getInstance(c);
            KeyStore keyStore = KeyStore.getInstance(b);
            keyStore.load(null);
            a(context, keyStore);
            Certificate certificate = keyStore.getCertificate(d);
            if (certificate != null) {
                cipher.init(1, certificate.getPublicKey());
                return Base64.encodeToString(cipher.doFinal(str.getBytes(Keyczar.DEFAULT_ENCODING)), 0);
            }
            return null;
        }
    }

    public static synchronized String b(Context context, String str) throws Exception {
        String str2;
        synchronized (b.class) {
            Cipher cipher = Cipher.getInstance(c);
            KeyStore keyStore = KeyStore.getInstance(b);
            keyStore.load(null);
            a(context, keyStore);
            cipher.init(2, (PrivateKey) keyStore.getKey(d, null));
            str2 = new String(cipher.doFinal(Base64.decode(str, 0)), Keyczar.DEFAULT_ENCODING);
        }
        return str2;
    }

    private static void a(Context context, KeyStore keyStore) {
        try {
            if (!keyStore.containsAlias(d)) {
                int i = Build.VERSION.SDK_INT;
                if (i < 18) {
                    return;
                }
                if (i < 23) {
                    a(context);
                } else {
                    a();
                }
            }
        } catch (Exception e) {
            k.d(a, "createKey e", e);
        }
    }

    private static void a(Context context) throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        GregorianCalendar gregorianCalendar2 = new GregorianCalendar();
        gregorianCalendar2.add(1, 1);
        KeyPairGeneratorSpec build = new KeyPairGeneratorSpec.Builder(context).setAlias(d).setSubject(new X500Principal("CN=RSA_KEY")).setSerialNumber(BigInteger.valueOf(1337L)).setStartDate(gregorianCalendar.getTime()).setEndDate(gregorianCalendar2.getTime()).build();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", b);
        keyPairGenerator.initialize(build);
        keyPairGenerator.generateKeyPair();
    }

    private static void a() throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException, InvalidAlgorithmParameterException, NoSuchProviderException, NoSuchAlgorithmException {
        Class<?> cls = Class.forName("android.security.keystore.KeyGenParameterSpec$Builder");
        Constructor<?> constructor = cls.getConstructor(String.class, Integer.TYPE);
        Class<?> cls2 = Class.forName("android.security.keystore.KeyProperties");
        Object newInstance = constructor.newInstance(d, Integer.valueOf(cls2.getDeclaredField("PURPOSE_ENCRYPT").getInt(null) | cls2.getDeclaredField("PURPOSE_DECRYPT").getInt(null)));
        cls.getMethod("setDigests", String[].class).invoke(newInstance, new String[]{(String) cls2.getDeclaredField("DIGEST_SHA256").get(null), (String) cls2.getDeclaredField("DIGEST_SHA512").get(null)});
        cls.getMethod("setEncryptionPaddings", String[].class).invoke(newInstance, new String[]{(String) cls2.getDeclaredField("ENCRYPTION_PADDING_RSA_PKCS1").get(null)});
        Object invoke = cls.getMethod("build", new Class[0]).invoke(newInstance, new Object[0]);
        Class<?> cls3 = Class.forName("java.security.KeyPairGenerator");
        KeyPairGenerator keyPairGenerator = (KeyPairGenerator) cls3.getMethod("getInstance", String.class, String.class).invoke(null, "RSA", b);
        cls3.getMethod("initialize", AlgorithmParameterSpec.class).invoke(keyPairGenerator, invoke);
        keyPairGenerator.generateKeyPair();
    }
}
