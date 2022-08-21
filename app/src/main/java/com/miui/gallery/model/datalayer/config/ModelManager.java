package com.miui.gallery.model.datalayer.config;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes2.dex */
public class ModelManager {
    public final Map<String, Object> mRepositorys;

    /* loaded from: classes2.dex */
    public interface ModelInstanceCallback<M> {
        /* renamed from: newModel */
        M mo1602newModel(Class cls);
    }

    public ModelManager() {
        this.mRepositorys = new HashMap(4);
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final ModelManager INSTANCE = new ModelManager();
    }

    public static ModelManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <M> M getModel(Class<M> cls) {
        return (M) getModel(cls, null);
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x001e, code lost:
        if (r2.mRepositorys.get(r3) == null) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public <M> M getModel(java.lang.Class<M> r3, com.miui.gallery.model.datalayer.config.ModelManager.ModelInstanceCallback r4) {
        /*
            r2 = this;
            if (r3 == 0) goto L50
            java.lang.String r3 = r3.getName()
            java.util.Map<java.lang.String, java.lang.Object> r0 = r2.mRepositorys
            java.lang.Object r0 = r0.get(r3)
            if (r4 == 0) goto L10
            r1 = 1
            goto L11
        L10:
            r1 = 0
        L11:
            if (r1 != 0) goto L15
            if (r0 != 0) goto L4d
        L15:
            monitor-enter(r2)
            if (r1 != 0) goto L20
            java.util.Map<java.lang.String, java.lang.Object> r1 = r2.mRepositorys     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            java.lang.Object r1 = r1.get(r3)     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            if (r1 != 0) goto L4c
        L20:
            java.util.HashMap r1 = com.miui.gallery.model.datalayer.config.ModelConfig.getModelConfigs()     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            java.lang.Object r1 = r1.get(r3)     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            java.lang.Class r1 = (java.lang.Class) r1     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            if (r1 != 0) goto L2f
            r3 = 0
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L40
            return r3
        L2f:
            if (r4 == 0) goto L36
            java.lang.Object r0 = r4.mo1602newModel(r1)     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            goto L4c
        L36:
            java.lang.Object r0 = r1.newInstance()     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            java.util.Map<java.lang.String, java.lang.Object> r4 = r2.mRepositorys     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            r4.put(r3, r0)     // Catch: java.lang.Throwable -> L40 java.lang.Exception -> L42
            goto L4c
        L40:
            r3 = move-exception
            goto L4e
        L42:
            r3 = move-exception
            java.lang.String r4 = "ModelManager"
            java.lang.String r3 = com.miui.gallery.base_optimization.util.ExceptionUtils.getStackTraceString(r3)     // Catch: java.lang.Throwable -> L40
            com.miui.gallery.util.logger.DefaultLogger.e(r4, r3)     // Catch: java.lang.Throwable -> L40
        L4c:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L40
        L4d:
            return r0
        L4e:
            monitor-exit(r2)     // Catch: java.lang.Throwable -> L40
            throw r3
        L50:
            java.lang.IllegalArgumentException r3 = new java.lang.IllegalArgumentException
            java.lang.String r4 = "classzz can't null"
            r3.<init>(r4)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.model.datalayer.config.ModelManager.getModel(java.lang.Class, com.miui.gallery.model.datalayer.config.ModelManager$ModelInstanceCallback):java.lang.Object");
    }
}
