package com.miui.gallery.scanner.extra.snapshot;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class InsertedValue implements Parcelable {
    public static final Parcelable.Creator<InsertedValue> CREATOR = new Parcelable.Creator<InsertedValue>() { // from class: com.miui.gallery.scanner.extra.snapshot.InsertedValue.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public InsertedValue mo1307createFromParcel(Parcel parcel) {
            return new InsertedValue(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public InsertedValue[] mo1308newArray(int i) {
            return new InsertedValue[i];
        }
    };
    public final int mAlbumAttributes;
    public final long mAlbumId;
    public final String mPath;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public InsertedValue(String str, long j, int i) {
        this.mPath = str;
        this.mAlbumId = j;
        this.mAlbumAttributes = i;
    }

    public InsertedValue(Parcel parcel) {
        this.mPath = parcel.readString();
        this.mAlbumId = parcel.readLong();
        this.mAlbumAttributes = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mPath);
        parcel.writeLong(this.mAlbumId);
        parcel.writeInt(this.mAlbumAttributes);
    }
}
