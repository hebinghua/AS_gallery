package com.baidu.platform.comapi.util.a;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import com.baidu.vi.VIContext;
import java.io.File;

/* loaded from: classes.dex */
public class b {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;

    public String a() {
        if (TextUtils.isEmpty(this.a)) {
            a(VIContext.getContext());
        }
        return this.a;
    }

    public void a(Context context) {
        this.a = context.getFilesDir().getAbsolutePath();
        String absolutePath = context.getCacheDir().getAbsolutePath();
        this.d = absolutePath;
        this.e = absolutePath;
        this.b = Environment.getExternalStorageDirectory().getPath();
        this.c = Environment.getExternalStorageDirectory().getPath();
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            this.f = externalFilesDir.getAbsolutePath();
        }
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null) {
            this.g = externalCacheDir.getAbsolutePath();
        }
    }

    public String b() {
        if (TextUtils.isEmpty(this.b)) {
            a(VIContext.getContext());
        }
        return this.b;
    }

    public String c() {
        if (TextUtils.isEmpty(this.c)) {
            a(VIContext.getContext());
        }
        return this.c;
    }

    public String d() {
        if (TextUtils.isEmpty(this.d)) {
            a(VIContext.getContext());
        }
        return this.d;
    }

    public String e() {
        if (TextUtils.isEmpty(this.f)) {
            a(VIContext.getContext());
        }
        return this.f;
    }
}
