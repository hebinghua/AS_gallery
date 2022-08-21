package com.baidu.mapapi.map.offline;

import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapsdkplatform.comapi.map.m;
import com.baidu.mapsdkplatform.comapi.map.p;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class OfflineMapUtil {
    public static MKOLSearchRecord getSearchRecordFromLocalCityInfo(m mVar) {
        m next;
        if (mVar == null) {
            return null;
        }
        MKOLSearchRecord mKOLSearchRecord = new MKOLSearchRecord();
        mKOLSearchRecord.cityID = mVar.a;
        mKOLSearchRecord.cityName = mVar.b;
        mKOLSearchRecord.cityType = mVar.d;
        long j = 0;
        if (mVar.a() != null) {
            ArrayList<MKOLSearchRecord> arrayList = new ArrayList<>();
            Iterator<m> it = mVar.a().iterator();
            while (it.hasNext()) {
                arrayList.add(getSearchRecordFromLocalCityInfo(it.next()));
                j += next.c;
                mKOLSearchRecord.childCities = arrayList;
            }
        }
        if (mKOLSearchRecord.cityType != 1) {
            j = mVar.c;
        }
        mKOLSearchRecord.dataSize = j;
        return mKOLSearchRecord;
    }

    public static MKOLUpdateElement getUpdatElementFromLocalMapElement(p pVar) {
        if (pVar == null) {
            return null;
        }
        MKOLUpdateElement mKOLUpdateElement = new MKOLUpdateElement();
        mKOLUpdateElement.cityID = pVar.a;
        mKOLUpdateElement.cityName = pVar.b;
        GeoPoint geoPoint = pVar.g;
        if (geoPoint != null) {
            mKOLUpdateElement.geoPt = CoordUtil.mc2ll(geoPoint);
        }
        mKOLUpdateElement.level = pVar.e;
        int i = pVar.i;
        mKOLUpdateElement.ratio = i;
        int i2 = pVar.h;
        mKOLUpdateElement.serversize = i2;
        if (i != 100) {
            i2 = (i2 / 100) * i;
        }
        mKOLUpdateElement.size = i2;
        mKOLUpdateElement.status = pVar.l;
        mKOLUpdateElement.update = pVar.j;
        return mKOLUpdateElement;
    }
}
