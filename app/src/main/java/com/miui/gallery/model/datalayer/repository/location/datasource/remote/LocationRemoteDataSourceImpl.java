package com.miui.gallery.model.datalayer.repository.location.datasource.remote;

import android.location.Address;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.data.ReverseGeocoder;
import com.miui.gallery.ui.photodetail.usecase.GetLocationInfo;
import io.reactivex.Flowable;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public class LocationRemoteDataSourceImpl implements ILocationRemoteDataSource {
    public static /* synthetic */ Address $r8$lambda$mdDMEscTQyHuxwxMyfzsLheSXeE(GetLocationInfo.RequestBean requestBean) {
        return lambda$getLocationInfo$0(requestBean);
    }

    @Override // com.miui.gallery.model.datalayer.repository.location.datasource.remote.ILocationRemoteDataSource
    public Flowable<Address> getLocationInfo(final GetLocationInfo.RequestBean requestBean) {
        return Flowable.fromCallable(new Callable() { // from class: com.miui.gallery.model.datalayer.repository.location.datasource.remote.LocationRemoteDataSourceImpl$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return LocationRemoteDataSourceImpl.$r8$lambda$mdDMEscTQyHuxwxMyfzsLheSXeE(GetLocationInfo.RequestBean.this);
            }
        });
    }

    public static /* synthetic */ Address lambda$getLocationInfo$0(GetLocationInfo.RequestBean requestBean) throws Exception {
        Address queryCachedAddress = (!requestBean.isHaveId() || !LocationManager.getInstance().loadLocation(requestBean.getId()) || !requestBean.isHaveCoordinate()) ? null : LocationManager.getInstance().queryCachedAddress(requestBean.getLatitude(), requestBean.getLongitude());
        return (queryCachedAddress == null && (queryCachedAddress = new ReverseGeocoder(GalleryApp.sGetAndroidContext()).lookupAddress(requestBean.getLatitude(), requestBean.getLongitude(), true)) == null) ? LocationUtil.getInvalidAddress() : queryCachedAddress;
    }
}
