package com.miui.gallery.editor.photo.core;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public abstract class Metadata implements Comparable<Metadata>, Parcelable {
    public final String name;
    public final int priority;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Metadata(short s, String str) {
        this.name = str;
        this.priority = s;
    }

    @Override // java.lang.Comparable
    public int compareTo(Metadata metadata) {
        return this.priority - metadata.priority;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeInt(this.priority);
    }

    public Metadata(Parcel parcel) {
        this.name = parcel.readString();
        this.priority = parcel.readInt();
    }
}
