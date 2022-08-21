package miuix.core.util;

import android.content.res.AssetManager;
import android.content.res.Resources;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* loaded from: classes3.dex */
public class ResourcesUtils {
    public static Method ASSET_MANAGER_ADD_ASSET_PATH;
    public static Constructor<AssetManager> ASSET_MANAGER_CONSTRUCTOR;

    static {
        try {
            ASSET_MANAGER_ADD_ASSET_PATH = AssetManager.class.getMethod("addAssetPath", String.class);
            ASSET_MANAGER_CONSTRUCTOR = AssetManager.class.getConstructor(new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Resources createResources(String... strArr) {
        return createResources(null, strArr);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0038  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.content.res.Resources createResources(android.content.res.Resources r8, java.lang.String... r9) {
        /*
            r0 = 0
            java.lang.reflect.Constructor<android.content.res.AssetManager> r1 = miuix.core.util.ResourcesUtils.ASSET_MANAGER_CONSTRUCTOR     // Catch: java.lang.reflect.InvocationTargetException -> L25 java.lang.IllegalAccessException -> L2b java.lang.InstantiationException -> L31
            r2 = 0
            java.lang.Object[] r3 = new java.lang.Object[r2]     // Catch: java.lang.reflect.InvocationTargetException -> L25 java.lang.IllegalAccessException -> L2b java.lang.InstantiationException -> L31
            java.lang.Object r1 = r1.newInstance(r3)     // Catch: java.lang.reflect.InvocationTargetException -> L25 java.lang.IllegalAccessException -> L2b java.lang.InstantiationException -> L31
            android.content.res.AssetManager r1 = (android.content.res.AssetManager) r1     // Catch: java.lang.reflect.InvocationTargetException -> L25 java.lang.IllegalAccessException -> L2b java.lang.InstantiationException -> L31
            int r3 = r9.length     // Catch: java.lang.reflect.InvocationTargetException -> L1f java.lang.IllegalAccessException -> L21 java.lang.InstantiationException -> L23
            r4 = r2
        Le:
            if (r4 >= r3) goto L36
            r5 = r9[r4]     // Catch: java.lang.reflect.InvocationTargetException -> L1f java.lang.IllegalAccessException -> L21 java.lang.InstantiationException -> L23
            java.lang.reflect.Method r6 = miuix.core.util.ResourcesUtils.ASSET_MANAGER_ADD_ASSET_PATH     // Catch: java.lang.reflect.InvocationTargetException -> L1f java.lang.IllegalAccessException -> L21 java.lang.InstantiationException -> L23
            r7 = 1
            java.lang.Object[] r7 = new java.lang.Object[r7]     // Catch: java.lang.reflect.InvocationTargetException -> L1f java.lang.IllegalAccessException -> L21 java.lang.InstantiationException -> L23
            r7[r2] = r5     // Catch: java.lang.reflect.InvocationTargetException -> L1f java.lang.IllegalAccessException -> L21 java.lang.InstantiationException -> L23
            r6.invoke(r1, r7)     // Catch: java.lang.reflect.InvocationTargetException -> L1f java.lang.IllegalAccessException -> L21 java.lang.InstantiationException -> L23
            int r4 = r4 + 1
            goto Le
        L1f:
            r9 = move-exception
            goto L27
        L21:
            r9 = move-exception
            goto L2d
        L23:
            r9 = move-exception
            goto L33
        L25:
            r9 = move-exception
            r1 = r0
        L27:
            r9.printStackTrace()
            goto L36
        L2b:
            r9 = move-exception
            r1 = r0
        L2d:
            r9.printStackTrace()
            goto L36
        L31:
            r9 = move-exception
            r1 = r0
        L33:
            r9.printStackTrace()
        L36:
            if (r8 != 0) goto L3e
            android.content.res.Resources r8 = new android.content.res.Resources
            r8.<init>(r1, r0, r0)
            goto L4c
        L3e:
            android.content.res.Resources r9 = new android.content.res.Resources
            android.util.DisplayMetrics r0 = r8.getDisplayMetrics()
            android.content.res.Configuration r8 = r8.getConfiguration()
            r9.<init>(r1, r0, r8)
            r8 = r9
        L4c:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: miuix.core.util.ResourcesUtils.createResources(android.content.res.Resources, java.lang.String[]):android.content.res.Resources");
    }
}
