package com.xiaomi.miplay.phoneclientsdk.external;

import android.content.Context;
import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;

/* loaded from: classes3.dex */
public class MiPlayClientManage {
    public MiplayClientControl mMiplayClientControl;

    public MiPlayClientManage(Context context) {
        this.mMiplayClientControl = new MiplayClientControl(context);
    }

    public boolean initAsync(MiplayClientCallback miplayClientCallback) {
        return this.mMiplayClientControl.initAsync(miplayClientCallback);
    }

    public void unInit() {
        this.mMiplayClientControl.unInit();
    }

    public int play(String str, MediaMetaData mediaMetaData) {
        return this.mMiplayClientControl.play(str, mediaMetaData);
    }

    public int stop(String str) {
        return this.mMiplayClientControl.stop(str);
    }

    public int pause(String str) {
        return this.mMiplayClientControl.pause(str);
    }

    public int resume(String str) {
        return this.mMiplayClientControl.resume(str);
    }

    public int seek(String str, long j) {
        return this.mMiplayClientControl.seek(str, j);
    }

    public int setVolume(String str, int i) {
        return this.mMiplayClientControl.setVolume(str, i);
    }

    public int getVolume(String str) {
        return this.mMiplayClientControl.getVolume(str);
    }

    public int getCirculateMode() {
        return this.mMiplayClientControl.getCirculateMode();
    }

    public int cancelCirculate(String str, int i) {
        return this.mMiplayClientControl.cancelCirculate(str, i);
    }
}
