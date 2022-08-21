package com.baidu.mapsdkplatform.comapi.util;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import java.io.File;

/* loaded from: classes.dex */
public final class f {
    private final boolean a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private final String f;

    public f(Context context) {
        String absolutePath;
        if (Build.VERSION.SDK_INT >= 29) {
            File externalFilesDir = context.getExternalFilesDir(null);
            absolutePath = ((externalFilesDir == null || !externalFilesDir.exists()) ? context.getFilesDir() : externalFilesDir).getPath();
        } else {
            absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        this.b = absolutePath;
        this.a = false;
        this.c = this.b + File.separator + "BaiduMapSDKNew";
        this.d = context.getCacheDir().getAbsolutePath();
        this.e = "";
        this.f = "";
    }

    public String a() {
        return this.b;
    }

    public String b() {
        return this.b + File.separator + "BaiduMapSDKNew";
    }

    public String c() {
        return this.d;
    }

    public String d() {
        return this.e;
    }

    public boolean equals(Object obj) {
        if (obj == null || !f.class.isInstance(obj)) {
            return false;
        }
        return this.b.equals(((f) obj).b);
    }
}
