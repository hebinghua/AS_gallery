package com.baidu.b.f;

import java.io.ByteArrayOutputStream;

/* loaded from: classes.dex */
public class a {
    private String a;
    private boolean b;
    private boolean c;

    public a(String str, boolean z, boolean z2) {
        this.a = str;
        this.b = z;
        this.c = z2;
    }

    private static int a(int i) {
        if (i != 1) {
            if (i == 2) {
                return 4;
            }
            if (i == 3) {
                return 3;
            }
            if (i == 4) {
                return 1;
            }
            return i != 5 ? -1 : 0;
        }
        return 6;
    }

    public String a(byte[] bArr) {
        int i;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (int i2 = 0; i2 < (bArr.length + 4) / 5; i2++) {
            short[] sArr = new short[5];
            int[] iArr = new int[8];
            int i3 = 5;
            for (int i4 = 0; i4 < 5; i4++) {
                int i5 = (i2 * 5) + i4;
                if (i5 < bArr.length) {
                    sArr[i4] = (short) (bArr[i5] & 255);
                } else {
                    sArr[i4] = 0;
                    i3--;
                }
            }
            int a = a(i3);
            iArr[0] = (byte) ((sArr[0] >> 3) & 31);
            iArr[1] = (byte) (((sArr[0] & 7) << 2) | ((sArr[1] >> 6) & 3));
            iArr[2] = (byte) ((sArr[1] >> 1) & 31);
            iArr[3] = (byte) (((sArr[1] & 1) << 4) | ((sArr[2] >> 4) & 15));
            iArr[4] = (byte) (((sArr[2] & 15) << 1) | ((sArr[3] >> 7) & 1));
            iArr[5] = (byte) ((sArr[3] >> 2) & 31);
            iArr[6] = (byte) (((sArr[4] >> 5) & 7) | ((sArr[3] & 3) << 3));
            iArr[7] = (byte) (sArr[4] & 31);
            int i6 = 0;
            while (true) {
                i = 8 - a;
                if (i6 >= i) {
                    break;
                }
                char charAt = this.a.charAt(iArr[i6]);
                if (this.c) {
                    charAt = Character.toLowerCase(charAt);
                }
                byteArrayOutputStream.write(charAt);
                i6++;
            }
            if (this.b) {
                while (i < 8) {
                    byteArrayOutputStream.write(61);
                    i++;
                }
            }
        }
        return new String(byteArrayOutputStream.toByteArray());
    }
}
