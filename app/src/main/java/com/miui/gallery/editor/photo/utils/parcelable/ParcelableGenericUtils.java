package com.miui.gallery.editor.photo.utils.parcelable;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import ch.qos.logback.core.joran.action.ActionConst;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class ParcelableGenericUtils {
    public static <T extends Parcelable> void writeList(Parcel parcel, int i, List<T> list) {
        if (list == null) {
            parcel.writeInt(-1);
        }
        parcel.writeInt(list.size());
        for (T t : list) {
            parcel.writeString(t.getClass().getName());
            parcel.writeParcelable(t, i);
        }
    }

    public static <T extends Parcelable> List<T> readList(Parcel parcel) {
        int readInt = parcel.readInt();
        if (readInt == -1) {
            return null;
        }
        ArrayList arrayList = new ArrayList(readInt);
        for (int i = 0; i < readInt; i++) {
            try {
                arrayList.add(parcel.readParcelable(Class.forName(parcel.readString()).getClassLoader()));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    public static <T extends Parcelable> void writeObject(Parcel parcel, int i, T t) {
        if (t == null) {
            parcel.writeString(ActionConst.NULL);
            return;
        }
        parcel.writeString(t.getClass().getName());
        parcel.writeParcelable(t, i);
    }

    public static <T extends Parcelable> T readObject(Parcel parcel) {
        try {
            String readString = parcel.readString();
            if (TextUtils.equals(readString, ActionConst.NULL)) {
                return null;
            }
            return (T) parcel.readParcelable(Class.forName(readString).getClassLoader());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Parcelable> void writeArray(Parcel parcel, int i, T[] tArr, Class<T> cls) {
        if (tArr == null) {
            parcel.writeInt(-1);
            return;
        }
        parcel.writeInt(tArr.length);
        parcel.writeString(cls.getName());
        for (T t : tArr) {
            parcel.writeParcelable(t, i);
        }
    }

    public static <T extends Parcelable> T[] readArray(Parcel parcel) {
        int readInt = parcel.readInt();
        T[] tArr = null;
        if (readInt == -1) {
            return null;
        }
        try {
            Class<?> cls = Class.forName(parcel.readString());
            T[] tArr2 = (T[]) ((Parcelable[]) Array.newInstance(cls, readInt));
            for (int i = 0; i < readInt; i++) {
                try {
                    tArr2[i] = parcel.readParcelable(cls.getClassLoader());
                } catch (ClassNotFoundException e) {
                    e = e;
                    tArr = tArr2;
                    e.printStackTrace();
                    return tArr;
                }
            }
            return tArr2;
        } catch (ClassNotFoundException e2) {
            e = e2;
        }
    }
}
