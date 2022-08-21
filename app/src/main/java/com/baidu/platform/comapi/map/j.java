package com.baidu.platform.comapi.map;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.baidu.platform.comapi.util.JsonBuilder;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes.dex */
public abstract class j {
    public ao a;
    public boolean b;
    public boolean c;
    public double[] d;
    public JsonBuilder h;
    public GeoPoint e = new GeoPoint(SearchStatUtils.POW, SearchStatUtils.POW);
    public GeoPoint f = new GeoPoint(SearchStatUtils.POW, SearchStatUtils.POW);
    public boolean g = true;
    public int i = -1;
    public int j = 0;

    public j(ao aoVar) {
        this.a = aoVar;
    }

    public abstract String a();

    public String a(int i) {
        JsonBuilder key;
        int i2;
        JsonBuilder jsonBuilder = new JsonBuilder();
        this.h = jsonBuilder;
        jsonBuilder.object();
        if (i == 0) {
            this.h.key(nexExportFormat.TAG_FORMAT_PATH).arrayValue();
            if (this.d != null) {
                int i3 = 0;
                while (true) {
                    double[] dArr = this.d;
                    if (i3 >= dArr.length) {
                        break;
                    }
                    this.h.value(dArr[i3]);
                    i3++;
                }
            }
            this.h.endArrayValue();
        } else if (i == 1) {
            this.h.key("sgeo");
            this.h.object();
            this.h.key("bound").arrayValue();
            GeoPoint geoPoint = this.e;
            if (geoPoint != null && this.f != null) {
                this.h.value(geoPoint.getLongitude());
                this.h.value(this.e.getLatitude());
                this.h.value(this.f.getLongitude());
                this.h.value(this.f.getLatitude());
            }
            this.h.endArrayValue();
            if (this.j == 4) {
                this.h.key(nexExportFormat.TAG_FORMAT_TYPE).value(3);
            } else {
                this.h.key(nexExportFormat.TAG_FORMAT_TYPE).value(this.j);
            }
            this.h.key("elements").arrayValue();
            this.h.object();
            this.h.key("points").arrayValue();
            if (this.d != null) {
                int i4 = 0;
                while (true) {
                    double[] dArr2 = this.d;
                    if (i4 >= dArr2.length) {
                        break;
                    }
                    this.h.value(dArr2[i4]);
                    i4++;
                }
            }
            this.h.endArrayValue();
            this.h.endObject();
            this.h.endArrayValue();
            this.h.endObject();
        }
        this.h.key("ud").value(String.valueOf(hashCode()));
        this.h.key(MapBundleKey.MapObjKey.OBJ_DIR).value(0);
        ao aoVar = this.a;
        if (aoVar == null || aoVar.a() == 0) {
            int i5 = this.j;
            if (i5 == 3) {
                key = this.h.key(MapBundleKey.MapObjKey.OBJ_TYPE);
                i2 = 3100;
            } else if (i5 == 4) {
                key = this.h.key(MapBundleKey.MapObjKey.OBJ_TYPE);
                i2 = 3200;
            } else {
                key = this.h.key(MapBundleKey.MapObjKey.OBJ_TYPE);
                i2 = -1;
            }
        } else {
            this.h.key(MapBundleKey.MapObjKey.OBJ_NORMALSTYTLE).value(this.a.a());
            this.h.key(MapBundleKey.MapObjKey.OBJ_FOCUSSTYTLE).value(this.a.a());
            key = this.h.key(MapBundleKey.MapObjKey.OBJ_TYPE);
            i2 = 32;
        }
        key.value(i2);
        this.h.key(MapBundleKey.MapObjKey.OBJ_OFFSET).value(0);
        this.h.key("in").value(0);
        this.h.key(MapBundleKey.MapObjKey.OBJ_TEXT).value("");
        this.h.key(MapBundleKey.MapObjKey.OBJ_DIS).value(0);
        this.h.key("align").value(0);
        if (this.b) {
            this.h.key("dash").value(1);
            this.h.key(MapBundleKey.MapObjKey.OBJ_TYPE).value(this.j);
        }
        if (this.c) {
            this.h.key("trackMove").object();
            this.h.key("pointStyle").value(((aq) this.a).e());
            this.h.endObject();
        }
        this.h.key("style").object();
        if (this.a != null) {
            this.h.key(nexExportFormat.TAG_FORMAT_WIDTH).value(this.a.c());
            this.h.key("color").value(ao.c(this.a.b()));
            int i6 = this.j;
            if (i6 == 3 || i6 == 4) {
                this.h.key("scolor").value(ao.c(this.a.d()));
            }
        }
        this.h.endObject();
        this.h.endObject();
        return this.h.toString();
    }
}
