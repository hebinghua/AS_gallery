package com.baidu.platform.comapi.map;

import android.text.TextUtils;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class IndoorMapInfo implements Serializable {
    private String a;
    private String b;
    private String[] c;
    private int[] d;
    private int e;
    private int f;
    private String g;

    public IndoorMapInfo(String str, String str2) {
        this.a = str;
        this.b = str2;
    }

    public IndoorMapInfo(String str, String str2, String[] strArr, int[] iArr, int i) {
        this(str, str2, strArr, iArr, i, 0, "");
    }

    public IndoorMapInfo(String str, String str2, String[] strArr, int[] iArr, int i, int i2) {
        this(str, str2, strArr, iArr, i, i2, "");
    }

    public IndoorMapInfo(String str, String str2, String[] strArr, int[] iArr, int i, int i2, String str3) {
        this.a = str;
        this.b = str2;
        this.e = i;
        this.f = i2;
        if (strArr != null) {
            String[] strArr2 = (String[]) Array.newInstance(String.class, strArr.length);
            this.c = strArr2;
            System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        }
        if (iArr != null) {
            int[] iArr2 = new int[iArr.length];
            this.d = iArr2;
            System.arraycopy(iArr, 0, iArr2, 0, iArr.length);
        }
        this.g = str3;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof IndoorMapInfo)) {
            IndoorMapInfo indoorMapInfo = (IndoorMapInfo) obj;
            if (!TextUtils.equals(this.a, indoorMapInfo.a) || !TextUtils.equals(this.b, indoorMapInfo.b) || !Arrays.equals(this.c, indoorMapInfo.c)) {
                return false;
            }
            return Arrays.equals(this.d, indoorMapInfo.d);
        }
        return false;
    }

    public String getBuildingId() {
        return this.a;
    }

    public final int[] getFloorAttribute() {
        return this.d;
    }

    public String getFloorId() {
        return this.b;
    }

    public final String[] getFloorList() {
        return this.c;
    }

    public String getIdrSearch() {
        return this.g;
    }

    public int getIdrguide() {
        return this.f;
    }

    public int getIndoorType() {
        return this.e;
    }

    public String toString() {
        return "IndoorMapInfo:building_id:" + this.a + ";floor_id:" + this.b + ";indoor_type:" + this.e + ";floor_list:" + Arrays.toString(this.c) + ";floor_attribute:" + Arrays.toString(this.d);
    }
}
