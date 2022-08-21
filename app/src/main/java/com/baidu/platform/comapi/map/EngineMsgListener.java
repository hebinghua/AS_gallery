package com.baidu.platform.comapi.map;

/* loaded from: classes.dex */
public interface EngineMsgListener {
    void onEnterIndoorMapMode(IndoorMapInfo indoorMapInfo);

    void onExitIndoorMapMode();

    void onLongLinkConnect();

    void onLongLinkDisConnect();
}
