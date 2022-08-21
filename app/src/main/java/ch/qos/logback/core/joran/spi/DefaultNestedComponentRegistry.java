package ch.qos.logback.core.joran.spi;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class DefaultNestedComponentRegistry {
    public Map<HostClassAndPropertyDouble, Class<?>> defaultComponentMap = new HashMap();

    public void add(Class<?> cls, String str, Class<?> cls2) {
        this.defaultComponentMap.put(new HostClassAndPropertyDouble(cls, str.toLowerCase(Locale.US)), cls2);
    }

    public Class<?> findDefaultComponentType(Class<?> cls, String str) {
        String lowerCase = str.toLowerCase(Locale.US);
        while (cls != null) {
            Class<?> oneShotFind = oneShotFind(cls, lowerCase);
            if (oneShotFind != null) {
                return oneShotFind;
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    private Class<?> oneShotFind(Class<?> cls, String str) {
        return this.defaultComponentMap.get(new HostClassAndPropertyDouble(cls, str));
    }
}
