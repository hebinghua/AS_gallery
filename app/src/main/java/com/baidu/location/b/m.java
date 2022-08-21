package com.baidu.location.b;

import android.util.Base64;
import com.baidu.location.Jni;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class m {
    private boolean a;
    private String[] b;

    /* loaded from: classes.dex */
    public static class a {
        private static m a = new m();
    }

    private m() {
        this.a = false;
        this.b = null;
        try {
            String str = Jni.getldkaiv();
            if (str == null || !str.contains("|")) {
                return;
            }
            String[] split = str.split("\\|");
            this.b = split;
            if (split == null || split.length != 2) {
                return;
            }
            this.a = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static m a() {
        return a.a;
    }

    public synchronized String a(String str) {
        if (this.a) {
            try {
                IvParameterSpec ivParameterSpec = new IvParameterSpec(this.b[1].getBytes(Keyczar.DEFAULT_ENCODING));
                SecretKeySpec secretKeySpec = new SecretKeySpec(this.b[0].getBytes(Keyczar.DEFAULT_ENCODING), "AES");
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
                cipher.init(1, secretKeySpec, ivParameterSpec);
                return Base64.encodeToString(cipher.doFinal(str.getBytes()), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public synchronized String b(String str) {
        if (!this.a) {
            return null;
        }
        try {
            IvParameterSpec ivParameterSpec = new IvParameterSpec(this.b[1].getBytes(Keyczar.DEFAULT_ENCODING));
            SecretKeySpec secretKeySpec = new SecretKeySpec(this.b[0].getBytes(Keyczar.DEFAULT_ENCODING), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(2, secretKeySpec, ivParameterSpec);
            return new String(cipher.doFinal(Base64.decode(str, 0)), Keyczar.DEFAULT_ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean b() {
        return this.a;
    }
}
