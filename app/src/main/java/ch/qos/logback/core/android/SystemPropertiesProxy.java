package ch.qos.logback.core.android;

import com.xiaomi.mirror.synergy.CallMethod;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class SystemPropertiesProxy {
    private static final SystemPropertiesProxy SINGLETON = new SystemPropertiesProxy(null);
    private Class<?> SystemProperties;
    private Method getBoolean;
    private Method getString;

    private SystemPropertiesProxy(ClassLoader classLoader) {
        try {
            setClassLoader(classLoader);
        } catch (Exception unused) {
        }
    }

    public static SystemPropertiesProxy getInstance() {
        return SINGLETON;
    }

    public void setClassLoader(ClassLoader classLoader) throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        if (classLoader == null) {
            classLoader = getClass().getClassLoader();
        }
        Class<?> loadClass = classLoader.loadClass("android.os.SystemProperties");
        this.SystemProperties = loadClass;
        this.getString = loadClass.getMethod(CallMethod.METHOD_GET, String.class, String.class);
        this.getBoolean = this.SystemProperties.getMethod("getBoolean", String.class, Boolean.TYPE);
    }

    public String get(String str, String str2) throws IllegalArgumentException {
        Method method;
        Class<?> cls = this.SystemProperties;
        String str3 = null;
        if (cls == null || (method = this.getString) == null) {
            return null;
        }
        try {
            str3 = (String) method.invoke(cls, str, str2);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception unused) {
        }
        return (str3 == null || str3.length() == 0) ? str2 : str3;
    }

    public Boolean getBoolean(String str, boolean z) throws IllegalArgumentException {
        if (this.SystemProperties == null || this.getBoolean == null) {
            return Boolean.valueOf(z);
        }
        try {
            return (Boolean) this.getBoolean.invoke(this.SystemProperties, str, Boolean.valueOf(z));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception unused) {
            return Boolean.valueOf(z);
        }
    }
}
