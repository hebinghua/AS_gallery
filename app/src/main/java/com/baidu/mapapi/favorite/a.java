package com.baidu.mapapi.favorite;

import ch.qos.logback.core.joran.action.Action;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapsdkplatform.comapi.favrite.FavSyncPoi;
import com.baidu.platform.comapi.basestruct.Point;
import org.json.JSONObject;

/* loaded from: classes.dex */
class a {
    public static FavoritePoiInfo a(FavSyncPoi favSyncPoi) {
        if (favSyncPoi == null || favSyncPoi.c == null || favSyncPoi.b.equals("")) {
            return null;
        }
        FavoritePoiInfo favoritePoiInfo = new FavoritePoiInfo();
        favoritePoiInfo.a = favSyncPoi.a;
        favoritePoiInfo.b = favSyncPoi.b;
        Point point = favSyncPoi.c;
        favoritePoiInfo.c = new LatLng(point.y / 1000000.0d, point.x / 1000000.0d);
        favoritePoiInfo.e = favSyncPoi.e;
        favoritePoiInfo.f = favSyncPoi.f;
        favoritePoiInfo.d = favSyncPoi.d;
        favoritePoiInfo.g = Long.parseLong(favSyncPoi.h);
        return favoritePoiInfo;
    }

    public static FavoritePoiInfo a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return null;
        }
        FavoritePoiInfo favoritePoiInfo = new FavoritePoiInfo();
        JSONObject optJSONObject = jSONObject.optJSONObject("pt");
        if (optJSONObject != null) {
            favoritePoiInfo.c = new LatLng(optJSONObject.optInt("y") / 1000000.0d, optJSONObject.optInt("x") / 1000000.0d);
        }
        favoritePoiInfo.b = jSONObject.optString("uspoiname");
        favoritePoiInfo.g = Long.parseLong(jSONObject.optString("addtimesec"));
        favoritePoiInfo.d = jSONObject.optString("addr");
        favoritePoiInfo.f = jSONObject.optString("uspoiuid");
        favoritePoiInfo.e = jSONObject.optString("ncityid");
        favoritePoiInfo.a = jSONObject.optString(Action.KEY_ATTRIBUTE);
        return favoritePoiInfo;
    }

    public static FavSyncPoi a(FavoritePoiInfo favoritePoiInfo) {
        String str;
        if (favoritePoiInfo == null || favoritePoiInfo.c == null || (str = favoritePoiInfo.b) == null || str.equals("")) {
            return null;
        }
        FavSyncPoi favSyncPoi = new FavSyncPoi();
        favSyncPoi.b = favoritePoiInfo.b;
        LatLng latLng = favoritePoiInfo.c;
        favSyncPoi.c = new Point((int) (latLng.longitude * 1000000.0d), (int) (latLng.latitude * 1000000.0d));
        favSyncPoi.d = favoritePoiInfo.d;
        favSyncPoi.e = favoritePoiInfo.e;
        favSyncPoi.f = favoritePoiInfo.f;
        favSyncPoi.i = false;
        return favSyncPoi;
    }
}
