package com.baidu.mapapi.map;

import android.os.Parcel;
import android.os.Parcelable;

/* loaded from: classes.dex */
final class l implements Parcelable.Creator<BaiduMapOptions> {
    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public BaiduMapOptions createFromParcel(Parcel parcel) {
        return new BaiduMapOptions(parcel);
    }

    @Override // android.os.Parcelable.Creator
    /* renamed from: a */
    public BaiduMapOptions[] newArray(int i) {
        return new BaiduMapOptions[i];
    }
}
