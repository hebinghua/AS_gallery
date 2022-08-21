package com.adobe.xmp.impl;

import java.io.UnsupportedEncodingException;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class Latin1Converter {
    public static ByteBuffer convert(ByteBuffer byteBuffer) {
        if (Keyczar.DEFAULT_ENCODING.equals(byteBuffer.getEncoding())) {
            byte[] bArr = new byte[8];
            ByteBuffer byteBuffer2 = new ByteBuffer((byteBuffer.length() * 4) / 3);
            int i = 0;
            boolean z = false;
            int i2 = 0;
            int i3 = 0;
            while (i < byteBuffer.length()) {
                int charAt = byteBuffer.charAt(i);
                if (z) {
                    if (i2 > 0 && (charAt & 192) == 128) {
                        int i4 = i3 + 1;
                        bArr[i3] = (byte) charAt;
                        i2--;
                        if (i2 == 0) {
                            byteBuffer2.append(bArr, 0, i4);
                        } else {
                            i3 = i4;
                        }
                    } else {
                        byteBuffer2.append(convertToUTF8(bArr[0]));
                        i -= i3;
                    }
                    z = false;
                    i3 = 0;
                } else if (charAt < 127) {
                    byteBuffer2.append((byte) charAt);
                } else if (charAt >= 192) {
                    i2 = -1;
                    for (int i5 = charAt; i2 < 8 && (i5 & 128) == 128; i5 <<= 1) {
                        i2++;
                    }
                    bArr[i3] = (byte) charAt;
                    i3++;
                    z = true;
                } else {
                    byteBuffer2.append(convertToUTF8((byte) charAt));
                }
                i++;
            }
            if (z) {
                for (int i6 = 0; i6 < i3; i6++) {
                    byteBuffer2.append(convertToUTF8(bArr[i6]));
                }
            }
            return byteBuffer2;
        }
        return byteBuffer;
    }

    public static byte[] convertToUTF8(byte b) {
        int i = b & 255;
        if (i >= 128) {
            try {
                return (i == 129 || i == 141 || i == 143 || i == 144 || i == 157) ? new byte[]{32} : new String(new byte[]{b}, "cp1252").getBytes(Keyczar.DEFAULT_ENCODING);
            } catch (UnsupportedEncodingException unused) {
            }
        }
        return new byte[]{b};
    }
}
