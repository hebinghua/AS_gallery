package com.miui.gallery.net.download;

import com.miui.gallery.util.logger.DefaultLogger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/* loaded from: classes2.dex */
public abstract class Verifier {
    public String mAlgorithm;
    public byte[] mHash;

    public Verifier(String str, byte[] bArr) {
        this.mAlgorithm = str;
        this.mHash = bArr;
    }

    public final boolean verify(byte[] bArr) {
        return Arrays.equals(bArr, this.mHash);
    }

    public MessageDigest getInstance() {
        try {
            return MessageDigest.getInstance(this.mAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            DefaultLogger.w("Verifier", e);
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static final class Sha1 extends Verifier {
        public Sha1(String str) {
            super("SHA-1", Verifier.decode(str, 40));
        }
    }

    public static byte[] decode(String str, int i) {
        if (str.length() != i) {
            throw new IllegalArgumentException();
        }
        byte[] bArr = new byte[str.length() / 2];
        for (int i2 = 0; i2 < str.length(); i2++) {
            int digit = Character.digit(str.charAt(i2), 16);
            if (digit == -1) {
                throw new IllegalArgumentException(str + " is not a hex string");
            }
            int i3 = i2 / 2;
            bArr[i3] = (byte) ((digit << (i2 % 2 == 0 ? 4 : 0)) | bArr[i3]);
        }
        return bArr;
    }
}
