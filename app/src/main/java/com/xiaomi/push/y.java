package com.xiaomi.push;

import android.content.Context;
import java.io.File;
import java.io.IOException;

/* loaded from: classes3.dex */
public abstract class y implements Runnable {
    public Context a;

    /* renamed from: a  reason: collision with other field name */
    public File f997a;

    /* renamed from: a  reason: collision with other field name */
    public Runnable f998a;

    public y(Context context, File file) {
        this.a = context;
        this.f997a = file;
    }

    public /* synthetic */ y(Context context, File file, z zVar) {
        this(context, file);
    }

    public static void a(Context context, File file, Runnable runnable) {
        new z(context, file, runnable).run();
    }

    public abstract void a(Context context);

    @Override // java.lang.Runnable
    public final void run() {
        x xVar = null;
        try {
            try {
                if (this.f997a == null) {
                    this.f997a = new File(this.a.getFilesDir(), "default_locker");
                }
                xVar = x.a(this.a, this.f997a);
                Runnable runnable = this.f998a;
                if (runnable != null) {
                    runnable.run();
                }
                a(this.a);
                if (xVar == null) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (xVar == null) {
                    return;
                }
            }
            xVar.a();
        } catch (Throwable th) {
            if (xVar != null) {
                xVar.a();
            }
            throw th;
        }
    }
}
