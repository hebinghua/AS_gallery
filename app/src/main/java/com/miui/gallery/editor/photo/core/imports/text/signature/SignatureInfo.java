package com.miui.gallery.editor.photo.core.imports.text.signature;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes2.dex */
public class SignatureInfo implements Parcelable {
    public static final Parcelable.Creator<SignatureInfo> CREATOR = new Parcelable.Creator<SignatureInfo>() { // from class: com.miui.gallery.editor.photo.core.imports.text.signature.SignatureInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public SignatureInfo mo900createFromParcel(Parcel parcel) {
            return new SignatureInfo(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public SignatureInfo[] mo901newArray(int i) {
            return new SignatureInfo[i];
        }
    };
    public String path;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.path);
    }

    public SignatureInfo() {
    }

    public SignatureInfo(Parcel parcel) {
        this.path = parcel.readString();
    }
}
