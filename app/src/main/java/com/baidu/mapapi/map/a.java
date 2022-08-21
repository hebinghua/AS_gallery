package com.baidu.mapapi.map;

import android.os.Bundle;
import com.baidu.mapapi.map.Overlay;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class a implements Overlay.a {
    public final /* synthetic */ BaiduMap a;

    public a(BaiduMap baiduMap) {
        this.a = baiduMap;
    }

    @Override // com.baidu.mapapi.map.Overlay.a
    public void a(Overlay overlay) {
        boolean z;
        List list;
        List list2;
        List list3;
        com.baidu.mapsdkplatform.comapi.map.c cVar;
        com.baidu.mapsdkplatform.comapi.map.c cVar2;
        List list4;
        List list5;
        List list6;
        com.baidu.mapsdkplatform.comapi.map.c cVar3;
        List list7;
        com.baidu.mapsdkplatform.comapi.map.c cVar4;
        z = this.a.V;
        if (z) {
            return;
        }
        if (overlay != null) {
            list6 = this.a.k;
            if (list6.contains(overlay)) {
                Bundle a = overlay.a();
                cVar3 = this.a.j;
                if (cVar3 != null) {
                    cVar4 = this.a.j;
                    cVar4.d(a);
                }
                list7 = this.a.k;
                list7.remove(overlay);
            }
        }
        if (overlay != null) {
            list4 = this.a.m;
            if (list4.contains(overlay)) {
                list5 = this.a.m;
                list5.remove(overlay);
            }
        }
        if (overlay == null) {
            return;
        }
        list = this.a.l;
        if (!list.contains(overlay)) {
            return;
        }
        Marker marker = (Marker) overlay;
        if (marker.p == null) {
            return;
        }
        list2 = this.a.l;
        list2.remove(marker);
        list3 = this.a.l;
        if (list3.size() != 0) {
            return;
        }
        cVar = this.a.j;
        if (cVar == null) {
            return;
        }
        cVar2 = this.a.j;
        cVar2.b(false);
    }

    @Override // com.baidu.mapapi.map.Overlay.a
    public void b(Overlay overlay) {
        boolean z;
        List list;
        List list2;
        List list3;
        List list4;
        com.baidu.mapsdkplatform.comapi.map.c cVar;
        boolean z2;
        com.baidu.mapsdkplatform.comapi.map.c cVar2;
        List list5;
        List list6;
        com.baidu.mapsdkplatform.comapi.map.c cVar3;
        com.baidu.mapsdkplatform.comapi.map.c cVar4;
        List list7;
        com.baidu.mapsdkplatform.comapi.map.c cVar5;
        boolean z3;
        com.baidu.mapsdkplatform.comapi.map.c cVar6;
        List list8;
        z = this.a.V;
        if (z) {
            return;
        }
        if (overlay != null) {
            list4 = this.a.k;
            if (list4.contains(overlay)) {
                boolean z4 = false;
                if (overlay instanceof Marker) {
                    Marker marker = (Marker) overlay;
                    if (marker.b != null) {
                        ArrayList<BitmapDescriptor> arrayList = marker.p;
                        if (arrayList != null && arrayList.size() > 1) {
                            Bundle bundle = new Bundle();
                            cVar5 = this.a.j;
                            if (cVar5 != null) {
                                z3 = this.a.V;
                                if (!z3) {
                                    marker.remove();
                                    marker.p.clear();
                                    cVar6 = this.a.j;
                                    cVar6.b(overlay.a(bundle));
                                    list8 = this.a.k;
                                    list8.add(overlay);
                                    z4 = true;
                                }
                            }
                        }
                    } else {
                        ArrayList<BitmapDescriptor> arrayList2 = marker.p;
                        if (arrayList2 != null && arrayList2.size() != 0) {
                            list5 = this.a.l;
                            if (list5.contains(marker)) {
                                list7 = this.a.l;
                                list7.remove(marker);
                            }
                            list6 = this.a.l;
                            list6.add(marker);
                            cVar3 = this.a.j;
                            if (cVar3 != null) {
                                cVar4 = this.a.j;
                                cVar4.b(true);
                            }
                        }
                    }
                }
                cVar = this.a.j;
                if (cVar != null && !z4) {
                    z2 = this.a.V;
                    if (!z2) {
                        Bundle bundle2 = new Bundle();
                        cVar2 = this.a.j;
                        cVar2.c(overlay.a(bundle2));
                    }
                }
            }
        }
        list = this.a.m;
        if (list.contains(overlay)) {
            list3 = this.a.m;
            list3.remove(overlay);
        }
        if (!(overlay instanceof Marker)) {
            return;
        }
        list2 = this.a.m;
        list2.add((Marker) overlay);
    }

    @Override // com.baidu.mapapi.map.Overlay.a
    public boolean c(Overlay overlay) {
        List list;
        List list2;
        list = this.a.k;
        if (list != null) {
            list2 = this.a.k;
            if (!list2.contains(overlay)) {
                return true;
            }
        }
        return false;
    }
}
