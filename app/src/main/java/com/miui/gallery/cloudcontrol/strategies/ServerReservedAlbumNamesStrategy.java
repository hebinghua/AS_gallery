package com.miui.gallery.cloudcontrol.strategies;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes.dex */
public class ServerReservedAlbumNamesStrategy extends BaseStrategy {
    @SerializedName("reserved-names")
    private List<String> mReservedAlbumNames;

    public boolean containsName(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        if (!BaseMiscUtil.isValid(this.mReservedAlbumNames)) {
            return false;
        }
        for (String str2 : this.mReservedAlbumNames) {
            if (str.equalsIgnoreCase(str2)) {
                return true;
            }
        }
        return false;
    }

    public static ServerReservedAlbumNamesStrategy createDefault() {
        ServerReservedAlbumNamesStrategy serverReservedAlbumNamesStrategy = new ServerReservedAlbumNamesStrategy();
        serverReservedAlbumNamesStrategy.mReservedAlbumNames = Arrays.asList("我的照片", "截屏", "截图", "截屏录屏", "my photo", "my photos", "screenshot", "screenshots", "Screenshots and screen recordings", "我的照片", "截圖", "camera photos", "相机", "相機", "所有视频", "所有影片", "All videos", "宠物相册", "Pet Album", "视频", "全景", "視頻", "Videos", "Panoramas");
        return serverReservedAlbumNamesStrategy;
    }

    public String toString() {
        return "ServerReservedAlbumNamesStrategy{mReservedAlbumNames=" + this.mReservedAlbumNames + '}';
    }
}
