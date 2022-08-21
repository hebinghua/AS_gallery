package miuix.internal.hybrid;

import android.util.Log;
import android.webkit.JavascriptInterface;

/* loaded from: classes3.dex */
public class JsInterface {
    public HybridManager mManager;

    public JsInterface(HybridManager hybridManager) {
        this.mManager = hybridManager;
    }

    @JavascriptInterface
    public String config(String str) {
        String config = this.mManager.config(str);
        if (Log.isLoggable("hybrid", 3)) {
            Log.d("hybrid", "config response is " + config);
        }
        return config;
    }

    @JavascriptInterface
    public String lookup(String str, String str2) {
        String lookup = this.mManager.lookup(str, str2);
        if (Log.isLoggable("hybrid", 3)) {
            Log.d("hybrid", "lookup response is " + lookup);
        }
        return lookup;
    }

    @JavascriptInterface
    public String invoke(String str, String str2, String str3, String str4) {
        String invoke = this.mManager.invoke(str, str2, str3, str4);
        if (Log.isLoggable("hybrid", 3)) {
            Log.d("hybrid", "blocking response is " + invoke);
        }
        return invoke;
    }
}
