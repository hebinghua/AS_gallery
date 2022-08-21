package com.github.chrisbanes.photoview.log;

import android.util.Log;
import com.miui.os.Rom;

/* loaded from: classes.dex */
public class LoggerDefault implements Logger {
    public static final boolean sLogable = !Rom.IS_STABLE;

    @Override // com.github.chrisbanes.photoview.log.Logger
    public void d(String str, String str2) {
        if (sLogable) {
            Log.d(str, str2);
        }
    }

    @Override // com.github.chrisbanes.photoview.log.Logger
    public void i(String str, String str2) {
        if (sLogable) {
            Log.i(str, str2);
        }
    }

    @Override // com.github.chrisbanes.photoview.log.Logger
    public void w(String str, String str2) {
        if (sLogable) {
            Log.w(str, str2);
        }
    }

    @Override // com.github.chrisbanes.photoview.log.Logger
    public void e(String str, String str2) {
        if (sLogable) {
            Log.e(str, str2);
        }
    }
}
