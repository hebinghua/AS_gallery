package com.baidu.mapapi.map;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class p implements Parcelable.Creator<MapStatus> {
    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public MapStatus createFromParcel(Parcel parcel) {
        return new MapStatus(parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public MapStatus[] newArray(int i) {
        return new MapStatus[i];
    }
}
