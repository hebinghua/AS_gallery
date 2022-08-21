package com.baidu.b.a;

import java.io.ObjectStreamField;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/* loaded from: classes.dex */
public class b implements Serializable, Cloneable {
    public static final /* synthetic */ boolean a = true;
    private static final ObjectStreamField[] b = {new ObjectStreamField("bits", long[].class)};
    private long[] c;
    private transient int d;
    private transient boolean e;

    public b() {
        this.d = 0;
        this.e = false;
        g(64);
        this.e = false;
    }

    public b(int i) {
        this.d = 0;
        this.e = false;
        if (i >= 0) {
            g(i);
            this.e = true;
            return;
        }
        throw new NegativeArraySizeException("nbits < 0: " + i);
    }

    private b(long[] jArr) {
        this.d = 0;
        this.e = false;
        this.c = jArr;
        this.d = jArr.length;
        d();
    }

    public static b a(long[] jArr) {
        int length = jArr.length;
        while (length > 0 && jArr[length - 1] == 0) {
            length--;
        }
        return new b(Arrays.copyOf(jArr, length));
    }

    private void d() {
        int i;
        int i2;
        boolean z = a;
        if (z || (i2 = this.d) == 0 || this.c[i2 - 1] != 0) {
            if (!z && ((i = this.d) < 0 || i > this.c.length)) {
                throw new AssertionError();
            }
            if (z) {
                return;
            }
            int i3 = this.d;
            long[] jArr = this.c;
            if (i3 != jArr.length && jArr[i3] != 0) {
                throw new AssertionError();
            }
            return;
        }
        throw new AssertionError();
    }

    private static void d(int i, int i2) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("fromIndex < 0: " + i);
        } else if (i2 < 0) {
            throw new IndexOutOfBoundsException("toIndex < 0: " + i2);
        } else if (i <= i2) {
        } else {
            throw new IndexOutOfBoundsException("fromIndex: " + i + " > toIndex: " + i2);
        }
    }

    private void e() {
        int i = this.d - 1;
        while (i >= 0 && this.c[i] == 0) {
            i--;
        }
        this.d = i + 1;
    }

    private static int f(int i) {
        return i >> 6;
    }

    private void f() {
        int i = this.d;
        long[] jArr = this.c;
        if (i != jArr.length) {
            this.c = Arrays.copyOf(jArr, i);
            d();
        }
    }

    private void g(int i) {
        this.c = new long[f(i - 1) + 1];
    }

    private void h(int i) {
        long[] jArr = this.c;
        if (jArr.length < i) {
            this.c = Arrays.copyOf(this.c, Math.max(jArr.length * 2, i));
            this.e = false;
        }
    }

    private void i(int i) {
        int i2 = i + 1;
        if (this.d < i2) {
            h(i2);
            this.d = i2;
        }
    }

    public void a(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("bitIndex < 0: " + i);
        }
        int f = f(i);
        i(f);
        long[] jArr = this.c;
        jArr[f] = jArr[f] | (1 << i);
        d();
    }

    public void a(int i, int i2) {
        d(i, i2);
        if (i == i2) {
            return;
        }
        int f = f(i);
        int f2 = f(i2 - 1);
        i(f2);
        long j = (-1) << i;
        long j2 = (-1) >>> (-i2);
        if (f == f2) {
            long[] jArr = this.c;
            jArr[f] = (j2 & j) | jArr[f];
        } else {
            long[] jArr2 = this.c;
            jArr2[f] = j | jArr2[f];
            while (true) {
                f++;
                if (f >= f2) {
                    break;
                }
                this.c[f] = -1;
            }
            long[] jArr3 = this.c;
            jArr3[f2] = j2 | jArr3[f2];
        }
        d();
    }

    public void a(int i, int i2, boolean z) {
        if (z) {
            a(i, i2);
        } else {
            b(i, i2);
        }
    }

    public void a(int i, boolean z) {
        if (z) {
            a(i);
        } else {
            b(i);
        }
    }

    public void a(b bVar) {
        if (this == bVar) {
            return;
        }
        while (true) {
            int i = this.d;
            if (i <= bVar.d) {
                break;
            }
            long[] jArr = this.c;
            int i2 = i - 1;
            this.d = i2;
            jArr[i2] = 0;
        }
        for (int i3 = 0; i3 < this.d; i3++) {
            long[] jArr2 = this.c;
            jArr2[i3] = jArr2[i3] & bVar.c[i3];
        }
        e();
        d();
    }

    public byte[] a() {
        int i = this.d;
        if (i == 0) {
            return new byte[0];
        }
        int i2 = i - 1;
        int i3 = i2 * 8;
        for (long j = this.c[i2]; j != 0; j >>>= 8) {
            i3++;
        }
        byte[] bArr = new byte[i3];
        ByteBuffer order = ByteBuffer.wrap(bArr).order(ByteOrder.LITTLE_ENDIAN);
        for (int i4 = 0; i4 < i2; i4++) {
            order.putLong(this.c[i4]);
        }
        for (long j2 = this.c[i2]; j2 != 0; j2 >>>= 8) {
            order.put((byte) (255 & j2));
        }
        return bArr;
    }

    public int b() {
        int i = this.d;
        if (i == 0) {
            return 0;
        }
        return ((i - 1) * 64) + (64 - Long.numberOfLeadingZeros(this.c[i - 1]));
    }

    public void b(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("bitIndex < 0: " + i);
        }
        int f = f(i);
        if (f >= this.d) {
            return;
        }
        long[] jArr = this.c;
        jArr[f] = jArr[f] & (~(1 << i));
        e();
        d();
    }

    public void b(int i, int i2) {
        int f;
        d(i, i2);
        if (i != i2 && (f = f(i)) < this.d) {
            int f2 = f(i2 - 1);
            if (f2 >= this.d) {
                i2 = b();
                f2 = this.d - 1;
            }
            long j = (-1) << i;
            long j2 = (-1) >>> (-i2);
            if (f == f2) {
                long[] jArr = this.c;
                jArr[f] = (~(j2 & j)) & jArr[f];
            } else {
                long[] jArr2 = this.c;
                jArr2[f] = (~j) & jArr2[f];
                while (true) {
                    f++;
                    if (f >= f2) {
                        break;
                    }
                    this.c[f] = 0;
                }
                long[] jArr3 = this.c;
                jArr3[f2] = (~j2) & jArr3[f2];
            }
            e();
            d();
        }
    }

    public void b(b bVar) {
        if (this == bVar) {
            return;
        }
        int min = Math.min(this.d, bVar.d);
        int i = this.d;
        int i2 = bVar.d;
        if (i < i2) {
            h(i2);
            this.d = bVar.d;
        }
        for (int i3 = 0; i3 < min; i3++) {
            long[] jArr = this.c;
            jArr[i3] = jArr[i3] | bVar.c[i3];
        }
        if (min < bVar.d) {
            System.arraycopy(bVar.c, min, this.c, min, this.d - min);
        }
        d();
    }

    public int c() {
        int i = 0;
        for (int i2 = 0; i2 < this.d; i2++) {
            i += Long.bitCount(this.c[i2]);
        }
        return i;
    }

    public b c(int i, int i2) {
        int i3;
        long j;
        d(i, i2);
        d();
        int b2 = b();
        int i4 = 0;
        if (b2 <= i || i == i2) {
            return new b(0);
        }
        if (i2 > b2) {
            i2 = b2;
        }
        int i5 = i2 - i;
        b bVar = new b(i5);
        int f = f(i5 - 1) + 1;
        int f2 = f(i);
        int i6 = i & 63;
        boolean z = i6 == 0;
        while (true) {
            i3 = f - 1;
            if (i4 >= i3) {
                break;
            }
            long[] jArr = bVar.c;
            long[] jArr2 = this.c;
            jArr[i4] = z ? jArr2[f2] : (jArr2[f2] >>> i) | (jArr2[f2 + 1] << (-i));
            i4++;
            f2++;
        }
        long j2 = (-1) >>> (-i2);
        long[] jArr3 = bVar.c;
        if (((i2 - 1) & 63) < i6) {
            long[] jArr4 = this.c;
            j = ((jArr4[f2 + 1] & j2) << (-i)) | (jArr4[f2] >>> i);
        } else {
            j = (this.c[f2] & j2) >>> i;
        }
        jArr3[i3] = j;
        bVar.d = f;
        bVar.e();
        bVar.d();
        return bVar;
    }

    public void c(b bVar) {
        int min = Math.min(this.d, bVar.d);
        int i = this.d;
        int i2 = bVar.d;
        if (i < i2) {
            h(i2);
            this.d = bVar.d;
        }
        for (int i3 = 0; i3 < min; i3++) {
            long[] jArr = this.c;
            jArr[i3] = jArr[i3] ^ bVar.c[i3];
        }
        int i4 = bVar.d;
        if (min < i4) {
            System.arraycopy(bVar.c, min, this.c, min, i4 - min);
        }
        e();
        d();
    }

    public boolean c(int i) {
        if (i >= 0) {
            d();
            int f = f(i);
            return f < this.d && (this.c[f] & (1 << i)) != 0;
        }
        throw new IndexOutOfBoundsException("bitIndex < 0: " + i);
    }

    public Object clone() {
        if (!this.e) {
            f();
        }
        try {
            b bVar = (b) super.clone();
            bVar.c = (long[]) this.c.clone();
            bVar.d();
            return bVar;
        } catch (CloneNotSupportedException unused) {
            throw new InternalError();
        }
    }

    public int d(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("fromIndex < 0: " + i);
        }
        d();
        int f = f(i);
        if (f >= this.d) {
            return -1;
        }
        long j = this.c[f] & ((-1) << i);
        while (j == 0) {
            f++;
            if (f == this.d) {
                return -1;
            }
            j = this.c[f];
        }
        return (f * 64) + Long.numberOfTrailingZeros(j);
    }

    public void d(b bVar) {
        for (int min = Math.min(this.d, bVar.d) - 1; min >= 0; min--) {
            long[] jArr = this.c;
            jArr[min] = jArr[min] & (~bVar.c[min]);
        }
        e();
        d();
    }

    public int e(int i) {
        if (i < 0) {
            throw new IndexOutOfBoundsException("fromIndex < 0: " + i);
        }
        d();
        int f = f(i);
        if (f >= this.d) {
            return i;
        }
        long j = (~this.c[f]) & ((-1) << i);
        while (j == 0) {
            f++;
            int i2 = this.d;
            if (f == i2) {
                return i2 * 64;
            }
            j = ~this.c[f];
        }
        return (f * 64) + Long.numberOfTrailingZeros(j);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof b)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        b bVar = (b) obj;
        d();
        bVar.d();
        if (this.d != bVar.d) {
            return false;
        }
        for (int i = 0; i < this.d; i++) {
            if (this.c[i] != bVar.c[i]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = this.d;
        long j = 1234;
        while (true) {
            i--;
            if (i >= 0) {
                j ^= this.c[i] * (i + 1);
            } else {
                return (int) ((j >> 32) ^ j);
            }
        }
    }

    public String toString() {
        d();
        int i = this.d;
        StringBuilder sb = new StringBuilder(((i > 128 ? c() : i * 64) * 6) + 2);
        sb.append('{');
        int d = d(0);
        if (d != -1) {
            sb.append(d);
            while (true) {
                d = d(d + 1);
                if (d < 0) {
                    break;
                }
                int e = e(d);
                do {
                    sb.append(", ");
                    sb.append(d);
                    d++;
                } while (d < e);
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
