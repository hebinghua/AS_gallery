package com.baidu.mapapi.navi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import com.baidu.mapapi.VersionInfo;
import com.baidu.mapapi.model.CoordUtil;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.b;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class BaiduMapNavigation {
    private static boolean a = true;

    private static String a(Context context) {
        PackageManager packageManager;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext().getPackageManager();
            try {
                applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException unused) {
            }
        } catch (PackageManager.NameNotFoundException unused2) {
            packageManager = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    private static void a(NaviParaOption naviParaOption, Context context) throws IllegalNaviArgumentException {
        if (naviParaOption == null || context == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: para or context can not be null.");
        }
        LatLng latLng = naviParaOption.a;
        if (latLng == null || naviParaOption.c == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: you must set start and end point.");
        }
        GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
        GeoPoint ll2mc2 = CoordUtil.ll2mc(naviParaOption.c);
        StringBuilder sb = new StringBuilder();
        sb.append("http://app.navi.baidu.com/mobile/#navi/naving/");
        sb.append("&sy=0");
        sb.append("&endp=");
        sb.append("&start=");
        sb.append("&startwd=");
        sb.append("&endwd=");
        sb.append("&fromprod=map_sdk");
        sb.append("&app_version=");
        sb.append(VersionInfo.VERSION_INFO);
        JSONArray jSONArray = new JSONArray();
        JSONObject jSONObject = new JSONObject();
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject.put(nexExportFormat.TAG_FORMAT_TYPE, "1");
            String str = naviParaOption.b;
            if (str == null || str.equals("")) {
                jSONObject.put("keyword", "");
            } else {
                jSONObject.put("keyword", naviParaOption.b);
            }
            jSONObject.put("xy", String.valueOf(ll2mc.getLongitudeE6()) + "," + String.valueOf(ll2mc.getLatitudeE6()));
            jSONArray.put(jSONObject);
            jSONObject2.put(nexExportFormat.TAG_FORMAT_TYPE, "1");
            String str2 = naviParaOption.d;
            if (str2 == null || str2.equals("")) {
                jSONObject.put("keyword", "");
            } else {
                jSONObject.put("keyword", naviParaOption.d);
            }
            jSONObject2.put("xy", String.valueOf(ll2mc2.getLongitudeE6()) + "," + String.valueOf(ll2mc2.getLatitudeE6()));
            jSONArray.put(jSONObject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jSONArray.length() > 0) {
            sb.append("&positions=");
            sb.append(jSONArray.toString());
        }
        sb.append("&ctrl_type=");
        sb.append("&mrsl=");
        sb.append("/vt=map&state=entry");
        Uri parse = Uri.parse(sb.toString());
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setFlags(268435456);
        intent.setData(parse);
        context.startActivity(intent);
    }

    public static void finish(Context context) {
        if (context != null) {
            b.a(context);
        }
    }

    public static boolean openBaiduMapBikeNavi(NaviParaOption naviParaOption, Context context) {
        String str;
        if (naviParaOption == null || context == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: para or context can not be null.");
        }
        if (naviParaOption.c == null || naviParaOption.a == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: start point or end point can not be null.");
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            str = "BaiduMap app is not installed.";
        } else if (baiduMapVersion >= 869) {
            return b.a(naviParaOption, context, 8);
        } else {
            str = "Baidumap app version is too lowl.Version is greater than 8.6.6";
        }
        Log.e("baidumapsdk", str);
        return false;
    }

    public static boolean openBaiduMapNavi(NaviParaOption naviParaOption, Context context) {
        if (naviParaOption == null || context == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: para or context can not be null.");
        }
        if (naviParaOption.c == null || naviParaOption.a == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: start point or end point can not be null.");
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            Log.e("baidumapsdk", "BaiduMap app is not installed.");
            if (!a) {
                throw new BaiduMapAppNotSupportNaviException("BDMapSDKException: BaiduMap app is not installed.");
            }
            a(naviParaOption, context);
            return true;
        } else if (baiduMapVersion >= 830) {
            return b.a(naviParaOption, context, 5);
        } else {
            Log.e("baidumapsdk", "Baidumap app version is too lowl.Version is greater than 8.2");
            if (!a) {
                throw new BaiduMapAppNotSupportNaviException("BDMapSDKException: Baidumap app version is too lowl.Version is greater than 8.2");
            }
            a(naviParaOption, context);
            return true;
        }
    }

    public static boolean openBaiduMapWalkNavi(NaviParaOption naviParaOption, Context context) {
        String str;
        if (naviParaOption == null || context == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: para or context can not be null.");
        }
        if (naviParaOption.c == null || naviParaOption.a == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: start point or end point can not be null.");
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            str = "BaiduMap app is not installed.";
        } else if (baiduMapVersion >= 869) {
            return b.a(naviParaOption, context, 7);
        } else {
            str = "Baidumap app version is too lowl.Version is greater than 8.6.6";
        }
        Log.e("baidumapsdk", str);
        return false;
    }

    public static boolean openBaiduMapWalkNaviAR(NaviParaOption naviParaOption, Context context) {
        String str;
        if (naviParaOption == null || context == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: para or context can not be null.");
        }
        if (naviParaOption.c == null || naviParaOption.a == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: start point or end point can not be null.");
        }
        int baiduMapVersion = OpenClientUtil.getBaiduMapVersion(context);
        if (baiduMapVersion == 0) {
            str = "BaiduMap app is not installed.";
        } else if (baiduMapVersion >= 869) {
            return b.a(naviParaOption, context, 9);
        } else {
            str = "Baidumap app version is too lowl.Version is greater than 8.6.6";
        }
        Log.e("baidumapsdk", str);
        return false;
    }

    @Deprecated
    public static void openWebBaiduMapNavi(NaviParaOption naviParaOption, Context context) throws IllegalNaviArgumentException {
        String str;
        Uri parse;
        Intent intent;
        if (naviParaOption == null || context == null) {
            throw new IllegalNaviArgumentException("BDMapSDKException: para or context can not be null.");
        }
        LatLng latLng = naviParaOption.a;
        if (latLng == null || naviParaOption.c == null) {
            String str2 = naviParaOption.b;
            if (str2 == null || str2.equals("") || (str = naviParaOption.d) == null || str.equals("")) {
                throw new IllegalNaviArgumentException("BDMapSDKException: you must set start and end point or set the start and end name.");
            }
            parse = Uri.parse("http://daohang.map.baidu.com/mobile/#search/search/qt=nav&sn=2$$$$$$" + naviParaOption.b + "$$$$$$&en=2$$$$$$" + naviParaOption.d + "$$$$$$&fromprod=" + a(context));
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
        } else {
            GeoPoint ll2mc = CoordUtil.ll2mc(latLng);
            GeoPoint ll2mc2 = CoordUtil.ll2mc(naviParaOption.c);
            parse = Uri.parse("http://daohang.map.baidu.com/mobile/#navi/naving/start=" + ll2mc.getLongitudeE6() + "," + ll2mc.getLatitudeE6() + "&endp=" + ll2mc2.getLongitudeE6() + "," + ll2mc2.getLatitudeE6() + "&fromprod=" + a(context) + "/vt=map&state=entry");
            intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.setFlags(268435456);
        }
        intent.setData(parse);
        context.startActivity(intent);
    }

    public static void setSupportWebNavi(boolean z) {
        a = z;
    }
}
