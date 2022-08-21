package com.miui.gallery.util;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class ParcelableUtil {
    public static byte[] marshall(Parcelable parcelable) {
        Parcel obtain = Parcel.obtain();
        try {
            parcelable.writeToParcel(obtain, 0);
            return obtain.marshall();
        } finally {
            obtain.recycle();
        }
    }

    public static <T extends Parcelable> T unmarshall(byte[] bArr, Parcelable.Creator<T> creator) {
        Parcel parcel;
        try {
            parcel = unmarshall(bArr);
            try {
                T createFromParcel = creator.createFromParcel(parcel);
                if (parcel != null) {
                    parcel.recycle();
                }
                return createFromParcel;
            } catch (Throwable th) {
                th = th;
                if (parcel != null) {
                    parcel.recycle();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            parcel = null;
        }
    }

    public static Parcel unmarshall(byte[] bArr) {
        Parcel obtain = Parcel.obtain();
        obtain.unmarshall(bArr, 0, bArr.length);
        obtain.setDataPosition(0);
        return obtain;
    }

    public static void writeBool(Parcel parcel, Boolean bool) {
        int i;
        if (bool == null) {
            i = 0;
        } else {
            i = bool.booleanValue() ? 1 : 2;
        }
        parcel.writeByte((byte) i);
    }

    public static void writeBool(Parcel parcel, Boolean... boolArr) {
        int i;
        if (boolArr == null) {
            return;
        }
        for (Boolean bool : boolArr) {
            if (bool == null) {
                i = 0;
            } else {
                i = bool.booleanValue() ? 1 : 2;
            }
            parcel.writeByte((byte) i);
        }
    }

    public static Boolean readBool(Parcel parcel) {
        byte readByte = parcel.readByte();
        if (readByte == 0) {
            return null;
        }
        boolean z = true;
        if (readByte != 1) {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
