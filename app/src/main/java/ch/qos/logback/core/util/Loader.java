package ch.qos.logback.core.util;

import ch.qos.logback.core.Context;
import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/* loaded from: classes.dex */
public class Loader {
    private static boolean HAS_GET_CLASS_LOADER_PERMISSION = false;
    public static final String IGNORE_TCL_PROPERTY_NAME = "logback.ignoreTCL";
    public static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
    private static boolean ignoreTCL = false;

    static {
        String systemProperty = OptionHelper.getSystemProperty(IGNORE_TCL_PROPERTY_NAME, null);
        if (systemProperty != null) {
            ignoreTCL = Boolean.valueOf(systemProperty).booleanValue();
        }
        HAS_GET_CLASS_LOADER_PERMISSION = ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: ch.qos.logback.core.util.Loader.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean mo171run() {
                try {
                    AccessController.checkPermission(new RuntimePermission("getClassLoader"));
                    return Boolean.TRUE;
                } catch (SecurityException unused) {
                    return Boolean.FALSE;
                }
            }
        })).booleanValue();
    }

    public static Set<URL> getResourceOccurrenceCount(String str, ClassLoader classLoader) throws IOException {
        HashSet hashSet = new HashSet();
        Enumeration<URL> resources = classLoader.getResources(str);
        while (resources.hasMoreElements()) {
            hashSet.add(resources.nextElement());
        }
        return hashSet;
    }

    public static URL getResource(String str, ClassLoader classLoader) {
        try {
            return classLoader.getResource(str);
        } catch (Throwable unused) {
            return null;
        }
    }

    public static URL getResourceBySelfClassLoader(String str) {
        return getResource(str, getClassLoaderOfClass(Loader.class));
    }

    public static ClassLoader getTCL() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Class<?> loadClass(String str, Context context) throws ClassNotFoundException {
        return getClassLoaderOfObject(context).loadClass(str);
    }

    public static ClassLoader getClassLoaderOfObject(Object obj) {
        Objects.requireNonNull(obj, "Argument cannot be null");
        return getClassLoaderOfClass(obj.getClass());
    }

    public static ClassLoader getClassLoaderAsPrivileged(final Class<?> cls) {
        if (!HAS_GET_CLASS_LOADER_PERMISSION) {
            return null;
        }
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: ch.qos.logback.core.util.Loader.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader mo172run() {
                return cls.getClassLoader();
            }
        });
    }

    public static ClassLoader getClassLoaderOfClass(Class<?> cls) {
        ClassLoader classLoader = cls.getClassLoader();
        return classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader;
    }

    public static Class<?> loadClass(String str) throws ClassNotFoundException {
        if (ignoreTCL) {
            return Class.forName(str);
        }
        try {
            return getTCL().loadClass(str);
        } catch (Throwable unused) {
            return Class.forName(str);
        }
    }
}
