package miuix.internal.hybrid.provider;

import android.content.Context;
import android.util.Log;
import miuix.internal.hybrid.webkit.WebkitFactoryProvider;

/* loaded from: classes3.dex */
public class WebViewFactory {
    public static WebViewFactoryProvider sProviderInstance;
    public static final Object sProviderLock = new Object();

    public static WebViewFactoryProvider getProvider(Context context) {
        synchronized (sProviderLock) {
            WebViewFactoryProvider webViewFactoryProvider = sProviderInstance;
            if (webViewFactoryProvider != null) {
                return webViewFactoryProvider;
            }
            sProviderInstance = new WebkitFactoryProvider();
            if (Log.isLoggable("hybrid", 3)) {
                Log.d("hybrid", "loaded provider:" + sProviderInstance);
            }
            return sProviderInstance;
        }
    }
}
