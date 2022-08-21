package com.miui.gallery.editor.photo.core.imports.text.utils;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class ParcelUtils {
    public static <T> T copy(Parcelable parcelable) {
        Parcel parcel;
        try {
            parcel = Parcel.obtain();
            try {
                parcel.writeParcelable(parcelable, 0);
                parcel.setDataPosition(0);
                T t = (T) parcel.readParcelable(parcelable.getClass().getClassLoader());
                parcel.recycle();
                return t;
            } catch (Throwable th) {
                th = th;
                parcel.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            parcel = null;
        }
    }
}
