package com.miui.gallery.data;

import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

/* loaded from: classes.dex */
public class CityDatabaseUtils {
    public static String TAG = "CityDatabaseUtils";

    /* loaded from: classes.dex */
    public static class PolygonHelper {
        public static <T> T parseFromByteArray(byte[] bArr) {
            ObjectInputStream objectInputStream;
            ByteArrayInputStream byteArrayInputStream;
            if (bArr == null || bArr.length == 0) {
                DefaultLogger.e(CityDatabaseUtils.TAG, "cannot parse polygon from a byte array, the byte array is null or empty");
                return null;
            }
            try {
                byteArrayInputStream = new ByteArrayInputStream(bArr);
                try {
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    try {
                        T t = (T) objectInputStream.readObject();
                        BaseMiscUtil.closeSilently(byteArrayInputStream);
                        BaseMiscUtil.closeSilently(objectInputStream);
                        return t;
                    } catch (Throwable th) {
                        th = th;
                        try {
                            th.printStackTrace();
                            return null;
                        } finally {
                            if (byteArrayInputStream != null) {
                                BaseMiscUtil.closeSilently(byteArrayInputStream);
                            }
                            if (objectInputStream != null) {
                                BaseMiscUtil.closeSilently(objectInputStream);
                            }
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    objectInputStream = null;
                }
            } catch (Throwable th3) {
                th = th3;
                objectInputStream = null;
                byteArrayInputStream = null;
            }
        }
    }
}
