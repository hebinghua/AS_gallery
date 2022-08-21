package com.miui.gallery.model.datalayer.repository.location.datasource.local;

import android.location.Address;
import android.text.TextUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.ui.photodetail.usecase.GetLocationInfo;
import com.miui.gallery.util.GsonUtils;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/* loaded from: classes2.dex */
public class LocationLocalDataSourceImpl implements ILocationLocalDataSource {
    @Override // com.miui.gallery.model.datalayer.repository.location.datasource.local.ILocationLocalDataSource
    public Flowable<Address> getLocationInfo(final GetLocationInfo.RequestBean requestBean) {
        return Flowable.fromCallable(new Callable<Address>() { // from class: com.miui.gallery.model.datalayer.repository.location.datasource.local.LocationLocalDataSourceImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public Address mo1126call() throws Exception {
                String queryAddressByCloudId = CloudUtils.queryAddressByCloudId(GalleryApp.sGetAndroidContext(), requestBean.getId());
                if (TextUtils.isEmpty(queryAddressByCloudId) || queryAddressByCloudId.length() < 10) {
                    return LocationUtil.getInvalidAddress();
                }
                try {
                    Address fromJson = LocationManager.AddressParser.fromJson(queryAddressByCloudId);
                    return fromJson == null ? LocationUtil.getInvalidAddress() : fromJson;
                } catch (JsonSyntaxException unused) {
                    List list = (List) GsonUtils.fromJson(queryAddressByCloudId, new TypeToken<ArrayList<Address>>() { // from class: com.miui.gallery.model.datalayer.repository.location.datasource.local.LocationLocalDataSourceImpl.1.1
                    }.getType());
                    if (list.isEmpty()) {
                        return LocationUtil.getInvalidAddress();
                    }
                    return (Address) list.get(0);
                }
            }
        });
    }
}
