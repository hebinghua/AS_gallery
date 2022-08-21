package kotlinx.coroutines.internal;

/* compiled from: SystemProps.kt */
/* loaded from: classes3.dex */
public final /* synthetic */ class SystemPropsKt__SystemPropsKt {
    public static final int AVAILABLE_PROCESSORS = Runtime.getRuntime().availableProcessors();

    public static final int getAVAILABLE_PROCESSORS() {
        return AVAILABLE_PROCESSORS;
    }

    public static final String systemProp(String str) {
        try {
            return System.getProperty(str);
        } catch (SecurityException unused) {
            return null;
        }
    }
}
