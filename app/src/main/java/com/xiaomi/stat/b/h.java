package com.xiaomi.stat.b;

import com.xiaomi.stat.d.k;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class h {
    public static final String a = "key_update_time";
    public static final String b = "get_all_config";
    public static final String c = "mistats/v3";
    public static final String d = "key_get";
    public static final String e = "http://";
    public static final String f = "https://";
    public static final String g = "/";
    private static final String h = "RegionManagerHelper";

    public HashMap<String, String> a(String str, JSONObject jSONObject) {
        JSONObject optJSONObject = jSONObject.optJSONObject(str);
        if (optJSONObject != null) {
            HashMap<String, String> hashMap = new HashMap<>();
            k.b(h, "parse the map contains key:" + str);
            Iterator<String> keys = optJSONObject.keys();
            while (keys.hasNext()) {
                try {
                    String next = keys.next();
                    String string = optJSONObject.getString(next);
                    k.b(h, "[region]:" + next + "\n[domain]:" + string);
                    hashMap.put(next, string);
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
            return hashMap;
        }
        k.d(h, "can not find the specific key" + str);
        return null;
    }

    public HashMap<String, String> a(HashMap<String, String> hashMap, HashMap<String, String> hashMap2) {
        HashMap<String, String> hashMap3 = new HashMap<>();
        if (hashMap2 != null) {
            hashMap3.putAll(hashMap2);
        }
        Set<String> keySet = hashMap.keySet();
        Set<String> keySet2 = hashMap3.keySet();
        for (String str : keySet) {
            if (!keySet2.contains(str)) {
                hashMap3.put(str, hashMap.get(str));
            }
        }
        return hashMap3;
    }
}
