package com.miui.mediaeditor.api;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public class ParcelableFunctionModel implements Parcelable {
    public static final Parcelable.Creator<ParcelableFunctionModel> CREATOR = new Parcelable.Creator<ParcelableFunctionModel>() { // from class: com.miui.mediaeditor.api.ParcelableFunctionModel.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ParcelableFunctionModel mo1837createFromParcel(Parcel parcel) {
            return new ParcelableFunctionModel(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ParcelableFunctionModel[] mo1838newArray(int i) {
            return new ParcelableFunctionModel[i];
        }
    };
    public boolean mDeviceSupport;
    public Bundle mExtraInfo;
    public String mFunctionClassName1;
    public String mFunctionClassName2;
    public String mFunctionDesc;
    public Uri mFunctionIcon;
    public int mFunctionLimitMaxSize;
    public int mFunctionLimitMinSize;
    public String mFunctionLoadCode;
    public String mFunctionName;
    public ArrayList<String> mFunctionSupportMimeType;
    public String mFunctionTag;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ParcelableFunctionModel(String str, Uri uri, String str2, int i, int i2, ArrayList<String> arrayList, String str3, String str4, String str5, String str6, boolean z, Bundle bundle) {
        this.mFunctionName = str;
        this.mFunctionIcon = uri;
        this.mFunctionDesc = str2;
        this.mFunctionLimitMaxSize = i;
        this.mFunctionLimitMinSize = i2;
        this.mFunctionSupportMimeType = arrayList;
        this.mFunctionClassName1 = str3;
        this.mFunctionClassName2 = str4;
        this.mFunctionLoadCode = str5;
        this.mFunctionTag = str6;
        this.mDeviceSupport = z;
        this.mExtraInfo = bundle;
    }

    public String getFunctionName() {
        return this.mFunctionName;
    }

    public Uri getFunctionIcon() {
        return this.mFunctionIcon;
    }

    public String getFunctionDesc() {
        return this.mFunctionDesc;
    }

    public int getFunctionLimitMaxSize() {
        return this.mFunctionLimitMaxSize;
    }

    public int getFunctionLimitMinSize() {
        return this.mFunctionLimitMinSize;
    }

    public ArrayList<String> getFunctionSupportMimeType() {
        return this.mFunctionSupportMimeType;
    }

    public String getFunctionClassName1() {
        return this.mFunctionClassName1;
    }

    public String getFunctionClassName2() {
        return this.mFunctionClassName2;
    }

    public String getFunctionLoadCode() {
        return this.mFunctionLoadCode;
    }

    public String getFunctionTag() {
        return this.mFunctionTag;
    }

    public boolean isDeviceSupport() {
        return this.mDeviceSupport;
    }

    public Bundle getExtraInfo() {
        return this.mExtraInfo;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mFunctionName);
        parcel.writeParcelable(this.mFunctionIcon, i);
        parcel.writeString(this.mFunctionDesc);
        parcel.writeInt(this.mFunctionLimitMaxSize);
        parcel.writeInt(this.mFunctionLimitMinSize);
        parcel.writeStringList(this.mFunctionSupportMimeType);
        parcel.writeString(this.mFunctionClassName1);
        parcel.writeString(this.mFunctionClassName2);
        parcel.writeString(this.mFunctionLoadCode);
        parcel.writeString(this.mFunctionTag);
        parcel.writeByte(this.mDeviceSupport ? (byte) 1 : (byte) 0);
        parcel.writeBundle(this.mExtraInfo);
    }

    public ParcelableFunctionModel(Parcel parcel) {
        this.mFunctionName = parcel.readString();
        this.mFunctionIcon = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
        this.mFunctionDesc = parcel.readString();
        this.mFunctionLimitMaxSize = parcel.readInt();
        this.mFunctionLimitMinSize = parcel.readInt();
        this.mFunctionSupportMimeType = parcel.createStringArrayList();
        this.mFunctionClassName1 = parcel.readString();
        this.mFunctionClassName2 = parcel.readString();
        this.mFunctionLoadCode = parcel.readString();
        this.mFunctionTag = parcel.readString();
        this.mDeviceSupport = parcel.readByte() != 0;
        this.mExtraInfo = parcel.readBundle();
    }
}
