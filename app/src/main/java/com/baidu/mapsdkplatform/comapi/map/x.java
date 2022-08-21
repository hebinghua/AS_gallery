package com.baidu.mapsdkplatform.comapi.map;

import android.text.TextUtils;
import android.util.Log;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapsdkplatform.comapi.util.AlgorithmUtil;
import com.baidu.mapsdkplatform.comjni.tools.JNITools;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class x {
    private String c(String str) {
        String str2 = com.baidu.mapsdkplatform.comapi.util.h.d;
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        String aESSaltKey = JNITools.getAESSaltKey(str2);
        String aESViKey = JNITools.getAESViKey(str2);
        if (!TextUtils.isEmpty(aESSaltKey) && !TextUtils.isEmpty(aESViKey) && !TextUtils.isEmpty(str)) {
            try {
                return new String(AlgorithmUtil.getDecryptInfo(aESViKey, aESSaltKey, e(str))).trim();
            } catch (Exception unused) {
                Log.e("PrismBuildingInfo", "getBuildingGeom Decrypt failed");
            }
        }
        return null;
    }

    private String d(String str) {
        String str2 = com.baidu.mapsdkplatform.comapi.util.h.d;
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        String aESSaltKey = JNITools.getAESSaltKey(str2);
        String aESViKey = JNITools.getAESViKey(str2);
        if (!TextUtils.isEmpty(aESSaltKey) && !TextUtils.isEmpty(aESViKey) && !TextUtils.isEmpty(str)) {
            try {
                return new String(AlgorithmUtil.getDecryptInfo(aESViKey, aESSaltKey, e(str))).trim();
            } catch (Exception unused) {
                Log.e("PrismBuildingInfo", "getBuildingCenter Decrypt failed");
            }
        }
        return null;
    }

    private byte[] e(String str) {
        int length = str.length() / 2;
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            int i3 = i2 + 1;
            bArr[i] = (byte) ((Integer.parseInt(str.substring(i2, i3), 16) * 16) + Integer.parseInt(str.substring(i3, i2 + 2), 16));
        }
        return bArr;
    }

    public ArrayList<LatLng> a(String str) {
        String c = c(str);
        ArrayList<LatLng> arrayList = new ArrayList<>();
        if (!TextUtils.isEmpty(c)) {
            for (String str2 : c.split(";")) {
                String[] split = str2.split(",");
                for (int i = 0; i < split.length; i++) {
                    try {
                        arrayList.add(new LatLng(Double.parseDouble(split[1]), Double.parseDouble(split[0])));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return arrayList;
    }

    public LatLng b(String str) {
        String d = d(str);
        LatLng latLng = null;
        if (!TextUtils.isEmpty(d)) {
            String[] split = d.split(",");
            for (int i = 0; i < split.length; i++) {
                String str2 = split[0];
                try {
                    latLng = new LatLng(Double.parseDouble(split[1]), Double.parseDouble(str2));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return latLng;
    }
}
