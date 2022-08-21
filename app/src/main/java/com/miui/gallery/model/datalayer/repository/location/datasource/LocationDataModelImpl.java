package com.miui.gallery.model.datalayer.repository.location.datasource;

import android.location.Address;
import com.miui.gallery.model.datalayer.repository.location.datasource.local.ILocationLocalDataSource;
import com.miui.gallery.model.datalayer.repository.location.datasource.local.LocationLocalDataSourceImpl;
import com.miui.gallery.model.datalayer.repository.location.datasource.remote.ILocationRemoteDataSource;
import com.miui.gallery.model.datalayer.repository.location.datasource.remote.LocationRemoteDataSourceImpl;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.photodetail.usecase.GetLocationInfo;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class LocationDataModelImpl implements ILocationDataModel {
    public final ILocationLocalDataSource mLocalDataSource = new LocationLocalDataSourceImpl();
    public final ILocationRemoteDataSource mRemoteDataSource = new LocationRemoteDataSourceImpl();

    @Override // com.miui.gallery.model.datalayer.repository.location.datasource.ILocationDataModel
    public Flowable<PageResults<Address>> getLocationInfo(GetLocationInfo.RequestBean requestBean) {
        if (requestBean.isHaveId() && requestBean.isHaveCoordinate()) {
            return Flowable.concatArrayDelayError(PageResults.wrapperDataToPageResult(2, this.mLocalDataSource.getLocationInfo(requestBean)), PageResults.wrapperDataToPageResult(3, this.mRemoteDataSource.getLocationInfo(requestBean)));
        }
        if (requestBean.isHaveId()) {
            return PageResults.wrapperDataToPageResult(2, this.mLocalDataSource.getLocationInfo(requestBean));
        }
        return PageResults.wrapperDataToPageResult(3, this.mRemoteDataSource.getLocationInfo(requestBean));
    }
}
