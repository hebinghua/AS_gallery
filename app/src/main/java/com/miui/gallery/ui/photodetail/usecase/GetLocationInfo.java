package com.miui.gallery.ui.photodetail.usecase;

import android.location.Address;
import com.miui.gallery.app.base.BaseUseCase;
import com.miui.gallery.model.datalayer.repository.ILocationRepository;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.search.statistics.SearchStatUtils;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public class GetLocationInfo extends BaseUseCase<PageResults<Address>, RequestBean> {
    public ILocationRepository mRepository;

    public GetLocationInfo(ILocationRepository iLocationRepository) {
        this.mRepository = iLocationRepository;
    }

    @Override // com.miui.gallery.base_optimization.clean.UseCase
    public Flowable<PageResults<Address>> buildUseCaseFlowable(RequestBean requestBean) {
        return this.mRepository.getLocationInfo(requestBean);
    }

    /* loaded from: classes2.dex */
    public static class RequestBean {
        public long cloudId;
        public double latitude;
        public double longitude;

        public RequestBean(long j, double d, double d2) {
            this.cloudId = j;
            this.latitude = d;
            this.longitude = d2;
        }

        public long getId() {
            return this.cloudId;
        }

        public double getLatitude() {
            return this.latitude;
        }

        public double getLongitude() {
            return this.longitude;
        }

        public boolean isHaveId() {
            return 0 != this.cloudId;
        }

        public boolean isHaveCoordinate() {
            return (SearchStatUtils.POW == this.latitude || SearchStatUtils.POW == this.longitude) ? false : true;
        }
    }
}
