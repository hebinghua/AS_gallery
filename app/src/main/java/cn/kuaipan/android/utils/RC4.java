package cn.kuaipan.android.utils;

import java.security.InvalidKeyException;

/* loaded from: classes.dex */
public class RC4 {
    public final int[] sBox = new int[256];
    public int x;
    public int y;

    public void makeKey(byte[] bArr) throws InvalidKeyException {
        if (bArr == null) {
            throw new InvalidKeyException("Null user key");
        }
        int length = bArr.length;
        if (length == 0) {
            throw new InvalidKeyException("Invalid user key length");
        }
        this.x = 0;
        this.y = 0;
        for (int i = 0; i < 256; i++) {
            this.sBox[i] = i;
        }
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < 256; i4++) {
            int[] iArr = this.sBox;
            i3 = ((bArr[i2] & 255) + iArr[i4] + i3) & 255;
            int i5 = iArr[i4];
            iArr[i4] = iArr[i3];
            iArr[i3] = i5;
            i2 = (i2 + 1) % length;
        }
    }

    public void genRC4(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        int i4 = 0;
        while (i4 < i2) {
            int i5 = (this.x + 1) & 255;
            this.x = i5;
            int[] iArr = this.sBox;
            int i6 = (iArr[i5] + this.y) & 255;
            this.y = i6;
            int i7 = iArr[i5];
            iArr[i5] = iArr[i6];
            iArr[i6] = i7;
            bArr2[i3] = (byte) (bArr[i] ^ iArr[(iArr[i5] + iArr[i6]) & 255]);
            i4++;
            i3++;
            i++;
        }
    }

    public void skip(long j) {
        for (long j2 = 0; j2 < j; j2++) {
            int i = (this.x + 1) & 255;
            this.x = i;
            int[] iArr = this.sBox;
            int i2 = (iArr[i] + this.y) & 255;
            this.y = i2;
            int i3 = iArr[i];
            iArr[i] = iArr[i2];
            iArr[i2] = i3;
        }
    }
}
