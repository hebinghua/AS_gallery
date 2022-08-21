package ch.qos.logback.classic.util;

import java.util.HashMap;

/* loaded from: classes.dex */
public class CopyOnInheritThreadLocal extends InheritableThreadLocal<HashMap<String, String>> {
    @Override // java.lang.InheritableThreadLocal
    public HashMap<String, String> childValue(HashMap<String, String> hashMap) {
        if (hashMap == null) {
            return null;
        }
        return new HashMap<>(hashMap);
    }
}
