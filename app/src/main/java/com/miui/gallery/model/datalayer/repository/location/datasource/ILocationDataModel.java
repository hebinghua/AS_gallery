package com.miui.gallery.model.datalayer.repository.location.datasource;

import android.location.Address;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.ui.photodetail.usecase.GetLocationInfo;
import io.reactivex.Flowable;

/* loaded from: classes2.dex */
public interface ILocationDataModel {
    Flowable<PageResults<Address>> getLocationInfo(GetLocationInfo.RequestBean requestBean);
}
