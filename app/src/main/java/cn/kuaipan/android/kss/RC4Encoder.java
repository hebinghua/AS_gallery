package cn.kuaipan.android.kss;

import cn.kuaipan.android.http.IKscDecoder;
import cn.kuaipan.android.utils.RC4;
import java.security.InvalidKeyException;

/* loaded from: classes.dex */
public class RC4Encoder implements IKscDecoder {
    public byte[] buf;
    public int bufUsed = 0;
    public final byte[] key;
    public final RC4 rc4;

    @Override // cn.kuaipan.android.http.IKscDecoder
    public boolean canEnd() {
        return true;
    }

    @Override // cn.kuaipan.android.http.IKscDecoder
    public boolean supportRepeat() {
        return true;
    }

    public RC4Encoder(byte[] bArr) throws InvalidKeyException {
        this.key = bArr;
        RC4 rc4 = new RC4();
        this.rc4 = rc4;
        rc4.makeKey(bArr);
        this.buf = new byte[8192];
    }

    @Override // cn.kuaipan.android.http.IKscDecoder
    public void init() {
        try {
            this.rc4.makeKey(this.key);
            this.bufUsed = 0;
        } catch (InvalidKeyException unused) {
        }
    }

    @Override // cn.kuaipan.android.http.IKscDecoder
    public void skip(long j) {
        try {
            this.rc4.makeKey(this.key);
            this.bufUsed = 0;
            this.rc4.skip(j);
        } catch (InvalidKeyException unused) {
        }
    }

    @Override // cn.kuaipan.android.http.IKscDecoder
    public void end() {
        this.bufUsed = 0;
    }

    @Override // cn.kuaipan.android.http.IKscDecoder
    public int needFill() {
        return Math.max(8192 - this.bufUsed, 0);
    }

    @Override // cn.kuaipan.android.http.IKscDecoder
    public synchronized void fillData(byte[] bArr, int i, int i2) {
        int i3 = this.bufUsed + i2;
        byte[] bArr2 = this.buf;
        if (i3 > bArr2.length) {
            int length = bArr2.length;
            while (true) {
                length <<= 1;
                if (i3 <= length) {
                    break;
                }
            }
            byte[] bArr3 = new byte[length];
            this.buf = bArr3;
            System.arraycopy(bArr2, 0, bArr3, 0, this.bufUsed);
        }
        this.rc4.genRC4(bArr, i, i2, bArr, i);
        System.arraycopy(bArr, i, this.buf, this.bufUsed, i2);
        this.bufUsed = i3;
    }

    @Override // cn.kuaipan.android.http.IKscDecoder
    public synchronized int readDecodeData(byte[] bArr, int i, int i2) {
        int i3 = this.bufUsed;
        if (i3 <= 0) {
            return 0;
        }
        int min = Math.min(Math.min(i3, i2), bArr.length - i);
        System.arraycopy(this.buf, 0, bArr, i, min);
        int i4 = this.bufUsed - min;
        if (i4 > 0) {
            byte[] bArr2 = this.buf;
            System.arraycopy(bArr2, min, bArr2, 0, i4);
        }
        this.bufUsed = i4;
        return min;
    }

    /* renamed from: clone */
    public RC4Encoder m173clone() {
        try {
            return new RC4Encoder(this.key);
        } catch (InvalidKeyException unused) {
            return null;
        }
    }
}
