package com.miui.gallery.model.datalayer.repository.location;

import android.location.Address;
import com.miui.gallery.model.datalayer.repository.ILocationRepository;
import com.miui.gallery.model.datalayer.repository.location.datasource.ILocationDataModel;
import com.miui.gallery.model.datalayer.repository.location.datasource.LocationDataModelImpl;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.photodetail.usecase.GetLocationInfo;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class LocationRepositoryImpl implements ILocationRepository {
    public final ILocationDataModel mLocationDataModelImpl = new LocationDataModelImpl();

    @Override // com.miui.gallery.model.datalayer.repository.location.datasource.ILocationDataModel
    public Flowable<PageResults<Address>> getLocationInfo(GetLocationInfo.RequestBean requestBean) {
        return this.mLocationDataModelImpl.getLocationInfo(requestBean);
    }
}
