package xcrash;

import android.util.Log;

/* loaded from: classes3.dex */
public class DefaultLogger implements ILogger {
    @Override // xcrash.ILogger
    public void d(String str, String str2) {
        Log.d(str, str2);
    }

    @Override // xcrash.ILogger
    public void i(String str, String str2, Throwable th) {
        Log.i(str, str2, th);
    }

    @Override // xcrash.ILogger
    public void w(String str, String str2, Throwable th) {
        Log.w(str, str2, th);
    }

    @Override // xcrash.ILogger
    public void e(String str, String str2) {
        Log.e(str, str2);
    }

    @Override // xcrash.ILogger
    public void e(String str, String str2, Throwable th) {
        Log.e(str, str2, th);
    }
}
