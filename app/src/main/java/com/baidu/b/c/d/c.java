package com.baidu.b.c.d;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.util.Locale;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.OAEPParameterSpec;

/* loaded from: classes.dex */
public final class c {
    private static final byte[] a = new byte[0];
    private int b;
    private f d;
    private byte[] f;
    private int g;
    private int h;
    private d i;
    private OAEPParameterSpec e = null;
    private String j = "SHA-1";
    private String c = "PKCS1Padding";

    /* JADX WARN: Removed duplicated region for block: B:14:0x002c  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00c6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void a(int r8, com.baidu.b.c.d.d r9, java.security.SecureRandom r10, java.security.spec.AlgorithmParameterSpec r11) {
        /*
            Method dump skipped, instructions count: 206
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.b.c.d.c.a(int, com.baidu.b.c.d.d, java.security.SecureRandom, java.security.spec.AlgorithmParameterSpec):void");
    }

    private byte[] a() {
        int i = this.g;
        byte[] bArr = this.f;
        if (i > bArr.length) {
            throw new IllegalBlockSizeException("Data must not be longer than " + this.f.length + " bytes");
        }
        try {
            int i2 = this.b;
            if (i2 == 1) {
                return b.a(this.d.a(bArr, 0, i), this.i);
            }
            if (i2 == 2) {
                throw new UnsupportedOperationException("only verify supported");
            }
            if (i2 == 3) {
                throw new UnsupportedOperationException("only verify supported");
            }
            if (i2 != 4) {
                throw new AssertionError("Internal error");
            }
            return this.d.b(b.a(b.a(bArr, 0, i), this.i));
        } finally {
            this.g = 0;
        }
    }

    private void b(byte[] bArr, int i, int i2) {
        int i3;
        if (i2 == 0 || bArr == null) {
            return;
        }
        int i4 = this.g;
        int i5 = i4 + i2;
        byte[] bArr2 = this.f;
        if (i5 > bArr2.length) {
            i3 = bArr2.length + 1;
        } else {
            System.arraycopy(bArr, i, bArr2, i4, i2);
            i3 = this.g + i2;
        }
        this.g = i3;
    }

    public void a(int i, d dVar, SecureRandom secureRandom) {
        try {
            a(i, dVar, secureRandom, null);
        } catch (InvalidAlgorithmParameterException e) {
            InvalidKeyException invalidKeyException = new InvalidKeyException("Wrong parameters");
            invalidKeyException.initCause(e);
            throw invalidKeyException;
        }
    }

    public void a(String str) {
        String str2 = "NoPadding";
        if (!str.equalsIgnoreCase(str2)) {
            str2 = "PKCS1Padding";
            if (!str.equalsIgnoreCase(str2)) {
                String lowerCase = str.toLowerCase(Locale.ENGLISH);
                if (lowerCase.equals("oaeppadding")) {
                    this.c = "OAEP";
                    return;
                } else if (!lowerCase.startsWith("oaepwith") || !lowerCase.endsWith("andmgf1padding")) {
                    throw new NoSuchPaddingException("Padding " + str + " not supported");
                } else {
                    this.c = "OAEP";
                    this.j = str.substring(8, str.length() - 14);
                    throw new NoSuchPaddingException("MessageDigest not available for " + str);
                }
            }
        }
        this.c = str2;
    }

    public byte[] a(byte[] bArr, int i, int i2) {
        b(bArr, i, i2);
        return a();
    }
}
