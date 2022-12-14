package com.baidu.mapapi.utils.poi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapsdkplatform.comapi.util.CoordTrans;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.List;

/* loaded from: classes.dex */
public class BaiduMapPoiSearch {
    private static boolean a = true;

    private static void a(PoiParaOption poiParaOption, Context context) {
        Uri parse = Uri.parse("http://api.map.baidu.com/place/detail?uid=" + poiParaOption.a + "&output=html&src=" + context.getPackageName());
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setData(parse);
        context.startActivity(intent);
    }

    private static void b(PoiParaOption poiParaOption, Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("http://api.map.baidu.com/place/search?");
        sb.append("query=");
        sb.append(poiParaOption.b);
        sb.append("&location=");
        LatLng latLng = poiParaOption.c;
        if (SDKInitializer.getCoordType() == CoordType.GCJ02) {
            latLng = CoordTrans.gcjToBaidu(latLng);
        }
        sb.append(latLng.latitude);
        sb.append(",");
        sb.append(latLng.longitude);
        sb.append("&radius=");
        sb.append(poiParaOption.d);
        sb.append("&output=html");
        sb.append("&src=");
        sb.append(context.getPackageName());
        Uri parse = Uri.parse(sb.toString());
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setData(parse);
        context.startActivity(intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void b(String str, Context context) {
        if (str == null || str.length() == 0) {
            throw new RuntimeException("BDMapSDKException: pano id can not be null.");
        }
        if (context == null) {
            throw new RuntimeException("BDMapSDKException: context cannot be null.");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("baidumap://map/streetscape?");
        sb.append("panoid=");
        sb.append(str);
        sb.append("&pid=");
        sb.append(str);
        sb.append("&panotype=");
        sb.append("street");
        sb.append("&src=");
        sb.append("sdk_[" + context.getPackageName() + "]");
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
        intent.setFlags(268435456);
        if (intent.resolveActivity(context.getPackageManager()) == null) {
            throw new RuntimeException("BDMapSDKException: BaiduMap app is not installed.");
        }
        context.startActivity(intent);
    }

    public static boolean dispatchPoiToBaiduMap(List<DispathcPoiData> list, Context context) throws Exception {
        String str;
        if (list.isEmpty() || list.size() <= 0) {
            throw new NullPointerException("BDMapSDKException: dispatch poidata is null");
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            str = "BaiduMap app is not installed.";
        } else if (baiduMapVersion >= 840) {
            return com.baidu.mapapi.utils.b.a(list, context, 6);
        } else {
            str = "Baidumap app version is too lowl.Version is greater than 8.4";
        }
        Log.e("baidumapsdk", str);
        return false;
    }

    public static void finish(Context context) {
        if (context != null) {
            com.baidu.mapapi.utils.b.a(context);
        }
    }

    public static void openBaiduMapPanoShow(String str, Context context) {
        new com.baidu.platform.comapi.pano.a().a(str, new a(context));
    }

    public static boolean openBaiduMapPoiDetialsPage(PoiParaOption poiParaOption, Context context) {
        if (poiParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("BDMapSDKException: para or context can not be null.");
        }
        String str = poiParaOption.a;
        if (str == null) {
            throw new IllegalPoiSearchArgumentException("BDMapSDKException: poi uid can not be null.");
        }
        if (str.equals("")) {
            throw new IllegalPoiSearchArgumentException("BDMapSDKException: poi uid can not be empty string.");
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            Log.e("baidumapsdk", "BaiduMap app is not installed.");
            if (!a) {
                throw new IllegalPoiSearchArgumentException("BDMapSDKException: BaiduMap app is not installed.");
            }
            a(poiParaOption, context);
            return true;
        } else if (baiduMapVersion >= 810) {
            return com.baidu.mapapi.utils.b.a(poiParaOption, context, 3);
        } else {
            Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
            if (!a) {
                throw new IllegalPoiSearchArgumentException("BDMapSDKException: Baidumap app version is too lowl.Version is greater than 8.1");
            }
            a(poiParaOption, context);
            return true;
        }
    }

    public static boolean openBaiduMapPoiNearbySearch(PoiParaOption poiParaOption, Context context) {
        if (poiParaOption == null || context == null) {
            throw new IllegalPoiSearchArgumentException("BDMapSDKException: para or context can not be null.");
        }
        String str = poiParaOption.b;
        if (str == null) {
            throw new IllegalPoiSearchArgumentException("BDMapSDKException: poi search key can not be null.");
        }
        LatLng latLng = poiParaOption.c;
        if (latLng == null) {
            throw new IllegalPoiSearchArgumentException("BDMapSDKException: poi search center can not be null.");
        }
        if (latLng.longitude == SearchStatUtils.POW || latLng.latitude == SearchStatUtils.POW) {
            throw new IllegalPoiSearchArgumentException("BDMapSDKException: poi search center longitude or latitude can not be 0.");
        }
        if (poiParaOption.d == 0) {
            throw new IllegalPoiSearchArgumentException("BDMapSDKException: poi search radius larger than 0.");
        }
        if (str.equals("")) {
            throw new IllegalPoiSearchArgumentException("BDMapSDKException: poi key can not be empty string");
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            Log.e("baidumapsdk", "BaiduMap app is not installed.");
            if (!a) {
                throw new IllegalPoiSearchArgumentException("BDMapSDKException: BaiduMap app is not installed.");
            }
            b(poiParaOption, context);
            return true;
        } else if (baiduMapVersion >= 810) {
            return com.baidu.mapapi.utils.b.a(poiParaOption, context, 4);
        } else {
            Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.1");
            if (!a) {
                throw new IllegalPoiSearchArgumentException("BDMapSDKException: Baidumap app version is too lowl.Version is greater than 8.1");
            }
            b(poiParaOption, context);
            return true;
        }
    }

    public static void setSupportWebPoi(boolean z) {
        a = z;
    }
}
