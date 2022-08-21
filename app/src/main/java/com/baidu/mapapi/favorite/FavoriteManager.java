package com.baidu.mapapi.favorite;

import android.util.Log;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapsdkplatform.comapi.favrite.FavSyncPoi;
import com.baidu.mapsdkplatform.comapi.map.i;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class FavoriteManager {
    private static FavoriteManager a;
    private static com.baidu.mapsdkplatform.comapi.favrite.a b;

    private FavoriteManager() {
    }

    public static FavoriteManager getInstance() {
        if (a == null) {
            a = new FavoriteManager();
        }
        return a;
    }

    public int add(FavoritePoiInfo favoritePoiInfo) {
        String str;
        if (b == null) {
            str = "you may have not call init method!";
        } else if (favoritePoiInfo != null && favoritePoiInfo.c != null) {
            String str2 = favoritePoiInfo.b;
            if (str2 == null || str2.equals("")) {
                Log.e("baidumapsdk", "poiName can not be null or empty!");
                return -1;
            }
            FavSyncPoi a2 = a.a(favoritePoiInfo);
            int a3 = b.a(a2.b, a2);
            if (a3 == 1) {
                favoritePoiInfo.a = a2.a;
                favoritePoiInfo.g = Long.parseLong(a2.h);
            }
            return a3;
        } else {
            str = "object or pt can not be null!";
        }
        Log.e("baidumapsdk", str);
        return 0;
    }

    public boolean clearAllFavPois() {
        com.baidu.mapsdkplatform.comapi.favrite.a aVar = b;
        if (aVar == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return false;
        }
        return aVar.c();
    }

    public boolean deleteFavPoi(String str) {
        if (b == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return false;
        } else if (str != null && !str.equals("")) {
            return b.a(str);
        } else {
            return false;
        }
    }

    public void destroy() {
        com.baidu.mapsdkplatform.comapi.favrite.a aVar = b;
        if (aVar != null) {
            aVar.b();
            b = null;
            BMapManager.destroy();
            i.b();
        }
    }

    public List<FavoritePoiInfo> getAllFavPois() {
        JSONArray optJSONArray;
        com.baidu.mapsdkplatform.comapi.favrite.a aVar = b;
        if (aVar == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return null;
        }
        String f = aVar.f();
        if (f != null && !f.equals("")) {
            try {
                JSONObject jSONObject = new JSONObject(f);
                if (jSONObject.optInt("favpoinum") != 0 && (optJSONArray = jSONObject.optJSONArray("favcontents")) != null && optJSONArray.length() > 0) {
                    ArrayList arrayList = new ArrayList();
                    for (int i = 0; i < optJSONArray.length(); i++) {
                        JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                        if (jSONObject2 != null) {
                            arrayList.add(a.a(jSONObject2));
                        }
                    }
                    return arrayList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public FavoritePoiInfo getFavPoi(String str) {
        FavSyncPoi b2;
        if (b == null) {
            Log.e("baidumapsdk", "you may have not call init method!");
            return null;
        } else if (str != null && !str.equals("") && (b2 = b.b(str)) != null) {
            return a.a(b2);
        } else {
            return null;
        }
    }

    public void init() {
        if (b == null) {
            i.a();
            BMapManager.init();
            b = com.baidu.mapsdkplatform.comapi.favrite.a.a();
        }
    }

    public boolean updateFavPoi(String str, FavoritePoiInfo favoritePoiInfo) {
        String str2;
        if (b == null) {
            str2 = "you may have not call init method!";
        } else if (str == null || str.equals("") || favoritePoiInfo == null) {
            return false;
        } else {
            if (favoritePoiInfo.c == null) {
                str2 = "object or pt can not be null!";
            } else {
                String str3 = favoritePoiInfo.b;
                if (str3 != null && !str3.equals("")) {
                    favoritePoiInfo.a = str;
                    return b.b(str, a.a(favoritePoiInfo));
                }
                str2 = "poiName can not be null or empty!";
            }
        }
        Log.e("baidumapsdk", str2);
        return false;
    }
}
