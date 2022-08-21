package com.miui.gallery.model.datalayer.repository.location.datasource.remote;

import android.location.Address;
import com.miui.gallery.ui.photodetail.usecase.GetLocationInfo;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public interface ILocationRemoteDataSource {
    Flowable<Address> getLocationInfo(GetLocationInfo.RequestBean requestBean);
}
