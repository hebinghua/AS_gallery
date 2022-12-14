package com.baidu.platform.comapi.util.a;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import ch.qos.logback.core.net.SyslogConstants;
import com.baidu.vi.VIContext;

/* loaded from: classes.dex */
public class a {
    private int a = -1;
    private int b = -1;
    private float c = -1.0f;
    private int d = -1;
    private int e = -1;
    private int f = -1;
    private double g = -1.0d;

    public int a() {
        if (this.a == -1) {
            a(VIContext.getContext());
        }
        return this.a;
    }

    public void a(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        this.a = displayMetrics.widthPixels;
        this.b = displayMetrics.heightPixels;
        this.c = displayMetrics.density;
        this.d = (int) displayMetrics.xdpi;
        this.e = (int) displayMetrics.ydpi;
        if (Build.VERSION.SDK_INT > 3) {
            int i = displayMetrics.densityDpi;
            this.f = i;
            if (i < 240) {
                this.f = i;
            }
        } else {
            this.f = SyslogConstants.LOG_LOCAL4;
        }
        if (this.f == 0) {
            this.f = SyslogConstants.LOG_LOCAL4;
        }
        this.g = this.f / 240.0d;
    }

    public int b() {
        if (this.b == -1) {
            a(VIContext.getContext());
        }
        return this.b;
    }

    public float c() {
        if (this.c == -1.0f) {
            a(VIContext.getContext());
        }
        return this.c;
    }

    public int d() {
        if (this.f == -1) {
            a(VIContext.getContext());
        }
        return this.f;
    }
}
