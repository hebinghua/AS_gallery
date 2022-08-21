package com.miui.gallery.permission.core;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/* loaded from: classes2.dex */
public class Permission implements Parcelable {
    public static final Parcelable.Creator<Permission> CREATOR = new Parcelable.Creator<Permission>() { // from class: com.miui.gallery.permission.core.Permission.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public Permission mo1172createFromParcel(Parcel parcel) {
            return new Permission(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public Permission[] mo1173newArray(int i) {
            return new Permission[i];
        }
    };
    public final String mDesc;
    public final String mName;
    public final String mPermissionGroup;
    public final boolean mRequired;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public Permission(String str, String str2, String str3, boolean z) {
        this.mPermissionGroup = str;
        this.mName = str2;
        this.mDesc = str3;
        this.mRequired = z;
    }

    public Permission(String str, String str2, boolean z) {
        this.mPermissionGroup = "";
        this.mName = str;
        this.mDesc = str2;
        this.mRequired = z;
    }

    public Permission(Parcel parcel) {
        this.mPermissionGroup = parcel.readString();
        this.mName = parcel.readString();
        this.mDesc = parcel.readString();
        this.mRequired = parcel.readByte() != 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mPermissionGroup);
        parcel.writeString(this.mName);
        parcel.writeString(this.mDesc);
        parcel.writeByte(this.mRequired ? (byte) 1 : (byte) 0);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Permission) {
            if (TextUtils.isEmpty(this.mPermissionGroup)) {
                if (TextUtils.isEmpty(this.mName)) {
                    return false;
                }
                return this.mName.equals(((Permission) obj).mName);
            }
            return this.mPermissionGroup.equalsIgnoreCase(((Permission) obj).mPermissionGroup);
        }
        return false;
    }
}
