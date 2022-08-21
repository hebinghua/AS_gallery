package com.miui.gallery.util;

import android.util.Base64;
import com.xiaomi.onetrack.util.oaid.a;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import org.keyczar.Keyczar;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class Encode {
    public static final String[] HEXDIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", a.a, "b", "c", "d", "e", "f"};

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

    public static String SHA1Encode(byte[] bArr) {
        try {
            return byteArrayToHexString(MessageDigest.getInstance("sha1").digest(bArr));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encodeBase64(String str) {
        return Base64.encodeToString(str.getBytes(Charset.forName(Keyczar.DEFAULT_ENCODING)), 10);
    }

    public static String decodeBase64(String str) {
        return new String(Base64.decode(str.replace('/', '_').replace(Marker.ANY_NON_NULL_MARKER, "-").getBytes(Charset.forName(Keyczar.DEFAULT_ENCODING)), 10));
    }
}
