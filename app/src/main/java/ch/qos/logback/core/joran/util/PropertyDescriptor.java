package ch.qos.logback.core.joran.util;

import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class PropertyDescriptor {
    private String name;
    private Method readMethod;
    private Class<?> type;
    private Method writeMethod;

    public PropertyDescriptor(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public Method getWriteMethod() {
        return this.writeMethod;
    }

    public void setWriteMethod(Method method) {
        this.writeMethod = method;
    }

    public Method getReadMethod() {
        return this.readMethod;
    }

    public void setReadMethod(Method method) {
        this.readMethod = method;
    }

    public Class<?> getPropertyType() {
        return this.type;
    }

    public void setPropertyType(Class<?> cls) {
        this.type = cls;
    }
}
