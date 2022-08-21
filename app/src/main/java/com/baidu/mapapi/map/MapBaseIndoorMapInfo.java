package com.baidu.mapapi.map;

import java.util.ArrayList;

/* loaded from: classes.dex */
public final class MapBaseIndoorMapInfo {
    private static final String d = "MapBaseIndoorMapInfo";
    public String a;
    public String b;
    public ArrayList<String> c;

    /* loaded from: classes.dex */
    public enum SwitchFloorError {
        SWITCH_OK,
        FLOOR_INFO_ERROR,
        FLOOR_OVERLFLOW,
        FOCUSED_ID_ERROR,
        SWITCH_ERROR
    }

    public MapBaseIndoorMapInfo() {
    }

    public MapBaseIndoorMapInfo(MapBaseIndoorMapInfo mapBaseIndoorMapInfo) {
        this.a = mapBaseIndoorMapInfo.a;
        this.b = mapBaseIndoorMapInfo.b;
        this.c = mapBaseIndoorMapInfo.c;
    }

    public MapBaseIndoorMapInfo(String str, String str2, ArrayList<String> arrayList) {
        this.a = str;
        this.b = str2;
        this.c = arrayList;
    }

    public String getCurFloor() {
        return this.b;
    }

    public ArrayList<String> getFloors() {
        return this.c;
    }

    public String getID() {
        return this.a;
    }
}
