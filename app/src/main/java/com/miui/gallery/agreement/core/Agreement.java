package com.miui.gallery.agreement.core;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
public class Agreement implements Parcelable {
    public static final Parcelable.Creator<Agreement> CREATOR = new Parcelable.Creator<Agreement>() { // from class: com.miui.gallery.agreement.core.Agreement.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Agreement mo543createFromParcel(Parcel parcel) {
            return new Agreement(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Agreement[] mo544newArray(int i) {
            return new Agreement[i];
        }
    };
    public final String mLink;
    public final boolean mRequired;
    public final String mText;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Agreement(String str, String str2, boolean z) {
        this.mText = str;
        this.mLink = str2;
        this.mRequired = z;
    }

    public Agreement(Parcel parcel) {
        this.mText = parcel.readString();
        this.mLink = parcel.readString();
        this.mRequired = parcel.readByte() != 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mText);
        parcel.writeString(this.mLink);
        parcel.writeByte(this.mRequired ? (byte) 1 : (byte) 0);
    }
}
