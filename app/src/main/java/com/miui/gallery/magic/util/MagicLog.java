package com.miui.gallery.magic.util;

import android.util.Log;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class MagicLog {
    public static MagicLog INSTANCE = new MagicLog();
    public Map<String, Long> mTimeMap = new HashMap();

    public void showLog(String str, String str2) {
        Log.i(str, str2);
    }

    public void showLog(String str) {
        showLog("MagicLog", str);
    }

    public void startLog(String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        this.mTimeMap.put(str, Long.valueOf(currentTimeMillis));
        Log.i(str, str2 + " : startTime: " + currentTimeMillis + "  毫秒");
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(" 开始 ");
        sb.append(str2);
        showLog("MagicLogger", sb.toString());
    }

    public void endLog(String str, String str2) {
        long longValue;
        String str3;
        long currentTimeMillis = System.currentTimeMillis();
        if (this.mTimeMap.get(str).longValue() > 0) {
            str3 = " : endTime: " + currentTimeMillis + ", useTime: " + (currentTimeMillis - longValue) + "  毫秒";
        } else {
            str3 = "";
        }
        Log.i(str, str2 + str3);
        showLog("MagicLogger", str + " 结束 " + str2);
    }
}
