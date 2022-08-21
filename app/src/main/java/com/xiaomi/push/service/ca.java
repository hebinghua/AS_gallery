package com.xiaomi.push.service;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.push.hn;
import com.xiaomi.push.it;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/* loaded from: classes3.dex */
public class ca {
    public static final Object a = new Object();

    /* renamed from: a  reason: collision with other field name */
    public static ArrayList<hn> f943a = new ArrayList<>();

    public static void a() {
        f943a.clear();
    }

    public static void a(Context context, hn hnVar) {
        if (!bz.a(hnVar.e())) {
            return;
        }
        com.xiaomi.push.al.a(context).a(new cb(context, hnVar));
    }

    public static void a(hn hnVar) {
        if (f943a.size() > 10) {
            f943a.remove(0);
        }
        f943a.add(hnVar);
    }

    public static byte[] a(Context context) {
        String a2 = com.xiaomi.push.r.a(context).a("mipush", "td_key", "");
        if (TextUtils.isEmpty(a2)) {
            a2 = com.xiaomi.push.bp.a(20);
            com.xiaomi.push.r.a(context).m2408a("mipush", "td_key", a2);
        }
        return a(a2);
    }

    public static byte[] a(String str) {
        byte[] copyOf = Arrays.copyOf(com.xiaomi.push.bm.m1977a(str), 16);
        copyOf[0] = 68;
        copyOf[15] = 84;
        return copyOf;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r0v19 */
    /* JADX WARN: Type inference failed for: r0v20 */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v7, types: [java.io.Closeable] */
    public static void c(Context context, hn hnVar) {
        BufferedOutputStream bufferedOutputStream;
        BufferedOutputStream bufferedOutputStream2;
        String str;
        String str2;
        BufferedOutputStream a2 = a(context);
        try {
            try {
                byte[] b = com.xiaomi.push.i.b(a2, it.a(hnVar));
                if (b != null && b.length >= 1) {
                    if (b.length > 10240) {
                        str2 = "TinyData write to cache file failed case too much data content item:" + hnVar.d() + "  ts:" + System.currentTimeMillis();
                        com.xiaomi.channel.commonutils.logger.b.m1859a(str2);
                        com.xiaomi.push.ab.a((Closeable) null);
                        com.xiaomi.push.ab.a((Closeable) null);
                    }
                    BufferedOutputStream bufferedOutputStream3 = new BufferedOutputStream(new FileOutputStream(new File(context.getFilesDir(), "tiny_data.data"), true));
                    try {
                        bufferedOutputStream3.write(com.xiaomi.push.af.a(b.length));
                        bufferedOutputStream3.write(b);
                        bufferedOutputStream3.flush();
                        com.xiaomi.push.ab.a((Closeable) null);
                        com.xiaomi.push.ab.a(bufferedOutputStream3);
                        return;
                    } catch (IOException e) {
                        bufferedOutputStream2 = bufferedOutputStream3;
                        e = e;
                        str = "TinyData write to cache file failed cause io exception item:" + hnVar.d();
                        a2 = bufferedOutputStream2;
                        com.xiaomi.channel.commonutils.logger.b.a(str, e);
                        com.xiaomi.push.ab.a((Closeable) null);
                        com.xiaomi.push.ab.a((Closeable) a2);
                        return;
                    } catch (Exception e2) {
                        bufferedOutputStream = bufferedOutputStream3;
                        e = e2;
                        str = "TinyData write to cache file  failed item:" + hnVar.d();
                        a2 = bufferedOutputStream;
                        com.xiaomi.channel.commonutils.logger.b.a(str, e);
                        com.xiaomi.push.ab.a((Closeable) null);
                        com.xiaomi.push.ab.a((Closeable) a2);
                        return;
                    } catch (Throwable th) {
                        a2 = bufferedOutputStream3;
                        th = th;
                        com.xiaomi.push.ab.a((Closeable) null);
                        com.xiaomi.push.ab.a(a2);
                        throw th;
                    }
                }
                str2 = "TinyData write to cache file failed case encryption fail item:" + hnVar.d() + "  ts:" + System.currentTimeMillis();
                com.xiaomi.channel.commonutils.logger.b.m1859a(str2);
                com.xiaomi.push.ab.a((Closeable) null);
                com.xiaomi.push.ab.a((Closeable) null);
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException e3) {
            e = e3;
            bufferedOutputStream2 = null;
        } catch (Exception e4) {
            e = e4;
            bufferedOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            a2 = null;
        }
    }
}
