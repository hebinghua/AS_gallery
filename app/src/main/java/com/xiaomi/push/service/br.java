package com.xiaomi.push.service;

import com.xiaomi.push.hw;
import java.util.Map;

/* loaded from: classes3.dex */
public class br {
    public static hw a(hw hwVar) {
        Map<String, String> map;
        if (hwVar != null && (map = hwVar.f528b) != null) {
            map.remove("score_info");
        }
        return hwVar;
    }
}
