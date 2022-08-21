package com.baidu.mapapi.search.core;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class a implements Parcelable.Creator<BuildingInfo> {
    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public BuildingInfo createFromParcel(Parcel parcel) {
        return new BuildingInfo(parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public BuildingInfo[] newArray(int i) {
        return new BuildingInfo[i];
    }
}
