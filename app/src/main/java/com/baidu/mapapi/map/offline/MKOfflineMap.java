package com.baidu.mapapi.map.offline;

import com.baidu.mapsdkplatform.comapi.map.i;
import com.baidu.mapsdkplatform.comapi.map.m;
import com.baidu.mapsdkplatform.comapi.map.n;
import com.baidu.mapsdkplatform.comapi.map.p;
import com.baidu.mapsdkplatform.comapi.map.q;
import com.baidu.mapsdkplatform.comapi.map.r;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class MKOfflineMap {
    public static final int TYPE_DOWNLOAD_UPDATE = 0;
    public static final int TYPE_NETWORK_ERROR = 2;
    public static final int TYPE_NEW_OFFLINE = 6;
    public static final int TYPE_VER_UPDATE = 4;
    private static final String a = "MKOfflineMap";
    private n b;
    private MKOfflineMapListener c;

    public void destroy() {
        this.b.d(0);
        this.b.b((r) null);
        this.b.b();
        i.b();
    }

    public ArrayList<MKOLUpdateElement> getAllUpdateInfo() {
        ArrayList<q> e = this.b.e();
        if (e == null) {
            return null;
        }
        ArrayList<MKOLUpdateElement> arrayList = new ArrayList<>();
        Iterator<q> it = e.iterator();
        while (it.hasNext()) {
            arrayList.add(OfflineMapUtil.getUpdatElementFromLocalMapElement(it.next().a()));
        }
        return arrayList;
    }

    public ArrayList<MKOLSearchRecord> getHotCityList() {
        ArrayList<m> c = this.b.c();
        if (c == null) {
            return null;
        }
        ArrayList<MKOLSearchRecord> arrayList = new ArrayList<>();
        Iterator<m> it = c.iterator();
        while (it.hasNext()) {
            arrayList.add(OfflineMapUtil.getSearchRecordFromLocalCityInfo(it.next()));
        }
        return arrayList;
    }

    public ArrayList<MKOLSearchRecord> getOfflineCityList() {
        ArrayList<m> d = this.b.d();
        if (d == null) {
            return null;
        }
        ArrayList<MKOLSearchRecord> arrayList = new ArrayList<>();
        Iterator<m> it = d.iterator();
        while (it.hasNext()) {
            arrayList.add(OfflineMapUtil.getSearchRecordFromLocalCityInfo(it.next()));
        }
        return arrayList;
    }

    public MKOLUpdateElement getUpdateInfo(int i) {
        q g = this.b.g(i);
        if (g == null) {
            return null;
        }
        return OfflineMapUtil.getUpdatElementFromLocalMapElement(g.a());
    }

    @Deprecated
    public int importOfflineData() {
        return importOfflineData(false);
    }

    @Deprecated
    public int importOfflineData(boolean z) {
        ArrayList<q> e = this.b.e();
        int i = 0;
        if (e != null) {
            i = e.size();
        }
        int i2 = i;
        this.b.a(z, true);
        ArrayList<q> e2 = this.b.e();
        if (e2 != null) {
            i2 = e2.size();
        }
        return i2 - i;
    }

    public boolean init(MKOfflineMapListener mKOfflineMapListener) {
        i.a();
        n a2 = n.a();
        this.b = a2;
        if (a2 == null) {
            return false;
        }
        a2.a(new a(this));
        this.c = mKOfflineMapListener;
        return true;
    }

    public boolean pause(int i) {
        return this.b.c(i);
    }

    public boolean remove(int i) {
        return this.b.e(i);
    }

    public ArrayList<MKOLSearchRecord> searchCity(String str) {
        ArrayList<m> a2 = this.b.a(str);
        if (a2 == null) {
            return null;
        }
        ArrayList<MKOLSearchRecord> arrayList = new ArrayList<>();
        Iterator<m> it = a2.iterator();
        while (it.hasNext()) {
            arrayList.add(OfflineMapUtil.getSearchRecordFromLocalCityInfo(it.next()));
        }
        return arrayList;
    }

    public boolean start(int i) {
        int i2;
        n nVar = this.b;
        if (nVar == null) {
            return false;
        }
        if (nVar.e() != null) {
            Iterator<q> it = this.b.e().iterator();
            while (it.hasNext()) {
                p pVar = it.next().a;
                if (pVar.a == i) {
                    if (!pVar.j && (i2 = pVar.l) != 2 && i2 != 3 && i2 != 6) {
                        return false;
                    }
                    return this.b.b(i);
                }
            }
        }
        return this.b.a(i);
    }

    public boolean update(int i) {
        n nVar = this.b;
        if (nVar != null && nVar.e() != null) {
            Iterator<q> it = this.b.e().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                p pVar = it.next().a;
                if (pVar.a == i) {
                    if (pVar.j) {
                        return this.b.f(i);
                    }
                }
            }
        }
        return false;
    }
}
