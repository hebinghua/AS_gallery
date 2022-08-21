package com.baidu.b.c.a;

import java.lang.reflect.Array;
import java.security.InvalidKeyException;

/* loaded from: classes.dex */
public final class b implements a {
    private static int[] g;
    private static int[] h;
    private static final byte[] i = new byte[256];
    private static final byte[] j = new byte[256];
    private static final int[] k = new int[256];
    private static final int[] l = new int[256];
    private static final int[] m = new int[256];
    private static final int[] n = new int[256];
    private static final int[] o = new int[256];
    private static final int[] p = new int[256];
    private static final int[] q = new int[256];
    private static final int[] r = new int[256];
    private static final int[] s = new int[256];
    private static final int[] t = new int[256];
    private static final int[] u = new int[256];
    private static final int[] v = new int[256];
    private static final byte[] w = new byte[30];
    private boolean b = false;
    private boolean c = false;
    private Object[] d = null;
    private int[] e = null;
    private int f = 0;

    static {
        int[] iArr = new int[256];
        g = iArr;
        h = new int[256];
        iArr[0] = 1;
        for (int i2 = 1; i2 < 256; i2++) {
            int[] iArr2 = g;
            int i3 = i2 - 1;
            int i4 = iArr2[i3] ^ (iArr2[i3] << 1);
            if ((i4 & 256) != 0) {
                i4 ^= 283;
            }
            iArr2[i2] = i4;
        }
        for (int i5 = 1; i5 < 255; i5++) {
            h[g[i5]] = i5;
        }
        byte[][] bArr = {new byte[]{1, 1, 1, 1, 1, 0, 0, 0}, new byte[]{0, 1, 1, 1, 1, 1, 0, 0}, new byte[]{0, 0, 1, 1, 1, 1, 1, 0}, new byte[]{0, 0, 0, 1, 1, 1, 1, 1}, new byte[]{1, 0, 0, 0, 1, 1, 1, 1}, new byte[]{1, 1, 0, 0, 0, 1, 1, 1}, new byte[]{1, 1, 1, 0, 0, 0, 1, 1}, new byte[]{1, 1, 1, 1, 0, 0, 0, 1}};
        byte[] bArr2 = {0, 1, 1, 0, 0, 0, 1, 1};
        byte[][] bArr3 = (byte[][]) Array.newInstance(byte.class, 256, 8);
        bArr3[1][7] = 1;
        for (int i6 = 2; i6 < 256; i6++) {
            int i7 = g[255 - h[i6]];
            for (int i8 = 0; i8 < 8; i8++) {
                bArr3[i6][i8] = (byte) ((i7 >>> (7 - i8)) & 1);
            }
        }
        byte[][] bArr4 = (byte[][]) Array.newInstance(byte.class, 256, 8);
        for (int i9 = 0; i9 < 256; i9++) {
            for (int i10 = 0; i10 < 8; i10++) {
                bArr4[i9][i10] = bArr2[i10];
                for (int i11 = 0; i11 < 8; i11++) {
                    byte[] bArr5 = bArr4[i9];
                    bArr5[i10] = (byte) (bArr5[i10] ^ (bArr[i10][i11] * bArr3[i9][i11]));
                }
            }
        }
        for (int i12 = 0; i12 < 256; i12++) {
            i[i12] = (byte) (bArr4[i12][0] << 7);
            for (int i13 = 1; i13 < 8; i13++) {
                byte[] bArr6 = i;
                bArr6[i12] = (byte) (bArr6[i12] ^ (bArr4[i12][i13] << (7 - i13)));
            }
            j[i[i12] & 255] = (byte) i12;
        }
        byte[][] bArr7 = {new byte[]{2, 1, 1, 3}, new byte[]{3, 2, 1, 1}, new byte[]{1, 3, 2, 1}, new byte[]{1, 1, 3, 2}};
        byte[][] bArr8 = (byte[][]) Array.newInstance(byte.class, 4, 8);
        for (int i14 = 0; i14 < 4; i14++) {
            for (int i15 = 0; i15 < 4; i15++) {
                bArr8[i14][i15] = bArr7[i14][i15];
            }
            bArr8[i14][i14 + 4] = 1;
        }
        byte[][] bArr9 = (byte[][]) Array.newInstance(byte.class, 4, 4);
        for (int i16 = 0; i16 < 4; i16++) {
            byte b = bArr8[i16][i16];
            if (b == 0) {
                int i17 = i16 + 1;
                while (bArr8[i17][i16] == 0 && i17 < 4) {
                    i17++;
                }
                if (i17 == 4) {
                    throw new RuntimeException("G matrix is not invertible");
                }
                for (int i18 = 0; i18 < 8; i18++) {
                    byte b2 = bArr8[i16][i18];
                    bArr8[i16][i18] = bArr8[i17][i18];
                    bArr8[i17][i18] = b2;
                }
                b = bArr8[i16][i16];
            }
            for (int i19 = 0; i19 < 8; i19++) {
                if (bArr8[i16][i19] != 0) {
                    byte[] bArr10 = bArr8[i16];
                    int[] iArr3 = g;
                    int[] iArr4 = h;
                    bArr10[i19] = (byte) iArr3[((iArr4[bArr8[i16][i19] & 255] + 255) - iArr4[b & 255]) % 255];
                }
            }
            for (int i20 = 0; i20 < 4; i20++) {
                if (i16 != i20) {
                    for (int i21 = i16 + 1; i21 < 8; i21++) {
                        byte[] bArr11 = bArr8[i20];
                        bArr11[i21] = (byte) (bArr11[i21] ^ a(bArr8[i16][i21], bArr8[i20][i16]));
                    }
                    bArr8[i20][i16] = 0;
                }
            }
        }
        for (int i22 = 0; i22 < 4; i22++) {
            for (int i23 = 0; i23 < 4; i23++) {
                bArr9[i22][i23] = bArr8[i22][i23 + 4];
            }
        }
        for (int i24 = 0; i24 < 256; i24++) {
            byte b3 = i[i24];
            k[i24] = a(b3, bArr7[0]);
            l[i24] = a(b3, bArr7[1]);
            m[i24] = a(b3, bArr7[2]);
            n[i24] = a(b3, bArr7[3]);
            byte b4 = j[i24];
            o[i24] = a(b4, bArr9[0]);
            p[i24] = a(b4, bArr9[1]);
            q[i24] = a(b4, bArr9[2]);
            r[i24] = a(b4, bArr9[3]);
            s[i24] = a(i24, bArr9[0]);
            t[i24] = a(i24, bArr9[1]);
            u[i24] = a(i24, bArr9[2]);
            v[i24] = a(i24, bArr9[3]);
        }
        int i25 = 1;
        w[0] = 1;
        for (int i26 = 1; i26 < 30; i26++) {
            i25 = a(2, i25);
            w[i26] = (byte) i25;
        }
        h = null;
        g = null;
    }

    private static final int a(int i2, int i3) {
        if (i2 == 0 || i3 == 0) {
            return 0;
        }
        int[] iArr = g;
        int[] iArr2 = h;
        return iArr[(iArr2[i2 & 255] + iArr2[i3 & 255]) % 255];
    }

    private static final int a(int i2, byte[] bArr) {
        int i3 = 0;
        if (i2 == 0) {
            return 0;
        }
        int[] iArr = h;
        int i4 = iArr[i2 & 255];
        int i5 = bArr[0] != 0 ? g[(iArr[bArr[0] & 255] + i4) % 255] & 255 : 0;
        int i6 = bArr[1] != 0 ? g[(iArr[bArr[1] & 255] + i4) % 255] & 255 : 0;
        int i7 = bArr[2] != 0 ? g[(iArr[bArr[2] & 255] + i4) % 255] & 255 : 0;
        if (bArr[3] != 0) {
            i3 = g[(i4 + iArr[bArr[3] & 255]) % 255] & 255;
        }
        return (i5 << 24) | (i6 << 16) | (i7 << 8) | i3;
    }

    private void a(boolean z) {
        int[][] iArr = (int[][]) this.d[z ? 1 : 0];
        int length = iArr.length;
        this.e = new int[length * 4];
        boolean z2 = false;
        for (int i2 = 0; i2 < length; i2++) {
            for (int i3 = 0; i3 < 4; i3++) {
                this.e[(i2 * 4) + i3] = iArr[i2][i3];
            }
        }
        if (z) {
            int[] iArr2 = this.e;
            int i4 = iArr2[iArr2.length - 4];
            int i5 = iArr2[iArr2.length - 3];
            int i6 = iArr2[iArr2.length - 2];
            int i7 = iArr2[iArr2.length - 1];
            for (int length2 = iArr2.length - 1; length2 > 3; length2--) {
                int[] iArr3 = this.e;
                iArr3[length2] = iArr3[length2 - 4];
            }
            int[] iArr4 = this.e;
            iArr4[0] = i4;
            iArr4[1] = i5;
            iArr4[2] = i6;
            iArr4[3] = i7;
        }
        this.b = length >= 13;
        if (length == 15) {
            z2 = true;
        }
        this.c = z2;
        this.f = (length - 1) * 4;
    }

    public static final boolean a(int i2) {
        int i3 = 0;
        while (true) {
            int[] iArr = a.a;
            if (i3 < iArr.length) {
                if (i2 == iArr[i3]) {
                    return true;
                }
                i3++;
            } else {
                return false;
            }
        }
    }

    private static Object[] a(byte[] bArr) {
        int i2;
        if (bArr != null) {
            if (!a(bArr.length)) {
                throw new InvalidKeyException("Invalid AES key length: " + bArr.length + " bytes");
            }
            int b = b(bArr.length);
            int i3 = b + 1;
            int i4 = i3 * 4;
            int i5 = 0;
            int[][] iArr = (int[][]) Array.newInstance(int.class, i3, 4);
            int[][] iArr2 = (int[][]) Array.newInstance(int.class, i3, 4);
            int length = bArr.length / 4;
            int[] iArr3 = new int[length];
            int i6 = 0;
            int i7 = 0;
            while (i6 < length) {
                iArr3[i6] = ((bArr[i7 + 2] & 255) << 8) | (bArr[i7] << 24) | ((bArr[i7 + 1] & 255) << 16) | (bArr[i7 + 3] & 255);
                i6++;
                i7 += 4;
            }
            int i8 = 0;
            int i9 = 0;
            while (i8 < length && i9 < i4) {
                int i10 = i9 / 4;
                int i11 = i9 % 4;
                iArr[i10][i11] = iArr3[i8];
                iArr2[b - i10][i11] = iArr3[i8];
                i8++;
                i9++;
            }
            int i12 = 0;
            while (i9 < i4) {
                int i13 = iArr3[length - 1];
                int i14 = iArr3[i5];
                byte[] bArr2 = i;
                int i15 = (((bArr2[(i13 >>> 16) & 255] << 24) ^ ((bArr2[(i13 >>> 8) & 255] & 255) << 16)) ^ ((bArr2[i13 & 255] & 255) << 8)) ^ (bArr2[i13 >>> 24] & 255);
                int i16 = i12 + 1;
                iArr3[i5] = ((w[i12] << 24) ^ i15) ^ i14;
                int i17 = 1;
                int i18 = i5;
                if (length != 8) {
                    while (i17 < length) {
                        iArr3[i17] = iArr3[i17] ^ iArr3[i18];
                        i17++;
                        i18++;
                    }
                } else {
                    while (true) {
                        i2 = length / 2;
                        if (i17 >= i2) {
                            break;
                        }
                        iArr3[i17] = iArr3[i17] ^ iArr3[i18];
                        i17++;
                        i18++;
                    }
                    int i19 = iArr3[i2 - 1];
                    int i20 = iArr3[i2];
                    byte[] bArr3 = i;
                    iArr3[i2] = ((bArr3[i19 >>> 24] << 24) ^ ((((bArr3[(i19 >>> 8) & 255] & 255) << 8) ^ (bArr3[i19 & 255] & 255)) ^ ((bArr3[(i19 >>> 16) & 255] & 255) << 16))) ^ i20;
                    int i21 = i2 + 1;
                    while (i21 < length) {
                        iArr3[i21] = iArr3[i21] ^ iArr3[i2];
                        i21++;
                        i2++;
                    }
                }
                int i22 = 0;
                while (i22 < length && i9 < i4) {
                    int i23 = i9 / 4;
                    int i24 = i9 % 4;
                    iArr[i23][i24] = iArr3[i22];
                    iArr2[b - i23][i24] = iArr3[i22];
                    i22++;
                    i9++;
                }
                i12 = i16;
                i5 = 0;
            }
            for (int i25 = 1; i25 < b; i25++) {
                for (int i26 = 0; i26 < 4; i26++) {
                    int i27 = iArr2[i25][i26];
                    iArr2[i25][i26] = v[i27 & 255] ^ ((s[(i27 >>> 24) & 255] ^ t[(i27 >>> 16) & 255]) ^ u[(i27 >>> 8) & 255]);
                }
            }
            return new Object[]{iArr, iArr2};
        }
        throw new InvalidKeyException("Empty key");
    }

    private static int b(int i2) {
        return (i2 >> 2) + 6;
    }

    public int a() {
        return 16;
    }

    public void a(boolean z, String str, byte[] bArr) {
        if (a(bArr.length)) {
            this.d = a(bArr);
            a(z);
            return;
        }
        throw new InvalidKeyException("Invalid AES key length: " + bArr.length + " bytes");
    }

    public void a(byte[] bArr, int i2, byte[] bArr2, int i3) {
        int i4 = i2 + 1;
        int i5 = i4 + 1;
        int i6 = i5 + 1;
        int i7 = ((bArr[i4] & 255) << 16) | (bArr[i2] << 24) | ((bArr[i5] & 255) << 8);
        int i8 = i6 + 1;
        int i9 = i7 | (bArr[i6] & 255);
        int[] iArr = this.e;
        int i10 = i9 ^ iArr[0];
        int i11 = i8 + 1;
        int i12 = i11 + 1;
        int i13 = (bArr[i8] << 24) | ((bArr[i11] & 255) << 16);
        int i14 = i12 + 1;
        int i15 = i13 | ((bArr[i12] & 255) << 8);
        int i16 = i14 + 1;
        int i17 = (i15 | (bArr[i14] & 255)) ^ iArr[1];
        int i18 = i16 + 1;
        int i19 = i18 + 1;
        int i20 = ((bArr[i18] & 255) << 16) | (bArr[i16] << 24);
        int i21 = i19 + 1;
        int i22 = i20 | ((bArr[i19] & 255) << 8);
        int i23 = i21 + 1;
        int i24 = (i22 | (bArr[i21] & 255)) ^ iArr[2];
        int i25 = i23 + 1;
        int i26 = i25 + 1;
        int i27 = iArr[3];
        int i28 = i27 ^ (((((bArr[i25] & 255) << 16) | (bArr[i23] << 24)) | ((bArr[i26] & 255) << 8)) | (bArr[i26 + 1] & 255));
        int i29 = 4;
        while (i29 < this.f) {
            int[] iArr2 = k;
            int i30 = iArr2[i10 >>> 24];
            int[] iArr3 = l;
            int i31 = i30 ^ iArr3[(i17 >>> 16) & 255];
            int[] iArr4 = m;
            int i32 = i31 ^ iArr4[(i24 >>> 8) & 255];
            int[] iArr5 = n;
            int i33 = i32 ^ iArr5[i28 & 255];
            int[] iArr6 = this.e;
            int i34 = i29 + 1;
            int i35 = iArr6[i29] ^ i33;
            int i36 = i34 + 1;
            int i37 = (((iArr2[i17 >>> 24] ^ iArr3[(i24 >>> 16) & 255]) ^ iArr4[(i28 >>> 8) & 255]) ^ iArr5[i10 & 255]) ^ iArr6[i34];
            int i38 = ((iArr2[i24 >>> 24] ^ iArr3[(i28 >>> 16) & 255]) ^ iArr4[(i10 >>> 8) & 255]) ^ iArr5[i17 & 255];
            int i39 = i36 + 1;
            int i40 = (((iArr3[(i10 >>> 16) & 255] ^ iArr2[i28 >>> 24]) ^ iArr4[(i17 >>> 8) & 255]) ^ iArr5[i24 & 255]) ^ iArr6[i39];
            i17 = i37;
            i24 = i38 ^ iArr6[i36];
            i28 = i40;
            i10 = i35;
            i29 = i39 + 1;
        }
        int[] iArr7 = this.e;
        int i41 = i29 + 1;
        int i42 = iArr7[i29];
        int i43 = i3 + 1;
        byte[] bArr3 = i;
        bArr2[i3] = (byte) (bArr3[i10 >>> 24] ^ (i42 >>> 24));
        int i44 = i43 + 1;
        bArr2[i43] = (byte) (bArr3[(i17 >>> 16) & 255] ^ (i42 >>> 16));
        int i45 = i44 + 1;
        bArr2[i44] = (byte) (bArr3[(i24 >>> 8) & 255] ^ (i42 >>> 8));
        int i46 = i45 + 1;
        bArr2[i45] = (byte) (i42 ^ bArr3[i28 & 255]);
        int i47 = i41 + 1;
        int i48 = iArr7[i41];
        int i49 = i46 + 1;
        bArr2[i46] = (byte) (bArr3[i17 >>> 24] ^ (i48 >>> 24));
        int i50 = i49 + 1;
        bArr2[i49] = (byte) (bArr3[(i24 >>> 16) & 255] ^ (i48 >>> 16));
        int i51 = i50 + 1;
        bArr2[i50] = (byte) (bArr3[(i28 >>> 8) & 255] ^ (i48 >>> 8));
        int i52 = i51 + 1;
        bArr2[i51] = (byte) (i48 ^ bArr3[i10 & 255]);
        int i53 = i47 + 1;
        int i54 = iArr7[i47];
        int i55 = i52 + 1;
        bArr2[i52] = (byte) (bArr3[i24 >>> 24] ^ (i54 >>> 24));
        int i56 = i55 + 1;
        bArr2[i55] = (byte) (bArr3[(i28 >>> 16) & 255] ^ (i54 >>> 16));
        int i57 = i56 + 1;
        bArr2[i56] = (byte) (bArr3[(i10 >>> 8) & 255] ^ (i54 >>> 8));
        int i58 = i57 + 1;
        bArr2[i57] = (byte) (i54 ^ bArr3[i17 & 255]);
        int i59 = iArr7[i53];
        int i60 = i58 + 1;
        bArr2[i58] = (byte) (bArr3[i28 >>> 24] ^ (i59 >>> 24));
        int i61 = i60 + 1;
        bArr2[i60] = (byte) (bArr3[(i10 >>> 16) & 255] ^ (i59 >>> 16));
        bArr2[i61] = (byte) (bArr3[(i17 >>> 8) & 255] ^ (i59 >>> 8));
        bArr2[i61 + 1] = (byte) (bArr3[i24 & 255] ^ i59);
    }

    public void b(byte[] bArr, int i2, byte[] bArr2, int i3) {
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9 = i2 + 1;
        int i10 = i9 + 1;
        int i11 = i10 + 1;
        int i12 = ((bArr[i9] & 255) << 16) | (bArr[i2] << 24) | ((bArr[i10] & 255) << 8);
        int i13 = i11 + 1;
        int i14 = i12 | (bArr[i11] & 255);
        int[] iArr = this.e;
        int i15 = i14 ^ iArr[4];
        int i16 = i13 + 1;
        int i17 = i16 + 1;
        int i18 = (bArr[i13] << 24) | ((bArr[i16] & 255) << 16);
        int i19 = i17 + 1;
        int i20 = i18 | ((bArr[i17] & 255) << 8);
        int i21 = i19 + 1;
        int i22 = (i20 | (bArr[i19] & 255)) ^ iArr[5];
        int i23 = i21 + 1;
        int i24 = i23 + 1;
        int i25 = ((bArr[i23] & 255) << 16) | (bArr[i21] << 24);
        int i26 = i24 + 1;
        int i27 = i25 | ((bArr[i24] & 255) << 8);
        int i28 = i26 + 1;
        int i29 = (i27 | (bArr[i26] & 255)) ^ iArr[6];
        int i30 = i28 + 1;
        int i31 = i30 + 1;
        int i32 = (((((bArr[i30] & 255) << 16) | (bArr[i28] << 24)) | ((bArr[i31] & 255) << 8)) | (bArr[i31 + 1] & 255)) ^ iArr[7];
        if (this.b) {
            int[] iArr2 = o;
            int i33 = iArr2[i15 >>> 24];
            int[] iArr3 = p;
            int i34 = i33 ^ iArr3[(i32 >>> 16) & 255];
            int[] iArr4 = q;
            int i35 = i34 ^ iArr4[(i29 >>> 8) & 255];
            int[] iArr5 = r;
            int i36 = (i35 ^ iArr5[i22 & 255]) ^ iArr[8];
            int i37 = (((iArr3[(i15 >>> 16) & 255] ^ iArr2[i22 >>> 24]) ^ iArr4[(i32 >>> 8) & 255]) ^ iArr5[i29 & 255]) ^ iArr[9];
            int i38 = (((iArr3[(i22 >>> 16) & 255] ^ iArr2[i29 >>> 24]) ^ iArr4[(i15 >>> 8) & 255]) ^ iArr5[i32 & 255]) ^ iArr[10];
            int i39 = iArr2[i32 >>> 24];
            int i40 = (iArr5[i15 & 255] ^ (iArr4[(i22 >>> 8) & 255] ^ (iArr3[(i29 >>> 16) & 255] ^ i39))) ^ iArr[11];
            i7 = iArr[12] ^ (((iArr2[i36 >>> 24] ^ iArr3[(i40 >>> 16) & 255]) ^ iArr4[(i38 >>> 8) & 255]) ^ iArr5[i37 & 255]);
            i6 = (((iArr2[i37 >>> 24] ^ iArr3[(i36 >>> 16) & 255]) ^ iArr4[(i40 >>> 8) & 255]) ^ iArr5[i38 & 255]) ^ iArr[13];
            i5 = (((iArr2[i38 >>> 24] ^ iArr3[(i37 >>> 16) & 255]) ^ iArr4[(i36 >>> 8) & 255]) ^ iArr5[i40 & 255]) ^ iArr[14];
            int i41 = (((iArr2[i40 >>> 24] ^ iArr3[(i38 >>> 16) & 255]) ^ iArr4[(i37 >>> 8) & 255]) ^ iArr5[i36 & 255]) ^ iArr[15];
            if (this.c) {
                int i42 = (((iArr2[i7 >>> 24] ^ iArr3[(i41 >>> 16) & 255]) ^ iArr4[(i5 >>> 8) & 255]) ^ iArr5[i6 & 255]) ^ iArr[16];
                int i43 = iArr[17] ^ (((iArr2[i6 >>> 24] ^ iArr3[(i7 >>> 16) & 255]) ^ iArr4[(i41 >>> 8) & 255]) ^ iArr5[i5 & 255]);
                int i44 = (((iArr2[i5 >>> 24] ^ iArr3[(i6 >>> 16) & 255]) ^ iArr4[(i7 >>> 8) & 255]) ^ iArr5[i41 & 255]) ^ iArr[18];
                int i45 = (((iArr2[i41 >>> 24] ^ iArr3[(i5 >>> 16) & 255]) ^ iArr4[(i6 >>> 8) & 255]) ^ iArr5[i7 & 255]) ^ iArr[19];
                i7 = iArr[20] ^ (((iArr2[i42 >>> 24] ^ iArr3[(i45 >>> 16) & 255]) ^ iArr4[(i44 >>> 8) & 255]) ^ iArr5[i43 & 255]);
                i6 = (((iArr2[i43 >>> 24] ^ iArr3[(i42 >>> 16) & 255]) ^ iArr4[(i45 >>> 8) & 255]) ^ iArr5[i44 & 255]) ^ iArr[21];
                i5 = (((iArr2[i44 >>> 24] ^ iArr3[(i43 >>> 16) & 255]) ^ iArr4[(i42 >>> 8) & 255]) ^ iArr5[i45 & 255]) ^ iArr[22];
                i4 = (((iArr2[i45 >>> 24] ^ iArr3[(i44 >>> 16) & 255]) ^ iArr4[(i43 >>> 8) & 255]) ^ iArr5[i42 & 255]) ^ iArr[23];
                i8 = 24;
            } else {
                i4 = i41;
                i8 = 16;
            }
        } else {
            i4 = i32;
            i5 = i29;
            i6 = i22;
            i7 = i15;
            i8 = 8;
        }
        int[] iArr6 = o;
        int i46 = iArr6[i7 >>> 24];
        int[] iArr7 = p;
        int i47 = i46 ^ iArr7[(i4 >>> 16) & 255];
        int[] iArr8 = q;
        int i48 = i47 ^ iArr8[(i5 >>> 8) & 255];
        int[] iArr9 = r;
        int i49 = i8 + 1;
        int i50 = iArr[i8] ^ (i48 ^ iArr9[i6 & 255]);
        int i51 = i49 + 1;
        int i52 = (((iArr6[i6 >>> 24] ^ iArr7[(i7 >>> 16) & 255]) ^ iArr8[(i4 >>> 8) & 255]) ^ iArr9[i5 & 255]) ^ iArr[i49];
        int i53 = i51 + 1;
        int i54 = (((iArr6[i5 >>> 24] ^ iArr7[(i6 >>> 16) & 255]) ^ iArr8[(i7 >>> 8) & 255]) ^ iArr9[i4 & 255]) ^ iArr[i51];
        int i55 = iArr9[i7 & 255] ^ ((iArr6[i4 >>> 24] ^ iArr7[(i5 >>> 16) & 255]) ^ iArr8[(i6 >>> 8) & 255]);
        int i56 = i53 + 1;
        int i57 = i55 ^ iArr[i53];
        int i58 = i56 + 1;
        int i59 = iArr[i56] ^ (((iArr6[i50 >>> 24] ^ iArr7[(i57 >>> 16) & 255]) ^ iArr8[(i54 >>> 8) & 255]) ^ iArr9[i52 & 255]);
        int i60 = i58 + 1;
        int i61 = (((iArr6[i52 >>> 24] ^ iArr7[(i50 >>> 16) & 255]) ^ iArr8[(i57 >>> 8) & 255]) ^ iArr9[i54 & 255]) ^ iArr[i58];
        int i62 = i60 + 1;
        int i63 = (((iArr6[i54 >>> 24] ^ iArr7[(i52 >>> 16) & 255]) ^ iArr8[(i50 >>> 8) & 255]) ^ iArr9[i57 & 255]) ^ iArr[i60];
        int i64 = iArr9[i50 & 255] ^ ((iArr6[i57 >>> 24] ^ iArr7[(i54 >>> 16) & 255]) ^ iArr8[(i52 >>> 8) & 255]);
        int i65 = i62 + 1;
        int i66 = i64 ^ iArr[i62];
        int i67 = i65 + 1;
        int i68 = iArr[i65] ^ (((iArr6[i59 >>> 24] ^ iArr7[(i66 >>> 16) & 255]) ^ iArr8[(i63 >>> 8) & 255]) ^ iArr9[i61 & 255]);
        int i69 = i67 + 1;
        int i70 = (((iArr6[i61 >>> 24] ^ iArr7[(i59 >>> 16) & 255]) ^ iArr8[(i66 >>> 8) & 255]) ^ iArr9[i63 & 255]) ^ iArr[i67];
        int i71 = i69 + 1;
        int i72 = (((iArr6[i63 >>> 24] ^ iArr7[(i61 >>> 16) & 255]) ^ iArr8[(i59 >>> 8) & 255]) ^ iArr9[i66 & 255]) ^ iArr[i69];
        int i73 = ((iArr6[i66 >>> 24] ^ iArr7[(i63 >>> 16) & 255]) ^ iArr8[(i61 >>> 8) & 255]) ^ iArr9[i59 & 255];
        int i74 = i71 + 1;
        int i75 = i73 ^ iArr[i71];
        int i76 = i74 + 1;
        int i77 = iArr[i74] ^ (((iArr6[i68 >>> 24] ^ iArr7[(i75 >>> 16) & 255]) ^ iArr8[(i72 >>> 8) & 255]) ^ iArr9[i70 & 255]);
        int i78 = i76 + 1;
        int i79 = (((iArr6[i70 >>> 24] ^ iArr7[(i68 >>> 16) & 255]) ^ iArr8[(i75 >>> 8) & 255]) ^ iArr9[i72 & 255]) ^ iArr[i76];
        int i80 = i78 + 1;
        int i81 = (((iArr6[i72 >>> 24] ^ iArr7[(i70 >>> 16) & 255]) ^ iArr8[(i68 >>> 8) & 255]) ^ iArr9[i75 & 255]) ^ iArr[i78];
        int i82 = ((iArr6[i75 >>> 24] ^ iArr7[(i72 >>> 16) & 255]) ^ iArr8[(i70 >>> 8) & 255]) ^ iArr9[i68 & 255];
        int i83 = i80 + 1;
        int i84 = i82 ^ iArr[i80];
        int i85 = i83 + 1;
        int i86 = iArr[i83] ^ (((iArr6[i77 >>> 24] ^ iArr7[(i84 >>> 16) & 255]) ^ iArr8[(i81 >>> 8) & 255]) ^ iArr9[i79 & 255]);
        int i87 = i85 + 1;
        int i88 = (((iArr6[i79 >>> 24] ^ iArr7[(i77 >>> 16) & 255]) ^ iArr8[(i84 >>> 8) & 255]) ^ iArr9[i81 & 255]) ^ iArr[i85];
        int i89 = i87 + 1;
        int i90 = (((iArr6[i81 >>> 24] ^ iArr7[(i79 >>> 16) & 255]) ^ iArr8[(i77 >>> 8) & 255]) ^ iArr9[i84 & 255]) ^ iArr[i87];
        int i91 = ((iArr6[i84 >>> 24] ^ iArr7[(i81 >>> 16) & 255]) ^ iArr8[(i79 >>> 8) & 255]) ^ iArr9[i77 & 255];
        int i92 = i89 + 1;
        int i93 = i91 ^ iArr[i89];
        int i94 = i92 + 1;
        int i95 = iArr[i92] ^ (((iArr6[i86 >>> 24] ^ iArr7[(i93 >>> 16) & 255]) ^ iArr8[(i90 >>> 8) & 255]) ^ iArr9[i88 & 255]);
        int i96 = i94 + 1;
        int i97 = (((iArr6[i88 >>> 24] ^ iArr7[(i86 >>> 16) & 255]) ^ iArr8[(i93 >>> 8) & 255]) ^ iArr9[i90 & 255]) ^ iArr[i94];
        int i98 = i96 + 1;
        int i99 = (((iArr6[i90 >>> 24] ^ iArr7[(i88 >>> 16) & 255]) ^ iArr8[(i86 >>> 8) & 255]) ^ iArr9[i93 & 255]) ^ iArr[i96];
        int i100 = ((iArr6[i93 >>> 24] ^ iArr7[(i90 >>> 16) & 255]) ^ iArr8[(i88 >>> 8) & 255]) ^ iArr9[i86 & 255];
        int i101 = i98 + 1;
        int i102 = i100 ^ iArr[i98];
        int i103 = i101 + 1;
        int i104 = iArr[i101] ^ (((iArr6[i95 >>> 24] ^ iArr7[(i102 >>> 16) & 255]) ^ iArr8[(i99 >>> 8) & 255]) ^ iArr9[i97 & 255]);
        int i105 = i103 + 1;
        int i106 = (((iArr6[i97 >>> 24] ^ iArr7[(i95 >>> 16) & 255]) ^ iArr8[(i102 >>> 8) & 255]) ^ iArr9[i99 & 255]) ^ iArr[i103];
        int i107 = i105 + 1;
        int i108 = (((iArr6[i99 >>> 24] ^ iArr7[(i97 >>> 16) & 255]) ^ iArr8[(i95 >>> 8) & 255]) ^ iArr9[i102 & 255]) ^ iArr[i105];
        int i109 = ((iArr6[i102 >>> 24] ^ iArr7[(i99 >>> 16) & 255]) ^ iArr8[(i97 >>> 8) & 255]) ^ iArr9[i95 & 255];
        int i110 = i107 + 1;
        int i111 = i109 ^ iArr[i107];
        int i112 = i110 + 1;
        int i113 = iArr[i110] ^ (((iArr6[i104 >>> 24] ^ iArr7[(i111 >>> 16) & 255]) ^ iArr8[(i108 >>> 8) & 255]) ^ iArr9[i106 & 255]);
        int i114 = i112 + 1;
        int i115 = (((iArr6[i106 >>> 24] ^ iArr7[(i104 >>> 16) & 255]) ^ iArr8[(i111 >>> 8) & 255]) ^ iArr9[i108 & 255]) ^ iArr[i112];
        int i116 = i114 + 1;
        int i117 = (((iArr6[i108 >>> 24] ^ iArr7[(i106 >>> 16) & 255]) ^ iArr8[(i104 >>> 8) & 255]) ^ iArr9[i111 & 255]) ^ iArr[i114];
        int i118 = ((iArr6[i111 >>> 24] ^ iArr7[(i108 >>> 16) & 255]) ^ iArr8[(i106 >>> 8) & 255]) ^ iArr9[i104 & 255];
        int i119 = i116 + 1;
        int i120 = i118 ^ iArr[i116];
        int i121 = i119 + 1;
        int i122 = iArr[i119] ^ (((iArr6[i113 >>> 24] ^ iArr7[(i120 >>> 16) & 255]) ^ iArr8[(i117 >>> 8) & 255]) ^ iArr9[i115 & 255]);
        int i123 = i121 + 1;
        int i124 = (((iArr6[i115 >>> 24] ^ iArr7[(i113 >>> 16) & 255]) ^ iArr8[(i120 >>> 8) & 255]) ^ iArr9[i117 & 255]) ^ iArr[i121];
        int i125 = (((iArr6[i117 >>> 24] ^ iArr7[(i115 >>> 16) & 255]) ^ iArr8[(i113 >>> 8) & 255]) ^ iArr9[i120 & 255]) ^ iArr[i123];
        int i126 = (((iArr6[i120 >>> 24] ^ iArr7[(i117 >>> 16) & 255]) ^ iArr8[(i115 >>> 8) & 255]) ^ iArr9[i113 & 255]) ^ iArr[i123 + 1];
        int i127 = iArr[0];
        int i128 = i3 + 1;
        byte[] bArr3 = j;
        bArr2[i3] = (byte) (bArr3[i122 >>> 24] ^ (i127 >>> 24));
        int i129 = i128 + 1;
        bArr2[i128] = (byte) (bArr3[(i126 >>> 16) & 255] ^ (i127 >>> 16));
        int i130 = i129 + 1;
        bArr2[i129] = (byte) (bArr3[(i125 >>> 8) & 255] ^ (i127 >>> 8));
        int i131 = i130 + 1;
        bArr2[i130] = (byte) (i127 ^ bArr3[i124 & 255]);
        int i132 = iArr[1];
        int i133 = i131 + 1;
        bArr2[i131] = (byte) (bArr3[i124 >>> 24] ^ (i132 >>> 24));
        int i134 = i133 + 1;
        bArr2[i133] = (byte) (bArr3[(i122 >>> 16) & 255] ^ (i132 >>> 16));
        int i135 = i134 + 1;
        bArr2[i134] = (byte) (bArr3[(i126 >>> 8) & 255] ^ (i132 >>> 8));
        int i136 = i135 + 1;
        bArr2[i135] = (byte) (i132 ^ bArr3[i125 & 255]);
        int i137 = iArr[2];
        int i138 = i136 + 1;
        bArr2[i136] = (byte) (bArr3[i125 >>> 24] ^ (i137 >>> 24));
        int i139 = i138 + 1;
        bArr2[i138] = (byte) (bArr3[(i124 >>> 16) & 255] ^ (i137 >>> 16));
        int i140 = i139 + 1;
        bArr2[i139] = (byte) (bArr3[(i122 >>> 8) & 255] ^ (i137 >>> 8));
        int i141 = i140 + 1;
        bArr2[i140] = (byte) (i137 ^ bArr3[i126 & 255]);
        int i142 = iArr[3];
        int i143 = i141 + 1;
        bArr2[i141] = (byte) (bArr3[i126 >>> 24] ^ (i142 >>> 24));
        int i144 = i143 + 1;
        bArr2[i143] = (byte) (bArr3[(i125 >>> 16) & 255] ^ (i142 >>> 16));
        bArr2[i144] = (byte) (bArr3[(i124 >>> 8) & 255] ^ (i142 >>> 8));
        bArr2[i144 + 1] = (byte) (bArr3[i122 & 255] ^ i142);
    }
}
