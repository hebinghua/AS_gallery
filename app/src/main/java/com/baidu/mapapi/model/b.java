package com.baidu.mapapi.model;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class b implements Parcelable.Creator<LatLngBounds> {
    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public LatLngBounds createFromParcel(Parcel parcel) {
        return new LatLngBounds(parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public LatLngBounds[] newArray(int i) {
        return new LatLngBounds[i];
    }
}
