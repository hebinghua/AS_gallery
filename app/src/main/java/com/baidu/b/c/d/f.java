package com.baidu.b.c.d;

import java.security.DigestException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.MGF1ParameterSpec;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

/* loaded from: classes.dex */
public final class f {
    private static final Map<String, byte[]> h = Collections.synchronizedMap(new HashMap());
    private final int a;
    private final int b;
    private SecureRandom c;
    private final int d;
    private MessageDigest e;
    private MessageDigest f;
    private byte[] g;

    private f(int i, int i2, SecureRandom secureRandom, OAEPParameterSpec oAEPParameterSpec) {
        String str;
        this.a = i;
        this.b = i2;
        this.c = secureRandom;
        if (i2 >= 64) {
            if (i == 1 || i == 2) {
                i2 -= 11;
            } else if (i != 3) {
                if (i != 4) {
                    throw new InvalidKeyException("Invalid padding: " + i);
                }
                byte[] bArr = null;
                String str2 = "SHA-1";
                if (oAEPParameterSpec != null) {
                    try {
                        str2 = oAEPParameterSpec.getDigestAlgorithm();
                        String mGFAlgorithm = oAEPParameterSpec.getMGFAlgorithm();
                        if (!mGFAlgorithm.equalsIgnoreCase("MGF1")) {
                            throw new InvalidAlgorithmParameterException("Unsupported MGF algo: " + mGFAlgorithm);
                        }
                        String digestAlgorithm = ((MGF1ParameterSpec) oAEPParameterSpec.getMGFParameters()).getDigestAlgorithm();
                        PSource pSource = oAEPParameterSpec.getPSource();
                        String algorithm = pSource.getAlgorithm();
                        if (!algorithm.equalsIgnoreCase("PSpecified")) {
                            throw new InvalidAlgorithmParameterException("Unsupported pSource algo: " + algorithm);
                        }
                        byte[] value = ((PSource.PSpecified) pSource).getValue();
                        str = digestAlgorithm;
                        bArr = value;
                    } catch (NoSuchAlgorithmException e) {
                        throw new InvalidKeyException("Digest " + str2 + " not available", e);
                    }
                } else {
                    str = str2;
                }
                this.e = MessageDigest.getInstance(str2);
                this.f = MessageDigest.getInstance(str);
                byte[] a = a(this.e, bArr);
                this.g = a;
                int length = (i2 - 2) - (a.length * 2);
                this.d = length;
                if (length > 0) {
                    return;
                }
                throw new InvalidKeyException("Key is too short for encryption using OAEPPadding with " + str2 + " and MGF1" + str);
            }
            this.d = i2;
            return;
        }
        throw new InvalidKeyException("Padded size must be at least 64");
    }

    public static f a(int i, int i2, SecureRandom secureRandom) {
        return new f(i, i2, secureRandom, null);
    }

    public static f a(int i, int i2, SecureRandom secureRandom, OAEPParameterSpec oAEPParameterSpec) {
        return new f(i, i2, secureRandom, oAEPParameterSpec);
    }

    private void a(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        byte[] bArr3 = new byte[4];
        byte[] bArr4 = new byte[20];
        while (i4 > 0) {
            this.f.update(bArr, i, i2);
            this.f.update(bArr3);
            try {
                this.f.digest(bArr4, 0, 20);
                for (int i5 = 0; i5 < 20 && i4 > 0; i5++) {
                    bArr2[i3] = (byte) (bArr4[i5] ^ bArr2[i3]);
                    i4--;
                    i3++;
                }
                if (i4 > 0) {
                    int i6 = 3;
                    while (true) {
                        byte b = (byte) (bArr3[i6] + 1);
                        bArr3[i6] = b;
                        if (b == 0 && i6 > 0) {
                            i6--;
                        }
                    }
                }
            } catch (DigestException e) {
                throw new BadPaddingException(e.toString());
            }
        }
    }

    private static byte[] a(MessageDigest messageDigest, byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            String algorithm = messageDigest.getAlgorithm();
            Map<String, byte[]> map = h;
            byte[] bArr2 = map.get(algorithm);
            if (bArr2 != null) {
                return bArr2;
            }
            byte[] digest = messageDigest.digest();
            map.put(algorithm, digest);
            return digest;
        }
        return messageDigest.digest(bArr);
    }

    private byte[] c(byte[] bArr) {
        int i;
        int i2;
        int i3 = this.b;
        byte[] bArr2 = new byte[i3];
        System.arraycopy(bArr, 0, bArr2, i3 - bArr.length, bArr.length);
        int length = (this.b - 3) - bArr.length;
        bArr2[0] = 0;
        int i4 = this.a;
        bArr2[1] = (byte) i4;
        int i5 = -1;
        int i6 = 2;
        if (i4 != 1) {
            if (this.c == null) {
                this.c = b.a;
            }
            byte[] bArr3 = new byte[64];
            while (true) {
                int i7 = length - 1;
                if (length <= 0) {
                    break;
                }
                while (true) {
                    if (i5 < 0) {
                        this.c.nextBytes(bArr3);
                        i5 = 63;
                    }
                    i = i5 - 1;
                    i2 = bArr3[i5] & 255;
                    if (i2 != 0) {
                        break;
                    }
                    i5 = i;
                }
                bArr2[i6] = (byte) i2;
                i5 = i;
                length = i7;
                i6++;
            }
        } else {
            while (true) {
                int i8 = length - 1;
                if (length <= 0) {
                    break;
                }
                bArr2[i6] = -1;
                i6++;
                length = i8;
            }
        }
        return bArr2;
    }

    private byte[] d(byte[] bArr) {
        if (bArr[0] == 0) {
            int i = 2;
            if (bArr[1] != this.a) {
                throw new BadPaddingException("Blocktype mismatch: " + ((int) bArr[1]));
            }
            while (true) {
                int i2 = i + 1;
                int i3 = bArr[i] & 255;
                if (i3 == 0) {
                    int length = bArr.length - i2;
                    if (length > this.d) {
                        throw new BadPaddingException("Padding string too short");
                    }
                    byte[] bArr2 = new byte[length];
                    System.arraycopy(bArr, bArr.length - length, bArr2, 0, length);
                    return bArr2;
                } else if (i2 == bArr.length) {
                    throw new BadPaddingException("Padding string not terminated");
                } else {
                    if (this.a == 1 && i3 != 255) {
                        throw new BadPaddingException("Padding byte not 0xff: " + i3);
                    }
                    i = i2;
                }
            }
        } else {
            throw new BadPaddingException("Data must start with zero");
        }
    }

    private byte[] e(byte[] bArr) {
        if (this.c == null) {
            this.c = b.a;
        }
        int length = this.g.length;
        byte[] bArr2 = new byte[length];
        this.c.nextBytes(bArr2);
        int i = this.b;
        byte[] bArr3 = new byte[i];
        System.arraycopy(bArr2, 0, bArr3, 1, length);
        int i2 = length + 1;
        int i3 = i - i2;
        int length2 = this.b - bArr.length;
        System.arraycopy(this.g, 0, bArr3, i2, length);
        bArr3[length2 - 1] = 1;
        System.arraycopy(bArr, 0, bArr3, length2, bArr.length);
        a(bArr3, 1, length, bArr3, i2, i3);
        a(bArr3, i2, i3, bArr3, 1, length);
        return bArr3;
    }

    private byte[] f(byte[] bArr) {
        int length = this.g.length;
        if (bArr[0] == 0) {
            int i = length + 1;
            int length2 = bArr.length - i;
            a(bArr, i, length2, bArr, 1, length);
            a(bArr, 1, length, bArr, i, length2);
            for (int i2 = 0; i2 < length; i2++) {
                if (this.g[i2] != bArr[i + i2]) {
                    throw new BadPaddingException("lHash mismatch");
                }
            }
            int i3 = i + length;
            while (bArr[i3] == 0) {
                i3++;
                if (i3 >= bArr.length) {
                    throw new BadPaddingException("Padding string not terminated");
                }
            }
            int i4 = i3 + 1;
            if (bArr[i3] != 1) {
                throw new BadPaddingException("Padding string not terminated by 0x01 byte");
            }
            int length3 = bArr.length - i4;
            byte[] bArr2 = new byte[length3];
            System.arraycopy(bArr, i4, bArr2, 0, length3);
            return bArr2;
        }
        throw new BadPaddingException("Data must start with zero");
    }

    public int a() {
        return this.d;
    }

    public byte[] a(byte[] bArr) {
        if (bArr.length > this.d) {
            throw new BadPaddingException("Data must be shorter than " + (this.d + 1) + " bytes");
        }
        int i = this.a;
        if (i == 1 || i == 2) {
            return c(bArr);
        }
        if (i == 3) {
            return bArr;
        }
        if (i != 4) {
            throw new AssertionError();
        }
        return e(bArr);
    }

    public byte[] a(byte[] bArr, int i, int i2) {
        return a(b.a(bArr, i, i2));
    }

    public byte[] b(byte[] bArr) {
        if (bArr.length != this.b) {
            throw new BadPaddingException("Padded length must be " + this.b);
        }
        int i = this.a;
        if (i == 1 || i == 2) {
            return d(bArr);
        }
        if (i == 3) {
            return bArr;
        }
        if (i != 4) {
            throw new AssertionError();
        }
        return f(bArr);
    }
}
