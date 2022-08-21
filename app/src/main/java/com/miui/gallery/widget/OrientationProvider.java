package com.miui.gallery.widget;

import android.content.Context;
import android.text.TextUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes2.dex */
public interface OrientationProvider {
    public static final OrientationProvider PORTRAIT = new OrientationProvider() { // from class: com.miui.gallery.widget.OrientationProvider.1
        @Override // com.miui.gallery.widget.OrientationProvider
        public boolean isPortrait(Context context) {
            return true;
        }
    };
    public static final OrientationProvider LANDSCAPE = new OrientationProvider() { // from class: com.miui.gallery.widget.OrientationProvider.2
        @Override // com.miui.gallery.widget.OrientationProvider
        public boolean isPortrait(Context context) {
            return false;
        }
    };

    boolean isPortrait(Context context);

    static OrientationProvider createOrientationProvider(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                Constructor constructor = Class.forName(str.trim()).asSubclass(OrientationProvider.class).getConstructor(new Class[0]);
                constructor.setAccessible(true);
                return (OrientationProvider) constructor.newInstance(new Object[0]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
                return null;
            } catch (InstantiationException e3) {
                e3.printStackTrace();
                return null;
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
                return null;
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
                return null;
            }
        }
        return null;
    }
}
