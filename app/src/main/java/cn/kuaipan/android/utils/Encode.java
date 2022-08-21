package cn.kuaipan.android.utils;

import android.util.Log;
import com.xiaomi.onetrack.util.oaid.a;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/* loaded from: classes.dex */
public class Encode {
    public static final String[] HEXDIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", a.a, "b", "c", "d", "e", "f"};

    public static byte[] hexStringToByteArray(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) ((Character.digit(str.charAt(i2), 16) * 16) + Character.digit(str.charAt(i2 + 1), 16));
        }
        return bArr;
    }

    public static String byteArrayToHexString(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer(bArr.length * 2);
        for (int i = 0; i < bArr.length; i++) {
            String[] strArr = HEXDIGITS;
            stringBuffer.append(strArr[(bArr[i] >>> 4) & 15]);
            stringBuffer.append(strArr[bArr[i] & 15]);
        }
        return stringBuffer.toString();
    }

    public static String MD5Encode(byte[] bArr) {
        try {
            return byteArrayToHexString(MessageDigest.getInstance("MD5").digest(bArr));
        } catch (Exception e) {
            Log.e("Encode", "MD5Encode failed.", e);
            return null;
        }
    }

    public static String SHA1Encode(InputStream inputStream, int i) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha1");
            byte[] bArr = new byte[16384];
            int i2 = 0;
            do {
                int read = inputStream.read(bArr, 0, Math.min(16384, i - i2));
                if (read < 0) {
                    break;
                }
                messageDigest.update(bArr, 0, read);
                i2 += read;
            } while (i - i2 > 0);
            return byteArrayToHexString(messageDigest.digest());
        } catch (Exception e) {
            Log.e("Encode", "SHA1Encode failed.", e);
            return null;
        }
    }

    public static String SHA1Encode(FileChannel fileChannel, long j, long j2) {
        int i;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("sha1");
            byte[] bArr = new byte[8192];
            long j3 = j2 + j;
            fileChannel.position(j);
            do {
                int read = fileChannel.read(ByteBuffer.wrap(bArr));
                if (read < 0) {
                    break;
                }
                j += read;
                i = (j > j3 ? 1 : (j == j3 ? 0 : -1));
                if (i < 0) {
                    messageDigest.update(bArr, 0, read);
                    continue;
                } else {
                    messageDigest.update(bArr, 0, read - ((int) (j - j3)));
                    continue;
                }
            } while (i < 0);
            return byteArrayToHexString(messageDigest.digest());
        } catch (Exception e) {
            Log.e("Encode", "SHA1Encode failed.", e);
            return null;
        }
    }
}
