package com.baidu.mapapi.map;

import android.os.Bundle;
import android.util.Log;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baidu.platform.comapi.map.MapBundleKey;

/* loaded from: classes.dex */
public final class TileOverlayOptions {
    private static Bundle c = null;
    private static final String j = "TileOverlayOptions";
    private TileProvider b;
    public int datasource;
    public String urlString;
    private int a = 209715200;
    private int d = 20;
    private int e = 3;
    private int f = 15786414;
    private int g = -20037726;
    private int h = -15786414;
    private int i = 20037726;

    public TileOverlayOptions() {
        Bundle bundle = new Bundle();
        c = bundle;
        bundle.putInt("rectr", this.f);
        c.putInt("rectb", this.g);
        c.putInt("rectl", this.h);
        c.putInt("rectt", this.i);
    }

    private TileOverlayOptions a(int i, int i2) {
        this.d = i;
        this.e = i2;
        return this;
    }

    public Bundle a() {
        c.putString(MapBundleKey.MapObjKey.OBJ_URL, this.urlString);
        c.putInt("datasource", this.datasource);
        c.putInt("maxDisplay", this.d);
        c.putInt("minDisplay", this.e);
        c.putInt("sdktiletmpmax", this.a);
        return c;
    }

    public TileOverlay a(BaiduMap baiduMap) {
        return new TileOverlay(baiduMap, this.b);
    }

    public TileOverlayOptions setMaxTileTmp(int i) {
        this.a = i;
        return this;
    }

    public TileOverlayOptions setPositionFromBounds(LatLngBounds latLngBounds) {
        if (latLngBounds != null) {
            GeoPoint ll2mc = CoordUtil.ll2mc(latLngBounds.northeast);
            GeoPoint ll2mc2 = CoordUtil.ll2mc(latLngBounds.southwest);
            double latitudeE6 = ll2mc.getLatitudeE6();
            double longitudeE6 = ll2mc2.getLongitudeE6();
            double latitudeE62 = ll2mc2.getLatitudeE6();
            double longitudeE62 = ll2mc.getLongitudeE6();
            if (latitudeE6 <= latitudeE62 || longitudeE62 <= longitudeE6) {
                Log.e(j, "BDMapSDKException: bounds is illegal, use default bounds");
            } else {
                c.putInt("rectr", (int) longitudeE62);
                c.putInt("rectb", (int) latitudeE62);
                c.putInt("rectl", (int) longitudeE6);
                c.putInt("rectt", (int) latitudeE6);
            }
            return this;
        }
        throw new IllegalArgumentException("BDMapSDKException: bound can not be null");
    }

    public TileOverlayOptions tileProvider(TileProvider tileProvider) {
        String str;
        String str2;
        int maxDisLevel;
        if (tileProvider == null) {
            return null;
        }
        if (!(tileProvider instanceof UrlTileProvider)) {
            if (!(tileProvider instanceof FileTileProvider)) {
                str = j;
                str2 = "tileProvider must be UrlTileProvider or FileTileProvider";
                Log.e(str, str2);
                return null;
            }
            this.datasource = 0;
            this.b = tileProvider;
            maxDisLevel = tileProvider.getMaxDisLevel();
            int minDisLevel = tileProvider.getMinDisLevel();
            if (maxDisLevel <= 21) {
            }
            Log.e(j, "display level is illegal");
            return this;
        }
        this.datasource = 1;
        String tileUrl = ((UrlTileProvider) tileProvider).getTileUrl();
        if (tileUrl == null || "".equals(tileUrl) || !tileUrl.contains("{x}") || !tileUrl.contains("{y}") || !tileUrl.contains("{z}")) {
            str = j;
            str2 = "tile url template is illegal, must contains {x}、{y}、{z}";
            Log.e(str, str2);
            return null;
        }
        this.urlString = tileUrl;
        this.b = tileProvider;
        maxDisLevel = tileProvider.getMaxDisLevel();
        int minDisLevel2 = tileProvider.getMinDisLevel();
        if (maxDisLevel <= 21 || minDisLevel2 < 3) {
            Log.e(j, "display level is illegal");
        } else {
            a(maxDisLevel, minDisLevel2);
        }
        return this;
    }
}
